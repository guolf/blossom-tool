
package org.springblossom.core.boot.tenant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 多租户配置
 *
 * @author Chill
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "blossom.tenant")
public class BlossomTenantProperties {

	/**
	 * 多租户字段名称
	 */
	private String column = "tenant_code";

	/**
	 * 多租户数据表
	 */
	private List<String> tables = new ArrayList<>();

	/**
	 * 多租户系统数据表
	 */
	private List<String> blossomTables = Arrays.asList("t_notice", "t_log_api", "t_log_error", "t_log_usual");
}
