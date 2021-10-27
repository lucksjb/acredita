package br.com.acredita.authorizationserver.config.springSecurity;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.acredita.authorizationserver.models.Usuario;
import br.com.acredita.authorizationserver.services.UsuarioService;

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioService usuarioService;
	
    @Autowired
    private Environment environment;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOptional; 
		if(loginName.contains("@")) {
			usuarioOptional = usuarioService.findFirstByEmail(loginName.trim());
		} else {
			usuarioOptional = usuarioService.findFirstByLogin(loginName.trim());
		}
		Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário / senha inválido "));
		// usuario.getPerfis().get(0).getId();
		return new UsuarioSistema(usuario, getPermissoes(usuario));
	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {

		Set<SimpleGrantedAuthority> authorities = new HashSet<>();

		usuario.getPerfis().forEach(perfil -> {
			perfil.getPermissoes().forEach(programa -> {
				if (programa.getEndPoint() != null) {
					String role = "ROLE_" + programa.getEndPoint();
					SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
					if (!authorities.contains(authority)) {
						authorities.add(authority);
					}
				}
			});
		});
		
		// Todos os usuarios terao acesso a essa ROLE
		// Será utilizada para login no modo basic_security
		if(Arrays.asList(environment.getActiveProfiles()).contains("basic-security")) {
			String role = "ROLE_BASIC_AUTHENTICATION";
			authorities.add(new SimpleGrantedAuthority(role));
		}

		return authorities;
	}

	public static String usuarioLogado() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username = "";
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		return username;
	}

}
