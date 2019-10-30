package org.springblossom.core.secure.event;

import org.springframework.context.ApplicationEvent;

/**
 * 登录失败事件
 *
 * @author guolf
 */
public class LoginFailEvent extends ApplicationEvent {

	private String loginName;

	public LoginFailEvent(String loginName) {
		super(loginName);
		this.loginName = loginName;
	}


	public String getLoginName() {
		return loginName;
	}
}
