/*
 Copyright (C) 2008 Srinivas Hasti
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.util;


/**
 * Helper class to parse Strings to Date
 * 
 * @author Srinivas Hasti
 * 
 */
public class DateParser {

    /**
     * Convert ISO format strings to Date. Ex: 2008-03-31
     * 
     * @param str
     * @return Date
     */
    public static Date parseISO(String str) {
        if (str.length() == 10 && str.charAt(4) == '-' && str.charAt(7) == '-') {
            int year = Integer.parseInt(str.substring(0, 4));
            int month = Integer.parseInt(str.substring(5, 7));
            int day = Integer.parseInt(str.substring(8, 10));
            return DateFactory.getFactory().getDate(day, Month.valueOf(month), year);
        } else {
        	throw new IllegalArgumentException("Invalid format " + str);	
        }
    }

    /**
     * Convert the String with separator '/' to Date using the format specified.
     * 
     * For example: "2008/03/31", "yyyy/MM/dd"
     * 
     * @param str
     * @param fmt
     * @return Date
     */
    public static Date parse(String str, String fmt) {
        String[] slist = null;
        String[] flist = null;
        int d = 0, m = 0, y = 0;

        slist = str.split(str, '/');
        flist = str.split(fmt, '/');

        if (slist.length != flist.length)
            throw new IllegalArgumentException("String " + str
                    + " didn't match with reference format " + fmt);

        for (int i = 0; i < flist.length; i++) {
            String sub = flist[i];
            if (sub.equalsIgnoreCase("dd"))
                d = Integer.parseInt(slist[i]);
            else if (sub.equalsIgnoreCase("mm"))
                m = Integer.parseInt(slist[i]);
            else if (sub.equalsIgnoreCase("yyyy")) {
                y = Integer.parseInt(slist[i]);
                if (y < 100)
                    y += 2000;
            }
        }
        return DateFactory.getFactory().getDate(d, Month.valueOf(m), y);
    }
}
