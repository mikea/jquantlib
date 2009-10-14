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
 Copyright (C) 2005 Joseph Wang
 Copyright (C) 2007 StatPro Italia srl

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

package org.jquantlib.pricingengines.vanilla.finitedifferences;

import org.jquantlib.cashflow.Dividend;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.time.Date;

/**
 * <p>Abstract base class for dividend engines. </p>
 * 
@Todo <p>The dividend class really needs to be made more sophisticated to distinguish between fixed dividends and fractional dividends </p>
 */
public abstract class FDDividendEngineBase extends FDMultiPeriodEngine {


    //
    // public methods
    //

    public FDDividendEngineBase(
            final GeneralizedBlackScholesProcess process) {
        this(process, 100, 100, false);
    }

    public FDDividendEngineBase(
            final GeneralizedBlackScholesProcess process,
            final int timeSteps) {
        this(process, timeSteps, 100, false);
    }

    public FDDividendEngineBase(
            final GeneralizedBlackScholesProcess process,
            final int timeSteps,
            final int gridPoints) {
        this(process, timeSteps, gridPoints, false);
    }

    public FDDividendEngineBase(
            final GeneralizedBlackScholesProcess process,
            final int timeSteps,
            final int gridPoints,
            final boolean timeDependent) {
        super(process, timeSteps, gridPoints, timeDependent);
    }


    //
    // protected methods
    //

    @Override
    protected void setupArguments(final Arguments  a) /* @ReadOnly */ {
        throw new UnsupportedOperationException();
    }

    protected double getDividendAmount(final int i) /* @ReadOnly */ {
        final Dividend dividend = (Dividend)(events.get(i));
        if (dividend!=null) {
            return dividend.amount();
        } else {
            return 0.0;
        }
    }

    protected double getDiscountedDividend(final int i) /* @ReadOnly */ {
        final double dividend = getDividendAmount(i);
        final Date date = events.get(i).date();
        final double discount = process.riskFreeRate().currentLink().discount(date) / process.dividendYield().currentLink().discount(date);
        return dividend * discount;
    }


    //
    // protected abstract methods
    //

    @Override
    protected abstract void setGridLimits() /* @ReadOnly */ ;

    @Override
    protected abstract void executeIntermediateStep(int step) /* @ReadOnly */ ;

}
