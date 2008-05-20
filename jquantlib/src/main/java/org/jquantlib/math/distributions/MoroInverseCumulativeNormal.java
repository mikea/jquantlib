package org.jquantlib.math.distributions;

import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.distributions.NormalDistribution;

public class MoroInverseCumulativeNormal extends NormalDistribution implements UnaryFunctionDouble{
	
	static final double a0_ =  2.50662823884;
	static final double a1_ =-18.61500062529;
	static final double a2_ = 41.39119773534;
	static final double a3_ =-25.44106049637;

	static final double b0_ = -8.47351093090;
	static final double b1_ = 23.08336743743;
	static final double b2_ =-21.06224101826;
	static final double b3_ =  3.13082909833;

	static final double c0_ = 0.3374754822726147;
	static final double c1_ = 0.9761690190917186;
	static final double c2_ = 0.1607979714918209;
	static final double c3_ = 0.0276438810333863;
	static final double c4_ = 0.0038405729373609;
	static final double c5_ = 0.0003951896511919;
	static final double c6_ = 0.0000321767881768;
	static final double c7_ = 0.0000002888167364;
	static final double c8_ = 0.0000003960315187;

    public MoroInverseCumulativeNormal() {
    	super();
    }

    public MoroInverseCumulativeNormal(double average, double sigma) {
    	super(average, sigma);
    }    
        
    public double evaluate(double x) /* Read-only */ {
        double result;
        double temp=x-0.5;
        
        // x has to be between 0.00 and 1.00
		if (x <= 0.0) {
			// System.out.println("x is " + x + " but has to be 0.0 < x < 1.0");
			return 0.00;
		}
		if (x >=1.0) {
			// System.out.println("x is " + x + " but has to be 0.0 < x < 1.0");
			return 1.00;
		}

        if (Math.abs(temp) < 0.42) {
            // Beasley and Springer, 1977
            result=temp*temp;
            result=temp*
                (((a3_*result+a2_)*result+a1_)*result+a0_) /
                ((((b3_*result+b2_)*result+b1_)*result+b0_)*result+1.0);
        } else {
            // improved approximation for the tail (Moro 1995)
            if (x<0.5)
                result = x;
            else
                result=1.0-x;
            result = Math.log(-Math.log(result));
            result = c0_+result*(c1_+result*(c2_+result*(c3_+result*
                                   (c4_+result*(c5_+result*(c6_+result*
                                                       (c7_+result*c8_)))))));
            if (x<0.5)
                result=-result;
        }
        return average + result * sigma;
    }
}
