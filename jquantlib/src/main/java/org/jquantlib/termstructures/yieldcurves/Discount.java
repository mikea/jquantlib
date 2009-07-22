package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.math.Array;
import org.jquantlib.math.Constants;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Date;

/**
 * Discount-curve traits
 * 
 * @author Richard Gomes
 *
 * @param <I> interpolated curve type
 */
public class Discount<I extends Interpolator> implements CurveTraits {

    @Override
    public double initialValue() {
        return 1.0;
    }

    @Override
    public double initialGuess() {
        return 0.9;
    }

    @Override
    public double guess(YieldTermStructure c, Date d) {
        return c.discount(d,true);
    }

    @Override
    public double minValueAfter(int i, Array data) {
        return Constants.QL_EPSILON;
    }

    @Override
    //TODO: solve macros
    public double maxValueAfter(int i, Array data) {
//      #if defined(QL_NEGATIVE_RATES)
      // discount are not required to be decreasing--all bets are off.
      // We choose as max a value very unlikely to be exceeded.
      return 3.0;
//      #else
//      // discounts cannot increase
//      return data[i-1];
//      #endif
    }

    @Override
    public void updateGuess(Array data, double value, int i) {
        data.set(i, value);
    }

}
