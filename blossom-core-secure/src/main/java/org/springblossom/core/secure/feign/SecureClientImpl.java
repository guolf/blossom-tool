package org.springblossom.core.secure.feign;

import lombok.AllArgsConstructor;
import org.springblossom.core.secure.BlossomUser;
import org.springblossom.core.secure.PermissionModel;
import org.springblossom.core.tool.api.R;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * 身份认证默认实现类
 *
 * @author guolf
 */
@Service
@ConditionalOnMissingBean(ISecureClient.class)
@AllArgsConstructor
public class SecureClientImpl implements ISecureClient {

	private ISecureFeignClient feignClient;

	/**
	 * 加载当前用户权限
	 *
	 * @return
	 */
	@Override
	public PermissionModel loadPermission() {
		R<PermissionModel> result = feignClient.loadPermission();
		if(result.isSuccess()) {
			return result.getData();
		}
		return null;
	}

	/**
	 * 根据登录名获取用户
	 *
	 * @param loginName
	 * @return
	 */
	@Override
	public BlossomUser getUserByLoginName(String loginName) {
		R<BlossomUser> result = feignClient.getUserByLoginName(loginName);
		if(result.isSuccess()) {
			return result.getData();
		}
		return null;
	}

	/**
	 * 根据登录名获取用户
	 *
	 * @param loginName 登录账号
	 * @param clientId  客户端ID
	 * @return
	 */
	@Override
	public BlossomUser getUserByLoginName(String loginName, String clientId) {
		R<BlossomUser> result = feignClient.getUserByLoginNameAndClientId(loginName,clientId);
		if(result.isSuccess()) {
			return result.getData();
		}
		return null;
	}
}
