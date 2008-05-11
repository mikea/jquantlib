/*
 Copyright (C) 2007 Richard Gomes

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
Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
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

package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;

// TODO : Finish (Richard)
public class ForwardRateStructure extends YieldTermStructure {

	public ForwardRateStructure(final DayCounter dc) {
		super(dc);
	}

	public ForwardRateStructure(final Date refDate, final Calendar cal, final DayCounter dc) {
		super(refDate, cal, dc);
	}

	public ForwardRateStructure(final int settlDays, final Calendar cal, final DayCounter dc) {
		super(settlDays, cal, dc);
	}
	
	
	@Override
	protected double discountImpl(double t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Date getMaxDate() {
		// TODO Auto-generated method stub
		return null;
	}

}



///*! \file forwardstructure.hpp
//   \brief Forward-based yield term structure
//*/
//
//#ifndef quantlib_forward_structure_hpp
//#define quantlib_forward_structure_hpp
//
//#include <ql/yieldtermstructure.hpp>
//
//namespace QuantLib {
//
//   //! %Forward-rate term structure
//   /*! This abstract class acts as an adapter to TermStructure allowing the
//       programmer to implement only the
//       <tt>forwardImpl(const Date&, bool)</tt> method in derived classes.
//       Zero yields and discounts are calculated from forwards.
//
//       Rates are assumed to be annual continuous compounding.
//
//       \ingroup yieldtermstructures
//   */
//   class ForwardRateStructure : public YieldTermStructure {
//     public:
//       /*! \name Constructors
//           See the TermStructure documentation for issues regarding
//           constructors.
//       */
//       //@{
//       ForwardRateStructure(const DayCounter& dayCounter =Actual365Fixed());
//       ForwardRateStructure(const Date& referenceDate,
//                            const Calendar& cal = Calendar(),
//                            const DayCounter& dayCounter =Actual365Fixed());
//       ForwardRateStructure(Natural settlementDays,
//                            const Calendar&,
//                            const DayCounter& dayCounter =Actual365Fixed());
//       //@}
//       virtual ~ForwardRateStructure() {}
//     protected:
//       //! \name YieldTermStructure implementation
//       //@{
//       /*! Returns the discount factor for the given date calculating it
//           from the instantaneous forward rate.
//       */
//       DiscountFactor discountImpl(Time) const;
//       //! instantaneous forward-rate calculation
//       virtual Rate forwardImpl(Time) const = 0;
//       /*! Returns the zero yield rate for the given date calculating it
//           from the instantaneous forward rate.
//
//           \warning This is just a default, highly inefficient and
//                    possibly wildly inaccurate implementation. Derived
//                    classes should implement their own zeroYield method.
//       */
//       virtual Rate zeroYieldImpl(Time) const;
//       //@}
//   };
//
//
//   // inline definitions
//
//   inline ForwardRateStructure::ForwardRateStructure(const DayCounter& dc)
//   : YieldTermStructure(dc) {}
//
//   inline ForwardRateStructure::ForwardRateStructure(const Date& refDate,
//                                                     const Calendar& cal,
//                                                     const DayCounter& dc)
//   : YieldTermStructure(refDate, cal, dc) {}
//
//   inline ForwardRateStructure::ForwardRateStructure(Natural settlDays,
//                                                     const Calendar& cal,
//                                                     const DayCounter& dc)
//   : YieldTermStructure(settlDays, cal, dc) {}
//
//   inline Rate ForwardRateStructure::zeroYieldImpl(Time t) const {
//       if (t == 0.0)
//           return forwardImpl(0.0);
//       // implement smarter integration if plan to use the following code
//       Rate sum = 0.5*forwardImpl(0.0);
//       Size N = 1000;
//       Time dt = t/N;
//       for (Time i=dt; i<t; i+=dt)
//           sum += forwardImpl(i);
//       sum += 0.5*forwardImpl(t);
//       return Rate(sum*dt/t);
//   }
//
//   inline DiscountFactor ForwardRateStructure::discountImpl(Time t) const {
//       Rate r = zeroYieldImpl(t);
//       return DiscountFactor(std::exp(-r*t));
//   }
//
//}
