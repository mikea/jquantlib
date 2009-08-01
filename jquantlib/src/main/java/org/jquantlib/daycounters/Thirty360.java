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

package org.jquantlib.daycounters;

import org.jquantlib.util.Date;

/**
 * 30/360 day count convention
 * <p>
 * The 30/360 day count can be calculated according to US, European, or Italian
 * conventions.
 * <p>
 * US (NASD) convention: if the starting date is the 31st of a month, it becomes
 * equal to the 30th of the same month. If the ending date is the 31st of a
 * month and the starting date is earlier than the 30th of a month, the ending
 * date becomes equal to the 1st of the next month, otherwise the ending date
 * becomes equal to the 30th of the same month. Also known as "30/360",
 * "360/360", or "Bond Basis"
 * <p>
 * European convention: starting dates or ending dates that occur on the 31st of
 * a month become equal to the 30th of the same month. Also known as "30E/360",
 * or "Eurobond Basis"
 * <p>
 * Italian convention: starting dates or ending dates that occur on February and
 * are grater than 27 become equal to 30 for computational sake.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Day_count_convention">Day count convention</a>
 * 
 * @author Srinivas Hasti
 * @author Richard Gomes
 */
public class Thirty360 extends AbstractDayCounter {

    //
    // public enums
    //

    /**
     * 30/360 Calendar Conventions
     */
    public enum Convention {
        USA, BOND_BASIS, EUROPEAN, EURO_BOND_BASIS, ITALIAN;
    }


    //
    // private final static fields
    //

    private static final Thirty360 THIRTY360_US = new Thirty360(Thirty360.Convention.USA);
    private static final Thirty360 THIRTY360_EU = new Thirty360(Thirty360.Convention.EUROPEAN);
    private static final Thirty360 THIRTY360_IT = new Thirty360(Thirty360.Convention.ITALIAN);


    //
    // public static final constructors
    //

    public static final Thirty360 getDayCounter() {
        return getDayCounter(Thirty360.Convention.BOND_BASIS);

    }

    public static final Thirty360 getDayCounter(final Thirty360.Convention c) {
        switch (c) {
        case USA:
        case BOND_BASIS:
            return THIRTY360_US;
        case EUROPEAN:
        case EURO_BOND_BASIS:
            return THIRTY360_EU;
        case ITALIAN:
            return THIRTY360_IT;
        default:
            throw new AssertionError(); //TODO: message
        }
    }


    //
    // private final fields
    //

    private final Thirty360Abstraction delegate;


    //
    // private constructors
    //

    private Thirty360(final Thirty360.Convention c) {
        super();

        switch (c) {
        case USA:
        case BOND_BASIS:
            delegate = new US();
            break;
        case EUROPEAN:
        case EURO_BOND_BASIS:
            delegate = new EU();
            break;
        case ITALIAN:
            delegate = new IT();
            break;
        default:
            throw new AssertionError(); // TODO: message
        }
    }


    //
    // public final methods
    //

    public final int compute(final int dd1, final int dd2, final int mm1, final int mm2, final int yy1, final int yy2){
        return 360 * (yy2 - yy1) + 30 * (mm2 - mm1 - 1) + Math.max(0, 30 - dd1) + Math.min(30, dd2);
    }


    //
    // implements DayCounter
    //

    @Override
    public final String name() /* @ReadOnly */{
        return delegate.getName();
    }

    @Override
    public final int dayCount(final Date d1, final Date d2) /* @ReadOnly */{
        final int dd1 = d1.getDayOfMonth();
        final int dd2 = d2.getDayOfMonth();
        final int mm1 = d1.getMonth();
        final int mm2 = d2.getMonth();
        final int yy1 = d1.getYear();
        final int yy2 = d2.getYear();
        return delegate.getDayCount(dd1, dd2, mm1, mm2, yy1, yy2);

    }

    @Override
    public final /* @Time */ double yearFraction(
            final Date dateStart, final Date dateEnd,
            final Date refPeriodStart, final Date refPeriodEnd) /* @ReadOnly */{
        return dayCount(dateStart, dateEnd) / 360.0;
    }

    @Override
    public final /* @Time */double yearFraction(final Date dateStart, final Date dateEnd) /* @ReadOnly */{
        return dayCount(dateStart, dateEnd) / 360.0;
    }


    //
    // private inner classes
    //

    /**
     * Abstraction of Thirty360 class
     * 
     * @see <a href="http://en.wikipedia.org/wiki/Bridge_pattern">Bridge pattern</a>
     * 
     * @author Richard Gomes
     */
    private interface Thirty360Abstraction {
        public String getName();
        public int getDayCount(int dd1, int dd2, int mm1, int mm2, int yy1, int yy2);
    }

    /**
     * Implementation of Thirty360 class abstraction according to US convention
     * 
     * @see <a href="http://en.wikipedia.org/wiki/Bridge_pattern">Bridge pattern</a>
     * 
     * @author Richard Gomes
     */
    private final class US implements Thirty360Abstraction {

        @Override
        public final String getName() /* @ReadOnly */{
            return "30/360 (Bond Basis)";
        }

        @Override
        public final int getDayCount(final int dd1, int dd2, final int mm1, int mm2, final int yy1, final int yy2){
            if (dd2 == 31 && dd1 < 30) {
                dd2 = 1;
                mm2++;
            }
            return compute(dd1,dd2,mm1,mm2,yy1,yy2);
        }
    }

    /**
     * Implementation of Thirty360 class abstraction according to European convention
     * 
     * @see <a href="http://en.wikipedia.org/wiki/Bridge_pattern">Bridge pattern</a>
     * 
     * @author Richard Gomes
     */
    private final class EU implements Thirty360Abstraction {

        @Override
        public final String getName() /* @ReadOnly */{
            return "30E/360 (Eurobond Basis)";
        }

        @Override
        public final int getDayCount(final int dd1, final int dd2, final int mm1, final int mm2, final int yy1, final int yy2){
            return compute(dd1,dd2,mm1,mm2,yy1,yy2);
        }
    }

    /**
     * Implementation of Thirty360 class abstraction according to Italian convention
     * 
     * @see <a href="http://en.wikipedia.org/wiki/Bridge_pattern">Bridge pattern</a>
     * 
     * @author Richard Gomes
     */
    private final class IT implements Thirty360Abstraction {

        @Override
        public final String getName() /* @ReadOnly */{
            return "30/360 (Italian)";
        }

        @Override
        public final int getDayCount(int dd1, int dd2, final int mm1, final int mm2, final int yy1, final int yy2) {
            if (mm1 == 2 && dd1 > 27)
                dd1 = 30;
            if (mm2 == 2 && dd2 > 27)
                dd2 = 30;
            return compute(dd1,dd2,mm1,mm2,yy1,yy2);
        }
    }

}
