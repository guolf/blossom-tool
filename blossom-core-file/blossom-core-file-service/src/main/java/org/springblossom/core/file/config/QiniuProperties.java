package org.springblossom.core.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 七牛对象存储配置
 *
 * @author guolf
 */
@Data
@ConfigurationProperties(prefix = "blossom.upload.qiniu")
public class QiniuProperties {

	/**
	 * 是否启用七牛云存储
	 */
	private Boolean enable = false;

	/**
	 * 对象存储名称
	 */
	private String name;

	/**
	 * 是否公开,私有需要token访问
	 */
	private Boolean publicSpace;

	/**
	 * 对象存储服务的URL
	 */
	private String endpoint;

	/**
	 * Access key就像用户ID，可以唯一标识你的账户
	 */
	private String accessKey;

	/**
	 * Secret key是你账户的密码
	 */
	private String secretKey;

	/**
	 * 默认的存储桶名称
	 */
	private String bucketName = "blossom";
}
