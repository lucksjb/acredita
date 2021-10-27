package br.com.acredita.authorizationserver.enums;

import javax.persistence.Converter;

public enum SimNao implements PersistableEnum<String> {
    SIM("S"), NAO("N");

    private final String name;

    private SimNao(String simNao) {
        this.name = simNao;
    }

    @Override
    public String getCode() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Converter(autoApply = true)
    public static class SimNaoConverter extends GenericEnumJPAConverter<SimNao, String> {
        public SimNaoConverter() {
            super(SimNao.class);
        }
    }
}
