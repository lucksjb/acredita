package br.com.acredita.applicationlog.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "Log")
public class Log  extends BaseModel {

    @Column(name = "dateandtime")
    private LocalDateTime dateAndTime;

    @Column(name = "message", nullable = false, length = 50)
    @NotBlank
    private String message;
 
}
