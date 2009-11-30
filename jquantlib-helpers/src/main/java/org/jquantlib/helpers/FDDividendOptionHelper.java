package org.jquantlib.helpers;

import java.lang.reflect.Constructor;
import java.util.List;

import org.jquantlib.Settings;
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
import org.jquantlib.processes.HullWhiteProcess;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Calendar;
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

    final private BlackScholesMertonProcess stochProcess;
    final private Calendar    cal;
    final private DayCounter  dc;
    final private SimpleQuote qRate;
    final private SimpleQuote rRate;
    final private SimpleQuote vol;


    public FDDividendOptionHelper(
            final Class<T> engineClass,
            final Option.Type type,
            final /*@Real*/ double u,
            final /*@Real*/ double strike,
            final /*@Rate*/ double r,
            final /*@Volatility*/ double v,
            final Exercise exercise,
            final List<Date> dividendDates,
            final List<Double> dividends) {
        this(engineClass, type, u, strike, r, v, exercise, dividendDates, dividends, 0.0, new NullCalendar(), new Actual360());
    }

    public FDDividendOptionHelper(
            final Class<T> engineClass,
            final Option.Type type,
            final /*@Real*/ double u,
            final /*@Real*/ double strike,
            final /*@Rate*/ double r,
            final /*@Volatility*/ double v,
            final Exercise exercise,
            final List<Date> dividendDates,
            final List<Double> dividends,
            final /*@Rate*/ double q) {
        this(engineClass, type, u, strike, r, v, exercise, dividendDates, dividends, q, new NullCalendar(), new Actual360());
    }

    public FDDividendOptionHelper(
            final Class<T> engineClass,
            final Option.Type type,
            final /*@Real*/ double u,
            final /*@Real*/ double strike,
            final /*@Rate*/ double r,
            final /*@Volatility*/ double v,
            final Exercise exercise,
            final List<Date> dividendDates,
            final List<Double> dividends,
            final /*@Rate*/ double q,
            final Calendar cal) {
        this(engineClass, type, u, strike, r, v, exercise, dividendDates, dividends, q, cal, new Actual360());
    }

    /**
     * Constructor for both European and American dividend options helper class using the finite differences engine
     * <p>
     * Beware that this helper class implies that {@link Actual360} day counter and a {@link NullCalendar} calendar
     * will be used, among other assumptions.
     *
     * @param type is the option call type (Call/Put)
     * @param u is the price of the underlying asset
     * @param strike is the strike price at expiration
     * @param r is the risk free rate
     * @param vol is the volatility
     * @param expiry is the expiration date
     * @param dividendDates is a list of dates when dividends are expected to be paid
     * @param dividends is a list of dividends amounts (as a pure value) expected to be paid
     * @param q is the yield rate. The default is zero.
     * @param cal is a Calendar. The default is a {@link NullCalendar}
     * @param dc is a DayCounter. The default is {@link Actual360}
     *
     * @see <a href="http://www.jquantlib.org/sites/jquantlib-helpers/xref/org/jquantlib/helpers/FDDividendOptionHelper.html">Source code</a>
     */
    public FDDividendOptionHelper(
            final Class<T> engineClass,
            final Option.Type type,
            final /*@Real*/ double u,
            final /*@Real*/ double strike,
            final /*@Rate*/ double r,
            final /*@Volatility*/ double v,
            final Exercise exercise,
            final List<Date> dividendDates,
            final List<Double> dividends,
            final /*@Rate*/ double q,
            final Calendar cal,
            final DayCounter dc) {

        super(new PlainVanillaPayoff(type, strike), exercise, dividendDates, dividends);

        this.cal = cal;
        this.dc = dc;

        final SimpleQuote spot = new SimpleQuote(0.0);
        this.qRate = new SimpleQuote(0.0);
        this.rRate = new SimpleQuote(0.0);
        this.vol = new SimpleQuote(0.0);

        final Handle<YieldTermStructure> qTS = new Handle<YieldTermStructure>(flatRate(qRate, cal, dc));
        final Handle<YieldTermStructure> rTS = new Handle<YieldTermStructure>(flatRate(rRate, cal, dc));
        final Handle<BlackVolTermStructure> volTS = new Handle<BlackVolTermStructure>(flatVol(vol, cal, dc));

        final StochasticProcess1D hw = new HullWhiteProcess(qTS, 0.0, 0.0);


        // obtain stochastic process
        this.stochProcess = new BlackScholesMertonProcess(new Handle<Quote>(spot), qTS, rTS, volTS);

        // obtain a pricing engine and assign to this option
        final PricingEngine engine;
        try {
            final Constructor<T> baseConstructor = engineClass.getConstructor(GeneralizedBlackScholesProcess.class
                    /* , int.class, int.class */ );
            engine = baseConstructor.newInstance(stochProcess /* , timeSteps, gridPoints */);
        } catch (final Exception e) {
            throw new LibraryException(e);
        }

        // assign the pricing engine to this option
        this.setPricingEngine(engine);

        // assign new values to spot, qRate, rRate and vol
        // note: this step is needed in order to notify observers
        spot.setValue(u);
        qRate.setValue(q);
        rRate.setValue(r);
        vol.setValue(v);
    }

    @Override
    public double theta() {
        final Settings settings = new Settings();
        final Date evalDate = settings.evaluationDate();
        // perturb date and get theta
        final Date yesterday = evalDate.sub(1);
        final Date tomorrow  = evalDate.add(1);
        final double dT = dc.yearFraction(yesterday, tomorrow);
        settings.setEvaluationDate(yesterday);
        final double value_m = super.NPV();
        settings.setEvaluationDate(tomorrow);
        final double value_p = super.NPV();
        // System.out.println(String.format("theta  = %f", (value_p - value_m)/dT));
        // final double theta = super.theta();
        final double theta = (value_p - value_m)/dT;
        settings.setEvaluationDate(evalDate);
        return theta;
    }

    @Override
    public double vega() {
        // perturb volatility and get vega
        final double v = vol.value();
        final double dv = v*1.0e-4;
        vol.setValue(v+dv);
        final double value_p = super.NPV();
        vol.setValue(v-dv);
        final double value_m = super.NPV();
        // System.out.println(String.format("vega  = %f", (value_p - value_m)/(2*dv)));
        // final double vega = super.vega();
        final double vega = (value_p - value_m)/(2*dv);
        vol.setValue(v);
        return vega;
    }

    @Override
    public double rho() {
        // perturb rates and get rho and dividend rho
        final double r = rRate.value();
        final double dr = r*1.0e-4;
        rRate.setValue(r+dr);
        final double value_p = super.NPV();
        rRate.setValue(r-dr);
        final double value_m = super.NPV();
        // System.out.println(String.format("rho  = %f", (value_p - value_m)/(2*dr)));
        // final double rho = super.dividendRho();
        final double rho = (value_p - value_m)/(2*dr);
        rRate.setValue(r);
        return rho;
    }

    public /*@Volatility*/ double impliedVolatility(final double price) /* @ReadOnly */ {
        return impliedVolatility(price, stochProcess, 1.0e-4, 100, 1.0e-7, 4.0);
    }


    //
    // private methods
    //

    private YieldTermStructure flatRate(final Quote forward, final Calendar cal, final DayCounter dc) {
        return new FlatForward(0, cal, new Handle<Quote>(forward), dc);
    }


    private BlackVolTermStructure flatVol(final Quote vol, final Calendar cal, final DayCounter dc) {
        return new BlackConstantVol(0, cal, new Handle<Quote>(vol), dc);
    }
}
