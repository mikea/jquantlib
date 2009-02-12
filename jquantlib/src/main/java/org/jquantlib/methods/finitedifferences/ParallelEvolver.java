/*
 Copyright (C) 2007 Srinivas Hasti

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

import java.util.List;
import java.util.Vector;

import org.jquantlib.math.Array;
import org.jquantlib.util.reflect.TypeToken;

/**
 * @author Srinivas Hasti
 * 
 */
//TODO: Code review
public class ParallelEvolver<T extends MixedScheme> {
	private Vector<T> evolvers;

	public ParallelEvolver(Vector<? extends Operator> L, BoundaryConditionSet bcs) {
		evolvers = new Vector<T>(L.size());
		for (int i = 0; i < L.size(); i++) {
			evolvers.add(getEvolver(L.get(i), bcs.get(i)));
		}
	}

	void step(Vector<Array> a, double t) {
		for (int i = 0; i < evolvers.size(); i++) {
			evolvers.get(i).step(a.get(i), t);
		}
	}

	void setStep(double dt) {
		for (int i = 0; i < evolvers.size(); i++) {
			evolvers.get(i).setStep(dt);
		}
	}

	protected T getEvolver(Operator l, List<BoundaryCondition> bcs) {
		try {
			return (T) TypeToken.getClazz(this.getClass()).getConstructor(
					l.getClass(), bcs.getClass()).newInstance(l, bcs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
