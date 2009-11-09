package org.jquantlib.testsuite.helpers;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.helpers.FDAmericanDividendOptionHelper;
import org.jquantlib.helpers.FDEuropeanDividendOptionHelper;
import org.jquantlib.instruments.DividendVanillaOption;
import org.jquantlib.instruments.Option;
import org.jquantlib.time.Date;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.StopClock;
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

        final StopClock clock = new StopClock();
        clock.startClock();

        final Date today = Date.todaysDate();
        final Date expiry = today.add(new Period(5, TimeUnit.Months));
        final Date divDate = today.add(new Period(3, TimeUnit.Months)).add(new Period(15, TimeUnit.Days));

        final List<Date> divDates = new ArrayList<Date>();
        divDates.add(divDate);
        final List<Double> divAmounts = new ArrayList<Double>();
        divAmounts.add(2.06);
        final DividendVanillaOption option = new FDEuropeanDividendOptionHelper(
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
            QL.info(String.format("value = %f", value));
            QL.info(String.format("delta = %f", delta));
            QL.info(String.format("gamma = %f", gamma));
            // QL.info(String.format("theta = %f", theta));
            // QL.info(String.format("vega  = %f", vega));
            // QL.info(String.format("rho   = %f", rho));
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
        final DividendVanillaOption option = new FDAmericanDividendOptionHelper(
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
            QL.info(String.format("value = %f", value));
            QL.info(String.format("delta = %f", delta));
            QL.info(String.format("gamma = %f", gamma));
            // QL.info(String.format("theta = %f", theta));
            // QL.info(String.format("vega  = %f", vega));
            // QL.info(String.format("rho   = %f", rho));
            clock.log();
        }
    }

}
