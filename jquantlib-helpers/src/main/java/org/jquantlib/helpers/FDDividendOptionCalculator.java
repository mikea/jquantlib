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
import org.jquantlib.testsuite.util.Utilities;
import org.jquantlib.time.Date;


public abstract class FDDividendOptionCalculator<T extends FDEngineAdapter> extends DividendVanillaOption {

    public FDDividendOptionCalculator(
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

    public FDDividendOptionCalculator(
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
        final Handle<YieldTermStructure> qTS = new Handle<YieldTermStructure>(Utilities.flatRate(qRate, dc));
        final SimpleQuote rRate = new SimpleQuote(r);
        final Handle<YieldTermStructure> rTS = new Handle<YieldTermStructure>(Utilities.flatRate(rRate, dc));
        final SimpleQuote vol = new SimpleQuote(volatility);
        final Handle<BlackVolTermStructure> volTS = new Handle<BlackVolTermStructure>(Utilities.flatVol(vol, dc));

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
