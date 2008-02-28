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
 * Time units
 * 
 * @author Richard Gomes
 */
public enum TimeUnit {
	Days, Weeks, Months, Years;


    /**
     * Returns the name of time unit in long format (e.g. "week")
     *
     * @return the name of time unit in long format (e.g. "week")
     */ 
	public String getLongFormat() {
		return new LongFormat(this).toString();
	}

    /**
     * Returns the name of time unit in short format (e.g. "w")
     *
     * @return the name of time unit in short format (e.g. "w")
     */ 
	public String getShortFormat() {
		return new ShortFormat(this).toString();
	}

    /**
     * Output time units in long format (e.g. "week")
     * 
     * @note message in singular form
     */ 
	private class LongFormat {
		private TimeUnit units;

		public LongFormat(TimeUnit units) {
			this.units = units;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(units.toString().toLowerCase());
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
	}

    /**
     * Output time units in short format (e.g. "w")
     */ 
	private class ShortFormat {
		private TimeUnit units;

		public ShortFormat(TimeUnit units) {
			this.units = units;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(units.toString().toLowerCase());
			sb.setLength(1);
			return sb.toString();
		}
	}

}
