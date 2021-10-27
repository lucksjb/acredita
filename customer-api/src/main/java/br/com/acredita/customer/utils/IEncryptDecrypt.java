package br.com.acredita.customer.utils;

public interface IEncryptDecrypt {
    public String encrypt(String decrypted, String strKey);
    public String decrypt(String encrypted, String strKey);
}
