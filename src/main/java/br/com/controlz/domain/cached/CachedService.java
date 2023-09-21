package br.com.controlz.domain.cached;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CachedService {

	private final CacheManager cacheManager;

	public CachedService(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public Optional<Cache.ValueWrapper> getCachedById(String cacheName, long id) {
		Cache cache = cacheManager.getCache(cacheName);
		Cache.ValueWrapper valueWrapper = Objects.isNull(cache.get(id)) ? null : cache.get(id);
		return Optional.ofNullable(valueWrapper);
	}

	public void putCachedById(long id, Object value, String cachedName) {
		Cache cache = cacheManager.getCache(cachedName);
		if (!Objects.isNull(cache)) {
			cache.put(id, value);
		}
	}

}


