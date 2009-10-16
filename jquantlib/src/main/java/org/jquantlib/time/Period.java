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
 Copyright (C) 2004, 2005, 2006 Ferdinando Ametrano
 Copyright (C) 2006 Katiuscia Manzoni
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006, 2007 StatPro Italia srl

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

import java.util.Formatter;
import java.util.Locale;

import org.jquantlib.QL;
import org.jquantlib.lang.exceptions.LibraryException;

/**
 * Time period to represent time by days, month and years as specified by
 * TimeUnit.
 *
 * @author Srinivas Hasti
 * @author Richard Gomes
 */
public class Period {

    private static final String UNKNOWN_UNITS = "unknown units";
    private static final String UNKNOWN_FREQUENCY = "unknown frequency";
    private static final String UNKNOWN_TIME_UNIT = "unknown time unit";
    private static final String CANNOT_INSTANTIATE = "cannot instantiate";
    private static final String UNDECIDABLE_COMPARISON = "undecidable comparison";



    /**
     * Constant that can be used to represent one year period forward
     */
    public static final Period ONE_YEAR_FORWARD = new Period(1, TimeUnit.Years);

    /**
     * Constant that can be used to represent one year period in the past
     */
    public static final Period ONE_YEAR_BACKWARD = new Period(-1, TimeUnit.Years);

    /**
     * Constant that can be used to represent one year period forward
     */
    public static final Period ONE_MONTH_FORWARD = new Period(1, TimeUnit.Months);

    /**
     * Constant that can be used to represent one year period in the past
     */
    public static final Period ONE_MONTH_BACKWARD = new Period(-1, TimeUnit.Months);

    /**
     * Constant that can be used to represent one year period forward
     */
    public static final Period ONE_DAY_FORWARD = new Period(1, TimeUnit.Days);

    /**
     * Constant that can be used to represent one year period in the past
     */
    public static final Period ONE_DAY_BACKWARD = new Period(-1, TimeUnit.Days);


    /**
     * Length of the period
     */
    private int length;

    /**
     * Units representing the period
     */
    private TimeUnit units;

    /**
     * Default constructor. Defaults to period of 0 days
     */
    public Period() {
        this.length = 0;
        this.units = TimeUnit.Days;
    }

    /**
     * To construct period representing the specified length and units
     *
     * @param length
     * @param units
     */
    public Period(final int length, final TimeUnit units) {
        this.length = length;
        this.units = units;
    }

    /**
     * To create a period by Frequency
     *
     * @param f
     */
    public Period(final Frequency f) {
        switch (f) {
        case Once:
        case NoFrequency:
            // same as Period()
            units = TimeUnit.Days;
            length = 0;
            break;
        case Annual:
            units = TimeUnit.Years;
            length = 1;
            break;
        case Semiannual:
        case EveryFourthMonth:
        case Quarterly:
        case Bimonthly:
        case Monthly:
            units = TimeUnit.Months;
            length = 12 / f.toInteger();
            break;
        case EveryFourthWeek:
        case Biweekly:
        case Weekly:
            units = TimeUnit.Weeks;
            length = 52 / f.toInteger();
            break;
        case Daily:
            units = TimeUnit.Days;
            length = 1; // FIXME: review
            break;
        default:
            throw new LibraryException(UNKNOWN_FREQUENCY); // QA:[RG]::verified
        }
    }

    /**
     * @return length of the period
     */
    public final int length() {
        return this.length;
    }

    /**
     * Time units represented by the period
     *
     * @return time units of the period
     */
    public final TimeUnit units() {
        return this.units;
    }

    /**
     * To get at Frequency represented by the period
     *
     * @return
     */
    public final Frequency frequency() {
        // unsigned version
        final int length = Math.abs(this.length);

        if (length == 0) {
            return Frequency.NoFrequency;
        }

        switch (units) {
        case Years:
            if (length == 1) {
                return Frequency.Annual;
            } else {
                return Frequency.OtherFrequency;
            }
          case Months:
            if (12%length == 0 && length <= 12) {
                return Frequency.valueOf(12 / length);
            } else {
                return Frequency.OtherFrequency;
            }
          case Weeks:
            if (length==1) {
                return Frequency.Weekly;
            } else if (length==2) {
                return Frequency.Biweekly;
            } else if (length==4) {
                return Frequency.EveryFourthWeek;
            } else {
                return Frequency.OtherFrequency;
            }
          case Days:
            if (length==1) {
                return Frequency.Daily;
            } else {
                return Frequency.OtherFrequency;
            }
        default:
            throw new LibraryException(UNKNOWN_TIME_UNIT); // QA:[RG]::verified
        }
    }

    /**
     * @return a new <code>Period</code> with opposite signal
     */
    public Period minus() {
        return new Period(-this.length, this.units);
    }

    /**
     *
     * @param n
     *            is the multiplier
     * @return a new <code>Period</code> with a multiplied length
     */
    public Period times(final int n) {
        return new Period(n * this.length, this.units);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + length;
        result = prime * result + ((units == null) ? 0 : units.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Period other = (Period) obj;
        if (length != other.length) {
            return false;
        }
        if (units == null) {
            if (other.units != null) {
                return false;
            }
        } else if (!units.equals(other.units)) {
            return false;
        }
        return true;
    }

    public boolean eq(final Period anotherPeriod) {
        return this.equals(anotherPeriod);
    }

    public boolean neq(final Period anotherPeriod) {
        return !this.equals(anotherPeriod);
    }

    public boolean gt(final Period anotherPeriod) {
        return anotherPeriod.lt(this);
    }

    public boolean le(final Period anotherPeriod) {
        return !anotherPeriod.lt(this);
    }

    public boolean ge(final Period anotherPeriod) {
        return !this.le(anotherPeriod);
    }

    public boolean lt(final Period p2) {
        if (this.length == 0) {
            return (p2.length > 0);
        }
        if (p2.length == 0) {
            return (this.length < 0);
        }

        switch (this.units) {
        case Days:
            switch (p2.units) {
            case Days:
                return (this.length < p2.length);
            case Weeks:
                return (this.length < p2.length * 7);
            case Months:
                if (this.length < p2.length * 28) {
                    return true;
                } else {
                    throw new LibraryException(UNDECIDABLE_COMPARISON); // QA:[RG]::verified
                }
            case Years:
                return (this.length < p2.length * 365);
            default:
                throw new LibraryException(UNKNOWN_UNITS); // QA:[RG]::verified
            }
        case Weeks:
            switch (p2.units) {
            case Days:
                return (this.length * 7 < p2.length);
            case Weeks:
                return (this.length < p2.length);
            case Months:
                if (this.length * 7 < p2.length * 28) {
                    return true;
                } else {
                    throw new LibraryException(UNDECIDABLE_COMPARISON); // QA:[RG]::verified
                }
            case Years:
                if (this.length * 7 < p2.length * 365) {
                    return true;
                } else {
                    throw new LibraryException(UNDECIDABLE_COMPARISON); // QA:[RG]::verified
                }
            default:
                throw new LibraryException(UNKNOWN_UNITS); // QA:[RG]::verified
            }
        case Months:
            switch (p2.units) {
            case Days:
                // Sup[days in this.length months] < days in p2
                if (this.length * 31 < p2.length) {
                    return true;
                } else if ((this.length != 0) && p2.length < 28) {
                    return false;
                } else {
                    throw new LibraryException(UNDECIDABLE_COMPARISON); // QA:[RG]::verified
                }
            case Weeks:
                if (this.length * 31 < p2.length * 7) {
                    return true;
                } else {
                    throw new LibraryException(UNDECIDABLE_COMPARISON); // QA:[RG]::verified
                }
            case Months:
                return (this.length < p2.length);
            case Years:
                return (this.length < p2.length * 12);
            default:
                throw new LibraryException(UNKNOWN_UNITS); // QA:[RG]::verified
            }
        case Years:
            switch (p2.units) {
            case Days:
                if (this.length * 366 < p2.length) {
                    return true;
                } else if ((this.length != 0) && p2.length < 365) {
                    return false;
                } else {
                    throw new LibraryException(UNDECIDABLE_COMPARISON); // QA:[RG]::verified
                }
            case Weeks:
                if (this.length * 366 < p2.length * 7) {
                    return true;
                } else {
                    throw new LibraryException(UNDECIDABLE_COMPARISON); // QA:[RG]::verified
                }
            case Months:
                return (this.length * 12 < p2.length);
            case Years:
                return (this.length < p2.length);
            default:
                throw new LibraryException(UNKNOWN_UNITS); // QA:[RG]::verified
            }
        default:
            throw new LibraryException(UNKNOWN_UNITS); // QA:[RG]::verified
        }
    }

    @Override
    public String toString() {
        return getLongFormat();
    }

    /**
     * Returns the name of period in long format
     *
     * @return the name of period in long format
     */
    public String getLongFormat() {
        return getInternalLongFormat();
    }

    /**
     * Returns the name of period in short format (3 letters)
     *
     * @return the name of period in short format (3 letters)
     */
    public String getShortFormat() {
        return getInternalShortFormat();
    }

    /**
     * Returns periods in long format (e.g. "2 weeks")
     */
    private String getInternalLongFormat() {
        String suffix;
        if (this.length == 1) {
            suffix = "";
        } else {
            suffix = "s";
        }
        final StringBuilder sb = new StringBuilder();
        final Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("%d %s%s", this.length, this.units.getLongFormat(),
                suffix);
        return sb.toString();
    }

    /**
     * Output periods in short format (e.g. "2w")
     */
    private String getInternalShortFormat() {
        final StringBuilder sb = new StringBuilder();
        final Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("%d%s", this.length, this.units.getShortFormat());
        return sb.toString();
    }

    public void normalize() {
        if (length!=0) {
            switch (units) {
            case Days:
                //if (!(length%7)) {
                //FIXME....c++ true !=null ? always true???
                if (!(length%7!=0)) {
                    length/=7;
                    units = TimeUnit.Weeks;
                }
                break;
            case Months:
                if (!(length%12!=0)) {
                    length/=12;
                    units = TimeUnit.Years;
                }
                break;
            case Weeks:
            case Years:
                break;
            default:
                QL.require(false , UNKNOWN_TIME_UNIT); // QA:[RG]::verified
            }
        }
    }

}
