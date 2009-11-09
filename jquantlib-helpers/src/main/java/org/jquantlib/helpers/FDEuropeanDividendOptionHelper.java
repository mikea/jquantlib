package org.jquantlib.helpers;

import java.util.List;

import org.jquantlib.exercise.EuropeanExercise;
import org.jquantlib.instruments.Option;
import org.jquantlib.pricingengines.vanilla.finitedifferences.FDDividendEuropeanEngine;
import org.jquantlib.time.Date;

/**
 * Helper class for European Dividend Options using the finite differences engine
 *
 * @see FDDividendOptionHelper
 *
 * @author Richard Gomes
 */
public class FDEuropeanDividendOptionHelper extends FDDividendOptionHelper<FDDividendEuropeanEngine> {

    /**
     * Constructor for European Dividend Options helper class using the finite differences engine
     *
     * @param type is the option call type (Call/Put)
     * @param underlying is the price of the underlying asset
     * @param strike is the strike price at expiration
     * @param r is the risk free rate
     * @param vol is the volatility
     * @param expiry is the expiration date
     * @param dividendDates is a list of dates when dividends are expected to be paid
     * @param dividends is a list of dividends amounts (as a pure value) expected to be paid
     */
    public FDEuropeanDividendOptionHelper(
            final Option.Type type,
            final /*@Real*/ double underlying,
            final /*@Real*/ double strike,
            final /*@Rate*/ double r,
            final /*@Volatility*/ double vol,
            final Date expiration,
            final List<Date> dividendDates,
            final List<Double> dividends) {

        super(FDDividendEuropeanEngine.class,
              type, underlying, strike, r, vol,
              new EuropeanExercise(expiration),
              dividendDates, dividends);
    }

    /**
     * Constructor for European Dividend Options helper class using the finite differences engine
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
     */
    public FDEuropeanDividendOptionHelper(
            final Option.Type type,
            final /*@Real*/ double underlying,
            final /*@Real*/ double strike,
            final /*@Rate*/ double r,
            final /*@Volatility*/ double vol,
            final Date expiration,
            final List<Date> dividendDates,
            final List<Double> dividends,
            final /*@Rate*/ double q) {

        super(FDDividendEuropeanEngine.class,
              type, underlying, strike, r, vol,
              new EuropeanExercise(expiration),
              dividendDates, dividends, q);
    }

}
