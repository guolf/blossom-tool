
package org.springblossom.core.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.boot.resolver.TokenArgumentResolver;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * WEB配置
 * @author Chill
 */
@Slf4j
@Configuration
@EnableCaching
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BlossomWebMvcConfiguration implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new TokenArgumentResolver());
	}

}
