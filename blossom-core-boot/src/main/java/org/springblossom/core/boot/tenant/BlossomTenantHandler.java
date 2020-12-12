
package org.springblossom.core.boot.tenant;

import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springblossom.core.secure.utils.SecureUtil;
import org.springblossom.core.tool.utils.StringUtil;

/**
 * 租户信息处理器
 *
 * @author Chill
 */
@Slf4j
@AllArgsConstructor
public class BlossomTenantHandler implements TenantHandler {

	private final BlossomTenantProperties properties;

	/**
	 * 获取租户编号
	 *
	 * @return 租户编号
	 */
	@Override
	public Expression getTenantId() {
		return new StringValue(SecureUtil.getTenantCode());
	}

	/**
	 * 获取租户字段名称
	 *
	 * @return 租户字段名称
	 */
	@Override
	public String getTenantIdColumn() {
		return properties.getColumn();
	}

	/**
	 * 过滤租户表
	 *
	 * @param tableName 表名
	 * @return 是否进行过滤
	 */
	@Override
	public boolean doTableFilter(String tableName) {
		return !(
			(
				(properties.getTables().size() > 0 && properties.getTables().contains(tableName))
					|| properties.getBlossomTables().contains(tableName)
			)
				&& StringUtil.isNotBlank(SecureUtil.getTenantCode())
		);
	}
}
