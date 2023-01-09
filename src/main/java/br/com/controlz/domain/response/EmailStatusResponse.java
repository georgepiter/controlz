package br.com.controlz.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailStatusResponse {

	@JsonProperty("event")
	private final String event;

	@JsonProperty("email")
	private final String email;

	@JsonProperty("id")
	private final Long id;

	@JsonProperty("ts")
	private final String ts;

	@JsonProperty("message-id")
	private final String messageId;

	@JsonProperty("ts_event")
	private final String tsEvent;

	@JsonProperty("subject")
	private final String subject;

	@JsonProperty("tag")
	private final String tag;

	@JsonProperty("sending_ip")
	private final String sendingIp;

	@JsonProperty("ts_epoch")
	private final String tsEpoch;

	public EmailStatusResponse(String event, String email, Long id,
							   String ts, String messageId, String tsEvent, String subject,
							   String tag, String sendingIp, String tsEpoch) {
		this.event = event;
		this.email = email;
		this.id = id;
		this.ts = ts;
		this.messageId = messageId;
		this.tsEvent = tsEvent;
		this.subject = subject;
		this.tag = tag;
		this.sendingIp = sendingIp;
		this.tsEpoch = tsEpoch;
	}

	public String getEvent() {
		return event;
	}

	public String getEmail() {
		return email;
	}

	public Long getId() {
		return id;
	}

	public String getTs() {
		return ts;
	}

	public String getMessageId() {
		return messageId;
	}

	public String getTsEvent() {
		return tsEvent;
	}

	public String getSubject() {
		return subject;
	}

	public String getTag() {
		return tag;
	}

	public String getSendingIp() {
		return sendingIp;
	}

	public String getTsEpoch() {
		return tsEpoch;
	}
}
