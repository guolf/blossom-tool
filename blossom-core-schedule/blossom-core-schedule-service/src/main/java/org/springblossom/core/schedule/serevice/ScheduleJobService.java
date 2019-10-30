package org.springblossom.core.schedule.serevice;

import org.apache.ibatis.annotations.Param;
import org.springblossom.core.mp.base.BaseService;
import org.springblossom.core.schedule.entity.ScheduleJobEntity;


/**
 * 调度任务服务
 *
 * @author guolf
 */
public interface ScheduleJobService extends BaseService<ScheduleJobEntity> {

	/**
	 * 立即执行
	 */
	void run(Integer[] jobIds);

	/**
	 * 暂停运行
	 */
	void pause(Integer[] jobIds);

	/**
	 * 恢复运行
	 */
	void resume(Integer[] jobIds);

	/**
	 * 根据业务key更新
	 * @param entity
	 * @return
	 */
	int updateByBusinessKey(ScheduleJobEntity entity);
}
