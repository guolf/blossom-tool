package org.springblossom.core.secure;

import java.io.Serializable;

/**
 * 用户基类
 *
 * @author guolf
 */
public interface BlossomUser extends Serializable {

	String getClientId();

	void setClientId(String clientId);

	Integer getUserId();

	void setUserId(Integer userId);

	String getTenantCode();

	void setTenantCode(String tenantCode);

	String getUserName();

	void setUserName(String userName);

	String getAccount();

	void setAccount(String account);

	Integer getDeptId();

	void setDeptId(Integer deptId);

	String getRoleId();

	void setRoleId(String roleId);

	String getRoleName();

	void setRoleName(String roleName);
}
