/*
Copyright (C) 2009 Ueli Hofstetter
Copyright (C) 2009 John Martin

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

/*
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005 StatPro Italia srl
 Copyright (C) 2006, 2007 Cristina Duminuco
 Copyright (C) 2006, 2007 Giorgio Facchinetti
 Copyright (C) 2006 Mario Pucci
 Copyright (C) 2007 Ferdinando Ametrano

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

package org.jquantlib.cashflow;

import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.indexes.InterestRateIndex;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.lang.reflect.TypeTokenTree;
import org.jquantlib.math.Constants;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Schedule;

/**
 * Cash flow vector builder
 *
 * @author Ueli Hofstetter
 * @author John Martin
 * @author Zahid Hussain
 */
//ZH: Fixed code to match QL097
//TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class FloatingLeg< InterestRateIndexType extends InterestRateIndex,
                          FloatingCouponType extends FloatingRateCoupon,
                          CappedFlooredCouponType
                         > extends Leg {
	//Make compiler happy
	private static final long serialVersionUID = 1L;
	
	public FloatingLeg(
            final Array nominals,
            final Schedule schedule,
            final InterestRateIndexType index,
            final DayCounter paymentDayCounter,
            final BusinessDayConvention paymentAdj,
            final Array fixingDays,
            final Array gearings,
            final Array spreads,
            final Array caps,
            final Array floors,
            final boolean isInArrears,
            final boolean isZero) {
		
        super(schedule.size() - 1);

        int n = schedule.size()-1;
        QL.require(nominals != null && nominals.size() <= n,
                   "too many nominals (" + nominals.size() +
                   "), only " + n + " required");
        QL.require(gearings != null && gearings.size()<=n,
                   "too many gearings (" + gearings.size() +
                   "), only " + n + " required");
        QL.require(spreads != null && spreads.size()<=n,
                   "too many spreads (" + spreads.size() +
                   "), only " + n + " required");
        QL.require(caps != null && caps.size()<=n,
                   "too many caps (" + caps.size() +
                   "), only " + n + " required");
        QL.require(floors != null && floors.size()<=n,
                   "too many floors (" + floors.size() +
                   "), only " + n + " required");
        QL.require(!isZero || !isInArrears,
                   "in-arrears and zero features are not compatible");

        // the following is not always correct
        Calendar calendar = schedule.calendar();

        Date refStart, start, refEnd, end;
        Date lastPaymentDate = calendar.adjust(schedule.date(n), paymentAdj);

        for (int i=0; i<n; ++i) {
            refStart = start = schedule.date(i);
            refEnd   =   end = schedule.date(i+1);
            Date paymentDate =
                isZero ? lastPaymentDate : calendar.adjust(end, paymentAdj);
            if (i==0   && !schedule.isRegular(i+1)) {
                BusinessDayConvention bdc = schedule.businessDayConvention();
                refStart = calendar.adjust(end.sub(schedule.tenor()), bdc);
            }
            if (i==n-1 && !schedule.isRegular(i+1)) {
                BusinessDayConvention bdc = schedule.businessDayConvention();
                refEnd = calendar.adjust(start.add(schedule.tenor()), bdc);
            }
            if (Detail.get(gearings, i, 1.0) == 0.0) { // fixed coupon
                add(new FixedRateCoupon(Detail.get(nominals, i, 1.0),
                                    paymentDate,
                                    Detail.effectiveFixedRate(spreads,caps,floors,i),
                                    paymentDayCounter,
                                    start, end, refStart, refEnd));
            }
           else if (Detail.noOption(caps, floors, i)) { //// floating coupon
        	   // get the generic type
        	   final Class<?> fctklass = new TypeTokenTree(this.getClass()).getElement(1);
        	   // construct a new instance using reflection. first get the
        	   FloatingCouponType frc;
        	   try {
        		   frc = (FloatingCouponType) fctklass.getConstructor(
                      Date.class, // paymentdate
                      double.class, // nominal
                      Date.class, // start date
                      Date.class, // enddate
                      int.class, // fixing days
                      IborIndex.class, // IbotIndex
                      double.class, // gearing
                      double.class, // spread
                      Date.class, // refperiodstart
                      Date.class, // refperiodend
                      DayCounter.class,// daycounter
                      boolean.class) // inarrears
                      // then create a new instance
                      .newInstance(
                              paymentDate,
                              Detail.get(nominals, i, 1.0),
                              start,
                              end,
                              (int)Detail.get(fixingDays, i, index.fixingDays()),
                              index,
                              Detail.get(gearings, i, 1.0),
                              Detail.get(spreads, i, 0.0), 
                              refStart,
                              refEnd, 
                              paymentDayCounter, 
                              isInArrears);
        	   } catch (Exception e) {
        		   throw new LibraryException(
        		   "Couldn't construct new instance from generic type for floating coupon"); // QA:[RG]::verified
        	   }
        	   add(frc);
          }
         else {
                final Class<?> cfcklass = new TypeTokenTree(this.getClass()).getElement(2);
                CappedFlooredCouponType cfctc;
                try {
                    cfctc = (CappedFlooredCouponType) cfcklass.getConstructor(
                            Date.class, // paymentdate
                            double.class, // nominal
                            Date.class, // start date
                            Date.class, // enddate
                            int.class, // fixing days
                            IborIndex.class, // IborIndex
                            double.class, // gearing
                            double.class, // spread
                            double.class, //caps
                            double.class, //floors
                            Date.class, // refperiodstart
                            Date.class, // refperiodend
                            DayCounter.class,// daycounter
                            boolean.class) // inarrears
                            // then create a new instance
                            .newInstance (
                                    paymentDate,
                                    Detail.get(nominals,i, 1.0),
                                    start,
                                    end,
                                    (int) Detail.get(fixingDays, i, index.fixingDays()),
                                    index,
                                    Detail.get(gearings, i, 1.0),
                                    Detail.get(spreads, i, 0.0),
                                    Detail.get(caps, i, Constants.NULL_RATE),
                                    Detail.get(floors, i, Constants.NULL_RATE),
                                    refStart,
                                    refEnd,
                                    paymentDayCounter,
                                    isInArrears);
                } catch (final Exception e) {
                    throw new LibraryException("Couldn't construct new instance from generic type:CappedFlooredCouponType");
                }
                add((CashFlow) cfctc);
            }
        }
    }



    private static class Detail {

        //TODO: Do we really need this generic method?
        // its a one to one translation from quantlib.
        // we could use the implementations below instead
        //XXX : unused
        //        public static <T extends Number, U extends Number> T get(
        //                final ArrayList<T> v,
        //                final int i,
        //                final U defaultValue) {
        //            if (v == null || v.size() == 0) {
        //                return (T) defaultValue;
        //            } else if (i < v.size()) {
        //                return v.get(i);
        //            } else {
        //                return v.get(v.size() - 1);
        //            }
        //        }
        //
        //        public static double get(
        //                final double[] v,
        //                final int i,
        //                final double defaultValue){
        //            if (v == null || v.length == 0) {
        //                return defaultValue;
        //            } else if (i < v.length) {
        //                return v[i];
        //            } else {
        //                return v[v.length - 1];
        //            }
        //        }
 /*
  * 
*/
        public static double get(
                final Array v,
                final int i,
                final double defaultValue) {
            if (v == null || v.empty())
                return defaultValue;
            else if (i < v.size())
                return v.get(i);
            else
                return v.get(v.size() - 1);
        }

        public static /* @Rate */ double effectiveFixedRate(
                final Array spreads,
                final Array caps,
                final Array floors,
                final /* @Size */ int i) {
           
        	double result = get(spreads, i, 0.0);
            double floor = get(floors, i, Constants.NULL_RATE);
            if (floor != Constants.NULL_RATE ) {
                result = Math.max(floor, result);
            }
            double cap = get(caps, i, Constants.NULL_RATE);
            if (cap != Constants.NULL_RATE ) {
                result = Math.min(cap, result);
            }
            return result;
        }

        public static boolean noOption(final Array caps, 
        							   final Array floors, 
        							   int /* @Size */ i) {
            return (get(caps, i, Constants.NULL_RATE) == Constants.NULL_RATE) && 
                   (get(floors, i, Constants.NULL_REAL) == Constants.NULL_RATE);
        }

    }

}
