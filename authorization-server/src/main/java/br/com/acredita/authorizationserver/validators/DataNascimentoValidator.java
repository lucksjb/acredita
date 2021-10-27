package br.com.acredita.authorizationserver.validators;

import java.time.LocalDate;
import java.time.Period;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DataNascimentoValidator implements ConstraintValidator<DataNascimento, LocalDate> {
	private int idadeMinima;
	private int idadeMaxima;
	
	@Override
	public void initialize(DataNascimento constraintAnnotation) {
		this.idadeMinima = constraintAnnotation.idadeMinima();
		this.idadeMaxima = constraintAnnotation.idadeMaxima();
		
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		if(value == null) {
			return false;
		}
		Period intervalPeriod = Period.between(value, LocalDate.now());
		if(intervalPeriod.getYears() < this.idadeMinima || intervalPeriod.getYears() > this.idadeMaxima) {
			return false;
		}
		return true;
	}
}
