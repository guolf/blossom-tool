package org.springblossom.core.schedule.serevice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblossom.core.mp.base.BaseServiceImpl;
import org.springblossom.core.schedule.entity.ScheduleJobLogEntity;
import org.springblossom.core.schedule.mapper.ScheduleJobLogMapper;
import org.springblossom.core.schedule.serevice.ScheduleJobLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogMapper,ScheduleJobLogEntity> implements ScheduleJobLogService {


	@Override
	public boolean deleteLogic(List<Integer> ids) {
		return false;
	}
}
