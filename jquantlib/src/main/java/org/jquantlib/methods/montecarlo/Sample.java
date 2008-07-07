/*
 Copyright (C) 2008 Richard Gomes

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

package org.jquantlib.methods.montecarlo;

/**
 * 
 * @author Dominik Holenstein
 * @author Aaron Roth
 *
 * @param <E>
 */

//TODO: Add JavaDocs
//TODO: Code review

//weighted sample
public class Sample<T> {
    /**
     * Since this class is intended to be immutable, all the relevant fields
     * are final; we allow them to be set only in the constructor and safely
     * forgo getters on them in favor of direct access.
     * 
     * The fields are redundantly marked with public to make these intentions
     * explicit.
     */
    public final T value;
    public final double weight;

    
    public Sample(final T value, double weight) {
    	this.value=value;
    	this.weight=weight;
    }
}
