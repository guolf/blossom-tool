package org.springblossom.core.ratelimit.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springblossom.core.ratelimit.annotation.RateLimiter;
import org.springblossom.core.tool.el.AspectSupportUtils;
import org.springblossom.core.tool.utils.Func;
import org.springblossom.core.tool.utils.SpringUtil;
import org.springblossom.core.tool.utils.StringPool;
import org.springblossom.core.tool.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 限流切面
 *
 * @author guolf
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RateLimiterAspect {
	private final static String SEPARATOR = ":";
	private final static String REDIS_LIMIT_KEY_PREFIX = "limit:";
	private final StringRedisTemplate stringRedisTemplate;
	private final RedisScript<Long> limitRedisScript;

	@Pointcut("@annotation(org.springblossom.core.ratelimit.annotation.RateLimiter)")
	public void rateLimit() {

	}

	@Around("rateLimit()")
	public Object pointcut(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		// 通过 AnnotationUtils.findAnnotation 获取 RateLimiter 注解
		RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
		if (rateLimiter != null) {
			String key = "";
			// 如果key生成器非空，优先使用KeyGenerator
			if (Func.isNotBlank(rateLimiter.keyGenerator())) {
				KeyGenerator generator = SpringUtil.getBean(rateLimiter.keyGenerator(), KeyGenerator.class);
				key = generator.generate(null, method, point.getArgs());
			} else if (Func.isNotBlank(rateLimiter.key())) {
				key = AspectSupportUtils.getKeyValue(point, rateLimiter.key());
			} else {
				key = method.getDeclaringClass().getName() + StringPool.DOT + method.getName();
				// 默认 key 为 方法名 + IP地址
				key = key + SEPARATOR + WebUtil.getIP();
			}

			long max = rateLimiter.max();
			long timeout = rateLimiter.timeout();
			TimeUnit timeUnit = rateLimiter.timeUnit();
			boolean limited = shouldLimited(key, max, timeout, timeUnit);
			if (limited) {
				throw new RuntimeException(rateLimiter.msg());
			}
		}

		return point.proceed();
	}

	/**
	 * @param key
	 * @param max      最大访问量
	 * @param timeout
	 * @param timeUnit
	 * @return
	 */
	private boolean shouldLimited(String key, long max, long timeout, TimeUnit timeUnit) {
		// 最终的 key 格式为：
		// limit:自定义key:IP
		// limit:类名.方法名:IP
		key = REDIS_LIMIT_KEY_PREFIX + key;
		// 统一使用单位毫秒
		long ttl = timeUnit.toMillis(timeout);
		// 当前时间毫秒数
		long now = Instant.now().toEpochMilli();
		long expired = now - ttl;
		// 注意这里必须转为 String,否则会报错 java.lang.Long cannot be cast to java.lang.String
		Long executeTimes = stringRedisTemplate.execute(limitRedisScript, Collections.singletonList(key), now + "", ttl + "", expired + "", max + "");
		if (executeTimes != null) {
			if (executeTimes == 0) {
				log.error("【{}】在单位时间 {} 毫秒内已达到访问上限，当前接口上限 {}", key, ttl, max);
				return true;
			} else {
				log.info("【{}】在单位时间 {} 毫秒内访问 {} 次", key, ttl, executeTimes);
				return false;
			}
		}
		return false;
	}
}
