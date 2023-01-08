package br.com.controlz.service;

import br.com.controlz.domain.dto.DebtDTO;
import br.com.controlz.domain.dto.DebtValueDTO;
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

	public ResponseEntity<HttpStatus> registerNewDebt(DebtDTO debt) throws RegisterNotFoundException {
		Optional<Register> register = registerRepository.findById(debt.getIdRegister());
		if (register.isEmpty()) {
			throw new RegisterNotFoundException("Registro não encontrado na base");
		}
		debtRepository.save(buildNewDebt(debt, register.get().getIdRegister()));
		return ResponseEntity.ok(HttpStatus.OK);
	}

	private Debt buildNewDebt(DebtDTO debt, Long idRegister) {
		return new Debt.Builder()
				.debtDescription(debt.getDebtDescription())
				.inputDate(LocalDate.now())
				.value(debt.getValue())
				.idRegister(idRegister)
				.status(StatusEnum.AWAITING_PAYMENT.getValue())
				.createDebt();
	}

	public DebtValueDTO getAllDebtsByRegister(Long registerId) throws DebtNotFoundException, RegisterNotFoundException {
		List<Debt> debts = getDebts(registerId);
		Optional<Register> register = registerRepository.findById(registerId);
		if (register.isEmpty()) {
			throw new RegisterNotFoundException("Registro não encontrado na base");
		}
		return buildDebtValueDTO(register.get(), debts);
	}

	private List<Debt> getDebts(Long registerId) throws DebtNotFoundException {
		List<Debt> debts = debtRepository.findByIdRegister(registerId);
		if (debts.isEmpty()) {
			throw new DebtNotFoundException("Não foram encontrados Valores para o registro");
		}
		return debts;
	}

	private List<DebtDTO> buildNewDebtListByRegister(List<Debt> debts) {
		List<DebtDTO> debtsDTO = new ArrayList<>();
		for (Debt debt : debts) {
			String statusLabel = debt.getStatus().equals(StatusEnum.PAY.getValue()) ? "PAGO" : "Aguardando Pagamento";
			DebtDTO newDebtDTO = new DebtDTO.Builder()
					.idDebt(debt.getIdDebt())
					.idRegister(debt.getIdRegister())
					.debtDescription(debt.getDebtDescription())
					.value(debt.getValue())
					.inputDate(debt.getInputDate())
					.status(statusLabel)
					.paymentDate(debt.getPaymentDate())
					.createNewDebtDTO();
			debtsDTO.add(newDebtDTO);
		}
		return debtsDTO;
	}

	public DebtValueDTO getFullDebt(Long registerId) throws DebtNotFoundException {
		List<Debt> debts = getDebts(registerId);
		double totalValue = debts.stream().mapToDouble(Debt::getValue).sum();
		return new DebtValueDTO.Builder()
				.totalDebt(totalValue)
				.createNewDebtValue();
	}

	public ResponseEntity<HttpStatus> updateValue(DebtDTO debtDTO) {
		Debt updateDebt = new Debt.Builder()
				.debtDescription(debtDTO.getDebtDescription())
				.idDebt(debtDTO.getIdDebt())
				.inputDate(LocalDate.now())
				.value(debtDTO.getValue())
				.idRegister(debtDTO.getIdRegister())
				.createDebt();
		debtRepository.save(updateDebt);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	public ResponseEntity<HttpStatus> deleteDebtById(Long debtId) throws DebtNotFoundException {
		debtRepository.delete(getRegisterFromDataBase(debtId));
		return ResponseEntity.ok(HttpStatus.OK);
	}

	private Debt getRegisterFromDataBase(Long debtId) throws DebtNotFoundException {
		Optional<Debt> debt = debtRepository.findById(debtId);
		if (debt.isEmpty()) {
			throw new DebtNotFoundException("Valor não encontrado pelo ID");
		}
		return debt.get();
	}

	public ResponseEntity<HttpStatus> payValue(DebtDTO debtDTO) throws DebtNotFoundException {
		Optional<Debt> debt = debtRepository.findById(debtDTO.getIdDebt());
		if (debt.isEmpty()) {
			throw new DebtNotFoundException("Débito não encontrado pelo ID informado");
		}
		debt.get().setStatus(StatusEnum.PAY.getValue());
		debt.get().setPaymentDate(LocalDate.now());
		debtRepository.save(debt.get());
		return ResponseEntity.ok(HttpStatus.OK);
	}

	public DebtValueDTO getAllDebtsByStatusAndRegister(boolean isPay, Long registerId) throws DebtNotFoundException, RegisterNotFoundException {
		List<Debt> debts;
		if (isPay) {
			debts = findByDebtByStatusAndRegisterId(StatusEnum.PAY.getValue(), registerId);
		} else {
			debts = findByDebtByStatusAndRegisterId(StatusEnum.AWAITING_PAYMENT.getValue(), registerId);
		}
		if (debts.isEmpty()) {
			throw new DebtNotFoundException("Não foram encontrados débitos na base");
		}
		Optional<Register> register = registerRepository.findById(registerId);
		if (register.isEmpty()) {
			throw new RegisterNotFoundException("Registro não encontrado na base pelo ID");
		}

		return buildDebtValueDTO(register.get(), debts);//todo olhar os dtos e usar herança por conta de atributos repetidos
	}

	private DebtValueDTO buildDebtValueDTO(Register register, List<Debt> debts) throws DebtNotFoundException {
		double fullDebt = getFullDebt(register.getIdRegister()).getTotalDebt();
		double salary = register.getSalary();
		double othersValue = Objects.isNull(register.getOthers()) ? 0.00 : register.getOthers();
		double currentTotalValue = (salary - fullDebt) + othersValue;

		return new DebtValueDTO.Builder()
				.name(register.getName())
				.debtList(buildNewDebtListByRegister(debts))
				.totalEntryValue(salary + register.getOthers())
				.currentTotalValue(currentTotalValue)
				.totalDebt(fullDebt)
				.createNewDebtValue();
	}

	private List<Debt> findByDebtByStatusAndRegisterId(Integer status, Long registerId) {
		return debtRepository.findByStatusAndIdRegister(status, registerId);
	}
}
