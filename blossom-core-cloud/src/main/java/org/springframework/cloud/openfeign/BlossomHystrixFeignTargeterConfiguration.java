package org.springframework.cloud.openfeign;

import feign.hystrix.HystrixFeign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @author guolf
 */
@Configuration
@ConditionalOnClass(HystrixFeign.class)
@ConditionalOnProperty(value = "feign.hystrix.enabled", matchIfMissing = true)
public class BlossomHystrixFeignTargeterConfiguration {

	@Bean
	@Primary
	public Targeter blossomHystrixTargeter() {
		return new BlossomHystrixTargeter();
	}
}
