package org.springblossom.core.schedule.serevice.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springblossom.core.mp.base.BaseEntity;
import org.springblossom.core.mp.base.BaseServiceImpl;
import org.springblossom.core.schedule.constant.ScheduleConstant;
import org.springblossom.core.schedule.entity.ScheduleJobEntity;
import org.springblossom.core.schedule.mapper.ScheduleJobMapper;
import org.springblossom.core.schedule.serevice.ScheduleJobService;
import org.springblossom.core.schedule.utils.ScheduleUtils;
import org.springblossom.core.tool.constant.BlossomConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 调度任务服务实现类
 *
 * @author guolf
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ScheduleJobServiceImpl extends BaseServiceImpl<ScheduleJobMapper, ScheduleJobEntity> implements ScheduleJobService {

	@Autowired
	private Scheduler scheduler;

	/**
	 * 项目启动时，初始化定时器
	 */
	@PostConstruct
	public void init() {
		List<ScheduleJobEntity> scheduleJobList = this.list(Wrappers.<ScheduleJobEntity>lambdaQuery()
			.eq(ScheduleJobEntity::getIsDeleted, BlossomConstant.DB_NOT_DELETED));
		for (ScheduleJobEntity scheduleJob : scheduleJobList) {
			CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getId());
			//如果不存在，则创建
			if (cronTrigger == null) {
				ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
			} else {
				ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
			}
		}
	}

	@Override
	public boolean save(ScheduleJobEntity entity) {
		super.save(entity);
		ScheduleUtils.createScheduleJob(scheduler, entity);
		return true;
	}

	@Override
	public boolean updateById(ScheduleJobEntity entity) {
		ScheduleUtils.updateScheduleJob(scheduler, entity);
		return super.updateById(entity);
	}

	@Override
	public boolean saveOrUpdate(ScheduleJobEntity entity) {
		if (entity.getId() == null) {
			// 新增
			save(entity);
		} else {
			updateById(entity);
		}
		return true;
	}

	@Override
	public boolean deleteLogic(List<Integer> ids) {
		for (Integer id : ids) {
			ScheduleUtils.deleteScheduleJob(scheduler, id);
		}
		baseMapper.deleteBatchIds(ids);
		return true;
	}


	/**
	 * 立即执行
	 *
	 * @param ids
	 */
	@Override
	public void run(Integer[] ids) {
		for (Integer id : ids) {
			ScheduleUtils.run(scheduler, getById(id));
		}
	}

	/**
	 * 暂停运行
	 *
	 * @param ids
	 */
	@Override
	public void pause(Integer[] ids) {
		for (Integer id : ids) {
			ScheduleUtils.pauseJob(scheduler, id);

			// 更新状态
			ScheduleJobEntity entity = getById(id);
			entity.setStatus(ScheduleConstant.ScheduleStatus.PAUSE.getValue());
			baseMapper.updateById(entity);
		}
	}

	/**
	 * 恢复运行
	 *
	 * @param ids
	 */
	@Override
	public void resume(Integer[] ids) {
		for (Integer id : ids) {
			ScheduleUtils.resumeJob(scheduler, id);

			// 更新状态
			ScheduleJobEntity entity = getById(id);
			entity.setStatus(ScheduleConstant.ScheduleStatus.NORMAL.getValue());
			baseMapper.updateById(entity);
		}
	}

	/**
	 * 根据业务key更新
	 *
	 * @param entity
	 * @return
	 */
	@Override
	public int updateByBusinessKey(ScheduleJobEntity entity) {
		ScheduleJobEntity job = getOne(Wrappers.<ScheduleJobEntity>lambdaQuery().eq(ScheduleJobEntity::getBusinessKey, entity.getBusinessKey()));
		if (job == null) {
			return 0;
		}
		entity.setId(job.getId());
		ScheduleUtils.updateScheduleJob(scheduler, entity);
		return baseMapper.updateByBusinessKey(entity);
	}
}
