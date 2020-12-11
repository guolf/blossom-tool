package org.springblossom.core.log.event;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.launch.props.BlossomProperties;
import org.springblossom.core.launch.server.ServerInfo;
import org.springblossom.core.log.constant.EventConstant;
import org.springblossom.core.log.feign.ILogCoreService;
import org.springblossom.core.log.model.LogError;
import org.springblossom.core.log.utils.LogAbstractUtil;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 异步监听错误日志事件
 *
 * @author Chill
 */
@Slf4j
@AllArgsConstructor
public class ErrorLogListener {

	private final ILogCoreService logService;
	private final ServerInfo serverInfo;
	private final BlossomProperties blossomProperties;

	@Async
	@Order
	@EventListener(ErrorLogEvent.class)
	public void saveErrorLog(ErrorLogEvent event) {
		Map<String, Object> source = (Map<String, Object>) event.getSource();
		LogError logError = (LogError) source.get(EventConstant.EVENT_LOG);
		LogAbstractUtil.addOtherInfoToLog(logError, blossomProperties, serverInfo);
		logService.saveErrorLog(logError);
	}

}
