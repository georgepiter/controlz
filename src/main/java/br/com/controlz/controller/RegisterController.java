package br.com.controlz.controller;

import br.com.controlz.domain.dto.DebtValueDTO;
import br.com.controlz.domain.dto.RegisterDTO;
import br.com.controlz.domain.exception.*;
import br.com.controlz.service.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/register")
public class RegisterController {

	private final RegisterService registerService;

	public RegisterController(RegisterService registerService) {
		this.registerService = registerService;
	}

	@PostMapping(value = "/")
	public ResponseEntity<HttpStatus> registerNewPerson(@RequestBody RegisterDTO registerDTO) throws ValueException, RegisterException, PhoneException, EmailException, FieldException {
		return registerService.registerNewPerson(registerDTO);
	}

	@GetMapping(value = "/id/{id}")
	public RegisterDTO getRegisterById(@PathVariable Long id) throws RegisterNotFoundException {
		return registerService.getRegisterById(id);
	}

	@GetMapping(value = "/all")
	public List<RegisterDTO> getAllRegisters() throws RegisterNotFoundException {
		return registerService.getAllRegisters();
	}

	@GetMapping(value = "/name/{name}")
	public RegisterDTO getRegister(@PathVariable String name) throws RegisterNotFoundException {
		return registerService.getRegister(name);
	}

	@GetMapping(value = "/totalEntryValue/{registerId}")
	public DebtValueDTO getTotalEntryValue(@PathVariable Long registerId) throws RegisterNotFoundException {
		return registerService.getTotalEntryValue(registerId);
	}

	@GetMapping(value = "/currentEntryValue/{registerId}")
	public DebtValueDTO getCurrentEntryValue(@PathVariable Long registerId) throws RegisterNotFoundException, DebtNotFoundException {
		return registerService.getCurrentEntryValue(registerId);
	}

	@PutMapping
	public ResponseEntity<HttpStatus> updateStudent(@RequestBody RegisterDTO registerDTO) {
		return registerService.updateRegister(registerDTO);
	}

	@DeleteMapping(value = "/id/{id}")
	public ResponseEntity<HttpStatus> deleteRegister(@PathVariable Long id) throws RegisterNotFoundException {
		return registerService.deleteRegister(id);
	}

}
