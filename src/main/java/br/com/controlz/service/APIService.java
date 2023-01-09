package br.com.controlz.service;

import br.com.controlz.api.API;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sibModel.SendSmtpEmail;

import java.util.HashMap;
import java.util.Map;

@Service
public class APIService {

	public static final Long CONNECT_TIME_OUT = 5L;
	public static final Long READ_TIME_OUT = 15L;

	@Value("${SENDINBLUE_URL}")
	private String url;

	@Value("${SENDINBLUE_API_KEY}")
	private String sendMailApiKey;

	public void sendMail(SendSmtpEmail sendSmtpEmail) {
		Map<String, String> headers = createHeaders();
		getClient().sendMail(headers, sendSmtpEmail);
	}

	private Map<String, String> createHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("api-key", sendMailApiKey);
		return headers;
	}

	public API getClient() {
		return new API(url, CONNECT_TIME_OUT, READ_TIME_OUT);
	}
}



