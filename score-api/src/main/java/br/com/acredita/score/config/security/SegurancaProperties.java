package br.com.acredita.score.config.security;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import br.com.acredita.score.enums.SimNao;

@Component
@ConfigurationProperties(prefix = "seguranca")
public class SegurancaProperties {
	/**
	 * Email do String administrador ao instalar o sistema
	 */
	private String installEmailAdmin = "lucksjb@gmail.com";

	/**
	 * Telefone do usuário administrador ao instalar o sistema
	 */
	private String installFoneAdmin = "16 98129-5353";

	/**
	 * O usuário administrador deverá alterar sua senha no primeiro login ? S/N
	 * (default S)
	 */
	private SimNao installAlteraSenhaProxLogin = SimNao.SIM;

	/**
	 * Senha pardrão para novo usuário (default 123) utilizado também ao rodar o
	 * sistema a primeira vez e atribuir ao administrador
	 */
	private String novoUsuarioSenhaPadrao = "123";

	/**
	 * Novos usuários deverão alterar sua senha no primeiro login ? S/N (default S)
	 */
	private SimNao novoUsuarioAlteraSenhaProxLogin = SimNao.SIM;

	/**
	 * Qual a validade da senha em dias para novos usuários ? (default 90 dias)
	 */
	private Long novoUsuarioValidadeSenhaDias = 90L;

	/**
	 * Exige senha forte para novos usuários tem de conter no minimo 6 digitos, com
	 * letras maiúsculas, minúsculas, números e símbolos
	 */
	private SimNao novoUsuarioExigeSenhaForte = SimNao.SIM;

	/**
	 * Especificar se vai ou não utilizar o HTTPS - (Default N);
	 * 
	 */
	private SimNao habilitaHttps = SimNao.NAO;

	/**
	 * Especificar se a senha será salva encoded ou nao - default S;
	 * 
	 */
	private SimNao senhaEncoded = SimNao.SIM;

	/**
	 * Especificar o CORS - Cross Origin Resource Sharing; que é de onde pode ser
	 * acessado a api ....
	 */
	private String corsOrigemPermitida = "http://localhost:4200";

	/**
	 * Quando usar oauth-security
	 * 
	 * O QUE É O AUTHORIZATIONKEY o authorizationKey é uma chave que credencia a
	 * aplicação cliente à consumir a api. (NÃO CONFUNDIR COM AS CREDENCIAIS DO
	 * USUÁRIO) o authorizationKey é do aplicativo cliente.
	 * 
	 * 
	 * ONDE IREI UTILIZAR o authorizationKey deverá ser passado no header da
	 * requisição HTTP que obtem o token POST http://localhost:8080/oauth/token
	 * 
	 * 
	 * HEADERS content-type:application/xwww-form-urlencoded Authorization:Basic
	 * <authorizationKey encodada>
	 * 
	 * 
	 * COMO É COMPOSTO O authorizationKey: O authorizationKey é composto por um
	 * clientId e secretId encodados em base64. Para encodar: utilizar o site
	 * www.base64encode.org e encodar a seguinte string <clientId>:<secretId> no
	 * nosso caso 'angular:@ngul@r0' que irá gerar a seguinte chave encriptada
	 * 'YW5ndWxhcjpAbmd1bEByMA=='
	 * 
	 * 
	 * ONDE O PROGRAMADOR DA API DEFINE O clientId e secretID: O clientId e secretId
	 * tem de ser definidas no AuthorizationServerConfig nas linhas
	 * clients.inMemory() .withClient("angular")
	 * .secret("$2a$10$4CvdsdqhNu/A1ERtlyqOYeSbwnRbL7xCbPclZ7k3o6HvWw0oU3v1u")
	 * // @ngul@r0
	 *
	 * observe que o secretId está codificado, isso deve ser feito utilizando o
	 * .seguranca.springSecurity.util.GeradorSenha clientId e secretId - default
	 * ('angular')
	 */
	private String authorizationKey = "YW5ndWxhcjpAbmd1bEByMA==";

	/**
	 * Em desenvolvimento deixar essa variavel para true Habilita o swagger na
	 * aplicação
	 */
	private SimNao swaggerAberto = SimNao.NAO;

	/**
	 * No reset da senha a nova senha será a senha padrão ou será criado senha
	 * aleatoria? SimNao = default (S)
	 */
	private SimNao senhaDeResetPadrao = SimNao.SIM;

	/**
	 * Quando solicitado o reset da senha, o link ficará valido por quanto tempo em
	 * minutos? default (5 minutos)
	 */
	private Long tempoEmMinutosExpiraLinkResetSenha = 5L;

	/**
	 * Arquivo contendo par de chaves
	 * 
	 */
	private String jwtKeystorePath="keystores/aCredita.jks";

	/**
	 * senha do arquivo de par de chaves 
	 * 
	 */
	private String jwtKeystorePassword="123456";

	/**
	 * nome do par de chaves 
	 * 
	 */

	private String jwtKeystoreKeypairAlias="aCreditaKey";
	
	
	public String getInstallEmailAdmin() {
		return installEmailAdmin;
	}

	public void setInstallEmailAdmin(String installEmailAdmin) {
		this.installEmailAdmin = installEmailAdmin;
	}

	public String getInstallFoneAdmin() {
		return installFoneAdmin;
	}

	public void setInstallFoneAdmin(String installFoneAdmin) {
		this.installFoneAdmin = installFoneAdmin;
	}

	public String getNovoUsuarioSenhaPadrao() {
		return novoUsuarioSenhaPadrao;
	}

	public void setNovoUsuarioSenhaPadrao(String novoUsuarioSenhaPadrao) {
		this.novoUsuarioSenhaPadrao = novoUsuarioSenhaPadrao;
	}

	public SimNao getInstallAlteraSenhaProxLogin() {
		return installAlteraSenhaProxLogin;
	}

	public void setInstallAlteraSenhaProxLogin(SimNao installAlteraSenhaProxLogin) {
		this.installAlteraSenhaProxLogin = installAlteraSenhaProxLogin;
	}

	public SimNao getNovoUsuarioAlteraSenhaProxLogin() {
		return novoUsuarioAlteraSenhaProxLogin;
	}

	public void setNovoUsuarioAlteraSenhaProxLogin(SimNao novoUsuarioAlteraSenhaProxLogin) {
		this.novoUsuarioAlteraSenhaProxLogin = novoUsuarioAlteraSenhaProxLogin;
	}

	public Long getNovoUsuarioValidadeSenhaDias() {
		return novoUsuarioValidadeSenhaDias;
	}

	public void setNovoUsuarioValidadeSenhaDias(Long novoUsuarioValidadeSenhaDias) {
		this.novoUsuarioValidadeSenhaDias = novoUsuarioValidadeSenhaDias;
	}

	public SimNao getNovoUsuarioExigeSenhaForte() {
		return novoUsuarioExigeSenhaForte;
	}

	public void setNovoUsuarioExigeSenhaForte(SimNao novoUsuarioExigeSenhaForte) {
		this.novoUsuarioExigeSenhaForte = novoUsuarioExigeSenhaForte;
	}

	public SimNao getHabilitaHttps() {
		return habilitaHttps;
	}

	public void setHabilitaHttps(SimNao habilitaHttps) {
		this.habilitaHttps = habilitaHttps;
	}

	public SimNao getSenhaEncoded() {
		return senhaEncoded;
	}

	public void setSenhaEncoded(SimNao senhaCodada) {
		this.senhaEncoded = senhaCodada;
	}

	public String getCorsOrigemPermitida() {
		return corsOrigemPermitida;
	}

	public void setCorsOrigemPermitida(String corsOrigemPermitida) {
		this.corsOrigemPermitida = corsOrigemPermitida;
	}

	public String getAuthorizationKey() {
		return authorizationKey;
	}

	public void setAuthorizationKey(String authorizationKey) {
		this.authorizationKey = authorizationKey;
	}

	public SimNao getSwaggerAberto() {
		return swaggerAberto;
	}

	public void setSwaggerAberto(SimNao swaggerAberto) {
		this.swaggerAberto = swaggerAberto;
	}

	public SimNao getSenhaDeResetPadrao() {
		return senhaDeResetPadrao;
	}

	public void setSenhaDeResetPadrao(SimNao senhaDeResetPadrao) {
		this.senhaDeResetPadrao = senhaDeResetPadrao;
	}

	public Long getTempoEmMinutosExpiraLinkResetSenha() {
		return tempoEmMinutosExpiraLinkResetSenha;
	}

	public void setTempoEmMinutosExpiraLinkResetSenha(Long tempoEmMinutosExpiraLinkResetSenha) {
		this.tempoEmMinutosExpiraLinkResetSenha = tempoEmMinutosExpiraLinkResetSenha;
	}

	
	public String getJwtKeystorePath() {
		return jwtKeystorePath;
	}

	public void setJwtKeystorePath(String jwtKeystorePath) {
		this.jwtKeystorePath = jwtKeystorePath;
	}

	public String getJwtKeystorePassword() {
		return jwtKeystorePassword;
	}

	public void setJwtKeystorePassword(String jwtKeystorePassword) {
		this.jwtKeystorePassword = jwtKeystorePassword;
	}

	public String getJwtKeystoreKeypairAlias() {
		return jwtKeystoreKeypairAlias;
	}

	public void setJwtKeystoreKeypairAlias(String jwtKeystoreKeypairAlias) {
		this.jwtKeystoreKeypairAlias = jwtKeystoreKeypairAlias;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
