
package org.springblossom.core.secure.constant;

/**
 * 授权校验常量
 *
 * @author Chill
 */
public interface SecureConstant {

	/**
	 * 认证请求头
	 */
	String BASIC_HEADER_KEY = "Authorization";

	/**
	 * 认证请求头前缀
	 */
	String BASIC_HEADER_PREFIX = "Basic ";

	/**
	 * blossom_client表字段
	 */
	String CLIENT_FIELDS = "client_id, client_secret, access_token_validity, refresh_token_validity";

	/**
	 * blossom_client查询语句
	 */
	String BASE_STATEMENT = "select " + CLIENT_FIELDS + " from t_auth_client";

	/**
	 * blossom_client查询排序
	 */
	String DEFAULT_FIND_STATEMENT = BASE_STATEMENT + " order by client_id";

	/**
	 * 查询client_id
	 */
	String DEFAULT_SELECT_STATEMENT = BASE_STATEMENT + " where client_id = ?";

	/**
	 * 密码错误次数redis key
	 */
	String PWD_ERROR_TIMES_KEY = "PWD_ERROR_TIMES::";

	String LOCK_USER_KEY = "LOCK_USER::";

	String VERIFICATION_CODE_USER_KEY = "VERIFICATION_CODE_USER::";

}
