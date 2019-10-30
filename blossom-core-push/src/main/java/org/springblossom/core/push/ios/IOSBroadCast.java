package org.springblossom.core.push.ios;

/**
 * IOS广播
 *
 * @author guolf
 */
public class IOSBroadCast extends IOSNotification {

	public IOSBroadCast(String appkey, String appMasterSecret) throws Exception {
		setAppMasterSecret(appMasterSecret);
		setPredefinedKeyValue("appkey", appkey);
		this.setPredefinedKeyValue("type", "broadcast");
	}

}
