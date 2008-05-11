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

import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Date;

public class ZeroYieldStructure extends YieldTermStructure {

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



///*! \file zeroyieldstructure.hpp
//   \brief Zero-yield based term structure
//*/
//
//#ifndef quantlib_zero_yield_structure_hpp
//#define quantlib_zero_yield_structure_hpp
//
//#include <ql/yieldtermstructure.hpp>
//
//namespace QuantLib {
//
//   //! Zero-yield term structure
//   /*! This abstract class acts as an adapter to YieldTermStructure
//       allowing the programmer to implement only the
//       <tt>zeroYieldImpl(Time, bool)</tt> method in derived classes.
//       Discount and forward are calculated from zero yields.
//
//       Rates are assumed to be annual continuous compounding.
//
//       \ingroup yieldtermstructures
//   */
//   class ZeroYieldStructure : public YieldTermStructure {
//     public:
//       /*! \name Constructors
//           See the TermStructure documentation for issues regarding
//           constructors.
//       */
//       //@{
//       ZeroYieldStructure(const DayCounter& dc = Actual365Fixed());
//       ZeroYieldStructure(const Date& referenceDate,
//                          const Calendar& calendar = Calendar(),
//                          const DayCounter& dc = Actual365Fixed());
//       ZeroYieldStructure(Natural settlementDays,
//                          const Calendar&,
//                          const DayCounter& dc = Actual365Fixed());
//       //@}
//       virtual ~ZeroYieldStructure() {}
//     protected:
//       //! \name YieldTermStructure implementation
//       //@{
//       /*! Returns the discount factor for the given date calculating it
//           from the zero yield.
//       */
//       DiscountFactor discountImpl(Time) const;
//       //! zero-yield calculation
//       virtual Rate zeroYieldImpl(Time) const = 0;
//       //@}
//   };
//
//   // inline definitions
//
//   inline ZeroYieldStructure::ZeroYieldStructure(const DayCounter& dc)
//   : YieldTermStructure(dc) {}
//
//   inline ZeroYieldStructure::ZeroYieldStructure(const Date& refDate,
//                                                 const Calendar& cal,
//                                                 const DayCounter& dc)
//   : YieldTermStructure(refDate, cal, dc) {}
//
//   inline ZeroYieldStructure::ZeroYieldStructure(Natural settlementDays,
//                                                 const Calendar& cal,
//                                                 const DayCounter& dc)
//   : YieldTermStructure(settlementDays, cal, dc) {}
//
//   inline DiscountFactor ZeroYieldStructure::discountImpl(Time t) const {
//       Rate r = zeroYieldImpl(t);
//       return DiscountFactor(std::exp(-r*t));
//   }
//
//}
