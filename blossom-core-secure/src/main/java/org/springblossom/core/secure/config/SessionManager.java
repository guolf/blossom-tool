package org.springblossom.core.secure.config;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springblossom.core.launch.constant.TokenConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义session管理
 * 支持从header或cookie中获取session id
 * 用于处理仅使用header token时下载文件无法鉴权的问题
 * @author guolf
 */
@AllArgsConstructor
public class SessionManager extends DefaultWebSessionManager {

	private ShiroSecureProperties shiroSecureProperties;

	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
		// 优先从请求头获取Token
		String token = WebUtils.toHttp(request).getHeader(shiroSecureProperties.getAuthTokenHeader());
		// 判断是否有值
		if (StringUtils.isNoneBlank(token)) {
			// 设置当前session状态
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "url");
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
			return token;
		}
		return super.getSessionId(request, response);
	}

	@Override
	protected void onInvalidation(Session session, InvalidSessionException ise, SessionKey key) {
		super.onInvalidation(session, ise, key);
	}


}
