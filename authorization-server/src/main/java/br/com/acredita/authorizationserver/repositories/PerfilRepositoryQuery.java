package br.com.acredita.authorizationserver.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.acredita.authorizationserver.DTOout.PerfilDTO;
import br.com.acredita.authorizationserver.filters.PerfilFilter;
import br.com.acredita.authorizationserver.models.MenuModel;
import br.com.acredita.authorizationserver.models.Perfil;

public interface PerfilRepositoryQuery {
	public List<MenuModel> menuDoPerfil(Long perfilId);
	public List<PerfilDTO> findToCombobox(PerfilFilter filter);
	public List<Perfil> findToReport(PerfilFilter filter);
	Page<PerfilDTO> findByFilter(Pageable pageable, PerfilFilter filter);

}
