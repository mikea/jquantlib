package org.jquantlib.cashflow;

import java.util.ArrayList;

import org.jquantlib.math.Array;
import org.jquantlib.math.Constants;

public class Detail {

        // TODO... do we really need this one... its a one to one translation from
        // quantlib... instead we could use the implementations below...
        public static <T extends Number, U extends Number> T get(ArrayList<T> v, int i, U defaultValue) {
            if (v == null || v.size() == 0) {
                return (T) defaultValue;
            } else if (i < v.size()) {
                return v.get(i);
            } else {
                return v.get(v.size() - 1);
            }
        }
        
        public static double get(Array v, int i, double defaultValue){
            if (v == null || v.empty()) {
                return defaultValue;
            } else if (i < v.length) {
                return v.get(i);
            } else {
                return v.get(v.length - 1);
            }
        }
        
        public static double get(double[] v, int i, double defaultValue){
            if (v == null || v.length == 0) {
                return defaultValue;
            } else if (i < v.length) {
                return v[i];
            } else {
                return v[v.length - 1];
            }
        }

        public static/* Rate */double effectiveFixedRate(final Array spreads, final Array caps, final Array floors,
        /* Size */int i) {
            /* Rate */double result = get(spreads, i, Constants.NULL_REAL);
            /* Rate */double floor = get(floors, i, Constants.NULL_REAL);
            if (floor != Constants.NULL_REAL) {
                result = Math.max(floor, result);
            }
            /* Rate */double cap = get(caps, i, Constants.NULL_REAL);
            if (cap != Constants.NULL_REAL) {
                result = Math.min(cap, result);
            }
            return result;
        }

        public static boolean noOption(final Array caps, final Array floors,
        /* Size */int i) {
            return (get(caps, i, Constants.NULL_REAL) == Constants.NULL_REAL)
                    && (get(floors, i, /* Null<Rate>()) == Null<Rate>() */Constants.NULL_REAL) ==Constants.NULL_REAL);
        }

}
