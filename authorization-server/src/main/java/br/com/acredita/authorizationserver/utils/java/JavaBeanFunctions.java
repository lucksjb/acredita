package br.com.acredita.authorizationserver.utils.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;

import br.com.acredita.authorizationserver.exceptions.ValidacaoException;


@Component
public class JavaBeanFunctions {
	@Autowired
	private SmartValidator validator;
	
	
	public void validate(Object object, String objectName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(object, objectName);
		validator.validate(object, bindingResult);
		
		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
	}

}
