package org.springblossom.core.secure.token;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springblossom.core.secure.BlossomUser;
import org.springblossom.core.secure.PermissionModel;
import org.springblossom.core.secure.config.ShiroSecureProperties;
import org.springblossom.core.secure.constant.SecureConstant;
import org.springblossom.core.secure.feign.ISecureClient;
import org.springblossom.core.secure.utils.SecureUtil;
import org.springblossom.core.tool.utils.CollectionUtil;
import org.springframework.stereotype.Service;

/**
 * 用户名密码校验
 *
 * @author guolf
 */
@Slf4j
@Service
@AllArgsConstructor
public class TokenRealm extends AuthorizingRealm {

	private ISecureClient secureClient;
	private ShiroSecureProperties shiroSecureProperties;


	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UserNamePasswordToken;
	}

	/**
	 * 权限校验
	 *
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		PermissionModel permissionModel = secureClient.loadPermission();

		if (CollectionUtil.isNotEmpty(permissionModel.getPermissionList())) {
			info.addStringPermissions(permissionModel.getPermissionList());
		}
		if (CollectionUtil.isNotEmpty(permissionModel.getRoleList())) {
			info.addRoles(permissionModel.getRoleList());
		}
		return info;
	}

	/**
	 * 授权缓存KEY
	 *
	 * @param principals
	 * @return
	 */
	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		BlossomUser user =  (BlossomUser)principals.getPrimaryPrincipal();
		return shiroSecureProperties.getCachePermissionKey() + user.getUserId();
	}

	@Override
	protected Object getAuthenticationCacheKey(AuthenticationToken token) {
		return super.getAuthenticationCacheKey(token);
	}


	/**
	 * 身份认证
	 *
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UserNamePasswordToken userNamePasswordToken = (UserNamePasswordToken) token;
		BlossomUser user = secureClient.getUserByLoginName(userNamePasswordToken.getLoginName());
		if (user != null) {
			return new SimpleAuthenticationInfo(user, userNamePasswordToken.getPassword(), user.getUserName());
		}
		return null;
	}


}
