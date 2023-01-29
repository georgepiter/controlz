package br.com.controlz.service;

import br.com.controlz.domain.dto.RegisterDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.entity.Register;
import br.com.controlz.domain.exception.PhoneException;
import br.com.controlz.domain.exception.RegisterException;
import br.com.controlz.domain.exception.RegisterNotFoundException;
import br.com.controlz.domain.exception.ValueException;
import br.com.controlz.domain.repository.RegisterRepository;
import br.com.controlz.utils.PhoneUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
public class RegisterService {

	private final RegisterRepository registerRepository;

	public RegisterService(RegisterRepository registerRepository) {
		this.registerRepository = registerRepository;
	}

	public ResponseEntityCustom registerNewPerson(RegisterDTO registerDTO) throws ValueException, RegisterException, PhoneException {
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
				.userId(registerDTO.getUserId())
				.cell(registerDTO.getCell())
				.registrationDate(LocalDate.now())
				.photo(registerDTO.getPhoto())
				.salary(registerDTO.getSalary())
				.others(Objects.isNull(registerDTO.getOthers()) ? 0.00 : registerDTO.getOthers())
				.createNewRegister();
	}

	private void isRecordAlreadyExists(RegisterDTO registerDTO) throws RegisterException {
		Optional<Register> register = registerRepository.findByUserId(registerDTO.getUserId());
		if (register.isPresent()) {
			throw new RegisterException("Registro já cadastrado para utilizador");
		}
	}

	private void validateSalary(RegisterDTO registerDTO) throws ValueException {
		if (Objects.isNull(registerDTO.getSalary()) || registerDTO.getSalary() == 0.00) {
			throw new ValueException("valor de salário inválido");
		}
	}

	public ResponseEntity<HttpStatus> updateRegister(RegisterDTO registerDTO) {
		Register updatedRegister = new Register.Builder()
				.userId(registerDTO.getUserId())
				.registerId(registerDTO.getIdRegister())
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

	public RegisterDTO getRegisterById(Long userId) {
		Optional<Register> register = registerRepository.findById(userId);

		return register.map(value -> new RegisterDTO.Builder()
				.userId(value.getUserId())
				.idRegister(value.getRegisterId())
				.cell(value.getCell())
				.others(value.getOthers())
				.salary(value.getSalary())
				.registrationDate(value.getRegistrationDate())
				.photo(value.getPhoto())
				.createNewRegisterDTO()).orElseGet(RegisterDTO::new);
	}


	private Register getRegisterFromDataBase(Long userId) throws RegisterNotFoundException {
		Optional<Register> register = registerRepository.findByUserId(userId);
		if (register.isEmpty()) {
			throw new RegisterNotFoundException("Registro não encontrado pelo ID");
		}
		return register.get();
	}

	public ResponseEntityCustom deleteRegister(Long userId) throws RegisterNotFoundException {
		registerRepository.delete(getRegisterFromDataBase(userId));
		return new ResponseEntityCustom(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, "Registro deletado com sucesso!");
	}

}
