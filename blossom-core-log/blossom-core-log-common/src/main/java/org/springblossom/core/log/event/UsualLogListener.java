package org.springblossom.core.log.event;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.launch.props.BlossomProperties;
import org.springblossom.core.launch.server.ServerInfo;
import org.springblossom.core.log.constant.EventConstant;
import org.springblossom.core.log.feign.ILogCoreService;
import org.springblossom.core.log.model.LogUsual;
import org.springblossom.core.log.utils.LogAbstractUtil;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 异步监听日志事件
 *
 * @author Chill
 */
@Slf4j
@AllArgsConstructor
public class UsualLogListener {

	private final ILogCoreService logService;
	private final ServerInfo serverInfo;
	private final BlossomProperties blossomProperties;

	@Async
	@Order
	@EventListener(UsualLogEvent.class)
	public void saveUsualLog(UsualLogEvent event) {
		Map<String, Object> source = (Map<String, Object>) event.getSource();
		LogUsual logUsual = (LogUsual) source.get(EventConstant.EVENT_LOG);
		LogAbstractUtil.addOtherInfoToLog(logUsual, blossomProperties, serverInfo);
		logService.saveUsualLog(logUsual);
	}

}
