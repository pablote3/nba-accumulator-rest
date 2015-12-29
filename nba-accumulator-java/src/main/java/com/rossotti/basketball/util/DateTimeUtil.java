package com.rossotti.basketball.util;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtil {

	static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
	static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
	
	static public String getStringDate(LocalDate date) {
		return date.toString(dateFormatter);
	}

	static public String getStringDateTime(LocalDateTime dateTime) {
		return dateTime.toString(dateTimeFormatter);
	}

	static public LocalDate getLocalDate(String strDate) {
		return dateFormatter.parseLocalDate(strDate);
	}

	static public LocalDate getLocalDate(LocalDateTime dateTime) {
		return dateTime.toLocalDate();
	}

	static public LocalDateTime getLocalDateTime(String strDateTime) {
		return dateTimeFormatter.parseLocalDateTime(strDateTime);
	}

	static public LocalDateTime getLocalDateTimeMin(LocalDate localDate) {
		String stringDate = DateTimeUtil.getStringDate(localDate);
		return dateTimeFormatter.parseLocalDateTime(stringDate + " 00:00");
	}

	static public LocalDateTime getLocalDateTimeMax(LocalDate localDate) {
		String stringDate = DateTimeUtil.getStringDate(localDate);
		return dateTimeFormatter.parseLocalDateTime(stringDate + " 23:59");
	}

	static public LocalDate getLocalDateSeasonMin(LocalDate localDate) {
		if (localDate.getMonthOfYear() <= 6) {
			return new LocalDate(localDate.getYear() - 1, 7, 1);
		}
		else {
			return new LocalDate(localDate.getYear(), 7, 1);
		}
	}

	static public LocalDate getLocalDateSeasonMax(LocalDate localDate) {
		if (localDate.getMonthOfYear() <= 6) {
			return new LocalDate(localDate.getYear(), 6, 30);
		}
		else {
			return new LocalDate(localDate.getYear() + 1, 6, 30);
		}
	}

	static public LocalDateTime getLocalDateTimeSeasonMin(LocalDate localDate) {
		if (localDate.getMonthOfYear() <= 6) {
			return new LocalDateTime(localDate.getYear() - 1, 7, 1, 0, 0);
		}
		else {
			return new LocalDateTime(localDate.getYear(), 7, 1, 0, 0);
		}
	}

	static public LocalDateTime getLocalDateTimeSeasonMax(LocalDate localDate) {
		if (localDate.getMonthOfYear() <= 6) {
			return new LocalDateTime(localDate.getYear(), 6, 30, 23, 59);
		}
		else {
			return new LocalDateTime(localDate.getYear() + 1, 6, 30, 23, 59);
		}
	}

//	static public String getSeason(LocalDate date) {
//		LocalDate minDate = getDateMinSeason(date);
//		String minYear = minDate.toString(DateTimeFormat.forPattern("yyyy"));
//		
//		LocalDate  maxDate = getDateMaxSeason(date);
//		String maxYear = maxDate.toString(DateTimeFormat.forPattern("yy"));
//		
//		return minYear + "-" + maxYear; 
//	}
//	
//	static public boolean isDate(String strDate)  {
//		try {
//			DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
//			dateTimeFormatter.parseDateTime(strDate);
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}
//
//	static public LocalDate getDateMinusOneDay(LocalDate date) {
//		return date.minusDays(1);
//	}
//
//	static public LocalDate getLocalDateFromDateTime(DateTime date) {
//		return new LocalDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
//	}
//
//	static public Long getDaysBetweenTwoDateTimes(DateTime minDate, DateTime maxDate) {
//		Long days = 0L;
//		if (minDate != null) {
//			Duration duration = new Duration(minDate.dayOfMonth().roundFloorCopy(), maxDate.dayOfMonth().roundFloorCopy());
//			if (duration.getStandardDays() < 30)
//				days = duration.getStandardDays();
//		}
//		return days;
//	}
}