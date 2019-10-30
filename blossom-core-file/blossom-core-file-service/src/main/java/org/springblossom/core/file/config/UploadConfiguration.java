package org.springblossom.core.file.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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

}
