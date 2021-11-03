package br.com.acredita.applicationlog.config.jackson;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.util.StdConverter;

public class StringToLocalDateTimeConverter extends StdConverter<String, LocalDateTime> {

  @Override
  public LocalDateTime convert(String value) {
      return LocalDateTime.parse(value, LocalDateTimeToStringConverter.DATE_FORMATTER);
  }
}