package org.springblossom.core.push.android;

import org.springblossom.core.tool.utils.CollectionUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Android 自定义广播
 *
 * @author guolf
 */
public class AndroidCustomizedCast extends AndroidNotification {

	public AndroidCustomizedCast(String appkey, String appMasterSecret) throws Exception {
		setAppMasterSecret(appMasterSecret);
		setPredefinedKeyValue("appkey", appkey);
		this.setPredefinedKeyValue("type", "customizedcast");
	}

	public void setAlias(String aliasType, List<Serializable> alias) throws Exception {
		setPredefinedKeyValue("alias", CollectionUtil.convertToString(alias,","));
		setPredefinedKeyValue("alias_type", aliasType);
	}

	public void setFileId(String fileId,String aliasType) throws Exception {
		setPredefinedKeyValue("file_id", fileId);
		setPredefinedKeyValue("alias_type", aliasType);
	}

}
