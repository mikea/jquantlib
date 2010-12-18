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
package org.jquantlib.pricingengines;

import java.lang.reflect.Constructor;

import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.bonds.ConvertibleBondOption;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.methods.lattices.BinomialTree;
import org.jquantlib.methods.lattices.Lattice;
import org.jquantlib.methods.lattices.TsiveriotisFernandesLattice;
import org.jquantlib.pricingengines.hybrid.DiscretizedConvertible;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
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
 * Binomial Tsiveriotis-Fernandes engine for convertible bonds
 *
 * @category hybridengines
 *
 * @author Richard Gomes
 * @author Zahid HussainS
 * @param <E>
 */
//TODO: work in progress
// Temp hack to pass class of T

public class BinomialConvertibleEngine<T extends BinomialTree> extends ConvertibleBondOption.EngineImpl {
    private final GeneralizedBlackScholesProcess process_;
    private final int timeSteps_;
    Class<T> clazz;
    public BinomialConvertibleEngine(Class<T> tcls, 
    								 final GeneralizedBlackScholesProcess process, 
    								 int timeSteps) {
        //this.clazz = (Class<T>)new TypeTokenTree(this.getClass()).getElement(0);//failing here
    	this.clazz = tcls;
    	this.process_ = process;
        this.timeSteps_ = timeSteps;
        QL.require(timeSteps>0, "timeSteps must be positive, " + timeSteps + " not allowed");
        this.process_.addObserver(this);
    }

    public void calculate() {

        DayCounter rfdc  = process_.riskFreeRate().currentLink().dayCounter();
        DayCounter divdc = process_.dividendYield().currentLink().dayCounter();
        DayCounter voldc = process_.blackVolatility().currentLink().dayCounter();
        Calendar volcal = process_.blackVolatility().currentLink().calendar();

        Double s0 = process_.x0();
        QL.require(s0 > 0.0, "negative or null underlying");
        double /*Volatility*/ v = process_.blackVolatility().currentLink().blackVol(
                                         arguments_.exercise.lastDate(), s0);
        Date maturityDate = arguments_.exercise.lastDate();
        double /*Rate*/ riskFreeRate = process_.riskFreeRate().currentLink().zeroRate(
                                 maturityDate, rfdc, Compounding.Continuous, Frequency.NoFrequency).rate();
        double q = process_.dividendYield().currentLink().zeroRate(
                                maturityDate, divdc, Compounding.Continuous, Frequency.NoFrequency).rate();
        Date referenceDate = process_.riskFreeRate().currentLink().referenceDate();

        // subtract dividends
        int i;
        for (i=0; i<arguments_.dividends.size(); i++) {
            if (arguments_.dividends.get(i).date().gt(referenceDate))
                s0 -= arguments_.dividends.get(i).amount() *
                      process_.riskFreeRate().currentLink().discount(
                                             arguments_.dividends.get(i).date());
        }
        QL.require(s0 > 0.0,
                   "negative value after subtracting dividends");

        // binomial trees with constant coefficient
        Handle<Quote> underlying = new Handle<Quote>(new SimpleQuote(s0));
        Handle<YieldTermStructure> flatRiskFree = 
        			new Handle<YieldTermStructure>(new FlatForward(referenceDate, riskFreeRate, rfdc));
        Handle<YieldTermStructure> flatDividends =
        			new Handle<YieldTermStructure>(new FlatForward(referenceDate, q, divdc));
        Handle<BlackVolTermStructure> flatVol = 
        			new Handle<BlackVolTermStructure>(new BlackConstantVol(referenceDate, volcal, v, voldc));

        PlainVanillaPayoff payoff = (PlainVanillaPayoff)(arguments_.payoff);
        QL.require(payoff != null, "non-plain payoff given");

        double maturity = rfdc.yearFraction(arguments_.settlementDate, maturityDate);

        GeneralizedBlackScholesProcess bs = new GeneralizedBlackScholesProcess(underlying, flatDividends,
                                                    flatRiskFree, flatVol);
        // final T tree = new T(bs, maturity, timeSteps_, payoff.strike());
        final T tree;
        try {
            final Constructor<T> c = clazz.getConstructor(StochasticProcess1D.class, double.class, int.class, double.class);
            tree = clazz.cast( c.newInstance(bs, maturity, timeSteps_, payoff.strike() ));
        } catch (final Exception e) {
            throw new LibraryException(e); // QA:[RG]::verified
        }

        double creditSpread = arguments_.creditSpread.currentLink().value();

        Lattice lattice = new TsiveriotisFernandesLattice<T>(tree,riskFreeRate,maturity,
                                                 timeSteps_,creditSpread,v,q);

        DiscretizedConvertible convertible = 
        				new DiscretizedConvertible((ConvertibleBondOption.ArgumentsImpl )arguments_, bs,
                                           			new TimeGrid(maturity, timeSteps_));

        convertible.initialize(lattice, maturity);
        convertible.rollback(0.0);
        results_.value = convertible.presentValue();
    }

}
