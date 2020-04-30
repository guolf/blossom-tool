package org.springblossom.core.boot.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MyBatis 配置类
 *
 * @author guolf
 */
@Data
@ConfigurationProperties("blossom.mybatis")
public class BlossomMyBatisProperties {

	/**
	 * 节点ID 0-1024
	 * 单机配置多实例，高并发下IdWorker会有重复现象，通过配置节点ID避免
	 */
	private Integer nodeId;

	/**
	 * 是否启用 性能分析拦截器，用于输出每条 SQL 语句及其执行时间
	 */
	private Boolean showLog = false;
}
