package org.springblossom.core.pay.wxpay;

import com.google.common.collect.Maps;
import lombok.Data;
import org.springblossom.core.pay.wxpay.config.WxPayConfig;
import org.springblossom.core.pay.wxpay.constant.SignType;
import org.springblossom.core.pay.wxpay.dto.UnifiedOrderDTO;
import org.springblossom.core.pay.wxpay.kit.WxPayKit;
import org.springblossom.core.tool.utils.Func;
import org.springblossom.core.tool.utils.RandomType;
import org.springblossom.core.tool.utils.StringUtil;
import org.springblossom.core.tool.utils.WebUtil;

import java.util.Map;

/**
 * 微信支付
 *
 * @author guolf
 */
@Data
public class WxPayClient {

	private WxPayConfig payConfig;

	/**
	 * 微信 统一下单
	 *
	 * @param dto
	 */
	public String commitWxOrder(UnifiedOrderDTO dto) {
		Map map = Maps.newHashMap();
		map.put("appid", payConfig.getAppId());
		map.put("mch_id", payConfig.getMchId());
		map.put("body", dto.getBody());
		map.put("detail",dto.getDetail());
		map.put("attach",dto.getAttach());
		map.put("out_trade_no",dto.getTradeNo());
		map.put("total_fee",dto.getTotalFee());
		map.put("spbill_create_ip", WebUtil.getIP());
		map.put("notify_url",payConfig.getDomain().concat(dto.getNotifyUrl()));
		map.put("trade_type",dto.getTradeType());
		if(Func.isNotEmpty(dto.getTimeExpire())) {
			map.put("time_expire",dto.getTimeExpire());
		}
		if(Func.isNotEmpty(dto.getOpenid())) {
			map.put("openid",dto.getOpenid());
		}

		return WxPayApi.pushOrder(payConfig.getIsSandbox(), WxPayKit.buildSign(map, payConfig.getPartnerKey(), SignType.MD5));
	}
}
