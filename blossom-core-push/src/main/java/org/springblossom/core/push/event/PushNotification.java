package org.springblossom.core.push.event;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springblossom.core.push.android.AndroidNotification;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 推送基类
 *
 * @author guolf
 */
@Data
public class PushNotification {

	PushNotification(List<Serializable> alias, String title, String content, LocalDateTime sendTime, AndroidNotification.AfterOpenAction action, String target,String ticker) {
		this.alias = alias;
		this.title = title;
		this.content = content;
		this.sendTime = sendTime;
		this.action = action;
		this.target = target;
		this.ticker = ticker;
	}

	public static PushNotification.PushNotificationBuilder builder() {
		return new PushNotification.PushNotificationBuilder();
	}

	private PushNotification() {
	}

	/**
	 * 用户别名
	 */
	private List<Serializable> alias;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 定时发送
	 */
	private LocalDateTime sendTime;

	/**
	 * 点击推送后的动作
	 */
	private AndroidNotification.AfterOpenAction action;

	/**
	 * 目标
	 */
	private String target;

	/**
	 * 通知栏提示文字
	 */
	private String ticker;


	public static class PushNotificationBuilder {
		private List<Serializable> alias = Lists.newArrayList();
		private String title;
		private String content;
		private LocalDateTime sendTime;
		private AndroidNotification.AfterOpenAction action;
		private String target;
		private String ticker;

		PushNotificationBuilder() {
		}

		public PushNotification.PushNotificationBuilder addAlias(Serializable alias) {
			this.alias.add(alias);
			return this;
		}

		public PushNotification.PushNotificationBuilder alias(List<Serializable> alias) {
			this.alias = alias;
			return this;
		}

		public PushNotification.PushNotificationBuilder title(String title) {
			this.title = title;
			return this;
		}

		public PushNotification.PushNotificationBuilder content(String content) {
			this.content = content;
			return this;
		}

		public PushNotification.PushNotificationBuilder sendTime(LocalDateTime sendTime) {
			this.sendTime = sendTime;
			return this;
		}

		public PushNotification.PushNotificationBuilder action(AndroidNotification.AfterOpenAction action) {
			this.action = action;
			return this;
		}

		public PushNotification.PushNotificationBuilder target(String target) {
			this.target = target;
			return this;
		}

		public PushNotification.PushNotificationBuilder ticker(String ticker) {
			this.ticker = ticker;
			return this;
		}

		public PushNotification build() {
			return new PushNotification(this.alias, this.title, this.content, this.sendTime, this.action, this.target,this.ticker);
		}
	}
}
