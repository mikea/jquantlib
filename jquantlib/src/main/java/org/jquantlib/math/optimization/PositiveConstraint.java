package org.jquantlib.math.optimization;

import org.jquantlib.math.Array;

public class PositiveConstraint extends Constraint {
      
      public  PositiveConstraint(){}
	  public boolean test(final Array params) {
                for (int i=0; i<params.size(); ++i) {
                    if (params.at(i) <= 0.0)
                        return false;
                }
                return true;
      }
	
 };
