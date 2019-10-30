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
package org.springblossom.core.log.error;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springblossom.core.log.publisher.ErrorLogPublisher;
import org.springblossom.core.tool.api.R;
import org.springblossom.core.tool.api.ResultCode;
import org.springblossom.core.tool.utils.BeanUtil;
import org.springblossom.core.tool.utils.Func;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author Chill
 */
@Slf4j
public class BlossomErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
		String requestUri = this.getAttr(webRequest, "javax.servlet.error.request_uri");
		Integer status = this.getAttr(webRequest, "javax.servlet.error.status_code");
		Throwable error = getError(webRequest);
		R result;
		if (error == null) {
			log.error("URL:{} error status:{}", requestUri, status);
			if(Func.equals(status, HttpServletResponse.SC_NOT_FOUND)) {
				result = R.fail(ResultCode.NOT_FOUND, this.getAttr(webRequest, "javax.servlet.error.message"));
				return BeanUtil.toMap(result);
			}
			result = R.fail(ResultCode.FAILURE, "系统未知异常[HttpStatus]:" + status);
		} else {
			log.error(String.format("URL:%s error status:%d", requestUri, status), error);
			result = R.fail(status, error.getMessage());
		}
		//发送服务异常事件
		ErrorLogPublisher.publishEvent(error, requestUri);
		return BeanUtil.toMap(result);
	}



	@Nullable
	private <T> T getAttr(WebRequest webRequest, String name) {
		return (T) webRequest.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
	}

}
