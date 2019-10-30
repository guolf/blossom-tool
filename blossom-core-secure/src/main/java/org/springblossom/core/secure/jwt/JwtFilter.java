package org.springblossom.core.secure.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springblossom.core.launch.constant.TokenConstant;
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
import java.util.Objects;

/**
 * Jwt 过滤器
 *
 * @author guolf
 */
@Slf4j
public class JwtFilter extends AccessControlFilter {


	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
		Subject subject = getSubject(request,response);
		if(subject.isAuthenticated()) {
			return true;
		}
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String auth = httpServletRequest.getHeader(TokenConstant.HEADER);
		if ((auth != null) && (auth.length() > TokenConstant.AUTH_LENGTH)) {
			String headStr = auth.substring(0, 6).toLowerCase();
			if (headStr.compareTo(TokenConstant.BEARER) == 0) {
				auth = auth.substring(7);
				subject.login(new JwtToken(auth));
				return true;
			}
		}

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		log.warn("token认证失败，请求接口：{}，请求IP：{}，请求参数：{}", ((HttpServletRequest) request).getRequestURI(), WebUtil.getIP(((HttpServletRequest) request)), JsonUtil.toJson(request.getParameterMap()));
		R result = R.fail(ResultCode.UN_AUTHORIZED);
		httpResponse.setCharacterEncoding(BlossomConstant.UTF_8);
		httpResponse.setHeader(BlossomConstant.CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_UTF8_VALUE);
		httpResponse.setStatus(HttpServletResponse.SC_OK);
		try {
			httpResponse.getWriter().write(Objects.requireNonNull(JsonUtil.toJson(result)));
		} catch (IOException ex) {
			log.error(ex.getMessage());
		}
		return false;
	}

}
