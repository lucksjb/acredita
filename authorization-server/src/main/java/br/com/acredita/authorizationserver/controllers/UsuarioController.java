package br.com.acredita.authorizationserver.controllers;

import java.net.URI;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.acredita.authorizationserver.DTOin.UsuarioDTOinCreate;
import br.com.acredita.authorizationserver.DTOin.UsuarioDTOinUpdate;
import br.com.acredita.authorizationserver.DTOout.UsuarioAlteraSenhaDTO;
import br.com.acredita.authorizationserver.DTOout.UsuarioDTO;
import br.com.acredita.authorizationserver.annotations.ObrigaTrocarSenha;
import br.com.acredita.authorizationserver.config.RestControllerPath;
import br.com.acredita.authorizationserver.filters.UsuarioFilter;
import br.com.acredita.authorizationserver.models.MenuModel;
import br.com.acredita.authorizationserver.models.Usuario;
import br.com.acredita.authorizationserver.services.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(path = RestControllerPath.Usuario_PATH)
@Api(tags = "usuario", description = "usuario")
public class UsuarioController {
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/")
	@ApiOperation(value = "Obtem Usuario paginados")
	@ApiImplicitParams({ @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Página que você deseja recuperar (0..N)"),
			@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Número de registros por página."),
			@ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sort formato: propriedade,asc|desc. "
					+ "Padrão do sort é ascendente. " + "sort com multiplos campos são permitidos") })
	@PreAuthorize("hasRole('ROLE_Usuario.Pesquisar')")
	@ObrigaTrocarSenha
	public Page<UsuarioDTO> findByFilter(Pageable pageable, UsuarioFilter filter) {
		return usuarioService.findByFilter(pageable, filter);
	}

	@GetMapping("/to-data-table")
	@ApiOperation(value = "Obtem Usuario para combobox (id e nome)")
	@ApiImplicitParams({ @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Página que você deseja recuperar (0..N)"),
			@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Número de registros por página."),
			@ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sort formato: propriedade,asc|desc. "
					+ "Padrão do sort é ascendente. " + "sort com multiplos campos são permitidos") })
	@PreAuthorize("hasRole('ROLE_Usuario.Pesquisar')")
	@ObrigaTrocarSenha
	public List<UsuarioDTO> findToCombobox(UsuarioFilter filter) {
		return usuarioService.findToCombobox(filter);
	}

	@GetMapping("/to-report")
	@ApiOperation(value = "Obtem Usuario para relatorios - todos os campos ")
	@ApiImplicitParams({ @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Página que você deseja recuperar (0..N)"),
			@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Número de registros por página."),
			@ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sort formato: propriedade,asc|desc. "
					+ "Padrão do sort é ascendente. " + "sort com multiplos campos são permitidos") })
	@PreAuthorize("hasRole('ROLE_Usuario.Pesquisar')")
	public List<Usuario> findToReport(UsuarioFilter filter) {
		return usuarioService.findToReport(filter);
	}

	@PatchMapping("/update/{id}")
	@ApiOperation(value = "Atualiza um Usuario por identificador")
	@PreAuthorize("hasRole('ROLE_Usuario.Alterar')")
	@ObrigaTrocarSenha
	public ResponseEntity<Usuario> update(@ApiParam(value = "identificador do Usuario requerido", required = true) @PathVariable Long id,
			@Valid @RequestBody UsuarioDTOinUpdate usuarioDTOinUpdate, HttpServletResponse response) {
		return ResponseEntity.ok(usuarioService.update(id, usuarioDTOinUpdate));
	}

	@PostMapping(path = "/create")
	@ApiOperation(value = "Cria um novo Usuario")
	@PreAuthorize("hasRole('ROLE_Usuario.Incluir')")
	@ObrigaTrocarSenha
	public ResponseEntity<Usuario> create(@RequestBody @Valid UsuarioDTOinCreate usuarioDTOinCreate, HttpServletResponse response) {
		Usuario salvo = usuarioService.create(usuarioDTOinCreate);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(salvo.getId()).toUri();
		response.setHeader("Location", uri.toASCIIString());
		return ResponseEntity.created(uri).body(salvo);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Remove um Usuario por identificador")
	@PreAuthorize("hasRole('ROLE_Usuario.Apagar')")
	@ObrigaTrocarSenha
	public void remove(@ApiParam(value = "identificador do Usuario requerido", required = true) @PathVariable Long id) {
		usuarioService.delete(id);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Obtem um Usuario através do identificador")
	@PreAuthorize("hasRole('ROLE_Usuario.Pesquisar')")
	@ObrigaTrocarSenha
	public ResponseEntity<Usuario> findById(@ApiParam(value = "identificador do tipo de pessoa requerido", required = true) @PathVariable Long id) {
		Optional<Usuario> busca = usuarioService.findById(id);
		return busca.isPresent() ? ResponseEntity.ok(busca.get()) : ResponseEntity.notFound().build();
	}

	/****************** OUTROS METODOS ************/
	@GetMapping("/findByEmail/{email}")
	@ApiOperation(value = "Obtem Usuario por Email")
	@PreAuthorize("hasRole('ROLE_Usuario.Pesquisar')")
	@ObrigaTrocarSenha
	public ResponseEntity<UsuarioDTO> findByEmail(@ApiParam(value = "Email do Usuario requerido", required = true) @PathVariable String email) {
		Optional<UsuarioDTO> busca = usuarioService.findByEmail(email);
		return busca.isPresent() ? ResponseEntity.ok(busca.get()) : ResponseEntity.notFound().build();
	}

	@GetMapping("/findByLogin/{login}")
	@ApiOperation(value = "Obtem Usuario por Login")
	@PreAuthorize("hasRole('ROLE_Usuario.Pesquisar')")
	@ObrigaTrocarSenha
	public ResponseEntity<UsuarioDTO> findByLogin(@ApiParam(value = "Login do Usuario requerido", required = true) @PathVariable String login) {
		Optional<UsuarioDTO> busca = usuarioService.findByLogin(login);
		return busca.isPresent() ? ResponseEntity.ok(busca.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping("/criaLoginValido")
	@ApiOperation(value = "Cria login válido")
	@PreAuthorize("hasRole('ROLE_Usuario.Pesquisar')")
	@ObrigaTrocarSenha
	public String criaLoginValido(@Valid @RequestBody SolicitaLoginValidoDTO solicitaLoginValidoDTO, HttpServletResponse response) {
		return usuarioService.criaLoginValido(solicitaLoginValidoDTO.nome, solicitaLoginValidoDTO.dataNascimento);
	}

	@PutMapping("/trocaSenha")
	@ApiOperation(value = "Troca Senha do usuário")
	@PreAuthorize("hasRole('ROLE_Usuario.AlterarSenha')")
	public ResponseEntity<Usuario> trocaSenha(@Valid @RequestBody UsuarioAlteraSenhaDTO usuarioAlteraSenhaDTO, HttpServletResponse response) {
		return ResponseEntity.ok().body(usuarioService.trocaSenha(usuarioAlteraSenhaDTO));

	}

	@GetMapping("/menuUsuario/{id}")
	@ApiOperation(value = "Obtem menu Usuario por id")
	@PreAuthorize("hasRole('ROLE_Usuario.Pesquisar')")
	public List<MenuModel> menuUsuario(@ApiParam(value = "identificador do tipo de pessoa requerido", required = true) @PathVariable Long id) {
		return usuarioService.menuDoUsuario(id);
	}

	@PostMapping("/solicitaResetDeSenha/{loginOrEmail}")
	@ApiOperation(value = "Solicitar reset de senha - envia o email")
	public void solicitaResetDeSenha(@ApiParam(value = "Informe o login ou Email", required = true) @PathVariable String loginOrEmail, HttpServletResponse response) {
		usuarioService.solicitaResetDeSenha(loginOrEmail);
	}

	@GetMapping("/resetarSenha/{uuid}")
	@ApiOperation(value = "Reset de senha - senha volta para 123")
	public @ResponseBody String resetarSenha(@ApiParam(value = "Informe o uuid que recebeu no email", required = true) @PathVariable String uuid, HttpServletResponse response) {
		return usuarioService.resetarSenha(uuid);
	}

}

class SolicitaLoginValidoDTO {
	String nome;
	LocalDate dataNascimento;

	public SolicitaLoginValidoDTO() {
	}

	public SolicitaLoginValidoDTO(String nome, LocalDate dataNascimento) {
		this.nome = nome;
		this.dataNascimento = dataNascimento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

}
