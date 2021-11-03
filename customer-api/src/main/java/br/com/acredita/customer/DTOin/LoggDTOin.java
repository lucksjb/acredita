package br.com.acredita.customer.DTOin;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.acredita.customer.config.jackson.LocalDateTimeToStringConverter;
import br.com.acredita.customer.config.jackson.StringToLocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class LoggDTOin {
	@JsonSerialize(converter = LocalDateTimeToStringConverter.class)
    @JsonDeserialize(converter = StringToLocalDateTimeConverter.class)
    private LocalDateTime dataHora;
    private String mensagem;
}
