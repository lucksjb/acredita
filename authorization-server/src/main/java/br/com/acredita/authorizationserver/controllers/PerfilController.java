package br.com.acredita.authorizationserver.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.acredita.authorizationserver.DTOin.PerfilDTOin;
import br.com.acredita.authorizationserver.DTOout.PerfilDTO;
import br.com.acredita.authorizationserver.annotations.ObrigaTrocarSenha;
import br.com.acredita.authorizationserver.config.RestControllerPath;
import br.com.acredita.authorizationserver.filters.PerfilFilter;
import br.com.acredita.authorizationserver.models.MenuModel;
import br.com.acredita.authorizationserver.models.Perfil;
import br.com.acredita.authorizationserver.services.PerfilService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(path = RestControllerPath.Perfil_PATH)
@Api(tags = "perfil", description = "perfil")
@ObrigaTrocarSenha
public class PerfilController  {
	@Autowired
	private PerfilService perfilService;

	@GetMapping("/")
	@ApiOperation(value = "Obtem Perfis paginados por filter")
	@ApiImplicitParams({ @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Página que você deseja recuperar (0..N)"),
			@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Número de registros por página."),
			@ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sort formato: propriedade,asc|desc. "
					+ "Padrão do sort é ascendente. " + "sort com multiplos campos são permitidos") })

	@PreAuthorize("hasRole('ROLE_PerfilUsuario.Pesquisar')")
	public Page<PerfilDTO> findByFilter(Pageable pageable, PerfilFilter filter) {
		return perfilService.findByFilter(pageable, filter);
	}

  
	@GetMapping("/{id}")
	@ApiOperation(value = "Obtem um Perfil através do identificador")
	@PreAuthorize("hasRole('ROLE_PerfilUsuario.Pesquisar')")
	public ResponseEntity<Perfil> findById(@ApiParam(value = "identificador do tipo de pessoa requerido", required = true) @PathVariable Long id) {
		Optional<Perfil> busca = perfilService.findById(id);
		return busca.isPresent() ? ResponseEntity.ok(busca.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping(path = "/create")
	@ApiOperation(value = "Cria um novo Perfil")
	@PreAuthorize("hasRole('ROLE_PerfilUsuario.Incluir')")
	public ResponseEntity<Perfil> create(@RequestBody @Valid PerfilDTOin perfilDTOin, HttpServletResponse response) {
		Perfil salvo = perfilService.create(perfilDTOin);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(salvo.getId()).toUri();
		response.setHeader("Location", uri.toASCIIString());
		return ResponseEntity.created(uri).body(salvo);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Remove um Perfil por identificador")
	@PreAuthorize("hasRole('ROLE_PerfilUsuario.Apagar')")
	public void delete(@ApiParam(value = "identificador do Perfil requerido", required = true) @PathVariable Long id) {
		perfilService.delete(id);
	}

	@PatchMapping("/update/{id}")
	@ApiOperation(value = "Atualiza um Perfil por identificador")
	@PreAuthorize("hasRole('ROLE_PerfilUsuario.Alterar')")
	public ResponseEntity<Perfil> update(@ApiParam(value = "identificador do Perfil requerido", required = true) @PathVariable Long id,
			@Valid @RequestBody PerfilDTOin perfilDTOin, HttpServletResponse response) {
		return ResponseEntity.ok(perfilService.update(id, perfilDTOin));
	}

	
	@GetMapping("/to-data-table")
	@ApiOperation(value = "Obtem Perfis para combobox ")
	@PreAuthorize("hasRole('ROLE_PerfilUsuario.Pesquisar')")
	public List<PerfilDTO> findToCombobox(PerfilFilter filter) {
		return perfilService.findToCombobox(filter);
	}


	@GetMapping("/to-report")
	@ApiOperation(value = "Lista todos os perfis (para relatórios) ")
	@PreAuthorize("hasRole('ROLE_PerfilUsuario.Pesquisar')")
	public List<Perfil> findToReport( PerfilFilter filter) {
		return perfilService.findToReport(filter);
	}
	
	
	// ****  OUTROS METODOS  ****// 
	@GetMapping("/menuDoPerfil/{perfilId}")
	@ApiOperation(value = "Obtem menu do perfil ")
	@PreAuthorize("hasRole('ROLE_PerfilUsuario.Pesquisar')")
	public List<MenuModel> menuDoPerfil(@ApiParam(value = "identificador do Perfil requerido", required = true) @PathVariable Long perfilId) {
		return perfilService.menuDoPerfil(perfilId);
	}
}
