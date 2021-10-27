package br.com.acredita.authorizationserver.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ObrigaTrocarSenha {
//	String message() default "{ListaNaoPodeSerVazia}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	boolean obrigar() default true;
}
