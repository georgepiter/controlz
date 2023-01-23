package br.com.controlz.service;

import br.com.controlz.domain.dto.DebtValueDTO;
import br.com.controlz.domain.dto.RegisterDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.entity.Register;
import br.com.controlz.domain.exception.*;
import br.com.controlz.domain.repository.RegisterRepository;
import br.com.controlz.utils.EmailUtils;
import br.com.controlz.utils.PhoneUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RegisterService {

	private final RegisterRepository registerRepository;
	private final DebtService debtService;

	public RegisterService(RegisterRepository registerRepository,
						   DebtService debtService) {
		this.registerRepository = registerRepository;
		this.debtService = debtService;
	}

	public ResponseEntityCustom registerNewPerson(RegisterDTO registerDTO) throws ValueException, RegisterException, PhoneException, EmailException, FieldException {
		if (Boolean.FALSE.equals(EmailUtils.isEmailPatternValid(registerDTO.getEmail()))) {
			throw new EmailException("Email inválido");
		}
		if (Boolean.FALSE.equals(PhoneUtils.validatePhone(registerDTO.getCell()))) {
			throw new PhoneException("Número de telefone inválido");
		}
		validateSalary(registerDTO);
		isRecordAlreadyExists(registerDTO);
		registerRepository.save(buildNewRegister(registerDTO));
		return new ResponseEntityCustom(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Registro cadastrado com sucesso!");
	}

	private Register buildNewRegister(RegisterDTO registerDTO) {
		return new Register.Builder()
				.name(registerDTO.getName())
				.email(registerDTO.getEmail())
				.cell(registerDTO.getCell())
				.registrationDate(LocalDate.now())
				.photo(registerDTO.getPhoto())
				.salary(registerDTO.getSalary())
				.others(Objects.isNull(registerDTO.getOthers()) ? 0.00 : registerDTO.getOthers())
				.createNewRegister();
	}

	private void isRecordAlreadyExists(RegisterDTO registerDTO) throws RegisterException, FieldException {
		String name = validateField(registerDTO.getName());
		String email = validateField(registerDTO.getEmail());
		List<Register> registers = registerRepository.findAll();
		for (Register register : registers) {
			if (register.getName().equals(name)
					|| register.getEmail().equals(email)) {
				throw new RegisterException("Registro já cadastrado com o nome ou email");
			}
		}
	}

	private String validateField(String field) throws FieldException {
		if (Objects.isNull(field) || field.isEmpty()) {
			throw new FieldException("Necessário cadastrar nome e email");
		}
		return field;
	}

	private void validateSalary(RegisterDTO registerDTO) throws ValueException {
		if (Objects.isNull(registerDTO.getSalary()) || registerDTO.getSalary() == 0.00) {
			throw new ValueException("valor de salário inválido");
		}
	}

	public List<RegisterDTO> getAllRegisters() throws RegisterNotFoundException {
		List<Register> registers = registerRepository.findAll();
		if (registers.isEmpty()) {
			throw new RegisterNotFoundException("Não foi encontrado registros na base");
		}
		return new ArrayList<>(buildRegisterListDTO(registers));
	}

	private List<RegisterDTO> buildRegisterListDTO(List<Register> registers) {
		List<RegisterDTO> list = new ArrayList<>();
		for (Register register : registers) {
			RegisterDTO registerDTO = new RegisterDTO.Builder()
					.id(register.getIdRegister())
					.name(register.getName())
					.email(register.getEmail())
					.cell(register.getCell())
					.others(register.getOthers())
					.salary(register.getSalary())
					.photo(register.getPhoto())
					.registrationDate(register.getRegistrationDate())
					.createNewRegisterDTO();
			list.add(registerDTO);
		}
		return list;
	}

	public RegisterDTO getRegister(String name) throws RegisterNotFoundException {
		Register register = getRegisterByName(name);
		return new RegisterDTO.Builder()
				.id(register.getIdRegister())
				.name(register.getName())
				.email(register.getEmail())
				.cell(register.getCell())
				.others(register.getOthers())
				.salary(register.getSalary())
				.photo(register.getPhoto())
				.createNewRegisterDTO();
	}

	private Register getRegisterByName(String name) throws RegisterNotFoundException {
		Optional<Register> register = registerRepository.findByName(name);
		if (register.isEmpty()) {
			throw new RegisterNotFoundException("Registro não encontrado na base");
		}
		return register.get();
	}

	public ResponseEntity<HttpStatus> updateRegister(RegisterDTO registerDTO) {
		Register updatedRegister = new Register.Builder()
				.id(registerDTO.getId())
				.name(registerDTO.getName())
				.email(registerDTO.getEmail())
				.registrationDate(registerDTO.getRegistrationDate())
				.cell(registerDTO.getCell())
				.others(registerDTO.getOthers())
				.salary(registerDTO.getSalary())
				.photo(registerDTO.getPhoto())
				.update(LocalDate.now())
				.createNewRegister();
		registerRepository.save(updatedRegister);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	public RegisterDTO getRegisterById(Long registerId) throws RegisterNotFoundException {
		Register register = getRegisterFromDataBase(registerId);
		return new RegisterDTO.Builder()
				.id(register.getIdRegister())
				.name(register.getName())
				.email(register.getEmail())
				.cell(register.getCell())
				.others(register.getOthers())
				.salary(register.getSalary())
				.registrationDate(register.getRegistrationDate())
				.photo(register.getPhoto())
				.createNewRegisterDTO();
	}

	private Register getRegisterFromDataBase(Long registerId) throws RegisterNotFoundException {
		Optional<Register> register = registerRepository.findById(registerId);
		if (register.isEmpty()) {
			throw new RegisterNotFoundException("Registro não encontrado pelo ID");
		}
		return register.get();
	}

	public ResponseEntityCustom deleteRegister(Long registerId) throws RegisterNotFoundException {
		registerRepository.delete(getRegisterFromDataBase(registerId));
		return new ResponseEntityCustom(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, "Registro deletado com sucesso!");
	}

	public DebtValueDTO getTotalEntryValue(Long registerId) throws RegisterNotFoundException {
		Register register = getRegister(registerId);
		double totalEntryValue = register.getSalary() + register.getOthers();
		return new DebtValueDTO.Builder()
				.totalEntryValue(totalEntryValue)
				.createNewDebtValue();
	}

	private Register getRegister(Long registerId) throws RegisterNotFoundException {
		Optional<Register> register = registerRepository.findById(registerId);
		if (register.isEmpty()) {
			throw new RegisterNotFoundException("Não foi encontrado registro na base");
		}
		return register.get();
	}

	public DebtValueDTO getCurrentEntryValue(Long registerId) throws RegisterNotFoundException, DebtNotFoundException {
		Register register = getRegister(registerId);
		double fullDebt = debtService.getFullDebt(registerId).getTotalDebt();
		double currentEntryValue = register.getSalary() - fullDebt;
		return new DebtValueDTO.Builder()
				.currentTotalValue(currentEntryValue)
				.createNewDebtValue();
	}
}
