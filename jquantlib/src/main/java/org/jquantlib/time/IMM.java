/*
 Copyright (C) 2007 Richard Gomes

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

/*
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006, 2007 StatPro Italia srl
 Copyright (C) 2004, 2005, 2006 Ferdinando Ametrano
 Copyright (C) 2006 Katiuscia Manzoni

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

package org.jquantlib.time;

import org.jquantlib.Settings;
import org.jquantlib.util.Date;

/**
 * Main cycle of the International %Money Market (a.k.a. %IMM) months
 * 
 * @see http://en.wikipedia.org/wiki/International_Monetary_Market
 * 
 * @author Richard Gomes
 * @author Srinivas Hasti
 */
public class IMM {

    public static boolean isIMMdate(final Date date) {
    	return isIMMdate(date, true);
    }

    /**
     * Returns whether or not the given date is an IMM date
     * 
     * @param date
     * @param mainCycle
     * @return
     */
    public static boolean isIMMdate(final Date date, boolean mainCycle) {
        if (date.getWeekday()!=Weekday.Wednesday)
            return false;

        int d = date.getDayOfMonth();
        if (d<15 || d>21)
            return false;

        if (!mainCycle) return true;

        Date.Month m = date.getMonthEnum(); 
        return (m==Date.Month.March || m==Date.Month.June || m==Date.Month.September || m==Date.Month.December);
    }


    public static boolean isIMMcode(final String in) {
    	return isIMMcode(in, true);
    }

    /**
     * Returns whether or not the given string is an IMM code
     * 
     * @param in
     * @param mainCycle
     * @return
     */
    public static boolean isIMMcode(final String in, boolean mainCycle) {
        if (in.length() != 2)
            return false;

        if ("0123456789".indexOf(in.charAt(1))==-1)
        	return false;

        String str1;
        if (mainCycle) str1 = "hmzuHMZU";
        else           str1 = "fghjkmnquvxzFGHJKMNQUVXZ";
        
        if (str1.indexOf(in.charAt(0))==-1)
        	return false;

        return true;
    }

    public static Date date(final String immCode) {
    	return date(immCode, Date.NULL_DATE);
    }
    
	/**
	 * Returns the IMM date for the given IMM code (e.g. March 20th, 2013 for H3).
	 * 
	 * @param immCode
	 * @param refDate
	 * @return
	 */
    public static Date date(final String immCode, final Date refDate) {
        if (! (isIMMcode(immCode, false)) ) throw new IllegalArgumentException(immCode+" is not a valid IMM code");

        Date referenceDate;
        if (Date.NULL_DATE.equals(refDate)) {
        	referenceDate = Settings.getInstance().getEvaluationDate();
        } else {
        	referenceDate = refDate;
        }

        char code = immCode.charAt(0);
        Date.Month m = Date.Month.valueOf(code);
        int y = referenceDate.getYear() % 10;

        /* year<1900 are not valid QuantLib years: to avoid a run-time
           exception few lines below we need to add 10 years right away */
        if (y==0 && referenceDate.getYear()<=1909) y+=10;
        int yMod = (referenceDate.getYear() % 10);
        y += referenceDate.getYear() - yMod;
        Date result = nextDate(new Date(1, m, y), false);
        if (result.lt(referenceDate))
            return nextDate(new Date(1, m, y+10), false);

        return result;
    }

    
    public static Date nextDate() {
    	return nextDate(Date.NULL_DATE, true);
    }

    public static Date nextDate(final Date date) {
    	return nextDate(date, true);
    }

    /**
     * next IMM date following the given date
     * 
     * @param date
     * @param mainCycle
     * @return the 1st delivery date for next contract listed in the
     *   International Money Market section of the Chicago Mercantile
     *   Exchange.
     */
    public static Date nextDate(final Date date, boolean mainCycle) {
        Date refDate;
        if (Date.NULL_DATE.equals(date)) {
        	refDate = Settings.getInstance().getEvaluationDate();
        } else {
        	refDate = date;        
        }
        
        int y = refDate.getYear();
        int m = refDate.getMonth();

        int offset = mainCycle ? 3 : 1;
        int skipMonths = offset-(m%offset);
        if (skipMonths != offset || refDate.getDayOfMonth() > 21) {
            skipMonths += m;
            if (skipMonths<=12) {
                m = skipMonths;
            } else {
                m = skipMonths-12;
                y += 1;
            }
        }

        Date result = Date.getNthWeekday(3, Weekday.Wednesday, m, y);
        if (result.le(refDate))
            result = nextDate(new Date(22, m, y), mainCycle);
        return result;
    }


    
    
    public static Date nextDate(final String immCode) {
    	return nextDate(immCode, true, Date.NULL_DATE);
    }
    
    public static Date nextDate(final String immCode, boolean mainCycle) {
    	return nextDate(immCode, mainCycle, Date.NULL_DATE);
    }

    
    /**
     * Next IMM date following the given IMM code
     * 
     * @param IMMcode
     * @param mainCycle
     * @param referenceDate
     * @return the 1st delivery date for next contract listed in the
     *  International Money Market section of the Chicago Mercantile
     *  Exchange.
     */
    public static Date nextDate(final String IMMcode,
                       boolean mainCycle,
                       final Date referenceDate)  {
        Date immDate = date(IMMcode, referenceDate);
        return nextDate(immDate.inc(), mainCycle);
    }


    
    public static String nextCode() {
    	return nextCode(Date.NULL_DATE);
    }

    public static String nextCode(final Date d) {
    	return nextCode(d, true);
    }


	/**
	 * 
	 * @param d
	 * @param mainCycle
	 * @return the IMM code for next contract listed in the
     * International Money Market section of the Chicago Mercantile
     * Exchange.
	 */
    public static String nextCode(final Date d,
                              boolean mainCycle) {
        Date date = nextDate(d, mainCycle);
        return code(date);
    }


    public static String nextCode(final String immCode) {
    	return nextCode(immCode, true);
    }

    public static String nextCode(final String immCode, boolean mainCycle) {
    	return nextCode(immCode, mainCycle, Date.NULL_DATE);
    }

    
	/**
	 * 
	 * @param immCode
	 * @param mainCycle
	 * @param referenceDate
	 * @return the IMM code for next contract listed in the
     * International Money Market section of the Chicago Mercantile
     * Exchange.
	 */
    public static String nextCode(final String immCode,
                              boolean mainCycle,
                              final Date referenceDate) {
        Date date = nextDate(immCode, mainCycle, referenceDate);
        return code(date);
    }
	
	
    /**
     * Returns the IMM code for the given date (e.g. H3 for March 20th, 2013).
     * 
     * @param date
     * @return
     */
    public static String code(final Date date) {
        if (! (isIMMdate(date, false)) ) throw new IllegalArgumentException(date+" is not an IMM date");

        int y = date.getYear() % 10;
        char code = date.getMonthEnum().getImmChar();
        StringBuilder sb = new StringBuilder();
        sb.append(code).append(y);
        
        if (Settings.getInstance().isExtraSafetyChecks()) {
        	if (! isIMMcode(sb.toString()) ) throw new IllegalArgumentException("the result "+sb.toString()+" is an invalid IMM code");
        }
        return sb.toString();
    }

}
