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

package org.springblossom.core.log.config;

import lombok.AllArgsConstructor;
import org.springblossom.core.log.aspect.ApiLogAspect;
import org.springblossom.core.log.event.ApiLogListener;
import org.springblossom.core.log.event.UsualLogListener;
import org.springblossom.core.log.event.ErrorLogListener;
import org.springblossom.core.log.logger.BlossomLogger;
import org.springblossom.core.launch.props.BlossomProperties;
import org.springblossom.core.launch.server.ServerInfo;
import org.springblossom.core.log.feign.ILogClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志工具自动配置
 *
 * @author Chill
 */
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class BlossomLogToolAutoConfiguration {

	private final ILogClient logService;
	private final ServerInfo serverInfo;
	private final BlossomProperties blossomProperties;

	@Bean
	@ConditionalOnMissingBean
	public ApiLogAspect apiLogAspect() {
		return new ApiLogAspect();
	}

	@Bean
	@ConditionalOnMissingBean
	public BlossomLogger blossomLogger() {
		return new BlossomLogger();
	}

	@Bean
	@ConditionalOnMissingBean(name = "apiLogListener")
	public ApiLogListener apiLogListener() {
		return new ApiLogListener(logService, serverInfo, blossomProperties);
	}

	@Bean
	@ConditionalOnMissingBean(name = "errorLogListener")
	public ErrorLogListener errorLogListener() {
		return new ErrorLogListener(logService, serverInfo, blossomProperties);
	}

	@Bean
	@ConditionalOnMissingBean(name = "usualLogListener")
	public UsualLogListener usualLogListener() {
		return new UsualLogListener(logService, serverInfo, blossomProperties);
	}

}
