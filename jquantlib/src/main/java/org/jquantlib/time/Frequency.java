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

/*
 Copyright (C) 2004, 2005, 2006 Ferdinando Ametrano
 Copyright (C) 2006 Katiuscia Manzoni
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006 StatPro Italia srl

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

/**
 * Frequency of events
 */
// TODO: document methods
public enum Frequency {
	/** null frequency */		NO_FREQUENCY     (-1),
	/** only once */			ONCE			 (0),
    /** once a year */			ANNUAL			 (1),
    /** twice a year */			SEMI_ANNUAL		 (2),
    /** every fourth month */	EVERY_FOURTH_DAY (3),
    /** every third month */	QUARTELY		 (4),
    /** every second month */	BI_MONTHLY		 (6),
    /** once a month */			MONTHLY			 (12),
    /** every second week */	BI_WEEKLY		 (26),
    /** once a week */			WEEKLY			 (52),
    /** once a day */			DAILY			 (365); 

	private final int enumValue;
	
	private Frequency(int frequency) {
		this.enumValue = frequency;
	}
	
	static public Frequency valueOf(int value) {
		switch (value) {
		case -1:
			return Frequency.NO_FREQUENCY;
		case 0:
			return Frequency.ONCE;
		case 1:
			return Frequency.ANNUAL;
		case 2:
			return Frequency.SEMI_ANNUAL;
		case 3:
			return Frequency.EVERY_FOURTH_DAY;
		case 4:
			return Frequency.QUARTELY;
		case 6:
			return Frequency.BI_MONTHLY;
		case 12:
			return Frequency.MONTHLY;
		case 26:
			return Frequency.BI_WEEKLY;
		case 52:
			return Frequency.WEEKLY;
		case 365:
			return Frequency.DAILY;
		default:
			throw new IllegalArgumentException("value must be one of -1,0,1,2,3,4,6,12,26,52,365");
		}
	}

	public int toInteger() {
		return this.enumValue;
	}
	
}
