/*
 Copyright (C) 2008 Srinivas Hasti

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

package org.jquantlib.daycounters;

import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;

/**
 * 
 * @author Srinivas Hasti
 * 
 */
// TODO: Is this the best way if implementing ?
// We could do it with a singletons to avoid multiple object creations
// TODO: finish and test
public class ActualActual extends DayCounterImpl {
    
    public static enum Convention {
        ISMA, Bond, ISDA, Historical, Actual365, AFB, Euro
    };

    private DayCounter reference;

    public ActualActual(Convention convention) {
        switch (convention) {
        case ISMA:
        case Bond:
            reference = new ISMA();
            break;
        case ISDA:
        case Historical:
        case Actual365:
            reference = new ISDA();
            break;
        case AFB:
        case Euro:
            reference = new AFB();
            break;
        default:
            throw new IllegalArgumentException("unknown act/act convention");
        }
    }

    public double getYearFraction(Date dateStart, Date dateEnd) {
        return reference.getYearFraction(dateStart, dateEnd);
    }

    public double getYearFraction(Date dateStart, Date dateEnd,
            Date refPeriodStart, Date refPeriodEnd) {
        return reference.getYearFraction(dateStart, dateEnd, refPeriodStart,
                refPeriodEnd);
    }

    private class ISMA extends DayCounterImpl {

        public double getYearFraction(Date dateStart, Date dateEnd) {
            return 0;
        }

        public double getYearFraction(Date d1, Date d2, Date d3, Date d4) {

            if (d1.eq(d2))
                return 0.0;

            if (d1.gt(d2))
                return -getYearFraction(d2, d1, d3, d4);

            // when the reference period is not specified, try taking
            // it equal to (d1,d2)
            Date refPeriodStart = (d3 == null) || d3.eq(Date.NULL_DATE) ? d1
                    : d3;
            Date refPeriodEnd = (d4 == null) || d4.eq(Date.NULL_DATE) ? d2 : d4;

            if (!(refPeriodEnd.gt(refPeriodStart)) && !(refPeriodEnd.gt(d1)))
                throw new IllegalArgumentException("Invalid reference period");

            // estimate roughly the length in months of a period
            int months = (int) Math.round(12
                    * (refPeriodEnd.getValue() - refPeriodStart.getValue())
                    / (double) 365);

            // for short periods...
            if (months == 0) {
                // ...take the reference period as 1 year from d1
                refPeriodStart = d1;
                refPeriodEnd = d1.add(new Period(1, TimeUnit.Years));// 1*Years;
                months = 12;
            }

            double period = months / 12.0;
            // Time period = Real(months)/12.0;

            if (d2.le(refPeriodEnd)) {
                // here refPeriodEnd is a future (notional?) payment date
                if (d1.gt(refPeriodStart)) {
                    // here refPeriodStart is the last (maybe notional)
                    // payment date.
                    // refPeriodStart <= d1 <= d2 <= refPeriodEnd
                    // [maybe the equality should be enforced, since
                    // refPeriodStart < d1 <= d2 < refPeriodEnd
                    // could give wrong results] ???
                    return period * getDayCount(d1, d2)
                            / getDayCount(refPeriodStart, refPeriodEnd);
                } else {
                    // here refPeriodStart is the next (maybe notional)
                    // payment date and refPeriodEnd is the second next
                    // (maybe notional) payment date.
                    // d1 < refPeriodStart < refPeriodEnd
                    // AND d2 <= refPeriodEnd
                    // this case is long first coupon

                    // the last notional payment date
                    Date previousRef = refPeriodStart.dec(new Period(months,
                            TimeUnit.Months));
                    if (d2.gt(refPeriodStart))
                        return getYearFraction(d1, refPeriodStart, previousRef,
                                refPeriodStart)
                                + getYearFraction(refPeriodStart, d2,
                                        refPeriodStart, refPeriodEnd);
                    else
                        return getYearFraction(d1, d2, previousRef,
                                refPeriodStart);
                }
            } else {
                // here refPeriodEnd is the last (notional?) payment date
                // d1 < refPeriodEnd < d2 AND refPeriodStart < refPeriodEnd
                if (!refPeriodStart.le(d1))
                    throw new IllegalStateException(
                            "invalid dates d1 < refPeriodStart < refPeriodEnd < d2");

                // the part from d1 to refPeriodEnd
                double sum = getYearFraction(d1, refPeriodEnd, refPeriodStart,
                        refPeriodEnd);

                // the part from refPeriodEnd to d2
                // count how many regular periods are in [refPeriodEnd, d2],
                // then add the remaining time
                Integer i = 0;
                Date newRefStart, newRefEnd;
                do {
                    newRefStart = refPeriodEnd.add(new Period((months * i),
                            TimeUnit.Months));
                    newRefEnd = refPeriodEnd.add(new Period((months * (i + 1)),
                            TimeUnit.Months));
                    if (d2.lt(newRefEnd)) {
                        break;
                    } else {
                        sum += period;
                        i++;
                    }
                } while (true);
                sum += getYearFraction(newRefStart, d2, newRefStart, newRefEnd);
                return sum;
            }

        }
    }

    // TODO: complete impl
    private class ISDA extends DayCounterImpl {

        public double getYearFraction(Date dateStart, Date dateEnd) {
            // TODO Auto-generated method stub
            return 0;
        }

        public double getYearFraction(Date dateStart, Date dateEnd,
                Date refPeriodStart, Date refPeriodEnd) {
            // TODO Auto-generated method stub
            return 0;
        }

    }

    // TODO: complete impl
    private class AFB extends DayCounterImpl {

        public double getYearFraction(Date dateStart, Date dateEnd) {
            // TODO Auto-generated method stub
            return 0;
        }

        public double getYearFraction(Date dateStart, Date dateEnd,
                Date refPeriodStart, Date refPeriodEnd) {
            // TODO Auto-generated method stub
            return 0;
        }

    }
}
