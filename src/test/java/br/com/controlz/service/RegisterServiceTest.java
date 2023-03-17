package br.com.controlz.service;

import br.com.controlz.domain.dto.RegisterDTO;
import br.com.controlz.domain.dto.ResponseEntityCustom;
import br.com.controlz.domain.entity.Register;
import br.com.controlz.domain.exception.PhoneException;
import br.com.controlz.domain.exception.RegisterException;
import br.com.controlz.domain.exception.RegisterNotFoundException;
import br.com.controlz.domain.exception.ValueException;
import br.com.controlz.domain.repository.RegisterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para RegisterService")
class RegisterServiceTest {

	@Mock
	private RegisterRepository registerRepository;

	@InjectMocks
	private RegisterService registerService;

	@BeforeEach
	void setUp() {
		registerService = new RegisterService(this.registerRepository);
	}

	@Test
	@DisplayName("Deve lançar PhoneException quando o telefone não é válido")
	void shouldThrowPhoneExceptionWhenPhoneIsInvalid() {
		RegisterDTO registerDTO = new RegisterDTO.Builder()
				.cell("12345698")
				.createNewRegisterDTO();

		assertThrows(PhoneException.class, () -> registerService.registerNewPerson(registerDTO));
	}

	@Test
	@DisplayName("Deve salvar um novo registro quando os dados são válidos")
	void shouldSaveNewRegisterWhenDataIsValid() throws ValueException, RegisterException, PhoneException {
		// given
		RegisterDTO registerDTO = new RegisterDTO.Builder()
				.userId(1L)
				.cell("11912345678")
				.salary(5000.0d)
				.createNewRegisterDTO();

		// when
		ResponseEntityCustom response = registerService.registerNewPerson(registerDTO);

		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		assertEquals(HttpStatus.CREATED, response.getError());
		assertEquals("Registro cadastrado com sucesso!", response.getMessage());

		// then
		verify(registerRepository, times(1)).save(any());
		ArgumentCaptor<Register> categoryCaptor = ArgumentCaptor.forClass(Register.class);
		verify(registerRepository).save(categoryCaptor.capture());
	}

	@Test
	@DisplayName("Testa o cadastro para novos valores")
	void testRegisterNewOthersValues() throws RegisterNotFoundException {
		// given
		Long userId = 1L;
		double otherValue = 100.0;
		Register register = new Register.Builder()
				.registrationDate(LocalDate.now())
				.salary(2000.0)
				.others(50.0)
				.cell("123456789")
				.registerId(1L)
				.userId(userId)
				.createNewRegister();

		// when
		when(registerRepository.findByUserId(userId)).thenReturn(Optional.of(register));
		when(registerRepository.save(any(Register.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ResponseEntity<HttpStatus> response = registerService.registerNewOthersValues(userId, otherValue);
		verify(registerRepository).findByUserId(userId);

		Register expectedRegister = new Register.Builder()
				.registrationDate(register.getRegistrationDate())
				.photo(register.getPhoto())
				.salary(register.getSalary())
				.others(register.getOthers() + otherValue)
				.cell(register.getCell())
				.registerId(register.getRegisterId())
				.userId(register.getUserId())
				.createNewRegister();

		verify(registerRepository).save(expectedRegister);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}
