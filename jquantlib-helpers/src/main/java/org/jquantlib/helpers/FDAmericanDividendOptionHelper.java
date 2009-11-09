package org.jquantlib.helpers;

import java.util.List;

import org.jquantlib.Settings;
import org.jquantlib.exercise.AmericanExercise;
import org.jquantlib.instruments.Option;
import org.jquantlib.pricingengines.vanilla.finitedifferences.FDDividendAmericanEngine;
import org.jquantlib.time.Date;

/**
 * Helper class for American Dividend Options using the finite differences engine
 *
 * @see FDDividendOptionHelper
 *
 * @author Richard Gomes
 */
public class FDAmericanDividendOptionHelper extends FDDividendOptionHelper<FDDividendAmericanEngine> {

    /**
     * Constructor for American Dividend Options helper class using the finite differences engine
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
    public FDAmericanDividendOptionHelper(
            final Option.Type type,
            final /*@Real*/ double underlying,
            final /*@Real*/ double strike,
            final /*@Rate*/ double r,
            final /*@Volatility*/ double vol,
            final Date expiration,
            final List<Date> dividendDates,
            final List<Double> dividends) {

        super(FDDividendAmericanEngine.class,
              type, underlying, strike, r, vol,
              new AmericanExercise(new Settings().evaluationDate(), expiration),
              dividendDates, dividends);
    }

    /**
     * Constructor for American Dividend Options helper class using the finite differences engine
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
    public FDAmericanDividendOptionHelper(
            final Option.Type type,
            final /*@Real*/ double underlying,
            final /*@Real*/ double strike,
            final /*@Rate*/ double r,
            final /*@Volatility*/ double vol,
            final Date expiration,
            final List<Date> dividendDates,
            final List<Double> dividends,
            final /*@Rate*/ double q) {

        super(FDDividendAmericanEngine.class,
              type, underlying, strike, r, vol,
              new AmericanExercise(new Settings().evaluationDate(), expiration),
              dividendDates, dividends, q);
    }

}
