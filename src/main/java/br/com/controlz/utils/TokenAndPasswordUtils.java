package br.com.controlz.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

public class TokenAndPasswordUtils {

    private static final Random random = new Random();

    public static String getToken() {
        String userName = "bruxo";
        return generateToken(userName);
    }

    protected static String generateToken(String username) {
        String secret = "MzQyNTAwbW9uaw==";
        long expires = 172800000L;
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expires))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public static String generateNewPassword() {
        char[] chars = new char[10];
        for (int i = 0; i < 10; i++) {
            chars[i] = getRandomChar();
        }
        return new String(chars);
    }

    private static char getRandomChar() {
        int opt = random.nextInt(3);
        if (opt == 0) {
            return (char) (random.nextInt(10) + 48);
        } else if (opt == 1) {
            return (char) (random.nextInt(26) + 65);
        } else {
            return (char) (random.nextInt(26) + 97);
        }
    }

//    public static void main(String[] args) {
//        String token = getToken();
//        System.out.println("token = " + token);
//    }
}
