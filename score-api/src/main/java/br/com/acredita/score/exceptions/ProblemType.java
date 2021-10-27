package br.com.acredita.score.exceptions;


import lombok.Getter;

@Getter
public enum ProblemType {

	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"), 
	OBRIGATORIO_TROCA_DE_SENHA("/obrigatorio-troca-de-senha", "Obrigatório troca de senha"), 
	ACESSO_NEGADO("/acesso-negado","Acesso Negado - você não tem permissão para acessar esse recurso"), 
	ERRO_AO_ENVIAR_O_EMAIL("/erro-ao-enviar-o-email","Erro ao enviar o email"), 
	OPERACAO_NAO_PERMITIDA("/operacao-nao-permitida","operação não permitida"), ;
	
	private String title;
	private String uri;
	
	ProblemType(String path, String title) {
		this.uri = "https://acredita.com.br" + path;
		this.title = title;
	}
	
}