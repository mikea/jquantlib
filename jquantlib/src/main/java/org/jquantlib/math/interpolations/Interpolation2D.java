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

/*
 Copyright (C) 2002, 2003, 2006 Ferdinando Ametrano
 Copyright (C) 2004, 2005, 2006, 2007 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
*/

package org.jquantlib.math.interpolations;

import org.jquantlib.math.Array;
import org.jquantlib.math.BinaryFunctionDouble;
import org.jquantlib.math.Matrix;

/**
 * Interface for 2-D interpolations.
 * <p>
 * Classes which implement this interface will provide interpolated values from two sequences 
 * of length {@latex$ N } and {@latex$ M }, representing the discretized values of 
 * the {@latex$ x }and {@latex$ y } variables, and a {@latex$ N \times M } matrix representing the
 * tabulated function values.
 * 
 * @author Richard Gomes
 */
public interface Interpolation2D extends Extrapolator, BinaryFunctionDouble {
	
    /**
     * This method performs the interpolation itself.
     * 
     * @note This method is deprecated as it causes confusion with
     * Observer.update. Concrete implementations must use {@link Interpolation2D#reload()} instead.
     * 
     * @see reload
     * 
     * @deprecated
     */
	public void update();
	
    /**
     * This method performs the interpolation itself and should be called
     * just after the construction of a interpolation class.
     * 
     * @see update
     */
	public void reload();

    public double xMin();
    public double xMax();
    public double yMin();
    public double yMax();
    public Array xValues();
    public Array yValues();
    public Matrix zData();
    public int locateX(double x);
    public int locateY(double y);
    public boolean isInRange(double x, double y);
}
