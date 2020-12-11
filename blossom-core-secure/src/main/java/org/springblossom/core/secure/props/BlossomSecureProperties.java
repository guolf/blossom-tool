
package org.springblossom.core.secure.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * secure放行额外配置
 *
 * @author Chill
 */
@Data
@ConfigurationProperties("blossom.secure.url")
public class BlossomSecureProperties {

	private final List<String> excludePatterns = new ArrayList<>();

	private final Map filterRuleMap = new HashMap();

}
