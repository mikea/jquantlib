/*
 Copyright (C) 2009 Ueli Hofstetter

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

package org.jquantlib.cashflow;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.Index;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Schedule;
import org.jquantlib.util.Date;

public class Leg extends ArrayList<CashFlow> {
        public Leg(int n) {
            super(n);
        }
}

//    private static final String coupon_rates_not_specified = "coupon rates not specified";
//    private static final String nominals_not_specified = "nominals not specified";
//    private static final String regular_first_coupon_day_count = "regular first coupon does not allow a first-period day count";
//    private static final String no_nominal_given = "no nominal given";
//    
//    @Deprecated
//    private static double get(double[] v, int i, double defaultValue) {
//        if (v == null) {
//            return defaultValue;
//        } else if (i < v.length) {
//            return v[i];
//        } else {
//            return v[v.length - 1];
//        }
//    }
//
//    @Deprecated
//    private static double effectiveFixedRate(double[] spreads, double[] caps, double[] floors, int i) {
//        double result = get(spreads, i, 0.0);
//        double floor = get(floors, i, 0);
//        if (floor != 0)
//            result = Math.max(floor, result);
//        double cap = get(caps, i, 0);
//        if (cap != 0)
//            result = Math.min(cap, result);
//        return result;
//    }
//    
//    /**
//     * @deprecated v 0.8.1 use the FixedRateLeg class instead
//     * @param nominals
//     * @param schedule
//     * @param couponRates
//     * @param paymentDayCounter
//     * @param paymentAdj
//     * @param firstPeriodDayCount
//     * @return
//     */
//    @Deprecated
//    public static List<CashFlow> FixedRateLeg(double[] nominals, 
//            Schedule schedule, 
//            double[] couponRates,
//            DayCounter paymentDayCounter, 
//            BusinessDayConvention paymentAdj, 
//            DayCounter firstPeriodDayCount) {
//        if (System.getProperty("EXPERIMENTAL") == null) {
//            throw new UnsupportedOperationException("Work in progress");
//        }
//
//        //FIXME: is this correct ?
//        if(couponRates.length == 0){
//            throw new IllegalArgumentException(coupon_rates_not_specified);
//        }
//        if(nominals.length == 0){
//            throw new IllegalArgumentException(nominals_not_specified);
//        }
//
//        List<CashFlow> leg = new ArrayList<CashFlow>();
//
//        // the following is not always correct (orginal c++ comment)
//        Calendar calendar = schedule.getCalendar();
//
//        // first period might be short or long
//        Date start = schedule.date(0);
//        Date end = schedule.date(1);
//        Date paymentDate = calendar.adjust(end, paymentAdj);
//        double rate = couponRates[0];
//        double nominal = nominals[0];
//        if (schedule.isRegular(1)) {
//            //FIXME: required additional check: firstPeriodDayCount.empty()
//            if(firstPeriodDayCount != paymentDayCounter && false){
//                throw new IllegalArgumentException(regular_first_coupon_day_count);
//            }
//            leg.add(new FixedRateCoupon(nominal, paymentDate, rate, paymentDayCounter,
//                                start, end, start, end));
//
//        } else {
//            Date ref = end.decrement(schedule.tenor());
//            ref = calendar.adjust(ref,schedule.businessDayConvention());
//            //FIXME: check this, seems like our internal implementation uses another approach
//            DayCounter dc = firstPeriodDayCount==null?
//                            paymentDayCounter :
//                            firstPeriodDayCount;
//            leg.add(new FixedRateCoupon(nominal, paymentDate, rate,
//                                dc, start, end, ref, end));
//        }
//        // regular periods
//        for (int i=2; i<schedule.size()-1; ++i) {
//            start = end; end = schedule.date(i);
//            paymentDate = calendar.adjust(end,paymentAdj);
//            if ((i-1) < couponRates.length){
//                rate = couponRates[i-1];
//            }
//            else{
//                rate = couponRates[couponRates.length - 1];
//            }
//            if ((i-1) < nominals.length){
//                nominal = nominals[i-1];
//            }
//            else{
//                nominal = nominals[nominals.length - 1];
//            }
//            leg.add(new FixedRateCoupon(nominal, paymentDate, rate, paymentDayCounter,
//                                start, end, start, end));
//        }
//        if (schedule.size() > 2) {
//            // last period might be short or long
//            int N = schedule.size();
//            start = end; end = schedule.date(N-1);
//            paymentDate = calendar.adjust(end,paymentAdj);
//            if ((N-2) < couponRates.length){
//                rate = couponRates[N-2];
//            }
//            else{
//                rate = couponRates[couponRates.length - 1];
//            }
//            if ((N-2) < nominals.length){
//                nominal = nominals[N-2];
//            }
//            else{
//                nominal = nominals[nominals.length - 1];
//            }
//            if (schedule.isRegular(N-1)) {
//                leg.add(new
//                    FixedRateCoupon(nominal, paymentDate,
//                                    rate, paymentDayCounter,
//                                    start, end, start, end));
//            } else {
//                Date ref = start.increment(schedule.tenor());
//                ref = calendar.adjust(ref,schedule.businessDayConvention());
//                leg.add(new FixedRateCoupon(nominal, paymentDate,
//                                    rate, paymentDayCounter,
//                                    start, end, start, ref));
//            }
//        }
//        return leg;
//    }
//    
//    /*
//    template <typename IndexType,
//    typename FloatingCouponType,
//    typename CappedFlooredCouponType>
//    */
//    public static List<CashFlow> FloatingLeg(double[] nominals, 
//            Schedule schedule, 
//            Index index,
//            DayCounter paymentDayCounter, 
//            BusinessDayConvention paymentAdj, 
//            int fixingDays,
//            double [] gearings,
//            double [] spreads,
//            double [] caps,
//            double [] floors,
//            boolean isInArrears
//            //used to mimic templates
//            //class CouponType
//            ){
//        
//        if(nominals==null || nominals.length == 0){
//            throw new IllegalArgumentException(no_nominal_given);
//        }
//        
//        int n = schedule.size() - 1;
//        if(nominals.length>n){
//            throw new IllegalArgumentException("too many nominals (" + nominals.length +
//            "), only " + n + " required");
//        }
//        
//        if(gearings.length>n){
//            throw new IllegalArgumentException("too many gearings (" + gearings.length +
//                   "), only " + n + " required");
//        }
//        
//        if(spreads.length > n){
//            throw new IllegalArgumentException("too many spreads (" + spreads.length +
//                   "), only " + n + " required");
//        }
//        
//        if(caps.length>n){
//            throw new IllegalArgumentException("too many caps (" + caps.length +
//                   "), only " + n + " required");
//        }
//        
//        if(floors.length>n){
//            throw new IllegalArgumentException("too many floors (" + floors.length +
//                "), only " + n + " required");
//        }
//        
//        List<CashFlow> leg = new ArrayList<CashFlow>(n);
//        
//        // the following is not always correct (orignial c++ comment)
//        Calendar calendar = schedule.getCalendar();
//        
//        //FIXME: constructor for uninitialized date available ?
//        Date refStart, start, refEnd, end;
//        Date paymentDate;
//        
//        for(int i = 0; i<n; i++){
//            refStart = start = schedule.date(i);
//            refEnd = end = schedule.date(i+1);
//            paymentDate =  calendar.adjust(end, paymentAdj);
//        
//            if (i==0   && !schedule.isRegular(i+1))
//                refStart = calendar.adjust(end.decrement(schedule.tenor()), paymentAdj);
//            if (i==n-1 && !schedule.isRegular(i+1))
//                refEnd = calendar.adjust(start.increment(schedule.tenor()), paymentAdj);
//            if (get(gearings, i, 1.0) == 0.0) { // fixed coupon
//                leg.add(new FixedRateCoupon(get(nominals, i, 0), paymentDate,
//                                    effectiveFixedRate(spreads, caps, floors, i),
//                                    paymentDayCounter,
//                                    start, end, refStart, refEnd));
//                
//            } else { // floating coupon
//                /*
//                if (noOption(caps, floors, i))
//                    leg.add(FloatingCouponType.(paymentDate,
//                                   get(nominals, i, 0),
//                                   start, end, fixingDays, index,
//                                   get(gearings, i, 1.0),
//                                   get(spreads, i, 0.0),
//                                   refStart, refEnd,
//                                   paymentDayCounter, isInArrears)));
//                else {
//                    leg.push_back(boost::shared_ptr<CashFlow>(new
//                        CappedFlooredCouponType(paymentDate,
//                                                get(nominals, i, Null<Real>()),
//                                                start, end, fixingDays, index,
//                                                get(gearings, i, 1.0),
//                                                get(spreads, i, 0.0),
//                                                get(caps,   i, Null<Rate>()),
//                                                get(floors, i, Null<Rate>()),
//                                                refStart, refEnd,
//                                                paymentDayCounter, isInArrears)));
//                }
//            }
//        }*/
//        
//    }
//           
//        }
//        return leg;
//    }
//}
//        
//        
//
//
///*
//QL_REQUIRE(!nominals.empty(), "no nominal given");
//
//Size n = schedule.size()-1;
//QL_REQUIRE(nominals.size() <= n,
//           "too many nominals (" << nominals.size() <<
//           "), only " << n << " required");
//QL_REQUIRE(gearings.size()<=n,
//           "too many gearings (" << gearings.size() <<
//           "), only " << n << " required");
//QL_REQUIRE(spreads.size()<=n,
//           "too many spreads (" << spreads.size() <<
//           "), only " << n << " required");
//QL_REQUIRE(caps.size()<=n,
//           "too many caps (" << caps.size() <<
//           "), only " << n << " required");
//QL_REQUIRE(floors.size()<=n,
//           "too many floors (" << floors.size() <<
//           "), only " << n << " required");
//
//Leg leg; leg.reserve(n);
//
//// the following is not always correct
//Calendar calendar = schedule.calendar();
//
//Date refStart, start, refEnd, end;
//Date paymentDate;
//
//for (Size i=0; i<n; ++i) {
//    refStart = start = schedule.date(i);
//    refEnd   =   end = schedule.date(i+1);
//    paymentDate = calendar.adjust(end, paymentAdj);
//    if (i==0   && !schedule.isRegular(i+1))
//        refStart = calendar.adjust(end - schedule.tenor(), paymentAdj);
//    if (i==n-1 && !schedule.isRegular(i+1))
//        refEnd = calendar.adjust(start + schedule.tenor(), paymentAdj);
//    if (get(gearings, i, 1.0) == 0.0) { // fixed coupon
//        leg.push_back(boost::shared_ptr<CashFlow>(new
//            FixedRateCoupon(get(nominals, i, Null<Real>()), paymentDate,
//                            effectiveFixedRate(spreads, caps, floors, i),
//                            paymentDayCounter,
//                            start, end, refStart, refEnd)));
//    } else { // floating coupon
//        if (noOption(caps, floors, i))
//            leg.push_back(boost::shared_ptr<CashFlow>(new
//                FloatingCouponType(paymentDate,
//                           get(nominals, i, Null<Real>()),
//                           start, end, fixingDays, index,
//                           get(gearings, i, 1.0),
//                           get(spreads, i, 0.0),
//                           refStart, refEnd,
//                           paymentDayCounter, isInArrears)));
//        else {
//            leg.push_back(boost::shared_ptr<CashFlow>(new
//                CappedFlooredCouponType(paymentDate,
//                                        get(nominals, i, Null<Real>()),
//                                        start, end, fixingDays, index,
//                                        get(gearings, i, 1.0),
//                                        get(spreads, i, 0.0),
//                                        get(caps,   i, Null<Rate>()),
//                                        get(floors, i, Null<Rate>()),
//                                        refStart, refEnd,
//                                        paymentDayCounter, isInArrears)));
//        }
//    }
//}
//return leg;
//}
//    /*
//     * Real get(const std::vector<Real>& v, Size i, Real defaultValue) { if (v.empty()) { return defaultValue; } else if (i <
//     * v.size()) { return v[i]; } else { return v.back(); } }
//     * 
//     * Rate effectiveFixedRate(const std::vector<Spread>& spreads, const std::vector<Rate>& caps, const std::vector<Rate>& floors,
//     * Size i) { Rate result = get(spreads, i, 0.0); Rate floor = get(floors, i, Null<Rate>()); if (floor!=Null<Rate>()) result =
//     * std::max(floor, result); Rate cap = get(caps, i, Null<Rate>()); if (cap!=Null<Rate>()) result = std::min(cap, result); return
//     * result; }
//     * 
//     * bool noOption(const std::vector<Rate>& caps, const std::vector<Rate>& floors, Size i) { return (get(caps, i, Null<Rate>()) ==
//     * Null<Rate>()) && (get(floors, i, Null<Rate>()) == Null<Rate>()); } }
//     */
//
//    /*
//     * 
//     * 
//     * Leg FixedRateLeg(const std::vector<Real>& nominals, const Schedule& schedule, const std::vector<Rate>& couponRates, const
//     * DayCounter& paymentDayCounter, BusinessDayConvention paymentAdj, const DayCounter& firstPeriodDayCount) {
//     * 
//     * QL_REQUIRE(!couponRates.empty(), "coupon rates not specified"); QL_REQUIRE(!nominals.empty(), "nominals not specified");
//     * 
//     * Leg leg;
//     * 
//     * // the following is not always correct Calendar calendar = schedule.calendar();
//     * 
//     * // first period might be short or long Date start = schedule.date(0), end = schedule.date(1); Date paymentDate =
//     * calendar.adjust(end, paymentAdj); Rate rate = couponRates[0]; Real nominal = nominals[0]; if (schedule.isRegular(1)) {
//     * QL_REQUIRE(firstPeriodDayCount.empty() || firstPeriodDayCount == paymentDayCounter, "regular first coupon "
//     * "does not allow a first-period day count"); leg.push_back(boost::shared_ptr<CashFlow>(new FixedRateCoupon(nominal,
//     * paymentDate, rate, paymentDayCounter, start, end, start, end))); } else { Date ref = end - schedule.tenor(); ref =
//     * calendar.adjust(ref, schedule.businessDayConvention()); DayCounter dc = firstPeriodDayCount.empty() ? paymentDayCounter :
//     * firstPeriodDayCount; leg.push_back(boost::shared_ptr<CashFlow>(new FixedRateCoupon(nominal, paymentDate, rate, dc, start,
//     * end, ref, end))); } // regular periods for (Size i=2; i<schedule.size()-1; ++i) { start = end; end = schedule.date(i);
//     * paymentDate = calendar.adjust(end,paymentAdj); if ((i-1) < couponRates.size()) rate = couponRates[i-1]; else rate =
//     * couponRates.back(); if ((i-1) < nominals.size()) nominal = nominals[i-1]; else nominal = nominals.back();
//     * leg.push_back(boost::shared_ptr<CashFlow>(new FixedRateCoupon(nominal, paymentDate, rate, paymentDayCounter, start, end,
//     * start, end))); } if (schedule.size() > 2) { // last period might be short or long Size N = schedule.size(); start = end; end
//     * = schedule.date(N-1); paymentDate = calendar.adjust(end,paymentAdj); if ((N-2) < couponRates.size()) rate = couponRates[N-2];
//     * else rate = couponRates.back(); if ((N-2) < nominals.size()) nominal = nominals[N-2]; else nominal = nominals.back(); if
//     * (schedule.isRegular(N-1)) { leg.push_back(boost::shared_ptr<CashFlow>(new FixedRateCoupon(nominal, paymentDate, rate,
//     * paymentDayCounter, start, end, start, end))); } else { Date ref = start + schedule.tenor(); ref = calendar.adjust(ref,
//     * schedule.businessDayConvention()); leg.push_back(boost::shared_ptr<CashFlow>(new FixedRateCoupon(nominal, paymentDate, rate,
//     * paymentDayCounter, start, end, start, ref))); } } return leg; }
//     * 
//     * 
//     * 
//     * template <typename IndexType, typename FloatingCouponType, typename CappedFlooredCouponType> Leg FloatingLeg(const
//     * std::vector<Real>& nominals, const Schedule& schedule, const boost::shared_ptr<IndexType>& index, const DayCounter&
//     * paymentDayCounter, BusinessDayConvention paymentAdj, Natural fixingDays, const std::vector<Real>& gearings, const
//     * std::vector<Spread>& spreads, const std::vector<Rate>& caps, const std::vector<Rate>& floors, bool isInArrears) {
//     * 
//     * QL_REQUIRE(!nominals.empty(), "no nominal given");
//     * 
//     * Size n = schedule.size()-1; QL_REQUIRE(nominals.size() <= n, "too many nominals (" << nominals.size() << "), only " << n <<
//     * " required"); QL_REQUIRE(gearings.size()<=n, "too many gearings (" << gearings.size() << "), only " << n << " required");
//     * QL_REQUIRE(spreads.size()<=n, "too many spreads (" << spreads.size() << "), only " << n << " required");
//     * QL_REQUIRE(caps.size()<=n, "too many caps (" << caps.size() << "), only " << n << " required"); QL_REQUIRE(floors.size()<=n,
//     * "too many floors (" << floors.size() << "), only " << n << " required");
//     * 
//     * Leg leg; leg.reserve(n);
//     * 
//     * // the following is not always correct Calendar calendar = schedule.calendar();
//     * 
//     * Date refStart, start, refEnd, end; Date paymentDate;
//     * 
//     * for (Size i=0; i<n; ++i) { refStart = start = schedule.date(i); refEnd = end = schedule.date(i+1); paymentDate =
//     * calendar.adjust(end, paymentAdj); if (i==0 && !schedule.isRegular(i+1)) refStart = calendar.adjust(end - schedule.tenor(),
//     * paymentAdj); if (i==n-1 && !schedule.isRegular(i+1)) refEnd = calendar.adjust(start + schedule.tenor(), paymentAdj); if
//     * (get(gearings, i, 1.0) == 0.0) { // fixed coupon leg.push_back(boost::shared_ptr<CashFlow>(new FixedRateCoupon(get(nominals,
//     * i, Null<Real>()), paymentDate, effectiveFixedRate(spreads, caps, floors, i), paymentDayCounter, start, end, refStart,
//     * refEnd))); } else { // floating coupon if (noOption(caps, floors, i)) leg.push_back(boost::shared_ptr<CashFlow>(new
//     * FloatingCouponType(paymentDate, get(nominals, i, Null<Real>()), start, end, fixingDays, index, get(gearings, i, 1.0),
//     * get(spreads, i, 0.0), refStart, refEnd, paymentDayCounter, isInArrears))); else {
//     * leg.push_back(boost::shared_ptr<CashFlow>(new CappedFlooredCouponType(paymentDate, get(nominals, i, Null<Real>()), start,
//     * end, fixingDays, index, get(gearings, i, 1.0), get(spreads, i, 0.0), get(caps, i, Null<Rate>()), get(floors, i,
//     * Null<Rate>()), refStart, refEnd, paymentDayCounter, isInArrears))); } } } return leg; }
//     * 
//     * Leg IborLeg(const std::vector<Real>& nominals, const Schedule& schedule, const boost::shared_ptr<IborIndex>& index, const
//     * DayCounter& paymentDayCounter, BusinessDayConvention paymentAdj, Natural fixingDays, const std::vector<Real>& gearings, const
//     * std::vector<Spread>& spreads, const std::vector<Rate>& caps, const std::vector<Rate>& floors, bool isInArrears) {
//     * 
//     * return FloatingLeg<IborIndex, IborCoupon, CappedFlooredIborCoupon>( nominals, schedule, index, paymentDayCounter, paymentAdj,
//     * fixingDays, gearings, spreads, caps, floors, isInArrears); }
//     * 
//     * Leg CmsLeg(const std::vector<Real>& nominals, const Schedule& schedule, const boost::shared_ptr<SwapIndex>& index, const
//     * DayCounter& paymentDayCounter, BusinessDayConvention paymentAdj, Natural fixingDays, const std::vector<Real>& gearings, const
//     * std::vector<Spread>& spreads, const std::vector<Rate>& caps, const std::vector<Rate>& floors, bool isInArrears) {
//     * 
//     * return FloatingLeg<SwapIndex, CmsCoupon, CappedFlooredCmsCoupon>( nominals, schedule, index, paymentDayCounter, paymentAdj,
//     * fixingDays, gearings, spreads, caps, floors, isInArrears); }
//     * 
//     * template <typename IndexType, typename FloatingCouponType, typename CappedFlooredFloatingCouponType> Leg
//     * FloatingZeroLeg(const std::vector<Real>& nominals, const Schedule& schedule, const boost::shared_ptr<IndexType>& index, const
//     * DayCounter& paymentDayCounter, BusinessDayConvention paymentAdj, Natural fixingDays, const std::vector<Real>& gearings, const
//     * std::vector<Spread>& spreads, const std::vector<Rate>& caps, const std::vector<Rate>& floors) {
//     * 
//     * QL_REQUIRE(!nominals.empty(), "no nominal given");
//     * 
//     * Size n = schedule.size()-1; QL_REQUIRE(nominals.size() <= n, "too many nominals (" << nominals.size() << "), only " << n <<
//     * " required"); QL_REQUIRE(gearings.size()<=n, "too many gearings (" << gearings.size() << "), only " << n << " required");
//     * QL_REQUIRE(spreads.size()<=n, "too many spreads (" << spreads.size() << "), only " << n << " required");
//     * QL_REQUIRE(caps.size()<=n, "too many caps (" << caps.size() << "), only " << n << " required"); QL_REQUIRE(floors.size()<=n,
//     * "too many floors (" << floors.size() << "), only " << n << " required");
//     * 
//     * Leg leg; leg.reserve(n);
//     * 
//     * // the following is not always correct Calendar calendar = schedule.calendar();
//     * 
//     * Date refStart, start, refEnd, end;
//     * 
//     * // All payment dates at the end!! in arrears fixing makes no sense Date paymentDate = calendar.adjust(schedule.date(n),
//     * paymentAdj); bool isInArrears = false;
//     * 
//     * for (Size i=0; i<n; ++i) { refStart = start = schedule.date(i); refEnd = end = schedule.date(i+1); //paymentDate =
//     * calendar.adjust(end, paymentAdj); if (i==0 && !schedule.isRegular(i+1)) refStart = calendar.adjust(end - schedule.tenor(),
//     * paymentAdj); if (i==n-1 && !schedule.isRegular(i+1)) refEnd = calendar.adjust(start + schedule.tenor(), paymentAdj); if
//     * (get(gearings, i, 1.0) == 0.0) { // fixed coupon leg.push_back(boost::shared_ptr<CashFlow>(new FixedRateCoupon(get(nominals,
//     * i, Null<Real>()), paymentDate, effectiveFixedRate(spreads, caps, floors, i), paymentDayCounter, start, end, refStart,
//     * refEnd))); } else { // floating coupon if (noOption(caps, floors, i)) { leg.push_back(boost::shared_ptr<CashFlow>(new
//     * FloatingCouponType(paymentDate, // diff get(nominals, i, Null<Real>()), start, end, fixingDays, index, get(gearings, i, 1.0),
//     * get(spreads, i, 0.0), refStart, refEnd, paymentDayCounter, isInArrears))); } else {
//     * leg.push_back(boost::shared_ptr<CashFlow>(new CappedFlooredFloatingCouponType(paymentDate, // diff get(nominals, i,
//     * Null<Real>()), start, end, fixingDays, index, get(gearings, i, 1.0), get(spreads, i, 0.0), get(caps, i, Null<Rate>()),
//     * get(floors, i, Null<Rate>()), refStart, refEnd, paymentDayCounter, isInArrears))); } } } return leg; }
//     * 
//     * Leg IborZeroLeg(const std::vector<Real>& nominals, const Schedule& schedule, const boost::shared_ptr<IborIndex>& index, const
//     * DayCounter& paymentDayCounter, BusinessDayConvention paymentAdj, Natural fixingDays, const std::vector<Real>& gearings, const
//     * std::vector<Spread>& spreads, const std::vector<Rate>& caps, const std::vector<Rate>& floors) {
//     * 
//     * return FloatingZeroLeg <IborIndex, IborCoupon, CappedFlooredIborCoupon>( nominals, schedule, index, paymentDayCounter,
//     * paymentAdj, fixingDays, gearings, spreads, caps, floors); }
//     * 
//     * Leg CmsZeroLeg(const std::vector<Real>& nominals, const Schedule& schedule, const boost::shared_ptr<SwapIndex>& index, const
//     * DayCounter& paymentDayCounter, BusinessDayConvention paymentAdj, Natural fixingDays, const std::vector<Real>& gearings, const
//     * std::vector<Spread>& spreads, const std::vector<Rate>& caps, const std::vector<Rate>& floors) {
//     * 
//     * return FloatingZeroLeg <SwapIndex, CmsCoupon, CappedFlooredCmsCoupon>( nominals, schedule, index, paymentDayCounter,
//     * paymentAdj, fixingDays, gearings, spreads, caps, floors); }
//     * 
//     * Leg RangeAccrualLeg(const std::vector<Real>& nominals, const Schedule& schedule, const boost::shared_ptr<IborIndex>& index,
//     * const DayCounter& paymentDayCounter, BusinessDayConvention paymentConvention, Natural fixingDays, const std::vector<Real>&
//     * gearings, const std::vector<Spread>& spreads, const std::vector<Rate>& lowerTriggers, const std::vector<Rate>& upperTriggers,
//     * const Period& observationTenor, BusinessDayConvention observationConvention) {
//     * 
//     * QL_REQUIRE(!nominals.empty(), "no nominal given");
//     * 
//     * Size n = schedule.size()-1; QL_REQUIRE(nominals.size() <= n, "too many nominals (" << nominals.size() << "), only " << n <<
//     * " required"); QL_REQUIRE(gearings.size()<=n, "too many gearings (" << gearings.size() << "), only " << n << " required");
//     * QL_REQUIRE(spreads.size()<=n, "too many spreads (" << spreads.size() << "), only " << n << " required");
//     * QL_REQUIRE(lowerTriggers.size()<=n, "too many lowerTriggers (" << lowerTriggers.size() << "), only " << n << " required");
//     * QL_REQUIRE(upperTriggers.size()<=n, "too many upperTriggers (" << upperTriggers.size() << "), only " << n << " required");
//     * 
//     * Leg leg; leg.reserve(n);
//     * 
//     * // the following is not always correct Calendar calendar = schedule.calendar();
//     * 
//     * Date refStart, start, refEnd, end; Date paymentDate; std::vector<boost::shared_ptr<Schedule> > observationsSchedules;
//     * 
//     * for (Size i=0; i<n; ++i) { refStart = start = schedule.date(i); refEnd = end = schedule.date(i+1); paymentDate =
//     * calendar.adjust(end, paymentConvention); if (i==0 && !schedule.isRegular(i+1)) refStart = calendar.adjust(end -
//     * schedule.tenor(), paymentConvention); if (i==n-1 && !schedule.isRegular(i+1)) refEnd = calendar.adjust(start +
//     * schedule.tenor(), paymentConvention); if (get(gearings, i, 1.0) == 0.0) { // fixed coupon
//     * leg.push_back(boost::shared_ptr<CashFlow>(new FixedRateCoupon(get(nominals, i, Null<Real>()), paymentDate, get(spreads, i,
//     * 0.0), paymentDayCounter, start, end, refStart, refEnd))); } else { // floating coupon observationsSchedules.push_back(
//     * boost::shared_ptr<Schedule>( new Schedule(start, end, observationTenor, calendar, observationConvention,
//     * observationConvention, false, false)));
//     * 
//     * leg.push_back(boost::shared_ptr<CashFlow>(new RangeAccrualFloatersCoupon( get(nominals, i, Null<Real>()), paymentDate, index,
//     * start, end, fixingDays, paymentDayCounter, get(gearings, i, 1.0), get(spreads, i, 0.0), refStart, refEnd,
//     * observationsSchedules.back(), get(lowerTriggers, i, Null<Rate>()), get(upperTriggers, i, Null<Rate>())))); } } return leg; }
//     */

