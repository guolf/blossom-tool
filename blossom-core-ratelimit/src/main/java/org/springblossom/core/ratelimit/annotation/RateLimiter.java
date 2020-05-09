package org.springblossom.core.ratelimit.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解，添加了 {@link AliasFor} 必须通过 {@link org.springframework.core.annotation.AnnotationUtils} 获取，才会生效
 *
 * @author guolf
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
	long DEFAULT_REQUEST = 10;

	/**
	 * max 最大请求数
	 */
	@AliasFor("max") long value() default DEFAULT_REQUEST;

	/**
	 * max 最大请求数
	 */
	@AliasFor("value") long max() default DEFAULT_REQUEST;

	/**
	 * 限流key
	 */
	String key() default "";

	/**
	 * key 生成器
	 * @return
	 */
	String keyGenerator() default "";

	/**
	 * 超时时长，默认1分钟
	 */
	long timeout() default 1;

	/**
	 * 超时时间单位，默认 分钟
	 */
	TimeUnit timeUnit() default TimeUnit.MINUTES;

	/**
	 * 错误消息
	 */
	String msg() default "手速太快了，慢点儿吧~";
}
