package org.jquantlib.math.optimization;

import org.jquantlib.math.Array;

public class BoundaryConstraint extends Constraint {
      private double low_, high_; //inclusive!
      public  BoundaryConstraint(double low,double high){ low_ = low; high_ = high;}
	  public boolean test(Array bndArray) {
                 for (int i=0; i<bndArray.size(); i++) {
                    if ((bndArray.at(i) < low_) || (bndArray.at(i) > high_))
                        return false;
                }
                return true;
      }
	
 };
