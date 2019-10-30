package org.springblossom.core.log.feign;

import org.springblossom.core.log.model.LogApi;
import org.springblossom.core.log.model.LogError;
import org.springblossom.core.log.model.LogUsual;
import org.springblossom.core.tool.api.R;
import org.springframework.stereotype.Component;

/**
 * 日志异常回调
 *
 * @author guolf
 */
@Component
public class LogClientFallback implements ILogClient {

	/**
	 * 保存错误日志
	 *
	 * @param log
	 * @return
	 */
	@Override
	public R<Boolean> saveUsualLog(LogUsual log) {
		return R.fail("usual log send fail");
	}

	/**
	 * 保存操作日志
	 *
	 * @param log
	 * @return
	 */
	@Override
	public R<Boolean> saveApiLog(LogApi log) {
		return R.fail("api log send fail");
	}

	/**
	 * 保存错误日志
	 *
	 * @param log
	 * @return
	 */
	@Override
	public R<Boolean> saveErrorLog(LogError log) {
		return R.fail("error log send fail");
	}
}
