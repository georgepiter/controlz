package br.com.controlz.service;

import br.com.controlz.domain.dto.DebtDTO;
import br.com.controlz.domain.dto.DebtValueDTO;
import br.com.controlz.domain.dto.DebtValueDashDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.entity.Debt;
import br.com.controlz.domain.entity.Register;
import br.com.controlz.domain.enums.StatusEnum;
import br.com.controlz.domain.exception.DebtNotFoundException;
import br.com.controlz.domain.exception.RegisterNotFoundException;
import br.com.controlz.domain.repository.DebtRepository;
import br.com.controlz.domain.repository.RegisterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DebtService {

	private final DebtRepository debtRepository;
	private final RegisterRepository registerRepository;

	public DebtService(DebtRepository debtRepository,
					   RegisterRepository registerRepository) {
		this.debtRepository = debtRepository;
		this.registerRepository = registerRepository;
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

	public DebtValueDTO getAllDebtsByRegister(Long registerId, Long userId) throws RegisterNotFoundException {
		List<Debt> debts = getDebts(registerId);
		Register register = getRegisterFromDataBase(userId, registerId);
		return buildDebtValueDTO(register, debts);
	}

	private List<Debt> getDebts(Long registerId) {
		LocalDate now = LocalDate.now();
		LocalDate firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());
		return debtRepository.findByDueDateAndRegisterId(firstDayOfMonth, lastDayOfMonth, registerId);
	}

	private List<DebtDTO> buildNewDebtListByRegister(List<Debt> debts) {
		List<DebtDTO> debtsDTO = new ArrayList<>();
		for (Debt debt : debts) {
			String statusLabel = debt.getStatus().equals(StatusEnum.PAY.getValue()) ? "Pago" : "Aguardando Pagamento";
			DebtDTO newDebtDTO = new DebtDTO.Builder()
					.debtId(debt.getDebtId())
					.registerId(debt.getRegisterId())
					.debtDescription(debt.getDebtDescription())
					.value(debt.getValue())
					.inputDate(debt.getInputDate())
					.status(statusLabel)
					.receiptPayment(debt.getReceiptPayment())
					.dueDate(debt.getDueDate())
					.paymentDate(debt.getPaymentDate())
					.categoryId(debt.getCategoryId())
					.createNewDebtDTO();
			debtsDTO.add(newDebtDTO);
		}
		return debtsDTO;
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

	private DebtValueDTO buildDebtValueDTO(Register register, List<Debt> debts) {
		double fullDebt = getFullDebt(register.getRegisterId()).getTotalDebt();
		double salary = register.getSalary();
		double othersValue = Objects.isNull(register.getOthers()) ? 0.00 : register.getOthers();
		double currentTotalValue = (salary - fullDebt) + othersValue;

		return new DebtValueDTO.Builder()
				.debtList(buildNewDebtListByRegister(debts))
				.totalEntryValue(salary + register.getOthers())
				.currentTotalValue(currentTotalValue)
				.totalDebt(fullDebt)
				.createNewDebtValue();
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

	public List<DebtDTO> getAllDebtsByUserIdAndRegisterId(Long userId, String monthDebt) throws RegisterNotFoundException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		LocalDate monthLocalDate = YearMonth.parse(monthDebt, formatter).atDay(1);
		Long registerId = registerRepository.findByUserId(userId)
				.orElseThrow(() -> new RegisterNotFoundException("Registro não encontrado para UserId"))
				.getRegisterId();

		return debtRepository.findByRegisterIdAndDueDate(registerId, monthLocalDate).stream()
				.map(debt -> new DebtDTO.Builder()
						.registerId(registerId)
						.userId(userId)
						.receiptPayment(debt.getReceiptPayment())
						.debtId(debt.getDebtId())
						.categoryId(debt.getCategoryId())
						.status(debt.getStatus() == StatusEnum.AWAITING_PAYMENT.getValue() ? StatusEnum.AWAITING_PAYMENT.getLabel() : StatusEnum.PAY.getLabel())
						.value(debt.getValue())
						.inputDate(debt.getInputDate())
						.debtDescription(debt.getDebtDescription())
						.dueDate(debt.getDueDate())
						.paymentDate(debt.getPaymentDate())
						.createNewDebtDTO())
				.toList();
	}

}

