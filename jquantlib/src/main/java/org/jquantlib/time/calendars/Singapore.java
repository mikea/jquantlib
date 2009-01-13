/*
 Copyright (C) 2008 Joon Tiang
 
 This source code is release under the BSD License.
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.time.calendars;

import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.SEPTEMBER;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * Singaporean calendar
 * <p>
 * Holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's day, January 1st</li>
 * <li>Good Friday</li>
 * <li>Labour Day, May 1st</li>
 * <li>National Day, August 9th</li>
 * <li>Christmas, December 25th</li>
 * <p>
 * Other holidays for which no rule is given (data available for 2004-2008
 * only:)
 * </p>
 * <li>Chinese New Year</li>
 * <li>Hari Raya Haji</li>
 * <li>Vesak Day</li>
 * <li>Deepavali</li>
 * <li>Diwali</li>
 * <li>Hari Raya Puasa</li>
 * </ul>
 * 
 * @category calendars
 * 
 * @see <a href="http://www.ses.com.sg/">Stock Exchange of Singapore</a>
 * 
 * @author Joon Tiang
 */


public class Singapore extends DelegateCalendar {
		
	private final static Singapore SGX_CALENDAR = new Singapore(Market.SGX);
			
	private Singapore(Market market) {
		Calendar delegate;
		switch (market) {
		case SGX:
			delegate = new SingaporeSettlementCalendar();
			break;
		
		default:
			throw new IllegalArgumentException("unknown market");
		}
		// FIXME
		setDelegate(delegate);
	}
	
	public static Singapore getCalendar(Market market) {
		switch (market) {
		case SGX:
			return SGX_CALENDAR;
		
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}


	//
	// public enums
	//

	public enum Market {
		/**
		 * Singapore Stock Exchange
		 */
		SGX
	}


	//
	// private inner classes
	//

	final private class SingaporeSettlementCalendar extends WesternCalendar {
		
		public boolean isBusinessDay(Date date) {
	        final Weekday w = date.getWeekday();
	        final int d = date.getDayOfMonth(), dd = date.getDayOfYear();
	        final Month m = date.getMonthEnum();
	        final int y = date.getYear();
	        final int em = easterMonday(y);

	        if (isWeekend(w)
	            // New Year's Day
	            || (d == 1 && m == JANUARY)
	            // Good Friday
	            || (dd == em-3)
	            // Labor Day
	            || (d == 1 && m == MAY)
	            // National Day
	            || (d == 9 && m == AUGUST)
	            // Christmas Day
	            || (d == 25 && m == DECEMBER)
	            
	            // *** variable holidays ***
	            
	            || (y == 2004 && 
	            		(
	            	            // Chinese New Year
	            				((d == 22 || d == 23) && m == JANUARY)
	            	            // Hari Raya Haji
	            				|| ((d == 1 || d == 2) && m == FEBRUARY)
	            	            // Vesak Poya Day
	            				|| (d == 2 && m == JUNE)
	            	            // Deepavali
	            				|| (d == 11 && m == NOVEMBER)
	            	            // Diwali
	            					// weekend??
	            	            // Hari Raya Puasa
	            	            || ((d == 14 || d == 15) && m == NOVEMBER)
	            		))
	            
	            || (y == 2005 &&
	            		(
	            	            // Chinese New Year
	            				((d == 9 || d == 10) && m == FEBRUARY)
	            	            // Hari Raya Haji
	            				|| (d == 21 && m == JANUARY)
	            	            // Vesak Poya Day
	            				|| (d == 22 && m == MAY)
	            	            // Deepavali
	            						// weekend??
	            	            // Diwali
	            				|| (d == 1 && m == NOVEMBER)
	            	            // Hari Raya Puasa
	            				|| (d == 3 && m == NOVEMBER)
	            		))
	            || (y == 2006 &&
	            		(
	            	            // Chinese New Year
	            				((d == 30 || d == 31) && m == JANUARY)
	            	            // Hari Raya Haji
	            				|| (d == 10 && m == JANUARY)
	            	            // Vesak Poya Day
	            	            || (d == 12 && m == MAY)
	            	            // Deepavali
	            	            	// weekend ??
	            	            // Diwali
	            	            	// weekend ??
	            	            // Hari Raya Puasa
	            	            || (d == 24 && m == OCTOBER)
	            		))
	            
	            || (y == 2007 &&
	            		(
	            	            // Chinese New Year
	            				((d == 19 || d == 20) && m == FEBRUARY)
	            	            // Hari Raya Haji
	            				|| (d == 2 && m == JANUARY)
	            				|| (d == 20 && m == DECEMBER)
	            	            // Vesak Poya Day
	            				|| (d == 31 && m == MAY)
	            	            // Deepavali
	            				|| (d == 8 && m == NOVEMBER)
	            	            // Diwali
	            					// weekend??
	            	            // Hari Raya Puasa
	            				|| (d == 13 && m == OCTOBER)
	            		))
	            
	            || (y == 2008 &&
	            		(
	            	            // Chinese New Year
	            				((d == 7 || d == 8) && m == FEBRUARY)
	            	            // Hari Raya Haji
	            				|| (d == 8 && m == DECEMBER)
	            	            // Vesak Poya Day
	            				|| (d == 19 && m == MAY)
	            	            // Deepavali
	            				|| (d == 27 && m == OCTOBER)
	            	            // Diwali
	            					// weekend
	            	            // Hari Raya Puasa
	            				|| (d == 1 && m == OCTOBER)
	            		))
	            
	            || (y == 2009 &&
	            		(
	            	            // Chinese New Year
	            				((d == 26 || d == 27) && m == JANUARY)
	            	            // Hari Raya Haji
	            				|| (d == 27 && m == NOVEMBER)
	            	            // Vesak Poya Day
	            				|| (d == 9 && m == MAY)
	            	            // Deepavali
	            				|| (d == 15 && m == NOVEMBER)
	            	            // Diwali
	            					// weekend??
	            	            // Hari Raya Puasa
	            				|| (d == 20 && m == SEPTEMBER)
	            		))

//XXX
//	            // Chinese New Year
//	            || ((d == 22 || d == 23) && m == JANUARY && y == 2004)
//	            || ((d == 9 || d == 10) && m == FEBRUARY && y == 2005)
//	            || ((d == 30 || d == 31) && m == JANUARY && y == 2006)
//	            || ((d == 19 || d == 20) && m == FEBRUARY && y == 2007)
//	            || ((d == 7 || d == 8) && m == FEBRUARY && y == 2008)
//
//	            // Hari Raya Haji
//	            || ((d == 1 || d == 2) && m == FEBRUARY && y == 2004)
//	            || (d == 21 && m == JANUARY && y == 2005)
//	            || (d == 10 && m == JANUARY && y == 2006)
//	            || (d == 2 && m == JANUARY && y == 2007)
//	            || (d == 20 && m == DECEMBER && y == 2007)
//	            || (d == 8 && m == DECEMBER && y == 2008)
//
//	            // Vesak Poya Day
//	            || (d == 2 && m == JUNE && y == 2004)
//	            || (d == 22 && m == MAY && y == 2005)
//	            || (d == 12 && m == MAY && y == 2006)
//	            || (d == 31 && m == MAY && y == 2007)
//	            || (d == 18 && m == MAY && y == 2008)
//
//	            // Deepavali
//	            || (d == 11 && m == NOVEMBER && y == 2004)
//	            || (d == 8 && m == NOVEMBER && y == 2007)
//	            || (d == 28 && m == OCTOBER && y == 2008)
//
//	            // Diwali
//	            || (d == 1 && m == NOVEMBER && y == 2005)
//
//	            // Hari Raya Puasa
//	            || ((d == 14 || d == 15) && m == NOVEMBER && y == 2004)
//	            || (d == 3 && m == NOVEMBER && y == 2005)
//	            || (d == 24 && m == OCTOBER && y == 2006)
//	            || (d == 13 && m == OCTOBER && y == 2007)
//	            || (d == 1 && m == OCTOBER && y == 2008)
//	            
	            )
	            return false;
	        
	        return true;

		}

		public String getName() {
	       return "Singapore exchange";
	    }
	    
	}

}
