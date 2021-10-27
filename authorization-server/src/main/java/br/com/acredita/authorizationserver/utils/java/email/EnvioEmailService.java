package br.com.acredita.authorizationserver.utils.java.email;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface EnvioEmailService {

	void enviar(Mensagem mensagem);

	class Mensagem {
		 
		private Set<String> destinatarios;
		private String assunto;
		private String corpo;
		private String template = "";
		private Map<String, String> variaveis = new HashMap<String, String>();

		public static Mensagem builder() {
			return new Mensagem();
		}

		public Mensagem destinatario(String destinatario) {
			Set<String> dest = new HashSet<>();
			dest.add(destinatario);
			this.destinatarios = dest;
			return this;
		}

		public Set<String> getDestinatarios() {
			return destinatarios;
		}

		public Mensagem destinatarios(Set<String> destinatarios) {
			this.destinatarios = destinatarios;
			return this;
		}

		public String getAssunto() {
			return assunto;
		}

		public Mensagem assunto(String assunto) {
			this.assunto = assunto;
			return this;
		}

		public String getCorpo() {
			return corpo;
		}

		public Mensagem corpo(String corpo) {
			this.corpo = corpo;
			return this;
		}

		public String getTemplate() {
			return template;
		}

		public Mensagem template(String template) {
			this.template = template;
			return this;
		}

		public Map<String, String> getVariaveis() {
			return variaveis;
		}

		public Mensagem variaveis(Map<String, String> variaveis) {
			this.variaveis = variaveis;
			return this;
		}

		public Mensagem variavel(String chave, String valor) {
			if (!this.variaveis.containsKey(chave)) {
				this.variaveis.put(chave, valor);
			}
			return this;
		}

		public Mensagem build() {

			// o correto é aqui validar todos os campos e retornar o Mensagem ou gerar uma
			// exception
			return this;
		}
		
	}

}