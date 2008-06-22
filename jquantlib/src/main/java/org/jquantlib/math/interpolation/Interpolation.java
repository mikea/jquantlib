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
 Copyright (C) 2002, 2003 Ferdinando Ametrano
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006 StatPro Italia srl

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

package org.jquantlib.math.interpolation;

import org.jquantlib.math.UnaryFunctionDouble;



/**
 * 
 * @author Richard Gomes
 */
// FIXME: comments
public interface Interpolation extends Extrapolator, UnaryFunctionDouble {

	/**
	 * This method performs the interpolation itself.
	 * 
	 * @note This method is deprecated as it causes confusion with
	 * Observer.update. Concrete implementations must use Interpolation#reload insted.
	 * 
	 * @deprecated
	 */
	// TODO: change this method name in order to avoid confusion with Observer.update 
	public void update();
	
	/**
	 * This method performs the interpolation itself and should be called
	 * just after the construction of a interpolation class.
	 */
	public void reload();
	
    public double getMinX();
    public double getMaxX();
    public double[] getValuesX();
    public double[] getValuesY();
    public boolean isInRange(double x);
    public double primitive(double x);
    public double derivative(double x);
    public double secondDerivative(double x);

    public double evaluate(double x, boolean b);
    public double primitive(double x, boolean b);
    public double derivative(double x, boolean b);
    public double secondDerivative(double x, boolean b);
    
}
