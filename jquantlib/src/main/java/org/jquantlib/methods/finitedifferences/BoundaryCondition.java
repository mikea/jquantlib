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

/**
 * @author Srinivas Hasti
 * 
 */
public interface BoundaryCondition<T, S> {
	
	public static enum Side {
		NONE, UPPER, LOWER;
	}

	/*
	 * ! This method modifies an operator \f$ L \f$ before it is applied to an
	 * array \f$ u \f$ so that \f$ v = Lu \f$ will satisfy the given condition.
	 */
	public void applyBeforeApplying(T operatorType);

	/*
	 * ! This method modifies an array \f$ u \f$ so that it satisfies the given
	 * condition.
	 */
	public void applyAfterApplying(S arrayType);

	/*
	 * ! This method modifies an operator \f$ L \f$ before the linear system \f$
	 * Lu' = u \f$ is solved so that \f$ u' \f$ will satisfy the given
	 * condition.
	 */
	public void applyBeforeSolving(T operatorType, S rhs);

	/*
	 * ! This method modifies an array \f$ u \f$ so that it satisfies the given
	 * condition.
	 */
	public void applyAfterSolving(S arrayType);

	/*
	 * ! This method sets the current time for time-dependent boundary
	 * conditions.
	 */
	public void setTime(/*@Time*/double t);
}
