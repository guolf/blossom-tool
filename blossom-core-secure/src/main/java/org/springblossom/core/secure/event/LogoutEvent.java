package org.springblossom.core.secure.event;

import org.springframework.context.ApplicationEvent;

/**
 * 注销事件
 *
 * @author guolf
 */
public class LogoutEvent extends ApplicationEvent {

	private String loginName;

	public LogoutEvent(String loginName) {
		super(loginName);
		this.loginName = loginName;
	}

	public String getLoginName() {
		return loginName;
	}
}
