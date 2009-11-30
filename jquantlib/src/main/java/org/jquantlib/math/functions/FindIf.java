/*
 Copyright (C) 2009 Richard Gomes

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
package org.jquantlib.math.functions;

import org.jquantlib.math.Ops;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Cells.ConstRowIterator;
import org.jquantlib.math.matrixutilities.Cells.RowIterator;

/**
 * This class verifies a condition and if true, returns the evaluation of
 * a function, otherwise returns Double.NaN.
 *
 * @author Richard Gomes
 */
public final class FindIf implements org.jquantlib.lang.iterators.Iterable {

    private final ConstRowIterator iterator;
    private final Ops.DoublePredicate predicate;

    public FindIf(final Array array, final Ops.DoublePredicate predicate) {
        this.iterator = array.constIterator();
        this.predicate = predicate;
    }


    //
    // implements Ops.DoubleOp
    //

    @Override
    public org.jquantlib.lang.iterators.Iterator iterator() {
        // find first element which satisfies predicate and insert it, if found
        final Array array = new Array(iterator.size());
        final RowIterator it = array.iterator();
        while (iterator.hasNext()) {
            final double a = iterator.nextDouble();
            if ( predicate.op(a) ) {
                it.setDouble( iterator.nextDouble() );
                break;
            }
        }
        // copy remaining elements
        while (iterator.hasNext()) {
            it.setDouble( iterator.nextDouble() );
            it.forward();
        }
        it.begin();
        return it;
    }

}


