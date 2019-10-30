package org.springblossom.core.schedule.feign;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springblossom.core.schedule.entity.ScheduleJobEntity;
import org.springblossom.core.schedule.serevice.ScheduleJobService;
import org.springblossom.core.tool.api.R;
import org.springblossom.core.tool.utils.StringUtil;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * 定时任务feign实现类
 *
 * @author guolf
 */
@ApiIgnore
@RestController
@AllArgsConstructor
public class ScheduleJobClientImpl implements IScheduleJobClient {

	private ScheduleJobService scheduleJobService;

	/**
	 * 创建定时任务
	 *
	 * @param entity
	 * @return
	 */
	@Override
	@PostMapping(API_PREFIX + "/createJob")
	public R createJob(@Valid @RequestBody ScheduleJobEntity entity) {
		if(StringUtil.isBlank(entity.getBusinessKey())) {
			return R.fail("businessKey不能为空");
		}
		int count = scheduleJobService.count(Wrappers.<ScheduleJobEntity>lambdaQuery().eq(ScheduleJobEntity::getBusinessKey,entity.getBusinessKey()));
		if(count > 0) {
			return R.fail("businessKey已存在");
		}
		scheduleJobService.save(entity);
		return R.status(true);
	}

	/**
	 * 更新定时任务
	 *
	 * @param entity
	 * @return
	 */
	@Override
	@PostMapping(API_PREFIX + "/updateJob")
	public R updateJobByBusinessKey(@Valid @RequestBody ScheduleJobEntity entity) {
		if(StringUtil.isBlank(entity.getBusinessKey())) {
			return R.fail("businessKey不能为空");
		}
		int count = scheduleJobService.updateByBusinessKey(entity);
		return R.status(count == 0);
	}

	/**
	 * 根据业务ID查询任务数量
	 *
	 * @param businessKey
	 * @return
	 */
	@Override
	@GetMapping(value = API_PREFIX + "/countByBusinessKey")
	public R countByBusinessKey(String businessKey) {
		return R.data(scheduleJobService.count(Wrappers.<ScheduleJobEntity>lambdaQuery().eq(ScheduleJobEntity::getBusinessKey,businessKey)));
	}

	/**
	 * 删除定时任务
	 *
	 * @param id
	 * @return
	 */
	@Override
	@PostMapping(API_PREFIX + "/deleteJob")
	public R deleteJob(@RequestParam Integer id) {
		 scheduleJobService.deleteLogic(Lists.newArrayList(id));
		 return R.status(true);
	}
}
