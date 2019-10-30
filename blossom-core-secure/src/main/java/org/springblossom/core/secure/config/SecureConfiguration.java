package org.springblossom.core.secure.config;

import lombok.AllArgsConstructor;
import org.springblossom.core.secure.interceptor.ClientInterceptor;
import org.springblossom.core.secure.props.BlossomClientProperties;
import org.springblossom.core.secure.props.BlossomSecureProperties;
import org.springblossom.core.secure.provider.ClientDetailsServiceImpl;
import org.springblossom.core.secure.provider.IClientDetailsService;
import org.springblossom.core.secure.twofactor.TwoFactorHandlerInterceptorAdapter;
import org.springblossom.core.secure.twofactor.TwoFactorValidatorManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 安全配置类
 *
 * @author guolf
 */
@Order
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({BlossomSecureProperties.class, BlossomClientProperties.class})
public class SecureConfiguration implements WebMvcConfigurer {

	private final BlossomClientProperties clientProperties;

	private final JdbcTemplate jdbcTemplate;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		clientProperties.getClient().forEach(cs -> registry.addInterceptor(new ClientInterceptor(cs.getClientId())).addPathPatterns(cs.getPathPatterns()));
	}

	@Bean
	@ConditionalOnMissingBean(IClientDetailsService.class)
	public IClientDetailsService clientDetailsService() {
		return new ClientDetailsServiceImpl(jdbcTemplate);
	}

	@Bean
	@ConditionalOnProperty(prefix = "blossom.secure.two-factor", name = "enable", havingValue = "true")
	@Order(100)
	public WebMvcConfigurer twoFactorHandlerConfigurer(TwoFactorValidatorManager manager) {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(new TwoFactorHandlerInterceptorAdapter(manager));
			}
		};
	}
}
