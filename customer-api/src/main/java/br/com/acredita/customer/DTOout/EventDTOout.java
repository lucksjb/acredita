package br.com.acredita.customer.DTOout;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTOout implements Serializable {
    private String uid;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ultConsulta;
    private List<MovFinDTOout> movfinan = new ArrayList<>();

    private UltCompraCartaoDTOout ultCompraCartao;
}

