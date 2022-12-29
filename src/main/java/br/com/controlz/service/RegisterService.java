package br.com.controlz.service;

import br.com.controlz.domain.dto.RegisterDTO;
import br.com.controlz.domain.entity.Register;
import br.com.controlz.domain.exception.*;
import br.com.controlz.domain.repository.RegisterRepository;
import br.com.controlz.utils.EmailUtils;
import br.com.controlz.utils.PhoneUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
public class RegisterService {

	private final RegisterRepository repository;

	public RegisterService(RegisterRepository repository) {
		this.repository = repository;
	}

	public ResponseEntity<HttpStatus> registerNewPerson(RegisterDTO registerDTO) throws ValueException, RegisterException, PhoneException, EmailException, FieldException {
		if (Boolean.FALSE.equals(EmailUtils.isEmailPatternValid(registerDTO.getEmail()))) {
			throw new EmailException("Email inválido");
		}
		if (Boolean.FALSE.equals(PhoneUtils.validatePhone(registerDTO.getCell()))) {
			throw new PhoneException("Número de cell inválido");
		}
		validateSalary(registerDTO);
		isRecordAlreadyExists(registerDTO);
		saveNewRegister(buildNewRegister(registerDTO));
		return ResponseEntity.ok(HttpStatus.CREATED);
	}

	private Register buildNewRegister(RegisterDTO registerDTO) {
		return new Register.Builder()
				.name(registerDTO.getName())
				.email(registerDTO.getEmail())
				.cell(registerDTO.getCell())
				.registrationDate(LocalDate.now())
				.photo(registerDTO.getPhoto())
				.salary(registerDTO.getSalary())
				.others(registerDTO.getOthers())
				.createNewRegister();
	}

	private void isRecordAlreadyExists(RegisterDTO registerDTO) throws RegisterException, FieldException {
		String name = validateField(registerDTO.getName());
		String email = validateField(registerDTO.getEmail());
		Optional<Register> register = repository.findByNameAndEmail(name, email);
		if (Objects.nonNull(register)) {
			throw new RegisterException("Registro já cadastrado com esse nome e email");
		}
	}

	private String validateField(String field) throws FieldException {
		if (Objects.isNull(field) || field.isEmpty()) {
			throw new FieldException("Necessário cadastrar nome e email");
		}
		return field;
	}

	private void saveNewRegister(Register register) {
		repository.save(register);
	}

	private void validateSalary(RegisterDTO registerDTO) throws ValueException {
		if (Objects.isNull(registerDTO.getSalary()) || registerDTO.getSalary() == 0.00) {
			throw new ValueException("valor de salário inválido");
		}
	}
}
