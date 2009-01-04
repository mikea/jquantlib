/*
 Copyright (C) 2007 Joon Tiang Heng

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

package org.jquantlib.math.optimization;
import java.util.Vector;
import org.jquantlib.math.Array;

/**
 * @author Joon Tiang Heng
 */
 //! Criteria to end optimization process:
    /*! - maximum number of iterations AND minimum number of iterations around stationary point
        - x (independent variable) stationary point
        - y=f(x) (dependent variable) stationary point
        - stationary gradient
    */
public class EndCriteria { 
    public enum CriteriaType {
				   None,
                   MaxIterations,
                   StationaryPoint,
                   StationaryFunctionValue,
                   StationaryFunctionAccuracy,
                   ZeroGradientNorm,
                   Unknown};
      
    //! Maximum number of iterations
    protected Integer maxIterations_;
    //! Maximun number of iterations in stationary state
    protected Integer maxStationaryStateIterations_;//mutable
    //! root, function and gradient epsilons
    protected Double rootEpsilon_, functionEpsilon_, gradientNormEpsilon_;

							 
	//! Initialization constructor						 
	public EndCriteria(Integer maxIterations,
                       Integer maxStationaryStateIterations,
                       Double rootEpsilon,
                       Double functionEpsilon,
                       Double gradientNormEpsilon) {
		maxIterations_ = maxIterations;
		maxStationaryStateIterations_ = maxStationaryStateIterations;
		rootEpsilon_ = rootEpsilon;
		functionEpsilon_ = functionEpsilon;
		gradientNormEpsilon_ = gradientNormEpsilon; 
		

        if (maxStationaryStateIterations_ == null)
            maxStationaryStateIterations_ = Math.min((int)(maxIterations/2),
                                                     (int)(100));
													 
		
        assert maxStationaryStateIterations_ > 1 : "maxStationaryStateIterations_ (" + 
												    maxStationaryStateIterations_ +
												   ") must be greater than one";
        assert maxStationaryStateIterations_ < maxIterations_ : "maxStationaryStateIterations_ (" +
															     maxStationaryStateIterations_ +
																") must be less than maxIterations_ (" +
																 maxIterations_ + ")";
        if (gradientNormEpsilon_ == null)
            gradientNormEpsilon_ = functionEpsilon_;
    }
	/*! Test if the number of iteration is below MaxIterations */
    public boolean checkMaxIterations(int iteration,
                                         CriteriaType ecType) {
        if (iteration < maxIterations_)
            return false;
        ecType = CriteriaType.MaxIterations;
        return true;
    }
	/*! Test if the root variation is below rootEpsilon */
    public boolean checkStationaryPoint(double xOld,
                                           double xNew,
                                           int statStateIterations,
                                           CriteriaType ecType) {
        if (Math.abs(xNew-xOld) >= rootEpsilon_) {
            statStateIterations = 0;
            return false;
        }
        ++statStateIterations;
        if (statStateIterations <= maxStationaryStateIterations_)
            return false;
        ecType = CriteriaType.StationaryPoint;
        return true;
    }
	/*! Test if the function variation is below functionEpsilon */
    public boolean checkStationaryFunctionValue(
                                            double fxOld,
                                            double fxNew,
                                            int statStateIterations,
                                            CriteriaType ecType) {
        if (Math.abs(fxNew-fxOld) >= functionEpsilon_) {
            statStateIterations = 0;
			
            return false;
        }
        ++statStateIterations;
        if (statStateIterations <= maxStationaryStateIterations_)
            return false;
        ecType = CriteriaType.StationaryFunctionValue;
        return true;
    }
	/*! Test if the function value is below functionEpsilon */
    public boolean checkStationaryFunctionAccuracy(
                                            double f,
                                            boolean positiveOptimization,
                                            CriteriaType ecType) {
        if (!positiveOptimization)
            return false;
        if (f >= functionEpsilon_)
            return false;
        ecType = CriteriaType.StationaryFunctionAccuracy;
        return true;
    }

   
	/*! Test if the gradient norm value is below gradientNormEpsilon */
    public boolean checkZeroGradientNorm(double gradientNorm,
                                            CriteriaType ecType) {
        if (gradientNorm >= gradientNormEpsilon_)
            return false;
        ecType = CriteriaType.ZeroGradientNorm;
        return true;
    }
	/*! Test if the number of iterations is not too big 
            and if a minimum point is not reached */
	//no operator method, have to use a method in place 
    public boolean bracket_operator(int iteration,
                                 int statStateIterations,
                                 boolean positiveOptimization,
                                 double fold,
                                 double normgold,
                                 double fnew,
                                 double normgnew,
                                 CriteriaType ecType) {
        return
            checkMaxIterations(iteration, ecType) ||
            checkStationaryFunctionValue(fold, fnew, statStateIterations, ecType) ||
            checkStationaryFunctionAccuracy(fnew, positiveOptimization, ecType) ||
            checkZeroGradientNorm(normgnew, ecType);
    }

    // Inspectors
    public int getMaxIterations() {
        return maxIterations_;
    }

    public int getMaxStationaryStateIterations() {
        return maxStationaryStateIterations_;
    }

    public double getRootEpsilon() {
        return rootEpsilon_;
    }

    public double getFunctionEpsilon() {
        return functionEpsilon_;
    }

    public double getGradientNormEpsilon() {
        return gradientNormEpsilon_;
    }

    public String toString(CriteriaType ec) {
        switch (ec) {
        case None:
            return "None";
        case MaxIterations:
            return "MaxIterations";
        case StationaryPoint:
            return "StationaryPoint";
        case StationaryFunctionValue:
            return "StationaryFunctionValue";
        case StationaryFunctionAccuracy:
            return "StationaryFunctionAccuracy";
        case ZeroGradientNorm:
            return "ZeroGradientNorm";
        case Unknown:
            return "Unknown";
        default:
            throw new RuntimeException("unknown EndCriteria::Type (" + ec + ")");
        }
    }
}
