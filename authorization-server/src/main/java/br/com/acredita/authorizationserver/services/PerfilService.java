package br.com.acredita.authorizationserver.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.acredita.authorizationserver.DTOin.PerfilDTOin;
import br.com.acredita.authorizationserver.DTOout.PerfilDTO;
import br.com.acredita.authorizationserver.filters.PerfilFilter;
import br.com.acredita.authorizationserver.models.MenuModel;
import br.com.acredita.authorizationserver.models.Perfil;
import br.com.acredita.authorizationserver.repositories.PerfilRepository;
import br.com.acredita.authorizationserver.repositories.ProgramaRepository;

@Service
@Transactional
public class PerfilService  {

	@Autowired
	private PerfilRepository perfilRepository;
	
	@Autowired
	private ProgramaRepository programaRepository;


	
	public Perfil create(PerfilDTOin perfilRequestDTO) {
		return perfilRepository.save(perfilRequestDTO.create(programaRepository));
	}

	
	public Perfil update(Long id, PerfilDTOin perfilRequestDTO) {
		Optional<Perfil> salvo = perfilRepository.findById(id);
		if (!salvo.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}

		return perfilRepository.save(perfilRequestDTO.update(salvo.get(), programaRepository));
	}

	
	public void delete(Long id) {
		perfilRepository.deleteById(id);
	}


	
	public Optional<Perfil> findById(Long id) {
		return perfilRepository.findById(id);
	}

	
	public Page<PerfilDTO> findByFilter(Pageable pageable, PerfilFilter filter) {
		return perfilRepository.findByFilter(pageable, filter);
	}


	
	public List<PerfilDTO> findToCombobox(PerfilFilter filter) {
		return perfilRepository.findToCombobox(filter);
	}

	
	public List<Perfil> findToReport(PerfilFilter filter) {
		return perfilRepository.findToReport(filter);
	}

	
	public List<MenuModel> menuDoPerfil(Long perfilId) {
		return perfilRepository.menuDoPerfil(perfilId);
	}

}
