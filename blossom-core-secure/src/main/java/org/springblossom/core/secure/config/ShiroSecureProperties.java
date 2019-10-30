package org.springblossom.core.secure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Shiro 属性配置
 *
 * @author guolf
 */
@Data
@ConfigurationProperties(prefix = "blossom.shiro")
public class ShiroSecureProperties {

	/**
	 * session有效时间，单位分钟
	 */
	private Long sessionTimeout = 15L;

	/**
	 * session 前缀
	 */
	private String sessionKeyPrefix = "shiro-secure::";

	/**
	 * cookie id
	 */
	private String sessionIdCookie = "x-blossom-token";

	private String cipherKey = "";

	/**
	 * 一天内密码连续错误几次显示验证码（0关闭验证码）
	 */
	private Long verificationCodeTimes = 0L;

	/**
	 * 一天内密码连续错误几次锁定账号
	 */
	private Long lockUserTimes = 10L;

	/**
	 * 锁定时长，单位分钟（默认一小时）
	 */
	private Long lockUserMinute = 60L;
}
