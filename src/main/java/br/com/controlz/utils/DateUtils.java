package br.com.controlz.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtils {

	private DateUtils() {
	}

	public static boolean checkDaysBeforeExpiration(LocalDate dueDate) {
		LocalDate localDateNow = LocalDate.now();
		boolean isBeforeExpiration = false;
		long daysUntilExpiration = ChronoUnit.DAYS.between(localDateNow, dueDate);
		if (daysUntilExpiration == 2) {
			isBeforeExpiration = true;
		}
		return isBeforeExpiration;
	}
}
