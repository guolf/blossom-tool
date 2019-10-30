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
import org.springblossom.core.log.model.LogUsual;
import org.springblossom.core.tool.utils.UrlUtil;
import org.springblossom.core.tool.utils.WebUtil;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 异步监听日志事件
 *
 * @author Chill
 */
@Slf4j
@AllArgsConstructor
public class UsualLogListener {

	private final ILogClient logService;
	private final ServerInfo serverInfo;
	private final BlossomProperties blossomProperties;

	@Async
	@Order
	@EventListener(UsualLogEvent.class)
	public void saveUsualLog(UsualLogEvent event) {
		LogUsual logUsual = event.getLogUsual();
		HttpServletRequest request = event.getRequest();
		if(request != null) {
			logUsual.setRequestUri(UrlUtil.getPath(request.getRequestURI()));
			logUsual.setUserAgent(request.getHeader(WebUtil.USER_AGENT_HEADER));
			logUsual.setMethod(request.getMethod());
			logUsual.setParams(WebUtil.getRequestParamString(request));
		}
		logUsual.setServerHost(serverInfo.getHostName());
		logUsual.setServiceId(blossomProperties.getName());
		logUsual.setEnv(blossomProperties.getEnv());
		logUsual.setServerIp(serverInfo.getIpWithPort());
		logUsual.setCreateTime(LocalDateTime.now());
		logService.saveUsualLog(logUsual);
	}

}
