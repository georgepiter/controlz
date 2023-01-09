package br.com.controlz.domain;

public final class Constant {

	private Constant() {
	}

	public static final String ERROR_CONVERTING_DATA = "Error converting data!\n";
	public static final String ERROR = "Error!";
	public static final String ADDRESS_NOT_FOUND = "Address not found";
	public static final String EMAIL_PATTERN_INVALID = "Email not valid";
	public static final String USER_NOT_FOUND = "User not found";
	public static final String CONTRACT_NOT_FOUND = "Contract not found";
	public static final String STUDENT_NOT_FOUND = "Student notFound";
	public static final String DUE_DATE = "DUE_DATE";
	public static final String TOTAL_INSTALLMENTS = "TOTAL_INSTALLMENTS";
	public static final String CONTRACT = "CONTRACT";
	public static final String ITEM = "ITEM";
	public static final String DUE_DATE_IN = "DUE_DATE_IN";
	public static final String DUE_DATE_OUT = "DUE_DATE_OUT";
	public static final String TOTAL_VALUE = "TOTAL_VALUE";
	public static final String PAYMENT_DATE = "PAYMENT_DATE";
	public static final String INSTALLMENT_NUMBER = "INSTALLMENT_NUMBER";
	public static final String INSTALLMENT_VALUE = "INSTALLMENT_VALUE";
	public static final String REGISTER_NEW_STUDENT = "Aviso de Matrícula";
	public static final String PAYMENT_NOTICE = "Aviso de Pagamento";
	public static final String REGISTRATION_INACTIVATED = "Pagamento congelado pois a matricula foi inativada";
	public static final String REGISTRATION_ACTIVED = "Matricula reativada, mensalidade liberada para pagamento";
	public static final String CONTRACT_PLAN = "Informações do plano contratado";
	public static final String VALUE_REGISTERED = "Informações de valor avulso registrado";
	public static final String INSTALLMENT_DUE = "Aviso de vencimento da mensalidade";
	public static final Integer EMAIL_SEND_ERROR = 1;
	public static final Boolean EMAIL_SEND = true;
	public static final Boolean EMAIL_NOT_SEND = false;
	public static final Long CONNECT_TIME_OUT = 5L;
	public static final Long READ_TIME_OUT = 15L;
	public static final Long TEMPLATE_ID_NEW_STUDENT = 1L;
	public static final Long TEMPLATE_ID_NEW_VALUE = 2L;
	public static final Long TEMPLATE_ID_NEW_PLAN = 3L;
	public static final Long TEMPLATE_ID_NEW_PAYMENT = 4L;
	public static final Long TEMPLATE_ID_INSTALLMENT_DUE = 6L;
	public static final Long TEMPLATE_ID_NEW_PASSWORD = 7L;
	public static final String NEW_PASSWORD = "Nova senha para acesso ao sistema";
	public static final String NAME = "NAME";
	public static final String PASSWORD = "NEW_PASSWORD";
	public static final String EMAIL_SENDER = "ControlZ - Controle Financeiro Pessoal";
	public static final String EMAIL_DELIVERED = "delivered";
	public static final String EMAIL_DEFERRED = "deferred";
	public static final String EMAIL_COMPLAINT = "complaint";
	public static final String EMAIL_INVALID = "invalid_email";
	public static final String EMAIL_ERROR = "error";
	public static final String EMAIL_SOFT_BOUNCE = "soft_bounce";
	public static final String EMAIL_HARD_BOUNCE = "hard_bounce";
	public static final Integer EMAIL_SEND_TRUE = 0;
}
