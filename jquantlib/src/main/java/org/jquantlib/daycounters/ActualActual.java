/*
 Copyright (C) 2008 Srinivas Hasti

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

import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;

/**
 * Implementation of ActualActual day counters.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Day_count_convention">Day count convention</a>
 * 
 * @author Srinivas Hasti
 * @author Richard Gomes
 * 
 */
public class ActualActual extends AbstractDayCounter {

	//
    // public enums
    //
    
    /**
	 * Actual/Actual Calendar Conventions
	 */
	public static enum Convention {
		ISMA, BOND, ISDA, HISTORICAL, ACTUAL365, AFB, EURO
	};

	
	//
	// private final static fields
	//
	
	private static final ActualActual ISMA_DAYCOUNTER = new ActualActual(Convention.ISMA);
	private static final ActualActual ACTUAL365_DAYCOUNTER = new ActualActual(Convention.ISDA);
	private static final ActualActual AFB_DAYCOUNTER = new ActualActual(Convention.AFB);

	
    //
    // public static final constructors
    //
    
    /**
     * Returns ActualActual day counter for the specified convention type
     * 
     * @param convention
     * @return
     */
    public static final ActualActual getDayCounter(final Convention convention) {
        switch (convention) {
        case ISMA:
        case BOND:
            return ISMA_DAYCOUNTER;
        case ISDA:
        case HISTORICAL:
        case ACTUAL365:
            return ACTUAL365_DAYCOUNTER;
        case AFB:
        case EURO:
            return AFB_DAYCOUNTER;
        default:
            throw new IllegalArgumentException("unknown act/act convention");
        }
    }

    //
	// private final fields
	//
	
	private final DayCounter delegate;

	//
	// private constructors
	//
	
	/**
	 * This constructor is intended to be used internally by <code>this</code> class at initialization time,
	 * when specialized day counters are constructed.
	 */
	private ActualActual(final Convention convention) {
	    super();
	    
		switch (convention) {
		case ISMA:
		case BOND:
			delegate = new ISMA();
			break;
		case ISDA:
		case HISTORICAL:
		case ACTUAL365:
			delegate = new ISDA();
			break;
		case AFB:
		case EURO:
			delegate = new AFB();
			break;
		default:
			throw new IllegalArgumentException("unknown act/act convention");
		}
	}

	//
	// implements DayCounter
	//
	
	@Override
	public final String getName() {
		return delegate.getName();
	}

    @Override
	public final double getYearFraction(final Date dateStart, final Date dateEnd) /* @ReadOnly */{
		return delegate.getYearFraction(dateStart, dateEnd);
	}

    @Override
	public final double getYearFraction(final Date dateStart, final Date dateEnd,
			final Date refPeriodStart, final Date refPeriodEnd) /* @ReadOnly */{
		return delegate.getYearFraction(dateStart, dateEnd, refPeriodStart,
				refPeriodEnd);
	}


    //
    // inner classes
    //
    
    private final class ISMA extends AbstractDayCounter {

        @Override
        public final String getName() /* @ReadOnly */{
			return "Actual/Actual (ISMA)";
		}

        @Override
		public final double getYearFraction(final Date dateStart, final Date dateEnd) {
			return getYearFraction(dateStart, dateEnd, Date.NULL_DATE,
					Date.NULL_DATE);
		}

        @Override
		public final double getYearFraction(final Date d1, final Date d2,
				final Date d3, final Date d4) {

			if (d1.equals(d2))
				return 0.0;

			if (d1.gt(d2))
				return -getYearFraction(d2, d1, d3, d4);

			// when the reference period is not specified, try taking
			// it equal to (d1,d2)
			Date refPeriodStart = (!d3.equals(Date.NULL_DATE) ? d3 : d1);
			Date refPeriodEnd = (!d4.equals(Date.NULL_DATE) ? d4 : d2);

			if (!(refPeriodEnd.gt(refPeriodStart) && refPeriodEnd.gt(d1))) {
				throw new IllegalArgumentException("invalid reference period: "
						+ "date 1: " + d1 + ", date 2: " + d2
						+ ", reference period start: " + refPeriodStart
						+ ", reference period end: " + refPeriodEnd);
			}

			// estimate roughly the length in months of a period
			int months = (int) (0.5 + 12 * (refPeriodStart
					.getDayCount(refPeriodEnd)) / 365.0);

			// for short periods...
			if (months == 0) {
				// ...take the reference period as 1 year from d1
				refPeriodStart = d1;
				refPeriodEnd = d1.getDateAfter(Period.ONE_YEAR_FORWARD);
				months = 12;
			}

			final double period = months / 12.0;

			if (d2.le(refPeriodEnd)) {
				// here refPeriodEnd is a future (notional?) payment date
				if (d1.ge(refPeriodStart)) {
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
					final Date previousRef = refPeriodStart.getDateAfter(new Period(
							-months, TimeUnit.MONTHS));
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
				// here refPeriodEnd is the last notional payment date
				// d1 < refPeriodEnd < d2 AND refPeriodStart < refPeriodEnd
				if (refPeriodStart.gt(d1)) {
					throw new IllegalArgumentException(
							"invalid dates: d1 < refPeriodStart < refPeriodEnd < d2");
				}

				// now it is: refPeriodStart <= d1 < refPeriodEnd < d2

				// the part from d1 to refPeriodEnd
				double sum = getYearFraction(d1, refPeriodEnd, refPeriodStart,
						refPeriodEnd);

				// the part from refPeriodEnd to d2
				// count how many regular periods are in [refPeriodEnd, d2],
				// then add the remaining time
				int i = 0;
				Date newRefStart, newRefEnd;
				do {
					newRefStart = refPeriodEnd.getDateAfter(new Period(months
							* i, TimeUnit.MONTHS));
					newRefEnd = refPeriodEnd.getDateAfter(new Period(months
							* (i + 1), TimeUnit.MONTHS));
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

	private final class ISDA extends AbstractDayCounter {

        @Override
		public final String getName() /* @ReadOnly */{
			return "Actual/Actual (ISDA)";
		}

        @Override
		public final double getYearFraction(final Date dateStart, final Date dateEnd) /* @ReadOnly */{
			if (dateStart.equals(dateEnd))
				return 0.0;

			if (dateStart.gt(dateEnd))
				return -getYearFraction(dateEnd, dateStart, Date.NULL_DATE,
						Date.NULL_DATE);

			final int y1 = dateStart.getYear();
			final int y2 = dateEnd.getYear();
			final double dib1 = DateFactory.getFactory().isLeap(y1) ? 366.0 : 365.0;
			final double dib2 = DateFactory.getFactory().isLeap(y2) ? 366.0 : 365.0;

			double sum = y2 - y1 - 1;

			// Days from start to starting of following year
			sum += (dib1 - dateStart.getDayOfYear() + 1) / dib1;
			// Days from beginning of year to the endDate
			sum += (dateEnd.getDayOfYear() - 1) / dib2;
			return sum;
		}

        @Override
		public final double getYearFraction(final Date dateStart, final Date dateEnd,
				final Date d3, final Date d4) /* @ReadOnly */{
			return this.getYearFraction(dateStart, dateEnd);
		}

	}

	private final class AFB extends AbstractDayCounter {

        @Override
		public final String getName() {
			return "Actual/Actual (AFB)";
		}

        @Override
		public final double getYearFraction(final Date dateStart, final Date dateEnd) {
			if (dateStart.equals(dateEnd))
				return 0.0;

			if (dateStart.gt(dateEnd))
				return -getYearFraction(dateEnd, dateStart, Date.NULL_DATE,
						Date.NULL_DATE);

			Date newD2 = dateEnd;
			Date temp = dateEnd;
			double sum = 0.0;
			while (temp.gt(dateStart)) {
				temp = newD2.getDateAfter(Period.ONE_YEAR_BACKWARD);
				if (temp.getDayOfMonth() == 28 && temp.getMonth() == 2
						&& DateFactory.getFactory().isLeap(temp.getYear())) {
					temp.increment(1);
				}
				if (temp.ge(dateStart)) {
					sum += 1.0;
					newD2 = temp;
				}
			}

			double den = 365.0;

			if (DateFactory.getFactory().isLeap(newD2.getYear())) {
				if (newD2.gt(29, Month.FEBRUARY, newD2.getYear())
						&& dateStart.le(29, Month.FEBRUARY, newD2.getYear()))
					den += 1.0;
			} else if (DateFactory.getFactory().isLeap(dateStart.getYear())) {
				if (newD2.gt(29, Month.FEBRUARY, dateStart.getYear())
						&& dateStart
								.le(29, Month.FEBRUARY, dateStart.getYear()))
					den += 1.0;
			}

			return sum + getDayCount(dateStart, newD2) / den;
		}

        @Override
		public final double getYearFraction(final Date dateStart, final Date dateEnd,
				final Date refPeriodStart, final Date refPeriodEnd) {
			return this.getYearFraction(dateStart, dateEnd);
		}

	}
}
