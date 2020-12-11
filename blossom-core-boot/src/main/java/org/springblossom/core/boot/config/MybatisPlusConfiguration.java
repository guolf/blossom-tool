
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

