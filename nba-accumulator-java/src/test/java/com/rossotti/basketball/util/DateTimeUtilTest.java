package com.rossotti.basketball.util;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

public class DateTimeUtilTest {

	@Test
	public void getStringDate() {
		String date = DateTimeUtil.getStringDate(new LocalDate(2013, 3, 30));
		Assert.assertEquals("2013-03-30", date);
	}

	@Test
	public void getStringDateTime() {
		String dateTime = DateTimeUtil.getStringDateTime(new LocalDateTime(2013, 3, 30, 10, 30));
		Assert.assertEquals("2013-03-30 10:30", dateTime);
	}

	@Test
	public void getLocalDate() {
		LocalDate date = DateTimeUtil.getLocalDate("2014-06-30");
		Assert.assertEquals(new LocalDate("2014-06-30"), date);
	}

	@Test
	public void getLocalDateTime() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTime("2014-06-30 10:30");
		Assert.assertEquals(new LocalDateTime("2014-06-30T10:30"), dateTime);
	}
	
	@Test
	public void getLocalDateTimeMin() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTimeMin(new LocalDate("2014-06-30"));
		Assert.assertEquals(new LocalDateTime("2014-06-30T00:00"), dateTime);
	}

	@Test
	public void getLocalDateTimeMax() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTimeMax(new LocalDate("2014-06-30"));
		Assert.assertEquals(new LocalDateTime("2014-06-30T23:59"), dateTime);
	}
}
