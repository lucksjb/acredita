package br.com.acredita.authorizationserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.acredita.authorizationserver.models.Programa;

public interface ProgramaRepository extends JpaRepository<Programa, String>{

}
