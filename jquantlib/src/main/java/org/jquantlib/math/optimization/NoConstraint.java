package org.jquantlib.math.optimization;

import org.jquantlib.math.Array;

public class NoConstraint extends Constraint {
      
      public  NoConstraint(){}
	  public boolean test(Array nocArray) {
                return true;
      }
	
 };
