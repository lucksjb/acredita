package br.com.acredita.authorizationserver.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString 
@Entity
@Table(name = "perfil")
public class Perfil extends BaseModel {
	@Column(name = "descricao", length = 50)
	@NotBlank
	private String descricao;

	@ManyToMany
	@JoinTable(name = "perfilPrograma", joinColumns = @JoinColumn(name = "perfil_id"), inverseJoinColumns = @JoinColumn(name = "programa_id"))
	@JsonIgnore
	private List<Programa> permissoes = new ArrayList<>();
}
