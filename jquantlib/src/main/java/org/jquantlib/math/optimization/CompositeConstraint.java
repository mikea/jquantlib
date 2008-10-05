package org.jquantlib.math.optimization;

import org.jquantlib.math.Array;
import org.jquantlib.math.optimization.Constraint;

public class CompositeConstraint extends Constraint {
      private Constraint c1_, c2_;
      public  CompositeConstraint(Constraint c1_, Constraint c2_){this.c1_ = c1_; this.c2_ = c2_;}
	  public boolean test(final Array params) {
               return c1_.test(params) && c2_.test(params);
      }
	
 };
