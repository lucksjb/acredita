package br.com.acredita.applicationlog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.acredita.applicationlog.DTOout.LogDTOout;
import br.com.acredita.applicationlog.filters.LogFilter;
import br.com.acredita.applicationlog.models.Log;

public interface LogRepositoryQuery {
    public Page<LogDTOout> findByFilter(Pageable pageable, LogFilter filter);

    public List<Log> findToReport(LogFilter filter);

    public List<LogDTOout> findToCombobox();

}
