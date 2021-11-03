package br.com.acredita.applicationlog.filters;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.acredita.applicationlog.config.jackson.LocalDateTimeToStringConverter;
import br.com.acredita.applicationlog.config.jackson.StringToLocalDateTimeConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LogFilter {
    @ApiModelProperty(position = 2)
    private Long id;

    @ApiModelProperty(position = 3)
    private String message;

    @ApiModelProperty(position = 4)
    @JsonSerialize(converter = LocalDateTimeToStringConverter.class)
    @JsonDeserialize(converter = StringToLocalDateTimeConverter.class)
    private LocalDateTime startDateTime;

    @ApiModelProperty(position = 5)
	@JsonSerialize(converter = LocalDateTimeToStringConverter.class)
    @JsonDeserialize(converter = StringToLocalDateTimeConverter.class)
    private LocalDateTime endDateTime;
    
}
