package org.springblossom.core.oss.config;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import org.springblossom.core.oss.QiniuTemplate;
import org.springblossom.core.oss.rule.BlossomOssRule;
import org.springblossom.core.oss.rule.OssRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * oss 存储配置
 *
 * @author guolf
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.enable", havingValue = "true")
public class OssConfiguration {

	private OssProperties ossProperties;

	@Bean
	@ConditionalOnMissingBean(OssRule.class)
	public OssRule ossRule() {
		return new BlossomOssRule();
	}

	@Bean
	@ConditionalOnMissingBean(com.qiniu.storage.Configuration.class)
	public com.qiniu.storage.Configuration qiniuConfiguration() {
		return new com.qiniu.storage.Configuration(Zone.zone0());
	}

	@Bean
	public Auth auth() {
		return Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
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
	@ConditionalOnBean({Auth.class, UploadManager.class, BucketManager.class, OssRule.class})
	public QiniuTemplate qiniuTemplate(Auth auth, UploadManager uploadManager, BucketManager bucketManager, OssRule ossRule) {
		return new QiniuTemplate(auth, uploadManager, bucketManager, ossProperties, ossRule);
	}
}
