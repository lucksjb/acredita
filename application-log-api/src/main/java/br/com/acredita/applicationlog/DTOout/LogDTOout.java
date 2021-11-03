package br.com.acredita.applicationlog.DTOout;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.acredita.applicationlog.config.jackson.LocalDateTimeToStringConverter;
import br.com.acredita.applicationlog.config.jackson.StringToLocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogDTOout implements Serializable {
    private Long id;
    
    @JsonSerialize(converter = LocalDateTimeToStringConverter.class)
    @JsonDeserialize(converter = StringToLocalDateTimeConverter.class)
    private LocalDateTime dateAndTime;

    private String message;

}
