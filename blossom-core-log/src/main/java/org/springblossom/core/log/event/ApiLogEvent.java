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

package org.springblossom.core.log.event;

import lombok.Getter;
import lombok.Setter;
import org.springblossom.core.log.model.LogApi;
import org.springblossom.core.tool.support.xss.XssHttpServletRequestWrapper;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统日志事件
 *
 * @author Chill
 */
@Getter
@Setter
public class ApiLogEvent extends ApplicationEvent {

	private LogApi logApi;
	private XssHttpServletRequestWrapper request;

	public ApiLogEvent(LogApi logApi, XssHttpServletRequestWrapper request) {
		super(logApi);
		this.logApi = logApi;
		this.request = request;
	}

}
