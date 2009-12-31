
/*
Copyright (C) 2009 John Martin

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


package org.jquantlib.termstructures;

import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.time.Date;

public interface Bootstrapable
{
    public abstract Array getData ();
    
    public abstract Date [] getDates ();
    
    public abstract Array getTimes ();
    
    public abstract Interpolator getInterpolator();
    
    public abstract Interpolation getInterpolation();
    
    public abstract void setInterpolation (Interpolation interpolation);
    
    public abstract void resetData (int size);
    
    public abstract void resetTime (int size);
    
    public abstract void resetDates (int size);
}