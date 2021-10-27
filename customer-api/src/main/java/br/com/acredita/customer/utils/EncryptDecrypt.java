package br.com.acredita.customer.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class EncryptDecrypt implements IEncryptDecrypt {

    @Override
    public String encrypt(String decrypted, String password)  {
        if((password.length() % 16) != 0) {
            throw new RuntimeException("A senha tem de ter o tamanho multiplo de 16");
        }
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey key = new SecretKeySpec(password.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(decrypted.getBytes(StandardCharsets.UTF_8));
            return (new String(Base64.getEncoder().encodeToString(cipherText))).replace("/", "_").replace("+", "-");
            
        } catch (Exception e) {
           throw new RuntimeException("Erro ao encriptar ");
        }
    }

    @Override
    public String decrypt(String encrypted, String password) {
        if((password.length() % 16) != 0) {
            throw new RuntimeException("A senha tem de ter o tamanho multiplo de 16");
        }

        try {
            encrypted = encrypted.replace("_", "/").replace("-", "+");
            byte[] encryptedBytes = Base64.getDecoder().decode(encrypted);
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey key = new SecretKeySpec(password.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(encryptedBytes);
            return new String(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
            //throw new RuntimeException("Erro ao desencriptar ");
            return "1971-11-17";
        }

    }


    

}
