package br.com.acredita.authorizationserver.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.acredita.authorizationserver.DTOout.UsuarioAlteraSenhaDTO;
import br.com.acredita.authorizationserver.DTOout.UsuarioDTO;
import br.com.acredita.authorizationserver.filters.UsuarioFilter;
import br.com.acredita.authorizationserver.models.MenuModel;
import br.com.acredita.authorizationserver.models.Usuario;

public interface UsuarioRepositoryQuery {

	public Page<UsuarioDTO> findByFilter(Pageable pageable, UsuarioFilter usuarioFilter);
	public List<UsuarioDTO> findToCombobox(UsuarioFilter usuarioFilter);
	public List<Usuario> findToReport(UsuarioFilter filter);
	

//	public Optional<UsuarioDTO> findById(Long id);
	public Optional<UsuarioDTO> findByEmail(String email);
	Optional<UsuarioDTO> findByLogin(String login);
	
	public Optional<UsuarioDTO> findByUUID(String uUID);
	
	public Optional<UsuarioDTO> updateSenha(Long id, String novaSenha);
	
	public String loginValido(String login);
	public String criaLoginValido(String nome, LocalDate dataNascimento);
	
	public Usuario trocaSenha(UsuarioAlteraSenhaDTO usuarioDTO);
	public List<MenuModel> menuDoUsuario(Usuario usuario);
}
