package org.springblossom.core.cloud.feign;

import feign.Target;
import feign.hystrix.FallbackFactory;
import lombok.AllArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;

/**
 * 默认 Fallback，避免写过多fallback类
 * @author guolf
 * @param <T>
 */
@AllArgsConstructor
public class BlossomFallbackFactory<T> implements FallbackFactory<T> {
	private final Target<T> target;

	@Override
	public T create(Throwable cause) {
		final Class<T> targetType = target.type();
		final String targetName = target.name();
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(targetType);
		enhancer.setUseCache(true);
		enhancer.setCallback(new BlossomFeignFallback<>(targetType, targetName, cause));
		return (T) enhancer.create();
	}
}
