package org.springblossom.core.secure.twofactor;


/**
 * 双重验证管理器
 * @author guolf
 */
public interface TwoFactorValidatorManager {

	/**
	 * 获取用户使用的双重验证器
	 * @param userId 用户账号
	 * @param operation
	 * @param provider 验证器供应商
	 * @return
	 */
	TwoFactorValidator getValidator(String userId,String operation, String provider);

}
