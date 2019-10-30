package org.springblossom.core.test;

import org.junit.runners.model.InitializationError;
import org.springblossom.core.launch.constant.AppConstant;
import org.springblossom.core.launch.constant.NacosConstant;
import org.springblossom.core.launch.constant.SentinelConstant;
import org.springblossom.core.launch.service.LauncherService;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 单元测试，启动参数设置
 *
 * @author guolf
 */
public class BlossomSpringRunner extends SpringJUnit4ClassRunner {

	public BlossomSpringRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
		setUpTestClass(clazz);
	}

	private void setUpTestClass(Class<?> clazz) {
		BlossomBootTest blossomBootTest = AnnotationUtils.getAnnotation(clazz, BlossomBootTest.class);
		if (blossomBootTest == null) {
			throw new BlossomBootTestException(String.format("%s must be @BlossomBootTest .", clazz));
		}
		String appName = blossomBootTest.appName();
		String profile = blossomBootTest.profile();
		Properties props = System.getProperties();
		props.setProperty("blossom.env", profile);
		props.setProperty("blossom.name", appName);
		props.setProperty("blossom.dev-mode", profile.equals(AppConstant.PROD_CODE) ? "false" : "true");
		props.setProperty("blossom.service.version", AppConstant.APPLICATION_VERSION);
		props.setProperty("spring.application.name", appName);
		props.setProperty("spring.profiles.active", profile);
		props.setProperty("info.version", AppConstant.APPLICATION_VERSION);
		props.setProperty("info.desc", appName);
		props.setProperty("spring.cloud.nacos.discovery.server-addr", NacosConstant.NACOS_ADDR);
		props.setProperty("spring.cloud.nacos.config.server-addr", NacosConstant.NACOS_ADDR);
		props.setProperty("spring.cloud.nacos.config.prefix", NacosConstant.NACOS_CONFIG_PREFIX);
		props.setProperty("spring.cloud.nacos.config.file-extension", NacosConstant.NACOS_CONFIG_FORMAT);
		props.setProperty("spring.cloud.sentinel.transport.dashboard", SentinelConstant.SENTINEL_ADDR);
		props.setProperty("spring.main.allow-bean-definition-overriding", "true");
		// 加载自定义组件
		if (blossomBootTest.enableLoader()) {
			List<LauncherService> launcherList = new ArrayList<>();
			SpringApplicationBuilder builder = new SpringApplicationBuilder(clazz);
			ServiceLoader.load(LauncherService.class).forEach(launcherList::add);
			launcherList.stream().sorted(Comparator.comparing(LauncherService::getOrder)).collect(Collectors.toList())
				.forEach(launcherService -> launcherService.launcher(builder, appName, profile));
		}
		System.err.println(String.format("---[junit.test]:[%s]---启动中，读取到的环境变量:[%s]", appName, profile));
	}
}
