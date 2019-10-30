package org.springblossom.core.launch.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 许可证
 *
 * @author guolf
 */
@Data
public class License implements Serializable {

	/**
	 * 机器码
	 */
	private String machineCode;

	/**
	 * 企业名称
	 */
	private String companyName;

	/**
	 * 许可到期时间
	 */
	private LocalDate expireAt;

	/**
	 * License生成时间
	 */
	private LocalDate issueDate;

	/**
	 * 签名
	 */
	private String signature;

}
