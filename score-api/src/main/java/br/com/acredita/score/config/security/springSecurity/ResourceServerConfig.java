package br.com.acredita.score.config.security.springSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;


// essa aula tem uma boa explicação sobre o cors 
// https://www.algaworks.com/aulas/2246/testando-o-fluxo-authorization-code-com-um-client-javascript/
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


	@Override
	public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
			 // .antMatchers("/api/private/perfis/**").permitAll()
			 // .antMatchers("/api/private/usuarios/**").permitAll()
			.antMatchers("/v2/api-docs").permitAll()
			.antMatchers("/**").permitAll()
			.antMatchers("/**/favicon.ico").permitAll()
			.antMatchers("/swagger-ui.html").permitAll()
			.antMatchers("/swagger-resources").permitAll()
			.antMatchers("/swagger-resources/configuration/ui").permitAll()
			.antMatchers("/swagger-resources/configuration/security").permitAll();
	http.authorizeRequests().anyRequest().authenticated()
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.csrf().disable();
	
	} 

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.stateless(true);
	}

	@Bean
	public MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}
}