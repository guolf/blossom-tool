package org.springblossom.core.log.publisher;

import org.springblossom.core.log.constant.EventConstant;
import org.springblossom.core.log.event.UsualLogEvent;
import org.springblossom.core.log.model.LogUsual;
import org.springblossom.core.log.utils.LogAbstractUtil;
import org.springblossom.core.tool.utils.SpringUtil;
import org.springblossom.core.tool.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * blossom日志信息事件发送
 *
 * @author Chill
 */
public class UsualLogPublisher {

	public static void publishEvent(String level, String id, String data) {
		HttpServletRequest request = WebUtil.getRequest();
		LogUsual logUsual = new LogUsual();
		logUsual.setLogLevel(level);
		logUsual.setLogId(id);
		logUsual.setLogData(data);

		LogAbstractUtil.addRequestInfoToLog(request, logUsual);
		Map<String, Object> event = new HashMap<>(16);
		event.put(EventConstant.EVENT_LOG, logUsual);
		event.put(EventConstant.EVENT_REQUEST, request);
		SpringUtil.publishEvent(new UsualLogEvent(event));
	}

}
