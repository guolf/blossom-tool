package org.springblossom.core.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 简化测试
 *
 * @author guolf
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest
public @interface BlossomBootTest {

	/**
	 * 服务名：appName
	 *
	 * @return appName
	 */
	@AliasFor("appName")
	String value() default "blossom-test";

	/**
	 * 服务名：appName
	 *
	 * @return appName
	 */
	@AliasFor("value")
	String appName() default "blossom-test";

	/**
	 * profile
	 *
	 * @return profile
	 */
	String profile() default "dev";

	/**
	 * 启用 ServiceLoader 加载 launcherService
	 *
	 * @return 是否启用
	 */
	boolean enableLoader() default false;
}
