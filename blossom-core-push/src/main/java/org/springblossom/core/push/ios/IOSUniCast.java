package org.springblossom.core.push.ios;

/**
 * IOS 单播
 *
 * @author guolf
 */
public class IOSUniCast extends IOSNotification {
	public IOSUniCast(String appkey, String appMasterSecret) throws Exception{
		setAppMasterSecret(appMasterSecret);
		setPredefinedKeyValue("appkey", appkey);
		this.setPredefinedKeyValue("type", "unicast");
	}

	public void setDeviceToken(String token) throws Exception {
		setPredefinedKeyValue("device_tokens", token);
	}
}

