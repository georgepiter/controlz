package br.com.controlz.domain.enums;

public enum StatusEnum {

    ACTIVE(1, "ACTIVE"),
    INACTIVE(0, "INACTIVE"),
    CANCELED(2, "CANCELED"),
    PAY(3, "PAY"),
    AWAITING_PAYMENT(4, "AWAITING_PAYMENT"),
    DUE(5, "DUE"),
    VALID(6, "VALID");

    private final Integer value;
    private final String label;

    StatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
