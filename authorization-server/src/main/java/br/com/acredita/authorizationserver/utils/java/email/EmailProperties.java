package br.com.acredita.authorizationserver.utils.java.email;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
@ConfigurationProperties("utils.email")
public class EmailProperties {

	@NotNull
	private String remetente = "Não responder lucksjb@gmail.com";

	public String getRemetente() {
		return remetente;
	}

	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}

}