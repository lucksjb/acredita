package br.com.acredita.authorizationserver.config;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableTranslator {

	public static Pageable translate(Pageable pageable, Map<String, String> fieldsMapping) {
		var orders = pageable.getSort().stream()
			.filter(order -> fieldsMapping.containsKey(order.getProperty()))
			.map(order -> new Sort.Order(order.getDirection(), 
					fieldsMapping.get(order.getProperty())))
			.collect(Collectors.toList());
							
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(orders));
	}
	
}

/*****
 * utilização :
 * 
 * no controler a primeira ação é chamar o metodo que traduz a pageable:
 * 
 * 	@GetMapping
	public Page<PedidoResumoModel> pesquisar(PedidoFilter filtro, 
			@PageableDefault(size = 10) Pageable pageable) {
		pageable = traduzirPageable(pageable);
		.
		.
		.
		
 * 
 * ----- esse metodo cria o de-para dos campos com o nome das propriedades do model 
 * 	private Pageable traduzirPageable(Pageable apiPageable) {
		var mapeamento = ImmutableMap.of(
				"codigo", "codigo",
				"restaurante.nome", "restaurante.nome",
				"nomeCliente", "cliente.nome",
				"valorTotal", "valorTotal"
			);
		
		return PageableTranslator.translate(apiPageable, mapeamento);
	}
 */

