
package org.springblossom.core.secure;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体
 *
 * @author Chill
 */
@Data
public class BlossomUser implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 客户端id
	 */
	@ApiModelProperty(hidden = true)
	private String clientId;

	/**
	 * 用户id
	 */
	@ApiModelProperty(hidden = true)
	private Integer userId;
	/**
	 * 租户编号
	 */
	@ApiModelProperty(hidden = true)
	private String tenantCode;
	/**
	 * 真实姓名
	 */
	@ApiModelProperty(hidden = true)
	private String userName;
	/**
	 * 账号
	 */
	@ApiModelProperty(hidden = true)
	private String account;

	@ApiModelProperty("部门ID")
	private Integer deptId;
	/**
	 * 角色id
	 */
	@ApiModelProperty(hidden = true)
	private String roleId;
	/**
	 * 角色名
	 */
	@ApiModelProperty(hidden = true)
	private String roleName;

	@ApiModelProperty("会话ID")
	private String sessionId;
}
