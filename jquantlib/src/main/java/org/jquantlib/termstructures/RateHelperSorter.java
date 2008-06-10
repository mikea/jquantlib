/*
 Copyright (C) 2008 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.termstructures;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This class implements a {@link Comparator} for {@link RateHelper} objects.
 * 
 * @see Comparator
 * @see RateHelper
 * 
 * @author Richard Gomes
 * @param <T>
 */
public class RateHelperSorter<T extends RateHelper<YieldTermStructure>> implements Comparator<T>, Serializable {
	
	private static final long serialVersionUID = 6335152611463577317L;

	public int compare(final T h1, final T h2) /* @ReadOnly */ {
		return h1.getLatestDate().compareTo(h2.getLatestDate());
	}
}