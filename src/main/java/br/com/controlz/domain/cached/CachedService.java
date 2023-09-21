package br.com.controlz.domain.cached;

import br.com.controlz.domain.dto.CategoryDTO;
import br.com.controlz.domain.exception.CategoryNotFoundException;
import br.com.controlz.service.CategoryService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CachedService {

	private final CacheManager cacheManager;
	private final CategoryService categoryService;

	public CachedService(CacheManager cacheManager,
	                     CategoryService categoryService) {
		this.cacheManager = cacheManager;
		this.categoryService = categoryService;
	}

	protected Optional<Cache.ValueWrapper> getCachedById(String cacheName, long id) {
		Cache cache = cacheManager.getCache(cacheName);
		Cache.ValueWrapper valueWrapper = Objects.isNull(cache.get(id)) ? null : cache.get(id);
		return Optional.ofNullable(valueWrapper);
	}

	protected void putCachedById(long id, Object value, String cachedName) {
		Cache cache = cacheManager.getCache(cachedName);
		if (!Objects.isNull(cache)) {
			cache.put(id, value);
		}
	}

	public List<CategoryDTO> getOrLoadCategoriesFromCache(Long registerId) throws CategoryNotFoundException {
		List<CategoryDTO> categoryDTOS;
		String cachedName = "category";
		Optional<Cache.ValueWrapper> cacheValue = getCachedById(cachedName, registerId);

		if (cacheValue.isPresent()) {
			categoryDTOS = new ArrayList<>((List<CategoryDTO>) cacheValue.get().get());
		} else {
			categoryDTOS = categoryService.getAllCategoriesByRegisterId(registerId);
			putCachedById(registerId, categoryDTOS, cachedName);
		}
		return categoryDTOS;
	}

	public CategoryDTO getOrLoadCategoryFromCache(Long categoryId) throws CategoryNotFoundException {
		CategoryDTO categoryDTO;
		String cachedName = "category";
		Optional<Cache.ValueWrapper> cacheValue = getCachedById(cachedName, categoryId);

		if (cacheValue.isPresent()) {
			categoryDTO = ((CategoryDTO) cacheValue.get().get());
		} else {
			categoryDTO = categoryService.getCategoryId(categoryId);
			putCachedById(categoryId, categoryDTO, cachedName);
		}
		return categoryDTO;
	}

}


