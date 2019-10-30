package org.springblossom.core.secure.token;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.springblossom.core.tool.api.R;
import org.springblossom.core.tool.api.ResultCode;
import org.springblossom.core.tool.constant.BlossomConstant;
import org.springblossom.core.tool.jackson.JsonUtil;
import org.springblossom.core.tool.utils.WebUtil;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录拦截器
 *
 * @author guolf
 */
@Slf4j
public class TokenFilter extends AccessControlFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if (isLoginRequest(request, response)) {
			return true;
		} else {
			Subject subject = getSubject(request, response);
			return subject.getPrincipal() != null;
		}
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		Subject subject = getSubject(request, response);
		if (subject.isAuthenticated()) {
			return true;
		}
		log.warn("token认证失败，请求接口：{}，请求IP：{}，请求参数：{}", ((HttpServletRequest) request).getRequestURI(), WebUtil.getIP(((HttpServletRequest) request)), JsonUtil.toJson(request.getParameterMap()));
		onLoginFail(response, "认证失败，请重新登录");
		return false;
	}

	/**
	 * 登录失败时默认返回401状态码
	 *
	 * @param response
	 * @param errorString
	 * @throws IOException
	 */
	private void onLoginFail(ServletResponse response, String errorString) throws IOException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpResponse.setCharacterEncoding(BlossomConstant.UTF_8);
		httpResponse.setHeader(BlossomConstant.CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_UTF8_VALUE);
		R result = R.fail(ResultCode.UN_AUTHORIZED);
		result.setCode(HttpServletResponse.SC_UNAUTHORIZED);
		result.setMsg(errorString);
		httpResponse.getWriter().write(JsonUtil.toJson(result));
	}
}
