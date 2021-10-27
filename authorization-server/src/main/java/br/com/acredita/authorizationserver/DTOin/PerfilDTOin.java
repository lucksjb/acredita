package br.com.acredita.authorizationserver.DTOin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import br.com.acredita.authorizationserver.models.Perfil;
import br.com.acredita.authorizationserver.models.Programa;
import br.com.acredita.authorizationserver.repositories.ProgramaRepository;

public class PerfilDTOin  {
	@NotBlank
	private String descricao;
	
	private List<String> programas = new ArrayList<>();

	public PerfilDTOin() {
	}

	public PerfilDTOin(String descricao, List<String> programas) {
		super();
		this.descricao = descricao;
		this.programas = programas;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<String> getProgramas() {
		return programas;
	}

	public void setProgramas(List<String> programas) {
		this.programas = programas;
	}

	public Perfil create(ProgramaRepository programaRepository) {
		PropertyMap<PerfilDTOin, Perfil> propertyMap = new PropertyMap<PerfilDTOin, Perfil>() {
			@Override
			protected void configure() {
				skip(destination.getId());
				skip(destination.getPermissoes());
			}
		};

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(propertyMap);
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

		Perfil perfil = new Perfil();
		perfil.setId(0L);
		modelMapper.map(this, perfil);

		if (this.programas != null) {
			perfil.getPermissoes().clear();
			List<Programa> permissoesList = new ArrayList<>();
			this.programas.forEach(permissao -> {
				Optional<Programa> programa = programaRepository.findById(permissao);
				if (programa.isPresent()) {
					permissoesList.add(programa.get());
				}
			});
			perfil.setPermissoes(permissoesList);
		}
		return perfil;
	}

	public Perfil update(Perfil perfil, ProgramaRepository programaRepository) {
		PropertyMap<PerfilDTOin, Perfil> propertyMap = new PropertyMap<PerfilDTOin, Perfil>() {
			@Override
			protected void configure() {
				skip(destination.getId());
				skip(destination.getPermissoes());
			}
		};

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(propertyMap);
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

		modelMapper.map(this, perfil);

		if (this.programas != null) {
			perfil.getPermissoes().clear();
			List<Programa> permissoesList = new ArrayList<>();
			this.programas.forEach(permissao -> {
				Optional<Programa> programa = programaRepository.findById(permissao);
				if (programa.isPresent()) {
					permissoesList.add(programa.get());
				}
			});
			perfil.setPermissoes(permissoesList);
		}
		return perfil;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); // reflectionToString coloca um
	}

}
