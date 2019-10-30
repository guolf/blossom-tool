package org.springblossom.core.schedule.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.springblossom.core.schedule.entity.ScheduleJobEntity;
import org.springblossom.core.schedule.entity.ScheduleJobLogEntity;
import org.springblossom.core.schedule.serevice.ScheduleJobLogService;
import org.springblossom.core.tool.utils.SpringUtil;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * @author guolf
 */
@Slf4j
public class ScheduleJob extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context) {
		ScheduleJobEntity scheduleJob = (ScheduleJobEntity) context.getMergedJobDataMap()
			.get(ScheduleJobEntity.JOB_PARAM_KEY);

		//获取spring bean
		ScheduleJobLogService scheduleJobLogService = SpringUtil.getBean(ScheduleJobLogService.class);

		//数据库保存执行记录
		ScheduleJobLogEntity scheduleJobLogEntity = new ScheduleJobLogEntity();
		scheduleJobLogEntity.setJobId(scheduleJob.getId());
		scheduleJobLogEntity.setBeanName(scheduleJob.getBeanName());
		scheduleJobLogEntity.setParams(scheduleJob.getParams());
		scheduleJobLogEntity.setCreateTime(LocalDateTime.now());

		//任务开始时间
		long startTime = System.currentTimeMillis();
		try {
			String result = null;
			//执行任务
			log.debug("任务准备执行，任务ID：{}", scheduleJob.getId());

			Object target = SpringUtil.getBean(scheduleJob.getBeanName());
			Method method = target.getClass().getDeclaredMethod("run", String.class);
			Class reType = method.getReturnType();
			if (reType.getClass().isInstance(String.class)){
				result = (String) method.invoke(target, scheduleJob.getParams());
			} else {
				method.invoke(target, scheduleJob.getParams());
			}

			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			scheduleJobLogEntity.setUpdateTime(LocalDateTime.now());
			scheduleJobLogEntity.setTimes((int) times);
			//任务状态    0：成功    1：失败
			scheduleJobLogEntity.setStatus(0);
			scheduleJobLogEntity.setError(result);
			log.debug("任务执行完毕，任务ID：{}  总共耗时：{}毫秒", scheduleJob.getId(), times);
		} catch (Exception e) {
			log.error("任务执行失败，任务ID：" + scheduleJob.getId(), e);

			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			scheduleJobLogEntity.setTimes((int) times);
			scheduleJobLogEntity.setUpdateTime(LocalDateTime.now());
			//任务状态    0：成功    1：失败
			scheduleJobLogEntity.setStatus(1);
			scheduleJobLogEntity.setError(StringUtils.substring(e.toString(), 0, 2000));
		} finally {
			scheduleJobLogService.save(scheduleJobLogEntity);
		}
	}

}
