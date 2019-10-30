package org.springblossom.core.secure.config;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

@Data
public class StatelessToken implements AuthenticationToken {

	private String token;

	public StatelessToken(String token) {
		this.token = token;
	}


	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
}
