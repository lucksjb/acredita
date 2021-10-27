package br.com.acredita.authorizationserver.enums;

import javax.persistence.Converter;

public enum TipoItemMenu implements PersistableEnum<String> { 
	ITEM("I"), 
	DIVISOR("D"), 
	SUBMENU("S"), 
	ENDPOINT("E"), 
	ROUTE("R"), 
	COMPONENT("C"); 
	private final String name;
	
	private TipoItemMenu(String tipoItemMenu) {
		this.name = tipoItemMenu;
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
    public static class TipoItemMenuConverter extends GenericEnumJPAConverter<TipoItemMenu, String> {
        public TipoItemMenuConverter() {
            super(TipoItemMenu.class);
        }
    }
}
