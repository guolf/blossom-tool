package org.springblossom.core.boot.redis;

import org.springblossom.core.tool.utils.StringUtil;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

/**
 * 添加设置缓存过期时间
 * cacheName设置格式，使用#分隔，如： cacheA#10000
 * @author guolf
 */
//@Component
public class RedisAutoCacheManager extends RedisCacheManager {

	private static final String SPLIT_FLAG = "#";
	private static final int CACHE_LENGTH = 2;

	/**
	 * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
	 * {@link RedisCacheConfiguration}.
	 *
	 * @param cacheWriter               must not be {@literal null}.
	 * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
	 *                                  {@link RedisCacheConfiguration#defaultCacheConfig()}.
	 */
	public RedisAutoCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
		super(cacheWriter, defaultCacheConfiguration);
	}

	/**
	 * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
	 * {@link RedisCacheConfiguration}.
	 *
	 * @param cacheWriter               must not be {@literal null}.
	 * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
	 *                                  {@link RedisCacheConfiguration#defaultCacheConfig()}.
	 * @param initialCacheNames         optional set of known cache names that will be created with given
	 *                                  {@literal defaultCacheConfiguration}.
	 */
	public RedisAutoCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, String... initialCacheNames) {
		super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
	}

	/**
	 * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
	 * {@link RedisCacheConfiguration}.
	 *
	 * @param cacheWriter                must not be {@literal null}.
	 * @param defaultCacheConfiguration  must not be {@literal null}. Maybe just use
	 *                                   {@link RedisCacheConfiguration#defaultCacheConfig()}.
	 * @param allowInFlightCacheCreation if set to {@literal true} no new caches can be acquire at runtime but limited to
	 *                                   the given list of initial cache names.
	 * @param initialCacheNames          optional set of known cache names that will be created with given
	 *                                   {@literal defaultCacheConfiguration}.
	 * @since 2.0.4
	 */
	public RedisAutoCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, boolean allowInFlightCacheCreation, String... initialCacheNames) {
		super(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation, initialCacheNames);
	}

	/**
	 * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
	 * {@link RedisCacheConfiguration}.
	 *
	 * @param cacheWriter                must not be {@literal null}.
	 * @param defaultCacheConfiguration  must not be {@literal null}. Maybe just use
	 *                                   {@link RedisCacheConfiguration#defaultCacheConfig()}.
	 * @param initialCacheConfigurations Map of known cache names along with the configuration to use for those caches.
	 *                                   Must not be {@literal null}.
	 */
	public RedisAutoCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
		super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations);
	}

	/**
	 * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
	 * {@link RedisCacheConfiguration}.
	 *
	 * @param cacheWriter                must not be {@literal null}.
	 * @param defaultCacheConfiguration  must not be {@literal null}. Maybe just use
	 *                                   {@link RedisCacheConfiguration#defaultCacheConfig()}.
	 * @param initialCacheConfigurations Map of known cache names along with the configuration to use for those caches.
	 *                                   Must not be {@literal null}.
	 * @param allowInFlightCacheCreation if set to {@literal false} this cache manager is limited to the initial cache
	 *                                   configurations and will not create new caches at runtime.
	 * @since 2.0.4
	 */
	public RedisAutoCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
		super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
	}

	@Override
	protected Collection<RedisCache> loadCaches() {
		return super.loadCaches();
	}

	@Override
	protected RedisCache getMissingCache(String name) {
		return super.getMissingCache(name);
	}

	/**
	 * @return unmodifiable {@link Map} containing cache name / configuration pairs. Never {@literal null}.
	 */
	@Override
	public Map<String, RedisCacheConfiguration> getCacheConfigurations() {
		return super.getCacheConfigurations();
	}

	/**
	 * Configuration hook for creating {@link RedisCache} with given name and {@code cacheConfig}.
	 *
	 * @param name        must not be {@literal null}.
	 * @param cacheConfig can be {@literal null}.
	 * @return never {@literal null}.
	 */
	@Override
	protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
		if (StringUtil.isBlank(name) || !name.contains(SPLIT_FLAG)) {
			return super.createRedisCache(name, cacheConfig);
		}

		String[] cacheArray = name.split(SPLIT_FLAG);
		if (cacheArray.length < CACHE_LENGTH) {
			return super.createRedisCache(name, cacheConfig);
		}

		if (cacheConfig != null) {
			long cacheAge = Long.parseLong(cacheArray[1]);
			cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(cacheAge));
		}
		return super.createRedisCache(name, cacheConfig);
	}
}
