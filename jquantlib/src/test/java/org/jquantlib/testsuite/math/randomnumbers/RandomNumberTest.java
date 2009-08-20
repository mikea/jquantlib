package org.jquantlib.testsuite.math.randomnumbers;

import org.jquantlib.QL;
import org.junit.Test;

public class RandomNumberTest {

    public RandomNumberTest() {
        QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
    }

    @Test
    public void testGaussian(){
        QL.info("Testing Gaussian pseudo-random number generation...");
        QL.info("to be implemented");

    }

    @Test
    public void testDefaultPoisson(){
        QL.info("Testing Poisson pseudo-random number generation...");
        QL.info("to be implemented");
    }

    @Test
    public void testCustomPoisson(){
        QL.info("Testing custom Poisson pseudo-random number generation...");
        QL.info("to be implemented");
    }

}
