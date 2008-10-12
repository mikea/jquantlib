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

package org.jquantlib.util;

import org.jquantlib.time.IMM;

/**
 * Month names
 */
public enum Month {
	JANUARY   (1),
	FEBRUARY  (2),
    MARCH     (3),
    APRIL     (4),
    MAY       (5),
    JUNE      (6),
    JULY      (7),
    AUGUST    (8),
    SEPTEMBER (9),
    OCTOBER   (10),
    NOVEMBER  (11),
    DECEMBER  (12);

	private final int enumValue;
	
	private Month(int month) {
		this.enumValue = month;
	}
	
	/**
	 * Returns the ordinal number of this Month
	 * 
	 * @return the ordinal number of this Month
	 */
	public int toInteger() {
		return enumValue;
	}
	
	/**
	 * Returns a new Month given it's ordinal number
	 * 
	 * @param month is the ordinal number
	 * @return a new Month given it's ordinal number
	 */
	static public Month valueOf(int month) {
		switch (month) {
		case 1:
			return Month.JANUARY;
		case 2:
			return Month.FEBRUARY;
		case 3:
			return Month.MARCH;
		case 4:
			return Month.APRIL;
		case 5:
			return Month.MAY;
		case 6:
			return Month.JUNE;
		case 7:
			return Month.JULY;
		case 8:
			return Month.AUGUST;
		case 9:
			return Month.SEPTEMBER;
		case 10:
			return Month.OCTOBER;
		case 11:
			return Month.NOVEMBER;
		case 12:
			return Month.DECEMBER;
		default:
			throw new IllegalArgumentException();
		}
	}
	
    /**
     * Returns the IMM char for this Month
     * 
     * @return the IMM char for this Month
     * @see IMM
     */
	public char getImmChar() {
		switch (enumValue) {
		case 1:
			return 'F';
		case 2:
			return 'G';
		case 3:
			return 'H';
		case 4:
			return 'J';
		case 5:
			return 'K';
		case 6:
			return 'M';
		case 7:
			return 'N';
		case 8:
			return 'Q';
		case 9:
			return 'U';
		case 10:
			return 'V';
		case 11:
			return 'X';
		case 12:
			return 'Z';
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns a new month given it's IMM code
	 * 
	 * @param immCode is the IMM code 
	 * @return a new month given it's IMM code
	 */
	static public Month valueOf(char immCode) {
		switch (immCode) {
		case 'F': return Month.JANUARY;
		case 'G': return Month.FEBRUARY;
		case 'H': return Month.MARCH;
		case 'J': return Month.APRIL;
		case 'K': return Month.MAY;
		case 'M': return Month.JUNE;
		case 'N': return Month.JULY;
		case 'Q': return Month.AUGUST;
		case 'U': return Month.SEPTEMBER;
		case 'V': return Month.OCTOBER;
		case 'X': return Month.NOVEMBER;
		case 'Z': return Month.DECEMBER;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public String toString() {
		switch (enumValue) {
		case 1:
			return "January";
		case 2:
			return "February";
		case 3:
			return "March";
		case 4:
			return "April";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December";
		default:
			throw new IllegalArgumentException();
		}
	}
}