package org.springblossom.core.pay.wxpay.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信支付 数据传输对象
 *
 * @author guolf
 */
@Data
public class UnifiedOrderDTO {

	@ApiModelProperty(value = "商品描述",required = true)
	private String body;

	@ApiModelProperty(value = "商品详情",required = false)
	private String detail;

	@ApiModelProperty(value = "附加数据",notes = "在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据")
	private String attach;

	@ApiModelProperty(value = "商户订单号",notes = "商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一",required = true)
	private String tradeNo;

	@ApiModelProperty(value = "总金额",notes = "订单总金额，单位：分",required = true)
	private String totalFee;

	@ApiModelProperty(value = "订单失效时间",notes = "格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010")
	private String timeExpire;

	@ApiModelProperty(value = "通知地址",notes = "接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数",required = true)
	private String notifyUrl;

	@ApiModelProperty(value = "交易类型",required = true,notes = "JSAPI - JSAPI支付, NATIVE - Native支付, APP - APP支付")
	private String tradeType;

	@ApiModelProperty(value = "用户标识",notes = "tradeType为JSAPI时，必传")
	private String openid;
}
