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

package org.jquantlib.termstructures;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.FunctionDouble;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;

// FIXME: comment this class
public class InterestRate implements FunctionDouble {

	private /*@Rate*/ double rate;
    private DayCounter dc;
    private Compounding compound;
    private boolean freqMakesSense;
    private int freq;
	
    /**
     * Default constructor returning a null interest rate.
     */
    public InterestRate() {
    	this.rate = 0.0;
    }

    public InterestRate(final /*@Rate*/ double r, final DayCounter dc, final Compounding comp) {
    	this(r, dc, comp, Frequency.ANNUAL);
    }
    
    public InterestRate(final /*@Rate*/ double r, final DayCounter dc, final Compounding comp, final Frequency freq) {
    	this.rate = r;
    	this.dc = dc;
    	this.compound = comp;
    	this.freqMakesSense = false;

        if (this.compound==Compounding.COMPOUNDED || this.compound==Compounding.SIMPLE_THEN_COMPOUNDED) {
            freqMakesSense = true;
            if (! (freq!=Frequency.ONCE && freq!=Frequency.NO_FREQUENCY)) throw new IllegalArgumentException("frequency not allowed for this interest rate");
            this.freq = freq.toInteger();
        }
    }
    
    /**
     * Compound factor implied by the rate compounded at time t.
     * 
     * @note Time must be measured using InterestRate's own day counter.
     * 
     * @return the compound (a.k.a capitalization) factor
     *         implied by the rate compounded at time t.
     */
    public final /*@CompoundFactor*/ double compoundFactor(final /*@Time*/ double time) {
    	/*@Time*/ double t = time;
    	if (t<0.0) throw new IllegalArgumentException("negative time not allowed");
        if (rate<0.0) throw new IllegalArgumentException("null interest rate");
        
    	/*@Rate*/ double r = rate;

    	if (compound==Compounding.SIMPLE) {
        	// 1+r*t
        	return 1.0+r*t;
        } else if (compound==Compounding.COMPOUNDED) {
        	// (1+r/f)^(f*t)
        	return Math.pow( (1+r/freq), (freq*t) );
        } else if (compound==Compounding.CONTINUOUS) {
        	// e^(r*t)
        	return Math.exp( (r*t) );
        } else if (compound==Compounding.SIMPLE_THEN_COMPOUNDED) {
            if (t < (1 / (double)freq) )
            	// 1+r*t
            	return 1.0+r*t;
            else
            	// (1+(r/f))^(f*t)
            	return Math.pow( (1+r/freq), (freq*t) );
        } else {
            throw new IllegalArgumentException("unknown compounding convention");
        }
    }

    public final DayCounter dayCounter() {
    	return this.dc;
    }
    
    public final Compounding compounding() {
    	return this.compound;
    }
    
    public final Frequency frequency() {
        return freqMakesSense ? Frequency.valueOf(this.freq) : Frequency.NO_FREQUENCY;
    }
    
    /**
     * Discount factor implied by the rate compounded at time t.
     * 
     * @note Time double must be measured using InterestRate's own day counter.
     */
    public final /*@DiscountFactor*/ double discountFactor(final /*@Time*/ double t) {
    	/*@DiscountFactor*/ double factor = compoundFactor(t);
        return 1/factor;
    }

    
    public final /*@DiscountFactor*/ double discountFactor(final Date d1, final Date d2) {
    	return discountFactor(d1, d2, Date.NULL_DATE);
    }

    public final /*@DiscountFactor*/ double discountFactor(final Date d1, final Date d2, final Date refStart) {
    	return discountFactor(d1, d2, refStart, Date.NULL_DATE);
    }

    /**
     * Compound factor implied by the rate compounded between two dates
     * 
     * @return the compound (a.k.a capitalization) factor
     *         implied by the rate compounded between two dates.
     */
    public final /*@DiscountFactor*/ double discountFactor(final Date d1, final Date d2, final Date refStart, final Date refEnd) {
        /*@Time*/ double t = this.dc.getYearFraction(d1, d2, refStart, refEnd);
        return discountFactor(t);
    }

    public final InterestRate equivalentRate(final /*@Time*/ double t, final Compounding comp) {
    	return equivalentRate(t, comp, Frequency.ANNUAL);
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
    public final InterestRate equivalentRate(final /*@Time*/ double t, final Compounding comp, final Frequency freq) {
        return impliedRate(compoundFactor(t), t, this.dc, comp, freq);
    }

    public final InterestRate equivalentRate(
			final Date d1,
			final Date d2,
			final DayCounter resultDC,
            final Compounding comp) {
    	return equivalentRate(d1, d2, resultDC, comp, Frequency.ANNUAL);
    }

    /**
     * Returns equivalent rate for a compounding period between two dates.
     * The resulting rate is calculated taking the required
     * day-counting rule into account.
     */
    public final InterestRate equivalentRate(
    			final Date d1,
    			final Date d2,
    			final DayCounter resultDC,
                final Compounding comp,
                final Frequency freq) {
    	if (d1.le(d2)) throw new IllegalArgumentException("d1 ("+d1+") later than or equal to d2 ("+d2+")");
        /*@Time*/ double t1 = this.dc.getYearFraction(d1, d2);
        /*@Time*/ double t2 = resultDC.getYearFraction(d1, d2);
        return impliedRate(compoundFactor(t1), t2, resultDC, comp, freq);
    }
    
    
    
    
    
    /**
     * Implied interest rate for a given compound factor at a given time.
     * The resulting InterestRate has the day-counter provided as input.
     * 
     * @note Time must be measured using the day-counter provided as input. 
     */
    static public InterestRate impliedRate(
    			final /*@CompoundFactor*/ double c, 
    			final /*@Time*/ double time,
    			final DayCounter resultDC,
    			final Compounding comp, 
    			final Frequency freq) {
    	
        /*@Time*/ double t = time;
        double f = freq.toInteger();
        if (c<=0.0) throw new IllegalArgumentException("positive compound factor required");
        if (t<=0.0) throw new IllegalArgumentException("positive time required");
        
        /*@Rate*/ double rate;
        switch (comp) {
          case SIMPLE:
        	// rate = (compound - 1)/time  
            rate = (c-1)/t;
            break;
          case COMPOUNDED:
        	// rate = (compound^(1/(f*t))-1)*f
        	rate = ( Math.pow( c,(1/(f*t)) )-1 )*f;
            break;
          case CONTINUOUS:
        	  // rate = log(compound)/t
        	rate = Math.log(c)/t;
            break;
          case SIMPLE_THEN_COMPOUNDED:
            if (t<=(1/f))
            	// rate = (compound - 1)/time  
                rate = (c-1)/t;
            else
            	// rate = (compound^(1/(f*t))-1)*f
            	rate = ( Math.pow( c,(1/(f*t)) )-1 )*f;
            break;
          default:
            throw new IllegalArgumentException("unknown compounding convention ("+comp+")");
        }
        return new InterestRate(rate, resultDC, comp, freq);
    }

    static public InterestRate impliedRate(
    			final /*@CompoundFactor*/ double compound,
    			final /*@Time*/ double t,
    			final DayCounter resultDC,
    			final Compounding comp) {
    	return impliedRate(compound, t, resultDC, comp, Frequency.ANNUAL);
    }
    
    static public InterestRate impliedRate(
    			final /*@CompoundFactor*/ double compound,
    			final Date d1,
    			final Date d2,
    			final DayCounter resultDC,
    			final Compounding comp) {
    	return impliedRate(compound, d1, d2, resultDC, comp, Frequency.ANNUAL);
    }

    /**
     * Implied rate for a given compound factor between two dates.
     * The resulting rate is calculated taking the required
     * day-counting rule into account.
     */
    static public InterestRate impliedRate(
    			final /*@CompoundFactor*/ double compound,
    			final Date d1,
    			final Date d2,
    			final DayCounter resultDC,
    			final Compounding comp,
    			final Frequency freq) {
    	if (d2.le(d1)) throw new IllegalArgumentException("d1 ("+d1+") later than or equal to d2 ("+d2+")");
        /*@Time*/ double t = resultDC.getYearFraction(d1, d2);
        return impliedRate(compound, t, resultDC, comp, freq);
    }
    

    
    public String toString() {
    	if (rate==0.0) return "null interest rate";
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append(rate).append(' ').append(dc).append(' ');
    	if (compound==Compounding.SIMPLE) {
    		sb.append("simple compounding");
    	} else if (compound==Compounding.COMPOUNDED) {
    		if ((freq==Frequency.NO_FREQUENCY.toInteger()) || (freq==Frequency.ONCE.toInteger())) {
    			throw new IllegalArgumentException(freq+" frequency not allowed for this interest rate");
    		} else {
    			sb.append(freq+" compounding");
    		}
    	} else if (compound==Compounding.CONTINUOUS) {
    		sb.append("continuous compounding");
    	} else if (compound==Compounding.SIMPLE_THEN_COMPOUNDED) {
    		if ((freq==Frequency.NO_FREQUENCY.toInteger()) || (freq==Frequency.ONCE.toInteger())) {
    			throw new IllegalArgumentException(freq+" frequency not allowed for this interest rate");
    		} else {
    			sb.append("simple compounding up to "+(12/freq)+" months, then "+freq+" compounding");
    		}
    	} else {
    		throw new IllegalArgumentException("unknown compounding convention ("+compound+")");
    	}
    	return sb.toString();
    }


    //
    // implements FunctionDouble
    //
    
    @Override
    public final /*@Rate*/ double evaluate() {
    	return this.rate;
    }

}
