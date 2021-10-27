package br.com.acredita.authorizationserver.annotations;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.acredita.authorizationserver.enums.SimNao;
import br.com.acredita.authorizationserver.models.Usuario;
import br.com.acredita.authorizationserver.repositories.UsuarioRepository;


@Aspect
@Component
public class ObrigaTrocarSenhaAspec {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Before(value = "@within(ObrigaTrocarSenha) || @annotation(ObrigaTrocarSenha)",argNames="JoinPoint")
	public void teste(JoinPoint joinPoint) {
		System.out.println("ObrigaTrocarSenha.before, class: " + joinPoint.getSignature().getDeclaringType().getSimpleName() + 
				", method: " + joinPoint.getSignature().getName());
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		
		Usuario usuario = usuarioRepository.findFirstByLogin(currentPrincipalName).
				orElseThrow(() -> new UsernameNotFoundException("Usuário não existe"));
		if(usuario.getAlterarSenhaProxLogin() == SimNao.SIM) {
			throw new ObrigatorioAlterarSenhaException("Obrigatório alterar a senha");
		}
	}

}
