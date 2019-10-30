package org.springblossom.core.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springblossom.core.schedule.entity.ScheduleJobEntity;

/**
 * 任务调度接口
 *
 * @author guolf
 */
public interface ScheduleJobMapper extends BaseMapper<ScheduleJobEntity> {

	/**
	 * 根据业务key更新
	 * @param entity
	 * @return
	 */
	int updateByBusinessKey(@Param("entity") ScheduleJobEntity entity);
}
