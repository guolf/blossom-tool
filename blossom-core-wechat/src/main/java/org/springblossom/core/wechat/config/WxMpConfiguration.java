package org.springblossom.core.wechat.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;

/**
 * 微信配置
 *
 * @author guolf
 */
public class WxMpConfiguration {

	public WxMpService wxMpService() {
		WxMpService wxMpService = new WxMpServiceImpl();

		return wxMpService;
	}
}
