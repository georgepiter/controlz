package br.com.controlz.utils;

import java.text.NumberFormat;
import java.util.Locale;

public final class MoneyUtils {

	private MoneyUtils() {
	}

	public static String formatMoneyToPatternBR(double value) {
		return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(value);
	}
}
