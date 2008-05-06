package org.jquantlib.perf.math;

import org.jquantlib.math.distributions.BinomialDistribution;
import org.jquantlib.testsuite.util.StopClock;
import org.junit.Test;

import cern.jet.random.Binomial;

public class BinomialTest {
  StopClock clock = new StopClock(StopClock.Unit.NANO);
 
  
  @Test public void runColt(){
	  System.out.println("Run colt");
	  Binomial binomial = new Binomial(2000,0.9,null);
	  for(int i=0;i<100;i++){
		  clock.reset();
		  clock.startClock();
		  binomial.pdf(5);
		  clock.stopClock();
	  }
	  
	  double v = 0;
	  clock.reset();
	  clock.startClock();
	  for(int i=0;i<1000;i++){
	   v = binomial.pdf(1900);
	  }
	  clock.stopClock();
	  System.out.println(v);
	  clock.log();
  }
  
  @Test public void runJQ(){
	  System.out.println("\nRun JQ");
	  BinomialDistribution binomial = new BinomialDistribution(0.9,2000);
	  for(int i=0;i<100;i++){
		  clock.reset();
		  clock.startClock();
		  binomial.evaluate(5);
		  clock.stopClock();
	  }
	  
	  double v = 0;
	  clock.reset();
	  clock.startClock();
	  for(int i=0;i<1000;i++){
	   v = binomial.evaluate(1900);
	  }
	  clock.stopClock();
	  System.out.println(v);
	  clock.log();
  }
  
  public static void main(String[] str){
	  BinomialTest binomialTest = new BinomialTest();
	  binomialTest.runJQ();
	  binomialTest.runColt();
	  
  }
}
