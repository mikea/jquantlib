/*
 Copyright (C) 2008 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */

package org.jquantlib.math;

/**
 * @author <Richard Gomes>
 */

//FIXME: refactor package "solvers1d"
abstract public class AbstractSolver1D<F extends UnaryFunctionDouble> {
	
	private static final int MAX_FUNCTION_EVALUATIONS = 100;

	private int maxEvaluations_;
	private boolean lowerBoundEnforced_;
	private boolean upperBoundEnforced_;

	protected double root_, xMin_, xMax_, fxMin_, fxMax_;
	protected int evaluationNumber_;
	protected double lowerBound_, upperBound_;

	//
	// public constructors
	//
	
	public AbstractSolver1D() {
		maxEvaluations_ = MAX_FUNCTION_EVALUATIONS;
		lowerBoundEnforced_ = false;
		upperBoundEnforced_ = false;
	}

	public AbstractSolver1D(int maxEvalautions, boolean lowerBoundEnforced,
			boolean upperBoundenforeced) {
		setMaxEvaluations(maxEvalautions);
		lowerBoundEnforced_ = lowerBoundEnforced;
		upperBoundEnforced_ = upperBoundenforeced;
	}

	//
	// protected abstract methods
	//
	
	abstract protected double solveImpl(final F f, double accuracy);

	
	//
	// public methods
	//
	
	/** This method returns the zero of the function <code>f</code>,
	 * determined with the given accuracy <code>epsilon</code>;
	 * depending on the particular solver, this might mean that
	 * the returned <code>x</code> is such that <code>|f(x)| < epsilon f</code>,
	 *  or that <code>f |x-\xi| < \epsilon \f$ where \f$ \xi \f$
	 *  is the real zero.
	 *  <p>
	 *  This method contains a bracketing routine to which an
	 *  initial guess must be supplied as well as a step used to
	 *  scan the range of the possible bracketing values.
	 *  */

	public double solve(final F f, double accuracy, double guess, double step) {

		if (!(accuracy > 0.0)) {
			throw new ArithmeticException("accuracy (" + accuracy + ") must be positive");

		}
		// check whether we really want to use epsilon
		accuracy = Math.max(accuracy, Constants.QL_EPSILON);

		final double growthFactor = 1.6;
		int flipflop = -1;

		root_ = guess;
		fxMax_ = f.evaluate(root_);

		// monotonically crescent bias, as in optionValue(volatility)
		if (fxMax_ == 0.0)
			return root_;
		else if (fxMax_ > 0.0) {
			xMin_ = enforceBounds(root_ - step);
			fxMin_ = f.evaluate(xMin_);
			xMax_ = root_;
		} else {
			xMin_ = root_;
			fxMin_ = fxMax_;
			xMax_ = enforceBounds(root_ + step);
			fxMax_ = f.evaluate(xMax_);
		}

		evaluationNumber_ = 2;
		while (evaluationNumber_ <= maxEvaluations_) {
			if (fxMin_ * fxMax_ <= 0.0) {//FIXME avoid product to check signs
				if (fxMin_ == 0.0)
					return xMin_;
				if (fxMax_ == 0.0)
					return xMax_;
				root_ = (xMax_ + xMin_) / 2.0;
				return solveImpl(f, accuracy);
			}
			if (Math.abs(fxMin_) < Math.abs(fxMax_)) {
				xMin_ = enforceBounds(xMin_ + growthFactor * (xMin_ - xMax_));
				fxMin_ = f.evaluate(xMin_);
			} else if (Math.abs(fxMin_) > Math.abs(fxMax_)) {
				xMax_ = enforceBounds(xMax_ + growthFactor * (xMax_ - xMin_));
				fxMax_ = f.evaluate(xMax_);
			} else if (flipflop == -1) {
				xMin_ = enforceBounds(xMin_ + growthFactor * (xMin_ - xMax_));
				fxMin_ = f.evaluate(xMin_);
				evaluationNumber_++;
				flipflop = 1;
			} else if (flipflop == 1) {
				xMax_ = enforceBounds(xMax_ + growthFactor * (xMax_ - xMin_));
				fxMax_ = f.evaluate(xMax_);
				flipflop = -1;
			}
			evaluationNumber_++;
		}

		//FIXME is it so exceptional, should we return s success/fail flag?
		throw new ArithmeticException("unable to bracket root in "
				+ maxEvaluations_
				+ " function evaluations (last bracket attempt: " + "f["
				+ xMin_ + "," + xMax_ + "] " + "-> [" + fxMin_ + "," + fxMax_
				+ "])");

	}

	//	   /*! This method returns the zero of the function \f$ f \f$,
	//    determined with the given accuracy \f$ \epsilon \f$;
	//    depending on the particular solver, this might mean that
	//    the returned \f$ x \f$ is such that \f$ |f(x)| < \epsilon
	//    \f$, or that \f$ |x-\xi| < \epsilon \f$ where \f$ \xi \f$
	//    is the real zero.
	//
	//    An initial guess must be supplied, as well as two values
	//    \f$ x_\mathrm{min} \f$ and \f$ x_\mathrm{max} \f$ which
	//    must bracket the zero (i.e., either \f$ f(x_\mathrm{min})
	//    \leq 0 \leq f(x_\mathrm{max}) \f$, or \f$
	//    f(x_\mathrm{max}) \leq 0 \leq f(x_\mathrm{min}) \f$ must
	//    be true).
	//*/
	public double solve(final F f, double accuracy, double guess,
			double xMin, double xMax) {

		if (!(accuracy > 0.0)) {
			throw new ArithmeticException("accuracy (" + accuracy + ") must be positive");
		}
		// check whether we really want to use epsilon
		accuracy = Math.max(accuracy, Constants.QL_EPSILON);

		xMin_ = xMin;
		xMax_ = xMax;

		if (!(xMin_ < xMax_)) {
			throw new ArithmeticException("invalid range: xMin_ (" + xMin_ + ") >= xMax_ (" + xMax_ + ")");
		}

		if (!(!lowerBoundEnforced_ || xMin_ >= lowerBound_)) {
			throw new ArithmeticException("xMin_ (" + xMin_	+ ") < enforced low bound (" + lowerBound_ + ")");
		}

		if (!(!upperBoundEnforced_ || xMax_ <= upperBound_)) {
			throw new ArithmeticException("xMax_ (" + xMax_ + ") > enforced hi bound (" + upperBound_ + ")");
		}

		fxMin_ = f.evaluate(xMin_);
		if (fxMin_ == 0.0)
			return xMin_;

		fxMax_ = f.evaluate(xMax_);
		if (fxMax_ == 0.0)
			return xMax_;

		evaluationNumber_ = 2;

		if (!(fxMin_ * fxMax_ < 0.0)) {
			throw new ArithmeticException("root not bracketed: f[" + xMin_ + "," + xMax_ + "] -> [" + fxMin_ + "," + fxMax_ + "]");
		}

		if (!(guess > xMin_)) {
			throw new ArithmeticException("guess (" + guess + ") < xMin_ ("	+ xMin_ + ")");
		}

		if (!(guess < xMax_)) {
			throw new ArithmeticException("guess (" + guess + ") > xMax_ ("	+ xMax_ + ")");
		}

		root_ = guess;

		return solveImpl(f, accuracy);

	}

	public void setMaxEvaluations(int evaluations) {
		maxEvaluations_ = Math.max(1, evaluations);
	}

	public int getMaxEvaluations() {
		return maxEvaluations_;
	}

	public void setLowerBound(double lowerBound) {
		lowerBound_ = lowerBound;
		lowerBoundEnforced_ = true;
	}

	public void setUpperBound(double upperBound) {
		upperBound_ = upperBound;
		upperBoundEnforced_ = true;
	}

	public int getNumEvaluations() {
		return evaluationNumber_;
	}

	
	//
	// private methods
	//
	
	private double enforceBounds(double x) {
		if (lowerBoundEnforced_ && x < lowerBound_)
			return lowerBound_;
		if (upperBoundEnforced_ && x > upperBound_)
			return upperBound_;
		return x;
	}

}
