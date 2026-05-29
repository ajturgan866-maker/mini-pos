package com.minipos.pos.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    /**
     * Превращает обычный пароль в хэш SHA-256.
     * Используется при регистрации или смене пароля.
     */
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Это исключение крайне маловероятно для SHA-256
            throw new RuntimeException("Ошибка: алгоритм хэширования не найден", e);
        }
    }

    /**
     * Проверяет, совпадает ли введенный пользователем пароль с хэшем из базы.
     * @param plainPassword пароль, введенный в текстовое поле.
     * @param hashedPassword хэш, который хранится в базе данных.
     * @return true, если пароли совпадают, иначе false.
     */
    public static boolean check(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        // Хэшируем введенный пароль тем же алгоритмом и сравниваем строки
        String hashedInput = hash(plainPassword);
        return hashedInput.equals(hashedPassword);
    }
}