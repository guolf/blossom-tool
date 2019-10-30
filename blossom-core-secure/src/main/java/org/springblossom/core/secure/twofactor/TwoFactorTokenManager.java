package org.springblossom.core.secure.twofactor;

/**
 * 两步认证 Token 管理
 *
 * @author guolf
 */
public interface TwoFactorTokenManager {

	/**
	 * 获取Token
	 * @param userId
	 * @param operation
	 * @return
	 */
	TwoFactorToken getToken(String userId, String operation);
}
