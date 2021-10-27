package br.com.acredita.authorizationserver.models;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.acredita.authorizationserver.enums.SimNao;
import br.com.acredita.authorizationserver.enums.TipoItemMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name = "programa")
public class Programa {
	@EqualsAndHashCode.Include
	@Id
	@Column(name = "id", length = 250)
	private String id;

	@Column(name = "descricao", length = 100)
	private String descricao;

	@Column(name = "icone", length = 50)
	private String icone;

	@Column(name = "route", length = 100)
	private String route;

	@Column(name = "endPoint", length = 150)
	private String endPoint;

	@ManyToOne
	@JoinColumn(name = "programaPai_id", referencedColumnName = "id", nullable = true)
	@Fetch(FetchMode.SELECT)
	@JsonIgnore
	private Programa programaPai;

	@OneToMany(mappedBy = "programaPai")
	@OrderBy("seqMenu")
	private List<Programa> subMenu = new ArrayList<>();

	@Column(name = "tipoItemMenu", length = 1)
	private TipoItemMenu tipoItemMenu;

	@Column(name = "seqMenu")
	private Long seqMenu;

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

	public Programa() {
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

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Transient
	@JsonIgnore
	public boolean isNew() {
		return null == this.getId() || this.getId().equals("");
	}

	@UpdateTimestamp // Essa anota data e hora pega do servidor na hora que atulizar
	public OffsetDateTime getLastUpdate() {
		return lastUpdate;
	}

	@ApiModelProperty(hidden = true) // anota do swagger para não aparecer no swagger
	public void setLastUpdate(OffsetDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@CreationTimestamp
	@Column(updatable = false)
	public OffsetDateTime getCreationDateTime() {
		return creationDateTime;
	}

	@ApiModelProperty(hidden = true) // Anotou metodo set para esconder esse metodo no swagger
	public void setCreationDateTime(OffsetDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	// teve de fazer na mao, pois o programaPai e subMenu fica recursivo
	@Override
	public String toString() {
		return "Programa [id=" + id + ", descricao=" + descricao + ", icone=" + icone + ", Route =" + route
				+ ", EndPoint =" + endPoint + ", tipoItemMenu=" + tipoItemMenu + ", seqMenu=" + seqMenu
				+ ", lastUpdate=" + lastUpdate + ", creationDateTime=" + creationDateTime + "]";
	}
}
