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
package org.springblossom.core.boot.config;

import lombok.AllArgsConstructor;
import org.springblossom.core.boot.props.BlossomAsyncProperties;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步处理
 *
 * @author Chill
 */
@Configuration
@EnableAsync
@EnableScheduling
@AllArgsConstructor
@EnableConfigurationProperties({
	BlossomAsyncProperties.class
})
public class BlossomExecutorConfiguration extends AsyncConfigurerSupport {

	private final BlossomAsyncProperties blossomAsyncProperties;

	@Override
	@Bean(name = "taskExecutor")
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(blossomAsyncProperties.getCorePoolSize());
		executor.setMaxPoolSize(blossomAsyncProperties.getMaxPoolSize());
		executor.setQueueCapacity(blossomAsyncProperties.getQueueCapacity());
		executor.setKeepAliveSeconds(blossomAsyncProperties.getKeepAliveSeconds());
		executor.setThreadNamePrefix("async-executor-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SimpleAsyncUncaughtExceptionHandler();
	}

}
