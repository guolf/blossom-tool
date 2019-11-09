package org.springblossom.core.secure.twofactor.annotation;

import java.lang.annotation.*;

/**
 * 开启2FA双重验证
 *
 * @see org.springblossom.core.secure.twofactor.TwoFactorValidatorManager
 * @see org.springblossom.core.secure.twofactor.TwoFactorValidatorProvider
 * @see org.springblossom.core.secure.twofactor.TwoFactorValidator
 * @author guolf
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TwoFactor {

	/**
	 * @return 接口的标识, 用于实现不同的操作, 可能会配置不同的验证规则
	 */
	String value();

	/**
	 * 有效期
	 * @return 验证有效期, 超过有效期后需要重新进行验证
	 */
	long timeout() default 10 * 60 * 1000L;

	/**
	 * 验证器供应商,如: totp,sms,email,verificationCode,由{@link org.springblossom.core.secure.twofactor.TwoFactorValidatorProvider}进行自定义.
	 *
	 * @return provider
	 * @see org.springblossom.core.secure.twofactor.TwoFactorValidator#getProvider()
	 */
	String provider() default "default";

	/**
	 * 验证码的http参数名,在进行验证的时候需要传入此参数
	 *
	 * @return 验证码的参数名
	 */
	String verifyCodeParameter() default "verifyCode";

	/**
	 * 账号的http参数名,在进行验证的时候需要传入此参数
	 *
	 * @return 验证码的参数名
	 */
	String accountParameter() default "account";

	/**
	 * @return 关闭验证
	 */
	boolean ignore() default false;

	/**
	 *
	 * @return 错误提示
	 * @since 3.0.6
	 */
	String message() default "需要进行双因子验证";
}
