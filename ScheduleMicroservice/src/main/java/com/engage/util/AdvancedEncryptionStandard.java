package com.engage.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.jboss.jandex.Main;

public class AdvancedEncryptionStandard
{
    private static String encryptionKey="Secure@helthapi1";

//    public AdvancedEncryptionStandard(String encryptionKey)
//    {
//        this.encryptionKey = "Secure@helthapi1";
//    }

    public static String encrypt(String plainText) throws Exception
    {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        return Base64.encodeBase64String(encryptedBytes);
    }

    public static String decrypt(String encrypted) throws Exception
    {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        byte[] plainBytes = cipher.doFinal(Base64.decodeBase64(encrypted));

        return new String(plainBytes);
    }

    private static Cipher getCipher(int cipherMode)
            throws Exception
    {
        String encryptionAlgorithm = "AES";
        SecretKeySpec keySpecification = new SecretKeySpec(
                encryptionKey.getBytes("UTF-8"), encryptionAlgorithm);
        Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
        cipher.init(cipherMode, keySpecification);

        return cipher;
    }
    
//    public static void main(String[] args) {
//		try {
//			System.out.println(encrypt(null));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
    
}