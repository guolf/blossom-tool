package org.springblossom.core.log.service.impl;

import lombok.AllArgsConstructor;
import org.springblossom.core.log.feign.ILogCoreService;
import org.springblossom.core.log.model.LogApi;
import org.springblossom.core.log.model.LogError;
import org.springblossom.core.log.model.LogUsual;
import org.springblossom.core.log.service.ILogApiService;
import org.springblossom.core.log.service.ILogErrorService;
import org.springblossom.core.log.service.ILogUsualService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 日志服务
 *
 * @author guolf
 */
@Service
@AllArgsConstructor
@ConditionalOnProperty(prefix = "blossom.log", name = "type", havingValue = "mysql")
public class MysqlLogServiceImpl implements ILogCoreService {

	private ILogApiService logApiService;
	private ILogUsualService logUsualService;
	private ILogErrorService logErrorService;

	/**
	 * 保存通用日志
	 *
	 * @param log
	 * @return
	 */
	@Override
	public void saveUsualLog(LogUsual log) {
		logUsualService.save(log);
	}

	/**
	 * 保存操作日志
	 *
	 * @param log
	 * @return
	 */
	@Override
	public void saveApiLog(LogApi log) {
		logApiService.save(log);
	}

	/**
	 * 保存错误日志
	 *
	 * @param log
	 * @return
	 */
	@Override
	public void saveErrorLog(LogError log) {
		logErrorService.save(log);
	}
}
