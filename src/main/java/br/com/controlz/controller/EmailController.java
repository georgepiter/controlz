package br.com.controlz.controller;

import br.com.controlz.domain.response.EmailStatusResponse;
import br.com.controlz.service.MailBuildService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@Api(value = "Email", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"Email"}, hidden = true)
@RequestMapping(value = "api/v1/email")
public class EmailController {

	private final MailBuildService mailbuildService;

	public EmailController(MailBuildService mailbuildService) {
		this.mailbuildService = mailbuildService;
	}

	@PostMapping
	public void validateEmailStatus(@RequestBody EmailStatusResponse emailStatusResponse) {
		mailbuildService.validateEmailStatus(emailStatusResponse);
	}
}
