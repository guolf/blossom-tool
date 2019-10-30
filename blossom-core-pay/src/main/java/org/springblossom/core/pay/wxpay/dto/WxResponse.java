package org.springblossom.core.pay.wxpay.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信返回数据
 *
 * @author guolf
 */
@Data
public class WxResponse {

	@ApiModelProperty(value = "返回状态码",notes = "SUCCESS/FAIL")
	private String returnCode;

	@ApiModelProperty("返回消息")
	private String returnMsg;

	@ApiModelProperty("应用APPID")
	private String appId;

	@ApiModelProperty("商户号")
	private String mchId;

	@ApiModelProperty("随机字符串")
	private String nonceStr;

	@ApiModelProperty("签名")
	private String sign;

	@ApiModelProperty(value = "业务结果",notes = "SUCCESS/FAIL")
	private String resultCode;

	@ApiModelProperty("错误代码")
	private String errCode;

	@ApiModelProperty("错误代码描述")
	private String errCodeDes;

	@ApiModelProperty(value = "预支付交易会话标识",notes = "微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时")
	private String prepayId;

	@ApiModelProperty("交易类型")
	private String tradeType;

}
