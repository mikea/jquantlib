package org.jquantlib.testsuite.math.randomnumbers;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomNumberTest {
    
    private final static Logger logger = LoggerFactory.getLogger(RandomNumberTest.class);
    
    public RandomNumberTest() {
        logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
    }
    
    @Test
    public void testGaussian(){
        logger.info("Testing Gaussian pseudo-random number generation...");
        logger.info("to be implemented");

    }
    
    @Test
    public void testDefaultPoisson(){
        logger.info("Testing Poisson pseudo-random number generation...");
        logger.info("to be implemented");
    }
    
    @Test
    public void testCustomPoisson(){
        logger.info("Testing custom Poisson pseudo-random number generation...");
        logger.info("to be implemented");
    }

}
