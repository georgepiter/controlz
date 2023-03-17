package br.com.controlz.domain;

public final class Constant {

	private Constant() {
	}

	public static final Integer EMAIL_SEND_ERROR = 1;
	public static final Integer EMAIL_SEND_TRUE = 0;
	public static final Long TEMPLATE_ID_NEW_PASSWORD = 7L;
	public static final Long TEMPLATE_ID_DEBT = 8L;
	public static final String NEW_PASSWORD = "Nova senha para acesso ao sistema";
	public static final String NAME = "NAME";
	public static final String PASSWORD = "NEW_PASSWORD";
	public static final String EMAIL_SENDER = "ControlZ - Controle Financeiro Pessoal";
	public static final String EMAIL_DEFERRED = "deferred";
	public static final String EMAIL_COMPLAINT = "complaint";
	public static final String EMAIL_INVALID = "invalid_email";
	public static final String EMAIL_ERROR = "error";
	public static final String EMAIL_SOFT_BOUNCE = "soft_bounce";
	public static final String EMAIL_HARD_BOUNCE = "hard_bounce";
	public static final String EMAIL_DELIVERED = "delivered";
	public static final String VALUE = "VALUE";
	public static final String DUE_DATE = "DUE_DATE";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String DUE_DATE_WARNING = "Alerta de vencimento do débito";
}
