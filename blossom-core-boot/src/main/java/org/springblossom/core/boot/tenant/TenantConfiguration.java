
package org.springblossom.core.boot.tenant;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 多租户配置类
 *
 * @author Chill
 */
@Configuration
@AllArgsConstructor
@AutoConfigureBefore(MybatisConfiguration.class)
@ConditionalOnProperty(value = "blossom.tenant")
@EnableConfigurationProperties(BlossomTenantProperties.class)
public class TenantConfiguration {

	/**
	 * 多租户配置类
	 */
	private final BlossomTenantProperties properties;

	/**
	 * 自定义租户处理器
	 *
	 * @return TenantHandler
	 */
	@Bean
	@ConditionalOnMissingBean(TenantHandler.class)
	public TenantHandler blossomTenantHandler() {
		return new BlossomTenantHandler(properties);
	}

	/**
	 * 自定义租户id生成器
	 *
	 * @return TenantId
	 */
	@Bean
	@ConditionalOnMissingBean(TenantId.class)
	public TenantId tenantId() {
		return new BlossomTenantId();
	}

	/**
	 * 分页插件
	 *
	 * @param tenantHandler 自定义租户处理器
	 * @return PaginationInterceptor
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor(TenantHandler tenantHandler) {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		List<ISqlParser> sqlParserList = new ArrayList<>();
		TenantSqlParser tenantSqlParser = new TenantSqlParser();
		tenantSqlParser.setTenantHandler(tenantHandler);
		sqlParserList.add(tenantSqlParser);
		paginationInterceptor.setSqlParserList(sqlParserList);
		return paginationInterceptor;
	}

}
