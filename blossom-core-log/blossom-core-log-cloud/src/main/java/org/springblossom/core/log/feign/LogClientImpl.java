package org.springblossom.core.log.feign;

import lombok.AllArgsConstructor;
import org.springblossom.core.log.model.LogApi;
import org.springblossom.core.log.model.LogError;
import org.springblossom.core.log.model.LogUsual;
import org.springblossom.core.log.service.ILogApiService;
import org.springblossom.core.log.service.ILogErrorService;
import org.springblossom.core.log.service.ILogUsualService;
import org.springblossom.core.tool.api.R;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

/**
 * 日志服务Feign实现类
 *
 * @author guolf
 */
@RestController
@AllArgsConstructor
@ConditionalOnProperty(prefix = "blossom", name = "type", havingValue = "cloud")
public class LogClientImpl implements ILogClient {

	ILogUsualService usualLogService;
	ILogApiService apiLogService;
	ILogErrorService errorLogService;

	@Override
	@PostMapping(API_PREFIX + "/saveUsualLog")
	public R<Boolean> saveUsualLog(@RequestBody LogUsual log) {
		log.setParams(HtmlUtils.htmlUnescape(log.getParams()));
		return R.data(usualLogService.save(log));
	}

	@Override
	@PostMapping(API_PREFIX + "/saveApiLog")
	public R<Boolean> saveApiLog(@RequestBody LogApi log) {
		log.setParams(HtmlUtils.htmlUnescape(log.getParams()));
		return R.data(apiLogService.save(log));
	}

	@Override
	@PostMapping(API_PREFIX + "/saveErrorLog")
	public R<Boolean> saveErrorLog(@RequestBody LogError log) {
		log.setParams(HtmlUtils.htmlUnescape(log.getParams()));
		return R.data(errorLogService.save(log));
	}
}
