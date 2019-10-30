package org.springblossom.core.secure.twofactor.defaults;

import lombok.Getter;
import org.springblossom.core.secure.twofactor.TwoFactorTokenManager;
import org.springblossom.core.secure.twofactor.TwoFactorValidator;
import org.springblossom.core.secure.twofactor.TwoFactorValidatorProvider;

/**
 *
 * @author guolf
 */
@Getter
public abstract class DefaultTwoFactorValidatorProvider implements TwoFactorValidatorProvider {

    private String provider;

    private TwoFactorTokenManager twoFactorTokenManager;

    public DefaultTwoFactorValidatorProvider(String provider, TwoFactorTokenManager twoFactorTokenManager) {
        this.provider = provider;
        this.twoFactorTokenManager = twoFactorTokenManager;
    }

	/**
	 * 校验
	 * @param userId 登录账号
	 * @param code 验证码
	 * @return
	 */
    protected abstract boolean validate(String userId, String code);

    @Override
    public TwoFactorValidator createTwoFactorValidator(String userId, String operation) {
        return new DefaultTwoFactorValidator(getProvider(),
			code -> validate(userId, code),
			() -> twoFactorTokenManager.getToken(userId, operation));
    }
}
