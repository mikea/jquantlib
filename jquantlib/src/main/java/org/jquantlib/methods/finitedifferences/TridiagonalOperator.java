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

package org.jquantlib.methods.finitedifferences;

/**
 * @author Srinivas Hasti
 */
public class TridiagonalOperator {
	protected final double lowerDiagnol[];
	protected final double diagnol[];
	protected final double upperDiagnol[];

	public TridiagonalOperator(int size) {
		if (size >= 2) {
			this.lowerDiagnol = new double[size - 1];
			this.diagnol = new double[size];
			this.upperDiagnol = new double[size - 1];
		} else if (size == 0) {
			this.lowerDiagnol = new double[0];
			this.diagnol = new double[0];
			this.upperDiagnol = new double[0];
		} else {
			throw new IllegalStateException("Invalid size " + size);
		}

	}

	public TridiagonalOperator(double[] ldiag, double[] diag, double[] udiag) {
		if (ldiag.length != diag.length - 1)
			throw new IllegalStateException("wrong size for lower diagnol");
		if (udiag.length == diag.length - 1)
			throw new IllegalStateException("wrong size for upper diagnol");
		this.lowerDiagnol = ldiag;
		this.diagnol = diag;
		this.upperDiagnol = udiag;
	}
	
	
}
