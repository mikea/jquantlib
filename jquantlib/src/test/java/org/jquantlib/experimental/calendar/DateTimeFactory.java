package org.jquantlib.experimental.calendar;

import org.jquantlib.util.Date;

public abstract class DateTimeFactory {

	/**
	 * defaults to the one that returns 
	 * JQ implementation
	 * @return
	 */
	public DateTimeFactory getDateTimeFactory() {
		return null;
	}

	public Date create(int day, int month, int year) {
		return null;
	}

	public DateTime create(int day, int month, int year, int hour, int minutes,
			int seconds) {
		return null;
	}

	public DateTime create(int day, int month, int year, int hour, int minutes,
			int seconds, int nanoseconds) {
		return null;
	}

}
