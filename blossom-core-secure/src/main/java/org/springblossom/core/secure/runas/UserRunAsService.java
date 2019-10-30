package org.springblossom.core.secure.runas;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springblossom.core.secure.BlossomUser;

import java.io.Serializable;
import java.util.List;

/**
 * 授予身份服务接口
 *
 * @author guolf
 */
public interface UserRunAsService {

	/**
	 * 把当前登录用户的身份授予给相应的用户
	 *
	 * @param toUserId 授予目标用户
	 */
	void grantRunAs(Serializable toUserId);

	/**
	 * 回收身份
	 *
	 * @param toUserId 授予目标用户
	 */
	void revokeRunAs(Serializable toUserId);

	/**
	 * 判断当前登录用户是否可以切换到该身份
	 *
	 * @param toUserId 目标身份
	 * @return
	 */
	boolean exists(Serializable toUserId);

	/**
	 * 展示当前用户能切换到身份列表
	 *
	 * @return
	 */
	List findFromUserIds();

	/**
	 * 授予给其他人的身份列表
	 *
	 * @return
	 */
	List findToUserIds();

	/**
	 * 切换到上一个身份
	 */
	default void releaseRunAs() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isRunAs()) {
			subject.releaseRunAs();
		}
	}

	/**
	 * 切换身份
	 *
	 * @param toUserId
	 */
	void switchToUserId(Serializable toUserId);

}
