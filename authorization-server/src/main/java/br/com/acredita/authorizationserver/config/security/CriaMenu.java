package br.com.acredita.authorizationserver.config.security;

import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.acredita.authorizationserver.enums.SimNao;
import br.com.acredita.authorizationserver.enums.TipoItemMenu;
import br.com.acredita.authorizationserver.models.Perfil;
import br.com.acredita.authorizationserver.models.Programa;
import br.com.acredita.authorizationserver.models.Usuario;
import br.com.acredita.authorizationserver.repositories.PerfilRepository;
import br.com.acredita.authorizationserver.repositories.ProgramaRepository;
import br.com.acredita.authorizationserver.repositories.UsuarioRepository;
import br.com.acredita.authorizationserver.utils.java.JavaFunctions;

@Service
public class CriaMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SegurancaProperties properties;

	private static Long seqMenu = 1L;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private ProgramaRepository programaRepository;

	public CriaMenu() {
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	// ******************* INICIO CADASTRO MENU ***************//
	private Optional<Perfil> perfilAdm;

	@Transactional
	public void cadastraMenuByXml() {

		String senhaPadrao = properties.getNovoUsuarioSenhaPadrao().trim();
		perfilAdm = perfilRepository.findById(1L);
		if (!perfilAdm.isPresent()) {
			if (properties.getNovoUsuarioExigeSenhaForte() == SimNao.SIM && !JavaFunctions.senhaForte(senhaPadrao)) {
				throw new SecurityException("Senha fraca e sistema de segurança configurado para senha forte");
			}

			Perfil perfil = new Perfil();
			perfil.setDescricao("ADMINISTRADOR");
			perfilRepository.save(perfil);

			Usuario usuario = new Usuario();
			usuario.setNome("Administrador");
			usuario.setLogin("admin");
			usuario.setNascimento(LocalDate.of(1971, 11, 17));
			usuario.setEmail(properties.getInstallEmailAdmin());
			usuario.setFone(properties.getInstallFoneAdmin());
			usuario.setAlterarSenhaProxLogin(properties.getInstallAlteraSenhaProxLogin());
			usuario.setValidadeDaSenha(properties.getNovoUsuarioValidadeSenhaDias());

			usuario.setPerfis(Arrays.asList(perfil));

			if (properties.getSenhaEncoded() == SimNao.SIM) {
				usuario.setSenha(passwordEncoder.encode(senhaPadrao));
			} else {
				usuario.setSenha(senhaPadrao);
			}
			usuario = usuarioRepository.save(usuario);

			perfilAdm = perfilRepository.findById(1L);
		}
		perfilAdm.get().getPermissoes().clear();

		InputStream is = getClass().getClassLoader().getResourceAsStream("security/menu.xml");
		// InputStream is =
		// CriaMenu.class.getClassLoader().getResourceAsStream(arquivo);

		DocumentBuilder dBuilder = null;
		try {
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document doc = dBuilder.parse(is);

			doc.normalize();

			Element root = doc.getDocumentElement();

			cadastraMenu(root);

			perfilRepository.save(perfilAdm.get());

		} catch (Exception e) {
			throw new SecurityException("Erro ao ler o arquivo menu.xml" + e.getMessage());
		}

		System.out.println("******************************************8 TERMINOU ");
	}

	private void cadastraMenu(Node item) {
		NodeList nodes = item.getChildNodes();
		// System.out.println("tamanho da lista " + nodes.getLength());
		for (int i = 0; i < nodes.getLength(); i++) {
			Node filho = nodes.item(i);
			if (filho.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			if (filho.getNodeName().toUpperCase().equals("DIVISOR")) {
				String idPai = atributo("id", filho.getParentNode()).equals("id") ? "000" : atributo("id", filho.getParentNode());
				Programa programaEntity = new Programa();

				Programa pai = new Programa();
				if (!idPai.equals("000")) {
					pai.setId(idPai);
					pai.setDescricao(atributo("label", filho.getParentNode()));
					pai.setIcone(atributo("icone", filho.getParentNode()));
					programaEntity.setProgramaPai(pai);
				} else {
					programaEntity.setProgramaPai(null);
				}
				programaEntity.setId(atributo("id", filho));
				programaEntity.setDescricao(null);
				programaEntity.setIcone(null);
				programaEntity.setRoute(null);
				programaEntity.setEndPoint(null);
				programaEntity.setTipoItemMenu(TipoItemMenu.DIVISOR);
				programaEntity.setSeqMenu(seqMenu++);
				programaRepository.save(programaEntity);
			}
			
			if (filho.getNodeName().toUpperCase().equals("MENUITEM")) {
				String idPai = atributo("id", filho.getParentNode()).equals("id") ? "000" : atributo("id", filho.getParentNode());
				Programa programaEntity = new Programa();

				Programa pai = new Programa();
				if (!idPai.equals("000")) {
					pai.setId(idPai);
					pai.setDescricao(atributo("label", filho.getParentNode()));
					pai.setIcone(atributo("icone", filho.getParentNode()));
					programaEntity.setProgramaPai(pai);
				} else {
					programaEntity.setProgramaPai(null);
				}

				if (atributo("route", filho).equals("route")) {
					throw new SecurityException("Obrigatório route em menuItem " + atributo("id", filho));
				}

				programaEntity.setId(atributo("id", filho));
				programaEntity.setDescricao(atributo("label", filho));
				programaEntity.setIcone(atributo("icone", filho));
				programaEntity.setRoute(atributo("route", filho));
				programaEntity.setEndPoint(null);
				programaEntity.setTipoItemMenu(TipoItemMenu.ITEM);
				programaEntity.setSeqMenu(seqMenu++);

				perfilAdm.get().getPermissoes().add(programaEntity);
				programaRepository.save(programaEntity);

				// aqui vai checar se tem ou nao os "endPoints" ou routes
				if (filho.hasChildNodes()) {
					cadastraMenu(filho);
				}

			}

			if (filho.getNodeName().toUpperCase().equals("ROUTE")) {
				// String idPai = !JavaFunctions.isNumeric(atributo("id",
				// filho.getParentNode())) ? "000"
				// : atributo("id", filho.getParentNode());
				String idPai = atributo("id", filho.getParentNode());
				Programa programa = new Programa();

				Programa pai = new Programa();
				if (!idPai.equals("000")) {
					pai.setId(idPai);
					pai.setDescricao(atributo("label", filho.getParentNode()));
					pai.setIcone(atributo("icone", filho.getParentNode()));
					programa.setProgramaPai(pai);
				} else {
					programa.setProgramaPai(null);
				}

				if (atributo("route", filho).equals("route")) {
					throw new SecurityException("Obrigatório route em Route " + atributo("id", filho));
				}

				programa.setId(atributo("id", filho));
				programa.setDescricao(atributo("label", filho));
				programa.setIcone(atributo("icone", filho));
				programa.setRoute(atributo("route", filho));
				programa.setEndPoint(null);
				programa.setTipoItemMenu(TipoItemMenu.ROUTE);
				programa.setSeqMenu(seqMenu++);

				// por hora nao vou add as routes na permissao 
				// perfilAdm.get().getPermissoes().add(programa);
				programaRepository.save(programa);
			}

			if (filho.getNodeName().toUpperCase().equals("ENDPOINTS") && filho.hasChildNodes()) {
				cadastraMenu(filho);
			}

			if (filho.getNodeName().toUpperCase().equals("ENDPOINT")) {
				// String idPai = !JavaFunctions.isNumeric(atributo("id",
				// filho.getParentNode())) ? "000"
				// : atributo("id", filho.getParentNode());
				String idPai = atributo("id", filho.getParentNode());

				Programa endPoint = new Programa();
				endPoint.setId(atributo("id", filho));
				endPoint.setDescricao(atributo("label", filho));
				endPoint.setTipoItemMenu(TipoItemMenu.ENDPOINT);
				endPoint.setSeqMenu(seqMenu++);

				if (atributo("endPoint", filho).equals("endPoint")) {
					throw new SecurityException("Obrigatório endPoint em endPoint " + atributo("id", filho));
				}

				if (!idPai.equals("000")) {
					Programa pai = new Programa();
					pai.setId(idPai);
					pai.setDescricao(atributo("label", filho.getParentNode()));
					pai.setIcone(atributo("icone", filho.getParentNode()));
					endPoint.setEndPoint(atributo("endPoint", filho));
					endPoint.setProgramaPai(pai);
				} else {
					endPoint.setEndPoint(atributo("endPoint", filho));
					endPoint.setProgramaPai(null);
				}

				perfilAdm.get().getPermissoes().add(endPoint);
				programaRepository.save(endPoint);
			}

			if (filho.getNodeName().toUpperCase().equals("COMPONENTES") && filho.hasChildNodes()) {
				cadastraMenu(filho);
			}

			if (filho.getNodeName().toUpperCase().equals("COMPONENTE")) {
				// String idPai = !JavaFunctions.isNumeric(atributo("id",
				// filho.getParentNode())) ? "000"
				// : atributo("id", filho.getParentNode());
				String idPai = atributo("id", filho.getParentNode());
				Programa componente = new Programa();
				componente.setId(atributo("id", filho));
				componente.setDescricao(atributo("label", filho));
				componente.setTipoItemMenu(TipoItemMenu.COMPONENT);
				componente.setSeqMenu(seqMenu++);

				if (!idPai.equals("000")) {
					Programa pai = new Programa();
					pai.setId(idPai);
					pai.setDescricao(atributo("label", filho.getParentNode()));
					pai.setIcone(atributo("icone", filho.getParentNode()));
					// aqui ele pega o do pai, mas como o label do pai as vezes
					// terá espaços
					// ex: "Grupo de Produtos" resolvi tirar e deixar ele pegar
					// o outcome do proprio componente
					// componente.setOutcome(atributo("label",
					// filho.getParentNode())+"."+atributo("label", filho) );
					componente.setEndPoint(atributo("endPoint", filho));
					componente.setProgramaPai(pai);
				} else {
					componente.setEndPoint(atributo("endPoint", filho));
					componente.setProgramaPai(null);
				}

				perfilAdm.get().getPermissoes().add(componente);
				programaRepository.save(componente);
			}

			if (filho.hasChildNodes()) {
				if (filho.getNodeName().toUpperCase().equals("SUBMENU")) {
					String idPai = atributo("id", filho.getParentNode()).equals("id") ? "000" : atributo("id", filho.getParentNode());

					Programa subMenu = new Programa();
					subMenu.setId(atributo("id", filho));
					subMenu.setDescricao(atributo("label", filho));
					subMenu.setIcone(atributo("icone", filho));
					subMenu.setTipoItemMenu(TipoItemMenu.SUBMENU);
					subMenu.setSeqMenu(seqMenu++);

					if (!idPai.equals("000")) {
						Programa pai = new Programa();
						pai.setId(idPai);
						pai.setDescricao(atributo("label", filho.getParentNode()));
						pai.setIcone(atributo("icone", filho.getParentNode()));
						subMenu.setProgramaPai(pai);
					} else {
						subMenu.setProgramaPai(null);
					}
					perfilAdm.get().getPermissoes().add(subMenu);
					try {
						programaRepository.save(subMenu);
					} catch (Exception e) {
						e.printStackTrace();
					}
					cadastraMenu(filho);
				}
			}
		}
	}
	// ******************* FIM CADASTRO MENU ***************//

	private String atributo(String atributo, Node item) {
		if (item == null) {
			return atributo;
		}

		NamedNodeMap nodeMap = item.getAttributes();
		for (int i = 0; i < nodeMap.getLength(); i++) {
			Node node = nodeMap.item(i);

			if (node.getNodeName().equals(atributo)) {
				atributo = node.getNodeValue();
			}
		}
		return atributo;
	}

}
