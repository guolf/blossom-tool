package org.springblossom.core.log.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.log.annotation.ApiLog;
import org.springblossom.core.log.constant.EventConstant;
import org.springblossom.core.log.event.ApiLogEvent;
import org.springblossom.core.log.model.LogApi;
import org.springblossom.core.log.utils.LogAbstractUtil;
import org.springblossom.core.tool.constant.BlossomConstant;
import org.springblossom.core.tool.utils.SpringUtil;
import org.springblossom.core.tool.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * API日志信息事件发送
 *
 * @author Chill
 */
@Slf4j
public class ApiLogPublisher {

	/**
	 * API请求成功
	 *
	 * @param methodName
	 * @param methodClass
	 * @param apiLog
	 * @param time
	 */
	public static void publishEvent(String methodName, String methodClass, ApiLog apiLog, long time) {
		HttpServletRequest request = WebUtil.getRequest();

		LogApi logApi = new LogApi();
		logApi.setType(BlossomConstant.LOG_NORMAL_TYPE);
		logApi.setTitle(apiLog.value());
		logApi.setTime(String.valueOf(time));
		logApi.setMethodClass(methodClass);
		logApi.setMethodName(methodName);

		LogAbstractUtil.addRequestInfoToLog(request, logApi);
		Map<String, Object> event = new HashMap<>(16);
		event.put(EventConstant.EVENT_LOG, logApi);
		SpringUtil.publishEvent(new ApiLogEvent(event));
	}
}
