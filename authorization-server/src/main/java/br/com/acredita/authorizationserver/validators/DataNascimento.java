package br.com.acredita.authorizationserver.validators;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = { DataNascimentoValidator.class})
public @interface DataNascimento { 
	String message() default "{br.com.acredita.authorizationserver.validators.DataNascimentoValidator.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	int idadeMinima() default 5;
	int idadeMaxima() default 70;
}
