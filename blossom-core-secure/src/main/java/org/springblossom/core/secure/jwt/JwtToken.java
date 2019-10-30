package org.springblossom.core.secure.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 *
 * @author guolf
 */
public class JwtToken implements AuthenticationToken {

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	public JwtToken(String token) {
		this.token = token;
	}
}
