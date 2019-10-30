/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblossom.core.cloud.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springblossom.core.cloud.feign.BlossomFeignRequestHeaderInterceptor;
import org.springblossom.core.cloud.feign.FeignErrorDecoder;
import org.springblossom.core.cloud.feign.FeignHystrixConcurrencyStrategy;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WEB配置
 *
 * @author Chill
 */
@Slf4j
@Configuration
@EnableCaching
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BlossomFeignConfiguration implements WebMvcConfigurer {

	@Bean
	@ConditionalOnMissingBean
	public RequestInterceptor requestInterceptor() {
		return new BlossomFeignRequestHeaderInterceptor();
	}

	@Bean
	public FeignHystrixConcurrencyStrategy feignHystrixConcurrencyStrategy() {
		return new FeignHystrixConcurrencyStrategy();
	}

	@Bean
	public Decoder feignDecoder() {
		HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(customObjectMapper());

		ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
		return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
	}

	@Bean
	public ErrorDecoder errorDecoder() {
		return new FeignErrorDecoder();
	}

	public ObjectMapper customObjectMapper(){
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}
}
