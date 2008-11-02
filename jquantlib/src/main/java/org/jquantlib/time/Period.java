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

/**
 * Time period to represent time by days, month and years as specified by
 * TimeUnit.
 * 
 * @author Srinivas Hasti
 * @author Richard Gomes
 */
public class Period {
	
	/**
	 * Constant that can be used to represent one year period forward
	 */
	public static final Period ONE_YEAR_FORWARD = new Period(1, TimeUnit.YEARS);

	/**
	 * Constant that can be used to represent one year period in the past
	 */
	public static final Period ONE_YEAR_BACKWARD = new Period(-1, TimeUnit.YEARS);

	/**
	 * Constant that can be used to represent one year period forward
	 */
	public static final Period ONE_MONTH_FORWARD = new Period(1, TimeUnit.MONTHS);

	/**
	 * Constant that can be used to represent one year period in the past
	 */
	public static final Period ONE_MONTH_BACKWARD = new Period(-1, TimeUnit.MONTHS);

	/**
	 * Constant that can be used to represent one year period forward
	 */
	public static final Period ONE_DAY_FORWARD = new Period(1, TimeUnit.DAYS);

	/**
	 * Constant that can be used to represent one year period in the past
	 */
	public static final Period ONE_DAY_BACKWARD = new Period(-1, TimeUnit.DAYS);

	
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
		this.units = TimeUnit.DAYS;
	}

	/**
	 * To construct period representing the specified length and units
	 * 
	 * @param length
	 * @param units
	 */
	public Period(int length, final TimeUnit units) {
		this.length = length;
		this.units = units;
	}

	/**
	 * To create a period by Frequency
	 * 
	 * @param f
	 */
	public Period(Frequency f) {
		switch (f) {
		case ONCE:
		case NO_FREQUENCY:
			// same as Period()
			units = TimeUnit.DAYS;
			length = 0;
			break;
		case ANNUAL:
			units = TimeUnit.YEARS;
			length = 1;
			break;
		case SEMI_ANNUAL:
		case EVERY_FOURTH_DAY:
		case QUARTERLY:
		case BI_MONTHLY:
		case MONTHLY:
			units = TimeUnit.MONTHS;
			length = 12 / f.toInteger();
			break;
		case BI_WEEKLY:
		case WEEKLY:
			units = TimeUnit.WEEKS;
			length = 52 / f.toInteger();
			break;
		case DAILY:
			units = TimeUnit.DAYS;
			length = 1; // FIXME: review
			break;
		default:
			throw new IllegalArgumentException("unknown frequency ("
					+ f.toInteger());
		}
	}

	/**
	 * Length of the period represented as number of days
	 * 
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
		int length = Math.abs(this.length);

		if (length == 0)
			return Frequency.NO_FREQUENCY;

		switch (units) {
		case YEARS:
			if (!(length == 1))
				throw new IllegalArgumentException(
						"cannot instantiate a Frequency from " + this);
			return Frequency.ANNUAL;
		case MONTHS:
			if (!((12 % length) == 0 && (length <= 12)))
				throw new IllegalArgumentException(
						"cannot instantiate a Frequency from " + this);
			return Frequency.valueOf(12 / length);
		case WEEKS:
			if (length == 1)
				return Frequency.WEEKLY;
			else if (length == 2)
				return Frequency.BI_WEEKLY;
			else
				throw new IllegalArgumentException(
						"cannot instantiate a Frequency from " + this);
		case DAYS:
			if (!(length == 1))
				throw new IllegalArgumentException(
						"cannot instantiate a Frequency from " + this);
			return Frequency.DAILY;
		default:
			throw new IllegalArgumentException("unknown time unit (" + units);
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Period other = (Period) obj;
		if (length != other.length)
			return false;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
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
		if (this.length == 0)
			return (p2.length > 0);
		if (p2.length == 0)
			return (this.length < 0);

		switch (this.units) {
		case DAYS:
			switch (p2.units) {
			case DAYS:
				return (this.length < p2.length);
			case WEEKS:
				return (this.length < p2.length * 7);
			case MONTHS:
				if (this.length < p2.length * 28)
					return true;
				else
					throw new IllegalArgumentException(
							"undecidable comparison between periods");
			case YEARS:
				return (this.length < p2.length * 365);
			default:
				throw new IllegalArgumentException("unknown units");
			}
		case WEEKS:
			switch (p2.units) {
			case DAYS:
				return (this.length * 7 < p2.length);
			case WEEKS:
				return (this.length < p2.length);
			case MONTHS:
				if (this.length * 7 < p2.length * 28)
					return true;
				else
					throw new IllegalArgumentException(
							"undecidable comparison between periods");
			case YEARS:
				if (this.length * 7 < p2.length * 365)
					return true;
				else
					throw new IllegalArgumentException(
							"undecidable comparison between periods");
			default:
				throw new IllegalArgumentException("unknown units");
			}
		case MONTHS:
			switch (p2.units) {
			case DAYS:
				// Sup[days in this.length months] < days in p2
				if (this.length * 31 < p2.length)
					return true;
				// almost 28 days in p1 and less than 28 days in p2
				else if ((this.length != 0) && p2.length < 28)
					return false;
				else
					throw new IllegalArgumentException(
							"undecidable comparison between periods");
			case WEEKS:
				if (this.length * 31 < p2.length * 7)
					return true;
				else
					throw new IllegalArgumentException(
							"undecidable comparison between periods");
			case MONTHS:
				return (this.length < p2.length);
			case YEARS:
				return (this.length < p2.length * 12);
			default:
				throw new IllegalArgumentException("unknown units");
			}
		case YEARS:
			switch (p2.units) {
			case DAYS:
				if (this.length * 366 < p2.length)
					return true;
				// almost 365 days in p1 and less than 365 days in p2
				else if ((this.length != 0) && p2.length < 365)
					return false;
				else
					throw new IllegalArgumentException(
							"undecidable comparison between periods");
			case WEEKS:
				if (this.length * 366 < p2.length * 7)
					return true;
				else
					throw new IllegalArgumentException(
							"undecidable comparison between periods");
			case MONTHS:
				return (this.length * 12 < p2.length);
			case YEARS:
				return (this.length < p2.length);
			default:
				throw new IllegalArgumentException("unknown units");
			}
		default:
			throw new IllegalArgumentException("unknown units");
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
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format("%d %s%s", this.length, this.units.getLongFormat(),
				suffix);
		return sb.toString();
	}

	/**
	 * Output periods in short format (e.g. "2w")
	 */
	private String getInternalShortFormat() {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format("%d%s", this.length, this.units.getShortFormat());
		return sb.toString();
	}

}
