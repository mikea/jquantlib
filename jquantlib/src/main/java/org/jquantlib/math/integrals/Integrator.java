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

package org.jquantlib.math.integrals;

import org.jquantlib.math.Constants;
import org.jquantlib.math.UnaryFunctionDouble;

/**
 * This is the abstract base class for all integrators
 * 
 * @author Richard Gomes
 */
public abstract class Integrator {

    //
	// private fields
	//
	
	private double absoluteAccuracy;
    private double absoluteError;
    private int maxEvaluations;
    private int numberOfEvaluations;
	
    
	//
	// public constructors
	//
	
	public Integrator(final double absoluteAccuracy, final int maxEvaluations) {
		if (absoluteAccuracy <= Constants.QL_EPSILON)
			throw new IllegalArgumentException("required tolerance(" + absoluteAccuracy + ") must be > "+Constants.QL_EPSILON);

		this.absoluteAccuracy = absoluteAccuracy;
		this.maxEvaluations = maxEvaluations;
	}
	
    //
    // protected abstract methods
    //
    
    protected abstract double integrate(UnaryFunctionDouble f, double a, double b) /* @ReadOnly */;

	
    //
    // protected final methods
    //
    
    protected final void setAbsoluteError(final double error) /* @ReadOnly */ {
        absoluteError = error;
    }

    protected final void setNumberOfEvaluations(final int evaluations) /* @ReadOnly */ {
        this.numberOfEvaluations = evaluations;
    }

    protected final void increaseNumberOfEvaluations(final int increase) /* @ReadOnly */ {
        this.numberOfEvaluations += increase;
    }


    //
    // protected virtual methods
    //
    
    protected int getNumberOfEvaluations() /* @ReadOnly */ {
        return this.numberOfEvaluations;
    }


	//
	// public final methods
	//
	
	public double evaluate(UnaryFunctionDouble f, double a, double b) /* @ReadOnly */{
		if (a == b) return 0.0;
		if (a > b) return -1 * evaluate(f, b, a);
		this.numberOfEvaluations = 0;
		return integrate(f, a, b);
	}
    
    public final void setAbsoluteAccuracy(final double accuracy) {
        absoluteAccuracy= accuracy;
    }

    public final void setMaxEvaluations(final int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    public final double getAbsoluteAccuracy() /* @ReadOnly */ {
        return absoluteAccuracy;
    }

    public final int getMaxEvaluations() /* @ReadOnly */ {
        return this.maxEvaluations;
    }

    public final double getAbsoluteError() /* @ReadOnly */ {
        return absoluteError;
    }

    
    //
    // public virtual methods
    //
    
    public boolean isIntegrationSuccess() /* @ReadOnly */ {
        return numberOfEvaluations <= maxEvaluations && absoluteError <= absoluteAccuracy;
    }

}
