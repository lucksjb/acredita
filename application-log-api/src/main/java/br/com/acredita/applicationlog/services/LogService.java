package br.com.acredita.applicationlog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.acredita.applicationlog.DTOout.LogDTOout;
import br.com.acredita.applicationlog.config.CacheConfiguration;
import br.com.acredita.applicationlog.filters.LogFilter;
import br.com.acredita.applicationlog.models.Log;
import br.com.acredita.applicationlog.repositories.LogRepository;

@Service
public class LogService implements ILogService{
    
    @Autowired
    private LogRepository logRepository;


    @Override
    @Cacheable(CacheConfiguration.LOG)
    public Page<LogDTOout> findByFilter(Pageable pageable, LogFilter filter) {
        return logRepository.findByFilter(pageable, filter);
    }

    @Override
    @Cacheable(CacheConfiguration.LOG)
    public List<LogDTOout> findToCombobox() {
        return logRepository.findToCombobox();
    }

    @Override
    @Cacheable(CacheConfiguration.LOG)
    public Optional<Log> findById(Long id) {
        return logRepository.findById(id);
    }

    @Override
    @Cacheable(CacheConfiguration.LOG)
    public List<Log> findToReport(LogFilter filter) {
        return logRepository.findToReport(filter);
    }
}
