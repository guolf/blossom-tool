package org.springblossom.core.pay.wxpay.kit;

import org.springblossom.core.pay.wxpay.constant.SignType;
import org.springblossom.core.tool.utils.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 微信支付工具类
 *
 * @author guolf
 */
public class WxPayKit {
	private static final String FIELD_SIGN = "sign";
	private static final String FIELD_SIGN_TYPE = "sign_type";

	public static String hmacSHA256(String data, String key) {
		return null;
	}

	public static String md5(String data) {
		return DigestUtil.md5Hex(data);
	}


	public static String generateStr() {
		return StringUtil.random(18);
	}

	/**
	 * 组装签名的字段
	 *
	 * @param params     参数
	 * @param urlEncoder 是否urlEncoder
	 * @return {String}
	 */
	public static String packageSign(Map<String, String> params, boolean urlEncoder) {
		// 先将参数以其参数名的字典序升序进行排序
		TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> param : sortedParams.entrySet()) {
			String value = param.getValue();
			if (Func.isEmpty(value)) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				sb.append("&");
			}
			sb.append(param.getKey()).append("=");
			if (urlEncoder) {
				try {
					value = urlEncode(value);
				} catch (UnsupportedEncodingException e) {
				}
			}
			sb.append(value);
		}
		return sb.toString();
	}


	public static String urlEncode(String src) throws UnsupportedEncodingException {
		return URLEncoder.encode(src, Charsets.UTF_8_NAME).replace("+", "%20");
	}

	/**
	 * 生成签名
	 *
	 * @param params     需要签名的参数
	 * @param partnerKey 密钥
	 * @param signType   签名类型
	 * @return 签名后的数据
	 */
	public static String createSign(Map<String, String> params, String partnerKey, SignType signType) {
		if (signType == null) {
			signType = SignType.MD5;
		}
		// 生成签名前先去除sign
		params.remove(FIELD_SIGN);
		String stringA = packageSign(params, false);
		String stringSignTemp = stringA + "&key=" + partnerKey;
		if (signType == SignType.MD5) {
			return md5(stringSignTemp).toUpperCase();
		} else {
			return hmacSHA256(stringSignTemp, partnerKey).toUpperCase();
		}
	}

	/**
	 * 构建签名
	 *
	 * @param params     需要签名的参数
	 * @param partnerKey 密钥
	 * @param signType   签名类型
	 * @return <@link Map<String, String>>  签名后的
	 */
	public static Map<String, String> buildSign(Map<String, String> params, String partnerKey, SignType signType) {
		if (signType == null) {
			signType = SignType.MD5;
		}
		params.put("nonce_str", WxPayKit.generateStr());
		params.put(FIELD_SIGN_TYPE, signType.getType());
		String sign = createSign(params, partnerKey, signType);
		params.put(FIELD_SIGN, sign);
		return params;
	}

	/**
	 * 微信下单 map to xml
	 *
	 * @param params Map 参数
	 * @return xml 字符串
	 */
	public static String toXml(Map<String, String> params) {
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>");
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			// 略过空值
			if (Func.isEmpty(value)) {
				continue;
			}
			xml.append("<").append(key).append(">");
			xml.append(entry.getValue());
			xml.append("</").append(key).append(">");
		}
		xml.append("</xml>");
		return xml.toString();
	}

	/**
	 * 针对支付的 xml，没有嵌套节点的简单处理
	 *
	 * @param xmlStr xml 字符串
	 * @return <@link Map<String, String>>
	 */
	public static Map<String, String> xmlToMap(String xmlStr) {
		XmlUtil xmlHelper = XmlUtil.of(xmlStr);
		return xmlHelper.toMap();
	}

	/**
	 * 替换url中的参数
	 *
	 * @param str   原始字符串
	 * @param regex 表达式
	 * @param args  替换字符串
	 * @return {String}
	 */
	public static String replace(String str, String regex, String... args) {
		for (String arg : args) {
			str = str.replaceFirst(regex, arg);
		}
		return str;
	}

	/**
	 * 判断接口返回的 code
	 *
	 * @param codeValue code 值
	 * @return 是否是 SUCCESS
	 */
	public static boolean codeIsOK(String codeValue) {
		return Func.isNotEmpty(codeValue) && "SUCCESS".equals(codeValue);
	}

	/**
	 * 预付订单再次签名
	 *
	 * @param prepay_id  预付订单号
	 * @param appId      应用编号
	 * @param partnerKey API Key
	 * @return {@link Map<String,String>} 再次签名后的 Map
	 */
	public static Map<String, String> prepayIdCreateSign(String prepay_id, String appId, String partnerKey) {
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appId", appId);
		packageParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
		packageParams.put("nonceStr", String.valueOf(System.currentTimeMillis()));
		packageParams.put("package", "prepay_id=" + prepay_id);
		packageParams.put("signType", "MD5");
		String packageSign = WxPayKit.createSign(packageParams, partnerKey, SignType.MD5);
		packageParams.put("paySign", packageSign);
		return packageParams;
	}


}
