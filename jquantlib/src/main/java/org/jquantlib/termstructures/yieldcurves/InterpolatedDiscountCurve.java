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
Copyright (C) 2002, 2003 Decillion Pty(Ltd)
Copyright (C) 2005, 2006 StatPro Italia srl

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

import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Date;
import org.jquantlib.util.Pair;

// TODO: Finish (Richard)

public final class InterpolatedDiscountCurve<I extends Interpolator> extends YieldTermStructure implements YieldCurve {

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

	@Override
	public double[] getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date[] getDates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Date, Double>[] getNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getTimes() {
		// TODO Auto-generated method stub
		return null;
	}


}


///*! \file discountcurve.hpp
//   \brief interpolated discount factor structure
//*/
//
//#ifndef quantlib_discount_curve_hpp
//#define quantlib_discount_curve_hpp
//
//#include <ql/yieldtermstructure.hpp>
//#include <ql/math/interpolations/loglinearinterpolation.hpp>
//#include <vector>
//#include <utility>
//
//namespace QuantLib {
//
//   //! Term structure based on interpolation of discount factors
//   /*! \ingroup yieldtermstructures */
//   template <class Interpolator>
//   class InterpolatedDiscountCurve : public YieldTermStructure {
//     public:
//       InterpolatedDiscountCurve(const std::vector<Date>& dates,
//                                 const std::vector<DiscountFactor>& dfs,
//                                 const DayCounter& dayCounter,
//                                 const Calendar& cal = Calendar(),
//                                 const Interpolator& interpolator
//                                                           = Interpolator());
//       //! \name Inspectors
//       //@{
//       Date maxDate() const;
//       const std::vector<Time>& times() const;
//       const std::vector<Date>& dates() const;
//       const std::vector<DiscountFactor>& discounts() const;
//       std::vector<std::pair<Date,DiscountFactor> > nodes() const;
//       //@}
//     protected:
//       InterpolatedDiscountCurve(const DayCounter&,
//                                 const Interpolator& interpolator
//                                                           = Interpolator());
//       InterpolatedDiscountCurve(const Date& referenceDate,
//                                 const DayCounter&,
//                                 const Interpolator& interpolator
//                                                           = Interpolator());
//       InterpolatedDiscountCurve(Natural settlementDays,
//                                 const Calendar&,
//                                 const DayCounter&,
//                                 const Interpolator& interpolator
//                                                           = Interpolator());
//       DiscountFactor discountImpl(Time) const;
//       mutable std::vector<Date> dates_;
//       mutable std::vector<Time> times_;
//       mutable std::vector<DiscountFactor> data_;
//       mutable Interpolation interpolation_;
//       Interpolator interpolator_;
//   };
//
//   //! Term structure based on log-linear interpolation of discount factors
//   /*! Log-linear interpolation guarantees piecewise-constant forward
//       rates.
//
//       \ingroup yieldtermstructures
//   */
//   typedef InterpolatedDiscountCurve<LogLinear> DiscountCurve;
//
//
//   // inline definitions
//
//   template <class T>
//   inline Date InterpolatedDiscountCurve<T>::maxDate() const {
//       return dates_.back();
//   }
//
//   template <class T>
//   inline const std::vector<Time>&
//   InterpolatedDiscountCurve<T>::times() const {
//       return times_;
//   }
//
//   template <class T>
//   inline const std::vector<Date>&
//   InterpolatedDiscountCurve<T>::dates() const {
//       return dates_;
//   }
//
//   template <class T>
//   inline const std::vector<DiscountFactor>&
//   InterpolatedDiscountCurve<T>::discounts() const {
//       return data_;
//   }
//
//   template <class T>
//   inline std::vector<std::pair<Date,DiscountFactor> >
//   InterpolatedDiscountCurve<T>::nodes() const {
//       std::vector<std::pair<Date,DiscountFactor> > results(dates_.size());
//       for (Size i=0; i<dates_.size(); ++i)
//           results[i] = std::make_pair(dates_[i],data_[i]);
//       return results;
//   }
//
//   template <class T>
//   inline InterpolatedDiscountCurve<T>::InterpolatedDiscountCurve(
//                                                const DayCounter& dayCounter,
//                                                const T& interpolator)
//   : YieldTermStructure(dayCounter), interpolator_(interpolator) {}
//
//   template <class T>
//   inline InterpolatedDiscountCurve<T>::InterpolatedDiscountCurve(
//                                                const Date& referenceDate,
//                                                const DayCounter& dayCounter,
//                                                const T& interpolator)
//   : YieldTermStructure(referenceDate, Calendar(), dayCounter),
//     interpolator_(interpolator) {}
//
//   template <class T>
//   inline InterpolatedDiscountCurve<T>::InterpolatedDiscountCurve(
//                                                Natural settlementDays,
//                                                const Calendar& calendar,
//                                                const DayCounter& dayCounter,
//                                                const T& interpolator)
//   : YieldTermStructure(settlementDays, calendar, dayCounter),
//     interpolator_(interpolator) {}
//
//
//   template <class T>
//   inline DiscountFactor InterpolatedDiscountCurve<T>::discountImpl(Time t)
//                                                                      const {
//       return interpolation_(t, true);
//   }
//
//   // template definitions
//
//   template <class T>
//   InterpolatedDiscountCurve<T>::InterpolatedDiscountCurve(
//                                const std::vector<Date>& dates,
//                                const std::vector<DiscountFactor>& discounts,
//                                const DayCounter& dayCounter,
//                                const Calendar& cal,
//                                const T& interpolator)
//   : YieldTermStructure(dates[0], cal, dayCounter),
//     dates_(dates), data_(discounts),
//     interpolator_(interpolator) {
//       QL_REQUIRE(!dates_.empty(), "no input dates given");
//       QL_REQUIRE(!data_.empty(), "no input discount factors given");
//       QL_REQUIRE(data_.size() == dates_.size(),
//                  "dates/discount factors count mismatch");
//       QL_REQUIRE(data_[0] == 1.0,
//                  "the first discount must be == 1.0 "
//                  "to flag the corrsponding date as settlement date");
//
//       times_.resize(dates_.size());
//       times_[0] = 0.0;
//       for (Size i = 1; i < dates_.size(); i++) {
//           QL_REQUIRE(dates_[i] > dates_[i-1],
//                      "invalid date (" << dates_[i] << ", vs "
//                      << dates_[i-1] << ")");
//           QL_REQUIRE(data_[i] > 0.0, "negative discount");
//           times_[i] = dayCounter.yearFraction(dates_[0], dates_[i]);
//       }
//
//       interpolation_ = interpolator_.interpolate(times_.begin(),
//                                                  times_.end(),
//                                                  data_.begin());
//       interpolation_.update();
//   }
//
//}
