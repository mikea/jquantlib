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

import org.jquantlib.Configuration;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolation.Interpolation;
import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.Date;
import org.jquantlib.util.Pair;

// TODO: Finish (Richard)
public final class InterpolatedForwardCurve<T extends Interpolator> extends ForwardRateStructure implements Curve {

	private Date[]				dates;
	private/* @Rate */double[]	data;			// FIXME: refactor: forwards
	private/* @Time */double[]	times;
	private Interpolator		interpolator;

	private Interpolation		interpolation;

	/**
	 * Default global settings
	 */
	private Settings			settings;

	public InterpolatedForwardCurve(final Date[] dates, final/* @Rate */double[] forwards, final DayCounter dayCounter,
			final T interpolator) {
		// FIXME: code review: calendar
		// FIXME: must check dates
		super(dates[0], Target.getCalendar(), dayCounter);
		this.dates = dates;
		this.data = forwards;
		this.interpolator = interpolator;

		if (dates.length <= 1) throw new IllegalArgumentException("too few dates"); // FIXME: message
		if (dates.length != data.length) throw new IllegalArgumentException("dates/yields count mismatch"); // FIXME: message

		// obtain reference to Settings
		settings = Configuration.getSystemConfiguration(null).getGlobalSettings();
		boolean isNegativeRates = settings.isNegativeRates();

		times = new /* @Time */double[dates.length];
		for (int i = 1; i < dates.length; i++) {
			if (dates[i].le(dates[i - 1])) { throw new IllegalArgumentException("invalid date (" + dates[i] + ", vs "
					+ dates[i - 1] + ")"); // FIXME: message
			}
			if (!isNegativeRates && (data[i] < 0.0)) { throw new IllegalArgumentException("negative forward"); // FIXME: message
			}
			times[i] = dayCounter.getYearFraction(dates[0], dates[i]);
		}

		this.interpolation = interpolator.interpolate(times, data);
		this.interpolation.update();
	}

	protected InterpolatedForwardCurve(final DayCounter dayCounter, final T interpolator) {
		super(dayCounter);
		this.interpolator = interpolator;
	}

	protected InterpolatedForwardCurve(final Date referenceDate, final DayCounter dayCounter, final T interpolator) {
		super(referenceDate, Target.getCalendar(), dayCounter); // FIXME: code review: calendar
		this.interpolator = interpolator;
	}

	protected InterpolatedForwardCurve(final int settlementDays, final Calendar calendar, final DayCounter dayCounter,
			final T interpolator) {
		super(settlementDays, calendar, dayCounter);
		this.interpolator = interpolator;
	}

	
	
	
	
	
	
	
	//
	// extends ForwardRateStructure
	//
	
	@Override
	protected final double discountImpl(double t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public final Date getMaxDate() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	//
	// implements PiecewiseYieldCurve.Curve
	//
	
	@Override
	public final Date[] dates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final double[] discounts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final Date maxDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final Pair<Date, Double>[] nodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final double[] times() {
		// TODO Auto-generated method stub
		return null;
	}


}



/*! \file forwardcurve.hpp
   \brief interpolated forward-rate structure
*/

//#ifndef quantlib_forward_curve_hpp
//#define quantlib_forward_curve_hpp
//
//#include <ql/termstructures/yieldcurves/forwardstructure.hpp>
//#include <ql/math/interpolations/backwardflatinterpolation.hpp>
//#include <vector>
//#include <utility>
//
//namespace QuantLib {
//
//   //! Term structure based on interpolation of forward rates
//   /*! \ingroup yieldtermstructures */
//   template <class Interpolator>
//   class InterpolatedForwardCurve : public ForwardRateStructure {
//     public:
//       // constructor
//       InterpolatedForwardCurve(const std::vector<Date>& dates,
//                                const std::vector<Rate>& forwards,
//                                const DayCounter& dayCounter,
//                                const Interpolator& interpolator
//                                                           = Interpolator());
//       //! \name Inspectors
//       //@{
//       Date maxDate() const;
//       const std::vector<Time>& times() const;
//       const std::vector<Date>& dates() const;
//       const std::vector<Rate>& forwards() const;
//       std::vector<std::pair<Date,Rate> > nodes() const;
//     protected:
//       InterpolatedForwardCurve(const DayCounter&,
//                                const Interpolator& interpolator
//                                                           = Interpolator());
//       InterpolatedForwardCurve(const Date& referenceDate,
//                                const DayCounter&,
//                                const Interpolator& interpolator
//                                                           = Interpolator());
//       InterpolatedForwardCurve(Natural settlementDays,
//                                const Calendar&,
//                                const DayCounter&,
//                                const Interpolator& interpolator
//                                                           = Interpolator());
//       Rate forwardImpl(Time t) const;
//       Rate zeroYieldImpl(Time t) const;
//       mutable std::vector<Date> dates_;
//       mutable std::vector<Time> times_;
//       mutable std::vector<Rate> data_;
//       mutable Interpolation interpolation_;
//       Interpolator interpolator_;
//   };
//
//   //! Term structure based on flat interpolation of forward rates
//   /*! \ingroup yieldtermstructures */
//   typedef InterpolatedForwardCurve<BackwardFlat> ForwardCurve;
//
//
//   // inline definitions
//
//   template <class T>
//   inline Date InterpolatedForwardCurve<T>::maxDate() const {
//       return dates_.back();
//   }
//
//   template <class T>
//   inline const std::vector<Time>& InterpolatedForwardCurve<T>::times()
//                                                                      const {
//       return times_;
//   }
//
//   template <class T>
//   inline const std::vector<Date>&
//   InterpolatedForwardCurve<T>::dates() const {
//       return dates_;
//   }
//
//   template <class T>
//   inline const std::vector<Rate>&
//   InterpolatedForwardCurve<T>::forwards() const {
//       return data_;
//   }
//
//   template <class T>
//   inline std::vector<std::pair<Date,Rate> >
//   InterpolatedForwardCurve<T>::nodes() const {
//       std::vector<std::pair<Date,Rate> > results(dates_.size());
//       for (Size i=0; i<dates_.size(); ++i)
//           results[i] = std::make_pair(dates_[i],data_[i]);
//       return results;
//   }
//
//   template <class T>
//   inline InterpolatedForwardCurve<T>::InterpolatedForwardCurve(
//                                                const DayCounter& dayCounter,
//                                                const T& interpolator)
//   : interpolator_(interpolator) {}
//
//   template <class T>
//   inline InterpolatedForwardCurve<T>::InterpolatedForwardCurve(
//                                                const Date& referenceDate,
//                                                const DayCounter& dayCounter,
//                                                const T& interpolator)
//   : ForwardRateStructure(referenceDate, Calendar(), dayCounter),
//     interpolator_(interpolator) {}
//
//   template <class T>
//   inline InterpolatedForwardCurve<T>::InterpolatedForwardCurve(
//                                                Natural settlementDays,
//                                                const Calendar& calendar,
//                                                const DayCounter& dayCounter,
//                                                const T& interpolator)
//   : ForwardRateStructure(settlementDays, calendar, dayCounter),
//     interpolator_(interpolator) {}
//
//   template <class T>
//   Rate InterpolatedForwardCurve<T>::forwardImpl(Time t) const {
//       return interpolation_(t, true);
//   }
//
//   template <class T>
//   Rate InterpolatedForwardCurve<T>::zeroYieldImpl(Time t) const {
//       if (t == 0.0)
//           return forwardImpl(0.0);
//       else
//           return interpolation_.primitive(t, true)/t;
//   }
//
//
//
//}
