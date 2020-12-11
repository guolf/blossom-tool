
package org.springblossom.core.secure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * AuthInfo
 *
 * @author Chill
 */
@Data
@ApiModel(description = "认证信息")
public class AuthInfo {

	@ApiModelProperty("用户ID")
	private Integer userId;

	@ApiModelProperty(value = "令牌")
	private String accessToken;

	@ApiModelProperty(value = "令牌类型")
	private String tokenType;

	@ApiModelProperty(value = "头像")
	private String avatar = "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png";

	@ApiModelProperty(value = "角色名")
	private String authority;

	@ApiModelProperty(value = "用户名")
	private String userName;

	@ApiModelProperty(value = "账号名")
	private String account;

	@ApiModelProperty(value = "过期时间")
	private long expiresIn;

	@ApiModelProperty(value = "许可证")
	private String license = "powered by blossom";

	@ApiModelProperty(value = "其他扩展信息")
	private Map info;
}
