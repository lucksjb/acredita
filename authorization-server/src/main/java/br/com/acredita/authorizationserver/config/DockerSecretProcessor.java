package br.com.acredita.authorizationserver.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

// https://bmuschko.com/blog/docker-secret-spring-boot/

// para criar o secret usar sempre chave=valor
// exemplo:
// spring.datasource.password=123456
public class DockerSecretProcessor implements EnvironmentPostProcessor {
	private final Logger logger = LoggerFactory.getLogger(DockerSecretProcessor.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
    	
    	File f = new File("/run/secrets");
    	if(!f.exists()) {
    		return;
    	}
    	
    	List<String> files = Arrays.asList(f.list());

    	
    	files.forEach(file -> {
    		logger.error("aqui "+ file);
    		Resource resource = new FileSystemResource("/run/secrets/"+file);
            if (resource.exists()) {
            	try {
                    if (logger.isInfoEnabled()) {
                        logger.info(String.format("Using secret %s from injected Docker secret file", file));
                    }

                    String secret = StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
                    
                    String[] keyValue = secret.split("=");
                    keyValue[0] = keyValue[0];
                    Properties props = new Properties();
                    props.put(keyValue[0].trim(), keyValue[1].trim());
					environment.getPropertySources().addLast(new PropertiesPropertySource("props", props));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } 	
    	});
    
   }
}