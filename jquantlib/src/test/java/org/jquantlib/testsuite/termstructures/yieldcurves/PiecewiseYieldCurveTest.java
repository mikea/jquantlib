/*
 Copyright (C) 2008 Srinivas Hasti

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

package org.jquantlib.testsuite.termstructures.yieldcurves;


import org.jquantlib.QL;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.Actual360;
import org.jquantlib.daycounters.ActualActual;
import org.jquantlib.daycounters.ActualActual.Convention;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.Euribor;
import org.jquantlib.indexes.Euribor3M;
import org.jquantlib.indexes.Euribor6M;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.instruments.ForwardRateAgreement;
import org.jquantlib.instruments.MakeVanillaSwap;
import org.jquantlib.instruments.Position;
import org.jquantlib.instruments.VanillaSwap;
import org.jquantlib.instruments.bonds.FixedRateBond;
import org.jquantlib.math.interpolations.Interpolation.Interpolator;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.bond.DiscountingBondEngine;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.Bootstrap;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.yieldcurves.DepositRateHelper;
import org.jquantlib.termstructures.yieldcurves.FixedRateBondHelper;
import org.jquantlib.termstructures.yieldcurves.FraRateHelper;
import org.jquantlib.termstructures.yieldcurves.PiecewiseYieldCurve;
import org.jquantlib.termstructures.yieldcurves.SwapRateHelper;
import org.jquantlib.termstructures.yieldcurves.Traits;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.DateGeneration;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;
import org.jquantlib.time.Schedule;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Target;
import org.junit.Test;

/**
 * @author Srinivas Hasti
 * @author Richard Gomes
 */
public class PiecewiseYieldCurveTest {

	private Datum depositData[] = new Datum[] {
    	new Datum( 1, TimeUnit.Weeks,  4.559 ),
    	new Datum( 1, TimeUnit.Months, 4.581 ),
    	new Datum( 2, TimeUnit.Months, 4.573 ),
    	new Datum( 3, TimeUnit.Months, 4.557 ),
    	new Datum( 6, TimeUnit.Months, 4.496 ),
    	new Datum( 9, TimeUnit.Months, 4.490 )
    };

    private Datum fraData[] = {
    	new Datum( 1, TimeUnit.Months, 4.581 ),
    	new Datum( 2, TimeUnit.Months, 4.573 ),
    	new Datum( 3, TimeUnit.Months, 4.557 ),
    	new Datum( 6, TimeUnit.Months, 4.496 ),
    	new Datum( 9, TimeUnit.Months, 4.490 )
    };

    private Datum swapData[] = {
    	new Datum(  1, TimeUnit.Years, 4.54 ),
    	new Datum(  2, TimeUnit.Years, 4.63 ),
    	new Datum(  3, TimeUnit.Years, 4.75 ),
    	new Datum(  4, TimeUnit.Years, 4.86 ),
    	new Datum(  5, TimeUnit.Years, 4.99 ),
    	new Datum(  6, TimeUnit.Years, 5.11 ),
    	new Datum(  7, TimeUnit.Years, 5.23 ),
    	new Datum(  8, TimeUnit.Years, 5.33 ),
    	new Datum(  9, TimeUnit.Years, 5.41 ),
    	new Datum( 10, TimeUnit.Years, 5.47 ),
    	new Datum( 12, TimeUnit.Years, 5.60 ),
    	new Datum( 15, TimeUnit.Years, 5.75 ),
    	new Datum( 20, TimeUnit.Years, 5.89 ),
    	new Datum( 25, TimeUnit.Years, 5.95 ),
    	new Datum( 30, TimeUnit.Years, 5.96 )
    };

    private BondDatum bondData[] = {
    	new BondDatum(  6, TimeUnit.Months, 5, Frequency.Semiannual, 4.75, 101.320 ),
    	new BondDatum(  1, TimeUnit.Years,  3, Frequency.Semiannual, 2.75, 100.590 ),
    	new BondDatum(  2, TimeUnit.Years,  5, Frequency.Semiannual, 5.00, 105.650 ),
    	new BondDatum(  5, TimeUnit.Years, 11, Frequency.Semiannual, 5.50, 113.610 ),
    	new BondDatum( 10, TimeUnit.Years, 11, Frequency.Semiannual, 3.75, 104.070 )
    };

    private Datum bmaData[] = {
    	new Datum(  1, TimeUnit.Years, 67.56 ),
    	new Datum(  2, TimeUnit.Years, 68.00 ),
    	new Datum(  3, TimeUnit.Years, 68.25 ),
    	new Datum(  4, TimeUnit.Years, 68.50 ),
    	new Datum(  5, TimeUnit.Years, 68.81 ),
    	new Datum(  7, TimeUnit.Years, 69.50 ),
    	new Datum( 10, TimeUnit.Years, 70.44 ),
    	new Datum( 15, TimeUnit.Years, 71.69 ),
    	new Datum( 20, TimeUnit.Years, 72.69 ),
    	new Datum( 30, TimeUnit.Years, 73.81 )
    };
    
    
    public PiecewiseYieldCurveTest() {
        QL.info("::::: "+this.getClass().getSimpleName()+" :::::");
    }
    
    @Test
    public void dummyTest() {
    	// this is not a test
    }
    
    
    
    //
    // private inner classes
    //
	
	private static class Datum {
        public final int n;
        public final TimeUnit units;
        public final /*@Rate*/ double rate;
        
        public Datum(
        		final int n,
        		final TimeUnit units,
        		final /*@Rate*/ double rate) {
        	this.n = n;
        	this.units = units;
        	this.rate = rate;
        }
    }

	private static class BondDatum {
    	public final int n;
    	public final TimeUnit units;
    	public final int length;
    	public final Frequency frequency;
    	public final /*@Rate*/ double coupon;
    	public final /*@Real*/ double price;
        
        public BondDatum(
        		final int n,
        		final TimeUnit units,
        		final int length,
        		final Frequency frequency,
        		final /*@Rate*/ double coupon,
        		final /*@Real*/ double price) {
        	this.n = n;
        	this.units = units;
        	this.length = length;
        	this.frequency = frequency;
        	this.coupon = coupon;
        	this.price = price;
        }
    }

	private class CommonVars {
		// global variables
		public final Calendar calendar;
		public final int settlementDays;
		public final Date today;
		public final Date settlement;
		public final BusinessDayConvention fixedLegConvention;
		public final Frequency fixedLegFrequency;
		public final DayCounter fixedLegDayCounter;
		public final int bondSettlementDays;
		public final DayCounter bondDayCounter;
		public final BusinessDayConvention bondConvention;
		public final double bondRedemption;
		public final Frequency bmaFrequency;
		public final BusinessDayConvention bmaConvention;
		public final DayCounter bmaDayCounter;

		public final int deposits;
		public final int fras;
		public final int swaps;
		public final int bonds;
		public final int bmas;
		public final Quote[] rates;
		public final Quote[] fraRates;
		public final Quote[] prices;
		public final Quote[] fractions;
		public final RateHelper[] instruments;
		public final RateHelper[] fraHelpers;
		public final RateHelper[] bondHelpers;
		public final RateHelper[] bmaHelpers;
		public final Schedule[] schedules;
		

		public YieldTermStructure termStructure;


		//public SavedSettings backup;
		//public IndexHistoryCleaner cleaner;

		
		public CommonVars() {
			// data
			calendar = new Target();
			settlementDays = 2;
			
			today = calendar.adjust(Date.todaysDate());
			new Settings().setEvaluationDate(today);
			
			settlement = calendar.advance(today,settlementDays,TimeUnit.Days);
			fixedLegConvention = BusinessDayConvention.Unadjusted;
			fixedLegFrequency = Frequency.Annual;
			fixedLegDayCounter = new org.jquantlib.daycounters.Thirty360();
			bondSettlementDays = 3;
			bondDayCounter = new ActualActual(Convention.Bond);
			bondConvention = BusinessDayConvention.Following;
			bondRedemption = 100.0;
			bmaFrequency = Frequency.Quarterly;
			bmaConvention = BusinessDayConvention.Following;
			bmaDayCounter = new ActualActual(Convention.Bond);

			deposits = depositData.length;
            fras = fraData.length;
            swaps = swapData.length;
            bonds = bondData.length;
            bmas = bmaData.length;

            
            // market elements
            rates = new SimpleQuote[deposits+swaps];
            fraRates = new SimpleQuote[fras];
            fractions = new SimpleQuote[bmas];
            prices = new SimpleQuote[bonds];
            
            for (int i=0; i<deposits; i++) {
                rates[i] = new SimpleQuote(depositData[i].rate/100);
            }

            for (int i=0; i<swaps; i++) {
            	rates[i+deposits] = new SimpleQuote(swapData[i].rate/100);
            }
            
            for (int i=0; i<fras; i++) {
                fraRates[i] = new SimpleQuote(fraData[i].rate/100);
            }
            
            for (int i=0; i<bonds; i++) {
                prices[i] = new SimpleQuote(bondData[i].price);
            }
            
            for (int i=0; i<bmas; i++) {
                fractions[i] = new SimpleQuote(bmaData[i].rate/100);
            }

            // rate helpers
            instruments = new RateHelper[deposits+swaps];
            fraHelpers  = new RateHelper[fras];
            bondHelpers = new RateHelper[bonds];
            schedules   = new Schedule[bonds];
            bmaHelpers  = new RateHelper[bmas];
            
            IborIndex euribor6m = new Euribor(new Period(6, TimeUnit.Months), new Handle<YieldTermStructure>());
            for (int i=0; i<deposits; i++) {
                Handle<Quote> r = new Handle<Quote>(rates[i]);
                instruments[i] = new
                    DepositRateHelper(r, new Period(depositData[i].n,depositData[i].units),
                                      euribor6m.fixingDays(), calendar,
                                      euribor6m.businessDayConvention(),
                                      euribor6m.endOfMonth(),
                                      euribor6m.dayCounter());
            }

            for (int i=0; i<swaps; i++) {
                Handle<Quote> r = new Handle<Quote>(rates[i+deposits]);
                instruments[i+deposits] = new
                    SwapRateHelper(r, new Period(swapData[i].n, swapData[i].units),
                                   calendar,
                                   fixedLegFrequency, fixedLegConvention,
                                   fixedLegDayCounter, euribor6m);
            }

            Euribor euribor3m = new Euribor(new Period(3, TimeUnit.Months), new Handle<YieldTermStructure>());
            for (int i=0; i<fras; i++) {
                Handle<Quote> r = new Handle<Quote>(fraRates[i]);
                fraHelpers[i] = new
                    FraRateHelper(r, fraData[i].n, fraData[i].n + 3,
                                  euribor3m.fixingDays(),
                                  euribor3m.fixingCalendar(),
                                  euribor3m.businessDayConvention(),
                                  euribor3m.endOfMonth(),
                                  euribor3m.dayCounter());
            }

            for (int i=0; i<bonds; i++) {
                Handle<Quote> p = new Handle<Quote>(prices[i]);
                Date maturity = calendar.advance(today, bondData[i].n, bondData[i].units);
                Date issue = calendar.advance(maturity, -bondData[i].length, TimeUnit.Years);
                
                /*@Rate*/ double[] coupons = new double[1];
                coupons[0] = bondData[i].coupon/100.0;

                schedules[i] = new Schedule(issue, maturity,
                                        new Period(bondData[i].frequency),
                                        calendar,
                                        bondConvention, bondConvention,
                                        DateGeneration.Rule.Backward, false, new Date(), new Date());
                
                bondHelpers[i] = new FixedRateBondHelper(p,
                                        bondSettlementDays,
                                        bondRedemption, schedules[i],
                                        coupons, bondDayCounter,
                                        bondConvention,
                                        bondRedemption, issue);
            }
        }
	}
    
    
    
    private <T extends Traits, I extends Interpolator, B extends Bootstrap> void testCurveConsistency(
    		Class<T> classT,
    		Class<I> classI,
    		Class<B> classB,
    		final CommonVars vars) {
    	I interpolator;
		try {
			interpolator = classI.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	testCurveConsistency(classT, classI, classB, vars, interpolator, 1.0e-9);
    }
    private <T extends Traits, I extends Interpolator, B extends Bootstrap> void testCurveConsistency(
    		Class<T> classT,
    		Class<I> classI,
    		Class<B> classB,
    		final CommonVars vars,
            final Interpolator interpolator) {
    	testCurveConsistency(classT, classI, classB, vars, interpolator, 1.0e-9);
    }
    private <T extends Traits, I extends Interpolator, B extends Bootstrap> void testCurveConsistency(
    		Class<T> classT,
    		Class<I> classI,
    		Class<B> classB,
    		final CommonVars vars,
            final Interpolator interpolator,
            /*@Real*/ double tolerance) {
    	
        vars.termStructure = new PiecewiseYieldCurve<T,I,B>(
										classT, classI, classB,
										vars.settlement, vars.instruments,
										new Actual360(),
										new Handle/*<Quote>*/[0],
										new Date[0],
										1.0e-12,
										interpolator);

        RelinkableHandle<YieldTermStructure> curveHandle = new RelinkableHandle<YieldTermStructure>();
        curveHandle.linkTo(vars.termStructure);

        // check deposits
        for (int i=0; i<vars.deposits; i++) {
            Euribor index = new Euribor(new Period(depositData[i].n, depositData[i].units), curveHandle);
            /*@Rate*/ double expectedRate  = depositData[i].rate/100;
            /*@Rate*/ double estimatedRate = index.fixing(vars.today);
            if (Math.abs(expectedRate-estimatedRate) > tolerance) {
            	throw new RuntimeException(
	                String.format("%d %s %s %s %f %s %f",
	                    depositData[i].n,
	                    depositData[i].units == TimeUnit.Weeks ? "week(s)" : "month(s)",
	                    " deposit:",
	                    "\n    estimated rate: ", estimatedRate,
	                    "\n    expected rate:  ", expectedRate));
            }
        }

        // check swaps
        IborIndex euribor6m = new Euribor6M(curveHandle);
        for (int i=0; i<vars.swaps; i++) {
            Period tenor = new Period(swapData[i].n, swapData[i].units);

            VanillaSwap swap = new MakeVanillaSwap(tenor, euribor6m, 0.0)
                .withEffectiveDate(vars.settlement)
                .withFixedLegDayCount(vars.fixedLegDayCounter)
                .withFixedLegTenor(new Period(vars.fixedLegFrequency))
                .withFixedLegConvention(vars.fixedLegConvention)
                .withFixedLegTerminationDateConvention(vars.fixedLegConvention)
                .value();

            /*@Rate*/ double expectedRate = swapData[i].rate/100;
            /*@Rate*/ double estimatedRate = swap.fairRate();
            /*@Spread*/ double error = Math.abs(expectedRate-estimatedRate);
            if (error > tolerance) {
            	throw new RuntimeException(
        			String.format("%d %s %s %f %s %f %s %f %s %f",
	                    swapData[i].n, " year(s) swap:\n",
	                    "\n estimated rate: ", estimatedRate,
	                    "\n expected rate:  ", expectedRate,
	                    "\n error:          ", error,
	                    "\n tolerance:      ", tolerance));
            }
        }

        // check bonds
        vars.termStructure = new PiecewiseYieldCurve<T,I,B>(
									classT, classI, classB,
									vars.settlement, vars.bondHelpers,
									new Actual360(),
									new Handle/*<Quote>*/[0],
									new Date[0],
									1.0e-12,
									interpolator);
        
        curveHandle.linkTo(vars.termStructure);

        for (int i=0; i<vars.bonds; i++) {
            Date maturity = vars.calendar.advance(vars.today, bondData[i].n, bondData[i].units);
            Date issue = vars.calendar.advance(maturity, -bondData[i].length, TimeUnit.Years);
            /*@Rate*/ double[] coupons = new double[1];
            coupons[0] = bondData[i].coupon/100.0;

            FixedRateBond bond = new FixedRateBond(vars.bondSettlementDays, 100.0,
                               vars.schedules[i], coupons,
                               vars.bondDayCounter, vars.bondConvention,
                               vars.bondRedemption, issue);

            PricingEngine bondEngine = new DiscountingBondEngine(curveHandle);
            bond.setPricingEngine(bondEngine);

            /*@Real*/ double expectedPrice = bondData[i].price, estimatedPrice = bond.cleanPrice();
            /*@Real*/ double error = Math.abs(expectedPrice-estimatedPrice);
            if (error > tolerance) {
            	throw new RuntimeException(
            			String.format("#%d %s %s %f %s %f %s %f",
        					i+1, " bond failure:",
                            "\n  estimated price: ", estimatedPrice,
                            "\n  expected price:  ", expectedPrice,
                            "\n  error:           ", error));
            }
        }

        // check FRA
        vars.termStructure = new PiecewiseYieldCurve<T,I,B>(
        							classT, classI, classB,
        							vars.settlement, vars.fraHelpers,
                                    new Actual360(),
									new Handle/*<Quote>*/[0],
									new Date[0],
                                    1.0e-12,
                                    interpolator);
        curveHandle.linkTo(vars.termStructure);

        IborIndex euribor3m = new Euribor3M(curveHandle);
        for (int i=0; i<vars.fras; i++) {
            Date start = vars.calendar.advance(vars.settlement,
		                                       fraData[i].n,
		                                       fraData[i].units,
		                                       euribor3m.businessDayConvention(),
		                                       euribor3m.endOfMonth());
            Date end = vars.calendar.advance(start, 3, TimeUnit.Months,
                                             euribor3m.businessDayConvention(),
                                             euribor3m.endOfMonth());

            ForwardRateAgreement fra = new ForwardRateAgreement(start, end, Position.Long,
            													fraData[i].rate/100, 100.0,
            													euribor3m, curveHandle);
            /*@Rate*/ double expectedRate = fraData[i].rate/100, estimatedRate = fra.forwardRate().rate();
            if (Math.abs(expectedRate-estimatedRate) > tolerance) {
            	throw new RuntimeException(
            			String.format("#%d %s %s %f %s %f",
        					i+1, " FRA failure:",
                            "\n  estimated rate: ", estimatedRate,
                            "\n  expected rate:  ", expectedRate));
            }
        }
    }


//    template <class T, class I, template<class C> class B>
//    void testBMACurveConsistency(CommonVars& vars,
//                                 const I& interpolator = I(),
//                                 Real tolerance = 1.0e-9) {
//
//        // re-adjust settlement
//        vars.calendar = JointCalendar(BMAIndex().fixingCalendar(),
//                                      USDLibor(3*Months).fixingCalendar(),
//                                      JoinHolidays);
//        vars.today = vars.calendar.adjust(Date::todaysDate());
//        Settings::instance().evaluationDate() = vars.today;
//        vars.settlement =
//            vars.calendar.advance(vars.today,vars.settlementDays,Days);
//
//
//        Handle<YieldTermStructure> riskFreeCurve(
//            boost::shared_ptr<YieldTermStructure>(
//                        new FlatForward(vars.settlement, 0.04, Actual360())));
//
//        boost::shared_ptr<BMAIndex> bmaIndex(new BMAIndex);
//        boost::shared_ptr<IborIndex> liborIndex(
//                                        new USDLibor(3*Months,riskFreeCurve));
//        for (Size i=0; i<vars.bmas; ++i) {
//            Handle<Quote> f(vars.fractions[i]);
//            vars.bmaHelpers[i] = boost::shared_ptr<RateHelper>(
//                      new BMASwapRateHelper(f, bmaData[i].n*bmaData[i].units,
//                                            vars.settlementDays,
//                                            vars.calendar,
//                                            Period(vars.bmaFrequency),
//                                            vars.bmaConvention,
//                                            vars.bmaDayCounter,
//                                            bmaIndex,
//                                            liborIndex));
//        }
//
//        Weekday w = vars.today.weekday();
//        Date lastWednesday =
//            (w >= 4) ? vars.today - (w - 4) : vars.today + (4 - w - 7);
//        Date lastFixing = bmaIndex->fixingCalendar().adjust(lastWednesday);
//        bmaIndex->addFixing(lastFixing, 0.03);
//
//        vars.termStructure = boost::shared_ptr<YieldTermStructure>(new
//            PiecewiseYieldCurve<T,I,B>(vars.settlement, vars.bmaHelpers,
//                                       Actual360(),
//                                       std::vector<Handle<Quote> >(),
//                                       std::vector<Date>(),
//                                       1.0e-12,
//                                       interpolator));
//
//        RelinkableHandle<YieldTermStructure> curveHandle;
//        curveHandle.linkTo(vars.termStructure);
//
//        // check BMA swaps
//        boost::shared_ptr<BMAIndex> bma(new BMAIndex(curveHandle));
//        boost::shared_ptr<IborIndex> libor3m(new USDLibor(3*Months,
//                                                          riskFreeCurve));
//        for (Size i=0; i<vars.bmas; i++) {
//            Period tenor = bmaData[i].n*bmaData[i].units;
//
//            Schedule bmaSchedule = MakeSchedule(vars.settlement,
//                                                vars.settlement+tenor,
//                                                Period(vars.bmaFrequency),
//                                                bma->fixingCalendar(),
//                                                vars.bmaConvention).backwards();
//            Schedule liborSchedule = MakeSchedule(vars.settlement,
//                                                  vars.settlement+tenor,
//                                                  libor3m->tenor(),
//                                                  libor3m->fixingCalendar(),
//                                                  libor3m->businessDayConvention())
//                .endOfMonth(libor3m->endOfMonth())
//                .backwards();
//
//
//            BMASwap swap(BMASwap::Payer, 100.0,
//                         liborSchedule, 0.75, 0.0,
//                         libor3m, libor3m->dayCounter(),
//                         bmaSchedule, bma, vars.bmaDayCounter);
//            swap.setPricingEngine(boost::shared_ptr<PricingEngine>(
//                        new DiscountingSwapEngine(libor3m->termStructure())));
//
//            Real expectedFraction = bmaData[i].rate/100,
//                 estimatedFraction = swap.fairLiborFraction();
//            Real error = std::fabs(expectedFraction-estimatedFraction);
//            if (error > tolerance) {
//                BOOST_ERROR(bmaData[i].n << " year(s) BMA swap:\n"
//                            << std::setprecision(8)
//                            << "\n estimated libor fraction: " << estimatedFraction
//                            << "\n expected libor fraction:  " << expectedFraction
//                            << "\n error:          " << error
//                            << "\n tolerance:      " << tolerance);
//            }
//        }
//    }

    
//	@Test
//	public void testLogCubicDiscountConsistency() {
//
//	    BOOST_MESSAGE(
//	        "Testing consistency of piecewise-log-cubic discount curve...");
//
//	    CommonVars vars;
//
//	    testCurveConsistency<Discount,LogCubic,IterativeBootstrap>(
//	        vars,
//	        LogCubic(CubicInterpolation::Spline, true,
//	                 CubicInterpolation::SecondDerivative, 0.0,
//	                 CubicInterpolation::SecondDerivative, 0.0));
//	    testBMACurveConsistency<Discount,LogCubic,IterativeBootstrap>(
//	        vars,
//	        LogCubic(CubicInterpolation::Spline, true,
//	                 CubicInterpolation::SecondDerivative, 0.0,
//	                 CubicInterpolation::SecondDerivative, 0.0));
//	}
//
//	@Test
//	public void testLogLinearDiscountConsistency() {
//
//	    BOOST_MESSAGE(
//	        "Testing consistency of piecewise-log-linear discount curve...");
//
//	    CommonVars vars;
//
//	    testCurveConsistency<Discount,LogLinear,IterativeBootstrap>(vars);
//	    testBMACurveConsistency<Discount,LogLinear,IterativeBootstrap>(vars);
//	}
//
//	@Test
//	public void testLinearDiscountConsistency() {
//
//	    BOOST_MESSAGE(
//	        "Testing consistency of piecewise-linear discount curve...");
//
//	    CommonVars vars;
//
//	    testCurveConsistency<Discount,Linear,IterativeBootstrap>(vars);
//	    testBMACurveConsistency<Discount,Linear,IterativeBootstrap>(vars);
//	}
//
//	@Test
//	public void testLogLinearZeroConsistency() {
//
//	    BOOST_MESSAGE(
//	        "Testing consistency of piecewise-log-linear zero-yield curve...");
//
//	    CommonVars vars;
//
//	    testCurveConsistency<ZeroYield,LogLinear,IterativeBootstrap>(vars);
//	    testBMACurveConsistency<ZeroYield,LogLinear,IterativeBootstrap>(vars);
//	}
//
//	@Test
//	public void testLinearZeroConsistency() {
//
//	    BOOST_MESSAGE("Testing consistency of piecewise-linear zero-yield curve...");
//
//	    CommonVars vars;
//
//	    testCurveConsistency<ZeroYield,Linear,IterativeBootstrap>(vars);
//	    testBMACurveConsistency<ZeroYield,Linear,IterativeBootstrap>(vars);
//	}
//
//	@Test
//	public void testSplineZeroConsistency() {
//
//	    BOOST_MESSAGE("Testing consistency of piecewise-cubic zero-yield curve...");
//
//	    CommonVars vars;
//
//	    testCurveConsistency<ZeroYield,Cubic,IterativeBootstrap>(
//	                   vars,
//	                   Cubic(CubicInterpolation::Spline, true,
//	                         CubicInterpolation::SecondDerivative, 0.0,
//	                         CubicInterpolation::SecondDerivative, 0.0));
//	    testBMACurveConsistency<ZeroYield,Cubic,IterativeBootstrap>(
//	                   vars,
//	                   Cubic(CubicInterpolation::Spline, true,
//	                         CubicInterpolation::SecondDerivative, 0.0,
//	                         CubicInterpolation::SecondDerivative, 0.0));
//	}
//
//	@Test
//	public void testLinearForwardConsistency() {
//
//	    BOOST_MESSAGE("Testing consistency of piecewise-linear forward-rate curve...");
//
//	    CommonVars vars;
//
//	    testCurveConsistency<ForwardRate,Linear,IterativeBootstrap>(vars);
//	    testBMACurveConsistency<ForwardRate,Linear,IterativeBootstrap>(vars);
//	}
//
//	@Test
//	public void testFlatForwardConsistency() {
//
//	    BOOST_MESSAGE("Testing consistency of piecewise-flat forward-rate curve...");
//
//	    CommonVars vars;
//
//	    testCurveConsistency<ForwardRate,BackwardFlat,IterativeBootstrap>(vars);
//	    testBMACurveConsistency<ForwardRate,BackwardFlat,IterativeBootstrap>(vars);
//	}
//
//	@Test
//	public void testSplineForwardConsistency() {
//
//	    BOOST_MESSAGE("Testing consistency of piecewise-cubic forward-rate curve...");
//
//	    CommonVars vars;
//
//	    testCurveConsistency<ForwardRate,Cubic,IterativeBootstrap>(
//	                   vars,
//	                   Cubic(CubicInterpolation::Spline, true,
//	                         CubicInterpolation::SecondDerivative, 0.0,
//	                         CubicInterpolation::SecondDerivative, 0.0));
//	    testBMACurveConsistency<ForwardRate,Cubic,IterativeBootstrap>(
//	                   vars,
//	                   Cubic(CubicInterpolation::Spline, true,
//	                         CubicInterpolation::SecondDerivative, 0.0,
//	                         CubicInterpolation::SecondDerivative, 0.0));
//	}
//
//	@Test
//	public void testConvexMonotoneForwardConsistency() {
//	    BOOST_MESSAGE("Testing consistency of convex monotone forward-rate curve...");
//
//	    CommonVars vars;
//	    testCurveConsistency<ForwardRate,ConvexMonotone,IterativeBootstrap>(vars);
//
//	    testBMACurveConsistency<ForwardRate,ConvexMonotone,
//	                            IterativeBootstrap>(vars);
//	}
//
//
//	@Test
//	public void testLocalBootstrapConsistency() {
//	    BOOST_MESSAGE("Testing consistency of local-bootstrap algorithm...");
//
//	    CommonVars vars;
//	    testCurveConsistency<ForwardRate,ConvexMonotone,LocalBootstrap>(
//	                                              vars, ConvexMonotone(), 1.0e-7);
//	    testBMACurveConsistency<ForwardRate,ConvexMonotone,LocalBootstrap>(
//	                                              vars, ConvexMonotone(), 1.0e-7);
//	}
//
//
//	@Test
//	public void testObservability() {
//
//	    BOOST_MESSAGE("Testing observability of piecewise yield curve...");
//
//	    CommonVars vars;
//
//	    vars.termStructure = boost::shared_ptr<YieldTermStructure>(
//	       new PiecewiseYieldCurve<Discount,LogLinear>(vars.settlementDays,
//	                                                   vars.calendar,
//	                                                   vars.instruments,
//	                                                   Actual360()));
//	    Flag f;
//	    f.registerWith(vars.termStructure);
//
//	    for (Size i=0; i<vars.deposits+vars.swaps; i++) {
//	        Time testTime =
//	            Actual360().yearFraction(vars.settlement,
//	                                     vars.instruments[i]->latestDate());
//	        DiscountFactor discount = vars.termStructure->discount(testTime);
//	        f.lower();
//	        vars.rates[i]->setValue(vars.rates[i]->value()*1.01);
//	        if (!f.isUp())
//	            BOOST_FAIL("Observer was not notified of underlying rate change");
//	        if (vars.termStructure->discount(testTime,true) == discount)
//	            BOOST_FAIL("rate change did not trigger recalculation");
//	        vars.rates[i]->setValue(vars.rates[i]->value()/1.01);
//	    }
//
//	    f.lower();
//	    Settings::instance().evaluationDate() =
//	        vars.calendar.advance(vars.today,15,Days);
//	    if (!f.isUp())
//	        BOOST_FAIL("Observer was not notified of date change");
//	}
//
//
//	@Test
//	public void testLiborFixing() {
//
//	    BOOST_MESSAGE("Testing use of today's LIBOR fixings in swap curve...");
//
//	    CommonVars vars;
//
//	    std::vector<boost::shared_ptr<RateHelper> > swapHelpers(vars.swaps);
//	    boost::shared_ptr<IborIndex> euribor6m(new Euribor6M);
//
//	    for (Size i=0; i<vars.swaps; i++) {
//	        Handle<Quote> r(vars.rates[i+vars.deposits]);
//	        swapHelpers[i] = boost::shared_ptr<RateHelper>(new
//	            SwapRateHelper(r, Period(swapData[i].n, swapData[i].units),
//	                           vars.calendar,
//	                           vars.fixedLegFrequency, vars.fixedLegConvention,
//	                           vars.fixedLegDayCounter, euribor6m));
//	    }
//
//	    vars.termStructure = boost::shared_ptr<YieldTermStructure>(new
//	        PiecewiseYieldCurve<Discount,LogLinear>(vars.settlement,
//	                                                swapHelpers,
//	                                                Actual360()));
//
//	    Handle<YieldTermStructure> curveHandle =
//	        Handle<YieldTermStructure>(vars.termStructure);
//
//	    boost::shared_ptr<IborIndex> index(new Euribor6M(curveHandle));
//	    for (Size i=0; i<vars.swaps; i++) {
//	        Period tenor = swapData[i].n*swapData[i].units;
//
//	        VanillaSwap swap = MakeVanillaSwap(tenor, index, 0.0)
//	            .withEffectiveDate(vars.settlement)
//	            .withFixedLegDayCount(vars.fixedLegDayCounter)
//	            .withFixedLegTenor(Period(vars.fixedLegFrequency))
//	            .withFixedLegConvention(vars.fixedLegConvention)
//	            .withFixedLegTerminationDateConvention(vars.fixedLegConvention);
//
//	        Rate expectedRate = swapData[i].rate/100,
//	             estimatedRate = swap.fairRate();
//	        Real tolerance = 1.0e-9;
//	        if (std::fabs(expectedRate-estimatedRate) > tolerance) {
//	            BOOST_ERROR("before LIBOR fixing:\n"
//	                        << swapData[i].n << " year(s) swap:\n"
//	                        << std::setprecision(8)
//	                        << "    estimated rate: "
//	                        << io::rate(estimatedRate) << "\n"
//	                        << "    expected rate:  "
//	                        << io::rate(expectedRate));
//	        }
//	    }
//
//	    Flag f;
//	    f.registerWith(vars.termStructure);
//	    f.lower();
//
//	    index->addFixing(vars.today, 0.0425);
//
//	    if (!f.isUp())
//	        BOOST_ERROR("Observer was not notified of rate fixing");
//
//	    for (Size i=0; i<vars.swaps; i++) {
//	        Period tenor = swapData[i].n*swapData[i].units;
//
//	        VanillaSwap swap = MakeVanillaSwap(tenor, index, 0.0)
//	            .withEffectiveDate(vars.settlement)
//	            .withFixedLegDayCount(vars.fixedLegDayCounter)
//	            .withFixedLegTenor(Period(vars.fixedLegFrequency))
//	            .withFixedLegConvention(vars.fixedLegConvention)
//	            .withFixedLegTerminationDateConvention(vars.fixedLegConvention);
//
//	        Rate expectedRate = swapData[i].rate/100,
//	             estimatedRate = swap.fairRate();
//	        Real tolerance = 1.0e-9;
//	        if (std::fabs(expectedRate-estimatedRate) > tolerance) {
//	            BOOST_ERROR("after LIBOR fixing:\n"
//	                        << swapData[i].n << " year(s) swap:\n"
//	                        << std::setprecision(8)
//	                        << "    estimated rate: "
//	                        << io::rate(estimatedRate) << "\n"
//	                        << "    expected rate:  "
//	                        << io::rate(expectedRate));
//	        }
//	    }
//	}
//
//	@Test
//	public void testJpyLibor() {
//	    BOOST_MESSAGE("Testing bootstrap over JPY LIBOR swaps...");
//
//	    CommonVars vars;
//
//	    vars.today = Date(4, October, 2007);
//	    Settings::instance().evaluationDate() = vars.today;
//
//	    vars.calendar = Japan();
//	    vars.settlement =
//	        vars.calendar.advance(vars.today,vars.settlementDays,Days);
//
//	    // market elements
//	    vars.rates = std::vector<boost::shared_ptr<SimpleQuote> >(vars.swaps);
//	    for (Size i=0; i<vars.swaps; i++) {
//	        vars.rates[i] = boost::shared_ptr<SimpleQuote>(
//	                                       new SimpleQuote(swapData[i].rate/100));
//	    }
//
//	    // rate helpers
//	    vars.instruments = std::vector<boost::shared_ptr<RateHelper> >(vars.swaps);
//
//	    boost::shared_ptr<IborIndex> index(new JPYLibor(6*Months));
//	    for (Size i=0; i<vars.swaps; i++) {
//	        Handle<Quote> r(vars.rates[i]);
//	        vars.instruments[i] = boost::shared_ptr<RateHelper>(
//	           new SwapRateHelper(r, swapData[i].n*swapData[i].units,
//	                              vars.calendar,
//	                              vars.fixedLegFrequency, vars.fixedLegConvention,
//	                              vars.fixedLegDayCounter, index));
//	    }
//
//	    vars.termStructure = boost::shared_ptr<YieldTermStructure>(
//	        new PiecewiseYieldCurve<Discount,LogLinear>(
//	                                       vars.settlement, vars.instruments,
//	                                       Actual360(),
//	                                       std::vector<Handle<Quote> >(),
//	                                       std::vector<Date>(),
//	                                       1.0e-12));
//
//	    RelinkableHandle<YieldTermStructure> curveHandle;
//	    curveHandle.linkTo(vars.termStructure);
//
//	    // check swaps
//	    boost::shared_ptr<IborIndex> jpylibor6m(new JPYLibor(6*Months,curveHandle));
//	    for (Size i=0; i<vars.swaps; i++) {
//	        Period tenor = swapData[i].n*swapData[i].units;
//
//	        VanillaSwap swap = MakeVanillaSwap(tenor, jpylibor6m, 0.0)
//	            .withEffectiveDate(vars.settlement)
//	            .withFixedLegDayCount(vars.fixedLegDayCounter)
//	            .withFixedLegTenor(Period(vars.fixedLegFrequency))
//	            .withFixedLegConvention(vars.fixedLegConvention)
//	            .withFixedLegTerminationDateConvention(vars.fixedLegConvention)
//	            .withFixedLegCalendar(vars.calendar)
//	            .withFloatingLegCalendar(vars.calendar);
//
//	        Rate expectedRate = swapData[i].rate/100,
//	             estimatedRate = swap.fairRate();
//	        Spread error = std::fabs(expectedRate-estimatedRate);
//	        Real tolerance = 1.0e-9;
//
//	        if (error > tolerance) {
//	            BOOST_ERROR(swapData[i].n << " year(s) swap:\n"
//	                        << std::setprecision(8)
//	                        << "\n estimated rate: " << io::rate(estimatedRate)
//	                        << "\n expected rate:  " << io::rate(expectedRate)
//	                        << "\n error:          " << io::rate(error)
//	                        << "\n tolerance:      " << io::rate(tolerance));
//	        }
//	    }
//	}	

}
