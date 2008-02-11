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

import java.util.List;

import javolution.util.FastTable;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolation.Interpolation2D;
import org.jquantlib.termstructures.BlackVarianceTermStructure;
import org.jquantlib.util.Date;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;
import org.jscience.mathematics.vector.Matrix;

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
      private FastTable<Date> dates_;
      private FastTable<Real> times_;
      private FastTable<Real> strikes_;
      private Matrix<Real> variances_;
      private Interpolation2D varianceSurface_;
      private Extrapolation lowerExtrapolation_;
      private Extrapolation upperExtrapolation_;

      public BlackVarianceSurface(final Date referenceDate,
              final List<Date> dates,
              final List<Real> strikes, // FIXME: create new named type?
              final Matrix<Real> blackVolMatrix,
              final DayCounter dayCounter) {
    	  this(referenceDate, dates, strikes, blackVolMatrix, dayCounter, Extrapolation.InterpolatorDefaultExtrapolation, Extrapolation.InterpolatorDefaultExtrapolation);
      }
      
      
        public BlackVarianceSurface(final Date referenceDate,
                             final List<Date> dates,
                             final List<Real> strikes,  // FIXME: create new named type?
                             final Matrix<Real> blackVolMatrix,
                             final DayCounter dayCounter,
                             final Extrapolation lowerExtrapolation,
                             final Extrapolation upperExtrapolation) {
            super(referenceDate);
            this.dayCounter_ = dayCounter;
            this.dates_ = new FastTable<Date>(dates);
            this.strikes_ = new FastTable<Real>(strikes);
            this.maxDate_ = this.dates_.getLast();
            this.lowerExtrapolation_ = lowerExtrapolation;
            this.upperExtrapolation_ = upperExtrapolation;

              if (dates.size()!=blackVolMatrix.numColumns()) throw new IllegalArgumentException("mismatch between date vector and vol matrix colums");
              if (strikes_.size()!=blackVolMatrix.numRows()) throw new IllegalArgumentException("mismatch between money-strike vector and vol matrix rows");
              if (this.dates_.getFirst().le(referenceDate)) throw new IllegalArgumentException("cannot have dates[0] <= referenceDate");

              DenseVector times_ = new DenseVector(dates.size()+1);
              times_.set(0, 0.0);

              variances_ = new DenseMatrix(strikes_.size(), dates.size()+1);
              for (int i=0; i<blackVolMatrix.numRows(); i++) {
                  variances_.set(i, 0, 0.0);
              }

              for (int j=1; j<=blackVolMatrix.numColumns(); j++) {
            	  double time = getTimeFromReference(dates.get(j-1)).doubleValue();
                  times_.set(j, time);
                  if (times_.get(j) <= times_.get(j-1)) throw new IllegalArgumentException("dates must be sorted unique!");
                  for (int i=0; i<blackVolMatrix.numRows(); i++) {
                	  double vol = blackVolMatrix.get(i, j-1);
                	  double var = time * vol * vol;
                      variances_.set(i, j, var);
                      if (variances_.get(i, j) < variances_.get(i, j-1)) throw new IllegalArgumentException("variance must be non-decreasing");
                  }
              }
              // default: bilinear interpolation
              setInterpolation(Bilinear);
          }
        
        
        public final DayCounter dayCounter() { return dayCounter_; }
        
        public final Date maxDate() {
            return maxDate_;
        }
        
        public final Real minStrike() {
            return strikes_.getFirst();
        }
        
        public final Real maxStrike() {
            return strikes_.getLast();
        }

        public void setInterpolation() {
        	setInterpolator(new Interpolator());
        }
        
        public void setInterpolation(final Interpolator i) {
            varianceSurface_ =
                i.interpolate(times_[0], times_[times_.length-1],
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


        protected final Real blackVarianceImpl(final /*@Time*/ double t, final Real strike) {

            if (t.isEQ(Real.ZERO)) return Real.ZERO;
 
            // enforce constant extrapolation when required
            if (strike.isLT(strikes_.getFirst()) && lowerExtrapolation_ == Extrapolation.ConstantExtrapolation) {
                strike = strikes_.getFirst();
            }
            if (strike.isGT(strikes_.getLast()) && upperExtrapolation_ == Extrapolation.ConstantExtrapolation) {
                strike = strikes_.getLast();
            }

            if (t.isLE(times_.getLast())) {
                return varianceSurface_(t, strike, true);
            } else {
                // t>times_.back() || extrapolate
                return varianceSurface_(times_.getLast(), strike, true) * t.getValue() / times_.getLast();
            }
        }
	
}
