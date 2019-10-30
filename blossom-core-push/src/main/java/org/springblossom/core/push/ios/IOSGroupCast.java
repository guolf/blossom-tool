package org.springblossom.core.push.ios;

import org.json.JSONObject;

/**
 * IOS 组播
 *
 * @author guolf
 */
public class IOSGroupCast extends IOSNotification {

	public IOSGroupCast(String appkey, String appMasterSecret) throws Exception {
		setAppMasterSecret(appMasterSecret);
		setPredefinedKeyValue("appkey", appkey);
		this.setPredefinedKeyValue("type", "groupcast");
	}

	public void setFilter(JSONObject filter) throws Exception {
		setPredefinedKeyValue("filter", filter);
	}

}
