package org.springblossom.core.push.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 友盟推送配置
 *
 * @author guolf
 */
@Data
@ConfigurationProperties(prefix = "blossom.push.umeng")
public class UmengPushProperties {

	/**
	 * App Key
	 */
	private String appKey;

	/**
	 * App Master Secret
	 */
	private String appMasterSecret;

	/**
	 * dev/prod
	 */
	private String profile = "dev";
}
