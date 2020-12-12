
package org.springblossom.core.mp.base;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 租户基础实体类
 *
 * @author Chill
 */
@Data
public class TenantEntity extends BaseEntity {

	/**
	 * 租户编号
	 */
	@ApiModelProperty(value = "租户编号")
	private String tenantCode;

}
