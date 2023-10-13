package com.rocketteam.passkeeper.util;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;

public class HashUtility {

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
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);
            return new String(salt);
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
            String passwordWithSalt = password + salt;
            return BCrypt.hashpw(passwordWithSalt, BCrypt.gensalt());
        } catch (Exception e) {
            e.printStackTrace();
            throw new HashingException("Error al hashear la contraseña.");
        }
    }

    /**
     * Verifica si la contraseña coincide con el hash almacenado en la base de datos.
     *
     * @param password                   La contraseña proporcionada por el usuario para la verificación.
     * @param hashedPasswordFromDatabase El hash almacenado en la base de datos que se desea comparar.
     * @return true si la contraseña coincide con el hash almacenado, false en caso contrario.
     */
    public static boolean checkPassword(String password, String hashedPasswordFromDatabase) {
        // Verifica si la contraseña coincide con el hash usando BCrypt
        return BCrypt.checkpw(password, hashedPasswordFromDatabase);
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
