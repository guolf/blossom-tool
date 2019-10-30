package org.springblossom.core.secure;

import lombok.Data;

import java.util.Collection;

/**
 * 权限模型
 *
 * @author guolf
 */
@Data
public class PermissionModel {

	Collection<String> roleList;
	Collection<String> permissionList;
}
