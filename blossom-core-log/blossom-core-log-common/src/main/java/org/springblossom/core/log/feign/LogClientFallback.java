package org.springblossom.core.log.feign;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class LogClientFallback implements ILogClient {

	/**
	 * 保存错误日志
	 *
	 * @param logUsual
	 * @return
	 */
	@Override
	public R<Boolean> saveUsualLog(LogUsual logUsual) {
		log.debug(logUsual.getLogId(), logUsual);
		return R.fail("usual log send fail");
	}

	/**
	 * 保存操作日志
	 *
	 * @param logApi
	 * @return
	 */
	@Override
	public R<Boolean> saveApiLog(LogApi logApi) {
		log.debug("接口日志", logApi);
		return R.fail("api log send fail");
	}

	/**
	 * 保存错误日志
	 *
	 * @param logError
	 * @return
	 */
	@Override
	public R<Boolean> saveErrorLog(LogError logError) {
		log.debug("logError", logError);
		return R.fail("error log send fail");
	}
}
