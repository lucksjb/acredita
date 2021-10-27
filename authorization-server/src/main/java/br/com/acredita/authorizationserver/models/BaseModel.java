package br.com.acredita.authorizationserver.models;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.acredita.authorizationserver.enums.SimNao;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@MappedSuperclass
public class BaseModel implements Cloneable {
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 1)
	private SimNao active;
	
	@Column(length = 36)
	private String uuid;

	@UpdateTimestamp // Essa anota data e hora pega do servidor na hora que atulizar
	@ApiModelProperty(hidden = true) // anota do swagger para não aparecer no swagger
	private OffsetDateTime lastUpdate;

	@CreationTimestamp
	@Column(updatable = false)
	@ApiModelProperty(hidden = true) // Anotou metodo set para esconder esse metodo no swagger
	private OffsetDateTime creationDateTime;
	
	public BaseModel() {
		this.active = SimNao.SIM;
	}
	
	
	private void setActive(SimNao active) {
		this.active = active;
	}
	
	@PrePersist
	public void onInsert() {
		// https://www.uuidgenerator.net/
		this.uuid = UUID.randomUUID().toString();
	}
	
	
	public void activate() {
		setActive(SimNao.SIM);
	}
	
	public void inactivate() {
		setActive(SimNao.NAO);
	}
	
	@Transient
	@JsonIgnore
	public boolean isNew() {
		return null == this.getId() || this.getId() == 0L;
	}
	
	public Object clone() {  
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}  
	}  
}
