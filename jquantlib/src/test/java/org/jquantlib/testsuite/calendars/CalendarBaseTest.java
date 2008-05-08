/*
 Copyright (C) 2008 Dominik Holenstein

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */

package org.jquantlib.testsuite.calendars;

import static org.jquantlib.time.calendars.JointCalendar.JointCalendarRule.JOIN_BUSINESSDAYS;
import static org.jquantlib.time.calendars.JointCalendar.JointCalendarRule.JOIN_HOLIDAYS;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Argentina;
import org.jquantlib.time.calendars.Brazil;
import org.jquantlib.time.calendars.Germany;
import org.jquantlib.time.calendars.Italy;
import org.jquantlib.time.calendars.Japan;
import org.jquantlib.time.calendars.JointCalendar;
import org.jquantlib.time.calendars.Switzerland;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.time.calendars.UnitedKingdom;
import org.jquantlib.time.calendars.UnitedStates;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;
import org.junit.Test;

/**
 * @author Dominik Holenstein <p>
 * <strong>Description</strong><br>
 * This is the general test base class for Calendars including 
 * generic methods. 
 * 
 */

public class CalendarBaseTest {
	
	public void HolidayListCheck(List <Date> expectedHol, Calendar c, int year)
    {
    	List<Date> hol = c.getHolidayList(DateFactory.getFactory().getDate(1,JANUARY,year),
                DateFactory.getFactory().getDate(31,DECEMBER,year),false);
    	
    	for (int i =0;i<Math.min(hol.size(), expectedHol.size()); i++) {
    		if (!hol.get(i).equals(expectedHol.get(i)))
    			throw new IllegalStateException("expected holiday was " + expectedHol.get(i)
    					+ " while calculated holiday is " + hol.get(i));
    		}
    		if (hol.size()!=expectedHol.size())
    			throw new IllegalStateException("there were " + expectedHol.size()
    					+ " expected holidays, while there are " + hol.size()
    					+ " calculated holidays");
    }

}
