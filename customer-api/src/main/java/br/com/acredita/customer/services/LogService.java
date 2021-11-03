package br.com.acredita.customer.services;


import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.acredita.customer.DTOin.LoggDTOin;
import br.com.acredita.customer.config.RabbitMqConfiguration;

@Service
public class LogService {
    @Autowired
    private RabbitTemplate rabbitTemplate; 
    
    public void sendMessage(String mensagem) {
        LoggDTOin loggDTOin = new LoggDTOin();
        loggDTOin.setDataHora(LocalDateTime.now());
        loggDTOin.setMensagem(mensagem);

        rabbitTemplate.convertAndSend(RabbitMqConfiguration.EXCHANGE_NAME, "LogExchange.logging", loggDTOin);

    }
}
