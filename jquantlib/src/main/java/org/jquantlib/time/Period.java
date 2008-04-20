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
 * Time period described by a number of a given time unit
 */
// TODO: document methods
public class Period {

	private int length;
	private TimeUnit units;

	public Period() {
		this.length = 0;
		this.units = TimeUnit.Days;
	}
	
	public Period(int length, final TimeUnit units) {
		this.length = length;
		this.units = units;
	}
	
	public Period(Frequency f) {
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
            length = 12/f.toInteger();
            break;
          case Biweekly:
          case Weekly:
            units = TimeUnit.Weeks;
            length = 52/f.toInteger();
            break;
          case Daily:
            units = TimeUnit.Days;
            length = 1; // FIXME: review
            break;
          default:
            throw new IllegalArgumentException("unknown frequency ("+f.toInteger());
        }
    }

    public final int getLength() {
    	return this.length;
    }
    
    public final TimeUnit getUnits() {
    	return this.units;
    }
    
    public final Frequency getFrequency() {
        // unsigned version
        int length = Math.abs(this.length);

        if (length==0) return Frequency.NoFrequency;

        switch (units) {
          case Years:
            if (! (length==1) ) throw new IllegalArgumentException("cannot instantiate a Frequency from "+this);
            return Frequency.Annual;
          case Months:
            if (! ((12%length)==0 && (length<=12)) ) throw new IllegalArgumentException("cannot instantiate a Frequency from "+this);
            return Frequency.valueOf(12/length);
          case Weeks:
            if (length==1)
                return Frequency.Weekly;
            else if (length==2)
                return Frequency.Biweekly;
            else
                throw new IllegalArgumentException("cannot instantiate a Frequency from "+this);
          case Days:
            if (! (length==1) ) throw new IllegalArgumentException("cannot instantiate a Frequency from "+this);
            return Frequency.Daily;
          default:
            throw new IllegalArgumentException("unknown time unit ("+units);
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
     * @param n is the multiplier
     * @return a new <code>Period</code> with a multiplied length
     */
    public Period multiply(int n) {
        return new Period(n*this.length, this.units);
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
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Period other = (Period) obj;
		if (length != other.length) return false;
		if (units == null) {
			if (other.units != null) return false;
		} else if (!units.equals(other.units)) return false;
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
        if (this.length==0) return (p2.length>0);
        if (p2.length==0) return (this.length<0);

        switch (this.units) {
          case Days:
            switch (p2.units) {
              case Days:
                return (this.length < p2.length);
              case Weeks:
                return (this.length < p2.length * 7);
              case Months:
                if (this.length < p2.length * 28)
                    return true;
                else
                    throw new IllegalArgumentException("undecidable comparison between periods");
              case Years:
                return (this.length < p2.length * 365);
              default:
                throw new IllegalArgumentException("unknown units");
            }
          case Weeks:
            switch (p2.units) {
              case Days:
                return (this.length * 7 < p2.length);
              case Weeks:
                return (this.length < p2.length);
              case Months:
                if (this.length * 7 < p2.length * 28)
                    return true;
                else
                    throw new IllegalArgumentException("undecidable comparison between periods");
              case Years:
                if (this.length * 7 < p2.length * 365)
                    return true;
                else
                    throw new IllegalArgumentException("undecidable comparison between periods");
              default:
                  throw new IllegalArgumentException("unknown units");
            }
          case Months:
            switch (p2.units) {
              case Days:
                // Sup[days in this.length months] < days in p2
                if (this.length * 31 < p2.length)
                    return true;
                // almost 28 days in p1 and less than 28 days in p2
                else if ((this.length!=0) && p2.length< 28)
                    return false;
                else
                    throw new IllegalArgumentException("undecidable comparison between periods");
              case Weeks:
                if (this.length* 31 < p2.length  * 7)
                    return true;
                else
                    throw new IllegalArgumentException("undecidable comparison between periods");
              case Months:
                return (this.length < p2.length);
              case Years:
                return (this.length < p2.length * 12);
              default:
                  throw new IllegalArgumentException("unknown units");
            }
          case Years:
            switch (p2.units) {
              case Days:
                if (this.length * 366 < p2.length)
                    return true;
                // almost 365 days in p1 and less than 365 days in p2
                else if ((this.length!=0) && p2.length< 365)
                    return false;
                else
                    throw new IllegalArgumentException("undecidable comparison between periods");
              case Weeks:
                if (this.length * 366 < p2.length * 7)
                    return true;
                else
                    throw new IllegalArgumentException("undecidable comparison between periods");
              case Months:
                return (this.length * 12 < p2.length);
              case Years:
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
		return new LongFormat(this).toString();
	}

    /**
     * Returns the name of period in short format (3 letters)
     *
     * @return the name of period in short format (3 letters)
     */ 
	public String getShortFormat() {
		return new ShortFormat(this).toString();
	}


	
    /**
     * Output periods in long format (e.g. "2 weeks")
     */ 
	private class LongFormat {
		private Period period;

		public LongFormat(Period period) {
			this.period = period;
		}

		public String toString() {
			String suffix;
			if (period.length==1) {
				suffix = "";
			} else {
				suffix = "s";
			}
			StringBuilder sb = new StringBuilder();
			Formatter formatter = new Formatter(sb, Locale.US);
			formatter.format("%d %s%s", period.length, period.units.getLongFormat(), suffix);
			return sb.toString();
		}
	}

    /**
     * Output periods in short format (e.g. "2w")
     */ 
	private class ShortFormat {
		private Period period;

		public ShortFormat(Period period) {
			this.period = period;
		}


		public String toString() {
			StringBuilder sb = new StringBuilder();
			Formatter formatter = new Formatter(sb, Locale.US);
			formatter.format("%d%s", period.length, period.units.getShortFormat());
			return sb.toString();
		}
	}


}
