/*
 Copyright (C) 2009 Richard Gomes

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
 Copyright (C) 2004, 2007 StatPro Italia srl

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

package org.jquantlib.instruments;

import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.cashflow.Dividend;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.arguments.DividendVanillaOptionArguments;
import org.jquantlib.pricingengines.vanilla.AnalyticDividendEuropeanEngine;
import org.jquantlib.pricingengines.vanilla.finitedifferences.FDDividendAmericanEngine;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.time.Date;

/**
 * Single-asset vanilla option (no barriers) with discrete dividends
 *
 * @category instruments
 * 
 * @author Richard Gomes
 */
public class DividendVanillaOption extends VanillaOption {

    private static final String WRONG_ARGUMENT_TYPE = "wrong argument type";

    private final List<? extends Dividend> cashFlow;

    public DividendVanillaOption(
            final StochasticProcess process,
            final Payoff payoff,
            final Exercise exercise,
            final List<Date> dividendDates,
            final List<Double> dividends,
            final PricingEngine engine) {
        super(process, payoff, exercise, engine);
        cashFlow = Dividend.DividendVector(dividendDates, dividends);
    }

    //
    // public methods
    //


    public /*@Volatility*/ double impliedVolatility(
            final double price,
            final GeneralizedBlackScholesProcess process) /* @ReadOnly */ {
        return impliedVolatility(price, process, 1.0e-4, 100, 1.0e-7, 4.0);
    }

    public /*@Volatility*/ double impliedVolatility(
            final double price,
            final GeneralizedBlackScholesProcess process,
            final double accuracy) /* @ReadOnly */ {
        return impliedVolatility(price, process, accuracy, 100, 1.0e-7, 4.0);
    }

    public /*@Volatility*/ double impliedVolatility(
            final double price,
            final GeneralizedBlackScholesProcess process,
            final double accuracy,
            final int maxEvaluations) /* @ReadOnly */ {
        return impliedVolatility(price, process, accuracy, maxEvaluations, 1.0e-7, 4.0);
    }

    public /*@Volatility*/ double impliedVolatility(
            final double price,
            final GeneralizedBlackScholesProcess process,
            final double accuracy,
            final int maxEvaluations,
            /*@Volatility*/ final double minVol) /* @ReadOnly */ {
        return impliedVolatility(price, process, accuracy, maxEvaluations, minVol, 4.0);
    }


    /**
     * see VanillaOption for notes on implied-volatility calculation.
     */
    public /*@Volatility*/ double impliedVolatility(
            final double targetValue,
            final GeneralizedBlackScholesProcess process,
            final double accuracy,
            final int maxEvaluations,
            /*@Volatility*/ final double minVol,
            /*@Volatility*/ final double maxVol) /* @ReadOnly */ {

        QL.require(!isExpired(), "option expired");

        final SimpleQuote volQuote = new SimpleQuote();

        final GeneralizedBlackScholesProcess newProcess = ImpliedVolatilityHelper.clone(process, volQuote);

        // engines are built-in for the time being
        final PricingEngine engine;
        switch (exercise.type()) {
            case European:
                engine = new AnalyticDividendEuropeanEngine(newProcess);
                break;
            case American:
                engine = new FDDividendAmericanEngine(newProcess);
                break;
            case Bermudan:
                throw new LibraryException("engine not available for Bermudan option with dividends"); // TODO: message
            default:
                throw new LibraryException("unknown exercise type"); // // TODO: message
        }

        return ImpliedVolatilityHelper.calculate(
                this,
                engine,
                volQuote,
                targetValue,
                accuracy,
                maxEvaluations,
                minVol, maxVol);
    }




    //
    // Overrides OneAssetStrikedOption
    //

    @Override
    public void setupArguments(final Arguments args) {
        QL.require(args instanceof DividendVanillaOptionArguments , WRONG_ARGUMENT_TYPE); // QA:[RG]::verified
        super.setupArguments(args);
        final DividendVanillaOptionArguments arguments = (DividendVanillaOptionArguments)args;
        arguments.cashFlow = cashFlow;
    }
}
