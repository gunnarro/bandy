package com.gunnarro.android.bandy.utility;

import static org.junit.Assert.*;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import com.gunnarro.android.bandy.utility.Utility;

public class UtilityTest extends TestCase {

	// @Test
	public void formatTime() {
		assertEquals("22:23", Utility.formatTime(22, 23));
		assertEquals(23, Utility.getHour("23:45"));
		assertEquals(45, Utility.getMinute("23:45"));
	}

	// @Test
	public void createSearch() {
		assertEquals("22:23", Utility.createSearch("23233456"));
	}

	// @Test
	public void testToDate() {
		Date date = Utility.timeToDate("22.02.2014 22:23", Utility.DATE_TIME_PATTERN);
		assertEquals("22.02.2014 22:23", Utility.formatTime(date.getTime(), Utility.DATE_TIME_PATTERN));
	}

	// @Test
	public void isInActiveTimePeriode() {
		assertTrue(Utility.isInActiveTimePeriode("01:00", "24:00"));
	}
}
