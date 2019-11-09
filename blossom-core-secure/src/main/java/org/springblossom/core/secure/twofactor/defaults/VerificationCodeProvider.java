package org.springblossom.core.secure.twofactor.defaults;

import org.springblossom.core.secure.constant.SecureConstant;
import org.springblossom.core.secure.exception.SecureException;
import org.springblossom.core.secure.twofactor.TwoFactorTokenManager;
import org.springblossom.core.tool.utils.Func;
import org.springblossom.core.tool.utils.StringUtil;
import org.springblossom.core.tool.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 验证码校验
 *
 * @author guolf
 */
@Component
@ConditionalOnProperty(prefix = "blossom.shiro", name = "two-factor", havingValue = "true")
public class VerificationCodeProvider extends DefaultTwoFactorValidatorProvider {

	@Autowired
	private RedisTemplate redisTemplate;

	public VerificationCodeProvider(TwoFactorTokenManager twoFactorTokenManager) {
		super("verificationCode", twoFactorTokenManager);
	}

	/**
	 * 校验
	 *
	 * @param userId           登录账号
	 * @param verificationCode 验证码
	 * @return
	 */
	@Override
	protected boolean validate(String userId, String verificationCode) {
		String verificationKey = WebUtil.getRequest().getParameter(SecureConstant.VERIFICATION_KEY_PARAM);
		if (redisTemplate.hasKey(SecureConstant.LOCK_USER_KEY + userId)) {
			// 账号被锁定
			throw new SecureException("账号被锁定");
		}
		if (redisTemplate.hasKey(SecureConstant.VERIFICATION_CODE_USER_KEY + userId)) {
			// 需要验证码
			if (StringUtil.isBlank(verificationCode) || StringUtil.isBlank(verificationKey)) {
				return false;
			}
			String code = Func.toStr(redisTemplate.opsForValue().get(SecureConstant.VERIFICATION_KEY + verificationKey));
			if (!code.equalsIgnoreCase(verificationCode)) {
				redisTemplate.delete(SecureConstant.VERIFICATION_KEY + verificationKey);
				return false;
			}
		}
		return true;
	}
}
