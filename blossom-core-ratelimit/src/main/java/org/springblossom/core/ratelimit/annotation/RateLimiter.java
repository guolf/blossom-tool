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
	 * Spring Expression Language (SpEL) expression for computing the key dynamically.
	 * <p>Default is {@code ""}, meaning all method parameters are considered as a key,
	 * unless a custom {@link #keyGenerator} has been configured.
	 * <p>The SpEL expression evaluates against a dedicated context that provides the
	 * following meta-data:
	 * <ul>
	 * <li>{@code #root.method}, {@code #root.target}, and {@code #root.caches} for
	 * references to the {@link java.lang.reflect.Method method}, target object, and
	 * affected cache(s) respectively.</li>
	 * <li>Shortcuts for the method name ({@code #root.methodName}) and target class
	 * ({@code #root.targetClass}) are also available.
	 * <li>Method arguments can be accessed by index. For instance the second argument
	 * can be accessed via {@code #root.args[1]}, {@code #p1} or {@code #a1}. Arguments
	 * can also be accessed by name if that information is available.</li>
	 * </ul>
	 */
	String key() default "";

	/**
	 * 实现 {@link org.springblossom.core.ratelimit.aspect.KeyGenerator} 接口
	 * 与 {@link #key()} 属性互斥
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
