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

package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Date;

/**
 * 
 * @author Richard Gomes
 */
public interface BootstrapTraits {

    /**
     * value at reference
     */ 
    public double initialValue();
    
    /**
     * initial date
     */
    public Date initialDate (final YieldTermStructure curve);

    /**
     * max iterations
     */
    public int maxIterations();

    /**
     * dummy initial value
     */
    public boolean dummyInitialValue ();

    /**
     * initial guess
     */
    public double initialGuess();
    
    /**
     * further guesses
     */
    public double guess(final YieldTermStructure c, final Date d);

    /**
     * possible constraints based on previous values
     */
    public double minValueAfter(int i, final Array data);
    
    /**
     * possible constraints based on maximum values
     */
    public double maxValueAfter(int i, final Array data);
    
    /**
     * update with new guess
     */
    public void updateGuess(final Array data, double value, int i);

    //FIXME, can instruments go somewhere else?
    public YieldTermStructure buildCurve (int instruments, final Date referenceDate, final DayCounter dc, Interpolator interpolator);

    public double getAccuracy ();
    
}