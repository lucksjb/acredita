package br.com.acredita.applicationlog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.acredita.applicationlog.DTOout.LogDTOout;
import br.com.acredita.applicationlog.filters.LogFilter;
import br.com.acredita.applicationlog.models.Log;


public interface ILogService {
    public Page<LogDTOout> findByFilter(Pageable pageable, LogFilter filter);
    public List<LogDTOout> findToCombobox();

    public Optional<Log> findById(Long id);

    
    public List<Log> findToReport(LogFilter filter);
}
