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

package org.springblossom.core.log.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.log.annotation.ApiLog;
import org.springblossom.core.log.event.ApiLogEvent;
import org.springblossom.core.log.model.LogApi;
import org.springblossom.core.secure.utils.SecureUtil;
import org.springblossom.core.tool.constant.BlossomConstant;
import org.springblossom.core.tool.support.xss.XssHttpServletRequestWrapper;
import org.springblossom.core.tool.utils.Func;
import org.springblossom.core.tool.utils.SpringUtil;
import org.springblossom.core.tool.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * API日志信息事件发送
 *
 * @author Chill
 */
@Slf4j
public class ApiLogPublisher {

	/**
	 * API请求成功
	 *
	 * @param methodName
	 * @param methodClass
	 * @param apiLog
	 * @param time
	 */
	public static void publishEvent(String methodName, String methodClass, ApiLog apiLog, long time) {
		try {
			HttpServletRequest request = WebUtil.getRequest();
			XssHttpServletRequestWrapper requestWrapper = new XssHttpServletRequestWrapper(request);
			LogApi logApi = new LogApi();
			logApi.setType(BlossomConstant.LOG_NORMAL_TYPE);
			logApi.setTitle(apiLog.value());
			logApi.setTime(String.valueOf(time));
			logApi.setMethodClass(methodClass);
			logApi.setSuccess(1);
			logApi.setMethodName(methodName);
			logApi.setCreateBy(Func.toStr(SecureUtil.getUserId()));
			SpringUtil.publishEvent(new ApiLogEvent(logApi, requestWrapper));
		} catch (IOException ex) {
			log.error("记录日志出错", ex);
		}
	}
}
