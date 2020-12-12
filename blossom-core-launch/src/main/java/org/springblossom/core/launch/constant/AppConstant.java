
package org.springblossom.core.launch.constant;

/**
 * 系统常量
 *
 * @author Chill
 */
public interface AppConstant {

	/**
	 * 应用版本
	 */
	String APPLICATION_VERSION = "2.0.0";

	/**
	 * 基础包
	 */
	String BASE_PACKAGES = "org.springblossom";

	/**
	 * 应用名前缀
	 */
	String APPLICATION_NAME_PREFIX = "blossom-";
	/**
	 * 网关模块名称
	 */
	String APPLICATION_GATEWAY_NAME = APPLICATION_NAME_PREFIX + "gateway";
	/**
	 * 授权模块名称
	 */
	String APPLICATION_AUTH_NAME = APPLICATION_NAME_PREFIX + "auth";
	/**
	 * 监控模块名称
	 */
	String APPLICATION_ADMIN_NAME = APPLICATION_NAME_PREFIX + "admin";
	/**
	 * 系统模块名称
	 */
	String APPLICATION_SYSTEM_NAME = APPLICATION_NAME_PREFIX + "system";
	/**
	 * 日志模块名称
	 */
	String APPLICATION_LOG_NAME = APPLICATION_NAME_PREFIX + "log";
	/**
	 * 开发模块名称
	 */
	String APPLICATION_DEVELOP_NAME = APPLICATION_NAME_PREFIX + "develop";
	/**
	 * 测试模块名称
	 */
	String APPLICATION_TEST_NAME = APPLICATION_NAME_PREFIX + "test";

	/**
	 * 开发环境
	 */
	String DEV_CDOE = "dev";
	/**
	 * 生产环境
	 */
	String PROD_CODE = "prod";
	/**
	 * 测试环境
	 */
	String TEST_CODE = "test";

	/**
	 * 代码部署于 linux 上，工作默认为 mac 和 Windows
	 */
	String OS_NAME_LINUX = "LINUX";

}
