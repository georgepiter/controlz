package br.com.controlz.utils;

import java.util.Random;

public class PasswordUtils {

	private static final Random random = new Random();

	private PasswordUtils() {
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
}
