/*
 Copyright (C) 2009 Apratim Rajendra
 
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

package org.jquantlib.examples;

import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultDate;
import org.jquantlib.util.Month;
import org.jquantlib.util.StopClock;
import org.jquantlib.util.Updatable;

/**
 * 
 * This class explores the functionalities provided by Date interface.
 * 
 * @author Apratim Rajendra
 *
 */

public class Dates {
	
	public static void main(String args[]){
		
		System.out.println("\n\n::::: "+Dates.class.getSimpleName()+" :::::");

		StopClock clock = new StopClock();
		clock.startClock();
		
		//Let's take todays date to explore the date interface
		Date dateToday = DefaultDate.JQLibDateUtil.getFactory().getTodaysDate();
		System.out.println("Today's date is = "+dateToday);
		
		//Get the Month enum and the month's integer equivalent of this date
		Month month = dateToday.getMonthEnum();
		int integerEquivalentOfMonth = dateToday.getMonth();
		System.out.println("The month of today's date is = "+month);
		System.out.println("The integer equivalent of this month as obtained from the date is = "+integerEquivalentOfMonth);
		System.out.println("The integer equivalent of the date as obtained from the Month is also = "+month.toInteger());
		
		//Get the weekday
		Weekday weekDayOfThisDate = dateToday.getWeekday();
		System.out.println("The weekday of this date is = "+weekDayOfThisDate);
		
		//Get the day of the date for it's month
		System.out.println("The day of the date as a day in this date's month(1-31) is = "+dateToday.getDayOfMonth());
		
		//Get the day of the date for it's year
		System.out.println("The day of the date as day in it's year(1-366) is = "+dateToday.getDayOfYear());
		
		//Check if the date belongs to a leap year
		if(dateToday.isLeap()){
			System.out.println("Today's date belong to leap year");
		}
		
		//Get the next nextWeekdayDate of this date's month having weekday as TUESDAY
		Date nextWeekdayDate = dateToday.getNextWeekday(Weekday.TUESDAY);
		System.out.println("The date of the next weekday is = "+nextWeekdayDate);
		
		//Get the 4th weekdayDate of this date's month having weekday as TUESDAY
		Date fourthWeekdayDate = dateToday.getNthWeekday(4,Weekday.TUESDAY);
		System.out.println("The fourthWeekdayDate which is TUESDAY is = "+fourthWeekdayDate);
		
		//Let's try getting the first date of the month to which today's date belong to
		Date dateEndOfMonth = dateToday.getEndOfMonth();
		int dayOfEndOfMonth = dateEndOfMonth.getDayOfMonth();
		Date dateStartOfMonth = dateEndOfMonth.getDateAfter(-dayOfEndOfMonth+1);
		System.out.println("The first date of the month to which todays date belong to is = "+dateStartOfMonth);
		
		//Let's try getting the first date of the month to which today's date belong to using Period
		Period period = new Period(-dateToday.getDayOfMonth()+1,TimeUnit.DAYS);
		Date dateStartOfMonthUsingPeriod = dateToday.getDateAfter(period);
		System.out.println("The first date of the month to which today's date belong to using period is = "+dateStartOfMonthUsingPeriod);
		
		//Let's use adjust function to get the same result as obtained above
		dateStartOfMonthUsingPeriod = dateToday.adjust(period);
		System.out.println("The first date of the month to which today's date belong to using adjustment of period is = "+dateStartOfMonthUsingPeriod);
		
		
		//<================Let's do some date comparisons========================>
		
		//Startdate of the month is less than endDate of this dates month
		if(dateStartOfMonthUsingPeriod.le(dateEndOfMonth)){
			System.out.println("Start date is less than end date?");
		}
		
		//EndDate of the month is greater than Startdate  of this dates month
		if(dateEndOfMonth.ge(dateStartOfMonthUsingPeriod)){
			System.out.println("End date is greater than start date");
		}
		
		//Let's increment today's date till the endOfMonth
		Date date = dateToday;
		while(!date.eq(dateEndOfMonth)){
			date.increment();
		}
		System.out.println("The date variable has been incrmented to endOfMonth and is = "+date);
		
		//Let's decrement the same till begining of the month
		date = dateToday;
		while(!date.eq(dateStartOfMonth)){
			date.decrement();
		}
		System.out.println("The date variable has been decremented to startOfMonth and is = "+date);
		
		//Let's update the todaysDate with the date just after it as the date is updatable
		dateToday = DefaultDate.JQLibDateUtil.getFactory().getTodaysDate();
		Updatable<Date> updatableDate = dateToday.getUpdatable();
		updatableDate.update(dateToday.getDateAfter(1));
		System.out.println("Today's date dateToday has been updated to = "+dateToday);
		
		//Let's change the dateToday to current date
		updatableDate.update(dateToday.getDateAfter(-1));
		System.out.println("Today's date dateToday has been updated to = "+dateToday);
		
		clock.stopClock();
		clock.log();
		
	}

}
