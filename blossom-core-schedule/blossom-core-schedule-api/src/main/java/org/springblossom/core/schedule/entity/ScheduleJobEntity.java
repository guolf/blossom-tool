package org.springblossom.core.schedule.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblossom.core.mp.base.BaseEntity;

import javax.validation.constraints.NotBlank;

/**
 * 定时任务
 *
 * @author guolf
 */
@Data
@TableName("t_schedule_job")
public class ScheduleJobEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 任务调度参数key
	 */
	public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

	/**
	 * spring bean名称
	 */
	@NotBlank(message = "bean名称不能为空")
	private String beanName;

	/**
	 * 参数
	 */
	@ApiModelProperty("参数")
	private String params;

	/**
	 * cron表达式
	 */
	@NotBlank(message = "cron表达式不能为空")
	private String cronExpression;

	@ApiModelProperty("业务key")
	private String businessKey;

	/**
	 * 任务状态
	 */
	@ApiModelProperty("任务状态: 0正常，1暂停")
	private Integer status;

	/**
	 * 备注
	 */
	private String remark;

}
