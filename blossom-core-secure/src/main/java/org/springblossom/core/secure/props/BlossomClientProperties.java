
package org.springblossom.core.secure.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户端校验配置
 *
 * @author Chill
 */
@Data
@ConfigurationProperties("blossom.secure")
public class BlossomClientProperties {

	private final List<ClientSecure> client = new ArrayList<>();

}
