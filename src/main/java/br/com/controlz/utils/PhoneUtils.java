package br.com.controlz.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtils {

	private PhoneUtils() {
	}

	public static boolean validatePhone(String phoneNumber) {
		boolean isPhoneValid = false;
		String phoneMatch = "^\\(?[1-9]{2}\\)? ?(?:[2-8]|9[1-9])[0-9]{3}\\-?[0-9]{4}$";
		final Pattern pattern = Pattern.compile(phoneMatch, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(phoneNumber);
		if (matcher.find()) {
			isPhoneValid = true;
		}
		return isPhoneValid;
	}
}
