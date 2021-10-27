package br.com.acredita.authorizationserver.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.acredita.authorizationserver.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryQuery {
	Optional<Usuario> findFirstByEmail(String email);
	Optional<Usuario> findFirstByLogin(String login);
}
