/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblossom.core.secure.constant;

/**
 * 授权校验常量
 *
 * @author guolf
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
	 * 用户权限缓存名称
	 */
	String CACHE_USER_PERMISSION = "authorizationCache::";

	/**
	 * 密码错误次数redis key
	 */
	String PWD_ERROR_TIMES_KEY = "PWD_ERROR_TIMES::";

	/**
	 * 锁定用户前缀
	 */
	String LOCK_USER_KEY = "LOCK_USER::";

	/**
	 * 需要验证码用户前缀
	 */
	String VERIFICATION_CODE_USER_KEY = "VERIFICATION_CODE_USER::";

	/**
	 * 验证码前缀
	 */
	String VERIFICATION_KEY = "verification::";

	/**
	 * 验证码参数，前端传入参数名称
	 */
	String VERIFICATION_KEY_PARAM = "verificationKey";

}
