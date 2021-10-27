package br.com.acredita.authorizationserver.utils.java;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


public class JavaFunctions {
	public static final String DATE_FORMATTER= "yyyy-MM-dd HH:mm:ss";
	
	
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	
	public static String md5(String s) {
		MessageDigest m = null;

		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Erro ao encriptar a senha");
			e.printStackTrace();
		}
		m.update(s.getBytes(), 0, s.length());
		BigInteger i = new BigInteger(1, m.digest());

		// Formatando o resuldado em uma cadeia de 32 caracteres, completando
		// com 0 caso falte
		return String.format("%1$032X", i);
	}

	public static String gerarNomeArquivo() {
		// quantidade e milisegundos desde 1/01/1970
		return String.valueOf(Calendar.getInstance().getTimeInMillis());
	}

	
	public static String leArquivo(String arquivo) throws IOException {
		return FileUtils.readFileToString(new File(arquivo),StandardCharsets.UTF_8); // ,
																// StandardCharsets.UTF_8
																// nao funciona
																// somente no
																// commmons-io
																// 2.4
	}

	/* DETERMINADO A ACABAR COM NullPointerException */
	public static Boolean isNullorEmpty(Object o) {
		if (o == null) {
			return true;
		}

		if (o instanceof String) {
			return StringUtils.isEmpty((String) o);
		}

		if (o instanceof Long) {
			Long longo = (Long) o;
			return longo == 0L;
		}

		if (o instanceof Integer) {
			Integer inteiro = (Integer) o;
			return inteiro == 0;
		}

		return false;
	}

	public static boolean isNumeric(String str) {
		try {
			@SuppressWarnings("unused")
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	
	
    public static int idade(final LocalDate aniversario) {
        final LocalDate dataAtual = LocalDate.now();
        final Period periodo = Period.between(aniversario, dataAtual);
        return periodo.getYears();
    }

    public static boolean senhaForte(String senha) {
        if (senha.length() < 6) return false;

        boolean achouNumero = false;
        boolean achouMaiuscula = false;
        boolean achouMinuscula = false;
        boolean achouSimbolo = false;
        for (char c : senha.toCharArray()) {
             if (c >= '0' && c <= '9') {
                 achouNumero = true;
             } else if (c >= 'A' && c <= 'Z') {
                 achouMaiuscula = true;
             } else if (c >= 'a' && c <= 'z') {
                 achouMinuscula = true;
             } else {
                 achouSimbolo = true;
             }
        }
        return achouNumero && achouMaiuscula && achouMinuscula && achouSimbolo;
    }    
    
    
    public static String senhaEncriptada(Long usuario, String senha, LocalDateTime creationDateTime) {
    	return md5(usuario.toString().trim()+senha.trim()+LocalDatetimeToString(creationDateTime));
    }
    
    
    public static String LocalDatetimeToString(LocalDateTime localDateTime) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        return localDateTime.format(formatter);
    }
}
