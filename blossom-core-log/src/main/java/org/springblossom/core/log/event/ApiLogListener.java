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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.launch.props.BlossomProperties;
import org.springblossom.core.launch.server.ServerInfo;
import org.springblossom.core.log.feign.ILogClient;
import org.springblossom.core.log.model.LogApi;
import org.springblossom.core.tool.support.xss.XssHttpServletRequestWrapper;
import org.springblossom.core.tool.utils.UrlUtil;
import org.springblossom.core.tool.utils.WebUtil;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.concurrent.*;


/**
 * 异步监听日志事件
 *
 * @author Chill
 */
@Slf4j
@AllArgsConstructor
public class ApiLogListener {

	private final ILogClient logService;
	private final ServerInfo serverInfo;
	private final BlossomProperties blossomProperties;

	@Order
	@Async
	@EventListener(ApiLogEvent.class)
	public void saveApiLog(ApiLogEvent event) {
		LogApi logApi = event.getLogApi();
		XssHttpServletRequestWrapper request = event.getRequest();
		logApi.setServiceId(blossomProperties.getName());
		logApi.setServerHost(serverInfo.getHostName());
		logApi.setServerIp(serverInfo.getIpWithPort());
		logApi.setEnv(blossomProperties.getEnv());
		logApi.setRemoteIp(WebUtil.getIP(request));
		logApi.setUserAgent(request.getHeader(WebUtil.USER_AGENT_HEADER));
		logApi.setRequestUri(UrlUtil.getPath(request.getRequestURI()));
		logApi.setMethod(request.getMethod());
		logApi.setParams(HtmlUtils.htmlUnescape(WebUtil.getRequestParamString(request)));
		logApi.setCreateTime(LocalDateTime.now());
		logService.saveApiLog(logApi);
	}
}
