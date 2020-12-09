package org.springblossom.core.file.config;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import org.springblossom.core.file.qiniu.QiniuTemplate;
import org.springblossom.core.file.rule.BlossomOssRule;
import org.springblossom.core.file.rule.UploadRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云存储配置
 *
 * @author guolf
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(QiniuProperties.class)
@ConditionalOnProperty(prefix = "blossom.upload.qiniu", name = "enable", havingValue = "true")
public class QiniuConfiguration {

	private QiniuProperties qiniuProperties;

	@Bean
	@ConditionalOnMissingBean(com.qiniu.storage.Configuration.class)
	public com.qiniu.storage.Configuration qiniuConfiguration() {
		return new com.qiniu.storage.Configuration(Zone.zone0());
	}

	@Bean
	public Auth auth() {
		return Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
	}

	@Bean
	@ConditionalOnBean(com.qiniu.storage.Configuration.class)
	public UploadManager uploadManager(com.qiniu.storage.Configuration cfg) {
		return new UploadManager(cfg);
	}

	@Bean
	@ConditionalOnBean(com.qiniu.storage.Configuration.class)
	public BucketManager bucketManager(com.qiniu.storage.Configuration cfg) {
		return new BucketManager(auth(), cfg);
	}

	@Bean
	@ConditionalOnMissingBean(QiniuTemplate.class)
	@ConditionalOnBean({Auth.class, UploadManager.class, BucketManager.class, UploadRule.class})
	public QiniuTemplate qiniuTemplate(Auth auth, UploadManager uploadManager, BucketManager bucketManager, UploadRule ossRule) {
		return new QiniuTemplate(auth, uploadManager, bucketManager, qiniuProperties, ossRule);
	}
}
