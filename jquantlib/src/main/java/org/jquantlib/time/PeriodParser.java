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

package org.jquantlib.time;

import org.jquantlib.QL;

/**
 * To convert short and long format string representations
 * to period object.
 *
 * @author Srinivas Hasti
 *
 */
// TODO: code review :: please verify against QL/C++ code
public final class PeriodParser {

    /**
     * To convert the string to Period.
     *
     * @param str
     * @return period derived from str
     */
    public static Period parse(final String str) {
        TimeUnit units = null;
        int index = -1;
        if ((index = str.indexOf('d')) > 0 || (index = str.indexOf('D')) > 0) {
            units = TimeUnit.DAYS;
            enforceUnit(index, str, units);
        } else if ((index = str.indexOf('w')) > 0 || (index = str.indexOf('W')) > 0) {
            units = TimeUnit.WEEKS;
            enforceUnit(index, str, units);
        } else if ((index = str.indexOf('m')) > 0 || (index = str.indexOf('M')) > 0) {
            units = TimeUnit.MONTHS;
            enforceUnit(index, str, units);
        } else if ((index = str.indexOf('y')) > 0 || (index = str.indexOf('Y')) > 0) {
            units = TimeUnit.YEARS;
            enforceUnit(index, str, units);
        }

        final String length = str.substring(0, index).trim();
        return new Period(Integer.parseInt(length), units);
    }

    private static void enforceUnit(final int index, final String str, final TimeUnit units) {
        final String unitStr = str.substring(index);
        //If this is the last char in string, assume it is represented
        //in short format.
        if (unitStr.length()==0) return;

        //Now enforce my unit
        QL.require(unitStr.equalsIgnoreCase(units.toString()) , "unable to convert to period");
    }

}
