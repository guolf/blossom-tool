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

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springblossom.core.boot.props.BlossomMyBatisProperties;
import org.springblossom.core.mp.BlossomMetaObjectHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * mybatisplus 配置
 *
 * @author Chill
 */
@Configuration
@MapperScan("org.springblossom.**.mapper.**")
@EnableConfigurationProperties(BlossomMyBatisProperties.class)
public class MybatisPlusConfiguration {

	@Autowired
	private BlossomMyBatisProperties myBatisProperties;

	/**
	 * 分页拦截器
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(PaginationInterceptor.class)
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

	/**
	 * 元对象字段填充控制器抽象类，实现公共字段自动写入
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(MetaObjectHandler.class)
	public MetaObjectHandler metaObjectHandler() {
		return new BlossomMetaObjectHandler();
	}

	/**
	 * SQL 逻辑删除注入器
	 *
	 * @return
	 */
	@Bean
	public LogicSqlInjector logicSqlInjector() {
		return new LogicSqlInjector();
	}

	/**
	 * SQL执行效率插件
	 *
	 * @return PerformanceInterceptor
	 */
	@Bean
	@ConditionalOnProperty(prefix = "blossom.mybatis", name = "showLog", havingValue = "true")
	public PerformanceInterceptor performanceInterceptor() {
		return new PerformanceInterceptor();
	}

	@PostConstruct
	@ConditionalOnProperty(prefix = "blossom.mybatis", name = "nodeId")
	public void initIdWorker() {
		long dataCenterId = (myBatisProperties.getNodeId() >> 5) & 0b00011111;
		long workerId = (myBatisProperties.getNodeId() & 0b00011111);
		IdWorker.initSequence(workerId, dataCenterId);
	}
}

