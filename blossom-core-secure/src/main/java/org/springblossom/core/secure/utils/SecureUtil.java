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
package org.springblossom.core.secure.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springblossom.core.launch.constant.TokenConstant;
import org.springblossom.core.secure.BlossomUser;
import org.springblossom.core.secure.constant.SecureConstant;
import org.springblossom.core.secure.exception.SecureException;
import org.springblossom.core.secure.provider.IClientDetails;
import org.springblossom.core.secure.provider.IClientDetailsService;
import org.springblossom.core.tool.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Objects;

/**
 * Secure工具类
 *
 * @author Chill
 */
@Slf4j
public class SecureUtil {

	private static final String BLOSSOM_USER_REQUEST_ATTR = "_BLOSSOM_USER_REQUEST_ATTR_";

	private final static String HEADER = TokenConstant.HEADER;

	/**
	 * 获取用户信息
	 *
	 * @return blossomUser
	 */
	public static BlossomUser getUser() {
		HttpServletRequest request = WebUtil.getRequest();
		if (request == null) {
			return null;
		}
		// 优先从 request 中获取
		Object blossomUser = request.getAttribute(BLOSSOM_USER_REQUEST_ATTR);
		if (blossomUser == null) {
			blossomUser = getUser(request);
			if (blossomUser != null) {
				// 设置到 request 中
				request.setAttribute(BLOSSOM_USER_REQUEST_ATTR, blossomUser);
			}
		}
		return (BlossomUser) blossomUser;
	}

	/**
	 * 获取用户信息
	 *
	 * @param request request
	 * @return blossomUser
	 */
	public static BlossomUser getUser(HttpServletRequest request) {
		return (BlossomUser) SecurityUtils.getSubject().getPrincipal();
	}

	/**
	 * 获取用户id
	 *
	 * @return userId
	 */
	public static Integer getUserId() {
		BlossomUser user = getUser();
		return (null == user) ? -1 : user.getUserId();
	}

	/**
	 * 获取用户id
	 *
	 * @param request request
	 * @return userId
	 */
	public static Integer getUserId(HttpServletRequest request) {
		BlossomUser user = getUser(request);
		return (null == user) ? -1 : user.getUserId();
	}

	/**
	 * 获取用户账号
	 *
	 * @return userAccount
	 */
	public static String getUserAccount() {
		BlossomUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getAccount();
	}

	/**
	 * 获取用户账号
	 *
	 * @param request request
	 * @return userAccount
	 */
	public static String getUserAccount(HttpServletRequest request) {
		BlossomUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getAccount();
	}

	/**
	 * 获取用户名
	 *
	 * @return userName
	 */
	public static String getUserName() {
		BlossomUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getUserName();
	}

	/**
	 * 获取用户名
	 *
	 * @param request request
	 * @return userName
	 */
	public static String getUserName(HttpServletRequest request) {
		BlossomUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getUserName();
	}

	/**
	 * 获取用户角色
	 *
	 * @return userName
	 */
	public static String getUserRole() {
		BlossomUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getRoleName();
	}

	/**
	 * 获取用角色
	 *
	 * @param request request
	 * @return userName
	 */
	public static String getUserRole(HttpServletRequest request) {
		BlossomUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getRoleName();
	}

	/**
	 * 获取租户编号
	 *
	 * @return tenantCode
	 */
	public static String getTenantCode() {
		BlossomUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getTenantCode();
	}

	/**
	 * 获取租户编号
	 *
	 * @param request request
	 * @return tenantCode
	 */
	public static String getTenantCode(HttpServletRequest request) {
		BlossomUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getTenantCode();
	}

	/**
	 * 获取客户端id
	 *
	 * @return
	 */
	public static String getClientId() {
		BlossomUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getClientId();
	}

	/**
	 * 获取客户端id
	 *
	 * @param request request
	 * @return tenantCode
	 */
	public static String getClientId(HttpServletRequest request) {
		BlossomUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getClientId();
	}

	/**
	 * 获取请求头
	 *
	 * @return header
	 */
	public static String getHeader() {
		return getHeader(Objects.requireNonNull(WebUtil.getRequest()));
	}

	/**
	 * 获取请求头
	 *
	 * @param request request
	 * @return header
	 */
	public static String getHeader(HttpServletRequest request) {
		return request.getHeader(HEADER);
	}

	/**
	 * 客户端信息解码
	 */
	@SneakyThrows
	public static String[] extractAndDecodeHeader() {
		// 获取请求头客户端信息
		String header = Objects.requireNonNull(WebUtil.getRequest()).getHeader(SecureConstant.BASIC_HEADER_KEY);
		if (header == null || !header.startsWith(SecureConstant.BASIC_HEADER_PREFIX)) {
			throw new SecureException("No client information in request header");
		}
		byte[] base64Token = header.substring(6).getBytes(Charsets.UTF_8_NAME);

		byte[] decoded;
		try {
			decoded = Base64.getDecoder().decode(base64Token);
		} catch (IllegalArgumentException var7) {
			throw new RuntimeException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, Charsets.UTF_8_NAME);
		int index = token.indexOf(StringPool.COLON);
		if (index == -1) {
			throw new RuntimeException("Invalid basic authentication token");
		} else {
			return new String[]{token.substring(0, index), token.substring(index + 1)};
		}
	}

	public static String createToken() {
		org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
		return subject.getSession().getId().toString();
	}

	/**
	 * 获取请求头中的客户端id
	 */
	public static String getClientIdFromHeader() {
		String[] tokens = extractAndDecodeHeader();
		assert tokens.length == 2;
		return tokens[0];
	}

	/**
	 * 校验Client
	 *
	 * @param clientId     客户端id
	 * @param clientSecret 客户端密钥
	 * @return boolean
	 */
	public static boolean validateClient(String clientId, String clientSecret) {
		IClientDetails clientDetails = SpringUtil.getBean(IClientDetailsService.class).loadClientByClientId(clientId);
		if (clientDetails != null) {
			return StringUtil.equals(clientId, clientDetails.getClientId()) && StringUtil.equals(clientSecret, clientDetails.getClientSecret());
		}
		return false;
	}
}
