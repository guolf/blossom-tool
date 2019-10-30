//package org.springblossom.core.secure.jwt;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.SimpleAuthenticationInfo;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.springblossom.core.secure.BlossomUser;
//import org.springblossom.core.secure.PermissionModel;
//import org.springblossom.core.secure.feign.ISecureClient;
//import org.springblossom.core.secure.utils.SecureUtil;
//import org.springblossom.core.tool.api.R;
//import org.springblossom.core.tool.utils.CollectionUtil;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.stereotype.Service;
//
///**
// * jwt
// *
// * @author guolf
// */
//@Slf4j
//@Service
//@AllArgsConstructor
//@ConditionalOnMissingBean(value = AuthorizingRealm.class)
//public class JwtRealm extends AuthorizingRealm {
//
//	@Override
//	public boolean supports(AuthenticationToken token) {
//		return token instanceof JwtToken;
//	}
//
//	private ISecureClient secureClient;
//
//	/**
//	 * 身份认证
//	 *
//	 * @param authenticationToken
//	 * @return
//	 * @throws AuthenticationException
//	 */
//	@Override
//	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
//		String token = ((JwtToken) authenticationToken).getToken();
//		BlossomUser user = SecureUtil.getUser(token);
//		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, token, user.getUserName());
//		return info;
//	}
//
//	/**
//	 * 权限校验
//	 *
//	 * @param principalCollection
//	 * @return
//	 */
//	@Override
//	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//		Integer userId = ((BlossomUser) principalCollection.getPrimaryPrincipal()).getUserId();
//		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//		R<PermissionModel> permissionModel = secureClient.loadPermission();
//		if (permissionModel.isSuccess()) {
//			if (CollectionUtil.isNotEmpty(permissionModel.getData().getPermissionList())) {
//				info.addStringPermissions(permissionModel.getData().getPermissionList());
//			}
//			if (CollectionUtil.isNotEmpty(permissionModel.getData().getRoleList())) {
//				info.addRoles(permissionModel.getData().getRoleList());
//			}
//		}
//		return info;
//	}
//
//	@Override
//	protected Object getAuthenticationCacheKey(AuthenticationToken token) {
//		if (token != null) {
//			String jwtToken = ((JwtToken) token).getToken();
//			BlossomUser user = SecureUtil.getUser(jwtToken);
//			return user.getUserId();
//		}
//		return null;
//	}
//
//	@Override
//	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
//		Integer userId = ((BlossomUser) principals.getPrimaryPrincipal()).getUserId();
//		return userId;
//	}
//
//	@Override
//	protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
//		super.clearCachedAuthorizationInfo(principals);
//	}
//
//	@Override
//	protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
//
//	}
//
//
//}
