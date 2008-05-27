package org.jquantlib.math.integrals;

import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.Integrator;

/**
 * 
 * @author Dominik Holenstein
 *
 */

//Integral of a one-dimensional function
//Test the correctness of the result is tested by checking it
//against known good values.

// TODO: Add test case.  
public class SimpsonIntegral extends TrapezoidIntegral implements Integrator {
	
	public SimpsonIntegral (double accuracy, int NumberOfEvaluations) {
		super(accuracy,NumberOfEvaluations, Method.Default);
	}
	/*
    class SimpsonIntegral : public TrapezoidIntegral {
      public:
        SimpsonIntegral(Real accuracy,
                        Size maxIterations = Null<Size>())
        : TrapezoidIntegral(accuracy, Default, maxIterations) {}
      
        	
        } */
      public double integrate (UnaryFunctionDouble f, double a, double b) {
        
        // start from the coarsest trapezoid...
        int N = 1;
        double I = (f.evaluate(a)+f.evaluate(b))*(b-a)/2.0, newI;
        double adjI = I, newAdjI;
        // ...and refine it
        int i = 1;
        do {
           newI = defaultIteration(f,a,b,I,N);
           N *= 2;
           newAdjI = (4.0*newI-I)/3.0;
           // good enough? Also, don't run away immediately
           if (Math.abs(adjI-newAdjI) <= getAccuracy() && i > 5)
             // ok, exit
             return newAdjI;
             // oh well. Another step.
             I = newI;
             adjI = newAdjI;
             i++;
         } while (i < getNumberOfEvaluations());
         throw new ArithmeticException("max number of iterations reached");
    }
}
