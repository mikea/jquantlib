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

package org.jquantlib.termstructures;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.number.DiscountFactor;
import org.jquantlib.number.Rate;
import org.jquantlib.number.Time;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;
import org.jscience.mathematics.number.Real;

// FIXME: comment this class
public class InterestRate {

	private Rate rate;
    private DayCounter dc;
    private Compounding compound;
    private boolean freqMakesSense;
    private int freq;
	
    /**
     * Default constructor returning a null interest rate.
     */
    public InterestRate() {
    	this.rate = null;
    }

    public InterestRate(final Rate r, final DayCounter dc, final Compounding comp) {
    	this(r, dc, comp, Frequency.Annual);
    }
    
    public InterestRate(final Rate r, final DayCounter dc, final Compounding comp, final Frequency freq) {
    	this.rate = r;
    	this.dc = dc;
    	this.compound = comp;
    	this.freqMakesSense = false;

        if (this.compound==Compounding.Compounded || this.compound==Compounding.SimpleThenCompounded) {
            freqMakesSense = true;
            if (! (freq!=Frequency.Once && freq!=Frequency.NoFrequency)) throw new IllegalArgumentException("frequency not allowed for this interest rate");
            this.freq = freq.toInteger();
        }
    }
    
    /**
     * Compound factor implied by the rate compounded at time t.
     * 
     * <p><b>Note:</b> Time must be measured using InterestRate's own day counter.
     * 
     * @return the compound (a.k.a capitalization) factor
     *         implied by the rate compounded at time t.
     */
    public final Real compoundFactor(final Time time) {
    	double t = time.doubleValue();
    	if (t<0.0) throw new IllegalArgumentException("negative time not allowed");
        if (rate==null) throw new IllegalArgumentException("null interest rate");
    	double r = rate.doubleValue();

    	if (compound==Compounding.Simple) {
        	// 1+r*t
        	return new Real( 1.0+r*t );
        } else if (compound==Compounding.Compounded) {
        	// (1+r/f)^(f*t)
        	return new Real( Math.pow( (1+r/freq), (freq*t) ) );
        } else if (compound==Compounding.Continuous) {
        	// e^(r*t)
        	return new Real( Math.exp( (r*t) ) );
        } else if (compound==Compounding.SimpleThenCompounded) {
            if (t<(1/freq))
            	// 1+r*t
            	return new Real( 1.0+r*t );
            else
            	// (1+(r/f))^(f*t)
            	return new Real( Math.pow( (1+r/freq), (freq*t) ) );
        } else {
            throw new IllegalArgumentException("unknown compounding convention");
        }
    }

    public final Rate getRate() {
    	return this.rate;
    }

    public final DayCounter getDayCounter() {
    	return this.dc;
    }
    
    public final Compounding getCompounding() {
    	return this.compound;
    }
    
    public final Frequency getFrequency() {
        return freqMakesSense ? Frequency.valueOf(this.freq) : Frequency.NoFrequency;
    }
    
    /**
     * Discount factor implied by the rate compounded at time t.
     * 
     * <p><b>Note:</b>Time must be measured using InterestRate's own day counter.
     */
    public final DiscountFactor getDiscountFactor(final Time t) {
    	double factor = compoundFactor(t).doubleValue();
        return new DiscountFactor(1/(factor));
    }

    
    public final DiscountFactor getDiscountFactor(final Date d1, final Date d2) {
    	return getDiscountFactor(d1, d2, new Date());
    }

    public final DiscountFactor getDiscountFactor(final Date d1, final Date d2, final Date refStart) {
    	return getDiscountFactor(d1, d2, refStart, new Date());
    }

    /**
     * Compound factor implied by the rate compounded between two dates
     * 
     * @return the compound (a.k.a capitalization) factor
     *         implied by the rate compounded between two dates.
     */
    public final DiscountFactor getDiscountFactor(final Date d1, final Date d2, final Date refStart, final Date refEnd) {
        Time t = this.dc.getYearFraction(d1, d2, refStart, refEnd);
        return getDiscountFactor(t);
    }

    public final InterestRate getEquivalentRate(final Time t, final Compounding comp) {
    	return getEquivalentRate(t, comp, Frequency.Annual);
    }

    /**
     * Returns equivalent interest rate for a compounding period t.
     * The resulting InterestRate shares the same implicit
     * day-counting rule of the original InterestRate instance.
     * 
     * <p>Time must be measured using the InterestRate's own day counter.
     * 
     * @return equivalent interest rate for a compounding period t.
     */
    public final InterestRate getEquivalentRate(final Time t, final Compounding comp, final Frequency freq) {
        return getImpliedRate(compoundFactor(t), t, this.dc, comp, freq);
    }

    public final InterestRate equivalentRate(
			final Date d1,
			final Date d2,
			final DayCounter resultDC,
            final Compounding comp) {
    	return getEquivalentRate(d1, d2, resultDC, comp, Frequency.Annual);
    }

    /**
     * Returns equivalent rate for a compounding period between two dates.
     * The resulting rate is calculated taking the required
     * day-counting rule into account.
     */
    public final InterestRate getEquivalentRate(
    			final Date d1,
    			final Date d2,
    			final DayCounter resultDC,
                final Compounding comp,
                final Frequency freq) {
    	if (d1.le(d2)) throw new IllegalArgumentException("d1 ("+d1+") later than or equal to d2 ("+d2+")");
        Time t1 = this.dc.getYearFraction(d1, d2);
        Time t2 = resultDC.getYearFraction(d1, d2);
        return getImpliedRate(compoundFactor(t1), t2, resultDC, comp, freq);
    }
    
    
    
    
    
    /**
     * Implied interest rate for a given compound factor at a given time.
     * The resulting InterestRate has the day-counter provided as input.
     * 
     * <p><b>Note:</b> Time must be measured using the day-counter provided as input. 
     */
    static public InterestRate getImpliedRate(
    			final Real compound, 
    			final Time time,
    			final DayCounter resultDC,
    			final Compounding comp, 
    			final Frequency freq) {
    	
    	double c = compound.doubleValue();
        double t = time.doubleValue();
        double f = freq.toInteger();
        if (c<=0.0) throw new IllegalArgumentException("positive compound factor required");
        if (t<=0.0) throw new IllegalArgumentException("positive time required");
        
        Rate rate;
        switch (comp) {
          case Simple:
        	// rate = (compound - 1)/time  
            rate = new Rate( (c-1)/t );
            break;
          case Compounded:
        	// rate = (compound^(1/(f*t))-1)*f
        	rate = new Rate( ( Math.pow( c,(1/(f*t)) )-1 )*f );
            break;
          case Continuous:
        	  // rate = log(compound)/t
        	rate = new Rate( Math.log(c)/t );
            break;
          case SimpleThenCompounded:
            if (t<=(1/f))
            	// rate = (compound - 1)/time  
                rate = new Rate( (c-1)/t );
            else
            	// rate = (compound^(1/(f*t))-1)*f
            	rate = new Rate( ( Math.pow( c,(1/(f*t)) )-1 )*f );
            break;
          default:
            throw new IllegalArgumentException("unknown compounding convention ("+comp+")");
        }
        return new InterestRate(rate, resultDC, comp, freq);
    }

    static public InterestRate getImpliedRate(
    			final Real compound,
    			final Time t,
    			final DayCounter resultDC,
    			final Compounding comp) {
    	return getImpliedRate(compound, t, resultDC, comp, Frequency.Annual);
    }
    
    static public InterestRate getImpliedRate(
    			final Real compound,
    			final Date d1,
    			final Date d2,
    			final DayCounter resultDC,
    			final Compounding comp) {
    	return getImpliedRate(compound, d1, d2, resultDC, comp, Frequency.Annual);
    }

    /**
     * Implied rate for a given compound factor between two dates.
     * The resulting rate is calculated taking the required
     * day-counting rule into account.
     */
    static public InterestRate getImpliedRate(
    			final Real compound,
    			final Date d1,
    			final Date d2,
    			final DayCounter resultDC,
    			final Compounding comp,
    			final Frequency freq) {
    	if (d2.le(d1)) throw new IllegalArgumentException("d1 ("+d1+") later than or equal to d2 ("+d2+")");
        Time t = resultDC.getYearFraction(d1, d2);
        return getImpliedRate(compound, t, resultDC, comp, freq);
    }
    

    
    public String toString() {
    	if (rate==null) return "null interest rate";
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append(rate).append(' ').append(dc).append(' ');
    	if (compound==Compounding.Simple) {
    		sb.append("simple compounding");
    	} else if (compound==Compounding.Compounded) {
    		if ((freq==Frequency.NoFrequency.toInteger()) || (freq==Frequency.Once.toInteger())) {
    			throw new IllegalArgumentException(freq+" frequency not allowed for this interest rate");
    		} else {
    			sb.append(freq+" compounding");
    		}
    	} else if (compound==Compounding.Continuous) {
    		sb.append("continuous compounding");
    	} else if (compound==Compounding.SimpleThenCompounded) {
    		if ((freq==Frequency.NoFrequency.toInteger()) || (freq==Frequency.Once.toInteger())) {
    			throw new IllegalArgumentException(freq+" frequency not allowed for this interest rate");
    		} else {
    			sb.append("simple compounding up to "+(12/freq)+" months, then "+freq+" compounding");
    		}
    	} else {
    		throw new IllegalArgumentException("unknown compounding convention ("+compound+")");
    	}
    	return sb.toString();
    }

}
