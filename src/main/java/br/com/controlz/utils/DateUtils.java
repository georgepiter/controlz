package br.com.controlz.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateUtils {

	private DateUtils() {
	}

	public static LocalDate localDateNowSumDays(int days) {
		SimpleDateFormat formatConvert = new SimpleDateFormat("yyyy/MM/dd");
		Calendar dateNow = Calendar.getInstance();
		dateNow.add(Calendar.DATE, +days);
		String dateFormated = formatConvert.format(dateNow.getTime());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return LocalDate.parse(dateFormated, formatter);
	}

}
