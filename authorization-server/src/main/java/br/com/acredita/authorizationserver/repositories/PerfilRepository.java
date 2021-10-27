package br.com.acredita.authorizationserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.acredita.authorizationserver.models.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long>,  PerfilRepositoryQuery {
			
}
