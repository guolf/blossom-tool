package org.springblossom.core.pay.wxpay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置
 *
 * @author guolf
 */
@Data
@Component
@ConfigurationProperties(prefix = "blossom.pay.wx")
public class WxPayConfig {

	/**
	 * 应用ID
	 */
	private String appId;

	/**
	 * API密钥
	 */
	private String appSecret;

	/**
	 * 商户号
	 */
	private String mchId;

	/**
	 * 证书路径
	 */
	private String certPath;

	/**
	 * 应用地址，用于回调
	 */
	private String domain;

	/**
	 * 是否测试环境
	 */
	private Boolean isSandbox;

	/**
	 * 商户平台设置的密钥key,用于加密参数
	 */
	private String partnerKey;
}
