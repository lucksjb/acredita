package br.com.acredita.authorizationserver.freemarker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import br.com.acredita.authorizationserver.exceptions.NegocioException;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class FMTemplate {
	@Autowired
	private Configuration freemarkerConfig;
	
	private String templateName;
	private Map<String, String> variaveis = new HashMap<String, String>();

//	public static FMTemplate builder() {
//		return new FMTemplate();
//	}
	
	public FMTemplate build() {
		// se tiver validações faz aqui 
		return this;
	}

	public String getTemplateName() {
		return templateName;
	}


	public FMTemplate templateName(String templateName) {
		this.templateName = templateName;
		return this;
	}


	public Map<String, String> getVariaveis() {
		return variaveis;
	}

	public FMTemplate variaveis(Map<String, String> variaveis) {
		this.variaveis = variaveis;
		return this;
	}

	public FMTemplate variavel(String chave, String valor) {
		if (!this.variaveis.containsKey(chave)) {
			this.variaveis.put(chave, valor);
		}
		return this;
	}

	public String processarTemplate()  {
		try {
			Template template = freemarkerConfig.getTemplate(this.getTemplateName());

			return FreeMarkerTemplateUtils.processTemplateIntoString(template, this.getVariaveis());
		} catch (Exception e) {
			throw new NegocioException("Não conseguiu processar o template '"+this.getTemplateName()+"'");
		}
	}

}
