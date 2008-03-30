package org.jquantlib.math;

import org.jquantlib.math.distributions.GammaFunction;

/**
 * @author <Richard Gomes>
 */
public class RegularisedIncompleteBeta {

	private double tolerance_;
	private int maxIterations_;
	
	public RegularisedIncompleteBeta(double tolerance, int maxIterations){

		checkMaxIterations(maxIterations);

		tolerance_ = tolerance;
		maxIterations_ = maxIterations;
	}
	
	public RegularisedIncompleteBeta(){
		tolerance_ = 1.0e-16;
		maxIterations_ = 100;
	}
	
    /*
    The implementation of the algorithm was inspired by
    "Numerical Recipes in C", 2nd edition,
    Press, Teukolsky, Vetterling, Flannery, chapter 6
  */
  private double betaContinuedFraction(double a, double  b, double  x,
                             double accuracy, int maxIteration) {

	  checkMaxIterations(maxIteration);
      double aa, del;
      double qab = a+b;
      double qap = a+1.0;
      double qam = a-1.0;
      double c = 1.0;
      double d = 1.0-qab*x/qap;
      if (Math.abs(d) < Constants.QL_EPSILON)
          d = Constants.QL_EPSILON;
      d = 1.0/d;
      double result = d;

      int m, m2;
      for (m=1; m<=maxIteration; m++) {
          m2=2*m;
          aa=m*(b-m)*x/((qam+m2)*(a+m2));
          d=1.0+aa*d;
          if (Math.abs(d) < Constants.QL_EPSILON) d=Constants.QL_EPSILON;
          c=1.0+aa/c;
          if (Math.abs(c) < Constants.QL_EPSILON) c=Constants.QL_EPSILON;
          d=1.0/d;
          result *= d*c;
          aa = -(a+m)*(qab+m)*x/((a+m2)*(qap+m2));
          d=1.0+aa*d;
          if (Math.abs(d) < Constants.QL_EPSILON) d=Constants.QL_EPSILON;
          c=1.0+aa/c;
          if (Math.abs(c) < Constants.QL_EPSILON) c=Constants.QL_EPSILON;
          d=1.0/d;
          del=d*c;
          result *= del;
          if (Math.abs(del-1.0) < accuracy)
              return result;
      }
      
      throw new ArithmeticException("a or b too big, or maxIteration too small in betacf");
  }

  /**
   * Regularised incomplete beta function
   * @param a
   * @param b
   * @param x
   * @param accuracy
   * @param maxIteration
   * @return regularised incomplete beta 
   */
  public double evaluate(double x,double a, double b) {
	  
	  checkMaxIterations(maxIterations_);

	  if (!(a > 0.0)){
		  throw new ArithmeticException("a must be greater than zero");
	  }
  
	  if (!(b > 0.0)){
		  throw new ArithmeticException("b must be greater than zero");
	  }


	  if (x == 0.0)
		  return 0.0;
	  
	  if (x == 1.0)
		  return 1.0;
	  
		
	  if(!(x>0.0 && x<1.0)){
		  throw new ArithmeticException("x must be in [0,1]");
	  }

	  double result = Math.exp(_gammaFunction.logValue(a+b) -
			  _gammaFunction.logValue(a) - _gammaFunction.logValue(b) +
			  a*Math.log(x) + b*Math.log(1.0-x));

	  if (x < (a+1.0)/(a+b+2.0)) {
		  return result *
          	betaContinuedFraction(a, b, x, tolerance_, maxIterations_)/a;
	  }
	  
	  return 1.0 - result *
          	betaContinuedFraction(b, a, 1.0-x, tolerance_, maxIterations_)/b;
  	}

	private void checkMaxIterations(int maxIterations){
		if (maxIterations<1) throw new IllegalArgumentException("Expected maxIterations>0, " + maxIterations);
	}

	private static final GammaFunction _gammaFunction = new GammaFunction();
	

}
