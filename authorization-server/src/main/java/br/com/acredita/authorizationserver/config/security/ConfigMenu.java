package br.com.acredita.authorizationserver.config.security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigMenu {
	@Autowired
	private CriaMenu criaMenu;

	@PostConstruct
	public void init() {
		criaMenu.cadastraMenuByXml();
	}
}
