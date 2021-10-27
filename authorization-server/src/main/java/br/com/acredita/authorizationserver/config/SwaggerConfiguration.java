package br.com.acredita.authorizationserver.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;

import com.google.common.collect.Lists;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
	@Autowired
	private StopWatch watch;
	
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";
    private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

    @Bean
    public StopWatch stopWatch() {
    	return new StopWatch();
    }
    
    
    @Bean
    public Docket swaggerSpringfoxDocket() {
    	watch.start();
        log.debug("Starting Swagger");

        //@SuppressWarnings("rawtypes")
		// List<VendorExtension> vext = new ArrayList<>();
        @SuppressWarnings("deprecation")
		ApiInfo apiInfo = new ApiInfo(
        		"aCredita - Authorizarion server",
        		"Api utilizada para emissão de token / cadastro de usuários / cadastro de perfís", 
        		"20.1.0.0", 
        		"http:\\\\www.aCredita.com.br\\aCredita\\TermoDeServico.txt",
        		"lucksjb@gmail.com",
        		"Software Proprietário", 
        		"http:\\\\www.acredita.com.br\\aCredita\\Licenca.txt");
            
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo)
            .pathMapping("/")
            .apiInfo(apiInfo)
            .forCodeGeneration(true)
            .genericModelSubstitutes(ResponseEntity.class)
            .ignoredParameterTypes(Pageable.class)
            .ignoredParameterTypes(java.sql.Date.class)
            .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
            .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
            .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
            .securityContexts(Lists.newArrayList(securityContext()))
            .securitySchemes(Lists.newArrayList(apiKey()))
            .useDefaultResponseMessages(false);

        docket = docket.select()
            .paths(regex(DEFAULT_INCLUDE_PATTERN))
            .build();
        
        watch.stop();
        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }


    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
            .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
            new SecurityReference("JWT", authorizationScopes));
    }
}