/*
Copyright (C) 2008 Praneet Tiwari

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
package org.jquantlib.model.shortrate;

/**
 * 
 * @author Praneet Tiwari
 */
public abstract class EndCriteria {

    // ! Maximum number of iterations
    protected double /* @Size */maxIterations;
    // ! Maximun number of iterations in stationary state
    protected volatile/* mutable */double /* @Size */maxStationaryStateIterations;
    // ! root, function and gradient epsilons
    protected double /* @Real */rootEpsilon, functionEpsilon, gradientNormEpsilon;

    public enum Type {

        None, MaxIterations, StationaryPoint, StationaryFunctionValue, StationaryFunctionAccuracy, ZeroGradientNorm, Unknown
    }

    // Inspectors
    public abstract double /* @Size */maxIterations();

    public abstract double /* @Size */maxStationaryStateIterations();

    public abstract double /* @Real */rootEpsilon();

    public abstract double /* @Real */functionEpsilon();

    public abstract double /* @Real */gradientNormEpsilon();

    // ! Initialization constructor
    public EndCriteria(double /* @Size */maxIterations, double /* @Size */maxStationaryStateIterations,
            double /* @Real */rootEpsilon, double /* @Real */functionEpsilon, double /* @Real */gradientNormEpsilon) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.maxIterations = maxIterations;
        this.maxStationaryStateIterations = maxStationaryStateIterations;
        this.rootEpsilon = rootEpsilon;
        this.functionEpsilon = functionEpsilon;
        this.gradientNormEpsilon = gradientNormEpsilon;
    }

    /*
     * ! Test if the number of iterations is not too big and if a minimum point is not reached bool operator()(const Size iteration,
     * Size& statState, const bool positiveOptimization, const Real fold, const Real normgold, const Real fnew, const Real normgnew,
     * EndCriteria::Type& ecType) const;
     */
    /* ! Test if the number of iteration is below MaxIterations */
    public abstract boolean checkMaxIterations(double /* @Size */iteration, EndCriteria.Type ecType);

    /* ! Test if the root variation is below rootEpsilon */

    public abstract boolean checkStationaryPoint(double /* @Real */xOld, double /* @Real */xNew,
            Double /* @Real */statStateIterations, EndCriteria.Type ecType);

    /* ! Test if the function variation is below functionEpsilon */

    public abstract boolean checkStationaryFunctionValue(double /* @Real */fxOld, double /* @Real */fxNew,
            Double /* @Real */statStateIterations, EndCriteria.Type ecType);

    /* ! Test if the function value is below functionEpsilon */

    public abstract boolean checkStationaryFunctionAccuracy(double /* @Real */f, boolean positiveOptimization,
            EndCriteria.Type ecType);

    /* ! Test if the gradient norm variation is below gradientNormEpsilon */
    // bool checkZerGradientNormValue(const Real gNormOld,
    // const Real gNormNew,
    // EndCriteria::Type& ecType) const;
    /* ! Test if the gradient norm value is below gradientNormEpsilon */

    public abstract boolean checkZeroGradientNorm(double /* @Real */gNorm, EndCriteria.Type ecType);
}
