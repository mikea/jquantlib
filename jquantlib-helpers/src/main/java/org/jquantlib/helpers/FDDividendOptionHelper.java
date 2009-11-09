package org.jquantlib.helpers;

import java.lang.reflect.Constructor;
import java.util.List;

import org.jquantlib.daycounters.Actual360;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.DividendVanillaOption;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.vanilla.finitedifferences.FDEngineAdapter;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.NullCalendar;


/**
 * Base helper class for European and American dividend options using the finite difference engines.
 * <p>
 * Beware that this helper class implies that {@link Actual360} day counter and a {@link NullCalendar} calendar
 * will be used, among other assumptions.
 *
 * @see <a href="http://www.jquantlib.org/sites/jquantlib-helpers/xref/org/jquantlib/helpers/FDDividendOptionHelper.html">Source code</a>
 *
 * @author Richard Gomes
 */
public abstract class FDDividendOptionHelper<T extends FDEngineAdapter> extends DividendVanillaOption {

    /**
     * Constructor for both European and American dividend options helper class using the finite differences engine
     * <p>
     * Beware that this helper class implies that {@link Actual360} day counter and a {@link NullCalendar} calendar
     * will be used, among other assumptions.
     *
     * @param type is the option call type (Call/Put)
     * @param underlying is the price of the underlying asset
     * @param strike is the strike price at expiration
     * @param r is the risk free rate
     * @param vol is the volatility
     * @param expiry is the expiration date
     * @param dividendDates is a list of dates when dividends are expected to be paid
     * @param dividends is a list of dividends amounts (as a pure value) expected to be paid
     *
     * @see <a href="http://www.jquantlib.org/sites/jquantlib-helpers/xref/org/jquantlib/helpers/FDDividendOptionHelper.html">Source code</a>
     */
    public FDDividendOptionHelper(
            final Class<T> engineClass,                 // option style
            final Option.Type type,                     // option type (call/put)
            final /*@Real*/ double underlying,          // underlying
            final /*@Real*/ double strike,              // strike price
            final /*@Rate*/ double r,                   // risk free interest rate
            final /*@Volatility*/ double volatility,    // volatility
            final Exercise exercise,                    // Exercise date(s)
            final List<Date> dividendDates,             // dividend dates
            final List<Double> dividends) {             // dividend amounts
        this(engineClass, type, underlying, strike, r, volatility, exercise, dividendDates, dividends, 0.0);
    }

    /**
     * Constructor for both European and American dividend options helper class using the finite differences engine
     * <p>
     * Beware that this helper class implies that {@link Actual360} day counter and a {@link NullCalendar} calendar
     * will be used, among other assumptions.
     *
     * @param type is the option call type (Call/Put)
     * @param underlying is the price of the underlying asset
     * @param strike is the strike price at expiration
     * @param r is the risk free rate
     * @param vol is the volatility
     * @param expiry is the expiration date
     * @param dividendDates is a list of dates when dividends are expected to be paid
     * @param dividends is a list of dividends amounts (as a pure value) expected to be paid
     * @param q is the yield rate
     *
     * @see <a href="http://www.jquantlib.org/sites/jquantlib-helpers/xref/org/jquantlib/helpers/FDDividendOptionHelper.html">Source code</a>
     */
    public FDDividendOptionHelper(
            final Class<T> engineClass,                 // option style
            final Option.Type type,                     // option type (call/put)
            final /*@Real*/ double underlying,          // underlying
            final /*@Real*/ double strike,              // strike price
            final /*@Rate*/ double r,                   // risk free interest rate
            final /*@Volatility*/ double volatility,    // volatility
            final Exercise exercise,                    // Exercise date(s)
            final List<Date> dividendDates,             // dividend dates
            final List<Double> dividends,               // dividend amounts
            final /*@Rate*/ double q) {                 // dividend interest rate

        super(new PlainVanillaPayoff(type, strike), exercise, dividendDates, dividends);

        final DayCounter dc = new Actual360();
        final SimpleQuote spot = new SimpleQuote(underlying);

        final SimpleQuote qRate = new SimpleQuote(q);
        final YieldTermStructure qFlatRate = new FlatForward(0, new NullCalendar(), new Handle<Quote>(qRate), dc);
        final Handle<YieldTermStructure> qTS = new Handle<YieldTermStructure>(qFlatRate);

        final SimpleQuote rRate = new SimpleQuote(r);
        final YieldTermStructure rFlatRate = new FlatForward(0, new NullCalendar(), new Handle<Quote>(rRate), dc);
        final Handle<YieldTermStructure> rTS = new Handle<YieldTermStructure>(rFlatRate);

        final SimpleQuote vol = new SimpleQuote(volatility);
        final BlackVolTermStructure flatVol = new BlackConstantVol(0, new NullCalendar(), new Handle<Quote>(vol), dc);
        final Handle<BlackVolTermStructure> volTS = new Handle<BlackVolTermStructure>(flatVol);

        final BlackScholesMertonProcess stochProcess = new BlackScholesMertonProcess(new Handle<Quote>(spot), qTS, rTS, volTS);
        final PricingEngine engine;
        try {
            final Constructor<T> baseConstructor = engineClass.getConstructor(GeneralizedBlackScholesProcess.class);
            engine = baseConstructor.newInstance(stochProcess);
        } catch (final Exception e) {
            throw new LibraryException(e);
        }
        this.setPricingEngine(engine);
    }

}
