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

package org.jquantlib.daycounters;

import org.jquantlib.util.Date;

/**
 * 30/360 day count convention
 * 
 * <p>
 * The 30/360 day count can be calculated according to US, European, or Italian
 * conventions.
 * 
 * US (NASD) convention: if the starting date is the 31st of a month, it becomes
 * equal to the 30th of the same month. If the ending date is the 31st of a
 * month and the starting date is earlier than the 30th of a month, the ending
 * date becomes equal to the 1st of the next month, otherwise the ending date
 * becomes equal to the 30th of the same month. Also known as "30/360",
 * "360/360", or "Bond Basis"
 * 
 * European convention: starting dates or ending dates that occur on the 31st of
 * a month become equal to the 30th of the same month. Also known as "30E/360",
 * or "Eurobond Basis"
 * 
 * Italian convention: starting dates or ending dates that occur on February and
 * are grater than 27 become equal to 30 for computational sake.
 */
public class Thirty360 extends AbstractDayCounter {

	public enum Convention {
		USA, BondBasis, European, EurobondBasis, Italian;
	}

	private int dd1, dd2;
	private int mm1, mm2;
	private int yy1, yy2;

	private Thirty360Abstraction delegate = null;

	public Thirty360() {
		this(Thirty360.Convention.BondBasis);
	}

	public Thirty360(final Thirty360.Convention c) {
		super();
		switch (c) {
		case USA:
		case BondBasis:
			delegate = new US();
			break;
		case European:
		case EurobondBasis:
			delegate = new EU();
			break;
		case Italian:
			delegate = new IT();
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public String getName() /* @ReadOnly */{
		return delegate.getName();
	}

	public int getDayCount(final Date d1, final Date d2) /* @ReadOnly */{
		this.dd1 = d1.getDayOfMonth();
		this.dd2 = d2.getDayOfMonth();
		this.mm1 = d1.getMonth();
		this.mm2 = d2.getMonth();
		this.yy1 = d1.getYear();
		this.yy2 = d2.getYear();

		((Thirty360Abstraction) delegate).adjust();

		return 360 * (yy2 - yy1) + 30 * (mm2 - mm1 - 1) + Math.max(0, 30 - dd1) + Math.min(30, dd2);
	}

	public/* @Time */double getYearFraction(
						final Date dateStart, final Date dateEnd, 
						final Date refPeriodStart, final Date refPeriodEnd) /* @ReadOnly */{
		return getDayCount(dateStart, dateEnd) / 360.0;
	}

	public/* @Time */double getYearFraction(final Date dateStart, final Date dateEnd) /* @ReadOnly */{
		return getDayCount(dateStart, dateEnd) / 360.0;
	}

	
	//
	// inner classes
	//

	
	/**
	 * Abstraction of Thirty360 class according to the Bridge Pattern
	 * 
	 * @see http://en.wikipedia.org/wiki/Bridge_pattern
	 * 
	 * @author Richard Gomes
	 */
	private interface Thirty360Abstraction {
		public String getName();
		public void adjust();
	}

	/**
	 * Implementation of Thirty360 class abstraction according to US convention
	 * 
	 * @see http://en.wikipedia.org/wiki/Bridge_pattern
	 * 
	 * @author Richard Gomes
	 */
	private class US implements Thirty360Abstraction {

		public final String getName() /* @ReadOnly */{
			return "30/360 (Bond Basis)";
		}

		public void adjust() {
			if (dd2 == 31 && dd1 < 30) {
				dd2 = 1;
				mm2++;
			}
		}
	}

	/**
	 * Implementation of Thirty360 class abstraction according to European convention
	 * 
	 * @see http://en.wikipedia.org/wiki/Bridge_pattern
	 * 
	 * @author Richard Gomes
	 */
	private class EU implements Thirty360Abstraction {
		public final String getName() /* @ReadOnly */{
			return "30E/360 (Eurobond Basis)";
		}

		public void adjust() {
		}
	}

	/**
	 * Implementation of Thirty360 class abstraction according to Italian convention
	 * 
	 * @see http://en.wikipedia.org/wiki/Bridge_pattern
	 * 
	 * @author Richard Gomes
	 */
	private class IT implements Thirty360Abstraction {

		public final String getName() /* @ReadOnly */{
			return "30/360 (Italian)";
		}

		public void adjust() {
			if (mm1 == 2 && dd1 > 27)
				dd1 = 30;
			if (mm2 == 2 && dd2 > 27)
				dd2 = 30;
		}
	}

}
