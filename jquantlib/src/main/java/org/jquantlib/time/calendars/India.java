/*
 Copyright (C) 2008 Renjith Nair
  
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
/*
 Copyright (C) 2005, 2007 StatPro Italia srl

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.
*/


package org.jquantlib.time.calendars;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;


/** 
 * Holidays for the National Stock Exchange of India
 * 	<ul>
 * 	<li>Saturdays</li>
 * 	<li>Sundays</li>
 * 	<li>Republic Day, January 26th</li>
 * 	<li>Good Friday</li>
 * 	<li>Ambedkar Jayanti, April 14th</li>
 * 	<li>Independence Day, August 15th</li>
 * 	<li>Gandhi Jayanti, October 2nd</li>
 * 	<li>Christmas, December 25th</li>
 * 	</ul>
 * 	Other holidays for which no rule is given (data available for 2005-2008 only:)
 *  2008 data taken from QuantLib 0.9.6
 * 	<ul>
 * 	<li>Bakri Id</li>
 * 	<li>Moharram</li>
 * 	<li>Mahashivratri</li>
 * 	<li>Holi</li>
 * 	<li>Ram Navami</li>
 * 	<li>Mahavir Jayanti</li>
 * 	<li>Id-E-Milad</li>
 * 	<li>Maharashtra Day</li>
 * 	<li>Buddha Pournima</li>
 * 	<li>Ganesh Chaturthi</li>
 * 	<li>Dasara</li>
 * 	<li>Laxmi Puja</li>
 * 	<li>Bhaubeej</li>
 * 	<li>Ramzan Id</li>
 * 	<li>Guru Nanak Jayanti</li>
 * 	</ul>
 * 
 * @category calendars
 * 
 * @see <a href="http://www.nse-india.com/">National Stock Exchange of India</a>
 * 
 * @author Renjith Nair
 */

public class India extends DelegateCalendar {
	
	// National stock-exchange of India Calendar
	private final static India NSE_CALENDAR = new India(Market.NSE);

	private India(Market market) {
		Calendar delegate;
		switch (market) {
		case NSE:
			delegate = new IndiaNSECalendar();
			break;
		default:
			throw new IllegalArgumentException("unknown market");
		}
		setDelegate(delegate);
	}

	public static India getCalendar(Market market) {
		switch (market) {
		case NSE:
			return NSE_CALENDAR;
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}
	
	
	//
	// public enums
	//
	
	public enum Market {
		/*
		 * National stock-exchange of India
		 */
		NSE,
	};

	
	//
	// private inner classes
	//
	
	final private class IndiaNSECalendar extends WesternCalendar {

		public String getName() {
			return "National Stock Exchange of India";
		}

		public boolean isBusinessDay(Date date) {
			Weekday w = date.getWeekday();
			int d = date.getDayOfMonth(), dd = date.getDayOfYear();
			int m = date.getMonth();
			int y = date.getYear();
			int em = easterMonday(y);
			
			if (isWeekend(w)
				 // Republic Day
	            || (d == 26 && m == Month.JANUARY.toInteger())
	            // Good Friday
	            || (dd == em-3)
	            // Ambedkar Jayanti
	            || (d == 14 && m == Month.APRIL.toInteger())
	            // Independence Day
	            || (d == 15 && m == Month.AUGUST.toInteger())
	            // Gandhi Jayanti
	            || (d == 2 && m == Month.OCTOBER.toInteger())
	            // Christmas
	            || (d == 25 && m == Month.DECEMBER.toInteger())
	            )
				return false;
			
			if (y == 2005) {
	            // Moharram, Holi, Maharashtra Day, and Ramzan Id fall
	            // on Saturday or Sunday in 2005		
	            if (
	            	// Bakri Id
	                (d == 21 && m == Month.JANUARY.toInteger())
	                // Ganesh Chaturthi
	                || (d == 7 && m == Month.SEPTEMBER.toInteger())
	                // Dasara
	                || (d == 12 && m == Month.OCTOBER.toInteger())
	                // Laxmi Puja
	                || (d == 1 && m == Month.NOVEMBER.toInteger())
	                // Bhaubeej
	                || (d == 3 && m == Month.NOVEMBER.toInteger())
	                // Guru Nanak Jayanti
	                || (d == 15 && m == Month.NOVEMBER.toInteger())
	                )
	                return false;
			}
			
			if (y == 2006) {
	            if (
	            	// Bakri Id
	                (d == 11 && m == Month.JANUARY.toInteger())
	                // Moharram
	                || (d == 9 && m == Month.FEBRUARY.toInteger())
	                // Holi
	                || (d == 15 && m == Month.MARCH.toInteger())
	                // Ram Navami
	                || (d == 6 && m == Month.APRIL.toInteger())
	                // Mahavir Jayanti
	                || (d == 11 && m == Month.APRIL.toInteger())
	                // Maharashtra Day
	                || (d == 1 && m == Month.MAY.toInteger())
	                // Bhaubeej
	                || (d == 24 && m == Month.OCTOBER.toInteger())
	                // Ramzan Id
	                || (d == 25 && m == Month.OCTOBER.toInteger())
	                )
	                return false;
	        }
			
	        if (y == 2007) {
	            if (
	            	// Bakri Id
	                (d == 1 && m == Month.JANUARY.toInteger())
	                // Moharram
	                || (d == 30 && m == Month.JANUARY.toInteger())
	                // Mahashivratri
	                || (d == 16 && m == Month.FEBRUARY.toInteger())
	                // Ram Navami
	                || (d == 27 && m == Month.MARCH.toInteger())
	                // Maharashtra Day
	                || (d == 1 && m == Month.MAY.toInteger())
	                // Buddha Pournima 
	                || (d == 2 && m == Month.MAY.toInteger())
	                // Laxmi Puja
	                || (d == 9 && m == Month.NOVEMBER.toInteger())
	                // Bakri Id (again)
	                || (d == 21 && m == Month.DECEMBER.toInteger())
	                )
	                return false;
	        }
	        
	        //2008 data taken from QuantLib 0.9.6
	        if (y == 2008) {
	            if (
	            	// Mahashivratri
	                (d == 6 && m == Month.MARCH.toInteger())
	                // Id-E-Milad
	                || (d == 20 && m == Month.MARCH.toInteger())
	                // Mahavir Jayanti
	                || (d == 18 && m == Month.APRIL.toInteger())
	                // Maharashtra Day
	                || (d == 1 && m == Month.MAY.toInteger())
	                // Buddha Pournima
	                || (d == 19 && m == Month.MAY.toInteger())
	                // Ganesh Chaturthi
	                || (d == 3 && m == Month.SEPTEMBER.toInteger())
	                // Ramzan Id
	                || (d == 2 && m == Month.OCTOBER.toInteger())
	                // Dasara
	                || (d == 9 && m == Month.OCTOBER.toInteger())
	                // Laxmi Puja
	                || (d == 28 && m == Month.OCTOBER.toInteger())
	                // Bhau bhij
	                || (d == 30 && m == Month.OCTOBER.toInteger())
	                // Gurunanak Jayanti
	                || (d == 13 && m == Month.NOVEMBER.toInteger())
	                // Bakri Id
	                || (d == 9 && m == Month.DECEMBER.toInteger())
	                // Mumbai Terror attack
	                || (d == 27 && m == Month.NOVEMBER.toInteger())
	                )
	                return false;
	        }
	        
			return true;
		}
	}
	
}

