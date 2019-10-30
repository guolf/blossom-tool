package org.springblossom.core.push.android;

import org.json.JSONObject;

public class AndroidGroupCast extends AndroidNotification {

	public AndroidGroupCast(String appkey, String appMasterSecret) throws Exception {
		setAppMasterSecret(appMasterSecret);
		setPredefinedKeyValue("appkey", appkey);
		this.setPredefinedKeyValue("type", "groupcast");
	}

	public void setFilter(JSONObject filter) throws Exception {
		setPredefinedKeyValue("filter", filter);
	}
}
