package org.springblossom.core.push.android;

public class AndroidBroadCast extends AndroidNotification {

	public AndroidBroadCast(String appkey, String appMasterSecret) throws Exception {
		setAppMasterSecret(appMasterSecret);
		setPredefinedKeyValue("appkey", appkey);
		this.setPredefinedKeyValue("type", "broadcast");
	}
}
