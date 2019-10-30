package org.springblossom.core.secure.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.secure.config.ShiroSecureProperties;
import org.springblossom.core.secure.constant.SecureConstant;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 事件监听
 *
 * @author guolf
 */
@Slf4j
@Service
@AllArgsConstructor
public class SecureListener {

	private RedisTemplate<String, Object> redisTemplate;
	private ShiroSecureProperties shiroSecureProperties;

	@Async
	@Order
	@EventListener(LoginFailEvent.class)
	public void loginFailListener(LoginFailEvent event) {
		log.info("账号【{}】登录失败", event.getLoginName());
		Long times = redisTemplate.opsForValue().increment(SecureConstant.PWD_ERROR_TIMES_KEY + event.getLoginName(), 1);
		redisTemplate.expire(SecureConstant.PWD_ERROR_TIMES_KEY + event.getLoginName(), 1, TimeUnit.DAYS);

		if (times >= shiroSecureProperties.getVerificationCodeTimes()) {
			// 需要验证码
			redisTemplate.opsForValue().set(SecureConstant.VERIFICATION_CODE_USER_KEY + event.getLoginName(), 1);
			redisTemplate.expire(SecureConstant.VERIFICATION_CODE_USER_KEY + event.getLoginName(), 1, TimeUnit.DAYS);
		}

		if (times >= shiroSecureProperties.getLockUserTimes()) {
			// 锁定账号
			redisTemplate.opsForValue().set(SecureConstant.LOCK_USER_KEY + event.getLoginName(), 1);
			redisTemplate.expire(SecureConstant.LOCK_USER_KEY + event.getLoginName(), shiroSecureProperties.getLockUserMinute(), TimeUnit.MINUTES);
		}
	}

	@Async
	@Order
	@EventListener(LoginSuccessEvent.class)
	public void loginSuccessListener(LoginSuccessEvent event) {
		log.info("账号【{}】登录成功", event.getLoginName());
		redisTemplate.delete(SecureConstant.PWD_ERROR_TIMES_KEY + event.getLoginName());
		redisTemplate.delete(SecureConstant.VERIFICATION_CODE_USER_KEY + event.getLoginName());
		redisTemplate.delete(SecureConstant.LOCK_USER_KEY + event.getLoginName());
	}

	@Async
	@Order
	@EventListener(LogoutEvent.class)
	public void logoutListener(LogoutEvent event) {
		log.info("账号【{}】注销登录", event.getLoginName());
	}

	@Async
	@Order
	@EventListener(PermissionChangeEvent.class)
	public void permissionChange(PermissionChangeEvent event) {
		log.info("监听到权限变更,清除权限缓存...");
		Set<byte[]> keys = redisTemplate.getConnectionFactory().getConnection().keyCommands().keys(("*" + SecureConstant.CACHE_USER_PERMISSION.concat("*")).getBytes());
		for (byte[] key : keys) {
			redisTemplate.getConnectionFactory().getConnection().keyCommands().del(key);
		}
	}
}
