package org.springblossom.core.secure.event;

import org.springframework.context.ApplicationEvent;

/**
 * 登录成功事件
 *
 * @author guolf
 */
public class LoginSuccessEvent extends ApplicationEvent {

	private String loginName;

	public LoginSuccessEvent(String loginName) {
		super(loginName);
		this.loginName = loginName;
	}

	public String getLoginName() {
		return loginName;
	}
}
