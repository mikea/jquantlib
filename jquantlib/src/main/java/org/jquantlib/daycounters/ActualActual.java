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
 * @author Richard Gomes
 * 
 */
// TODO: Is this the best way if implementing ?
// We could do it with a singletons to avoid multiple object creations
// TODO: finish,clean and test
public class ActualActual extends AbstractDayCounter {
    
    public static enum Convention {
        ISMA, Bond, ISDA, Historical, Actual365, AFB, Euro
    };

    private DayCounter delegate;

    public ActualActual(Convention convention) {
        switch (convention) {
        case ISMA:
        case Bond:
            delegate = new ISMA();
            break;
        case ISDA:
        case Historical:
        case Actual365:
            delegate = new ISDA();
            break;
        case AFB:
        case Euro:
            delegate = new AFB();
            break;
        default:
            throw new IllegalArgumentException("unknown act/act convention");
        }
    }

	public String getName() /* @ReadOnly */{
		return delegate.getName();
	}

    public double getYearFraction(Date dateStart, Date dateEnd) {
        return delegate.getYearFraction(dateStart, dateEnd);
    }

    public double getYearFraction(final Date dateStart, final Date dateEnd, final Date refPeriodStart, final Date refPeriodEnd) {
        return delegate.getYearFraction(dateStart, dateEnd, refPeriodStart, refPeriodEnd);
    }

    private class ISMA extends AbstractDayCounter {

		public final String getName() /* @ReadOnly */{
			return "Actual/Actual (ISMA)";
		}

        public double getYearFraction(final Date dateStart, final Date dateEnd) {
            return getYearFraction(dateStart, dateEnd, Date.NULL_DATE, Date.NULL_DATE);
        }

        public double getYearFraction(final Date dateStart, final Date dateEnd, final Date refPeriodStart, final Date refPeriodEnd) {

            if (dateStart.eq(dateEnd))
                return 0.0;

            if (dateStart.gt(dateEnd))
                return -getYearFraction(dateEnd, dateStart, refPeriodStart, refPeriodEnd);

            // when the reference period is not specified, try taking
            // it equal to (d1,d2)
            Date refStart = (refPeriodStart == null) || refPeriodStart.eq(Date.NULL_DATE) ? dateStart : refPeriodStart;
            Date refEnd   = (refPeriodEnd == null) || refPeriodEnd.eq(Date.NULL_DATE) ? dateEnd : refPeriodEnd;

            if (!(refEnd.gt(refStart)) && !(refEnd.gt(dateStart)))
                throw new IllegalArgumentException("Invalid reference period");

            // estimate roughly the length in months of a period
            int months = (int) Math.round(12 * (refEnd.getValue() - refStart.getValue()) / (double) 365);

            // for short periods...
            if (months == 0) {
                // ...take the reference period as 1 year from d1
                refStart = dateStart;
                refEnd = dateStart.add(new Period(1, TimeUnit.Years));// 1*Years;
                months = 12;
            }

            double period = months / 12.0;
            // Time period = Real(months)/12.0;

            if (dateEnd.le(refEnd)) {
                // here refPeriodEnd is a future (notional?) payment date
                if (dateStart.gt(refStart)) {
                    // here refPeriodStart is the last (maybe notional)
                    // payment date.
                    // refPeriodStart <= d1 <= d2 <= refPeriodEnd
                    // [maybe the equality should be enforced, since
                    // refPeriodStart < d1 <= d2 < refPeriodEnd
                    // could give wrong results] ???
                    return period * getDayCount(dateStart, dateEnd)
                            / getDayCount(refStart, refEnd);
                } else {
                    // here refPeriodStart is the next (maybe notional)
                    // payment date and refPeriodEnd is the second next
                    // (maybe notional) payment date.
                    // d1 < refPeriodStart < refPeriodEnd
                    // AND d2 <= refPeriodEnd
                    // this case is long first coupon

                    // the last notional payment date
                    Date previousRef = refStart.dec(new Period(months,
                            TimeUnit.Months));
                    if (dateEnd.gt(refStart))
                        return getYearFraction(dateStart, refStart, previousRef,
                                refStart)
                                + getYearFraction(refStart, dateEnd,
                                        refStart, refEnd);
                    else
                        return getYearFraction(dateStart, dateEnd, previousRef,
                                refStart);
                }
            } else {
                // here refPeriodEnd is the last (notional?) payment date
                // d1 < refPeriodEnd < d2 AND refPeriodStart < refPeriodEnd
                if (!refStart.le(dateStart))
                    throw new IllegalStateException(
                            "invalid dates d1 < refPeriodStart < refPeriodEnd < d2");

                // the part from d1 to refPeriodEnd
                double sum = getYearFraction(dateStart, refEnd, refStart,
                        refEnd);

                // the part from refPeriodEnd to d2
                // count how many regular periods are in [refPeriodEnd, d2],
                // then add the remaining time
                Integer i = 0;
                Date newRefStart, newRefEnd;
                do {
                    newRefStart = refEnd.add(new Period((months * i),
                            TimeUnit.Months));
                    newRefEnd = refEnd.add(new Period((months * (i + 1)),
                            TimeUnit.Months));
                    if (dateEnd.lt(newRefEnd)) {
                        break;
                    } else {
                        sum += period;
                        i++;
                    }
                } while (true);
                sum += getYearFraction(newRefStart, dateEnd, newRefStart, newRefEnd);
                return sum;
            }

        }
    }

    // TODO: complete impl
    private class ISDA extends AbstractDayCounter {

		public final String getName() /* @ReadOnly */{
			return "Actual/Actual (ISDA)";
		}

        public double getYearFraction(final Date dateStart, final Date dateEnd) {
            if (dateStart.eq(dateEnd))
                return 0.0;

            if (dateStart.gt(dateEnd))
                return -getYearFraction(dateEnd, dateStart, Date.NULL_DATE, Date.NULL_DATE);

            int y1 = dateStart.getYear();
            int y2 = dateEnd.getYear();
            double dib1 = (Date.isLeap(y1) ? 366.0 : 365.0);
            double dib2 = (Date.isLeap(y2) ? 366.0 : 365.0);

            /*@Time*/ double sum = y2 - y1 - 1;
            sum += getDayCount(dateStart, new Date(1, Date.Month.January, y1+1))/dib1;
            sum += getDayCount(new Date(1, Date.Month.January,y2),dateEnd)/dib2;
            return sum;
        }

        public double getYearFraction(final Date dateStart, final Date dateEnd, final Date d3, final Date d4) {
        	return this.getYearFraction(dateStart, dateEnd);
        }

    }

    // TODO: complete impl
    private class AFB extends AbstractDayCounter {

		public final String getName() /* @ReadOnly */{
			return "Actual/Actual (AFB)";
		}

        public double getYearFraction(final Date dateStart, final Date dateEnd) {
            if (dateStart.eq(dateEnd))
                return 0.0;

            if (dateStart.gt(dateEnd))
                return -getYearFraction(dateEnd, dateStart, Date.NULL_DATE, Date.NULL_DATE);

            Date newD2=dateEnd;
            Date temp=dateEnd;
            /*@Time*/ double sum = 0.0;
            while (temp.gt(dateStart)) {
                temp = newD2.subtract(new Period(1, TimeUnit.Years));
                if (temp.getDayOfMonth()==28 && temp.getMonth()==2 && Date.isLeap(temp.getYear())) {
                    temp.inc(1);
                }
                if (temp.ge(dateStart)) {
                    sum += 1.0;
                    newD2 = temp;
                }
            }

            double den = 365.0;

            if (Date.isLeap(newD2.getYear())) {
                temp = new Date(29, Date.Month.February, newD2.getYear());
                if (newD2.gt(temp) && dateStart.le(temp))
                    den += 1.0;
            } else if (Date.isLeap(dateStart.getYear())) {
                temp = new Date(29, Date.Month.February, dateStart.getYear());
                if (newD2.gt(temp) && dateStart.le(temp))
                    den += 1.0;
            }

            return sum+getDayCount(dateStart, newD2)/den;
        }

        public double getYearFraction(final Date dateStart, final Date dateEnd, final Date refPeriodStart, final Date refPeriodEnd) {
        	return this.getYearFraction(dateStart, dateEnd);
        }

    }
}
