package br.com.controlz.service;

import br.com.controlz.domain.dto.*;
import br.com.controlz.domain.entity.Category;
import br.com.controlz.domain.entity.Debt;
import br.com.controlz.domain.entity.Register;
import br.com.controlz.domain.enums.StatusEnum;
import br.com.controlz.domain.exception.DebtNotFoundException;
import br.com.controlz.domain.exception.RegisterNotFoundException;
import br.com.controlz.domain.repository.CategoryRepository;
import br.com.controlz.domain.repository.DebtRepository;
import br.com.controlz.domain.repository.RegisterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static java.util.stream.Collectors.toMap;

@Service
public class DebtService {

	private final DebtRepository debtRepository;
	private final RegisterRepository registerRepository;
	private final CategoryRepository categoryRepository;

	public DebtService(DebtRepository debtRepository,
	                   RegisterRepository registerRepository,
	                   CategoryRepository categoryRepository) {
		this.debtRepository = debtRepository;
		this.registerRepository = registerRepository;
		this.categoryRepository = categoryRepository;
	}

	public ResponseEntityCustom registerNewDebt(DebtDTO debt) throws RegisterNotFoundException {
		Register register = getRegisterFromDataBase(debt.getUserId(), debt.getRegisterId());
		debtRepository.save(buildNewDebt(debt, register.getRegisterId()));
		return new ResponseEntityCustom(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Débito salvo com sucesso!");
	}

	private Register getRegisterFromDataBase(Long userId, Long registerId) throws RegisterNotFoundException {
		return registerRepository.findByRegisterIdAndUserId(userId, registerId)
				.orElseThrow(() -> new RegisterNotFoundException("Registro não encontrado na base"));
	}

	private Debt buildNewDebt(DebtDTO debt, Long idRegister) {
		return new Debt.Builder()
				.debtDescription(debt.getDebtDescription())
				.inputDate(LocalDate.now())
				.dueDate(debt.getDueDate())
				.value(debt.getValue())
				.registerId(idRegister)
				.status(StatusEnum.AWAITING_PAYMENT.getValue())
				.categoryId(debt.getCategoryId())
				.createDebt();
	}

	public DebtGroupDTO getAllDebtsByRegister(Long registerId, Long userId) throws RegisterNotFoundException {
		List<Debt> debts = getDebts(registerId);
		Register register = getRegisterFromDataBase(userId, registerId);
		return buildDebtCategoryGroupDTO(register, debts);
	}

	public List<Debt> getDebts(Long registerId) {
		LocalDate now = LocalDate.now();
		LocalDate firstDayOfNextMonth = now.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDayOfNextMonth = now.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
		return debtRepository.findByDueDateAndRegisterId(firstDayOfNextMonth, lastDayOfNextMonth, registerId);
	}

	public DebtValueDTO getFullDebt(Long registerId) {
		List<Debt> debts = getDebts(registerId);
		double totalValue = debts.stream().mapToDouble(Debt::getValue).sum();
		return new DebtValueDTO.Builder()
				.totalDebt(totalValue)
				.createNewDebtValue();
	}

	public ResponseEntity<HttpStatus> updateValue(DebtDTO debtDTO) {
		Debt updateDebt = new Debt.Builder()
				.debtDescription(debtDTO.getDebtDescription())
				.debtId(debtDTO.getDebtId())
				.inputDate(LocalDate.now())
				.value(debtDTO.getValue())
				.registerId(debtDTO.getRegisterId())
				.dueDate(debtDTO.getDueDate())
				.paymentDate(debtDTO.getPaymentDate())
				.receiptPayment(Objects.isNull(debtDTO.getReceiptPayment()) ? null : debtDTO.getReceiptPayment())
				.categoryId(debtDTO.getCategoryId())
				.status(debtDTO.getStatus().equals(StatusEnum.PAY.getLabel()) ? StatusEnum.PAY.getValue() : StatusEnum.AWAITING_PAYMENT.getValue())
				.createDebt();
		debtRepository.save(updateDebt);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	public ResponseEntityCustom deleteDebtById(Long debtId) throws DebtNotFoundException {
		debtRepository.delete(getRegisterFromDataBase(debtId));
		return new ResponseEntityCustom(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, "Débito deletado com sucesso!");
	}

	private Debt getRegisterFromDataBase(Long debtId) throws DebtNotFoundException {
		return debtRepository.findByDebtId(debtId).orElseThrow(() -> new DebtNotFoundException("Valor não encontrado pelo ID"));
	}

	public ResponseEntity<HttpStatus> payValue(DebtDTO debtDTO) throws DebtNotFoundException {
		Debt debt = debtRepository.findByDebtIdAndRegisterId(debtDTO.getDebtId(), debtDTO.getRegisterId())
				.orElseThrow(() -> new DebtNotFoundException("Débito não encontrado pelo ID informado"));

		debt.setStatus(StatusEnum.PAY.getValue());
		debt.setPaymentDate(LocalDate.now());
		debt.setReceiptPayment(Objects.isNull(debtDTO.getReceiptPayment()) ? null : debtDTO.getReceiptPayment());
		debtRepository.save(debt);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	private DebtGroupDTO buildDebtCategoryGroupDTO(Register register, List<Debt> debts) {

		double fullDebt = getFullDebt(register.getRegisterId()).getTotalDebt();
		double salary = register.getSalary();
		double othersValue = Objects.isNull(register.getOthers()) ? 0.00 : register.getOthers();
		double currentTotalValue = (salary - fullDebt) + othersValue;

		List<Category> categories = categoryRepository.findAll();
		List<Map<String, List<DebtDTO>>> groupDebtsByCategory = groupDebtsByCategory(debts, categories);

		List<DebtCategoryGroupDTO> debtsCategoryGroupDTO = new ArrayList<>();

		DebtGroupDTO debtGroupDTO = new DebtGroupDTO();
		debtGroupDTO.setTotalDebt(fullDebt);
		debtGroupDTO.setCurrentTotalValue(currentTotalValue);
		debtGroupDTO.setTotalEntryValue(salary + othersValue);

		appendDebtsCategoryGroupDTO(categories, groupDebtsByCategory, debtsCategoryGroupDTO, debtGroupDTO);

		return debtGroupDTO;
	}

	private static void appendDebtsCategoryGroupDTO(List<Category> categories, List<Map<String, List<DebtDTO>>> groupDebtsByCategory, List<DebtCategoryGroupDTO> debtsCategoryGroupDTO, DebtGroupDTO debtGroupDTO) {
		for (Map<String, List<DebtDTO>> stringListMap : groupDebtsByCategory) {
			String categoryName = stringListMap.keySet().iterator().next();
			List<DebtDTO> debtList = stringListMap.get(categoryName);

			Optional<Category> category = categories.stream()
					.filter(c -> c.getDescription().equals(categoryName))
					.findAny();

			DebtCategoryGroupDTO debtCategoryDTO = new DebtCategoryGroupDTO();
			debtCategoryDTO.setTypeCategory(Objects.isNull(categoryName) ? "SEM CATEGORIA" : categoryName);
			debtCategoryDTO.setCategoryId(category.isEmpty() ? 0L : category.get().getCategoryId());
			debtCategoryDTO.setDebtList(debtList);
			debtsCategoryGroupDTO.add(debtCategoryDTO);
		}
		debtGroupDTO.setDebtsCategoryGroupDTO(debtsCategoryGroupDTO);
	}

	private List<Map<String, List<DebtDTO>>> groupDebtsByCategory(List<Debt> debts, List<Category> categories) {

		Map<Long, String> categoryMap = categories.stream()
				.collect(toMap(Category::getCategoryId, Category::getDescription));

		Map<Long, List<DebtDTO>> debtsMap = new HashMap<>();
		for (Debt debt : debts) {
			Long categoryId = debt.getCategoryId();
			String statusLabel = debt.getStatus().equals(StatusEnum.PAY.getValue()) ? "Pago" : "Aguardando Pagamento";
			DebtDTO debtDTO = new DebtDTO.Builder()
					.debtId(debt.getDebtId())
					.registerId(debt.getRegisterId())
					.debtDescription(debt.getDebtDescription())
					.value(debt.getValue())
					.inputDate(debt.getInputDate())
					.status(statusLabel)
					.receiptPayment(debt.getReceiptPayment())
					.dueDate(debt.getDueDate())
					.paymentDate(debt.getPaymentDate())
					.categoryId(categoryId)
					.createNewDebtDTO();

			debtsMap.computeIfAbsent(categoryId, k -> new ArrayList<>()).add(debtDTO);
		}

		List<Map<String, List<DebtDTO>>> debtsByCategory = new ArrayList<>();
		for (Map.Entry<Long, List<DebtDTO>> entry : debtsMap.entrySet()) {
			Long categoryId = entry.getKey();
			String categoryName = categoryMap.get(categoryId);
			List<DebtDTO> categoryDebts = entry.getValue();
			Map<String, List<DebtDTO>> mapCategory = new HashMap<>();
			mapCategory.put(categoryName, categoryDebts);
			debtsByCategory.add(mapCategory);
		}

		return debtsByCategory;
	}


	public DebtDTO getDebtById(Long debtId) throws DebtNotFoundException {
		Debt debt = debtRepository.findById(debtId).orElseThrow(() -> new DebtNotFoundException("Não foi encontrado o débito pelo Id informado"));
		return new DebtDTO.Builder()
				.debtId(debtId)
				.categoryId(debt.getCategoryId())
				.registerId(debt.getRegisterId())
				.paymentDate(debt.getPaymentDate())
				.debtDescription(debt.getDebtDescription())
				.dueDate(debt.getDueDate())
				.value(debt.getValue())
				.inputDate(debt.getInputDate())
				.status(debt.getStatus().equals(StatusEnum.AWAITING_PAYMENT.getValue()) ? StatusEnum.AWAITING_PAYMENT.getLabel() : StatusEnum.PAY.getLabel())
				.receiptPayment(debt.getReceiptPayment())
				.createNewDebtDTO();
	}

	public DebtValueDashDTO getValuesByMonth(Long registerId, Long userId) throws RegisterNotFoundException {
		Register registerNow = getRegisterNow(registerId, userId);

		double fullDebt = getFullDebt(registerNow.getRegisterId()).getTotalDebt();
		double salary = registerNow.getSalary();
		double othersValue = Optional.ofNullable(registerNow.getOthers()).orElse(0.00);
		double currentTotalValue = (salary - fullDebt) + othersValue;

		return new DebtValueDashDTO(
				fullDebt, salary + othersValue, currentTotalValue
		);
	}

	private Register getRegisterNow(Long registerId, Long userId) throws RegisterNotFoundException {
		return registerRepository.findByRegisterIdAndUserId(userId, registerId)
				.orElseThrow(() -> new RegisterNotFoundException("Registro não encontrado na base"));
	}

	public DebtGroupDTO getAllDebtsByUserIdAndRegisterId(Long userId, String monthDebt) throws RegisterNotFoundException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		LocalDate monthLocalDate = YearMonth.parse(monthDebt, formatter).atDay(1);

		Long registerId = registerRepository.findByUserId(userId)
				.orElseThrow(() -> new RegisterNotFoundException("Registro não encontrado para UserId"))
				.getRegisterId();

		List<Debt> allDebt = debtRepository.findByRegisterIdAndDueDate(registerId, monthLocalDate);
		List<Category> categories = categoryRepository.findAll();
		List<Map<String, List<DebtDTO>>> groupDebtsByCategory = groupDebtsByCategory(allDebt, categories);

		List<DebtCategoryGroupDTO> debtsCategoryGroupDTO = new ArrayList<>();

		DebtGroupDTO debtGroupDTO = new DebtGroupDTO();

		appendDebtsCategoryGroupDTO(categories, groupDebtsByCategory, debtsCategoryGroupDTO, debtGroupDTO);

		return debtGroupDTO;
	}

}

