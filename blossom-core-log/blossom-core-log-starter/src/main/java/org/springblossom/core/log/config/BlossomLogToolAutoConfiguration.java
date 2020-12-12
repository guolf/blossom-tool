package org.springblossom.core.log.config;

import lombok.AllArgsConstructor;
import org.springblossom.core.launch.props.BlossomProperties;
import org.springblossom.core.launch.server.ServerInfo;
import org.springblossom.core.log.event.ApiLogListener;
import org.springblossom.core.log.event.ErrorLogListener;
import org.springblossom.core.log.event.UsualLogListener;
import org.springblossom.core.log.feign.ILogCoreService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志工具自动配置
 *
 * @author guolf
 */
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class BlossomLogToolAutoConfiguration {

	private final ILogCoreService logService;
	private final ServerInfo serverInfo;
	private final BlossomProperties blossomProperties;

	@Bean
	@ConditionalOnMissingBean(name = "apiLogListener")
	public ApiLogListener apiLogListener() {
		return new ApiLogListener(logService, serverInfo, blossomProperties);
	}

	@Bean
	@ConditionalOnMissingBean(name = "errorLogListener")
	public ErrorLogListener errorLogListener() {
		return new ErrorLogListener(logService, serverInfo, blossomProperties);
	}

	@Bean
	@ConditionalOnMissingBean(name = "usualLogListener")
	public UsualLogListener usualLogListener() {
		return new UsualLogListener(logService, serverInfo, blossomProperties);
	}

}
