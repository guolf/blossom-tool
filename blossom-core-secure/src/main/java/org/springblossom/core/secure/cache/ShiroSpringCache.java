package org.springblossom.core.secure.cache;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.CacheException;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Set;

/**
 * 自定义缓存,将shiro缓存统一到spring cache
 * @param <K>
 * @param <V>
 * @author guolf
 */
@Slf4j
@AllArgsConstructor
public class ShiroSpringCache<K, V> implements org.apache.shiro.cache.Cache<K, V> {

	private CacheManager cacheManager;
	private Cache cache;

	public ShiroSpringCache(String name, CacheManager cacheManager) {
		if (name == null || cacheManager == null) {
			throw new IllegalArgumentException("cacheManager or CacheName cannot be null.");
		}
		this.cacheManager = cacheManager;
		this.cache = cacheManager.getCache(name);
	}

	@Override
	public V get(K key) throws CacheException {
		log.info("从缓存中获取key为{}的缓存信息", key);
		if (key == null) {
			return null;
		}
		ValueWrapper valueWrapper = cache.get(key);
		if (valueWrapper == null) {
			return null;
		}
		return (V) valueWrapper.get();
	}

	@Override
	public V put(K key, V value) throws CacheException {
		log.info("创建新的缓存，信息为：{}={}", key, value);
		cache.put(key, value);
		return get(key);
	}

	@Override
	public V remove(K key) throws CacheException {
		log.info("干掉key为{}的缓存", key);
		V v = get(key);
		cache.evict(key);
		return v;
	}

	@Override
	public void clear() throws CacheException {
		log.info("清空所有的缓存");
		cache.clear();
	}

	@Override
	public int size() {
		return cacheManager.getCacheNames().size();
	}

	/**
	 * 获取缓存中所的key值
	 */
	@Override
	public Set<K> keys() {
		return (Set<K>) cacheManager.getCacheNames();
	}

	/**
	 * 获取缓存中所有的values值
	 */
	@Override
	public Collection<V> values() {
		return (Collection<V>) cache.get(cacheManager.getCacheNames()).get();
	}
}
