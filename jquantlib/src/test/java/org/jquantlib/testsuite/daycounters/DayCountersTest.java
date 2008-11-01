/*
 Copyright (C) 2007 Richard Gomes

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
 Copyright (C) 2003 RiskMap srl
 Copyright (C) 2006 Piter Dias

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
*/

package org.jquantlib.testsuite.daycounters;

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.jquantlib.daycounters.ActualActual;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.daycounters.SimpleDayCounter;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;
import org.junit.Test;

/**
 * Test Day Counters
 * 
 * @author Richard Gomes
 */
public class DayCountersTest {

    private final static Logger logger = Logger.getLogger(DayCountersTest.class);

	public DayCountersTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	private class SingleCase {
	    private ActualActual.Convention convention;
	    private Date start;
	    private Date end;
	    private Date refStart;
	    private Date refEnd;
	    private /*@Time*/ double  result;

	    public SingleCase(
    			final ActualActual.Convention convention,
    			final Date start,
    			final Date end,
                final /*@Time*/ double result) {
	    	this(convention, start, end, Date.NULL_DATE, Date.NULL_DATE, result);
	    }

	    public SingleCase(
	    			final ActualActual.Convention convention,
	    			final Date start,
	    			final Date end,
	    			final Date refStart,
	    			final Date refEnd,
	                final /*@Time*/ double result) {
	    	this.convention = convention;
	    	this.start = start;
	    	this.end = end;
	    	this.refStart = refStart;
	    	this.refEnd = refEnd;
	    	this.result = result;
	    }
	    
	    private String dumpDate(final Date date) {
	    	if (date == null) {
	    		return "null";
	    	} else {
	    		return date.getISOFormat();
	    	}
	    }
	    
	    
	    @Override
	    public String toString() {
	    	StringBuilder sb = new StringBuilder();
	    	sb.append("[ ");
	    	sb.append(convention).append(", ");
	    	sb.append(dumpDate(start)).append(", ");
	    	sb.append(dumpDate(end)).append(", ");
	    	sb.append(dumpDate(refStart)).append(", ");
	    	sb.append(dumpDate(refEnd));
	    	sb.append(" ]");
	    	return sb.toString();
	    }
	}
	
	
	@Test
	public void testActualActual() {

	    logger.info("Testing actual/actual day counters...");

	    SingleCase testCases[] = new SingleCase[] {
	        // first example
	        new SingleCase(ActualActual.Convention.ISDA,
	                   DateFactory.getFactory().getDate(1,Month.NOVEMBER,2003), DateFactory.getFactory().getDate(1,Month.MAY,2004), 
	                   0.497724380567),
	        new SingleCase(ActualActual.Convention.ISMA,
	                   DateFactory.getFactory().getDate(1,Month.NOVEMBER,2003), DateFactory.getFactory().getDate(1,Month.MAY,2004),
	                   DateFactory.getFactory().getDate(1,Month.NOVEMBER,2003), DateFactory.getFactory().getDate(1,Month.MAY,2004),
	                   0.500000000000),
	        new SingleCase(ActualActual.Convention.AFB,
	                   DateFactory.getFactory().getDate(1,Month.NOVEMBER,2003), DateFactory.getFactory().getDate(1,Month.MAY,2004),
	                   0.497267759563),
	        // short first calculation period (first period)
	        new SingleCase(ActualActual.Convention.ISDA,
	                   DateFactory.getFactory().getDate(1,Month.FEBRUARY,1999), DateFactory.getFactory().getDate(1,Month.JULY,1999),
	                   0.410958904110),
	        new SingleCase(ActualActual.Convention.ISMA,
	                   DateFactory.getFactory().getDate(1,Month.FEBRUARY,1999), DateFactory.getFactory().getDate(1,Month.JULY,1999),
	                   DateFactory.getFactory().getDate(1,Month.JULY,1998), DateFactory.getFactory().getDate(1,Month.JULY,1999),
	                   0.410958904110),
	        new SingleCase(ActualActual.Convention.AFB,
	                   DateFactory.getFactory().getDate(1,Month.FEBRUARY,1999), DateFactory.getFactory().getDate(1,Month.JULY,1999),
	                   0.410958904110),
	        // short first calculation period (second period)
	        new SingleCase(ActualActual.Convention.ISDA,
	                   DateFactory.getFactory().getDate(1,Month.JULY,1999), DateFactory.getFactory().getDate(1,Month.JULY,2000),
	                   1.001377348600),
	        new SingleCase(ActualActual.Convention.ISMA,
	                   DateFactory.getFactory().getDate(1,Month.JULY,1999), DateFactory.getFactory().getDate(1,Month.JULY,2000),
	                   DateFactory.getFactory().getDate(1,Month.JULY,1999), DateFactory.getFactory().getDate(1,Month.JULY,2000),
	                   1.000000000000),
	        new SingleCase(ActualActual.Convention.AFB,
	                   DateFactory.getFactory().getDate(1,Month.JULY,1999), DateFactory.getFactory().getDate(1,Month.JULY,2000),
	                   1.000000000000),
	        // long first calculation period (first period)
	        new SingleCase(ActualActual.Convention.ISDA,
	                   DateFactory.getFactory().getDate(15,Month.AUGUST,2002), DateFactory.getFactory().getDate(15,Month.JULY,2003),
	                   0.915068493151),
	        new SingleCase(ActualActual.Convention.ISMA,
	                   DateFactory.getFactory().getDate(15,Month.AUGUST,2002), DateFactory.getFactory().getDate(15,Month.JULY,2003),
	                   DateFactory.getFactory().getDate(15,Month.JANUARY,2003), DateFactory.getFactory().getDate(15,Month.JULY,2003),
	                   0.915760869565),
	        new SingleCase(ActualActual.Convention.AFB,
	                   DateFactory.getFactory().getDate(15,Month.AUGUST,2002), DateFactory.getFactory().getDate(15,Month.JULY,2003),
	                   0.915068493151),
	        // long first calculation period (second period)
	        /* Warning: the ISDA case is in disagreement with mktc1198.pdf */
	        new SingleCase(ActualActual.Convention.ISDA,
	                   DateFactory.getFactory().getDate(15,Month.JULY,2003), DateFactory.getFactory().getDate(15,Month.JANUARY,2004),
	                   0.504004790778),
	        new SingleCase(ActualActual.Convention.ISMA,
	                   DateFactory.getFactory().getDate(15,Month.JULY,2003), DateFactory.getFactory().getDate(15,Month.JANUARY,2004),
	                   DateFactory.getFactory().getDate(15,Month.JULY,2003), DateFactory.getFactory().getDate(15,Month.JANUARY,2004),
	                   0.500000000000),
	        new SingleCase(ActualActual.Convention.AFB,
	                   DateFactory.getFactory().getDate(15,Month.JULY,2003), DateFactory.getFactory().getDate(15,Month.JANUARY,2004),
	                   0.504109589041),
	        // short final calculation period (penultimate period)
	        new SingleCase(ActualActual.Convention.ISDA,
	                   DateFactory.getFactory().getDate(30,Month.JULY,1999), DateFactory.getFactory().getDate(30,Month.JANUARY,2000),
	                   0.503892506924),
	        new SingleCase(ActualActual.Convention.ISMA,
	                   DateFactory.getFactory().getDate(30,Month.JULY,1999), DateFactory.getFactory().getDate(30,Month.JANUARY,2000),
	                   DateFactory.getFactory().getDate(30,Month.JULY,1999), DateFactory.getFactory().getDate(30,Month.JANUARY,2000),
	                   0.500000000000),
	        new SingleCase(ActualActual.Convention.AFB,
	                   DateFactory.getFactory().getDate(30,Month.JULY,1999), DateFactory.getFactory().getDate(30,Month.JANUARY,2000),
	                   0.504109589041),
	        // short final calculation period (final period)
	        new SingleCase(ActualActual.Convention.ISDA,
	                   DateFactory.getFactory().getDate(30,Month.JANUARY,2000), DateFactory.getFactory().getDate(30,Month.JUNE,2000),
	                   0.415300546448),
	        new SingleCase(ActualActual.Convention.ISMA,
	                   DateFactory.getFactory().getDate(30,Month.JANUARY,2000), DateFactory.getFactory().getDate(30,Month.JUNE,2000),
	                   DateFactory.getFactory().getDate(30,Month.JANUARY,2000), DateFactory.getFactory().getDate(30,Month.JULY,2000),
	                   0.417582417582),
	        new SingleCase(ActualActual.Convention.AFB,
	                   DateFactory.getFactory().getDate(30,Month.JANUARY,2000), DateFactory.getFactory().getDate(30,Month.JUNE,2000),
	                   0.41530054644)
	    };

	    for (int i=0; i<testCases.length-1; i++) {
	        ActualActual dayCounter =  ActualActual.getDayCounter(testCases[i].convention);
	        Date d1 = testCases[i].start;
	        Date d2 = testCases[i].end;
	        Date rd1 = testCases[i].refStart;
	        Date rd2 = testCases[i].refEnd;
	        
	        logger.info(testCases[i].toString());
	        
	        /*@Time*/ double  calculated = dayCounter.getYearFraction(d1, d2, rd1, rd2);

	        if (Math.abs(calculated-testCases[i].result) > 1.0e-10) {
	        	String period = "period: " + d1 + " to " + d2;
	        	String refPeriod = "";
	            if (testCases[i].convention == ActualActual.Convention.ISMA) {
	                refPeriod = "referencePeriod: " + rd1 + " to " + rd2;
	            }
	            fail(dayCounter.getName() + ":\n"
	                       + period + "\n" 
	                       + refPeriod + "\n"
	                       + "    calculated: " + calculated + "\n"
	                       + "    expected:   " + testCases[i].result);
	        }
	    }
	}


	@Test
	public void testSimple() {

	    logger.info("Testing simple day counter...");

	    Period p[] = new Period[] { new Period(3, TimeUnit.MONTHS), new Period(6, TimeUnit.MONTHS), new Period(1, TimeUnit.YEARS) };
	    /*@Time*/ double expected[] = { 0.25, 0.5, 1.0 };
	    
	    // 4 years should be enough
	    Date first = DateFactory.getFactory().getDate(1,Month.JANUARY,2002);
	    Date last  = DateFactory.getFactory().getDate(31,Month.DECEMBER,2005);
	    DayCounter dayCounter = new SimpleDayCounter();

	    for (Date start = first; start.le(last); start.increment()) {
	        for (int i=0; i<expected.length-1; i++) {
	            Date end = start.getDateAfter(p[i]);
	            /*@Time*/ double  calculated = dayCounter.getYearFraction(start, end);

	        	if (Math.abs(calculated-expected[i]) > 1.0e-12)
	                fail("from " + start + " to " + end + ":\n"
	                        + "    calculated: " + calculated + "\n"
	                        + "    expected:   " + expected[i]);
	        }
	    }
	}

	@Test
	public void testOne() {

	    logger.info("Testing 1/1 day counter...");

	    Period p[] = new Period[]{ new Period(3, TimeUnit.MONTHS), new Period(6, TimeUnit.MONTHS), new Period(1, TimeUnit.YEARS) };
	    /*@Time*/ double expected[] = new double[] { 1.0, 1.0, 1.0 };

	    // 1 years should be enough
	    Date first = DateFactory.getFactory().getDate(1,Month.JANUARY,2004);
	    Date last  = DateFactory.getFactory().getDate(31,Month.DECEMBER,2004);
	    DayCounter dayCounter = new SimpleDayCounter();

	    for (Date start = first; start.le(last); start.increment()) {
	        for (int i=0; i<expected.length-1; i++) {
	            Date end = start.getDateAfter(p[i]);
	            /*@Time*/ double  calculated = dayCounter.getYearFraction(start, end);

	            if (Math.abs(calculated-expected[i]) <= 1.0e-12) {
	                fail("from " + start + " to " + end + ":\n"
	                           + "    calculated: " + calculated + "\n"
	                           + "    expected:   " + expected[i]);
	            }
	        }
	    }
	}

	
// TODO: Test
//	public void testBusiness252() {
//
//	    logger.info("Testing business/252 day counter...");
//
//	    Date testDates[] = {
//	    DateUtil.getDateUtil().getDate(1,Month.FEBRUARY,2002),
//	    DateUtil.getDateUtil().getDate(4,Month.FEBRUARY,2002),
//	    DateUtil.getDateUtil().getDate(16,Month.MAY,2003),
//	    DateUtil.getDateUtil().getDate(17,Month.DECEMBER,2003),
//	    DateUtil.getDateUtil().getDate(17,Month.DECEMBER,2004),
//	    DateUtil.getDateUtil().getDate(19,Month.DECEMBER,2005),
//	    DateUtil.getDateUtil().getDate(2,Month.JANUARY,2006),
//	    DateUtil.getDateUtil().getDate(13,Month.MARCH,2006),
//	    DateUtil.getDateUtil().getDate(15,Month.MAY,2006),
//	    DateUtil.getDateUtil().getDate(17,Month.MARCH,2006),
//	    DateUtil.getDateUtil().getDate(15,Month.MAY,2006),
//	    DateUtil.getDateUtil().getDate(26,Month.JULY,2006) };
//
//	    /*@Time*/ double expected[] = {
//	        0.0039682539683,
//	        1.2738095238095,
//	        0.6031746031746,
//	        0.9960317460317,
//	        1.0000000000000,
//	        0.0396825396825,
//	        0.1904761904762,
//	        0.1666666666667,
//	        -0.1507936507937,
//	        0.1507936507937,
//	        0.2023809523810
//	        };
//
//	    DayCounter dayCounter = Business252(Brazil.getCalendar(Brazil.Market.SETTLEMENT));
//
//	    for (int i=1; i<testDates.length-1; i++) {
//	    	Date start = testDates[i-1];
//	    	Date end = testDates[i];
//	    	/*@Time*/ double  calculated = dayCounter.getYearFraction(start, end);
//	        
//	        StringBuilder sb = new StringBuilder();
//        	sb.append(dayCounter.getClass().getName()).append('\n');
//        	sb.append("  from ").append(start).append(" to ").append(end).append('\n');
//        	sb.append("    calculated: ").append(calculated).append('\n');
//        	sb.append("    expected:   ").append(expected[i]);
//            
//        	fail(sb.toString(), Math.abs(calculated-expected[i]) <= 1.0e-12);
//	    }
//	}

}
