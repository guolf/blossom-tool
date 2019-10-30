package org.springblossom.core.schedule.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务,演示demo
 *
 * @author guolf
 */
@Component
@Slf4j
public class TestTask implements ITask {

	@Override
	public void run(String params) {
		log.debug("TestTask定时任务正在执行，参数为：{}", params);
	}
}
