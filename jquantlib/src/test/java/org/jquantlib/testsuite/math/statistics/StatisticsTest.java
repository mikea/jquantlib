package org.jquantlib.testsuite.math.statistics;

import static org.junit.Assert.fail;

import org.jquantlib.math.Array;
import org.jquantlib.math.statistics.GenericSequenceStatistics;
import org.jquantlib.math.statistics.IStatistics;
import org.jquantlib.math.statistics.IncrementalStatistics;
import org.jquantlib.math.statistics.Statistics;
import org.jquantlib.util.stdlibc.Std;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class StatisticsTest {
    
    private final static Logger logger = LoggerFactory.getLogger(StatisticsTest.class);

    private static final double data[] =    { 3.0, 4.0, 5.0, 2.0, 3.0, 4.0, 5.0, 6.0, 4.0, 7.0 };
    private static final double weights[] = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
    
    
    public StatisticsTest() {
        logger.info("Testing volatility model construction...");
    }
    
    public void check(IStatistics s, String name){
        for(int i = 0; i<data.length; i++){
            s.add(data[i], weights[i] );
        }
        
        double calculated, expected;
        double tolerance;
        
        if(s.samples()!=data.length){
            fail("wrong number of samples \n" + 
                    "calculated: " + s.samples() + "\n" +
                    "expected: " + data.length);
        }
        expected = Std.getInstance().accumulate(weights,0.0);
        calculated = s.weightSum();
        if (calculated != expected){
            fail(name  + ": wrong sum of weights\n"
            + "    calculated: " + calculated + "\n"
            + "    expected:   " + expected);
        }
        
        expected = Std.getInstance().min_element(0, data.length, data);
        calculated = s.min();
        if(calculated != expected){
            fail(name + ": wrong minimum value \n" + 
                    "calculated: " + calculated + "\n" +
                    "expected: " + expected);
        }
        
        expected = Std.getInstance().max_element(0, data.length, data);
        calculated = s.max();
        if(calculated != expected){
            fail(name + ": wrong maxmimum value \n" +
                    "calculated: " + expected + "\n" +
                    "expected: " + expected);
        }
        
        expected = 4.3;
        tolerance = 1.0e-9;
        calculated = s.mean();
        if(Math.abs(calculated - expected)>tolerance){
            fail(name + "wrong mean value" + "\n" +
                    "calculated: " + calculated + "\n" + 
                    "expected: " + expected);
        }
        
        expected = 2.3333333333;
        calculated = s.variance();
        if(Math.abs(calculated - expected) > tolerance){
            fail(name + "wrong variance" + "\n" +
                    "calculated: " + calculated + "\n" + 
                    "expected: " + expected);
        }
        
        expected = 1.4944341181;
        calculated = s.standardDeviation();
        if (Math.abs(calculated-expected) > tolerance){
            fail(name + "wrong standard deviation" + "\n" +
                    "calculated: " + calculated + "\n" + 
                    "expected: " + expected);
        }

        expected = 0.359543071407;
        calculated = s.skewness();
        if (Math.abs(calculated-expected) > tolerance){
            fail(name + "wrong skewness" + "\n" +
                    "calculated: " + calculated + "\n" + 
                    "expected: " + expected);
        }

        expected = -0.151799637209;
        calculated = s.kurtosis();
        if (Math.abs(calculated-expected) > tolerance){
            fail(name + "wrong skewness" + "\n" +
                    "calculated: " + calculated + "\n" + 
                    "expected: " + expected);
        }
    }
    
    @Ignore
    @Test
    public void testStatistics(){
        logger.info("Testing statistics ...");
        //check(new IncrementalStatistics(), "IncrementalStatistics");
        //check(new Statistics(), "Statistics");
    }
    
    
    public void checkSequence(IStatistics statistics, String name, int dimension){
        /*
        GenericSequenceStatistics ss = new GenericSequenceStatistics(statistics, dimension);
        int i;
        for(i = 0; i<data.length; i++){
            double [] temp  = new double [dimension];
            java.util.Arrays.fill(temp, 0, dimension, data[i]);
            ss.add(temp, weights[i]);
        }
        */
        
        
    }

    
    
    
    
    
}
