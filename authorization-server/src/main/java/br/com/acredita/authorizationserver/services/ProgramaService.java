package br.com.acredita.authorizationserver.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.acredita.authorizationserver.DTOout.TreeDTO;
import br.com.acredita.authorizationserver.models.Programa;
import br.com.acredita.authorizationserver.repositories.ProgramaRepository;

@Service
public class ProgramaService {

	@Autowired
	private ProgramaRepository programaRepository;

	public Optional<Programa> findById(String id) {
		return programaRepository.findById(id);
	}

	public List<Programa> findAll() {
		return programaRepository.findAll();
	}

	public Page<Programa> findAllPaged(Pageable pageable) {
		return programaRepository.findAll(pageable);
	}

	public Programa create(Programa programa) {
		return programaRepository.save(programa);
	}

	public Programa update(String id, Programa programa) {
		Optional<Programa> salvo = programaRepository.findById(id);
		if (!salvo.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}

		// FIXME - trata o de/para do programa
		return programaRepository.save(salvo.get());
	}

	public void delete(String id) {
		programaRepository.deleteById(id);
	}

	public List<TreeDTO> treeDTOLista() {
		
		List<TreeDTO> treeDTOList = new ArrayList<>();
		programaRepository.findAll().stream().filter(x -> {
			return x.getProgramaPai() == null;
		}).forEach(p -> {
			treeDTOList.add(new TreeDTO(p.getDescricao(), p.getId().toString(), "", "", true, varreRecursivo(p.getSubMenu()) ) );
		});
		
		System.out.println(treeDTOList.toString());
  
		
	return treeDTOList;	
	}

	private List<TreeDTO> varreRecursivo(List<Programa> pList) {
		List<TreeDTO> treeDTOList = new ArrayList<>();
		pList.forEach(p ->{
			if (p.getSubMenu().isEmpty()) {
				treeDTOList.add(new TreeDTO(p.getDescricao(), p.getId().toString(), "", "", true, null) );
			} else {
				treeDTOList.add(new TreeDTO(p.getDescricao(), p.getId().toString(), "", "", true, varreRecursivo(p.getSubMenu()) ) );				
			}
			
		});
		return treeDTOList;
	}
	
}
