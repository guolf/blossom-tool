package org.springblossom.core.ratelimit.aspect;

import java.lang.reflect.Method;

/**
 * 限流key 默认生成
 * @author guolf
 */
public class SimpleKeyGenerator implements KeyGenerator {

	@Override
	public String generate(Object target, Method method, Object... params) {
		return null;
	}
}
