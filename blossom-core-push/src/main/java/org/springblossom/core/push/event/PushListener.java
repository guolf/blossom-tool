package org.springblossom.core.push.event;

import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.push.PushClient;
import org.springblossom.core.push.android.AndroidCustomizedCast;
import org.springblossom.core.push.android.AndroidNotification;
import org.springblossom.core.push.config.UmengPushProperties;
import org.springblossom.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 消息推送监听
 *
 * @author guolf
 */
@Slf4j
@Component
@EnableConfigurationProperties(UmengPushProperties.class)
public class PushListener {

	@Autowired
	private UmengPushProperties pushProperties;

	private PushClient client = new PushClient();

	@Async
	@Order
	@EventListener(PushEvent.class)
	public void sendCustomizedCast(PushEvent event) {
		try {
			PushNotification notification = event.getNotification();
			if (Func.isNull(notification) || Func.isEmpty(notification.getAlias())) {
				return;
			}
			AndroidCustomizedCast customizedcast = new AndroidCustomizedCast(pushProperties.getAppKey(), pushProperties.getAppMasterSecret());
			customizedcast.setAlias("userId", notification.getAlias());
			customizedcast.setTicker(notification.getTicker());
			customizedcast.setTitle(notification.getTitle());
			customizedcast.setText(notification.getContent());
			if (Func.isNull(notification.getAction())) {
				customizedcast.goAppAfterOpen();
			} else if (notification.getAction().equals(AndroidNotification.AfterOpenAction.go_app)) {
				customizedcast.goAppAfterOpen();
			} else if (notification.getAction().equals(AndroidNotification.AfterOpenAction.go_activity)) {
				customizedcast.goActivityAfterOpen(notification.getTarget());
			} else if (notification.getAction().equals(AndroidNotification.AfterOpenAction.go_url)) {
				customizedcast.goUrlAfterOpen(notification.getTarget());
			} else if (notification.getAction().equals(AndroidNotification.AfterOpenAction.go_custom)) {
				customizedcast.goCustomAfterOpen(notification.getTarget());
			}

			customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
			if ("prod".equalsIgnoreCase(pushProperties.getProfile())) {
				customizedcast.setProductionMode();
			} else {
				customizedcast.setTestMode();
			}
			client.send(customizedcast);
		} catch (Exception ex) {
			log.error("推送失败", ex);
		}
	}
}
