package org.springblossom.core.schedule.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 定时任务日志
 *
 * @author guolf
 */
@Data
@TableName("t_schedule_job_log")
public class ScheduleJobLogEntity implements Serializable {


	private Integer id;

	/**
	 * 任务id
	 */
	private Integer jobId;

	/**
	 * spring bean名称
	 */
	private String beanName;

	/**
	 * 参数
	 */
	private String params;

	/**
	 * 任务状态    0：成功    1：失败
	 */
	private Integer status;

	/**
	 * 失败信息
	 */
	private String error;

	/**
	 * 耗时(单位：毫秒)
	 */
	private Integer times;

	/**
	 * 任务开始时间
	 */
	private LocalDateTime createTime;

	/**
	 * 任务结束时间
	 */
	private LocalDateTime updateTime;

}
