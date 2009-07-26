package org.jquantlib.cashflow;

import java.util.ArrayList;

import org.jquantlib.math.Array;

public class Detail {

        // TODO ... move this somewhere else
        public static <T extends Number, U extends Number> T get(ArrayList<T> v, int i, U defaultValue) {
            if (v == null || v.size() == 0) {
                return (T) defaultValue;
            } else if (i < v.size()) {
                return v.get(i);
            } else {
                return v.get(v.size() - 1);
            }
        }

        public static/* Rate */double effectiveFixedRate(final Array spreads, final Array caps, final Array floors,
        /* Size */int i) {
            /* Rate */double result = get(spreads.toDoubleList(), i, new Double(0.0));
            /* Rate */double floor = get(floors.toDoubleList(), i, new Double(0.0));
            if (floor != 0.0) {
                result = Math.max(floor, result);
            }
            /* Rate */double cap = get(caps.toDoubleList(), i, new Double(0.0));
            if (cap != 0.0) {
                result = Math.min(cap, result);
            }
            return result;
        }

        public static boolean noOption(final Array caps, final Array floors,
        /* Size */int i) {
            return (get(caps.toDoubleList(), i, new Double(0.0)) == 0.0)
                    && (get(floors.toDoubleList(), i, /* Null<Rate>()) == Null<Rate>() */new Double(0.0)) == 0.0);
        }

}
