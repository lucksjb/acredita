package br.com.acredita.authorizationserver.annotations;

public class ObrigatorioAlterarSenhaException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ObrigatorioAlterarSenhaException(String msg) {
		super(msg);
	}

}
