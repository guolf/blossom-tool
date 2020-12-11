
package org.springblossom.core.boot.tenant;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * blossom租户id生成器
 *
 * @author Chill
 */
public class BlossomTenantId implements TenantId {
	@Override
	public String generate() {
		return IdWorker.getIdStr();
	}
}
