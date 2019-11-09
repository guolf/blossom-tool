package org.springblossom.core.secure.feign;

import org.springblossom.core.launch.constant.AppConstant;
import org.springblossom.core.secure.BlossomUser;
import org.springblossom.core.secure.PermissionModel;
import org.springblossom.core.tool.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 身份认证feign接口
 *
 * @author guolf
 */
@FeignClient(
	value = AppConstant.APPLICATION_SYSTEM_NAME
)
public interface ISecureFeignClient {

	String API_PREFIX = "/feign-client";

	/**
	 * 加载当前用户权限
	 *
	 * @return
	 */
	@GetMapping(API_PREFIX + "/permission/loadPermission")
	R<PermissionModel> loadPermission();

	/**
	 * 根据登录名获取用户
	 *
	 * @param loginName
	 * @return
	 */
	@GetMapping(API_PREFIX + "/auth/getUserByLoginName")
	R<BlossomUser> getUserByLoginName(@RequestParam String loginName);

	/**
	 * 根据登录名获取用户
	 *
	 * @param loginName
	 * @param clientId
	 * @return
	 */
	@GetMapping(API_PREFIX + "/auth/getUserByLoginNameAndClientId")
	default R<BlossomUser> getUserByLoginNameAndClientId(@RequestParam String loginName, @RequestParam String clientId) {
		return getUserByLoginName(loginName);
	};

}
