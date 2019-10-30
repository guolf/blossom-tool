package org.springblossom.core.secure;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 在线用户
 *
 * @author guolf
 */
@Data
public class OnlineUser extends BlossomUser {

	@ApiModelProperty("会话开始时间")
	private LocalDateTime startTimestamp;

	@ApiModelProperty("最后访问时间")
	private LocalDateTime lastAccessTime;

	@ApiModelProperty("浏览器")
	private String browser;

	@ApiModelProperty("IP地址")
	private String host;

	@ApiModelProperty("操作系统")
	private String os;

}
