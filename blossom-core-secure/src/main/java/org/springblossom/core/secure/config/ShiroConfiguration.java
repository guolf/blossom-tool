package org.springblossom.core.secure.config;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springblossom.core.secure.cache.ShiroSpringCacheManager;
import org.springblossom.core.secure.props.BlossomClientProperties;
import org.springblossom.core.secure.props.BlossomSecureProperties;
import org.springblossom.core.secure.provider.ClientDetailsServiceImpl;
import org.springblossom.core.secure.provider.IClientDetailsService;
import org.springblossom.core.secure.token.TokenFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;


/**
 * Shiro 配置
 *
 * @author guolf
 */
@Order(1000)
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({BlossomSecureProperties.class, BlossomClientProperties.class, ShiroSecureProperties.class})
public class ShiroConfiguration implements WebMvcConfigurer {

	private BlossomSecureProperties secureProperties;
	private JdbcTemplate jdbcTemplate;
	private RedisTemplate<String, Object> redisTemplate;
	private ShiroSpringCacheManager shiroCacheManager;

	private ShiroSecureProperties shiroSecureProperties;

	private AuthorizingRealm realm;

	@Bean("securityManager")
	public DefaultWebSecurityManager getManager() {
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

		realm.setCachingEnabled(true);
		realm.setCacheManager(shiroCacheManager);
		realm.setAuthenticationCachingEnabled(false);
		realm.setAuthorizationCachingEnabled(false);
		realm.setAuthenticationCacheName("authenticationCache");
		realm.setAuthorizationCacheName("authorizationCache");

		manager.setRealm(realm);
		manager.setCacheManager(shiroCacheManager);

		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(true);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		manager.setSubjectDAO(subjectDAO);

		manager.setSessionManager(sessionManager());
		manager.setRememberMeManager(rememberMeManager());

		return manager;
	}

	@Bean
	public SessionManager sessionManager() {
		SessionManager manager = new SessionManager();
		manager.setSessionDAO(new RedisSessionDAO(redisTemplate, shiroSecureProperties));

		Cookie cookie = new SimpleCookie(shiroSecureProperties.getSessionIdCookie());
		cookie.setHttpOnly(true);
		// secure设置为true,只能在https下使用
		// cookie.setSecure(true);

		manager.setSessionIdCookie(cookie);
		manager.setSessionIdUrlRewritingEnabled(false);
		return manager;
	}

	@Bean
	public RememberMeManager rememberMeManager() {
		CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
		rememberMeManager.setCookie(simpleCookie());
		rememberMeManager.setCipherKey(Base64.decode(shiroSecureProperties.getCipherKey()));
		return rememberMeManager;
	}

	public SimpleCookie simpleCookie() {
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		// 30天
		cookie.setMaxAge(2592000);
		return cookie;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

		Map<String, Filter> filterMap = Maps.newHashMap();
		filterMap.put("jwt", new TokenFilter());
		factoryBean.setFilters(filterMap);

		factoryBean.setSecurityManager(securityManager);
		factoryBean.setUnauthorizedUrl("/401");

		Map<String, String> filterRuleMap = Maps.newLinkedHashMap();

		if (!secureProperties.getFilterRuleMap().isEmpty()) {
			filterRuleMap.putAll(secureProperties.getFilterRuleMap());
		}

		for (String url : secureProperties.getExcludePatterns()) {
			filterRuleMap.put(url, "anon");
		}
		filterRuleMap.put("/**", "jwt");
		log.info("ruleMap = {} ", filterRuleMap);
		factoryBean.setFilterChainDefinitionMap(filterRuleMap);
		return factoryBean;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

	@Bean
	@ConditionalOnMissingBean(IClientDetailsService.class)
	public IClientDetailsService clientDetailsService() {
		return new ClientDetailsServiceImpl(jdbcTemplate);
	}

}
