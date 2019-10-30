package org.springblossom.core.secure.event;

import org.springframework.context.ApplicationEvent;

/**
 * 权限变更事件
 *
 * @author guolf
 */
public class PermissionChangeEvent extends ApplicationEvent {

	public PermissionChangeEvent(Object source) {
		super(source);
	}
}
