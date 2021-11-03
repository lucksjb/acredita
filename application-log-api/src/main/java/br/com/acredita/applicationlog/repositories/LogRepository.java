package br.com.acredita.applicationlog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.acredita.applicationlog.models.Log;

public interface LogRepository extends JpaRepository<Log, Long>, LogRepositoryQuery {
    
}
