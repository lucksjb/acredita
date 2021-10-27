package br.com.acredita.authorizationserver.annotations;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ToLowerCaseSpec {
	
	@Before(value = "@within(ToLowerCase) || @annotation(ToLowerCase)",argNames="JoinPoint")
	public void teste(JoinPoint joinPoint) {
		System.out.println("ToLowerCase.before, class: " + joinPoint.getSignature().getDeclaringType().getSimpleName() + 
				", method: " + joinPoint.getSignature().getName());
		
	}

}
