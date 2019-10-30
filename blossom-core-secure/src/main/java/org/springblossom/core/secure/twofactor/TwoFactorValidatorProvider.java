package org.springblossom.core.secure.twofactor;

/**
 * 两步验证 提供者接口
 *
 * @author guolf
 */
public interface TwoFactorValidatorProvider {

	/**
	 * 提供者名称
	 * @return
	 */
	String getProvider();

	/**
	 * 创建校验规则
	 * @param userId
	 * @param operation
	 * @return
	 */
	TwoFactorValidator createTwoFactorValidator(String userId,String operation);
}
