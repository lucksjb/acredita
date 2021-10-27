package br.com.acredita.authorizationserver.config;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
public class UnpagedConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver() {
            @Override
            public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                if ("true".equals(webRequest.getParameter(getParameterNameToUse("unpaged", methodParameter)))) {
                    return Pageable.unpaged();
                }

                return super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
            }
        };
        resolvers.add(pageableResolver);
    }
}