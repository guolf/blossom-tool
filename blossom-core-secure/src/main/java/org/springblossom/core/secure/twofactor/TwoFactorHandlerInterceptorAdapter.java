package org.springblossom.core.secure.twofactor;

import lombok.AllArgsConstructor;
import org.springblossom.core.secure.exception.NeedTwoFactorException;
import org.springblossom.core.secure.twofactor.annotation.TwoFactor;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 两步认证 拦截器
 *
 * @author guolf
 */
@AllArgsConstructor
public class TwoFactorHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

	private TwoFactorValidatorManager validatorManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod method = ((HandlerMethod) handler);
			TwoFactor factor = method.getMethodAnnotation(TwoFactor.class);
			if (factor == null || factor.ignore()) {
				return true;
			}
			String userId = request.getParameter(factor.accountParameter());
			if (userId == null) {
				userId = request.getHeader(factor.accountParameter());
			}
			TwoFactorValidator validator = validatorManager.getValidator(userId, factor.value(), factor.provider());
			if (!validator.expired()) {
				return true;
			}
			String code = request.getParameter(factor.verifyCodeParameter());
			if (code == null) {
				code = request.getHeader(factor.verifyCodeParameter());
			}
			if (!validator.verify(code, factor.timeout())) {
				throw new NeedTwoFactorException("验证码错误", factor.provider());
			}
		}
		return super.preHandle(request, response, handler);
	}
}
