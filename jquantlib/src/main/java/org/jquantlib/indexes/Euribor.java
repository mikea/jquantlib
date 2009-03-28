/*
 Copyright (C) 2007 Srinivas Hasti

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

package org.jquantlib.indexes;

import java.util.Currency;

import org.jquantlib.daycounters.Actual360;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Target;

/**
 * @author Srinivas Hasti
 * 
 */
// FIXME: code review
public class Euribor extends IborIndex {

	public static class Euribor365 extends Euribor {
		public Euribor365(Period tenor, Handle<YieldTermStructure> h) {
			super("Euribor365", tenor, 2, // settlement days
					Actual365Fixed.getDayCounter(), h);
			if (tenor.units() == TimeUnit.DAYS)
				throw new IllegalArgumentException("for daily tenors (" + tenor
						+ ") dedicated DailyTenor constructor must be used");
		}

	}

	public static class DailyTenorEuribor extends IborIndex {
		public DailyTenorEuribor(int settlementDays,
				Handle<YieldTermStructure> h) {
			super("Euribor", new Period(1, TimeUnit.DAYS), settlementDays,
					Target.getCalendar(), Currency.getInstance("EUR"),
					euriborConvention(new Period(1, TimeUnit.DAYS)),
					euriborEOM(new Period(1, TimeUnit.DAYS)), Actual360
							.getDayCounter(), h);
		}
	}
	
	public static class DailyTenorEuribor365 extends IborIndex {
		public DailyTenorEuribor365(int settlementDays,
				Handle<YieldTermStructure> h) {
			super("Euribor", new Period(1, TimeUnit.DAYS), settlementDays,
					Target.getCalendar(), Currency.getInstance("EUR"),
					euriborConvention(new Period(1, TimeUnit.DAYS)),
					euriborEOM(new Period(1, TimeUnit.DAYS)), Actual365Fixed.getDayCounter(), h);
		}
	}
	
    /**
     * 
     * 1-week Euribor index
     *
     */
	public static class EuriborSW extends Euribor{
	    public EuriborSW(Handle<YieldTermStructure> h){
	        super(new Period(1, TimeUnit.WEEKS), h);
	    }
	}
	
	/**
	 * 
	 * 2-weeks Euribor index
	 *
	 */
	public static class Euribor2W extends Euribor{
	    public Euribor2W(Handle<YieldTermStructure> h){
	        super(new Period(2, TimeUnit.WEEKS), h);
	    }
	}
	
	 /**
     * 
     * 3-weeks Euribor index
     *
     */
    public static class Euribor3W extends Euribor{
        public Euribor3W(Handle<YieldTermStructure> h){
            super(new Period(3, TimeUnit.WEEKS), h);
        }
    }
	
    /**
     * 
     * 1-month Euribor index
     *
     */
    public static class Euribor1M extends Euribor{
        public Euribor1M(Handle<YieldTermStructure> h){
            super(new Period(1, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 2-months Euribor index
     *
     */
    public static class Euribor2M extends Euribor{
        public Euribor2M(Handle<YieldTermStructure> h){
            super(new Period(2, TimeUnit.MONTHS), h);
        }
    }
	
    /**
     * 
     * 3-months Euribor index
     *
     */
    public static class Euribor3M extends Euribor{
        public Euribor3M(Handle<YieldTermStructure> h){
            super(new Period(3, TimeUnit.MONTHS), h);
        }
    }
	
    /**
     * 
     * 4-months Euribor index
     *
     */
    public static class Euribor4M extends Euribor{
        public Euribor4M(Handle<YieldTermStructure> h){
            super(new Period(4, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 5-months Euribor index
     *
     */
    public static class Euribor5M extends Euribor{
        public Euribor5M(Handle<YieldTermStructure> h){
            super(new Period(5, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 6-months Euribor index
     *
     */
    public static class Euribor6M extends Euribor{
        public Euribor6M(Handle<YieldTermStructure> h){
            super(new Period(6, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 7-months Euribor index
     *
     */
    public static class Euribor7M extends Euribor{
        public Euribor7M(Handle<YieldTermStructure> h){
            super(new Period(7, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 8-months Euribor index
     *
     */
    public static class Euribor8M extends Euribor{
        public Euribor8M(Handle<YieldTermStructure> h){
            super(new Period(8, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 9-months Euribor index
     *
     */
    public static class Euribor9M extends Euribor{
        public Euribor9M(Handle<YieldTermStructure> h){
            super(new Period(9, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 10-months Euribor index
     *
     */
    public static class Euribor10M extends Euribor{
        public Euribor10M(Handle<YieldTermStructure> h){
            super(new Period(10, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 11-months Euribor index
     *
     */
    public static class Euribor11M extends Euribor{
        public Euribor11M(Handle<YieldTermStructure> h){
            super(new Period(11, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 1-year Euribor index
     *
     */
    public static class Euribor1Y extends Euribor{
        public Euribor1Y(Handle<YieldTermStructure> h){
            super(new Period(1, TimeUnit.YEARS), h);
        }
    }
	
    /**
     * 
     * 1-week Euribor365 index
     *
     */
    public static class Euribor365_SW extends Euribor365{
        public Euribor365_SW(Handle<YieldTermStructure> h){
            super(new Period(1, TimeUnit.WEEKS), h);
        }
    }
    
    /**
     * 
     * 2-week Euribor365 index
     *
     */
    public static class Euribor365_2W extends Euribor365{
        public Euribor365_2W(Handle<YieldTermStructure> h){
            super(new Period(1, TimeUnit.WEEKS), h);
        }
    }
    
    /**
     * 
     * 3-week Euribor365 index
     *
     */
    public static class Euribor365_3W extends Euribor365{
        public Euribor365_3W(Handle<YieldTermStructure> h){
            super(new Period(3, TimeUnit.WEEKS), h);
        }
    }
    
    /**
     * 
     * 1-month Euribor365 index
     *
     */
    public static class Euribor365_1M extends Euribor365{
        public Euribor365_1M(Handle<YieldTermStructure> h){
            super(new Period(1, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 2-month Euribor365 index
     *
     */
    public static class Euribor365_2M extends Euribor365{
        public Euribor365_2M(Handle<YieldTermStructure> h){
            super(new Period(2, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 3-month Euribor365 index
     *
     */
    public static class Euribor365_3M extends Euribor365{
        public Euribor365_3M(Handle<YieldTermStructure> h){
            super(new Period(3, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 4-month Euribor365 index
     *
     */
    public static class Euribor365_4M extends Euribor365{
        public Euribor365_4M(Handle<YieldTermStructure> h){
            super(new Period(4, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 5-month Euribor365 index
     *
     */
    public static class Euribor365_5M extends Euribor365{
        public Euribor365_5M(Handle<YieldTermStructure> h){
            super(new Period(5, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 6-month Euribor365 index
     *
     */
    public static class Euribor365_6M extends Euribor365{
        public Euribor365_6M(Handle<YieldTermStructure> h){
            super(new Period(6, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 7-month Euribor365 index
     *
     */
    public static class Euribor365_7M extends Euribor365{
        public Euribor365_7M(Handle<YieldTermStructure> h){
            super(new Period(7, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 8-month Euribor365 index
     *
     */
    public static class Euribor365_8M extends Euribor365{
        public Euribor365_8M(Handle<YieldTermStructure> h){
            super(new Period(8, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 9-month Euribor365 index
     *
     */
    public static class Euribor365_9M extends Euribor365{
        public Euribor365_9M(Handle<YieldTermStructure> h){
            super(new Period(9, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 10-month Euribor365 index
     *
     */
    public static class Euribor365_10M extends Euribor365{
        public Euribor365_10M(Handle<YieldTermStructure> h){
            super(new Period(10, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 11-month Euribor365 index
     *
     */
    public static class Euribor365_11M extends Euribor365{
        public Euribor365_11M(Handle<YieldTermStructure> h){
            super(new Period(11, TimeUnit.MONTHS), h);
        }
    }
    
    /**
     * 
     * 1-year Euribor365 index
     *
     */
    public static class Euribor365_1Y extends Euribor365{
        public Euribor365_1Y(Handle<YieldTermStructure> h){
            super(new Period(1, TimeUnit.YEARS), h);
        }
    }
	

	protected Euribor(String name, Period tenor, int settlementDays,
			DayCounter dayCounter, Handle<YieldTermStructure> h) {
		super(name, tenor, settlementDays, Target.getCalendar(), Currency
				.getInstance("EUR"), euriborConvention(tenor),
				euriborEOM(tenor), dayCounter, h);
	}

	public Euribor(Period tenor, Handle<YieldTermStructure> h) {
		this("Euribor", tenor, 2, // settlement days
				Actual360.getDayCounter(), h);
		if (tenor.units() == TimeUnit.DAYS)
			throw new IllegalArgumentException("for daily tenors (" + tenor
					+ ") dedicated DailyTenor constructor must be used");
	}

	protected static BusinessDayConvention euriborConvention(Period p) {
		switch (p.units()) {
		case DAYS:
		case WEEKS:
			return BusinessDayConvention.FOLLOWING;
		case MONTHS:
		case YEARS:
			return BusinessDayConvention.MODIFIED_FOLLOWING;
		default:
			throw new IllegalArgumentException("invalid time units");
		}
	}

	protected static boolean euriborEOM(Period p) {
		switch (p.units()) {
		case DAYS:
		case WEEKS:
			return false;
		case MONTHS:
		case YEARS:
			return true;
		default:
			throw new IllegalArgumentException("invalid time units");
		}
	}

}
