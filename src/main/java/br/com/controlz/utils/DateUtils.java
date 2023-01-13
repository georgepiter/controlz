package br.com.controlz.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateUtils {

	private DateUtils() {
	}

	public static LocalDate localDateNowSubtractingDays(int days) {
		SimpleDateFormat formatConvert = new SimpleDateFormat("yyyy/MM/dd");
		Calendar dateNow = Calendar.getInstance();
		dateNow.add(Calendar.DATE, -days);
		String dateFormated = formatConvert.format(dateNow.getTime());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return LocalDate.parse(dateFormated, formatter);
	}

	public static int teste(LocalDate dueDate, LocalDate localDateNowSubtractingDay) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar data1 = Calendar.getInstance();
		Calendar data2 = Calendar.getInstance();
		try {
			data1.setTime(sdf.parse(String.valueOf(localDateNowSubtractingDay)));
			data2.setTime(sdf.parse(String.valueOf(dueDate)));
		} catch (ParseException e) {
		}
		int data22 = data2.get(Calendar.DAY_OF_YEAR);
		int data23 = data1.get(Calendar.DAY_OF_YEAR);
		int count = data22 - data23;

		return count;
	}

}
