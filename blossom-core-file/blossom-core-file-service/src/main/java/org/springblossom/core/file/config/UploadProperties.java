package org.springblossom.core.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件上传配置
 *
 * @author guolf
 */
@Data
@ConfigurationProperties(prefix = "blossom.upload")
public class UploadProperties {

	/**
	 * 静态文件存储目录,不能以/结尾
	 */
	private String staticFilePath = "./static/upload";

	/**
	 * 静态文件访问地址,上传静态文件后,将返回此地址+文件相对地址,以/结尾
	 */
	private String staticLocation = "http://static.zhdjplus.cn/";

	/**
	 * 文件上传目录
	 */
	private String filePath = "./upload/file";

	/**
	 * 文件下载地址
	 */
	private String downloadPath = "/file/download/";

	/**
	 * 文件下载前缀
	 */
	private String downloadPrefix = "/blossom-system";

	/**
	 * 存储类型，local本地，qiniu
	 */
	private String storage = "local";

}
