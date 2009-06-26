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

import org.jquantlib.cashflow.Dividend;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.arguments.DividendVanillaOptionArguments;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.util.Date;

public class DividendVanillaOption extends VanillaOption {

    public DividendVanillaOption(StochasticProcess process, 
                                Payoff payoff, Exercise exercise,
                                List<Date> dividendDates,
                                List<Double> dividends,
                                PricingEngine engine) {
        super(process, payoff, exercise, engine);
        cashFlow_ = Dividend.DividendVector(dividendDates, dividends);
    }

    @Override
    public void setupArguments(Arguments args) {
        super.setupArguments(args);
        if (!(args instanceof DividendVanillaOptionArguments)){
            throw new ArithmeticException("wrong engine type");
        }
        DividendVanillaOptionArguments arguments = (DividendVanillaOptionArguments)args;
 
        arguments.cashFlow = cashFlow_;
    }
    private List<? extends Dividend> cashFlow_;
}
