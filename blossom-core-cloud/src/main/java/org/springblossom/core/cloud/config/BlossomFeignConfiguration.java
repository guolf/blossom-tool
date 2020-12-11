
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
