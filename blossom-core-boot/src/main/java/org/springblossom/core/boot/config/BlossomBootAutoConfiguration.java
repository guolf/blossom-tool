
package org.springblossom.core.boot.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.launch.props.BlossomProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 配置类
 *
 * @author Chill
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({
	BlossomProperties.class
})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@AllArgsConstructor
public class BlossomBootAutoConfiguration {


}
