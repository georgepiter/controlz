package br.com.controlz.controller;

import br.com.controlz.domain.cached.CachedService;
import br.com.controlz.domain.dto.DebtDTO;
import br.com.controlz.domain.dto.DebtGroupDTO;
import br.com.controlz.domain.dto.DebtValueDashDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.exception.DebtNotFoundException;
import br.com.controlz.domain.exception.RegisterNotFoundException;
import br.com.controlz.service.DebtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.Cacheable;

@RestController
@Api(value = "Débito", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Débito"})
@RequestMapping(value = "api/v1/debt")
public class DebtController {

	private final DebtService debtService;
	private final CachedService cachedService;

	public DebtController(DebtService debtService,
	                      CachedService cachedService) {
		this.debtService = debtService;
		this.cachedService = cachedService;
	}

	@PostMapping(value = "/")
	@CacheEvict(value = "debt", allEntries = true)
	@ApiOperation(value = "Método que registra novo débito")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntityCustom registerNewDebt(@RequestBody DebtDTO debt) throws RegisterNotFoundException {
		return debtService.registerNewDebt(debt);
	}

	@GetMapping(value = "/{debtId}")
	@Cacheable(value = "debt")
	@ApiOperation(value = "Obtém o Debt pelo debtId")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtDTO getDebtById(@PathVariable Long debtId) throws DebtNotFoundException {
		return cachedService.getOrLoadDebtFromCache(debtId);
	}

	@GetMapping(value = "/all/{userId}/{registerId}")
	@Cacheable(value = "debt")
	@ApiOperation(value = "Método que retorna todos os débitos próximo mês")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtGroupDTO getAllDebtsById(@PathVariable Long userId, @PathVariable Long registerId) throws RegisterNotFoundException {
		return cachedService.getOrLoadDebtGroupFromCache(userId, registerId);
	}

	@GetMapping(value = "/allDebts/{userId}/{monthDebt}")
	@Cacheable(value = "debt")
	@ApiOperation(value = "Método que retorna todos os débitos")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtGroupDTO getAllDebtsByUserIdAndRegisterId(@PathVariable Long userId, @PathVariable String monthDebt) throws RegisterNotFoundException {
		return cachedService.getOrLoadDebtGroupFromCacheOrService(userId, monthDebt);
	}

	@GetMapping(value = "/dash/{userId}/{registerId}")
	@Cacheable(value = "debt")
	@ApiOperation(value = "Método que retorna valor total dos débitos e saldo do mês do próximo mês")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public DebtValueDashDTO getValuesByMonth(@PathVariable Long userId, @PathVariable Long registerId) throws RegisterNotFoundException {
		return cachedService.getOrLoadDebtValueDashFromCacheOrService(userId, registerId);
	}

	@PutMapping(value = "/update")
	@CachePut(value = "debt")
	@ApiOperation(value = "Método que atualiza um débito")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<HttpStatus> updateValue(@RequestBody DebtDTO debtDTO) {
		return debtService.updateValue(debtDTO);
	}

	@PutMapping(value = "/pay")
	@CachePut(value = "debt")
	@ApiOperation(value = "Método utilizado para pagar um débito")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<HttpStatus> payValue(@RequestBody DebtDTO debtDTO) throws DebtNotFoundException {
		return debtService.payValue(debtDTO);
	}

	@DeleteMapping(value = "/{debtId}")
	@CacheEvict(value = "debt", allEntries = true)
	@ApiOperation(value = "Método que deleta um débito")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntityCustom deleteDebt(@PathVariable Long debtId) throws DebtNotFoundException {
		return debtService.deleteDebtById(debtId);
	}

}
