package org.springblossom.core.log.feign;

import org.springblossom.core.log.model.LogApi;
import org.springblossom.core.log.model.LogError;
import org.springblossom.core.log.model.LogUsual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * 日志服务默认实现类
 *
 * @author guolf
 */
@Service
@ConditionalOnMissingBean(ILogCoreService.class)
public class LogCoreServiceImpl implements ILogCoreService {

	@Autowired
	private ILogClient logClient;

	/**
	 * 保存错误日志
	 *
	 * @param log
	 * @return
	 */
	@Override
	public void saveUsualLog(LogUsual log) {
		logClient.saveUsualLog(log);
	}

	/**
	 * 保存操作日志
	 *
	 * @param log
	 * @return
	 */
	@Override
	public void saveApiLog(LogApi log) {
		logClient.saveApiLog(log);
	}

	/**
	 * 保存错误日志
	 *
	 * @param log
	 * @return
	 */
	@Override
	public void saveErrorLog(LogError log) {
		logClient.saveErrorLog(log);
	}
}
