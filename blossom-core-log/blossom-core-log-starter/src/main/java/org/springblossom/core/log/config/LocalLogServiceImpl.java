package org.springblossom.core.log.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.log.feign.ILogCoreService;
import org.springblossom.core.log.model.LogApi;
import org.springblossom.core.log.model.LogError;
import org.springblossom.core.log.model.LogUsual;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 默认将日志写入文件
 *
 * @author guolf
 */
@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty(prefix = "blossom.log", name = "type", havingValue = "file", matchIfMissing = true)
public class LocalLogServiceImpl implements ILogCoreService {

	/**
	 * 保存错误日志
	 *
	 * @param logUsual
	 * @return
	 */
	@Override
	public void saveUsualLog(LogUsual logUsual) {
		log.info(logUsual.getLogId() + " : " + logUsual.toString());
	}

	/**
	 * 保存操作日志
	 *
	 * @param logApi
	 * @return
	 */
	@Override
	public void saveApiLog(LogApi logApi) {
		log.debug(logApi.getTitle() + " : " + logApi.toString());
	}

	/**
	 * 保存错误日志
	 *
	 * @param logError
	 * @return
	 */
	@Override
	public void saveErrorLog(LogError logError) {
		log.error(logError.getMethod() + " : " + logError.toString());
	}
}
