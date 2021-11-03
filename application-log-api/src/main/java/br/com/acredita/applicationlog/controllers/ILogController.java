package br.com.acredita.applicationlog.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import br.com.acredita.applicationlog.DTOout.LogDTOout;
import br.com.acredita.applicationlog.filters.LogFilter;
import br.com.acredita.applicationlog.models.Log;

public interface ILogController {
    public Page<LogDTOout> findByFilter(Pageable pageable, LogFilter filter);

    public ResponseEntity<Log> findById(Long id);
    
    public List<LogDTOout> findToCombobox();

    public List<Log> findToReport( LogFilter filter);    
}
