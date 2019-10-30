package org.springblossom.core.pay;

import org.springblossom.core.pay.wxpay.WxPayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付统一配置
 *
 * @author guolf
 */
@Configuration
public class PayConfiguration {

	@Bean
	public WxPayClient wxPayClient() {
		WxPayClient client = new WxPayClient();
		return client;
	}
}
