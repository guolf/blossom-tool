package org.springblossom.core.secure.feign;

import org.springblossom.core.secure.BlossomUser;
import org.springblossom.core.secure.PermissionModel;

/**
 * 身份认证接口类
 * 不直接使用feign接口，防止某些模块需要自定义账号权限，方便扩展
 *
 * @author guolf
 */
public interface ISecureClient {


	/**
	 * 加载当前用户权限
	 *
	 * @return
	 */
	PermissionModel loadPermission();

	/**
	 * 根据登录名获取用户
	 *
	 * @param loginName
	 * @return
	 */
	BlossomUser getUserByLoginName(String loginName);

	/**
	 * 根据登录名获取用户
	 *
	 * @param loginName 登录账号
	 * @param clientId 客户端ID
	 * @return
	 */
	default BlossomUser getUserByLoginName(String loginName, String clientId) {
		return getUserByLoginName(loginName);
	};

}
