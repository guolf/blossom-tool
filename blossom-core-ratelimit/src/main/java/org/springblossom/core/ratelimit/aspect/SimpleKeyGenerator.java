package org.springblossom.core.ratelimit.aspect;

import java.lang.reflect.Method;

/**
 * 限流key 默认生成
 */
public class SimpleKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		return null;
	}
}
