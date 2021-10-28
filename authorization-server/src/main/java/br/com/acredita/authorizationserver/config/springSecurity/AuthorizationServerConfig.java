package br.com.acredita.authorizationserver.config.springSecurity;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import br.com.acredita.authorizationserver.config.security.SegurancaProperties;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private SegurancaProperties properties;

	@Autowired 
	private ClientDetailsService clientDetailsService;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
					.withClient("any")
						.secret(passwordEncoder.encode("canela-seca")) 
						.scopes("read", "write")
						.authorizedGrantTypes("password", "refresh_token")
						.accessTokenValiditySeconds(60 * 60 * 10) // 10 horas
						.refreshTokenValiditySeconds(60 * 60 * 18) // 18 horas
				.and()
					.withClient("internal")
						.secret(passwordEncoder.encode("batatinha-frita-123")) 
						.scopes("read", "write")
						.authorizedGrantTypes("client_credentials")
						.accessTokenValiditySeconds(60 * 60 * 48); // 48 horas


	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));

		endpoints.tokenStore(tokenStore())
				.tokenEnhancer(tokenEnhancerChain)
				.reuseRefreshTokens(false)
				.userDetailsService(userDetailsService)
				.authenticationManager(authenticationManager);
	}

	//implementação JWKS - JSON WEB WEK SET RFC 7517
	// https://www.bortzmeyer.org/7517.pdf


	// Permite obter chave publica :
	// http://localhost:8081/oauth/check_token
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.checkTokenAccess("permitAll()")
		.tokenKeyAccess("permitAll()")
		.allowFormAuthenticationForClients();
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		// para gerar o arquivo jks :
		// keytool -genkeypair -alias aCreditaKey -keyalg RSA -keypass 123456 -keystore
		// aCredita.jks -storepass 123456 -validity 3650
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

		// se for assinar simetrico
		// accessTokenConverter.setSigningKey("<@R2i<@"); //MAC - message
		// autethentication code

		var jksResource = new ClassPathResource(properties.getJwtKeystorePath());
		var keyStorePass = properties.getJwtKeystorePassword();
		var keyPairAlias = properties.getJwtKeystoreKeypairAlias();

		var keyStoreKeyFactory = new KeyStoreKeyFactory(jksResource, keyStorePass.toCharArray());
		var keyPair = keyStoreKeyFactory.getKeyPair(keyPairAlias);

		jwtAccessTokenConverter.setKeyPair(keyPair);

		return jwtAccessTokenConverter;
	}

		
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	@Bean
	DefaultOAuth2RequestFactory defaultOAuth2RequestFactory() {
		return new DefaultOAuth2RequestFactory(clientDetailsService);
	}
}