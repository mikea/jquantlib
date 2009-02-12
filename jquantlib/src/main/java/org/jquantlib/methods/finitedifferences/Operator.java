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
package org.jquantlib.methods.finitedifferences;

import org.jquantlib.math.Array;

/**
 * @author Srinivas Hasti
 * 
 */
// CODE REVIEW: Do we really need this interface. Helps to easily replaces Tridiaginal implementations
public interface Operator {

	int size();

	boolean isTimeDependent();

	void setTime(double t);

	Operator identity(int size);

	Array applyTo(Array a);

	Array solveFor(Array a);

	Array SOR(Array rhs, int tol);

	void swap(Operator from);

	void swap(Operator op1, Operator op2);

	Operator add(Operator d); // for + with current instance

	Operator subtract(Operator d); // for - with current instance

	Operator add(Operator a, Operator b);

	Operator subtract(Operator a, Operator b);

	Operator multiply(double a, final Operator d);

	Operator multiply(Operator d, double a);

	Operator divide(Operator d, double a);
}
