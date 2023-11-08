package com.rocketteam.passkeeper.util;


import android.annotation.SuppressLint;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class HashUtility {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    /**
     * Excepción personalizada para errores durante el proceso de hashing.
     */
    public static class HashingException extends Exception {
        public HashingException(String message) {
            super(message);
        }
    }

    /**
     * Excepción personalizada para errores durante la generación de salt.
     */
    public static class SaltException extends Exception {
        public SaltException(String message) {
            super(message);
        }
    }

    /**
     * Genera un salt aleatorio de 16 bytes utilizando SecureRandom.
     *
     * @return El salt aleatorio generado.
     * @throws SaltException Si ocurre un error durante la generación de salt.
     */
    public static String generateSalt() throws SaltException {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SaltException("Error al generar el salt.");
        }
    }

    /**
     * Combina la contraseña con el salt y genera un hash utilizando BCrypt.
     *
     * @param password La contraseña que se desea hashear.
     * @param salt     El salt que se utilizará para hashear la contraseña.
     * @return El hash resultante de la combinación de la contraseña y el salt.
     * @throws HashingException Si ocurre un error durante el proceso de hashing.
     */
    public static String hashPassword(String password, String salt) throws HashingException {
        try {
            // Combina la contraseña con la sal
            String passwordWithSalt = password + salt;
            // Aplica el algoritmo de hash SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(passwordWithSalt.getBytes());
            // Convierte el hash a formato Base64 para almacenarlo
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
           e.printStackTrace();
           throw new HashingException("Error al hashear la contraseña.");
        }
    }

    /**
     * Verifica si la contraseña coincide con el hash almacenado en la base de datos.
     *
     * @param inputPassword     La contraseña proporcionada por el usuario para la verificación.
     * @param storedHash        El hash almacenado en la base de datos que se desea comparar.
     * @param salt              El salt del usuario
     * @return true si la contraseña coincide con el hash almacenado, false en caso contrario.
     */
    public static boolean checkPassword(String inputPassword, String storedHash, String salt) throws HashingException {
        // Calcula el hash de la contraseña ingresada con la misma sal
        String inputHash = HashUtility.hashPassword(inputPassword, salt);
        // Compara el hash calculado con el hash almacenado
        return inputHash.equals(storedHash);
    }

    /**
     * Este método cifra la cadena de datos proporcionada utilizando el algoritmo de cifrado especificado
     * y la clave proporcionada como sal. El resultado se devuelve como una cadena Base64 codificada.
     *
     * @param data La cadena de datos que se va a cifrar.
     * @param salt La clave utilizada como sal para el cifrado.
     * @return Una cadena cifrada codificada en Base64.
     * @throws Exception Si ocurre un error durante el proceso de cifrado.
     */
    public static String encrypt(String data, String salt) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(salt.getBytes(), ALGORITHM);
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Este método descifra la cadena cifrada proporcionada utilizando el algoritmo de cifrado especificado
     * y la clave proporcionada como sal. El resultado se devuelve como una cadena de texto sin cifrar.
     *
     * @param encryptedData La cadena cifrada que se va a descifrar.
     * @param salt La clave utilizada como sal para el cifrado.
     * @return Una cadena de texto sin cifrar que representa los datos descifrados.
     * @throws Exception Si ocurre un error durante el proceso de descifrado.
     */
    public static String decrypt(String encryptedData, String salt) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(salt.getBytes(), ALGORITHM);
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    /**
     * Genera una contraseña aleatoria segura con letras mayúsculas, minúsculas, números y caracteres especiales.
     *
     * @param length La longitud de la contraseña.
     * @return La contraseña generada.
     */
    public static String generateRandomPassword(int length) {
        final String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charset.length());
            password.append(charset.charAt(randomIndex));
        }
        return password.toString();
    }
}
