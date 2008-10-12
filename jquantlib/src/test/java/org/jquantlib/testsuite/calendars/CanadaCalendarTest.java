/*
 Copyright (C) 2008 Jia Jia

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

package org.jquantlib.testsuite.calendars;


import static org.jquantlib.util.Month.*;

import java.util.List;
import java.util.Vector;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Canada;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jia Jia
 *
 */

public class CanadaCalendarTest {
    private Calendar settlement;
    private Calendar tsx;
    private List<Date> expectedHol;
    
    @Before
    public void setUp() throws Exception {
        settlement = Canada.getCalendar(Canada.Market.SETTLEMENT);
        tsx = Canada.getCalendar(Canada.Market.TSX);
        expectedHol = new Vector<Date>();
    }
    
    @Test
    public void testCanadaSettlementYear2004() {
        int year = 2004;
        System.out.println("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }

    @Test
    public void testCanadaSettlementYear2005() {
        int year = 2005;
        System.out.println("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(23,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testCanadaSettlementYear2006() {
        int year = 2006;
        System.out.println("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(22,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(3,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(4,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(13,NOVEMBER,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testCanadaSettlementYear2007() {
        int year = 2007;
        System.out.println("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(3,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,NOVEMBER,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testCanadaSettlementYear2008() {
        int year = 2008;
        System.out.println("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(18,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(4,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(13,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testCanadaSettlementYear2009() {
        int year = 2009;
        System.out.println("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(16,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(18,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(3,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testCanadaSettlementYear2010() {
        int year = 2010;
        System.out.println("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(15,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
    
    @Test
    public void testCanadaSettlementYear2011() {
        int year = 2011;
        System.out.println("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(21,FEBRUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(23,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,NOVEMBER,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }    
    
    @Test
    public void testCanadaSettlementYear2012() {
        int year = 2012;
        System.out.println("Testing " + Canada.Market.SETTLEMENT + " holiday list for the year " + year + "...");
        
        expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(20,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(3,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,NOVEMBER,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, settlement, year);
    }
 
    @Test
    public void testCanadaTSXYear2004() {
        int year = 2004;
        System.out.println("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, tsx, year);

    }
    
    @Test
    public void testCanadaTSXYear2005() {
        int year = 2005;
        System.out.println("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");
 
        expectedHol.add(DateFactory.getFactory().getDate(25,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(23,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, tsx, year);

    }

    @Test
    public void testCanadaTSXYear2006() {
        int year = 2006;
        System.out.println("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(14,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(17,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(22,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(3,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(4,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, tsx, year);

    }

    @Test
    public void testCanadaTSXYear2007() {
        int year = 2007;
        System.out.println("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(3,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, tsx, year);

    }

    @Test
    public void testCanadaTSXYear2008() {
        int year = 2008;
        System.out.println("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(18,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,MARCH,year));
        expectedHol.add(DateFactory.getFactory().getDate(19,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(4,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(13,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, tsx, year);

    }

    @Test
    public void testCanadaTSXYear2009() {
        int year = 2009;
        System.out.println("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(16,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(13,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(18,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(3,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(7,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(12,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, tsx, year);

    }

    @Test
    public void testCanadaTSXYear2010() {
        int year = 2010;
        System.out.println("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(1,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(15,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(24,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(11,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(28,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, tsx, year);

    }

    @Test
    public void testCanadaTSXYear2011() {
        int year = 2011;
        System.out.println("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(21,FEBRUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(22,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(23,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(1,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(5,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(10,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(27,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, tsx, year);

    }

    @Test
    public void testCanadaTSXYear2012() {
        int year = 2012;
        System.out.println("Testing " + Canada.Market.TSX + " holiday list for the year " + year + "...");

        expectedHol.add(DateFactory.getFactory().getDate(2,JANUARY,year)); 
        expectedHol.add(DateFactory.getFactory().getDate(20,FEBRUARY,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(9,APRIL,year));
        expectedHol.add(DateFactory.getFactory().getDate(21,MAY,year));
        expectedHol.add(DateFactory.getFactory().getDate(2,JULY,year));
        expectedHol.add(DateFactory.getFactory().getDate(6,AUGUST,year));
        expectedHol.add(DateFactory.getFactory().getDate(3,SEPTEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(8,OCTOBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(25,DECEMBER,year));
        expectedHol.add(DateFactory.getFactory().getDate(26,DECEMBER,year));
        
        // Call the Holiday Check
        CalendarUtil cbt = new CalendarUtil();
        cbt.checkHolidayList(expectedHol, tsx, year);

    }

}
