package org.jquantlib.samples;

import java.util.Arrays;

import org.jquantlib.lang.iterators.Iterables;
import org.junit.Ignore;
import org.junit.Test;

public class TestSamples {

    private static final Class<?> complete[] = {
        Calendars.class, 
        ConvertibleBonds.class, 
        Dates.class, 
        VolatilityTermStructures.class, 
    };

    private static final Class<?> incomplete[] = {
        EquityOptions.class, 
    };

    
    private static final Class<?> pending[] = {
        Bonds.class, 
        YieldCurveTermStructures.class, 
        BermudanSwaption.class, 
        FRA.class, 
        Processes.class, 
        Replication.class, 
        Repo.class, 
        Swap.class, 
        DiscreteHedging.class, 
        CoxRossWithHullWhite.class, 
        SobolChartSample.class
    };
    

    @Test
    public void testCompleteSamples() {
        testSamples(complete);
    }
    
    @Test
    public void testIncompleteSamples() {
        testSamples(incomplete);
    }
    
    @Ignore
    @Test
    public void testPendingSamples() {
        testSamples(complete);
    }
    
    
    private void testSamples(final Class<?> klasses[]) {
        for (final Class<?> klass : Iterables.unmodifiableIterable(Arrays.asList(klasses).iterator())) {
            System.err.println();
            System.err.println("*************************************************************");
            System.err.println("* Running " + klass.getSimpleName());
            System.err.println("*************************************************************");
            Runnable r = null;
            try {
                r = (Runnable) klass.newInstance();
                r.run();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    

}
