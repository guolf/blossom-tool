package org.springblossom.core.push.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 推送事件
 *
 * @author guolf
 */

public class PushEvent extends ApplicationEvent {

	private PushNotification notification;

	public void setNotification(PushNotification notification) {
		this.notification = notification;
	}

	public PushNotification getNotification() {
		return notification;
	}

	public PushEvent(PushNotification notification) {
		super(notification);
		this.notification = notification;
	}
}
