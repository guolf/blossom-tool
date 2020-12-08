package org.springblossom.core.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件上传配置
 *
 * @author guolf
 */
@Data
@ConfigurationProperties(prefix = "blossom.upload.local")
public class UploadProperties {

	/**
	 * 文件访问地址,上传文件后,将返回此地址+文件相对地址
	 */
	private String endpoint = "http://static.zhdjplus.cn";

	/**
	 * 本地存储位置
	 */
		private String basePath = "./upload";

	/**
	 * 是否启用本地存储
	 */
	private Boolean enable = false;

}
