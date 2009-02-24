/*
 Copyright (C) 2008 Richard Gomes

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

import org.jquantlib.Configuration;
import org.jquantlib.Settings;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;

/**
 * Main cycle of the International %Money Market (a.k.a. %IMM) months
 * 
 * @see <a href="http://en.wikipedia.org/wiki/International_Monetary_Market">International Monetary Market</a>
 * 
 * @author Richard Gomes
 * @author Srinivas Hasti
 */
//TODO: Improve comments
public class IMM {
    /**
     * @see <a href="http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">The "Double-Checked Locking is Broken" Declaration </a>
     */
    private static volatile IMM DEFAULT_IMM;

    private final Configuration configuration;
    private final Settings settings;
    
    /**
     * To create a IMM with user Settings and Configuration
     * 
     * @param config
     * @param settings
     */
    // TODO: code review
    public IMM(Configuration config, Settings settings){
        this.configuration = config;
        this.settings = settings;
    }
    
    /**
     * Returns IMM with Global Configuration and Settings
     * @return
     */
    public static IMM getDefaultIMM(){
        if (DEFAULT_IMM == null) {
            synchronized (IMM.class) {
                if (DEFAULT_IMM == null)
                    DEFAULT_IMM = new IMM(
                            Configuration.getSystemConfiguration(null), 
                            Configuration.getSystemConfiguration(null).getGlobalSettings());
            }
        }
        return DEFAULT_IMM;
    }

    /**
     * Checks if Date is an IMM date or not
     * 
     * @param date
     * @return
     */
    public boolean isIMMdate(final Date date) {
    	return isIMMdate(date, true);
    }

    /**
     * Returns whether or not the given date is an IMM date. For non main cycle,
     * third Wednesday in a month is returned true,
     * 
     * @param date
     * @param mainCycle
     * @return
     */
    public boolean isIMMdate(final Date date, boolean mainCycle) {
        if (date.getWeekday()!=Weekday.WEDNESDAY)
            return false;

        int d = date.getDayOfMonth();
        if (d<15 || d>21)
            return false;

        if (!mainCycle) return true;

        Month m = date.getMonthEnum(); 
        return (m==Month.MARCH || m==Month.JUNE || m==Month.SEPTEMBER || m==Month.DECEMBER);
    }


    /**
     * Checks if Strings specifies a IMM code or not
     * 
     * @param in
     * @return
     */
    public boolean isIMMcode(final String in) {
    	return isIMMcode(in, true);
    }

    /**
     * Returns whether or not the given string is an IMM code
     * 
     * @param in
     * @param mainCycle
     * @return
     */
    public boolean isIMMcode(final String in, boolean mainCycle) {
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

    /**
     * Returns Date from IMM code
     * 
     * @param immCode
     * @return
     */
    public Date date(final String immCode) {
    	return date(immCode, Date.NULL_DATE);
    }
    
	/**
	 * Returns the IMM date for the given IMM code (e.g. March 20th, 2013 for H3).
	 * When <code>Date.NULL_DATE</code> is passed, <code>Settings.getEvaluationDate</code> is used as 
	 * a reference date.
	 * 
	 * @param immCode
	 * @param refDate
	 * @return
	 */
    // FIXME: this method is potentially harmful in heavily multi-threaded environments
    public Date date(final String immCode, final Date refDate) {
        if (! (isIMMcode(immCode, false)) ) throw new IllegalArgumentException(immCode+" is not a valid IMM code");

        Date referenceDate;
        if (Date.NULL_DATE.equals(refDate)) {
        	referenceDate = settings.getEvaluationDate();
        } else {
        	referenceDate = refDate;
        }

        char code = immCode.charAt(0);
        Month m = Month.valueOf(code);
        
        int y = immCode.charAt(1) - '0';

        /* year<1900 are not valid QuantLib years: to avoid a run-time
           exception few lines below we need to add 10 years right away */
        if (y==0 && referenceDate.getYear()<=1909) y+=10;
        int yMod = (referenceDate.getYear() % 10);
        y += referenceDate.getYear() - yMod;
        Date result = nextDate(DateFactory.getFactory().getDate(1, m, y), false);
        if (result.lt(referenceDate))
            return nextDate(DateFactory.getFactory().getDate(1, m, y+10), false);

        return result;
    }

    /**
     * Returns next main cycle IMM Date using Settings.getEvaluationDate as reference date.
     * 
     * @return
     */
    public Date nextDate() {
    	return nextDate(Date.NULL_DATE, true);
    }

    /**
     *  Returns next main cycle IMM Date from the specified date.
     *  
     *  
     * @param date
     * @return
     */
    public Date nextDate(final Date date) {
    	return nextDate(date, true);
    }

    /**
     * next IMM date following the given date. * When <code>Date.NULL_DATE</code> is passed, <code>Settings.getEvaluationDate</code> is used as 
     * a reference date.
     * 
     * @param date
     * @param mainCycle
     * @return the 1st delivery date for next contract listed in the
     *   International Money Market section of the Chicago Mercantile
     *   Exchange.
     */
    public Date nextDate(final Date date, boolean mainCycle) {
        Date refDate;
        if (Date.NULL_DATE.equals(date)) {
        	refDate = settings.getEvaluationDate();
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

        Date result = DateFactory.getFactory().getNthWeekday(3, Weekday.WEDNESDAY, Month.valueOf(m), y);
        if (result.le(refDate))
            result = nextDate(DateFactory.getFactory().getDate(22, Month.valueOf(m), y), mainCycle);
        return result;
    }


    
    /**
     * Returns next main cycle IMM date from the given IMM code
     * 
     * @param immCode
     * @return
     */
    public Date nextDate(final String immCode) {
    	return nextDate(immCode, true, Date.NULL_DATE);
    }
    
    
    /**
     * Returns next IMM date from the given IMM code
     * 
     * @param immCode
     * @param mainCycle
     * @return
     */
    public Date nextDate(final String immCode, boolean mainCycle) {
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
    public Date nextDate(final String IMMcode,
                       boolean mainCycle,
                       final Date referenceDate)  {
        Date immDate = date(IMMcode, referenceDate);
        return nextDate(immDate.increment(), mainCycle);
    }


    /**
     * Returns next main cycle IMM code from the reference date
     * 
     * @return
     */
    public String nextCode() {
    	return nextCode(Date.NULL_DATE);
    }

    /**
     * Returns next cycle IMM code from the specified date
     * @param d
     * @return
     */
    public String nextCode(final Date d) {
    	return nextCode(d, true);
    }


	/**
	 * Returns next cycle IMM code
	 * 
	 * @param d
	 * @param mainCycle
	 * @return the IMM code for next contract listed in the
     * International Money Market section of the Chicago Mercantile
     * Exchange.
	 */
    public String nextCode(final Date d,
                              boolean mainCycle) {
        Date date = nextDate(d, mainCycle);
        return code(date);
    }


    /**
     * Returns next main cycle IMM code from the given IMM Code.
     * 
     * @param immCode
     * @return
     */
    public String nextCode(final String immCode) {
    	return nextCode(immCode, true);
    }

    /**
     * Returns next IMM code from the given IMM code
     * 
     * @param immCode
     * @param mainCycle
     * @return
     */
    public String nextCode(final String immCode, boolean mainCycle) {
    	return nextCode(immCode, mainCycle, Date.NULL_DATE);
    }

    
	/**
	 * Return next IMM code from the given IMM code and reference date
	 *  
	 * @param immCode
	 * @param mainCycle
	 * @param referenceDate
	 * @return the IMM code for next contract listed in the
     * International Money Market section of the Chicago Mercantile
     * Exchange.
	 */
    public String nextCode(final String immCode,
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
    // FIXME: this method is potentially harmful in heavily multi-threaded environments
    public  String code(final Date date) {
        if (! (isIMMdate(date, false)) ) throw new IllegalArgumentException(date+" is not an IMM date");

        int y = date.getYear() % 10;
        char code = date.getMonthEnum().getImmChar();
        StringBuilder sb = new StringBuilder();
        sb.append(code).append(y);
        
        final String imm = sb.toString(); 
        
        // TODO: review usage of QL_EXTRA_SAFETY_CHECKS
        if (configuration.isExtraSafetyChecks()) {
            if (! isIMMcode(imm, false) ) 
                throw new RuntimeException("the result is an invalid IMM code");
        }
        return imm;
    }

}
