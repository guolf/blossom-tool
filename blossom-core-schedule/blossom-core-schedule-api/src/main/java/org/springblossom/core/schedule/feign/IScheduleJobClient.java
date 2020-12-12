package org.springblossom.core.schedule.feign;

import org.springblossom.core.schedule.entity.ScheduleJobEntity;
import org.springblossom.core.tool.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 定时任务接口
 *
 * @author guolf
 */
@FeignClient(
	value = "blossom-schedule"
)
public interface IScheduleJobClient {

	String API_PREFIX = "/feign-client/scheduleJob";

	/**
	 * 创建定时任务
	 *
	 * @param entity
	 * @return
	 */
	@PostMapping(value = API_PREFIX + "/createJob",consumes = "application/json;charset=UTF-8")
	R createJob(@RequestBody ScheduleJobEntity entity);

	/**
	 * 更新定时任务
	 *
	 * @param entity
	 * @return
	 */
	@PostMapping(value = API_PREFIX + "/updateJob",consumes = "application/json;charset=UTF-8")
	R updateJobByBusinessKey(@RequestBody ScheduleJobEntity entity);

	/**
	 * 根据业务ID查询任务数量
	 * @param businessKey
	 * @return
	 */
	@GetMapping(value = API_PREFIX + "/countByBusinessKey")
	R countByBusinessKey(@RequestParam String businessKey);

	/**
	 * 删除定时任务
	 *
	 * @param id
	 * @return
	 */
	@PostMapping(API_PREFIX + "/deleteJob")
	R deleteJob(@RequestParam Integer id);
}
