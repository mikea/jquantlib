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
 Copyright (C) 2002, 2003, 2004 Ferdinando Ametrano
 Copyright (C) 2003, 2004 StatPro Italia srl

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

package org.jquantlib.termstructures.volatilities;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolation.Interpolation2D;
import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.termstructures.BlackVarianceTermStructure;
import org.jquantlib.util.Date;

/*! This class calculates time/strike dependent Black volatilities
using as input a matrix of Black volatilities observed in the
market.

The calculation is performed interpolating on the variance
surface.  Bilinear interpolation is used as default; this can
be changed by the setInterpolation() method.

\todo check time extrapolation

*/
public class BlackVarianceSurface extends BlackVarianceTermStructure {

	
      public enum Extrapolation { ConstantExtrapolation, InterpolatorDefaultExtrapolation };

      private DayCounter dayCounter_;
      private Date maxDate_;
      private Date[] dates_;
      private /*@Time*/ double[] times_;
      private /*@Price*/ double[] strikes_;
      private /*@Variance*/ double[][] variances_;
      private Interpolation2D varianceSurface_;
      private Extrapolation lowerExtrapolation_;
      private Extrapolation upperExtrapolation_;
  	  private Interpolator2D factory;

      public BlackVarianceSurface(final Date referenceDate,
              final Date[] dates,
              final /*@Price*/ double[] strikes, // FIXME: create new named type?
              final /*@Volatility*/ double[][] blackVolMatrix,
              final DayCounter dayCounter) {
    	  this(referenceDate, dates, strikes, blackVolMatrix, dayCounter, Extrapolation.InterpolatorDefaultExtrapolation, Extrapolation.InterpolatorDefaultExtrapolation);
      }
      
      
        public BlackVarianceSurface(final Date referenceDate,
                             final Date[] dates,
                             final /*@Price*/ double[] strikes,  // FIXME: create new named type?
                             final /*@Volatility*/ double[][] blackVolMatrix,
                             final DayCounter dayCounter,
                             final Extrapolation lowerExtrapolation,
                             final Extrapolation upperExtrapolation) {
            super(referenceDate);

            this.dayCounter_ = dayCounter;
            Date maxDate_ = dates[dates.length];
            this.strikes_ = strikes;
            this.lowerExtrapolation_ = lowerExtrapolation;
            this.upperExtrapolation_ = upperExtrapolation;

            if ( (dates.length!=blackVolMatrix[0].length) ) throw new IllegalArgumentException("mismatch between date vector and vol matrix colums");
            if ( (strikes_.length!=blackVolMatrix.length) ) throw new IllegalArgumentException("mismatch between money-strike vector and vol matrix rows");
            if ( (dates[0].le(referenceDate)) ) throw new IllegalArgumentException("cannot have dates[0] <= referenceDate");

            this.times_ = new /*@Time*/ double[dates.length+1];
            this.times_[0] = 0.0;
            this.variances_ = new /*@Variance*/ double[strikes_.length][dates.length+1];
            for (int i=0; i<blackVolMatrix.length; i++) {
                variances_[i][0] = 0.0;
            }
            for (int j=1; j<=blackVolMatrix[0].length; j++) {
                times_[j] = getTimeFromReference(dates[j-1]);
                if (! (times_[j]>times_[j-1]) ) throw new IllegalArgumentException("dates must be sorted unique!");
                for (int i=0; i<blackVolMatrix.length; i++) {
                    variances_[i][j] = times_[j] * blackVolMatrix[i][j-1] * blackVolMatrix[i][j-1];
                    if (! (variances_[i][j]>=variances_[i][j-1]) ) throw new IllegalArgumentException("variance must be non-decreasing");
                }
            }
            // default: bilinear interpolation
        	factory = new Bilinear();
        }
        
        
        public final DayCounter dayCounter() { return dayCounter_; }
        
        public final Date maxDate() {
            return maxDate_;
        }
        
        public final /*@Price*/ double minStrike() {
            return strikes_[0];
        }
        
        public final /*@Price*/ double maxStrike() {
            return strikes_[strikes_.length-1];
        }

        public void setInterpolation(final Interpolator i) {
            varianceSurface_ = factory.interpolate(
            		times_[0], times_[times_.length-1],
                    strikes_[0], strikes_[strikes_.length-1],
                    variances_);
            notifyObservers();
        }

// virtual void accept(AcyclicVisitor&);
//public void BlackVarianceSurface::accept(AcyclicVisitor& v) {
//    Visitor<BlackVarianceSurface>* v1 =
//        dynamic_cast<Visitor<BlackVarianceSurface>*>(&v);
//    if (v1 != 0)
//        v1->visit(*this);
//    else
//        BlackVarianceTermStructure::accept(v);
//}


        protected final /*@Variance*/ double blackVarianceImpl(final /*@Time*/ double t, final /*@Price*/ double strike) {

            if (t==0.0) return 0.0;

            // enforce constant extrapolation when required
            if (strike < strikes_[0] && lowerExtrapolation_ == ConstantExtrapolation)
                strike = strikes_[0];
            if (strike > strikes_[strikes_.length-1] && upperExtrapolation_ == ConstantExtrapolation)
                strike = strikes_[strikes_.length-1];

            if (t<=times_[times_.length-1])
                return varianceSurface_.getValue(t, strike, true);
            else { 
            	// t>times_.back() || extrapolate
            	/*@Time*/ double lastTime = times_[times_.length-1];
                return varianceSurface_.getValue(lastTime, strike, true) * t/lastTime;
            }
        }
	
}
