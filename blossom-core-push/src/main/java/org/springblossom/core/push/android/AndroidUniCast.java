package org.springblossom.core.push.android;

public class AndroidUniCast extends AndroidNotification {

	public AndroidUniCast(String appkey, String appMasterSecret) throws Exception {
		setAppMasterSecret(appMasterSecret);
		setPredefinedKeyValue("appkey", appkey);
		this.setPredefinedKeyValue("type", "unicast");
	}

	public void setDeviceToken(String token) throws Exception {
		setPredefinedKeyValue("device_tokens", token);
	}

}
