package org.jquantlib.samples;



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.Actual360;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.ActualActual;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.daycounters.Thirty360;
import org.jquantlib.daycounters.Thirty360.Convention;
import org.jquantlib.indexes.Euribor6M;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.AbstractYieldTermStructure;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.yieldcurves.DepositRateHelper;
import org.jquantlib.termstructures.yieldcurves.FixedRateBondHelper;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.DateGeneration;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Month;
import org.jquantlib.time.Period;
import org.jquantlib.time.Schedule;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.time.calendars.UnitedStates;

public class Bonds implements Runnable {

    public static void main(final String[] args) {
        new Bonds().run();
    }

    @Override
    public void run() {

        QL.info("::::: " + this.getClass().getSimpleName() + " :::::");

        /*********************
         *** MARKET DATA ***
         *********************/

        final Calendar calendar = new Target();

        // FIXME: outdated...
        Date settlementDate = new Date(18, Month.September, 2008);
        // must be a business day
        settlementDate = calendar.adjust(settlementDate);

        final int fixingDays = 3;
        /* Natural */final int settlementDays = 3;

        final Date todaysDate = calendar.advance(settlementDate, -fixingDays, TimeUnit.Days);
        new Settings().setEvaluationDate(todaysDate);

        System.out.println("Today: " + todaysDate.weekday() + ", " + todaysDate);

        System.out.println("Settlement date: " + settlementDate.weekday() + ", " + settlementDate);

        // Building of the bonds discounting yield curve

        /*********************
         *** RATE HELPERS ***
         *********************/

        // RateHelpers are built from the above quotes together with
        // other instrument dependant infos. Quotes are passed in
        // relinkable handles which could be relinked to some other
        // data source later.
        // Common data
        // ZC rates for the short end
        /* @Rate */final double zc3mQuote = 0.0096;
        /* @Rate */final double zc6mQuote = 0.0145;
        /* @Rate */final double zc1yQuote = 0.0194;

        final Quote zc3mRate = (new SimpleQuote(zc3mQuote));
        final Quote zc6mRate = (new SimpleQuote(zc6mQuote));
        final Quote zc1yRate = (new SimpleQuote(zc1yQuote));

        final DayCounter zcBondsDayCounter = new Actual365Fixed();

        final RateHelper zc3m = new DepositRateHelper(
                new Handle<Quote>(zc3mRate), new Period(3, TimeUnit.Months),
                fixingDays, calendar,
                BusinessDayConvention.ModifiedFollowing, true, zcBondsDayCounter);
        final RateHelper zc6m = new DepositRateHelper(
                new Handle<Quote>(zc6mRate), new Period(6, TimeUnit.Months),
                fixingDays, calendar,
                BusinessDayConvention.ModifiedFollowing, true, zcBondsDayCounter);
        final RateHelper zc1y = new DepositRateHelper(
                new Handle<Quote>(zc1yRate), new Period(1, TimeUnit.Years),
                fixingDays, calendar,
                BusinessDayConvention.ModifiedFollowing, true, zcBondsDayCounter);

        // setup bonds
        /* @Real */final double redemption = 100.0;

        final/* @Size */int numberOfBonds = 5;

        final Date issueDates[] = {
                new Date(15, Month.March,    2005),
                new Date(15, Month.June,     2005),
                new Date(30, Month.June,     2006),
                new Date(15, Month.November, 2002),
                new Date(15, Month.May,      1987) };

        final Date maturities[] = {
                new Date(31, Month.August, 2010),
                new Date(31, Month.August, 2011),
                new Date(31, Month.August, 2013),
                new Date(15, Month.August, 2018),
                new Date(15, Month.May,    2038) };

        /* @Real */final double couponRates[] = { 0.02375, 0.04625, 0.03125, 0.04000, 0.04500 };

        /* @Real */final double marketQuotes[] = { 100.390625, 106.21875, 100.59375, 101.6875, 102.140625 };

        final List<SimpleQuote> quote = new LinkedList<SimpleQuote>();
        for (/* @Size */int i = 0; i < numberOfBonds; i++) {
            final SimpleQuote cp = (new SimpleQuote(marketQuotes[i]));
            quote.add(cp);
        }

        final List<RelinkableHandle<Quote>> quoteHandle = new ArrayList<RelinkableHandle<Quote>>(numberOfBonds);
        for(int i = 0; i<numberOfBonds; i++) {
            // quoteHandle.add(new RelinkableHandle<Quote>(Quote.class)); //FIXME::RG::Handle

            final Quote nullQuote = new Quote() {
                @Override
                public boolean isValid() {
                    throw new UnsupportedOperationException();
                }
                @Override
                public double value() {
                    throw new UnsupportedOperationException();
                }
            };
            quoteHandle.add(new RelinkableHandle<Quote>(nullQuote));
        }

        for (/* @Size */int i = 0; i < numberOfBonds; i++) {
            quoteHandle.get(i).linkTo(quote.get(i));
        }

        // Definition of the rate helpers
        final List<FixedRateBondHelper> bondsHelpers = new LinkedList<FixedRateBondHelper>();

        for (/* @Size */int i = 0; i < numberOfBonds; i++) {
            // TODO: check this constructor, last two date parameters shouldn't
            // be passed
            final Schedule schedule = new Schedule(
                    issueDates[i], maturities[i],
                    new Period(Frequency.Semiannual),
                    new UnitedStates(UnitedStates.Market.GOVERNMENTBOND),
                    BusinessDayConvention.Unadjusted,
                    BusinessDayConvention.Unadjusted,
                    DateGeneration.Rule.Backward,
                    false);
            final FixedRateBondHelper bondHelper = (
                    new FixedRateBondHelper(quoteHandle.get(i), settlementDays,
                            100.0, schedule,
                            new double[] { couponRates[i] },
                            new ActualActual(ActualActual.Convention.Bond),
                            BusinessDayConvention.Unadjusted,
                            redemption,
                            issueDates[i]));

            bondsHelpers.add(bondHelper);
        }

        /*********************
         ** CURVE BUILDING **
         *********************/

        // Any DayCounter would be fine.
        // ActualActual::ISDA ensures that 30 years is 30.0
        final DayCounter termStructureDayCounter = new ActualActual(ActualActual.Convention.ISDA);

        final double tolerance = 1.0e-15;

        // A depo-bond curve
        final List<RateHelper> bondInstruments = new LinkedList<RateHelper>();

        // Adding the ZC bonds to the curve for the short end
        bondInstruments.add(zc3m);
        bondInstruments.add(zc6m);
        bondInstruments.add(zc1y);

        // Adding the Fixed rate bonds to the curve for the long end
        for (/* @Size */int i = 0; i < numberOfBonds; i++) {
            bondInstruments.add(bondsHelpers.get(i));
        }

        //		YieldTermStructure bondDiscountingTermStructur = new PiecewiseYieldCurve<Discount, LogLinear>
        //		(settlementDate, bondInstruments, termStructureDayCounter, tolerance, null);

        // TODO: PieceWiseYieldTermStructure to be translated by Richard!
        // YieldTermStructure bondDiscountingTermStructure = new
        // YieldTermStructure(
        // new PiecewiseYieldCurve<Discount,LogLinear>(
        // settlementDate, bondInstruments,
        // termStructureDayCounter,
        // std::vector<Handle<Quote> >(),
        // std::vector<Date>(),
        // tolerance));

        // Building of the Libor forecasting curve
        // deposits
        /* @Rate */final double d1wQuote = 0.043375;
        /* @Rate */final double d1mQuote = 0.031875;
        /* @Rate */final double d3mQuote = 0.0320375;
        /* @Rate */final double d6mQuote = 0.03385;
        /* @Rate */final double d9mQuote = 0.0338125;
        /* @Rate */final double d1yQuote = 0.0335125;
        // swaps
        /* @Rate */final double s2yQuote = 0.0295;
        /* @Rate */final double s3yQuote = 0.0323;
        /* @Rate */final double s5yQuote = 0.0359;
        /* @Rate */final double s10yQuote = 0.0412;
        /* @Rate */final double s15yQuote = 0.0433;

        /********************
         *** QUOTES ***
         ********************/

        // SimpleQuote stores a value which can be manually changed;
        // other Quote subclasses could read the value from a database
        // or some kind of data feed.

        // deposits
        final Quote d1wRate = (new SimpleQuote(d1wQuote));
        final Quote d1mRate = (new SimpleQuote(d1mQuote));
        final Quote d3mRate = (new SimpleQuote(d3mQuote));
        final Quote d6mRate = (new SimpleQuote(d6mQuote));
        final Quote d9mRate = (new SimpleQuote(d9mQuote));
        final Quote d1yRate = (new SimpleQuote(d1yQuote));
        // swaps
        final Quote s2yRate = (new SimpleQuote(s2yQuote));
        final Quote s3yRate = (new SimpleQuote(s3yQuote));
        final Quote s5yRate = (new SimpleQuote(s5yQuote));
        final Quote s10yRate = (new SimpleQuote(s10yQuote));
        final Quote s15yRate = (new SimpleQuote(s15yQuote));

        /*********************
         *** RATE HELPERS ***
         *********************/

        // RateHelpers are built from the above quotes together with
        // other instrument dependant infos. Quotes are passed in
        // relinkable handles which could be relinked to some other
        // data source later.

        // deposits
        final DayCounter depositDayCounter = new Actual360();

        final RateHelper d1w = (new DepositRateHelper(new Handle<Quote>(d1wRate), new Period(1, TimeUnit.Weeks), fixingDays, calendar,
                BusinessDayConvention.ModifiedFollowing, true, depositDayCounter));
        final RateHelper d1m = (new DepositRateHelper(new Handle<Quote>(d1mRate), new Period(1, TimeUnit.Months), fixingDays, calendar,
                BusinessDayConvention.ModifiedFollowing, true, depositDayCounter));
        final RateHelper d3m = (new DepositRateHelper(new Handle<Quote>(d3mRate), new Period(3, TimeUnit.Months), fixingDays, calendar,
                BusinessDayConvention.ModifiedFollowing, true, depositDayCounter));
        final RateHelper d6m = (new DepositRateHelper(new Handle<Quote>(d6mRate), new Period(6, TimeUnit.Months), fixingDays, calendar,
                BusinessDayConvention.ModifiedFollowing, true, depositDayCounter));
        final RateHelper d9m = (new DepositRateHelper(new Handle<Quote>(d9mRate), new Period(9, TimeUnit.Months), fixingDays, calendar,
                BusinessDayConvention.ModifiedFollowing, true, depositDayCounter));
        final RateHelper d1y = (new DepositRateHelper(new Handle<Quote>(d1yRate), new Period(1, TimeUnit.Years), fixingDays, calendar,
                BusinessDayConvention.ModifiedFollowing, true, depositDayCounter));

        // setup swaps
        final Frequency swFixedLegFrequency = Frequency.Annual;
        final BusinessDayConvention swFixedLegConvention = BusinessDayConvention.Unadjusted;
        final DayCounter swFixedLegDayCounter = new Thirty360(Convention.European);


        // TODO and FIXME: not sure whether the class stuff works properly
        // final IborIndex swFloatingLegIndex = Euribor.getEuribor6M(new Handle<YieldTermStructure>(YieldTermStructure.class)); //FIXME::RG::Handle

        final YieldTermStructure nullYieldTermStructure = new AbstractYieldTermStructure() {
            @Override
            protected double discountImpl(final double t) {
                throw new UnsupportedOperationException();
            }
            @Override
            public Date maxDate() {
                throw new UnsupportedOperationException();
            }
        };
        final IborIndex swFloatingLegIndex = new Euribor6M(new Handle<YieldTermStructure>(nullYieldTermStructure));


        final Period forwardStart = new Period(1, TimeUnit.Days);

        // RateHelper s2y = (new SwapRateHelper(
        // new Handle<Quote>(s2yRate), new Period(2, TimeUnit.YEARS),
        // calendar, swFixedLegFrequency,
        // swFixedLegConvention, swFixedLegDayCounter,
        // swFloatingLegIndex, new Handle<Quote>(Quote.class),forwardStart));
        // RateHelper> s3y(new SwapRateHelper(
        // Handle<Quote>(s3yRate), 3*Years,
        // calendar, swFixedLegFrequency,
        // swFixedLegConvention, swFixedLegDayCounter,
        // swFloatingLegIndex, Handle<Quote>(),forwardStart));
        // boost::shared_ptr<RateHelper> s5y(new SwapRateHelper(
        // Handle<Quote>(s5yRate), 5*Years,
        // calendar, swFixedLegFrequency,
        // swFixedLegConvention, swFixedLegDayCounter,
        // swFloatingLegIndex, Handle<Quote>(),forwardStart));
        // boost::shared_ptr<RateHelper> s10y(new SwapRateHelper(
        // Handle<Quote>(s10yRate), 10*Years,
        // calendar, swFixedLegFrequency,
        // swFixedLegConvention, swFixedLegDayCounter,
        // swFloatingLegIndex, Handle<Quote>(),forwardStart));
        // boost::shared_ptr<RateHelper> s15y(new SwapRateHelper(
        // Handle<Quote>(s15yRate), 15*Years,
        // calendar, swFixedLegFrequency,
        // swFixedLegConvention, swFixedLegDayCounter,
        // swFloatingLegIndex, Handle<Quote>(),forwardStart));

        // /*********************
        // ** CURVE BUILDING **
        // *********************/
        //
        // // Any DayCounter would be fine.
        // // ActualActual::ISDA ensures that 30 years is 30.0
        //
        // // A depo-swap curve
        // std::vector<boost::shared_ptr<RateHelper> > depoSwapInstruments;
        // depoSwapInstruments.push_back(d1w);
        // depoSwapInstruments.push_back(d1m);
        // depoSwapInstruments.push_back(d3m);
        // depoSwapInstruments.push_back(d6m);
        // depoSwapInstruments.push_back(d9m);
        // depoSwapInstruments.push_back(d1y);
        // depoSwapInstruments.push_back(s2y);
        // depoSwapInstruments.push_back(s3y);
        // depoSwapInstruments.push_back(s5y);
        // depoSwapInstruments.push_back(s10y);
        // depoSwapInstruments.push_back(s15y);
        // boost::shared_ptr<YieldTermStructure> depoSwapTermStructure(
        // new PiecewiseYieldCurve<Discount,LogLinear>(
        // settlementDate, depoSwapInstruments,
        // termStructureDayCounter,
        // std::vector<Handle<Quote> >(),
        // std::vector<Date>(),
        // tolerance));
        //
        // // Term structures that will be used for pricing:
        // // the one used for discounting cash flows
        // RelinkableHandle<YieldTermStructure> discountingTermStructure;
        // // the one used for forward rate forecasting
        // RelinkableHandle<YieldTermStructure> forecastingTermStructure;
        //
        // /*********************
        // * BONDS TO BE PRICED *
        // **********************/
        //
        // // Common data
        // Real faceAmount = 100;
        //
        // // Pricing engine
        // boost::shared_ptr<PricingEngine> bondEngine(
        // new DiscountingBondEngine(discountingTermStructure));
        //
        // // Zero coupon bond
        // ZeroCouponBond zeroCouponBond(
        // settlementDays,
        // UnitedStates(UnitedStates::GovernmentBond),
        // faceAmount,
        // Date(15,August,2013),
        // Following,
        // Real(116.92),
        // Date(15,August,2003));
        //
        // zeroCouponBond.setPricingEngine(bondEngine);
        //
        // // Fixed 4.5% US Treasury Note
        // Schedule fixedBondSchedule(Date(15, May, 2007),
        // Date(15,May,2017), Period(Semiannual),
        // UnitedStates(UnitedStates::GovernmentBond),
        // Unadjusted, Unadjusted, DateGeneration::Backward, false);
        //
        // FixedRateBond fixedRateBond(
        // settlementDays,
        // faceAmount,
        // fixedBondSchedule,
        // std::vector<Rate>(1, 0.045),
        // ActualActual(ActualActual::Bond),
        // BusinessDayConvention.MODIFIED_FOLLOWING,
        // 100.0, Date(15, May, 2007));
        //
        // fixedRateBond.setPricingEngine(bondEngine);
        //
        // // Floating rate bond (3M USD Libor + 0.1%)
        // // Should and will be priced on another curve later...
        //
        // RelinkableHandle<YieldTermStructure> liborTermStructure;
        // const boost::shared_ptr<IborIndex> libor3m(
        // new USDLibor(Period(3,Months),liborTermStructure));
        // libor3m->addFixing(Date(17, July, 2008),0.0278625);
        //
        // Schedule floatingBondSchedule(Date(21, October, 2005),
        // Date(21, October, 2010), Period(Quarterly),
        // UnitedStates(UnitedStates::NYSE),
        // Unadjusted, Unadjusted, DateGeneration::Backward, true);
        //
        // FloatingRateBond floatingRateBond(
        // settlementDays,
        // faceAmount,
        // floatingBondSchedule,
        // libor3m,
        // Actual360(),
        // BusinessDayConvention.MODIFIED_FOLLOWING,
        // Natural(2),
        // // Gearings
        // std::vector<Real>(1, 1.0),
        // // Spreads
        // std::vector<Rate>(1, 0.001),
        // // Caps
        // std::vector<Rate>(),
        // // Floors
        // std::vector<Rate>(),
        // // Fixing in arrears
        // true,
        // Real(100.0),
        // Date(21, October, 2005));
        //
        // floatingRateBond.setPricingEngine(bondEngine);
        //
        // // Coupon pricers
        // boost::shared_ptr<IborCouponPricer> pricer(new
        // BlackIborCouponPricer);
        //
        // // optionLet volatilities
        // Volatility volatility = 0.0;
        // Handle<OptionletVolatilityStructure> vol;
        // vol = Handle<OptionletVolatilityStructure>(
        // boost::shared_ptr<OptionletVolatilityStructure>(new
        // ConstantOptionletVolatility(
        // settlementDays,
        // calendar,
        // BusinessDayConvention.MODIFIED_FOLLOWING,
        // volatility,
        // Actual365Fixed())));
        //
        // pricer->setCapletVolatility(vol);
        // setCouponPricer(floatingRateBond.cashflows(),pricer);
        //
        // // Yield curve bootstrapping
        // forecastingTermStructure.linkTo(depoSwapTermStructure);
        // discountingTermStructure.linkTo(bondDiscountingTermStructure);
        //
        // // We are using the depo & swap curve to estimate the future Libor
        // rates
        // liborTermStructure.linkTo(depoSwapTermStructure);
        //
        // /***************
        // * BOND PRICING *
        // ****************/
        //
        // std::cout << std::endl;
        //
        // // write column headings
        // Size widths[] = { 18, 10, 10, 10 };
        //
        // std::cout << std::setw(widths[0]) << "                 "
        // << std::setw(widths[1]) << "ZC"
        // << std::setw(widths[2]) << "Fixed"
        // << std::setw(widths[3]) << "Floating"
        // << std::endl;
        //
        // std::string separator = " | ";
        // Size width = widths[0]
        // + widths[1]
        // + widths[2]
        // + widths[3];
        // std::string rule(width, '-'), dblrule(width, '=');
        // std::string tab(8, ' ');
        //
        // std::cout << rule << std::endl;
        //
        // std::cout << std::fixed;
        // std::cout << std::setprecision(2);
        //
        // std::cout << std::setw(widths[0]) << "Net present value"
        // << std::setw(widths[1]) << zeroCouponBond.NPV()
        // << std::setw(widths[2]) << fixedRateBond.NPV()
        // << std::setw(widths[3]) << floatingRateBond.NPV()
        // << std::endl;
        //
        // std::cout << std::setw(widths[0]) << "Clean price"
        // << std::setw(widths[1]) << zeroCouponBond.cleanPrice()
        // << std::setw(widths[2]) << fixedRateBond.cleanPrice()
        // << std::setw(widths[3]) << floatingRateBond.cleanPrice()
        // << std::endl;
        //
        // std::cout << std::setw(widths[0]) << "Dirty price"
        // << std::setw(widths[1]) << zeroCouponBond.dirtyPrice()
        // << std::setw(widths[2]) << fixedRateBond.dirtyPrice()
        // << std::setw(widths[3]) << floatingRateBond.dirtyPrice()
        // << std::endl;
        //
        // std::cout << std::setw(widths[0]) << "Accrued coupon"
        // << std::setw(widths[1]) << zeroCouponBond.accruedAmount()
        // << std::setw(widths[2]) << fixedRateBond.accruedAmount()
        // << std::setw(widths[3]) << floatingRateBond.accruedAmount()
        // << std::endl;
        //
        // std::cout << std::setw(widths[0]) << "Previous coupon"
        // << std::setw(widths[1]) << "N/A" // zeroCouponBond
        // << std::setw(widths[2]) << io::rate(fixedRateBond.previousCoupon())
        // << std::setw(widths[3]) <<
        // io::rate(floatingRateBond.previousCoupon())
        // << std::endl;
        //
        // std::cout << std::setw(widths[0]) << "Next coupon"
        // << std::setw(widths[1]) << "N/A" // zeroCouponBond
        // << std::setw(widths[2]) << io::rate(fixedRateBond.nextCoupon())
        // << std::setw(widths[3]) << io::rate(floatingRateBond.nextCoupon())
        // << std::endl;
        //
        // std::cout << std::setw(widths[0]) << "Yield"
        // << std::setw(widths[1])
        // << io::rate(zeroCouponBond.yield(Actual360(),Compounded,Annual))
        // << std::setw(widths[2])
        // << io::rate(fixedRateBond.yield(Actual360(),Compounded,Annual))
        // << std::setw(widths[3])
        // << io::rate(floatingRateBond.yield(Actual360(),Compounded,Annual))
        // << std::endl;
        //
        // std::cout << std::endl;
        //
        // // Other computations
        // std::cout <<
        // "Sample indirect computations (for the floating rate bond): " <<
        // std::endl;
        // std::cout << rule << std::endl;
        //
        // std::cout << "Yield to Clean Price: "
        // <<
        // floatingRateBond.cleanPrice(floatingRateBond.yield(Actual360(),Compounded,Annual),Actual360(),Compounded,Annual,settlementDate)
        // << std::endl;
        //
        // std::cout << "Clean Price to Yield: "
        // <<
        // io::rate(floatingRateBond.yield(floatingRateBond.cleanPrice(),Actual360(),Compounded,Annual,settlementDate))
        // << std::endl;
        //
        // /* "Yield to Price"
        // "Price to Yield" */
        //
        // Real seconds = timer.elapsed();
        // Integer hours = int(seconds/3600);
        // seconds -= hours * 3600;
        // Integer minutes = int(seconds/60);
        // seconds -= minutes * 60;
        // std::cout << " \nRun completed in ";
        // if (hours > 0)
        // std::cout << hours << " h ";
        // if (hours > 0 || minutes > 0)
        // std::cout << minutes << " m ";
        // std::cout << std::fixed << std::setprecision(0)
        // << seconds << " s\n" << std::endl;
        //
        // return 0;
        //
        // } catch (std::exception& e) {
        // std::cout << e.what() << std::endl;
        // return 1;
        // } catch (...) {
        // std::cout << "unknown error" << std::endl;
        // return 1;
        // }
    }

}
