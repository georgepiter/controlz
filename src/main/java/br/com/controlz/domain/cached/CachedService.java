package br.com.controlz.domain.cached;

import br.com.controlz.domain.dto.CategoryDTO;
import br.com.controlz.domain.dto.DebtDTO;
import br.com.controlz.domain.dto.DebtGroupDTO;
import br.com.controlz.domain.dto.DebtValueDashDTO;
import br.com.controlz.domain.exception.CategoryNotFoundException;
import br.com.controlz.domain.exception.DebtNotFoundException;
import br.com.controlz.domain.exception.RegisterNotFoundException;
import br.com.controlz.service.CategoryService;
import br.com.controlz.service.DebtService;
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
	private final DebtService debtService;

	public CachedService(CacheManager cacheManager,
	                     CategoryService categoryService,
	                     DebtService debtService) {
		this.cacheManager = cacheManager;
		this.categoryService = categoryService;
		this.debtService = debtService;
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

	public DebtDTO getOrLoadDebtFromCache(Long debtId) throws DebtNotFoundException {
		DebtDTO debtDTO;
		String cachedName = "debt";
		Optional<Cache.ValueWrapper> cacheValue = getCachedById(cachedName, debtId);

		if (cacheValue.isPresent()) {
			debtDTO = ((DebtDTO) cacheValue.get().get());
		} else {
			debtDTO = debtService.getDebtById(debtId);
			putCachedById(debtId, debtDTO, cachedName);
		}
		return debtDTO;
	}

	public DebtGroupDTO getOrLoadDebtGroupFromCache(Long userId, Long registerId) throws RegisterNotFoundException {
		DebtGroupDTO debtGroupDTO;
		String cachedName = "debt";
		Optional<Cache.ValueWrapper> cacheValue = getCachedById(cachedName, userId);

		if (cacheValue.isPresent()) {
			debtGroupDTO = ((DebtGroupDTO) cacheValue.get().get());
		} else {
			debtGroupDTO = debtService.getAllDebtsByRegister(registerId, userId);
			putCachedById(userId, debtGroupDTO, cachedName);
		}
		return debtGroupDTO;
	}

	public DebtGroupDTO getOrLoadDebtGroupFromCacheOrService(Long userId, String monthDebt) throws RegisterNotFoundException {
		DebtGroupDTO debtGroupDTO;
		String cachedName = "debt";
		Optional<Cache.ValueWrapper> cacheValue = getCachedById(cachedName, userId);

		if (cacheValue.isPresent()) {
			debtGroupDTO = ((DebtGroupDTO) cacheValue.get().get());
		} else {
			debtGroupDTO = debtService.getAllDebtsByUserIdAndRegisterId(userId, monthDebt);
			putCachedById(userId, debtGroupDTO, cachedName);
		}
		return debtGroupDTO;
	}

	public DebtValueDashDTO getOrLoadDebtValueDashFromCacheOrService(Long userId, Long registerId) throws RegisterNotFoundException {
		DebtValueDashDTO debtValueDashDTO;
		String cachedName = "debt";
		Optional<Cache.ValueWrapper> cacheValue = getCachedById(cachedName, userId);

		if (cacheValue.isPresent()) {
			debtValueDashDTO = ((DebtValueDashDTO) cacheValue.get().get());
		} else {
			debtValueDashDTO = debtService.getValuesByMonth(registerId, userId);
			putCachedById(userId, debtValueDashDTO, cachedName);
		}
		return debtValueDashDTO;
	}
}


