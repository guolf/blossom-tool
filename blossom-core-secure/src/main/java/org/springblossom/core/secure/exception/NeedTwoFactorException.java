package org.springblossom.core.secure.exception;

import lombok.Getter;

/**
 * 需要二次认证 异常
 *
 * @author guolf
 */
@Getter
public class NeedTwoFactorException extends RuntimeException {
	private static final long serialVersionUID = 3655980280834947633L;
	private String provider;

	public NeedTwoFactorException(String message, String provider) {
		super(message);
		this.provider = provider;
	}

}
