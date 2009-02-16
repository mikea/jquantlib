/*
 Copyright (C) 2007 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */
package org.jquantlib.pricingengines.vanilla;

import java.lang.reflect.Constructor;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.math.Array;
import org.jquantlib.methods.lattices.BlackScholesLattice;
import org.jquantlib.methods.lattices.Tree;
import org.jquantlib.pricingengines.Greeks;
import org.jquantlib.pricingengines.VanillaOptionEngine;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.TimeGrid;
import org.jquantlib.util.Date;
import org.jquantlib.util.reflect.TypeToken;

/**
 * @author Srinivas Hasti
 * 
 */
//TODO: Finish
public class BinomialVanillaEngine<T extends Tree> extends VanillaOptionEngine {

	private GeneralizedBlackScholesProcess process_;
	private int timeSteps_;

	BinomialVanillaEngine(GeneralizedBlackScholesProcess process, int timeSteps) {
		this.process_ = process;
		this.timeSteps_ = timeSteps;
		if (timeSteps_ <= 0)
			throw new IllegalArgumentException("timeSteps must be positive, "
					+ timeSteps + " not allowed");
		//registerWith(process);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.pricingengines.PricingEngine#calculate()
	 */
	@Override
	//TODO: Finish
	public void calculate() {
		DayCounter rfdc  = process_.riskFreeRate().getLink().dayCounter();
        DayCounter divdc = process_.dividendYield().getLink().dayCounter();
        DayCounter voldc = process_.blackVolatility().getLink().dayCounter();
        Calendar volcal = process_.blackVolatility().getLink().calendar();

        double s0 = process_.stateVariable().getLink().evaluate();
        if(s0 <= 0.0) throw new IllegalStateException("negative or null underlying given");
        double v = process_.blackVolatility().getLink().blackVol(
            arguments.exercise.lastDate(), s0);
        Date maturityDate = arguments.exercise.lastDate();
        double r = process_.riskFreeRate().getLink().zeroRate(maturityDate,
            rfdc, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
        double q = process_.dividendYield().getLink().zeroRate(maturityDate,
            divdc,  Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
        Date referenceDate = process_.riskFreeRate().getLink().referenceDate();

        // binomial trees with constant coefficient
        Handle<YieldTermStructure> flatRiskFree = new Handle<YieldTermStructure>(new FlatForward(referenceDate, r, rfdc));
        Handle<YieldTermStructure> flatDividends = new Handle<YieldTermStructure>(
                new FlatForward(referenceDate, q, divdc));               
        Handle<BlackVolTermStructure> flatVol = new Handle<BlackVolTermStructure>(
                new BlackConstantVol(referenceDate, volcal, v, voldc));           
        PlainVanillaPayoff payoff = (PlainVanillaPayoff) arguments.payoff;
        //QL_REQUIRE(payoff, "non-plain payoff given");

        double maturity = rfdc.yearFraction(referenceDate, maturityDate);

        StochasticProcess1D bs = 
                         new GeneralizedBlackScholesProcess(
                                      process_.stateVariable(),
                                      flatDividends, flatRiskFree, flatVol);

        TimeGrid grid = new TimeGrid(maturity, timeSteps_);

        T tree = getTreeInstance(bs,maturity,timeSteps_,payoff.getStrike());
        
        BlackScholesLattice<T>  lattice = 
            new BlackScholesLattice<T>(tree, r, maturity, timeSteps_);

        DiscretizedVanillaOption option = new DiscretizedVanillaOption(arguments, process_, grid);

        option.initialize(lattice, maturity);

        // Partial derivatives calculated from various points in the
        // binomial tree (Odegaard)

        // Rollback to third-last step, and get underlying price (s2) &
        // option values (p2) at this point
        option.rollback(grid.at(2));
        Array va2 = new Array(option.values().getData());
        if(va2.size() != 3) throw new IllegalStateException("Expect 3 nodes in grid at second step");
        double p2h = va2.at(2); // high-price
        double s2 = lattice.underlying(2, 2); // high price

        // Rollback to second-last step, and get option value (p1) at
        // this point
        option.rollback(grid.at(1));
        Array va = new Array(option.values().getData());
        if(va.size() != 2) throw new IllegalStateException("Expect 2 nodes in grid at first step");
        double p1 = va.at(1);

        // Finally, rollback to t=0
        option.rollback(0.0);
        double p0 = option.presentValue();
        double s1 = lattice.underlying(1, 1);

        // Calculate partial derivatives
        double delta0 = (p1-p0)/(s1-s0);   // dp/ds
        double delta1 = (p2h-p1)/(s2-s1);  // dp/ds

        // Store results
        results.value = p0;
        results.delta = delta0;
        results.gamma = 2.0*(delta1-delta0)/(s2-s0);    //d(delta)/ds
        results.theta = Greeks.blackScholesTheta(process_,
                                           results.value,
                                           results.delta,
                                           results.gamma);
                                           
	}

	private T getTreeInstance(StochasticProcess1D bs, double maturity,
			int timeSteps, double strike) {
			try {
				final Class<T> rsgClass = (Class<T>) TypeToken.getClazz(this
						.getClass());
				final Constructor<T> c = rsgClass.getConstructor(
						StochasticProcess1D.class, double.class, int.class, double.class);
				return c.newInstance(bs, maturity, timeSteps,
						strike);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	}

}
