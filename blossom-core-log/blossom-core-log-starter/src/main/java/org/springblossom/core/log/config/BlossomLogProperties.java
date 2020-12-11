package org.springblossom.core.log.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * blossom log 配置
 *
 * @author guolf
 */
@Data
@ConfigurationProperties(prefix = "blossom.log")
public class BlossomLogProperties {

	/**
	 * 日志持久化方式：mysql,file
	 */
	private String type = "mysql";
}
