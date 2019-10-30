//package org.springblossom.core.secure.feign;
//
//
//import com.google.common.collect.Lists;
//import org.springblossom.core.secure.BlossomUser;
//import org.springblossom.core.secure.PermissionModel;
//import org.springblossom.core.tool.api.R;
//import org.springframework.stereotype.Component;
//
///**
// * 获取权限失败回调
// *
// * @author guolf
// */
//@Component
//public class SecureClientFallback implements ISecureClient {
//
//	/**
//	 * 加载当前用户权限
//	 *
//	 * @return
//	 */
//	@Override
//	public R<PermissionModel> loadPermission() {
//		PermissionModel model = new PermissionModel();
//		model.setRoleList(Lists.newArrayList("user"));
//		return R.data(model);
//	}
//
//	/**
//	 * 根据登录名获取用户
//	 *
//	 * @param loginName
//	 * @return
//	 */
//	@Override
//	public R<BlossomUser> getUserByLoginName(String loginName) {
//		return null;
//	}
//}
