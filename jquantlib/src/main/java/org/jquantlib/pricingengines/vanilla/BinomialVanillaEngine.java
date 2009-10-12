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

import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.lang.reflect.TypeToken;
import org.jquantlib.math.matrixutilities.Array;
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
import org.jquantlib.time.Date;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.TimeGrid;

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
        QL.require(timeSteps_ > 0 , "timeSteps must be positive"); // QA:[RG]::verified // TODO: message
    }


    //
    // private methods
    //

    private Object getTreeInstance(
            final StochasticProcess1D bs,
            final /*@Date*/ double maturity,
            final int timeSteps,
            final /*@Price*/ double strike) {
        try {
            final Constructor c = clazz.getConstructor(StochasticProcess1D.class, double.class, int.class, double.class);
            return clazz.cast(c.newInstance(bs, maturity, timeSteps, strike));
        } catch (final Exception e) {
            throw new LibraryException(e); // QA:[RG]::verified
        }
    }


    //
    // implements PricingEngine
    //

    @Override
    public void calculate() /*@ReadOnly*/ {
        final GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess) this.arguments.stochasticProcess;
        QL.require(process!=null , "Black-Scholes process required"); // QA:[RG]::verified // TODO: message

        final DayCounter rfdc = process.riskFreeRate().currentLink().dayCounter();
        final DayCounter divdc = process.dividendYield().currentLink().dayCounter();
        final DayCounter voldc = process.blackVolatility().currentLink().dayCounter();
        final Calendar volcal = process.blackVolatility().currentLink().calendar();

        final double s0 = process.stateVariable().currentLink().value();
        QL.require(s0 > 0.0 , "negative or null underlying given"); // QA:[RG]::verified // TODO: message
        final double v = process.blackVolatility().currentLink().blackVol(arguments.exercise.lastDate(), s0);
        final Date maturityDate = arguments.exercise.lastDate();
        final double r = process.riskFreeRate().currentLink().zeroRate(maturityDate, rfdc, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
        final double q = process.dividendYield().currentLink().zeroRate(maturityDate, divdc, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
        final Date referenceDate = process.riskFreeRate().currentLink().referenceDate();

        // binomial trees with constant coefficient
        final Handle<YieldTermStructure> flatRiskFree = new Handle<YieldTermStructure>(new FlatForward(referenceDate, r, rfdc));
        final Handle<YieldTermStructure> flatDividends = new Handle<YieldTermStructure>(new FlatForward(referenceDate, q, divdc));
        final Handle<BlackVolTermStructure> flatVol = new Handle<BlackVolTermStructure>(new BlackConstantVol(referenceDate, volcal, v, voldc));
        final PlainVanillaPayoff payoff = (PlainVanillaPayoff) arguments.payoff;
        QL.require(payoff!=null , "non-plain payoff given"); // QA:[RG]::verified // TODO: message

        final double maturity = rfdc.yearFraction(referenceDate, maturityDate);
        final StochasticProcess1D bs = new GeneralizedBlackScholesProcess(process.stateVariable(), flatDividends, flatRiskFree, flatVol);
        final TimeGrid grid = new TimeGrid(maturity, timeSteps_);
        final Tree tree = (Tree)getTreeInstance(bs, maturity, timeSteps_, payoff.strike());
        final BlackScholesLattice<Tree> lattice = new BlackScholesLattice<Tree>(tree, r, maturity, timeSteps_);
        final DiscretizedVanillaOption option = new DiscretizedVanillaOption(arguments, process, grid);

        option.initialize(lattice, maturity);

        // Partial derivatives calculated from various points in the binomial tree (Odegaard)

        // Rollback to third-last step, and get underlying price (s2) & option values (p2) at this point
        option.rollback(grid.at(2));
        // TODO: code review :: verifuy use of clone()
        final Array va2 = option.values().clone();
        QL.require(va2.size() == 3 , "expect 3 nodes in grid at second step"); // QA:[RG]::verified // TODO: message
        final double p2h = va2.get(2); // high-price
        final double s2 = lattice.underlying(2, 2); // high price

        // Rollback to second-last step, and get option value (p1) at this point
        option.rollback(grid.at(1));
        // TODO: code review :: verifuy use of clone()
        final Array va = option.values().clone();
        QL.require(va.size() == 2 , "expect 2 nodes in grid at first step"); // QA:[RG]::verified // TODO: message
        final double p1 = va.get(1);

        // Finally, rollback to t=0
        option.rollback(0.0);
        final double p0 = option.presentValue();
        final double s1 = lattice.underlying(1, 1);

        // Calculate partial derivatives
        final double delta0 = (p1 - p0) / (s1 - s0); // dp/ds
        final double delta1 = (p2h - p1) / (s2 - s1); // dp/ds

        // Store results
        results.value = p0;
        results.delta = delta0;
        results.gamma = 2.0 * (delta1 - delta0) / (s2 - s0); // d(delta)/ds
        results.theta = Greeks.blackScholesTheta(process, results.value, results.delta, results.gamma);
    }

}
