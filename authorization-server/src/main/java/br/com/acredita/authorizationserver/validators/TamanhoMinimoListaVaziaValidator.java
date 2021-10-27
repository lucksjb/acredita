package br.com.acredita.authorizationserver.validators;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TamanhoMinimoListaVaziaValidator implements ConstraintValidator<TamanhoMinimoLista, List<?>> {
	@SuppressWarnings("unused")
	private int tamanhoMinimo;
	
	@Override
	public void initialize(TamanhoMinimoLista constraintAnnotation) {
		this.tamanhoMinimo = constraintAnnotation.tamanhoMinimo();
		
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
	
	@Override
	public boolean isValid(List<?> value, ConstraintValidatorContext context) {
		if(value == null) {
			return false;
		}
		return value.size()>0;
	}
}
