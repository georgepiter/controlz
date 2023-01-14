package br.com.controlz.service;

import br.com.controlz.domain.dto.DebtDTO;
import br.com.controlz.domain.dto.DebtValueDTO;
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
		Optional<Register> register = registerRepository.findById(debt.getIdRegister());
		if (register.isEmpty()) {
			throw new RegisterNotFoundException("Registro não encontrado na base");
		}
		debtRepository.save(buildNewDebt(debt, register.get().getIdRegister()));
		return new ResponseEntityCustom(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Debt created");
	}

	private Debt buildNewDebt(DebtDTO debt, Long idRegister) {
		return new Debt.Builder()
				.debtDescription(debt.getDebtDescription())
				.inputDate(LocalDate.now())
				.dueDate(debt.getDueDate())
				.value(debt.getValue())
				.idRegister(idRegister)
				.status(StatusEnum.AWAITING_PAYMENT.getValue())
				.idCategory(debt.getIdCategory())
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
					.dueDate(debt.getDueDate())
					.idCategory(debt.getIdCategory())
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
				.dueDate(debtDTO.getDueDate())
				.idCategory(debtDTO.getIdCategory())
				.status(debtDTO.getStatus().equals(StatusEnum.PAY.getLabel()) ? StatusEnum.PAY.getValue() : StatusEnum.AWAITING_PAYMENT.getValue())
				.createDebt();
		debtRepository.save(updateDebt);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	public ResponseEntityCustom deleteDebtById(Long debtId) throws DebtNotFoundException {
		debtRepository.delete(getRegisterFromDataBase(debtId));
		return new ResponseEntityCustom(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, "debt deleted");
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
		debt.get().setReceiptPayment(debtDTO.getReceiptPayment());
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

		return buildDebtValueDTO(register.get(), debts);
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

	public List<DebtDTO> getAllDebtsBetweenDates(Long registerId, String startDate, String endDate) throws DebtNotFoundException {
		List<Debt> allDebts = debtRepository.findByIdRegister(registerId);
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);

		List<Debt> debts = allDebts.stream().filter(
				debt -> debt.getDueDate().isEqual(start)
						|| debt.getDueDate().isBefore(end)
						|| debt.getDueDate().isEqual(end)
		).toList();

		if (debts.isEmpty()) {
			throw new DebtNotFoundException("Não foram encontrados débitos na faixa de datas solicitada");
		}
		return new ArrayList<>(buildNewDebtListByRegister(debts));
	}
}
