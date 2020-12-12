package org.springblossom.core.file.config;

import lombok.AllArgsConstructor;
import org.springblossom.core.file.rule.BlossomOssRule;
import org.springblossom.core.file.rule.UploadRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传配置
 *
 * @author guolf
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(UploadProperties.class)
public class UploadConfiguration {

	@Bean
	@ConditionalOnMissingBean(UploadRule.class)
	public UploadRule ossRule() {
		return new BlossomOssRule();
	}
}
