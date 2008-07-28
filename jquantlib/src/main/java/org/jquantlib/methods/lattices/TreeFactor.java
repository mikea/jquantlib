/*
 Copyright (C) 2008 Srinivas Hasti

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
package org.jquantlib.methods.lattices;

/**
 * @author Srinivas Hasti
 * 
 */
public interface TreeFactor {
    /* Real */double underlying(int i, int index);

    int size(int i);

    int descendant(int i, int index, int branch);

    /* Real */ double probability(int i, int index, int branch);

    public static enum Branches {
        BINOMIAL(2), TRINOMIAL(3);

        private int value;

        private Branches(int i) {
            this.value = i;
        }

        public int getValue() {
            return value;
        }
    }
}
