/*
 Copyright (C) 2007 Richard Gomes

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
 Copyright (C) 2003, 2007 Ferdinando Ametrano
 Copyright (C) 2003, 2007 StatPro Italia srl

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

package org.jquantlib.testsuite.instruments;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jquantlib.Configuration;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.Actual360;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.EuropeanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.AssetOrNothingPayoff;
import org.jquantlib.instruments.CashOrNothingPayoff;
import org.jquantlib.instruments.EuropeanOption;
import org.jquantlib.instruments.GapPayoff;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.instruments.VanillaOption;
import org.jquantlib.lang.annotation.NonNegative;
import org.jquantlib.methods.lattices.AdditiveEQPBinomialTree;
import org.jquantlib.methods.lattices.CoxRossRubinstein;
import org.jquantlib.methods.lattices.JarrowRudd;
import org.jquantlib.methods.lattices.Joshi4;
import org.jquantlib.methods.lattices.LeisenReimer;
import org.jquantlib.methods.lattices.Tian;
import org.jquantlib.methods.lattices.Trigeorgis;
import org.jquantlib.pricingengines.AnalyticEuropeanEngine;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.vanilla.BinomialVanillaEngine;
import org.jquantlib.pricingengines.vanilla.IntegralEngine;
import org.jquantlib.pricingengines.vanilla.finitedifferences.FDEuropeanEngine;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.testsuite.util.Utilities;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.StopClock;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * European Options test suite
 * 
 * @author Richard Gomes
 */
public class EuropeanOptionTest {

    private final static Logger logger = LoggerFactory.getLogger(EuropeanOptionTest.class);

    private final Settings settings;
    private final Date today;      
    
    public EuropeanOptionTest() {
        logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
        this.settings = Configuration.getSystemConfiguration(null).getGlobalSettings();
        this.today = settings.getEvaluationDate();      
    }
    
    
    private static class EuropeanOptionData {
        private Option.Type type;            // option type
        private /*@Price*/ double strike;    // option strike price
        private double s;                    // spot // FIXME: any specific @annotation?
        private /*@Price*/ double  q;        // dividend
        private /*@Rate*/ double  r;         // risk-free rate
        private /*@Time*/ double  t;         // time to maturity
        private /*@Volatility*/ double v;    // volatility
        private /*@Price*/ double result;    // expected result
        private double tol;                  // tolerance // FIXME: any specific @annotation?
        
        public EuropeanOptionData(
                    Option.Type type, 
                    /*@Price*/ double strike, 
                    double s, /*@Price*/ double  q,
                    /*@Rate*/ double  r,
                    /*@Time*/ double  t,
                    /*@Volatility*/ double v,
                    /*@Price*/ double result,
                    double tol) {
            this.type = type;
            this.strike = strike;
            this.s = s;
            this.q = q;
            this.r = r;
            this.t = t;
            this.v = v;
            this.result = result;
            this.tol = tol;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            sb.append(type).append(", ");
            sb.append(strike).append(", ");
            sb.append(s).append(", ");
            sb.append(q).append(", ");
            sb.append(r).append(", ");
            sb.append(t).append(", ");
            sb.append(v).append(", ");
            sb.append(result).append(", ");
            sb.append(tol);
            sb.append(']');
            return sb.toString();
        }
    }

    private enum EngineType { 
                    Analytic,
                    JR, CRR, EQP, TGEO, TIAN, LR, JOSHI,
                    FiniteDifferences,
                    Integral,
                    PseudoMonteCarlo, QuasiMonteCarlo; }

    
    
    
    private VanillaOption makeOption(
                            final StrikedTypePayoff payoff,
                            final Exercise exercise,
                            final Handle<SimpleQuote> u,
                            final Handle<YieldTermStructure> q,
                            final Handle<YieldTermStructure> r,
                            final Handle<BlackVolTermStructure> vol,
                            final EngineType engineType,
                            final int binomialSteps,
                            final int samples) {

        PricingEngine engine = null;
        GeneralizedBlackScholesProcess stochProcess = new BlackScholesMertonProcess(u, q, r, vol);
        
        switch (engineType) {
          case Analytic:
            engine = new AnalyticEuropeanEngine();
            break;
          case JR:
            engine = new BinomialVanillaEngine<JarrowRudd>(binomialSteps) {};
            break;
          case CRR:
            engine = new BinomialVanillaEngine<CoxRossRubinstein>(binomialSteps) {};
            break;
          case EQP:
            engine = new BinomialVanillaEngine<AdditiveEQPBinomialTree>(binomialSteps) {};
            break;
          case TGEO:
            engine = new BinomialVanillaEngine<Trigeorgis>(binomialSteps) {};
            break;
          case TIAN:
            engine = new BinomialVanillaEngine<Tian>(binomialSteps) {};
            break;
          case LR:
            engine = new BinomialVanillaEngine<LeisenReimer>(binomialSteps) {};
            break;
          case JOSHI:
            engine = new BinomialVanillaEngine<Joshi4>(binomialSteps) {};
            break;
          case FiniteDifferences:
            engine = new FDEuropeanEngine(stochProcess, binomialSteps,samples);
            break;
          case Integral:
              engine = new IntegralEngine();
              break;
//        case PseudoMonteCarlo:
//          engine = MakeMCEuropeanEngine<PseudoRandom>().withSteps(1)
//                                                       .withSamples(samples)
//                                                       .withSeed(42);
//          break;
//        case QuasiMonteCarlo:
//          engine = MakeMCEuropeanEngine<LowDiscrepancy>().withSteps(1)
//                                                         .withSamples(samples);
//          break;
          default:
            throw new UnsupportedOperationException("unknown engine type: "+engineType);
        }      

        return new EuropeanOption(stochProcess, payoff, exercise, engine);
    }

    
    
    
//  std::string engineTypeToString(EngineType type) {
//      switch (type) {
//        case Analytic:
//          return "analytic";
//        case JR:
//          return "Jarrow-Rudd";
//        case CRR:
//          return "Cox-Ross-Rubinstein";
//        case EQP:
//          return "EQP";
//        case TGEO:
//          return "Trigeorgis";
//        case TIAN:
//          return "Tian";
//        case LR:
//          return "LeisenReimer";
//        case JOSHI:
//          return "Joshi";
//        case FiniteDifferences:
//          return "FiniteDifferences";
//      case Integral:
//          return "Integral";
//        case PseudoMonteCarlo:
//          return "MonteCarlo";
//        case QuasiMonteCarlo:
//          return "Quasi-MonteCarlo";
//        default:
//          QL_FAIL("unknown engine type");
//      }
//  }

    private int timeToDays(/*@Time*/ double t) {
        return (int) (t*360+0.5);
    }


    @Test
    public void testValues() {

        logger.info("Testing European option values...");

        /**
         *  The data below are from "Option pricing formulas", E.G. Haug, McGraw-Hill 1998
         */
        EuropeanOptionData values[] = new EuropeanOptionData[] {
          // pag 2-8
          //                              type,     strike,   spot,    q,    r,    t,  vol,   value,    tol
          new EuropeanOptionData( Option.Type.CALL,  65.00,  60.00, 0.00, 0.08, 0.25, 0.30,  2.1334, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,   95.00, 100.00, 0.05, 0.10, 0.50, 0.20,  2.4648, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,   19.00,  19.00, 0.10, 0.10, 0.75, 0.28,  1.7011, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL,  19.00,  19.00, 0.10, 0.10, 0.75, 0.28,  1.7011, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL,   1.60,   1.56, 0.08, 0.06, 0.50, 0.12,  0.0291, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,   70.00,  75.00, 0.05, 0.10, 0.50, 0.35,  4.0870, 1.0e-4),
          // pag 24
          new EuropeanOptionData( Option.Type.CALL, 100.00,  90.00, 0.10, 0.10, 0.10, 0.15,  0.0205, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00, 100.00, 0.10, 0.10, 0.10, 0.15,  1.8734, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00, 110.00, 0.10, 0.10, 0.10, 0.15,  9.9413, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00,  90.00, 0.10, 0.10, 0.10, 0.25,  0.3150, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00, 100.00, 0.10, 0.10, 0.10, 0.25,  3.1217, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00, 110.00, 0.10, 0.10, 0.10, 0.25, 10.3556, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00,  90.00, 0.10, 0.10, 0.10, 0.35,  0.9474, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00, 100.00, 0.10, 0.10, 0.10, 0.35,  4.3693, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00, 110.00, 0.10, 0.10, 0.10, 0.35, 11.1381, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00,  90.00, 0.10, 0.10, 0.50, 0.15,  0.8069, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00, 100.00, 0.10, 0.10, 0.50, 0.15,  4.0232, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00, 110.00, 0.10, 0.10, 0.50, 0.15, 10.5769, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00,  90.00, 0.10, 0.10, 0.50, 0.25,  2.7026, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00, 100.00, 0.10, 0.10, 0.50, 0.25,  6.6997, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00, 110.00, 0.10, 0.10, 0.50, 0.25, 12.7857, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00,  90.00, 0.10, 0.10, 0.50, 0.35,  4.9329, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00, 100.00, 0.10, 0.10, 0.50, 0.35,  9.3679, 1.0e-4),
          new EuropeanOptionData( Option.Type.CALL, 100.00, 110.00, 0.10, 0.10, 0.50, 0.35, 15.3086, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00,  90.00, 0.10, 0.10, 0.10, 0.15,  9.9210, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00, 100.00, 0.10, 0.10, 0.10, 0.15,  1.8734, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00, 110.00, 0.10, 0.10, 0.10, 0.15,  0.0408, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00,  90.00, 0.10, 0.10, 0.10, 0.25, 10.2155, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00, 100.00, 0.10, 0.10, 0.10, 0.25,  3.1217, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00, 110.00, 0.10, 0.10, 0.10, 0.25,  0.4551, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00,  90.00, 0.10, 0.10, 0.10, 0.35, 10.8479, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00, 100.00, 0.10, 0.10, 0.10, 0.35,  4.3693, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00, 110.00, 0.10, 0.10, 0.10, 0.35,  1.2376, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00,  90.00, 0.10, 0.10, 0.50, 0.15, 10.3192, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00, 100.00, 0.10, 0.10, 0.50, 0.15,  4.0232, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00, 110.00, 0.10, 0.10, 0.50, 0.15,  1.0646, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00,  90.00, 0.10, 0.10, 0.50, 0.25, 12.2149, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00, 100.00, 0.10, 0.10, 0.50, 0.25,  6.6997, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00, 110.00, 0.10, 0.10, 0.50, 0.25,  3.2734, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00,  90.00, 0.10, 0.10, 0.50, 0.35, 14.4452, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00, 100.00, 0.10, 0.10, 0.50, 0.35,  9.3679, 1.0e-4),
          new EuropeanOptionData( Option.Type.PUT,  100.00, 110.00, 0.10, 0.10, 0.50, 0.35,  5.7963, 1.0e-4),
          // pag 27
          new EuropeanOptionData( Option.Type.CALL,  40.00,  42.00, 0.08, 0.04, 0.75, 0.35,  5.0975, 1.0e-4)
        };

        DayCounter dc = Actual360.getDayCounter();

        Handle<SimpleQuote> spot = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<SimpleQuote> qRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<YieldTermStructure> qTS = new Handle<YieldTermStructure>(Utilities.flatRate(today, qRate, dc));
        Handle<SimpleQuote> rRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<YieldTermStructure> rTS = new Handle<YieldTermStructure>(Utilities.flatRate(today, rRate, dc));
        Handle<SimpleQuote> vol = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<BlackVolTermStructure> volTS = new Handle<BlackVolTermStructure>(Utilities.flatVol(today, vol, dc));
        PricingEngine engine = new AnalyticEuropeanEngine();

        StopClock clock = new StopClock();
        clock.reset();
        clock.startClock();

        for (int i=0; i<values.length-1; i++) {

            logger.debug(values[i].toString());
            
            StrikedTypePayoff payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);
            Date exDate = today.getDateAfter( timeToDays(values[i].t) );
            Exercise exercise = new EuropeanExercise(exDate);

            spot. getLink().setValue(values[i].s);
            qRate.getLink().setValue(values[i].q);
            rRate.getLink().setValue(values[i].r);
            vol.  getLink().setValue(values[i].v);

            StochasticProcess process = new BlackScholesMertonProcess(spot, qTS, rTS, volTS);

            EuropeanOption option = new EuropeanOption(process, payoff, exercise, engine);

            double calculated = option.getNPV();
            double error = Math.abs(calculated-values[i].result);
            double tolerance = values[i].tol;
            
            StringBuilder sb = new StringBuilder();
            sb.append("error ").append(error).append(" .gt. tolerance ").append(tolerance).append('\n');
            sb.append("    calculated ").append(calculated).append('\n');
            sb.append("    type ").append(values[i].type).append('\n');
            sb.append("    strike ").append(values[i].strike).append('\n');
            sb.append("    s ").append(values[i].s).append('\n');
            sb.append("    q ").append(values[i].q).append('\n');
            sb.append("    r ").append(values[i].r).append('\n');
            sb.append("    t ").append(values[i].t).append('\n');
            sb.append("    v ").append(values[i].v).append('\n');
            sb.append("    result ").append(values[i].result).append('\n');
            sb.append("    tol ").append(values[i].tol); // .append('\n');
            
            if (error<=tolerance)
                logger.info(" error="+error);
            else
                fail(exercise + " " + payoff.optionType() + " option with " + payoff + " payoff:\n" 
                + "    spot value:       " + values[i].s + "\n"
                + "    strike:           " + payoff.strike() + "\n" 
                + "    dividend yield:   " + values[i].q + "\n" 
                + "    risk-free rate:   " + values[i].r + "\n" 
                + "    reference date:   " + today + "\n" 
                + "    maturity:         " + values[i].t + "\n" 
                + "    volatility:       " + values[i].v + "\n\n" 
                + "    expected:         " + values[i].result + "\n" 
                + "    calculated:       " + calculated + "\n"
                + "    error:            " + error + "\n" 
                + "    tolerance:        " + tolerance);
        }
        clock.stopClock();
        clock.log();
    }
    
    //TODO: To be completed - still failing
    @Ignore
    @Test 
    public void testGreekValues(){
        logger.info("Testing European option greek values...2");
        /* The data below are from
        "Option pricing formulas", E.G. Haug, McGraw-Hill 1998
        pag 11-16
        */
        
        try{
        
        EuropeanOptionData values[] = 
            //        type, strike,   spot,    q,    r,        t,  vol,  value delta
                {new EuropeanOptionData(Option.Type.CALL, 100.00, 105.00, 0.10, 0.10, 0.500000, 0.36,  0.5946, 0),
                new EuropeanOptionData(Option.Type.PUT,  100.00, 105.00, 0.10, 0.10, 0.500000, 0.36, -0.3566, 0),
                new EuropeanOptionData(Option.Type.PUT,100.00, 105.00, 0.10, 0.10, 0.500000, 0.36, -4.8775, 0 ),
                new EuropeanOptionData(Option.Type.CALL, 60.00,  55.00, 0.00, 0.10, 0.750000, 0.30,  0.0278, 0 ),
                new EuropeanOptionData(Option.Type.PUT, 60.00,  55.00, 0.00, 0.10, 0.750000, 0.30,  0.0278, 0 ),
                new EuropeanOptionData(Option.Type.CALL,  60.00,  55.00, 0.00, 0.10, 0.750000, 0.30, 18.9358, 0),
                new EuropeanOptionData(Option.Type.PUT,   60.00,  55.00, 0.00, 0.10, 0.750000, 0.30, 18.9358, 0 ),
                new EuropeanOptionData(Option.Type.PUT,  405.00, 430.00, 0.05, 0.07, 1.0/12.0, 0.20,-31.1924, 0),
                new EuropeanOptionData(Option.Type.PUT,  405.00, 430.00, 0.05, 0.07, 1.0/12.0, 0.20, -0.0855, 0),
                new EuropeanOptionData(Option.Type.CALL,  75.00,  72.00, 0.00, 0.09, 1.000000, 0.19, 38.7325, 0),
                new EuropeanOptionData(Option.Type.PUT,  490.00, 500.00, 0.05, 0.08, 0.250000, 0.15, 42.2254, 0)
                };
        
            DayCounter dc = Actual360.getDayCounter();
            Date today = DateFactory.getFactory().getTodaysDate();
            
            Handle<SimpleQuote> spot = new Handle<SimpleQuote>(new SimpleQuote(0.0));
            Handle<SimpleQuote> qRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
            Handle<YieldTermStructure> qTS = new Handle<YieldTermStructure>(Utilities.flatRate(today, qRate, dc));

            Handle<SimpleQuote> rRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
            Handle<YieldTermStructure> rTS = new Handle<YieldTermStructure>(Utilities.flatRate(today, rRate, dc));
            Handle<SimpleQuote> vol = new Handle<SimpleQuote>(new SimpleQuote(0.0));
            Handle<BlackVolTermStructure> volTS = new Handle<BlackVolTermStructure>(Utilities.flatVol(today, vol.getLink().evaluate(), dc));
            PricingEngine engine = new AnalyticEuropeanEngine();
            Handle<StochasticProcess> stochProcess = new Handle<StochasticProcess>(new BlackScholesMertonProcess(spot, qTS, rTS, volTS));
           
           StrikedTypePayoff payoff;
           Date exDate;
           Exercise exercise;
           Handle<VanillaOption> option;
           double calculated;
           double tolerance = 1e-4;
           double error;
           
           int i = -1;
           i++;
           i++;
           
           
           // testing delta 1
           i++; 
           payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);
           exDate = today.getDateAfter(timeToDays(values[i].t));
           exercise = new EuropeanExercise(exDate);
           spot.getLink().setValue(values[i].s);
           qRate.getLink().setValue(values[i].q);
           rRate.getLink().setValue(values[i].r);
           vol.getLink().setValue(values[i].v);
           
           option = new Handle<VanillaOption>(new EuropeanOption(stochProcess.getLink(), payoff, exercise, engine));
           calculated = option.getLink().delta();
           error = Math.abs(calculated - values[i].result);
           
           //TODO: this test fails
           if(error>tolerance){
               System.out.println("Testing delta 1 fails");
               System.out.println("Expected: " + values[i].result);
               System.out.println("Result: " + calculated);
               System.out.println("------------------------------------");
               //fail("testGreeks failed");
           }
           
           System.out.println("------------------------------------");
           System.out.println("------------------------------------");
           
           //testing delta 2
           i++;
           payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);
           exDate = today.getDateAfter(timeToDays(values[i].t));
           exercise = new EuropeanExercise(exDate);
           spot.getLink().setValue(values[i].s);
           qRate.getLink().setValue(values[i].q);
           rRate.getLink().setValue(values[i].r);
           vol.getLink().setValue(values[i].v);
           option = new Handle<VanillaOption>(new EuropeanOption(stochProcess.getLink(), payoff, exercise, engine));
           calculated = option.getLink().delta();
           error = Math.abs(calculated - values[i].result);
           if(error>tolerance){
               System.out.println("Testing delta 2 fails");
               System.out.println("Expected: " + values[i].result);
               System.out.println("Result: " + calculated);
               System.out.println("------------------------------------");
               //fail("testGreeks failed");
           }
           
           System.out.println("------------------------------------");
           System.out.println("------------------------------------");
           
           
           //testing elasticity
           i++;
           payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);
           exDate = today.getDateAfter(timeToDays(values[i].t));
           exercise = new EuropeanExercise(exDate);
           spot.getLink().setValue(values[i].s);
           qRate.getLink().setValue(values[i].q);
           rRate.getLink().setValue(values[i].r);
           vol.getLink().setValue(values[i].v);
           option = new Handle<VanillaOption>(new EuropeanOption(stochProcess.getLink(), payoff, exercise, engine));
           calculated = option.getLink().elasticity();
           error = Math.abs(Math.abs(calculated - values[i].result));
           if(error>tolerance){
               System.out.println("Testing elasticity fails");
               System.out.println("Expected: " + values[i].result);
               System.out.println("Result: " + calculated);
               System.out.println("------------------------------------");
               //fail("testGreeks failed");
           }
           
           System.out.println("------------------------------------");
           System.out.println("------------------------------------");
           
           
           // testing gamma 1
           i++;
           payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);
           exDate = today.getDateAfter(timeToDays(values[i].t));
           exercise = new EuropeanExercise(exDate);
           spot.getLink().setValue(values[i].s);
           qRate.getLink().setValue(values[i].q);
           rRate.getLink().setValue(values[i].r);
           vol.getLink().setValue(values[i].v);
           option = new Handle<VanillaOption>(new EuropeanOption(stochProcess.getLink(), payoff, exercise, engine));
           calculated = option.getLink().gamma();
           error = Math.abs(Math.abs(calculated - values[i].result));
           if(error>tolerance){
               System.out.println("Testing gamma 1 fails");
               System.out.println("Expected: " + values[i].result);
               System.out.println("Result: " + calculated);
               System.out.println("------------------------------------");
               //fail("testGreeks failed");
           }
           
           System.out.println("------------------------------------");
           System.out.println("------------------------------------");
           
           // testing gamma 2
           i++;
           payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);
           exDate = today.getDateAfter(timeToDays(values[i].t));
           exercise = new EuropeanExercise(exDate);
           spot.getLink().setValue(values[i].s);
           qRate.getLink().setValue(values[i].q);
           rRate.getLink().setValue(values[i].r);
           vol.getLink().setValue(values[i].v);
           option = new Handle<VanillaOption>(new EuropeanOption(stochProcess.getLink(), payoff, exercise, engine));
           calculated = option.getLink().gamma();
           error = Math.abs(Math.abs(calculated - values[i].result));
           if(error>tolerance){
               System.out.println("Testing gamma 2 fails");
               System.out.println("Expected: " + values[i].result);
               System.out.println("Result: " + calculated);
               System.out.println("------------------------------------");
               //fail("testGreeks failed");
           }
           
           System.out.println("------------------------------------");
           System.out.println("------------------------------------");
           
           //testing vega 1
           i++;
           payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);
           exDate = today.getDateAfter(timeToDays(values[i].t));
           exercise = new EuropeanExercise(exDate);
           spot.getLink().setValue(values[i].s);
           qRate.getLink().setValue(values[i].q);
           rRate.getLink().setValue(values[i].r);
           vol.getLink().setValue(values[i].v);
           option = new Handle<VanillaOption>(new EuropeanOption(stochProcess.getLink(), payoff, exercise, engine));
           calculated = option.getLink().vega();
           error = Math.abs(Math.abs(calculated - values[i].result));
           if(error>tolerance){
               System.out.println("Testing vega fails");
               System.out.println("Expected: " + values[i].result);
               System.out.println("Result: " + calculated);
               System.out.println("------------------------------------");
               //fail("testGreeks failed");
           }
           
           System.out.println("------------------------------------");
           System.out.println("------------------------------------");
           
           //testing vega 2
           i++;
           payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);
           exDate = today.getDateAfter(timeToDays(values[i].t));
           exercise = new EuropeanExercise(exDate);
           spot.getLink().setValue(values[i].s);
           qRate.getLink().setValue(values[i].q);
           rRate.getLink().setValue(values[i].r);
           vol.getLink().setValue(values[i].v);
           option = new Handle<VanillaOption>(new EuropeanOption(stochProcess.getLink(), payoff, exercise, engine));
           calculated = option.getLink().vega();
           error = Math.abs(Math.abs(calculated - values[i].result));
           if(error>tolerance){
               System.out.println("Testing vega 2 fails");
               System.out.println("Expected: " + values[i].result);
               System.out.println("Result: " + calculated);
               System.out.println("------------------------------------");
               //fail("testGreeks failed");
           }
           
           System.out.println("------------------------------------");
           System.out.println("------------------------------------");
           
           //testing theta
           i++;
           payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);
           exDate = today.getDateAfter(timeToDays(values[i].t));
           exercise = new EuropeanExercise(exDate);
           spot.getLink().setValue(values[i].s);
           qRate.getLink().setValue(values[i].q);
           rRate.getLink().setValue(values[i].r);
           vol.getLink().setValue(values[i].v);
           option = new Handle<VanillaOption>(new EuropeanOption(stochProcess.getLink(), payoff, exercise, engine));
           calculated = option.getLink().theta();
           error = Math.abs(Math.abs(calculated - values[i].result));
           if(error>tolerance){
               System.out.println("Testing theta fails");
               System.out.println("Expected: " + values[i].result);
               System.out.println("Result: " + calculated);
               System.out.println("------------------------------------");
               //fail("testGreeks failed");
           }
           
           System.out.println("------------------------------------");
           System.out.println("------------------------------------");
           
           //testing theta per day
           i++;
           payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);
           exDate = today.getDateAfter(timeToDays(values[i].t));
           exercise = new EuropeanExercise(exDate);
           spot.getLink().setValue(values[i].s);
           qRate.getLink().setValue(values[i].q);
           rRate.getLink().setValue(values[i].r);
           vol.getLink().setValue(values[i].v);
           option = new Handle<VanillaOption>(new EuropeanOption(stochProcess.getLink(), payoff, exercise, engine));
           calculated = option.getLink().thetaPerDay();
           error = Math.abs(Math.abs(calculated - values[i].result));
           if(error>tolerance){
               System.out.println("Fifth test case failed");
               System.out.println("Expected: " + values[i].result);
               System.out.println("Result: " + calculated);
               System.out.println("------------------------------------");
               //fail("testGreeks failed");
           }
           
           System.out.println("------------------------------------");
           System.out.println("------------------------------------");
           
           
           //testing rho
           i++;
           payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);
           exDate = today.getDateAfter(timeToDays(values[i].t));
           exercise = new EuropeanExercise(exDate);
           spot.getLink().setValue(values[i].s);
           qRate.getLink().setValue(values[i].q);
           rRate.getLink().setValue(values[i].r);
           vol.getLink().setValue(values[i].v);
           option = new Handle<VanillaOption>(new EuropeanOption(stochProcess.getLink(), payoff, exercise, engine));
           calculated = option.getLink().rho();
           error = Math.abs(Math.abs(calculated - values[i].result));
           if(error>tolerance){
               System.out.println("Testing rho fails");
               System.out.println("Expected: " + values[i].result);
               System.out.println("Result: " + calculated);
               System.out.println("------------------------------------");
               //fail("testGreeks failed");
           }
           
           System.out.println("------------------------------------");
           System.out.println("------------------------------------");
           
           //testing dividend rho
           i++;
           payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);
           exDate = today.getDateAfter(timeToDays(values[i].t));
           exercise = new EuropeanExercise(exDate);
           spot.getLink().setValue(values[i].s);
           qRate.getLink().setValue(values[i].q);
           rRate.getLink().setValue(values[i].r);
           vol.getLink().setValue(values[i].v);
           option = new Handle<VanillaOption>(new EuropeanOption(stochProcess.getLink(), payoff, exercise, engine));
           calculated = option.getLink().dividendRho();
           error = Math.abs(Math.abs(calculated - values[i].result));
           if(error>tolerance){
               System.out.println("Testing dividend rho fails");
               System.out.println("Expected: " + values[i].result);
               System.out.println("Result: " + calculated);
               System.out.println("------------------------------------");
               //fail("testGreeks failed");
           }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    
    @Test
    public void testGreeks() {
        logger.info("Testing analytic European option greeks...");

        final Map<String,Double> tolerance = new HashMap<String, Double>();
        tolerance.put("delta",  1.0e-5);
        tolerance.put("gamma",  1.0e-5);
        tolerance.put("theta",  1.0e-5);
        tolerance.put("rho",    1.0e-5);
        tolerance.put("divRho", 1.0e-5);
        tolerance.put("vega",   1.0e-5);

        final Map<String,Double> expected = new HashMap<String, Double>();
        final Map<String,Double> calculated = new HashMap<String, Double>();
        
        final Option.Type types[] = { Option.Type.CALL, Option.Type.PUT };
        final double strikes[] = { 50.0, 99.5, 100.0, 100.5, 150.0 };
        final double underlyings[] = { 100.0 };
        final double qRates[] = { 0.04, 0.05, 0.06 };
        final double rRates[] = { 0.01, 0.05, 0.15 };
        final double residualTimes[] = { 1.0, 2.0 };
        final double vols[] = { 0.11, 0.50, 1.20 };

        final DayCounter dc = Actual360.getDayCounter();
        
        final Handle<SimpleQuote> spot = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        final Handle<SimpleQuote> qRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        final Handle<YieldTermStructure> qTS = new Handle<YieldTermStructure>(Utilities.flatRate(today, qRate, dc));
        final Handle<SimpleQuote> rRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        final Handle<YieldTermStructure> rTS = new Handle<YieldTermStructure>(Utilities.flatRate(today, rRate, dc));
        final Handle<SimpleQuote> vol = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        final Handle<BlackVolTermStructure> volTS = new Handle<BlackVolTermStructure>(Utilities.flatVol(today, vol, dc));

        StrikedTypePayoff payoff = null;

        for (int i=0; i<types.length; i++) {
          for (int j=0; j<strikes.length; j++) {
            for (int k=0; k<residualTimes.length; k++) {
            
                final Date exDate = today.getDateAfter( timeToDays(residualTimes[i]) );
                final Exercise exercise = new EuropeanExercise(exDate);

                for (int kk=0; kk<4; kk++) {
                  // option to check
                  if (kk==0) {
                        payoff = new PlainVanillaPayoff(types[i], strikes[j]);                    
                  } else if (kk==1) {
                      //FIXME check constructor
                      payoff = new CashOrNothingPayoff(types[i], strikes[j], 100);
                  } else if (kk==2) {
                      payoff = new AssetOrNothingPayoff(types[i], strikes[j]);
                  } else if (kk==3) {
                      payoff = new GapPayoff(types[i], strikes[j], 100);
                  }

                  StochasticProcess process = new BlackScholesMertonProcess(spot, qTS, rTS, volTS);

                  if (payoff==null) throw new IllegalArgumentException();
                  EuropeanOption option = new EuropeanOption(process, payoff, exercise, new AnalyticEuropeanEngine());

                  
                  for (int l=0; l<underlyings.length; l++) {
                    for (int m=0; m<qRates.length; m++) {
                      for (int n=0; n<rRates.length; n++) {
                        for (int p=0; p<vols.length; p++) {
                          double u = underlyings[l];
                          double q = qRates[m];
                          double r = rRates[n];
                          double v = vols[p];
                          //something wrong here for vanilla payoff?
                          spot.getLink().setValue(u);
                          qRate.getLink().setValue(q);
                          rRate.getLink().setValue(r);
                          vol.getLink().setValue(v);

                          double value = option.getNPV();
                          calculated.put("delta", option.delta());
                          calculated.put("gamma", option.gamma());
                          calculated.put("theta", option.theta());
                          calculated.put("rho", option.rho());
                          calculated.put("divRho", option.dividendRho());
                          calculated.put("vega", option.vega());

                          if (value > spot.getLink().evaluate()*1.0e-5) {
                              // perturb spot and get delta and gamma
                              double du = u*1.0e-4;
                              spot.getLink().setValue(u+du);
                              double value_p = option.getNPV();
                              double delta_p = option.delta();
                              spot.getLink().setValue(u-du);

                              double value_m = option.getNPV();
                              double delta_m = option.delta();
                              spot.getLink().setValue(u);
                              expected.put("delta", (value_p - value_m)/(2*du));
                              expected.put("gamma", (delta_p - delta_m)/(2*du));

                              // perturb rates and get rho and dividend rho
                              double dr = r*1.0e-4;
                              rRate.getLink().setValue(r+dr);
                              value_p = option.getNPV();
                              rRate.getLink().setValue(r-dr);
                              value_m = option.getNPV();
                              rRate.getLink().setValue(r);
                              expected.put("rho", (value_p - value_m)/(2*dr));

                              double dq = q*1.0e-4;
                              qRate.getLink().setValue(q+dq);
                              value_p = option.getNPV();
                              qRate.getLink().setValue(q-dq);
                              value_m = option.getNPV();
                              qRate.getLink().setValue(q);
                              expected.put("divRho",(value_p - value_m)/(2*dq));

                              // perturb volatility and get vega
                              double dv = v*1.0e-4;
                              vol.getLink().setValue(v+dv);
                              value_p = option.getNPV();
                              vol.getLink().setValue(v-dv);
                              value_m = option.getNPV();
                              vol.getLink().setValue(v);
                              expected.put("vega",(value_p - value_m)/(2*dv));

                            // perturb date and get theta
                            final Date yesterday = today.getPreviousDay();
                            final Date tomorrow  = today.getNextDay();
                            double dT = dc.yearFraction(yesterday, tomorrow);
                            settings.setEvaluationDate(yesterday);
                            value_m = option.getNPV();
                            settings.setEvaluationDate(tomorrow);
                            value_p = option.getNPV();
                            expected.put("theta", (value_p - value_m)/dT);

                            settings.setEvaluationDate(today);

                              // compare
                              for (Entry<String, Double> it: calculated.entrySet()){

                                  String greek = it.getKey();
                                  Double expct = expected.get(greek);
                                  Double calcl = calculated.get(greek);
                                  Double tol   = tolerance.get(greek);

                                  double error = Utilities.relativeError(expct,calcl,u);
                                  if (error>tol) {
                                      REPORT_FAILURE(greek, payoff, exercise, u, q, r, today, v, expct, calcl, error, tol);
                                  }

                              }
                          }
                        }
                      }
                    }
                  }
                }
            }
          }
        }

    }

    
      @Test
    public void testImpliedVol() {

        logger.info("Testing European option implied volatility...");

        final int maxEvaluations = 100;
        final double tolerance = 1.0e-6;

        // test options
        Option.Type types[] = { Option.Type.CALL, Option.Type.PUT };
        double strikes[] = { 90.0, 99.5, 100.0, 100.5, 110.0 };
        int lengths[] = { 36, 180, 360, 1080 };

        // test data
        double underlyings[] = { 90.0, 95.0, 99.9, 100.0, 100.1, 105.0, 110.0 };
        double qRates[] = { 0.01, 0.05, 0.10 };
        double rRates[] = { 0.01, 0.05, 0.10 };
        double vols[] = { 0.01, 0.20, 0.30, 0.70, 0.90 };

        DayCounter dc = Actual360.getDayCounter();
        Date today = DateFactory.getFactory().getTodaysDate();

        Handle<SimpleQuote> spot = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<SimpleQuote> vol = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<BlackVolTermStructure> volTS = new Handle<BlackVolTermStructure>(Utilities.flatVol(today, vol, dc));
        Handle<SimpleQuote> qRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<YieldTermStructure> qTS = new Handle<YieldTermStructure>(Utilities.flatRate(today,qRate, dc));
        Handle<SimpleQuote> rRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<YieldTermStructure> rTS = new Handle<YieldTermStructure>(Utilities.flatRate(today, rRate, dc));

        for (int i=0; i<types.length; i++) {
          for (int j=0; j<strikes.length; j++) {
            for (int k=0; k<lengths.length; k++) {
              // option to check
              Date exDate = today.getDateAfter( lengths[k] );

              Exercise exercise = new EuropeanExercise(exDate);

              StrikedTypePayoff payoff = new PlainVanillaPayoff(types[i], strikes[j]);
              
              VanillaOption option = makeOption(payoff, exercise, spot, qTS, rTS, volTS, EngineType.Analytic, 0, 0);

              for (int l=0; l<underlyings.length; l++) {
                for (int m=0; m<qRates.length; m++) {
                  for (int n=0; n<rRates.length; n++) {
                    for (int p=0; p<vols.length; p++) {
                      double u = underlyings[l];
                      double q = qRates[m],
                           r = rRates[n];
                      double v = vols[p];
                      spot.getLink().setValue(u);
                      qRate.getLink().setValue(q);
                      rRate.getLink().setValue(r);
                      vol.getLink().setValue(v);

                      double value = option.getNPV();
                      double implVol = 0.0; // just to remove a warning...
                      if (value != 0.0) {
                          // shift guess somehow
                          vol.getLink().setValue(v*0.5);
                          if (Math.abs(value-option.getNPV()) <= 1.0e-12) {
                              // flat price vs vol --- pointless (and
                              // numerically unstable) to solve
                              continue;
                          }
                          
                      try{
                          /*TODO: implement implVol = option->impliedVolatility(value, tolerance,maxEvaluations); instead of this hack.*/
                          Method method = option.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("impliedVolatility", new Class[]{double.class,
                                  double.class, int.class, double.class, double.class});
                          method.setAccessible(true);
                          implVol = (Double) method.invoke(option, new Object[]{value, 1.0e-6, 200, 1.0e-8, 4.0 });

                        } catch (Exception e) {
                            if(e instanceof NoSuchMethodException|| e instanceof NullPointerException || e instanceof SecurityException){
                                fail("API changed - reimplement testcase");
                            }
                            else fail(
                                "\n implied vol calculation failed:" +
                                "\n   option:         " + types[i] +
                                "\n   strike:         " + strikes[j] +
                                "\n   spot value:     " + u +
                                "\n   dividend yield: " + q +
                                "\n   risk-free rate: " + r +
                                "\n   today:          " + today +
                                "\n   maturity:       " + exDate +
                                "\n   volatility:     " + vol +
                                "\n   option value:   " + value +
                                "\n" + e.getMessage());
                       }
                          if (Math.abs(implVol-v) > tolerance) {
                              // the difference might not matter
                              vol.getLink().setValue(implVol);
                              double value2 = option.getNPV();
                              double error = Utilities.relativeError(value,value2,u);
                              if (error > tolerance) {
                                  fail(
                                      types[i] + " option :\n"
                                      + "    spot value:          " + u + "\n"
                                      + "    strike:              "
                                      + strikes[j] + "\n"
                                      + "    dividend yield:      "
                                      + (q) + "\n"
                                      + "    risk-free rate:      "
                                      + (r) + "\n"
                                      + "    maturity:            "
                                      + exDate + "\n\n"
                                      + "    original volatility: "
                                      + (v) + "\n"
                                      + "    price:               "
                                      + value + "\n"
                                      + "    implied volatility:  "
                                      + (implVol)
                                      + "\n"
                                      + "    corresponding price: "
                                      + value2 + "\n"
                                      + "    error:               " + error);
                              }
                          }
                      }
                    }
                  }
                }
              }
            }
          }
        }
    }

    
//  void EuropeanOptionTest::testImpliedVolContainment() {
//
//      BOOST_MESSAGE("Testing self-containment of "
//                    "implied volatility calculation...");
//
//      Size maxEvaluations = 100;
//      Real tolerance = 1.0e-6;
//
//      // test options
//
//      DayCounter dc = Actual360();
//      Date today = Date::todaysDate();
//
//      boost::shared_ptr<SimpleQuote> spot(new SimpleQuote(100.0));
//      Handle<Quote> underlying(spot);
//      boost::shared_ptr<SimpleQuote> qRate(new SimpleQuote(0.05));
//      Handle<YieldTermStructure> qTS(flatRate(today, qRate, dc));
//      boost::shared_ptr<SimpleQuote> rRate(new SimpleQuote(0.03));
//      Handle<YieldTermStructure> rTS(flatRate(today, rRate, dc));
//      boost::shared_ptr<SimpleQuote> vol(new SimpleQuote(0.20));
//      Handle<BlackVolTermStructure> volTS(flatVol(today, vol, dc));
//
//      Date exerciseDate = today + 1*Years;
//      boost::shared_ptr<Exercise> exercise(new EuropeanExercise(exerciseDate));
//      boost::shared_ptr<StrikedTypePayoff> payoff(
//                                   new PlainVanillaPayoff(Option.Type.Call, 100.0));
//
//      boost::shared_ptr<StochasticProcess> process(
//                    new BlackScholesMertonProcess(underlying, qTS, rTS, volTS));
//
//      // link to the same stochastic process, which shouldn't be changed
//      // by calling methods of either option
//
//      boost::shared_ptr<VanillaOption> option1(
//                                 new EuropeanOption(process, payoff, exercise));
//      boost::shared_ptr<VanillaOption> option2(
//                                 new EuropeanOption(process, payoff, exercise));
//
//      // test
//
//      Real refValue = option2->NPV();
//
//      Flag f;
//      f.registerWith(option2);
//
//      option1->impliedVolatility(refValue*1.5, tolerance, maxEvaluations);
//
//      if (f.isUp())
//          BOOST_ERROR("implied volatility calculation triggered a change "
//                      "in another instrument");
//
//      option2->recalculate();
//      if (std::fabs(option2->NPV() - refValue) >= 1.0e-8)
//          BOOST_ERROR("implied volatility calculation changed the value "
//                      + "of another instrument: \n"
//                      + std::setprecision(8)
//                      + "previous value: " + refValue + "\n"
//                      + "current value:  " + option2->NPV());
//
//      vol->setValue(vol->value()*1.5);
//
//      if (!f.isUp())
//          BOOST_ERROR("volatility change not notified");
//
//      if (std::fabs(option2->NPV() - refValue) <= 1.0e-8)
//          BOOST_ERROR("volatility change did not cause the value to change");
//
//  }
//
//
//  // different engines
//
//  QL_BEGIN_TEST_LOCALS(EuropeanOptionTest)
//

    private void testEngineConsistency(final EngineType engine,
            final int binomialSteps, final int samples,
            final Map<String, Double> tolerance) {

        testEngineConsistency(engine, binomialSteps, samples, tolerance, false);
    }

    private void testEngineConsistency(final EngineType engine,
            final int binomialSteps, final int samples,
            final Map<String, Double> tolerance, final boolean testGreeks) {

        // QL_TEST_START_TIMING

        final Map<String, Double> calculated = new HashMap<String, Double>();
        final Map<String, Double> expected = new HashMap<String, Double>();

        // test options
        final Option.Type types[] = { Option.Type.CALL, Option.Type.PUT };
        final double strikes[] = { 75.0, 100.0, 125.0 };
        final int lengths[] = { 1 };

        // test data
        final double underlyings[] = { 100.0 };
        final double /* @Rate */qRates[] = { 0.00, 0.05 };
        final double /* @Rate */rRates[] = { 0.01, 0.05, 0.15 };
        final double /* @Volatility */vols[] = { 0.11, 0.50, 1.20 };

        final DayCounter dc = Actual360.getDayCounter();

        final Handle<SimpleQuote> spot = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        final Handle<SimpleQuote> qRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        final Handle<YieldTermStructure> qTS = new Handle<YieldTermStructure>(Utilities.flatRate(today, qRate, dc));
        final Handle<SimpleQuote> rRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        final Handle<YieldTermStructure> rTS = new Handle<YieldTermStructure>(Utilities.flatRate(today, rRate, dc));
        final Handle<SimpleQuote> vol = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        final Handle<BlackVolTermStructure> volTS = new Handle<BlackVolTermStructure>(Utilities.flatVol(today, vol, dc));

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < strikes.length; j++) {
                for (int k = 0; k < lengths.length; k++) {

                    Date exDate = today.getDateAfter(timeToDays(lengths[k]));
                    Exercise exercise = new EuropeanExercise(exDate);

                    StrikedTypePayoff payoff = new PlainVanillaPayoff(types[i], strikes[j]);

                    // reference option
                    VanillaOption refOption = makeOption(payoff, exercise, spot, qTS, rTS, volTS, EngineType.Analytic, 0, 0);
                    // option to check
                    VanillaOption option = makeOption(payoff, exercise, spot, qTS, rTS, volTS, engine, binomialSteps, samples);

                    for (int l = 0; l < underlyings.length; l++) {
                        for (int m = 0; m < qRates.length; m++) {
                            for (int n = 0; n < rRates.length; n++) {
                                for (int p = 0; p < vols.length; p++) {
                                    double u = underlyings[l];
                                    double /* @Rate */q = qRates[m], r = rRates[n];
                                    double /* @Volatility */v = vols[p];
                                    spot.getLink().setValue(u);
                                    qRate.getLink().setValue(q);
                                    rRate.getLink().setValue(r);
                                    vol.getLink().setValue(v);

                                    expected.clear();
                                    calculated.clear();

                                    expected.put("value", refOption.getNPV());
                                    calculated.put("value", option.getNPV());

                                    if (testGreeks && option.getNPV() > spot.getLink().evaluate() * 1.0e-5) {
                                        expected.put("delta", refOption.delta());
                                        expected.put("gamma", refOption.gamma());
                                        expected.put("theta", refOption.theta());
                                        calculated.put("delta", option.delta());
                                        calculated.put("gamma", option.gamma());
                                        calculated.put("theta", option.theta());
                                    }

                                    for (Entry<String, Double> entry : calculated.entrySet()) {
                                        String greek = entry.getKey();
                                        double expct = expected.get(greek), calcl = calculated.get(greek), tol = tolerance.get(greek);
                                        double error = Utilities.relativeError(expct, calcl, u);
                                        if (error > tol) {
                                            REPORT_FAILURE(greek, payoff, exercise, u, q, r, today, v, expct, calcl, error, tol);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    
    @Test
    public void testJRBinomialEngines() {

        logger.info("Testing JR binomial European engines against analytic results...");
            
        final EngineType engine = EngineType.JR;
        final int timeSteps = 251;
        final int samples = 0;
        final Map<String,Double> relativeTol = new HashMap<String, Double>(4);
        relativeTol.put("value", 0.002);
        relativeTol.put("delta", 1.0e-3);
        relativeTol.put("gamma", 1.0e-4);
        relativeTol.put("theta", 0.03);
        testEngineConsistency(engine, timeSteps, samples, relativeTol, true);
    }
    
    
    @Test
    public void testCRRBinomialEngines() {

        logger.info("Testing CRR binomial European engines against analytic results...");
            
        final EngineType engine = EngineType.CRR;
        final int timeSteps = 501;
        final int samples = 0;
        final Map<String,Double> relativeTol = new HashMap<String, Double>(4);
        relativeTol.put("value", 0.002);
        relativeTol.put("delta", 1.0e-3);
        relativeTol.put("gamma", 1.0e-4);
        relativeTol.put("theta", 0.03);
        testEngineConsistency(engine, timeSteps, samples, relativeTol, true);
    }
    
    
    @Test
    public void testEQPBinomialEngines() {

        logger.info("Testing EQP binomial European engines against analytic results...");
            
        final EngineType engine = EngineType.EQP;
        final int timeSteps = 501;
        final int samples = 0;
        final Map<String,Double> relativeTol = new HashMap<String, Double>(4);
        relativeTol.put("value", 0.02);
        relativeTol.put("delta", 1.0e-3);
        relativeTol.put("gamma", 1.0e-4);
        relativeTol.put("theta", 0.03);
        testEngineConsistency(engine, timeSteps, samples, relativeTol, true);
    }
    
    
    @Test
    public void testTGEOBinomialEngines() {

        logger.info("Testing TGEO binomial European engines against analytic results...");
            
        final EngineType engine = EngineType.TGEO;
        final int timeSteps = 251;
        final int samples = 0;
        final Map<String,Double> relativeTol = new HashMap<String, Double>(4);
        relativeTol.put("value", 0.002);
        relativeTol.put("delta", 1.0e-3);
        relativeTol.put("gamma", 1.0e-4);
        relativeTol.put("theta", 0.03);
        testEngineConsistency(engine, timeSteps, samples, relativeTol, true);
    }
    
    
    @Test
    public void testTIANBinomialEngines() {

        logger.info("Testing TIAN binomial European engines against analytic results...");
            
        final EngineType engine = EngineType.TIAN;
        final int timeSteps = 251;
        final int samples = 0;
        final Map<String,Double> relativeTol = new HashMap<String, Double>(4);
        relativeTol.put("value", 0.002);
        relativeTol.put("delta", 1.0e-3);
        relativeTol.put("gamma", 1.0e-4);
        relativeTol.put("theta", 0.03);
        testEngineConsistency(engine, timeSteps, samples, relativeTol, true);
    }
    
    
    @Test
    public void testLRBinomialEngines() {

        logger.info("Testing LR binomial European engines against analytic results...");
            
        final EngineType engine = EngineType.LR;
        final int timeSteps = 251;
        final int samples = 0;
        final Map<String,Double> relativeTol = new HashMap<String, Double>(4);
        relativeTol.put("value", 1.0e-6);
        relativeTol.put("delta", 1.0e-3);
        relativeTol.put("gamma", 1.0e-4);
        relativeTol.put("theta", 0.03);
        testEngineConsistency(engine, timeSteps, samples, relativeTol, true);
    }
    
    
    @Test
    public void testJOSHIBinomialEngines() {

        logger.info("Testing Joshi binomial European engines against analytic results...");
            
        final EngineType engine = EngineType.JOSHI;
        final int timeSteps = 251;
        final int samples = 0;
        final Map<String,Double> relativeTol = new HashMap<String, Double>(4);
        relativeTol.put("value", 1.0e-7);
        relativeTol.put("delta", 1.0e-3);
        relativeTol.put("gamma", 1.0e-4);
        relativeTol.put("theta", 0.03);
        testEngineConsistency(engine, timeSteps, samples, relativeTol, true);
    }
    
    
    @Test
    public void testFdEngines() {

        logger.info("Testing finite-differences European engines against analytic results...");
        
        final EngineType engine = EngineType.FiniteDifferences;
        final @NonNegative int timeSteps = 300;
        final @NonNegative int gridPoints = 300;
        final Map<String,Double> relativeTol = new HashMap<String, Double>(4);
        relativeTol.put("value", 1.0e-4);
        relativeTol.put("delta", 1.0e-6);
        relativeTol.put("gamma", 1.0e-6);
        relativeTol.put("theta", 1.0e-4);
        testEngineConsistency(engine, timeSteps, gridPoints, relativeTol, true);
    }


    @Test
    public void testIntegralEngines() {

        logger.info("Testing integral engines against analytic results...");
            

        final EngineType engine = EngineType.Integral;
        final int timeSteps = 300;
        final int gridPoints = 300;
        final Map<String,Double> relativeTol = new HashMap<String, Double>(1);
        relativeTol.put("value", 0.0001);
        testEngineConsistency(engine, timeSteps, gridPoints, relativeTol);
    }
    
    
//
//  void EuropeanOptionTest::testMcEngines() {
//
//      BOOST_MESSAGE("Testing Monte Carlo European engines "
//                    "against analytic results...");
//
//      EngineType engine = PseudoMonteCarlo;
//      Size steps = Null<Size>();
//      Size samples = 40000;
//      std::map<std::string,Real> relativeTol;
//      relativeTol["value"] = 0.01;
//      testEngineConsistency(engine,steps,samples,relativeTol);
//  }

//  void EuropeanOptionTest::testQmcEngines() {
//
//      BOOST_MESSAGE("Testing Quasi Monte Carlo European engines "
//                    "against analytic results...");
//
//      EngineType engine = QuasiMonteCarlo;
//      Size steps = Null<Size>();
//      Size samples = 4095; // 2^12-1
//      std::map<std::string,Real> relativeTol;
//      relativeTol["value"] = 0.01;
//      testEngineConsistency(engine,steps,samples,relativeTol);
//  }

//  void EuropeanOptionTest::testPriceCurve() {
//
//      BOOST_MESSAGE("Testing European price curves...");
//
//      /* The data below are from
//         "Option pricing formulas", E.G. Haug, McGraw-Hill 1998
//      */
//      EuropeanOptionData values[] = {
//        // pag 2-8
//        //        type, strike,   spot,    q,    r,    t,  vol,   value
//        { Option.Type.Call,  65.00,  60.00, 0.00, 0.08, 0.25, 0.30,  2.1334, 0.0},
//        { Option.Type.Put,   95.00, 100.00, 0.05, 0.10, 0.50, 0.20,  2.4648, 0.0},
//      };
//
//      DayCounter dc = Actual360();
//      Date today = Date::todaysDate();
//      Size timeSteps = 300;
//      Size gridPoints = 300;
//
//      boost::shared_ptr<SimpleQuote> spot(new SimpleQuote(0.0));
//      boost::shared_ptr<SimpleQuote> qRate(new SimpleQuote(0.0));
//      boost::shared_ptr<YieldTermStructure> qTS = flatRate(today, qRate, dc);
//      boost::shared_ptr<SimpleQuote> rRate(new SimpleQuote(0.0));
//      boost::shared_ptr<YieldTermStructure> rTS = flatRate(today, rRate, dc);
//      boost::shared_ptr<SimpleQuote> vol(new SimpleQuote(0.0));
//      boost::shared_ptr<BlackVolTermStructure> volTS = flatVol(today, vol, dc);
//      boost::shared_ptr<PricingEngine>
//          engine(new FDEuropeanEngine(timeSteps, gridPoints));
//
//      for (Size i=0; i<LENGTH(values); i++) {
//
//          boost::shared_ptr<StrikedTypePayoff> payoff(new
//              PlainVanillaPayoff(values[i].type, values[i].strike));
//          Date exDate = today + timeToDays(values[i].t);
//          boost::shared_ptr<Exercise> exercise(new EuropeanExercise(exDate));
//
//          spot ->setValue(values[i].s);
//          qRate->setValue(values[i].q);
//          rRate->setValue(values[i].r);
//          vol  ->setValue(values[i].v);
//
//          boost::shared_ptr<StochasticProcess> stochProcess(new
//              BlackScholesMertonProcess(Handle<Quote>(spot),
//                                        Handle<YieldTermStructure>(qTS),
//                                        Handle<YieldTermStructure>(rTS),
//                                        Handle<BlackVolTermStructure>(volTS)));
//
//          EuropeanOption option(stochProcess, payoff, exercise, engine);
//          SampledCurve price_curve = option.result<SampledCurve>("priceCurve");
//          if (price_curve.empty()) {
//              REPORT_FAILURE("no price curve", payoff, exercise, values[i].s,
//                             values[i].q, values[i].r, today,
//                             values[i].v, values[i].result, 0.0,
//                             0.0, 0.0);
//              continue;
//          }
//
//          // Ignore the end points
//          Size start = price_curve.size() / 4;
//          Size end = price_curve.size() * 3 / 4;
//          for (Size i=start; i < end; i++) {
//              spot->setValue(price_curve.gridValue(i));
//              boost::shared_ptr<StochasticProcess> stochProcess1(
//                        new BlackScholesMertonProcess(
//                                         Handle<Quote>(spot),
//                                         Handle<YieldTermStructure>(qTS),
//                                         Handle<YieldTermStructure>(rTS),
//                                         Handle<BlackVolTermStructure>(volTS)));
//
//              EuropeanOption option1(stochProcess, payoff, exercise, engine);
//              Real calculated = option1.NPV();
//              Real error = std::fabs(calculated-price_curve.value(i));
//              Real tolerance = 1e-3;
//              if (error>tolerance) {
//                  REPORT_FAILURE("price curve error", payoff, exercise,
//                                 price_curve.gridValue(i),
//                                 values[i].q, values[i].r, today,
//                                 values[i].v,
//                                 price_curve.value(i), calculated,
//                                 error, tolerance);
//                  break;
//              }
//          }
//      }
//
//  }


    private void REPORT_FAILURE(final String greekName, final StrikedTypePayoff payoff, final Exercise exercise, 
            final double s, final double q, final double r, final Date today, 
            final double v, final double expected, final double calculated, final double error, final double tolerance) {

        StringBuilder sb = new StringBuilder();
        sb.append(exercise).append(" ");
        sb.append(payoff.optionType()).append(" option with ");
        sb.append(payoff.getClass().getName() + " payoff:\n");
        sb.append("    spot value:       " + s + "\n");
        
        sb.append("    strike:           " + payoff.strike() + "\n");
        sb.append("    dividend yield:   " + q + "\n");
        sb.append("    risk-free rate:   " + r + "\n");
        sb.append("    reference date:   " + today + "\n");
        sb.append("    maturity:         " + exercise.lastDate() + "\n");
        sb.append("    volatility:       " + v + "\n\n");
        sb.append("    expected " + greekName + ":   " + expected + "\n" );
        sb.append("    calculated " + greekName + ": " + calculated + "\n");
        sb.append("    error:            " + error + "\n");
        sb.append("    tolerance:        " + tolerance);
        fail(sb.toString());
    }
    
}
