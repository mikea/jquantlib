/*
 Copyright (C) 2008 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
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
 Copyright (C) 2002, 2003 Ferdinando Ametrano
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

package org.jquantlib.termstructures;

import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.NullCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.TypedVisitable;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

// FIXME: format comments, etc
public abstract class LocalVolTermStructure extends TermStructure implements TypedVisitable<TermStructure> {

        //! default constructor
        /*! \warning term structures initialized by means of this
                     constructor must manage their own reference date
                     by overriding the referenceDate() method.
        */
        public LocalVolTermStructure() {
			this(new Actual365Fixed());
		}

		public LocalVolTermStructure(final DayCounter dc) {
			super(dc);
		}


        //! initialize with a fixed reference date
        
		public LocalVolTermStructure(final Date referenceDate) {
        	this(referenceDate, new NullCalendar());
        }
        
        public LocalVolTermStructure(final Date referenceDate, final Calendar cal) {
        	this(referenceDate, cal, new Actual365Fixed());
        	
        }
        
        public LocalVolTermStructure(final Date referenceDate, final Calendar cal, final DayCounter dc) {
        	super(referenceDate, cal, dc);
        }
        
        
        //! calculate the reference date based on the global evaluation date

        public LocalVolTermStructure(int settlementDays) {
        	this(settlementDays, new NullCalendar());
        }
        
        public LocalVolTermStructure(int settlementDays, final Calendar cal) {
        	this(settlementDays, cal, new Actual365Fixed());
        }
        
        public LocalVolTermStructure(int settlementDays, final Calendar cal, final DayCounter dc) {
        	super(settlementDays, cal, dc);
        }

        
        //! \name Local Volatility
        
        public final /*@Volatility*/ double localVol(final Date d, final /*@Price*/ double underlyingLevel, boolean extrapolate) {
			/*@Time*/ double t = getTimeFromReference(d);
			checkRange(t, underlyingLevel, extrapolate);
			return localVolImpl(t, underlyingLevel);
		}

        public final /*@Volatility*/ double localVol(final /*@Time*/ double t, final /*@Price*/ double underlyingLevel, boolean extrapolate) {
			checkRange(t, underlyingLevel, extrapolate);
			return localVolImpl(t, underlyingLevel);
		}

        
        /**
         * @return the minimum strike for which the term structure can return vols
         */
        public abstract /*@Price*/ double getMinStrike();
        
        /**
         * @return the maximum strike for which the term structure can return vols
         */ 
        public abstract /*@Price*/ double getMaxStrike();



        
        
        /*! \name Calculations

            These methods must be implemented in derived classes to perform
            the actual volatility calculations. When they are called,
            range check has already been performed; therefore, they must
            assume that extrapolation is required.
        */
        //! local vol calculation
        protected abstract /*@Volatility*/ double localVolImpl(final /*@Time*/ double t, final /*@Price*/ double strike);

        

        private final void checkRange(final /*@Time*/ double t, final /*@Price*/ double strike, boolean extrapolate) {
        	super.checkRange(t, extrapolate);
        	/*@Price*/ double minStrike = getMinStrike();
        	/*@Price*/ double maxStrike = getMaxStrike();
        	if (! (extrapolate || allowsExtrapolation() || (strike >=  minStrike && strike <= maxStrike)) ) {
        		throw new ArithmeticException("strike (" + strike + ") is outside the curve domain [" + minStrike + "," + maxStrike + "]");
        	}
        }

    	//
    	// implements TypedVisitable
    	//
    	
    	@Override
    	public void accept(final TypedVisitor<TermStructure> v) {
    		Visitor<TermStructure> v1 = (v!=null) ? v.getVisitor(this.getClass()) : null;
    		if (v1 != null) {
    			v1.visit(this);
    		} else {
    			throw new UnsupportedOperationException("not a local-volatility term structure visitor");
    		}
    	}

}
