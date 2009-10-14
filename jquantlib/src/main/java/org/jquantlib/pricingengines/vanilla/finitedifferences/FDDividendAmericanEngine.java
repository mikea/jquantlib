package org.jquantlib.pricingengines.vanilla.finitedifferences;

import org.jquantlib.processes.GeneralizedBlackScholesProcess;

/**
 * Finite-differences pricing engine for dividend American options
 *
 * @test the correctness of the returned greeks is tested by reproducing numerical derivatives.
 * @test the invariance of the results upon addition of null dividends is tested.
 * 
 * @bug results are not overly reliable.
 * @bug method impliedVolatility() utterly fails
 * 
 * @category vanillaengines
 * 
 * @author Richard Gomes
 */


// FIXME: CODE REVIEW


public class FDDividendAmericanEngine extends FDEngineAdapter<FDDividendEngine> {

    //
    // public constructors
    //

    public FDDividendAmericanEngine(
            final GeneralizedBlackScholesProcess process,
            final int timeSteps,
            final int gridPoints,
            final boolean timeDependent) {
        super(process, timeSteps, gridPoints, timeDependent);
    }

    public FDDividendAmericanEngine(
            final GeneralizedBlackScholesProcess process,
            final int timeSteps,
            final int gridPoints) {
        this(process, timeSteps, gridPoints, false);
    }

    public FDDividendAmericanEngine(final GeneralizedBlackScholesProcess process) {
        this(process, 100, 100, false);
    }


}
