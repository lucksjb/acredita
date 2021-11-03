package br.com.acredita.applicationlog.config.jackson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.util.StdConverter;

public class LocalDateTimeToStringConverter extends StdConverter<LocalDateTime, String> {
  static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public String convert(LocalDateTime value) {
      return value.format(DATE_FORMATTER);
  }
}