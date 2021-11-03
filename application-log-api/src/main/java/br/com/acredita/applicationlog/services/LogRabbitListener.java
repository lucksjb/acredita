package br.com.acredita.applicationlog.services;

import java.io.IOException;

import com.rabbitmq.client.Channel;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import br.com.acredita.applicationlog.DTOin.LogDTOin;
import br.com.acredita.applicationlog.config.CacheConfiguration;
import br.com.acredita.applicationlog.models.Log;
import br.com.acredita.applicationlog.repositories.LogRepository;

@Configuration
public class LogRabbitListener {
    @Autowired
    private LogRepository logRepository;

    @Autowired
    private CacheManager cacheManager;

    @Transactional
    @RabbitListener(queues = { "logging" }, ackMode = "MANUAL" )
    public void create(LogDTOin logDTOin, Channel channel) throws IOException {
       logRepository.save(new Log(logDTOin.getDataHora(), logDTOin.getMensagem()));
       cacheManager.getCache(CacheConfiguration.LOG).clear(); // mesmo que @CacheEvict(....)  só que mais rapido 

        channel.basicAck(0L, true);  // será excluida da fila somente após este comando.
    } 
    
}
