package org.jquantlib.testsuite.math.statistics;

import org.jquantlib.math.statistics.Statistics;
import org.jquantlib.testsuite.model.volatility.EstimatorsTest;
import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultDate;
import org.jquantlib.util.Month;
import org.jquantlib.util.TimeSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticsTest {
    
    private final static Logger logger = LoggerFactory.getLogger(StatisticsTest.class);

    private static final double data[] =    { 3.0, 4.0, 5.0, 2.0, 3.0, 4.0, 5.0, 6.0, 4.0, 7.0 };
    private static final double weights[] = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
    
    
    public StatisticsTest() {
        logger.info("Testing volatility model construction...");
    }
    
    public void check(Statistics s){
        for(int i = 0; i<data.length; i++){
            
        }
        
    }

}
