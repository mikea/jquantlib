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
import org.jquantlib.lang.reflect.TypeToken;
import org.jquantlib.math.Array;
import org.jquantlib.methods.lattices.BinomialTree;
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

/**
 * Pricing engine for vanilla options using binomial trees
 * <p>
 * This class was designed to be called with parametric types, typically 
 * using anonymous instantiation. Below you can see an example:
 * <pre>
 * // instantiate an anonymous class :: notice '{ braces }' below
 * PricingEngine engine = new BinomialVanillaEngine<Trigeorgis>(timeSteps) {} ;
 * </pre>
 *  
 * @category vanillaengines
 *  
 * @test the correctness of the returned values is tested by
 *       checking it against analytic results.
 *        
 * @todo Greeks are not overly accurate. They could be improved
 *       by building a tree so that it has three points at the
 *       current time. The value would be fetched from the middle
 *       one, while the two side points would be used for
 *       estimating partial derivatives.
 *
 * @author Srinivas Hasti
 * @author Richard Gomes
 */
public abstract class BinomialVanillaEngine<T extends BinomialTree> extends VanillaOptionEngine {

    //
    // private fields
    //
    
    private final Class<T> clazz;
    private final int timeSteps_;
	
	
	//
	// public constructors
	//
	
    public BinomialVanillaEngine(final int timeSteps) {

        // obtain a BinomialTree concrete implementation from a generic type (first generic parameter)
        this.clazz = (Class<T>) TypeToken.getClazz(this.getClass());
	    
        this.timeSteps_ = timeSteps;
        if (timeSteps_ <= 0) throw new IllegalArgumentException("timeSteps must be positive");
    }

	
    //
	// private methods
	//
	
	private Object getTreeInstance(final StochasticProcess1D bs, final /*@Date*/double maturity, final int timeSteps, final /*@Price*/double strike) {
        try {
            final Constructor c = clazz.getConstructor(StochasticProcess1D.class, double.class, int.class, double.class);
            return clazz.cast(c.newInstance(bs, maturity, timeSteps, strike));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

	
	//
	// implements PricingEngine
	//
	
	@Override
	public void calculate() /*@ReadOnly*/ {
	    
	    // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess) this.arguments.stochasticProcess;
        if (process==null) throw new NullPointerException("Black-Scholes process required");
        
	    DayCounter rfdc = process.riskFreeRate().getLink().dayCounter();
		DayCounter divdc = process.dividendYield().getLink().dayCounter();
		DayCounter voldc = process.blackVolatility().getLink().dayCounter();
		Calendar volcal = process.blackVolatility().getLink().calendar();

		double s0 = process.stateVariable().getLink().evaluate();
		if (s0 <= 0.0) throw new IllegalStateException("negative or null underlying given");
        double v = process.blackVolatility().getLink().blackVol(arguments.exercise.lastDate(), s0);
        Date maturityDate = arguments.exercise.lastDate();
        double r = process.riskFreeRate().getLink().zeroRate(maturityDate, rfdc, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
        double q = process.dividendYield().getLink().zeroRate(maturityDate, divdc, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
        Date referenceDate = process.riskFreeRate().getLink().referenceDate();

        // binomial trees with constant coefficient
        Handle<YieldTermStructure> flatRiskFree = new Handle<YieldTermStructure>(new FlatForward(referenceDate, r, rfdc));
        Handle<YieldTermStructure> flatDividends = new Handle<YieldTermStructure>(new FlatForward(referenceDate, q, divdc));
        Handle<BlackVolTermStructure> flatVol = new Handle<BlackVolTermStructure>(new BlackConstantVol(referenceDate, volcal, v, voldc));
        PlainVanillaPayoff payoff = (PlainVanillaPayoff) arguments.payoff;
        if (payoff == null) throw new NullPointerException("non-plain payoff given");

        double maturity = rfdc.yearFraction(referenceDate, maturityDate);

        StochasticProcess1D bs = new GeneralizedBlackScholesProcess(process.stateVariable(), flatDividends, flatRiskFree, flatVol);

        TimeGrid grid = new TimeGrid(maturity, timeSteps_);

        Tree tree = (Tree)getTreeInstance(bs, maturity, timeSteps_, payoff.strike());

        BlackScholesLattice<Tree> lattice = new BlackScholesLattice<Tree>(tree, r, maturity, timeSteps_);

        DiscretizedVanillaOption option = new DiscretizedVanillaOption(arguments, process, grid);

        option.initialize(lattice, maturity);

        // Partial derivatives calculated from various points in the binomial tree (Odegaard)

        // Rollback to third-last step, and get underlying price (s2) & option values (p2) at this point
        option.rollback(grid.at(2));
        // TODO: code review :: verifuy use of clone()
        final Array va2 = option.values().clone();
        if (va2.length != 3) throw new IllegalStateException("Expect 3 nodes in grid at second step");
        double p2h = va2.get(2); // high-price
        double s2 = lattice.underlying(2, 2); // high price

        // Rollback to second-last step, and get option value (p1) at this point
        option.rollback(grid.at(1));
        // TODO: code review :: verifuy use of clone()
        Array va = option.values().clone();
        if (va.length != 2) throw new IllegalStateException("Expect 2 nodes in grid at first step");
        double p1 = va.get(1);

        // Finally, rollback to t=0
        option.rollback(0.0);
        double p0 = option.presentValue();
        double s1 = lattice.underlying(1, 1);

        // Calculate partial derivatives
        double delta0 = (p1 - p0) / (s1 - s0); // dp/ds
        double delta1 = (p2h - p1) / (s2 - s1); // dp/ds

        // Store results
        results.value = p0;
        results.delta = delta0;
        results.gamma = 2.0 * (delta1 - delta0) / (s2 - s0); // d(delta)/ds
        results.theta = Greeks.blackScholesTheta(process, results.value, results.delta, results.gamma);
    }

}
