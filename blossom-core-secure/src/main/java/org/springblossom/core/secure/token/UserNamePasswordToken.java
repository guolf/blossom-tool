package org.springblossom.core.secure.token;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 用户名密码登录Token
 *
 * @author guolf
 */
@Data
public class UserNamePasswordToken implements AuthenticationToken {

	/**
	 * 登录账号
	 */
	private String loginName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 客户端ID
	 */
	private String clientId;

	@Override
	public Object getPrincipal() {
		return loginName;
	}

	@Override
	public Object getCredentials() {
		return password;
	}
}
