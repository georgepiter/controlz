package br.com.controlz.domain.enums;

public enum RoleEnum {

    ADMIN(1, "ROLE_ADMIN"),
    MANAGER(2, "ROLE_MANAGER");

    private final Long cod;
    private final String description;

    private RoleEnum(Integer cod, String description) {
        this.cod = Long.valueOf(cod);
        this.description = description;
    }

    public Long getCod() {
        return cod;
    }

    public String getDescription() {
        return description;
    }

    public static RoleEnum toEnum(Long cod) {
        if (cod == null) {
            return null;
        }
        for (RoleEnum x : RoleEnum.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Invalid Id: " + cod);
    }
}


