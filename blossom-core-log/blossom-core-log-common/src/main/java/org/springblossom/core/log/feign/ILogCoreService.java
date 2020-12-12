package org.springblossom.core.log.feign;

import org.springblossom.core.log.model.LogApi;
import org.springblossom.core.log.model.LogError;
import org.springblossom.core.log.model.LogUsual;

/**
 * 日志服务接口
 * boot应用实现该接口进行入库
 * cloud应用通过feign调用log应用入库
 *
 * @author guolf
 */
public interface ILogCoreService {

	/**
	 * 保存错误日志
	 *
	 * @param log
	 * @return
	 */
	void saveUsualLog(LogUsual log);

	/**
	 * 保存操作日志
	 *
	 * @param log
	 * @return
	 */
	void saveApiLog(LogApi log);

	/**
	 * 保存错误日志
	 *
	 * @param log
	 * @return
	 */
	void saveErrorLog(LogError log);
}
