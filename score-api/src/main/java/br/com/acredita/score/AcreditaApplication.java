package br.com.acredita.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class AcreditaApplication implements CommandLineRunner {
	@Autowired
	private ApplicationContext applicationContext;


	
	
	public static void main(String[] args) {
		SpringApplication.run(AcreditaApplication.class, args);
	}




	@Override
	public void run(String... args) throws Exception {


		
	}

}
