package org.jquantlib.testsuite.helpers;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.helpers.FDAmericanDividendOptionCalculator;
import org.jquantlib.helpers.FDEuropeanDividendOptionCalculator;
import org.jquantlib.instruments.DividendVanillaOption;
import org.jquantlib.instruments.Option;
import org.jquantlib.samples.util.StopClock;
import org.jquantlib.samples.util.StopClock.Unit;
import org.jquantlib.time.Date;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.junit.Test;

public class FDDividendOptionTest implements Runnable {

    public static void main(final String[] args) {
        new FDDividendOptionTest().run();
    }

    private boolean quiet = false;

    public void run() {
        // training session: allow JIT compile code
        quiet = true;
        for (int i=0; i<20; i++) {
            testEuropeanFDDividendOption();
            testAmericanFDDividendOption();
        }

        quiet = false;
        testEuropeanFDDividendOption();
        testAmericanFDDividendOption();
    }

    @Test
    public void testEuropeanFDDividendOption() {
        if (!quiet) {
            QL.info("::::: " + this.getClass().getSimpleName() + " ::::: European Dividend Option :::::");
        }

        final StopClock clock = new StopClock(Unit.ns);
        clock.startClock();

        final Date today = Date.todaysDate();
        final Date expiry = today.add(new Period(5, TimeUnit.Months));
        final Date divDate = today.add(new Period(3, TimeUnit.Months)).add(new Period(15, TimeUnit.Days));

        final List<Date> divDates = new ArrayList<Date>();
        divDates.add(divDate);
        final List<Double> divAmounts = new ArrayList<Double>();
        divAmounts.add(2.06);
        final DividendVanillaOption option = new FDEuropeanDividendOptionCalculator(
                Option.Type.Call, 50.0, 50.0, 0.1, 0.394,
                expiry, divDates, divAmounts);

        final double value = option.NPV();
        final double delta = option.delta();
        final double gamma = option.gamma();
        //double theta = option.theta(); // requires a dT
        //double vega  = option.vega();  // requires a dT
        //double rho   = option.rho();   // requires a dT
        clock.stopClock();

        if (!quiet) {
            System.out.printf("value = %f\n", value);
            System.out.printf("delta = %f\n", delta);
            System.out.printf("gamma = %f\n", gamma);
            // System.out.printf("theta = %f\n", theta);
            // System.out.printf("vega  = %f\n", vega);
            // System.out.printf("rho   = %f\n", rho);
            clock.log();
        }

    }

    @Test
    public void testAmericanFDDividendOption() {
        if (!quiet) {
            QL.info("::::: " + this.getClass().getSimpleName() + " ::::: American Dividend Option :::::");
        }

        final StopClock clock = new StopClock();
        clock.startClock();

        final Date today = Date.todaysDate();
        final Date expiry = today.add(new Period(5, TimeUnit.Months));
        final Date divDate = today.add(new Period(3, TimeUnit.Months)).add(new Period(15, TimeUnit.Days));

        final List<Date> divDates = new ArrayList<Date>();
        divDates.add(divDate);
        final List<Double> divAmounts = new ArrayList<Double>();
        divAmounts.add(2.06);
        final DividendVanillaOption option = new FDAmericanDividendOptionCalculator(
                Option.Type.Call, 50.0, 50.0, 0.1, 0.394,
                expiry, divDates, divAmounts);

        final double value = option.NPV();
        final double delta = option.delta();
        final double gamma = option.gamma();
        //double theta = option.theta(); // requires a dT
        //double vega  = option.vega();  // requires a dT
        //double rho   = option.rho();   // requires a dT
        clock.stopClock();

        if (!quiet) {
            System.out.printf("value = %f\n", value);
            System.out.printf("delta = %f\n", delta);
            System.out.printf("gamma = %f\n", gamma);
            // System.out.printf("theta = %f\n", theta);
            // System.out.printf("vega  = %f\n", vega);
            // System.out.printf("rho   = %f\n", rho);
            clock.log();
        }
    }

}
