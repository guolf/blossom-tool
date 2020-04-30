package org.springblossom.core.launch.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springblossom.core.launch.entity.License;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * License 生成工具
 *
 * @author guolf
 */
public class LicenseGenerator {

	/**
	 * 公钥
	 */
	private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZ23LmlcpOGdwP/fggd8FvgV9JpHyeGZsP3crQCKQ2q+xCDQ6Cin9t0MQzsCleb4NHhdfumK9AuTXJCXiJIYXMCRW6e9TZkbuQ6DktZc+ur/ilYLkJ1bGOFjJK6tg34E+IjfQpo03qHI5OdMUXJc7ovciIPOvbNHKFELwaxZMmqwIDAQAB";

	/**
	 * RSA算法
	 * 公钥和私钥是一对，此处只用私钥加密
	 */
	private static final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJnbcuaVyk4Z3A/9+CB3wW+BX0mkfJ4Zmw/dytAIpDar7EINDoKKf23QxDOwKV5vg0eF1+6Yr0C5NckJeIkhhcwJFbp71NmRu5DoOS1lz66v+KVguQnVsY4WMkrq2DfgT4iN9CmjTeocjk50xRclzui9yIg869s0coUQvBrFkyarAgMBAAECgYBfzztHAiI/7nSOdN3OAiUv9cVLy6VaoqFgCQKh96+i5/6wxwaxHePqk6WKdnncd96tbFiiGOyRhoRK053+7yQsinAiq4cACqN5IQqdq+QJcMUFbRfB87cl4d41KDwXL/stvHRBR1myhGandTt3quQYN8bnDh/wkcKYGjDNn1DHAQJBAM0h45hS/SzVrbuY4zFC/leka9TLh2wW3YJ4AvUFh5XME+1UxrCN1q9BL89Kj5zRKEAO7n5qLfiCiDQwGfOjUSsCQQDAAokzs3gU3CDbpUjqidUnt9EtwLcGpMXPeccNEYolRpeZx212xkL/2IPiVoYqV5ct29UXVGbCyhtnvCxgtcCBAkAHPJLS5NqZolAmthYNvtdKMDq9nxB/966I08MG8dL+rdxmGv+HavmP5TTSk0bNuXM6D0zKmORWSASWErxXpANRAkAqDeQjEMHSP8xqjy8DP6ESC+6iyLzNlbVFlfSE5jyNoYJ+yfEtvIrcIn3wSucjyv9vqeBoNbI0I7QQ6mQF4okBAkEAtyUlfK7MWwJ8CCIdZJdV/BBK3MZLRrBFQNc2yEv0YstXF5pifH7zDrgBbNVVteVUBirS7pe5HCiFJmQvBf/uuw==";

	/**
	 * 生成注册码，私钥加密，公钥解密
	 *
	 * @param license
	 * @throws Exception
	 */
	private static void generator(License license) throws Exception {
		String json = new ObjectMapper().writeValueAsString(license);
		System.out.println("原文字：\r\n" + json);
		byte[] data = json.getBytes();
		byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
		System.out.println("加密后：\r\n" + HexEncoding.encode(encodedData));
	}

	/**
	 * 生成机器码
	 *
	 * @return
	 */
	public static String getSerialNumber() {
		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hal = si.getHardware();
		return hal.getComputerSystem().getBaseboard().getSerialNumber();
	}

	/**
	 * 校验 License
	 */
	public static void verify(String encodedStr) throws Exception {
		byte[] encodedData = HexEncoding.decode(encodedStr);

		// 解密
		byte[] decodedData = RSAUtils.decryptByPublicKey(encodedData, publicKey);
		String target = new String(decodedData);

		Map map = new ObjectMapper().readValue(target, HashMap.class);
		System.out.println("map = " + map);

		LocalDate expireAt = LocalDate.parse(map.getOrDefault("expireAt", "1970-01-01").toString());
		if (expireAt.isBefore(LocalDate.now())) {
			throw new RuntimeException("许可已过期，请重新购买许可证");
		}
	}

	public static void main(String[] args) {
		try {
			System.out.printf(getSerialNumber());
//			generator();
			verify("5ac0b69286d694295804e93b67b8c92894f405dac0fea175993c4e8dc80a0828143339e8b42c660e67cac05a6272ae9948704d70a728d049687de2d01441227d0d21bc0702a57b1f2d7bed2757d713f05ebda67e819a14bd26e03882b51d5f147bc41a87e02ca89c45080d091271610c945cca22c3646379adc3314d92a15bfa");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
