/*
 Copyright (C) 2008 Praneet Tiwari

 This source code is release under the BSD License.

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */
package org.jquantlib.math.statistics;

//~--- JDK imports ------------------------------------------------------------
import java.util.ArrayList;

import org.jquantlib.util.Pair;

/**
 *
 * @author Praneet Tiwari
 */
public class MathUtil {

    public static Double min(ArrayList<Pair<Double, Double>> values) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }

        // simplest algo in the first release
        Double std = 0.0;

        for (Pair<Double, Double> element : values) {
            if (std > (Double) element.getFirst()) {
                std = (Double) element.getFirst();
            }
        }

        return std;
    }

    public static Double max(ArrayList<Pair<Double, Double>> values) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }

        // simplest algo in the first release
        Double std = 0.0;

        for (Pair<Double, Double> element : values) {
            if (std < (Double) element.getFirst()) {
                std = (Double) element.getFirst();
            }
        }

        return std;
    }

    // more generic methods, don't know if they are useful...
    public static <T extends Number> T max(ArrayList<Pair<Number, Number>> values) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }

        // simplest algo in the first release
        Number std = 0.0;

        for (Pair<Number, Number> element : values) {
            if (std.doubleValue() > ((Number) element.getFirst()).doubleValue()) {
                std = (Number) element.getFirst();
            }
        }

        return (T) std;
    }

    public static <T extends Number> T min(ArrayList<Pair<Number, Number>> values) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }

        // simplest algo in the first release
        Number std = 0.0;

        for (Pair<Number, Number> element : values) {
            if (std.doubleValue() < ((Number) element.getFirst()).doubleValue()) {
                std = (Number) element.getFirst();
            }
        }

        return (T) std;
    }
}
 