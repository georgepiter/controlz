package br.com.controlz.api;

import feign.HeaderMap;
import feign.Headers;
import feign.RequestLine;
import sibModel.SendSmtpEmail;

import java.util.Map;

public interface Client {

	@RequestLine("POST /v3/smtp/email")
	@Headers("Content-Type: application/json")
	void sendMail(@HeaderMap Map<String, String> headers, SendSmtpEmail sendSmtpEmail);
}
