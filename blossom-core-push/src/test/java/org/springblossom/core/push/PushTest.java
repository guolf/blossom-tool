package org.springblossom.core.push;

import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springblossom.core.push.android.*;
import org.springblossom.core.push.event.PushNotification;

public class PushTest {

	private String appkey = null;
	private String appMasterSecret = null;
	private String deviceToken = "Ak7y8bSrnP7o-4l569HfS_AIMO0DbLTfY7sh6x_nbWRD";
	private String timestamp = null;
	private PushClient client = new PushClient();

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public PushTest(String key, String secret) {
		try {
			appkey = key;
			appMasterSecret = secret;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void sendAndroidBroadcast() throws Exception {
		AndroidBroadCast broadcast = new AndroidBroadCast(appkey, appMasterSecret);
		broadcast.setTicker("Android broadcast ticker");
		broadcast.setTitle("中文的title");
		broadcast.setText("Android broadcast text");
		broadcast.goAppAfterOpen();
		broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		broadcast.setProductionMode();
		// Set customized fields
		broadcast.setExtraField("test", "helloworld");
		client.send(broadcast);
	}

	/**
	 * 单播(unicast)：向指定的设备发送消息。
	 * @throws Exception
	 */
	public void sendAndroidUnicast() throws Exception {
		AndroidUniCast unicast = new AndroidUniCast(appkey, appMasterSecret);
		unicast.setDeviceToken(getDeviceToken());
		unicast.setTicker("Android unicast ticker");
		unicast.setTitle("中文的title");
		unicast.setText("Android unicast text");
		unicast.goAppAfterOpen();
		unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		unicast.setTestMode();
		unicast.setExtraField("test", "hello_world");
		client.send(unicast);
	}

	public void sendAndroidGroupcast() throws Exception {
		AndroidGroupCast groupcast = new AndroidGroupCast(appkey, appMasterSecret);
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		JSONObject TestTag = new JSONObject();
		testTag.put("tag", "test");
		TestTag.put("tag", "Test");
		tagArray.put(testTag);
		tagArray.put(TestTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString());

		groupcast.setFilter(filterJson);
		groupcast.setTicker("Android groupcast ticker");
		groupcast.setTitle("中文的title");
		groupcast.setText("Android groupcast text");
		groupcast.goAppAfterOpen();
		groupcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		groupcast.setProductionMode();
		client.send(groupcast);
	}

	public void sendAndroidCustomizedcast() throws Exception {
		AndroidCustomizedCast customizedcast = new AndroidCustomizedCast(appkey, appMasterSecret);
		// TODO Set your alias here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then
		// use file_id to send customized notification.
		customizedcast.setAlias("userId", Lists.newArrayList("88"));
		customizedcast.setTicker("Android customizedcast ticker");
		customizedcast.setTitle("中文的title");
		customizedcast.setText("Android customizedcast text");
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		customizedcast.setProductionMode();
		client.send(customizedcast);
	}

	public void sendAndroidCustomizedcastFile() throws Exception {
		AndroidCustomizedCast customizedcast = new AndroidCustomizedCast(appkey, appMasterSecret);
		// TODO Set your alias here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then
		// use file_id to send customized notification.
		String fileId = client.uploadContents(appkey, appMasterSecret, "aa" + "\n" + "bb" + "\n" + "alias");
		customizedcast.setFileId(fileId, "alias_type");
		customizedcast.setTicker("Android customizedcast ticker");
		customizedcast.setTitle("中文的title");
		customizedcast.setText("Android customizedcast text");
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		customizedcast.setProductionMode();
		client.send(customizedcast);
	}

	public void sendAndroidFilecast() throws Exception {
		AndroidFileCast filecast = new AndroidFileCast(appkey, appMasterSecret);
		// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
		String fileId = client.uploadContents(appkey, appMasterSecret, "aa" + "\n" + "bb");
		filecast.setFileId(fileId);
		filecast.setTicker("Android filecast ticker");
		filecast.setTitle("中文的title");
		filecast.setText("Android filecast text");
		filecast.goAppAfterOpen();
		filecast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		client.send(filecast);
	}

	public static void main(String[] args) {
		PushTest demo = new PushTest("5d5cb57c3fc195290e0001e2", "jvxxqfiiqogbexqi3itneg7bnaecznjt");
//		PushTest demo = new PushTest("5d1ac99b3fc1951d39000acb", "weij8v3rrxyjzeflb8d6lhofwtjwpk9x");

		try {
			demo.sendAndroidUnicast();
//			demo.sendAndroidCustomizedcast();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
