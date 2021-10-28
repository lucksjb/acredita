package br.com.acredita.customer.config;


import java.util.Collections;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(Collections.singletonList("*"));
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

		return bean;
	}

}


// import java.io.IOException;

// import javax.servlet.Filter;
// import javax.servlet.FilterChain;
// import javax.servlet.FilterConfig;
// import javax.servlet.ServletException;
// import javax.servlet.ServletRequest;
// import javax.servlet.ServletResponse;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.PropertySource;
// import org.springframework.core.Ordered;
// import org.springframework.core.annotation.Order;
// import org.springframework.stereotype.Component;

// import br.com.acredita.customer.config.security.SegurancaProperties;


// @Component
// @Order(Ordered.HIGHEST_PRECEDENCE)
// @PropertySource(ignoreResourceNotFound = true, value = "classpath:application.properties")
// public class CorsFilter implements Filter {

// 	@Autowired 
// 	private SegurancaProperties properties;
		
// 	@Override
// 	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
// 			throws IOException, ServletException {
		
// 		HttpServletRequest request = (HttpServletRequest) req;
// 		HttpServletResponse response = (HttpServletResponse) resp;
		
// 		response.setHeader("Access-Control-Allow-Origin", properties.getCorsOrigemPermitida());
//         response.setHeader("Access-Control-Allow-Credentials", "true");
		
// 		if ("OPTIONS".equals(request.getMethod()) && properties.getCorsOrigemPermitida().equals(request.getHeader("Origin"))) {
// 			response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, PATCH, OPTIONS");
//         	response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
//         	response.setHeader("Access-Control-Max-Age", "3600");
			
// 			response.setStatus(HttpServletResponse.SC_OK);
// 		} else {
// 			chain.doFilter(req, resp);
// 		}
		
// 	}
	
// 	@Override
// 	public void destroy() {
// 	}

// 	@Override
// 	public void init(FilterConfig arg0) throws ServletException {
// 	}

// }