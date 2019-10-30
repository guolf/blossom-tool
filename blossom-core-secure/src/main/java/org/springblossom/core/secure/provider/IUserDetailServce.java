package org.springblossom.core.secure.provider;

import org.springblossom.core.secure.BlossomUser;
import org.springblossom.core.secure.PermissionModel;

/**
 * 用户信息详情接口
 *
 * @author guolf
 */
public interface IUserDetailServce {

	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return
	 */
	BlossomUser getUserByLoginName(String loginName);

	/**
	 * 查询当前用户权限
	 * @return
	 */
	PermissionModel loadPermission();

}
