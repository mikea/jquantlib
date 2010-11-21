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
import org.jquantlib.lang.reflect.TypeTokenTree;
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
 */
//TODO: work in progress
public class BinomialConvertibleEngine<T extends BinomialTree> extends ConvertibleBondOption.EngineImpl {

    //
    // private fields
    //

    private final int timeSteps_;
    private final GeneralizedBlackScholesProcess process_;
    private final ConvertibleBondOption.ArgumentsImpl a;
    private final ConvertibleBondOption.ResultsImpl   r;

    private final Class<T> clazz;

    //
    // public constructors
    //

    public BinomialConvertibleEngine(final GeneralizedBlackScholesProcess process, final int timeSteps) {
        this.clazz = (Class<T>)new TypeTokenTree(this.getClass()).getElement(0);
        QL.require(timeSteps>0, "timeSteps must be positive"); // TODO: message
        this.timeSteps_ = timeSteps;
        this.a = arguments;
        this.r = results;
        this.process_ = process;
        this.process_.addObserver(this);
    }


    //
    // implements PricingEngine
    //

    @Override
    public void calculate() /* @ReadOnly */ {

        final DayCounter rfdc  = process_.riskFreeRate().currentLink().dayCounter();
        final DayCounter divdc = process_.dividendYield().currentLink().dayCounter();
        final DayCounter voldc = process_.blackVolatility().currentLink().dayCounter();
        final Calendar volcal = process_.blackVolatility().currentLink().calendar();

        /*@Real*/ double s0 = process_.x0();
        QL.require(s0 > 0.0, "negative or null underlying");
        final /*@Volatility*/ double v = process_.blackVolatility().currentLink().blackVol(a.exercise.lastDate(), s0);
        final Date maturityDate = a.exercise.lastDate();
        final /*@Rate*/ double  riskFreeRate = process_
        .riskFreeRate().currentLink()
        .zeroRate(maturityDate, rfdc, Compounding.Continuous, Frequency.NoFrequency)
        .rate();
        final /*@Rate*/ double  q = process_
        .dividendYield().currentLink()
        .zeroRate(maturityDate, divdc, Compounding.Continuous, Frequency.NoFrequency)
        .rate();
        final Date referenceDate = process_.riskFreeRate().currentLink().referenceDate();

        // subtract dividends
        int i;
        for (i=0; i<a.dividends.size(); i++) {
            if (a.dividends.get(i).date().gt(referenceDate)) {
                s0 -= a.dividends.get(i).amount() *
                process_.riskFreeRate().currentLink().discount(a.dividends.get(i).date());
            }
        }
        QL.require(s0 > 0.0, "negative value after subtracting dividends");

        // binomial trees with constant coefficient
        final Handle<Quote> underlying = new Handle<Quote>(new SimpleQuote(s0));
        final Handle<YieldTermStructure> flatRiskFree = new Handle<YieldTermStructure>(
                new FlatForward(referenceDate, riskFreeRate, rfdc));
        final Handle<YieldTermStructure> flatDividends = new Handle <YieldTermStructure>(
                new FlatForward(referenceDate, q, divdc));
        final Handle<BlackVolTermStructure> flatVol = new Handle<BlackVolTermStructure>(
                new BlackConstantVol(referenceDate, volcal, v, voldc));

        QL.require(a.payoff != null, "non-plain payoff given");
        final PlainVanillaPayoff payoff = (PlainVanillaPayoff) a.payoff;
        final /*@Time*/ double maturity = rfdc.yearFraction(a.settlementDate, maturityDate);
        final GeneralizedBlackScholesProcess bs = new GeneralizedBlackScholesProcess(underlying, flatDividends, flatRiskFree, flatVol);

        // final T tree = new T(bs, maturity, timeSteps_, payoff.strike());
        final T tree;
        try {
            final Constructor<T> c = clazz.getConstructor(StochasticProcess1D.class, double.class, int.class, double.class);
            tree = clazz.cast( c.newInstance(bs, maturity, timeSteps_, payoff.strike() ));
        } catch (final Exception e) {
            throw new LibraryException(e); // QA:[RG]::verified
        }

        final /*@Real*/ double creditSpread = a.creditSpread.currentLink().value();
        final Lattice lattice = new TsiveriotisFernandesLattice<T>(tree, riskFreeRate, maturity, timeSteps_, creditSpread, v, q);
        final DiscretizedConvertible convertible = new DiscretizedConvertible((ConvertibleBondOption.Arguments)a, bs, new TimeGrid(maturity, timeSteps_));

        convertible.initialize(lattice, maturity);
        convertible.rollback(0.0);
        r.value = convertible.presentValue();
    }

}
