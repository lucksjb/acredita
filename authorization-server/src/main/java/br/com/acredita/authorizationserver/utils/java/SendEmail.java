package br.com.acredita.authorizationserver.utils.java;

import java.io.IOException;
//import java.util.Properties;

//import org.apache.commons.mail.EmailException;
//import org.apache.commons.mail.HtmlEmail;

public class SendEmail {

	public static void enviaEmail(String emailDestino, String assunto, String texto) throws IOException {
//
//		try {
//    		Properties props = new Properties();
//    		props.load(SendEmail.class.getClassLoader().getResourceAsStream("mail.properties"));
//        	HtmlEmail email = new HtmlEmail();
//        	email.setHostName(props.getProperty("mail.server.host"));
//        	email.setSmtpPort(Integer.parseInt(props.getProperty("mail.server.port")));
//            email.setCharset("UTF-8");
//        	email.setSSLOnConnect(Boolean.parseBoolean(props.getProperty("mail.enable.ssl")));
//        	email.setAuthentication(props.getProperty("mail.username"), props.getProperty("mail.password"));
//        	email.setFrom(props.getProperty("mail.from"));
//            email.addTo(emailDestino);
//            email.setSubject(assunto);
//            email.setHtmlMsg("<html> <body>"+texto+" </body> </html>");
//            email.send();
//        } catch (EmailException ex) {
//            ex.printStackTrace();
//        }
//		
	}

}
