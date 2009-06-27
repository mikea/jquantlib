package org.jquantlib.testsuite.pricingengines;

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
import org.jquantlib.pricingengines.AnalyticEuropeanEngine;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.VanillaOptionEngine;
import org.jquantlib.pricingengines.vanilla.JumpDiffusionEngine;
import org.jquantlib.processes.Merton76Process;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.testsuite.util.Utilities;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JumpDiffusionEngineTest {

    private final static Logger logger = LoggerFactory.getLogger(JumpDiffusionEngineTest.class);

    private final Settings settings;
    private final Date today;      

    public JumpDiffusionEngineTest() {
        logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
        this.settings = Configuration.getSystemConfiguration(null).getGlobalSettings();
        this.today = DateFactory.getFactory().getTodaysDate(); //TODO: code review
    }
    

    @Test
    public void testMerton76() {
        logger.info("Testing Merton 76 jump-diffusion model for European options...");

        // The data below are from 
        //"Option pricing formulas", E.G. Haug, McGraw-Hill 1998, pag 9
        //
        // Haug use the arbitrary truncation criterium of 11 terms in the sum,
        // which doesn't guarantee convergence up to 1e-2.
        // Using Haug's criterium Haug's values have been correctly reproduced.
        // the following values have the right 1e-2 accuracy: any value different
        // from Haug has been noted.
        HaugMertonData values[] = {
                //        type, strike,   spot,    q,    r,    t,  vol, int, gamma, value, tol
                // gamma = 0.25, strike = 80
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.25, 20.67, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.25, 21.74, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.25, 23.63, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.25, 20.65, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.25, 21.70, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.25, 23.61, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.25, 20.64, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.25, 21.70, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.25, 23.61, 1e-2) , // Haug 23.28
                // gamma = 0.25, strike = 90
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.25, 11.00, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.25, 12.74, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.25, 15.40, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.25, 10.98, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.25, 12.75, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.25, 15.42, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.25, 10.98, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.25, 12.75, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.25, 15.42, 1e-2) , // Haug 15.20
                // gamma = 0.25, strike = 100
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.25,  3.42, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.25,  5.88, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.25,  8.95, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.25,  3.51, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.25,  5.96, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.25,  9.02, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.25,  3.53, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.25,  5.97, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.25,  9.03, 1e-2) , // Haug 8.89
                // gamma = 0.25, strike = 110
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.25,  0.55, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.25,  2.11, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.25,  4.67, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.25,  0.56, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.25,  2.16, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.25,  4.73, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.25,  0.56, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.25,  2.17, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.25,  4.74, 1e-2) , // Haug 4.66
                // gamma = 0.25, strike = 120
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.25,  0.10, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.25,  0.64, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.25,  2.23, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.25,  0.06, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.25,  0.63, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.25,  2.25, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.25,  0.05, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.25,  0.62, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.25,  2.25, 1e-2) , // Haug 2.21

                // gamma = 0.50, strike = 80
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.50, 20.72, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.50, 21.83, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.50, 23.71, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.50, 20.66, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.50, 21.73, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.50, 23.63, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.50, 20.65, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.50, 21.71, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.50, 23.61, 1e-2) , // Haug 23.28
                // gamma = 0.50, strike = 90
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.50, 11.04, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.50, 12.72, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.50, 15.34, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.50, 11.02, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.50, 12.76, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.50, 15.41, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.50, 11.00, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.50, 12.75, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.50, 15.41, 1e-2) , // Haug 15.18
                // gamma = 0.50, strike = 100
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.50,  3.14, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.50,  5.58, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.50,  8.71, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.50,  3.39, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.50,  5.87, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.50,  8.96, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.50,  3.46, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.50,  5.93, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.50,  9.00, 1e-2) , // Haug 8.85
                // gamma = 0.50, strike = 110
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.50,  0.53, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.50,  1.93, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.50,  4.42, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.50,  0.58, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.50,  2.11, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.50,  4.67, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.50,  0.57, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.50,  2.14, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.50,  4.71, 1e-2) , // Haug 4.62
                // gamma = 0.50, strike = 120
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.50,  0.19, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.50,  0.71, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.50,  2.15, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.50,  0.10, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.50,  0.66, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.50,  2.23, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.50,  0.07, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.50,  0.64, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.50,  2.24, 1e-2) , // Haug 2.19

                // gamma = 0.75, strike = 80
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.75, 20.79, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.75, 21.96, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.75, 23.86, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.75, 20.68, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.75, 21.78, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.75, 23.67, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.75, 20.66, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.75, 21.74, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  80.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.75, 23.64, 1e-2) , // Haug 23.30
                // gamma = 0.75, strike = 90
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.75, 11.11, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.75, 12.75, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.75, 15.30, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.75, 11.09, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.75, 12.78, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.75, 15.39, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.75, 11.04, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.75, 12.76, 1e-2) ,
                new HaugMertonData( Option.Type.CALL,  90.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.75, 15.40, 1e-2) , // Haug 15.17
                // gamma = 0.75, strike = 100
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.75,  2.70, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.75,  5.08, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.75,  8.24, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.75,  3.16, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.75,  5.71, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.75,  8.85, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.75,  3.33, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.75,  5.85, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 100.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.75,  8.95, 1e-2) , // Haug 8.79
                // gamma = 0.75, strike = 110
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.75,  0.54, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.75,  1.69, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.75,  3.99, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.75,  0.62, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.75,  2.05, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.75,  4.57, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.75,  0.60, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.75,  2.11, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 110.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.75,  4.66, 1e-2) , // Haug 4.56
                // gamma = 0.75, strike = 120
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.10, 0.25, 1.0,  0.75,  0.29, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.25, 0.25, 1.0,  0.75,  0.84, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.50, 0.25, 1.0,  0.75,  2.09, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.10, 0.25, 5.0,  0.75,  0.15, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.25, 0.25, 5.0,  0.75,  0.71, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.50, 0.25, 5.0,  0.75,  2.21, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.10, 0.25,10.0,  0.75,  0.11, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.25, 0.25,10.0,  0.75,  0.67, 1e-2) ,
                new HaugMertonData( Option.Type.CALL, 120.00, 100.00, 0.00, 0.08, 0.50, 0.25,10.0,  0.75,  2.23, 1e-2)  // Haug 2.17
        };
    
        DayCounter dc = Actual360.getDayCounter();
        Date today = DateFactory.getFactory().getTodaysDate();

        Handle<SimpleQuote> spot = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<SimpleQuote> qRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        YieldTermStructure qTS = Utilities.flatRate(today, qRate, dc);
        Handle<SimpleQuote> rRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        YieldTermStructure rTS = Utilities.flatRate(today, rRate, dc);
        Handle<SimpleQuote> vol = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        BlackVolTermStructure volTS = Utilities.flatVol(today, vol, dc);

        Handle<SimpleQuote> jumpIntensity = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<SimpleQuote> meanLogJump = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<SimpleQuote> jumpVol = new Handle<SimpleQuote>(new SimpleQuote(0.0));

        StochasticProcess stochProcess = new Merton76Process(spot, new Handle<YieldTermStructure>(qTS),
                new Handle<YieldTermStructure>(rTS), new Handle<BlackVolTermStructure>(volTS), jumpIntensity, meanLogJump, jumpVol);

        VanillaOptionEngine baseEngine = new AnalyticEuropeanEngine();
        PricingEngine engine = new JumpDiffusionEngine(baseEngine);

        for (int i = 0; i < values.length; i++) {
            StrikedTypePayoff payoff = new PlainVanillaPayoff(values[i].type, values[i].strike);

            Date exDate = today.getDateAfter((int) (values[i].t * 360 + 0.5));
            Exercise exercise = new EuropeanExercise(exDate);

            spot.getLink().setValue(values[i].s);
            qRate.getLink().setValue(values[i].q);
            rRate.getLink().setValue(values[i].r);

            jumpIntensity.getLink().setValue(values[i].jumpIntensity);

            // delta in Haug's notation
            double /* @Real */jVol = values[i].v * Math.sqrt(values[i].gamma / values[i].jumpIntensity);
            jumpVol.getLink().setValue(jVol);

            // z in Haug's notation
            double /* @Real */diffusionVol = values[i].v * Math.sqrt(1.0 - values[i].gamma);
            vol.getLink().setValue(diffusionVol);

            // Haug is assuming zero meanJump
            double /* @Real */meanJump = 0.0;
            meanLogJump.getLink().setValue(Math.log(1.0 + meanJump) - 0.5 * jVol * jVol);

            double totalVol = Math.sqrt(values[i].jumpIntensity * jVol * jVol + diffusionVol * diffusionVol);
            double volError = Math.abs(totalVol - values[i].v);

            if (volError >= 1.0e-13) {
                throw new ArithmeticException(" mismatch");
            }

            EuropeanOption option = new EuropeanOption(stochProcess, payoff, exercise, engine);

            double /* @Real */calculated = option.getNPV();
            double /* @Real */error = Math.abs(calculated - values[i].result);
            if (error > values[i].tol) {

                REPORT_FAILURE_2("value", payoff, exercise, values[i].s, values[i].q, values[i].r, today, values[i].v,
                        values[i].jumpIntensity, values[i].gamma, values[i].result, calculated, error, values[i].tol);
            }
        }
    }
    
    
    private void REPORT_FAILURE_2(String greekName, StrikedTypePayoff payoff, Exercise exercise, double s, double q, double r,
            Date today, double v, double intensity, double gamma, double expected, double calculated, double error, double tolerance) {

        Assert.fail(exercise + " " + payoff.optionType() + " option with " + payoff + " payoff:\n" + "    underlying value: " + s
                + "\n" + "    strike:           " + payoff.strike() + "\n" + "    dividend yield:   " + q + "\n"
                + "    risk-free rate:   " + r + "\n" + "    reference date:   " + today + "\n" + "    maturity:         "
                + exercise.lastDate() + "\n" + "    volatility:       " + v + "\n" + "    intensity:        " + intensity + "\n"
                + "    gamma:            " + gamma + "\n\n" + "    expected   " + greekName + ": " + expected + "\n"
                + "    calculated " + greekName + ": " + calculated + "\n" + "    error:            " + error + "\n"
                + "    tolerance:        " + tolerance);
    }

    
    @Test
    public void testGreeks() {
        logger.info("Testing jump-diffusion option greeks...");

        Map<String, Double> calculated = new HashMap<String, Double>();
        Map<String, Double> expected = new HashMap<String, Double>();
        Map<String, Double> tolerance = new HashMap<String, Double>();
        tolerance.put("delta", 1.0e-4);
        tolerance.put("gamma", 1.0e-4);
        tolerance.put("theta", 1.1e-4);
        tolerance.put("rho", 1.0e-4);
        tolerance.put("divRho", 1.0e-4);
        tolerance.put("vega", 1.0e-4);

        Option.Type types[] = { Option.Type.PUT, Option.Type.CALL };

        double strikes[] = { 50.0, 100.0, 150.0 };
        double underlyings[] = { 100.0 };
        double qRates[] = { -0.05, 0.0, 0.05 };
        double rRates[] = { 0.0, 0.01, 0.2 };
        // The testsuite check fails if a too short maturity is chosen(i.e. 1 year).
        // The problem is in the theta calculation. With the finite difference(fd) method
        // we might get values too close to the jump steps, invalidating the fd methodology
        // for calculating greeks.
        double residualTimes[] = { 5.0 };
        double vols[] = { 0.11 };
        double jInt[] = { 1.0, 5.0 };
        double mLJ[] = { -0.20, 0.0, 0.20 };
        double jV[] = { 0.01, 0.25 };

        DayCounter dc = Actual360.getDayCounter();

        Handle<SimpleQuote> spot = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<SimpleQuote> qRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        YieldTermStructure qTS = Utilities.flatRate(qRate, dc);
        Handle<SimpleQuote> rRate = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        YieldTermStructure rTS = Utilities.flatRate(rRate, dc);
        Handle<SimpleQuote> vol = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        BlackVolTermStructure volTS = Utilities.flatVol(vol, dc);

        Handle<SimpleQuote> jumpIntensity = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<SimpleQuote> meanLogJump = new Handle<SimpleQuote>(new SimpleQuote(0.0));
        Handle<SimpleQuote> jumpVol = new Handle<SimpleQuote>(new SimpleQuote(0.0));

        StochasticProcess stochProcess = new Merton76Process(spot, new Handle<YieldTermStructure>(qTS),
                new Handle<YieldTermStructure>(rTS), new Handle<BlackVolTermStructure>(volTS), jumpIntensity, meanLogJump, jumpVol);

        StrikedTypePayoff payoff = null;

        VanillaOptionEngine baseEngine = new AnalyticEuropeanEngine();
        // The jumpdiffusionengine greeks are very sensitive to the convergence level.
        // A tolerance of 1.0e-08 is usually sufficient to get reasonable results
        PricingEngine engine = new JumpDiffusionEngine(baseEngine, 1e-08);

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < strikes.length; j++) {
                for (int jj1 = 0; jj1 < jInt.length; jj1++) {
                    jumpIntensity.getLink().setValue(jInt[jj1]);
                    for (int jj2 = 0; jj2 < mLJ.length; jj2++) {
                        meanLogJump.getLink().setValue(mLJ[jj2]);
                        for (int jj3 = 0; jj3 < jV.length; jj3++) {
                            jumpVol.getLink().setValue(jV[jj3]);
                            for (int k = 0; k < residualTimes.length; k++) {
                                Date exDate = today.getDateAfter((int) (residualTimes[k] * 360 + 0.5));
                                Exercise exercise = new EuropeanExercise(exDate);

                                for (int kk = 0; kk < 1; kk++) {
                                    // option to check
                                    if (kk == 0) {
                                        payoff = new PlainVanillaPayoff(types[i], strikes[j]);
                                    } else if (kk == 1) {
                                        payoff = new CashOrNothingPayoff(types[i], strikes[j], 100.0);
                                    } else if (kk == 2) {
                                        payoff = new AssetOrNothingPayoff(types[i], strikes[j]);
                                    } else if (kk == 3) {
                                        payoff = new GapPayoff(types[i], strikes[j], 100.0);
                                    }
                                    EuropeanOption option = new EuropeanOption(stochProcess, payoff, exercise, engine);

                                    for (int l = 0; l < underlyings.length; l++) {
                                        double u = underlyings[l];
                                        for (int m = 0; m < qRates.length; m++) {
                                            double q = qRates[m];
                                            for (int n = 0; n < rRates.length; n++) {
                                                double r = rRates[n];
                                                for (int p = 0; p < vols.length; p++) {
                                                    double v = vols[p];
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

                                                    if (value > spot.getLink().evaluate() * 1.0e-5) {
                                                        // perturb spot and get delta and gamma
                                                        double du = u * 1.0e-5;
                                                        spot.getLink().setValue(u + du);
                                                        double value_p = option.getNPV();
                                                        double delta_p = option.delta();
                                                        spot.getLink().setValue(u - du);
                                                        double value_m = option.getNPV();
                                                        double delta_m = option.delta();
                                                        spot.getLink().setValue(u);
                                                        expected.put("delta", (value_p - value_m) / (2 * du));
                                                        expected.put("gamma", (delta_p - delta_m) / (2 * du));

                                                        // perturb rates and get rho and dividend rho
                                                        double dr = 1.0e-5;
                                                        rRate.getLink().setValue(r + dr);
                                                        value_p = option.getNPV();
                                                        rRate.getLink().setValue(r - dr);
                                                        value_m = option.getNPV();
                                                        rRate.getLink().setValue(r);
                                                        expected.put("rho", (value_p - value_m) / (2 * dr));

                                                        double dq = 1.0e-5;
                                                        qRate.getLink().setValue(q + dq);
                                                        value_p = option.getNPV();
                                                        qRate.getLink().setValue(q - dq);
                                                        value_m = option.getNPV();
                                                        qRate.getLink().setValue(q);
                                                        expected.put("divRho", (value_p - value_m) / (2 * dq));

                                                        // perturb volatility and get vega
                                                        double dv = v * 1.0e-4;
                                                        vol.getLink().setValue(v + dv);
                                                        value_p = option.getNPV();
                                                        vol.getLink().setValue(v - dv);
                                                        value_m = option.getNPV();
                                                        vol.getLink().setValue(v);
                                                        expected.put("vega", (value_p - value_m) / (2 * dv));

                                                        final Date yesterday = today.getPreviousDay();
                                                        final Date tomorrow = today.getNextDay();
                                                        double dT = dc.yearFraction(yesterday, tomorrow);
                                                        settings.setEvaluationDate(yesterday);
                                                        value_m = option.getNPV();
                                                        settings.setEvaluationDate(tomorrow);
                                                        value_p = option.getNPV();
                                                        expected.put("theta", (value_p - value_m) / dT);

                                                        settings.setEvaluationDate(today);

                                                        // compare
                                                        // compare
                                                        for (Entry<String, Double> it : calculated.entrySet()) {

                                                            String greek = it.getKey();
                                                            Double expct = expected.get(greek);
                                                            Double calcl = calculated.get(greek);
                                                            Double tol = tolerance.get(greek);

                                                            double error = Utilities.relativeError(expct, calcl, u);
                                                            if (error > tol) {
                                                                // TODO improve error message
                                                                // REPORT_FAILURE(greek, payoff, exercise, u, q, r, today, v, expct,
                                                                // calcl, error, tol);
                                                                Assert.fail("Failed on greek: " + greek);
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
            }
        }
    } 


    //
    // private inner classes
    //
    
    static private class HaugMertonData {
        public Option.Type type;
        public double strike;
        public double s;        // spot
        public double q;        // dividend
        public double r;        // risk-free rate
        public double t;        // time to maturity
        public double v;        // volatility
        public double jumpIntensity;
        public double gamma;
        public double result;   // result
        public double tol;      // tolerance
    
        public HaugMertonData(  Option.Type type,
                                double strike,
                                double spot,
                                double q,
                                double r,
                                double t,
                                double vol,
                                double intensity,
                                double gamma,
                                double value,
                                double tol){
            this.type = type;
            this.strike = strike;
            this.s = spot;
            this.q = q;
            this.r = r;
            this.t = t;
            this.v = vol;
            this.jumpIntensity = intensity;
            this.gamma = gamma;
            this.result = value;
            this.tol = tol;
        }
    }

}
