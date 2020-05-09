package org.springblossom.core.ratelimit.aspect;

import org.springblossom.core.tool.utils.StringPool;

import java.lang.reflect.Method;

/**
 * @author guolf
 */
public interface KeyGenerator {

	/**
	 * Generate a key for the given method and its parameters.
	 *
	 * @param target the target instance
	 * @param method the method being called
	 * @param params the method parameters (with any var-args expanded)
	 * @return a generated key
	 */
	default Object generate(Object target, Method method, Object... params) {
		String key = method.getDeclaringClass().getName() + StringPool.DOT + method.getName();
		return key;
	}
}
