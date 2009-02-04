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
 Copyright (C) 2007 Cristina Duminuco
 Copyright (C) 2007 Chiara Fornarola
 Copyright (C) 2003, 2004, 2005, 2006 Ferdinando Ametrano
 Copyright (C) 2006 Mark Joshi
 Copyright (C) 2001, 2002, 2003 Sadruddin Rejeb
 Copyright (C) 2006 StatPro Italia srl

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

package org.jquantlib.pricingengines;

import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;
import org.jquantlib.math.distributions.Derivative;
import org.jquantlib.math.solvers1D.NewtonSafe;

/**
 * 
 * Black 1976 formula
 * <p>
 * 
 * @Note: Instead of volatility it uses standard deviation, i.e.
 *        volatility*sqrt(timeToMaturity)
 * 
 * @author Richard Gomes
 * @author Srinivas Hasti
 */
// TODO: substiture UnsupportedOperationException by actual code
// TODO: adjust formulas (LaTeX)
public class BlackFormula {

	/**
	 * Black 1976 formula
	 * <p>
	 * 
	 * @Note instead of volatility it uses standard deviation, i.e.
	 *       volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormula(final Option.Type optionType,
			final double /* @Price */strike, final double /* @Price */forward,
			final double /* @StdDev */stdDev) {

		return blackFormula(optionType, strike, forward, stdDev, 1.0, 0.0);
	}

	/**
	 * Black 1976 formula
	 * <p>
	 * 
	 * @Note: Instead of volatility it uses standard deviation, i.e.
	 *        volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormula(final Option.Type optionType,
			final double /* @Price */strike, final double /* @Price */forward,
			final double /* @StdDev */stdDev,
			final double /* @DiscountFactor */discount) {

		return blackFormula(optionType, strike, forward, stdDev, discount, 0.0);
	}

	private static void checkParameters(double strike, double forward,
			double displacement) {
		if (strike < 0.0)
			throw new IllegalArgumentException("strike (" + strike
					+ ") must be non-negative");
		if (forward <= 0.0)
			throw new IllegalArgumentException("forward (" + forward
					+ ") must be positive");
		if (displacement < 0.0)
			throw new IllegalArgumentException("displacement (" + displacement
					+ ") must be non-negative");
	}

	/**
	 * 
	 * Black 1976 formula
	 * <p>
	 * 
	 * @Note: Instead of volatility it uses standard deviation, i.e.
	 *        volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormula(final Option.Type optionType,
			double /* @Price */strike, double /* @Price */forward,
			final double /* @StdDev */stdDev,
			final double /* @DiscountFactor */discount,
			final double /* @Real */displacement) {

		if (!(strike >= 0.0))
			throw new ArithmeticException("strike (" + strike
					+ ") must be non-negative");
		if (!(forward > 0.0))
			throw new ArithmeticException("forward (" + forward
					+ ") must be positive");
		if (!(stdDev >= 0.0))
			throw new ArithmeticException("stdDev (" + stdDev
					+ ") must be non-negative");
		if (!(discount > 0.0))
			throw new ArithmeticException("positive discount required: "
					+ discount + " not allowed");
		if (!(displacement >= 0.0))
			throw new ArithmeticException("displacement (" + displacement
					+ ") must be non-negative");

		forward = forward + displacement;
		strike = strike + displacement;
		if (stdDev == 0.0)
			return Math
					.max((forward - strike) * optionType.toInteger(), (0.0d))
					* discount;

		if (strike == 0.0) // strike=0 iff displacement=0
			return (optionType == Option.Type.CALL ? forward * discount : 0.0);

		double /* @Real */d1 = Math.log(forward / strike) / stdDev + 0.5
				* stdDev;
		double /* @Real */d2 = d1 - stdDev;
		CumulativeNormalDistribution phi = new CumulativeNormalDistribution();
		double /* @Real */result = discount
				* optionType.toInteger()
				* (forward * phi.evaluate(optionType.toInteger() * d1) - strike
						* phi.evaluate(optionType.toInteger() * d2));

		if (!(result >= 0.0)) {
			throw new ArithmeticException("negative value (" + result
					+ ") for a " + stdDev + " stdDev " + optionType
					+ " option struck at " + strike + " on a " + forward
					+ " forward");

		}
		return result;
	}

	// ---
	// ---
	// ---

	/**
	 * Black 1976 formula
	 * <p>
	 * 
	 * @Note instead of volatility it uses standard deviation, i.e.
	 *       volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormula(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @StdDev */stdDev) {

		return blackFormula(payoff, strike, forward, stdDev, 1.0, 0.0);
	}

	/**
	 * Black 1976 formula
	 * <p>
	 * 
	 * @Note: Instead of volatility it uses standard deviation, i.e.
	 *        volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormula(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @StdDev */stdDev,
			final double /* @DiscountFactor */discount) {

		return blackFormula(payoff, strike, forward, stdDev, discount, 0.0);
	}

	/**
	 * 
	 * Black 1976 formula
	 * <p>
	 * 
	 * @Note: Instead of volatility it uses standard deviation, i.e.
	 *        volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormula(
			final PlainVanillaPayoff payoff, double /* @Price */strike,
			double /* @Price */forward, final double /* @StdDev */stdDev,
			final double /* @DiscountFactor */discount,
			final double /* @Real */displacement) {

		return blackFormula(payoff.getOptionType(), payoff.getStrike(),
				forward, stdDev, discount, displacement);
	}

	// ---
	// ---
	// ---

	/**
	 * Approximated Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity).
	 * <p>
	 * It is calculated using Brenner and Subrahmanyan (1988) and Feinstein
	 * (1988) approximation for at-the-money forward option, with the extended
	 * moneyness approximation by Corrado and Miller (1996)
	 */

	public static double /* @Price */blackFormulaImpliedStdDevApproximation(
			final Option.Type optionType, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice) {

		return blackFormulaImpliedStdDevApproximation(optionType, strike,
				forward, blackPrice, 1.0, 0.0);
	}

	/**
	 * Approximated Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity).
	 * <p>
	 * It is calculated using Brenner and Subrahmanyan (1988) and Feinstein
	 * (1988) approximation for at-the-money forward option, with the extended
	 * moneyness approximation by Corrado and Miller (1996)
	 */

	public static double /* @Price */blackFormulaImpliedStdDevApproximation(
			final Option.Type optionType, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount) {

		return blackFormulaImpliedStdDevApproximation(optionType, strike,
				forward, blackPrice, discount, 0.0);
	}

	/**
	 * Approximated Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity).
	 * <p>
	 * It is calculated using Brenner and Subrahmanyan (1988) and Feinstein
	 * (1988) approximation for at-the-money forward option, with the extended
	 * moneyness approximation by Corrado and Miller (1996)
	 */
	public static double /* @Price */blackFormulaImpliedStdDevApproximation(
			final Option.Type optionType, double /* @Price */strike,
			double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount,
			final double /* @Real */displacement) {

		checkParameters(strike, forward, displacement);
		if (blackPrice < 0.0)
			throw new IllegalArgumentException("blackPrice (" + blackPrice
					+ ") must be non-negative");
		if (discount <= 0)
			throw new IllegalArgumentException("discount (" + discount
					+ ") must be positive");

		double stdDev;
		forward = forward + displacement;
		strike = strike + displacement;
		if (strike == forward)
			// Brenner-Subrahmanyan (1988) and Feinstein (1988) ATM approx.
			stdDev = blackPrice / discount * Math.sqrt(2.0 * Math.PI) / forward;
		else {
			// Corrado and Miller extended moneyness approximation
			double moneynessDelta = optionType.toInteger() * (forward - strike);
			double moneynessDelta_2 = moneynessDelta / 2.0;
			double temp = blackPrice / discount - moneynessDelta_2;
			double moneynessDelta_PI = moneynessDelta * moneynessDelta
					/ Math.PI;
			double temp2 = temp * temp - moneynessDelta_PI;
			if (temp2 < 0.0) // approximation breaks down, 2 alternatives:
				// 1. zero it
				temp2 = 0.0;
			// 2. Manaster-Koehler (1982) efficient Newton-Raphson seed
			// return std::fabs(std::log(forward/strike))*std::sqrt(2.0);
			temp2 = Math.sqrt(temp2);
			temp += temp2;
			temp *= Math.sqrt(2.0 * Math.PI);
			stdDev = temp / (forward + strike);
		}
		if (stdDev < 0.0)
			throw new IllegalStateException("stdDev (" + stdDev
					+ ") must be non-negative");
		return stdDev;
	}

	// ---
	// ---
	// ---

	/**
	 * Approximated Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity).
	 * <p>
	 * It is calculated using Brenner and Subrahmanyan (1988) and Feinstein
	 * (1988) approximation for at-the-money forward option, with the extended
	 * moneyness approximation by Corrado and Miller (1996)
	 */

	public static double /* @Price */blackFormulaImpliedStdDevApproximation(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice) {

		// TODO : complete
		return blackFormulaImpliedStdDevApproximation(payoff, strike, forward,
				blackPrice, 1.0, 0.0);
	}

	/**
	 * Approximated Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity).
	 * <p>
	 * It is calculated using Brenner and Subrahmanyan (1988) and Feinstein
	 * (1988) approximation for at-the-money forward option, with the extended
	 * moneyness approximation by Corrado and Miller (1996)
	 */

	public static double /* @Price */blackFormulaImpliedStdDevApproximation(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount) {

		// TODO : complete
		return blackFormulaImpliedStdDevApproximation(payoff, strike, forward,
				blackPrice, discount, 0.0);
	}

	/**
	 * Approximated Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity).
	 * <p>
	 * It is calculated using Brenner and Subrahmanyan (1988) and Feinstein
	 * (1988) approximation for at-the-money forward option, with the extended
	 * moneyness approximation by Corrado and Miller (1996)
	 */

	public static double /* @Price */blackFormulaImpliedStdDevApproximation(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount,
			final double /* @Real */displacement) {

		return blackFormulaImpliedStdDevApproximation(payoff.getOptionType(),
				payoff.getStrike(), forward, blackPrice, discount, displacement);
	}

	// ---
	// ---
	// ---

	/**
	 * Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaImpliedStdDev(
			final Option.Type optionType, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice) {

		return blackFormulaImpliedStdDev(optionType, strike, forward,
				blackPrice, 1.0, Double.NaN, 1.0e-6, 0.0);

	}

	/**
	 * Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaImpliedStdDev(
			final Option.Type optionType, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount) {

		return blackFormulaImpliedStdDev(optionType, strike, forward,
				blackPrice, discount, Double.NaN, 1.0e-6, 0.0);

	}

	/**
	 * Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaImpliedStdDev(
			final Option.Type optionType, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount,
			final double /* @Price */guess) {

		return blackFormulaImpliedStdDev(optionType, strike, forward,
				blackPrice, discount, guess, 1.0e-6, 0.0);

	}

	/**
	 * Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaImpliedStdDev(
			final Option.Type optionType, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount,
			final double /* @Price */guess, final double /* @Price */accuracy) {

		return blackFormulaImpliedStdDev(optionType, strike, forward,
				blackPrice, discount, guess, accuracy, 0.0);

	}

	/**
	 * Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaImpliedStdDev(
			final Option.Type optionType, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount,
			final double /* @Price */guess, final double /* @Price */accuracy,
			final double /* @Real */displacement) {
		return blackFormulaImpliedStdDev(optionType, strike, forward,
				blackPrice, discount, guess, accuracy, displacement, 1); // TODO:
		// IS
		// 1
		// right
		// maxIterations
	}

	/**
	 * Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaImpliedStdDev(
			final Option.Type optionType, double /* @Price */strike,
			double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount, double /* @Price */guess,
			final double /* @Price */accuracy,
			final double /* @Real */displacement, int maxIterations) {

		checkParameters(strike, forward, displacement);
		if (blackPrice < 0.0)
			throw new IllegalArgumentException("blackPrice (" + blackPrice
					+ ") must be non-negative");
		if (discount <= 0.0)
			throw new IllegalArgumentException("discount (" + discount
					+ ") must be positive");

		strike = strike + displacement;
		forward = forward + displacement;
		if (guess == Double.NaN)// if (guess== Null<Real>) //TODO: IS THIS THE
			// CORRECT WAY
			guess = blackFormulaImpliedStdDevApproximation(optionType, strike,
					forward, blackPrice, discount, displacement);
		else if (guess < 0.0)
			throw new IllegalArgumentException("stdDev guess (" + guess
					+ ") must be non-negative");
		BlackImpliedStdDevHelper f = new BlackImpliedStdDevHelper(optionType,
				strike, forward, blackPrice / discount);
		NewtonSafe solver = new NewtonSafe();
		solver.setMaxEvaluations(maxIterations);
		double minSdtDev = 0.0, maxStdDev = 3.0;
		double stdDev = solver.solve(f, accuracy, guess, minSdtDev, maxStdDev);
		if (stdDev < 0.0)
			throw new IllegalStateException("stdDev (" + stdDev
					+ ") must be non-negative");
		return stdDev;
	}

	// ---
	// ---
	// ---

	/**
	 * Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaImpliedStdDev(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice) {

		return blackFormulaImpliedStdDev(payoff, strike, forward, blackPrice,
				1.0, Double.NaN, 1.0e-6, 0.0);

	}

	/**
	 * Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaImpliedStdDev(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount) {

		return blackFormulaImpliedStdDev(payoff, strike, forward, blackPrice,
				discount, Double.NaN, 1.0e-6, 0.0);

	}

	/**
	 * Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaImpliedStdDev(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount,
			final double /* @Price */guess) {

		return blackFormulaImpliedStdDev(payoff, strike, forward, blackPrice,
				discount, guess, 1.0e-6, 0.0);

	}

	/**
	 * Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaImpliedStdDev(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount,
			final double /* @Price */guess, final double /* @Price */accuracy) {

		return blackFormulaImpliedStdDev(payoff, strike, forward, blackPrice,
				discount, guess, accuracy, 0.0);

	}

	/**
	 * Black 1976 implied standard deviation, i.e.
	 * volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaImpliedStdDev(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount,
			final double /* @Price */guess, final double /* @Price */accuracy,
			final double /* @Real */displacement) {

		// TODO : complete
		throw new UnsupportedOperationException();
	}

	// ---
	// ---
	// ---

	/**
	 * Black 1976 probability of being in the money (in the bond martingale
	 * measure), i.e. N(d2). It is a risk-neutral probability, not the real
	 * world one.
	 * 
	 * @Note Instead of volatility it uses standard deviation, i.e.
	 *       volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaCashItmProbability(
			final Option.Type optionType, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice) {

		return blackFormulaCashItmProbability(optionType, strike, forward,
				blackPrice, 1.0, 0.0);
	}

	/**
	 * Black 1976 probability of being in the money (in the bond martingale
	 * measure), i.e. N(d2). It is a risk-neutral probability, not the real
	 * world one.
	 * 
	 * @Note Instead of volatility it uses standard deviation, i.e.
	 *       volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaCashItmProbability(
			final Option.Type optionType, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount) {

		return blackFormulaCashItmProbability(optionType, strike, forward,
				blackPrice, discount, 0.0);
	}

	/**
	 * Black 1976 probability of being in the money (in the bond martingale
	 * measure), i.e. N(d2). It is a risk-neutral probability, not the real
	 * world one.
	 * 
	 * @Note Instead of volatility it uses standard deviation, i.e.
	 *       volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaCashItmProbability(
			final Option.Type optionType, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount,
			final double /* @Real */displacement) {

		// TODO : complete
		throw new UnsupportedOperationException();
	}

	// ---
	// ---
	// ---

	/**
	 * Black 1976 probability of being in the money (in the bond martingale
	 * measure), i.e. N(d2). It is a risk-neutral probability, not the real
	 * world one.
	 * 
	 * @Note Instead of volatility it uses standard deviation, i.e.
	 *       volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaCashItmProbability(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice) {

		return blackFormulaCashItmProbability(payoff, strike, forward,
				blackPrice, 1.0, 0.0);
	}

	/**
	 * Black 1976 probability of being in the money (in the bond martingale
	 * measure), i.e. N(d2). It is a risk-neutral probability, not the real
	 * world one.
	 * 
	 * @Note Instead of volatility it uses standard deviation, i.e.
	 *       volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaCashItmProbability(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount) {

		return blackFormulaCashItmProbability(payoff, strike, forward,
				blackPrice, discount, 0.0);
	}

	/**
	 * Black 1976 probability of being in the money (in the bond martingale
	 * measure), i.e. N(d2). It is a risk-neutral probability, not the real
	 * world one.
	 * 
	 * @Note Instead of volatility it uses standard deviation, i.e.
	 *       volatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */blackFormulaCashItmProbability(
			final PlainVanillaPayoff payoff, final double /* @Price */strike,
			final double /* @Price */forward, final double /* @Price */blackPrice,
			final double /* @DiscountFactor */discount,
			final double /* @Real */displacement) {

		// TODO : complete
		throw new UnsupportedOperationException();
	}

	// ---
	// ---
	// ---

	/**
	 * Black 1976 formula for standard deviation derivative
	 * <p>
	 * 
	 * @Note Instead of volatility it uses standard deviation, i.e.
	 *       volatilitysqrt(timeToMaturity), and it returns the derivative with
	 *       respect to the standard deviation. If T is the time to maturity
	 *       Black vega would be blackStdDevDerivative(strike, forward,
	 *       stdDev)sqrt(T)
	 */
	public static double /* @Price */blackFormulaStdDevDerivative(
			final double /* @Price */strike, final double /* @Price */forward,
			final double /* @StdDev */stdDev) {

		return blackFormulaStdDevDerivative(strike, forward, stdDev, 1.0, 0.0);
	}

	/**
	 * Black 1976 formula for standard deviation derivative
	 * <p>
	 * 
	 * @Note Instead of volatility it uses standard deviation, i.e.
	 *       volatilitysqrt(timeToMaturity), and it returns the derivative with
	 *       respect to the standard deviation. If T is the time to maturity
	 *       Black vega would be blackStdDevDerivative(strike, forward,
	 *       stdDev)sqrt(T)
	 */
	public static double /* @Price */blackFormulaStdDevDerivative(
			final double /* @Price */strike, final double /* @Price */forward,
			final double /* @StdDev */stdDev,
			final double /* @DiscountFactor */discount) {

		return blackFormulaStdDevDerivative(strike, forward, stdDev, discount,
				0.0);
	}

	/**
	 * Black 1976 formula for standard deviation derivative
	 * <p>
	 * 
	 * @Note Instead of volatility it uses standard deviation, i.e.
	 *       volatilitysqrt(timeToMaturity), and it returns the derivative with
	 *       respect to the standard deviation. If T is the time to maturity
	 *       Black vega would be blackStdDevDerivative(strike, forward,
	 *       stdDev)sqrt(T)
	 */
	public static double /* @Price */blackFormulaStdDevDerivative(
			final double /* @Price */strike, final double /* @Price */forward,
			final double /* @StdDev */stdDev,
			final double /* @DiscountFactor */discount,
			final double /* @Real */displacement) {

		// TODO : complete
		throw new UnsupportedOperationException();
	}

	// ---
	// ---
	// ---

	/**
	 * Black 1976 formula for standard deviation derivative
	 * <p>
	 * 
	 * @Note Instead of volatility it uses standard deviation, i.e.
	 *       volatilitysqrt(timeToMaturity), and it returns the derivative with
	 *       respect to the standard deviation. If T is the time to maturity
	 *       Black vega would be blackStdDevDerivative(strike, forward,
	 *       stdDev)sqrt(T)
	 */
	public static double /* @Price */blackFormulaStdDevDerivative(
			final PlainVanillaPayoff payoff, final double /* @Price */forward,
			final double /* @StdDev */stdDev) {

		return blackFormulaStdDevDerivative(payoff, forward, stdDev, 1.0, 0.0);
	}

	/**
	 * Black 1976 formula for standard deviation derivative
	 * <p>
	 * 
	 * @Note Instead of volatility it uses standard deviation, i.e.
	 *       volatilitysqrt(timeToMaturity), and it returns the derivative with
	 *       respect to the standard deviation. If T is the time to maturity
	 *       Black vega would be blackStdDevDerivative(strike, forward,
	 *       stdDev)sqrt(T)
	 */
	public static double /* @Price */blackFormulaStdDevDerivative(
			final PlainVanillaPayoff payoff, final double /* @Price */forward,
			final double /* @StdDev */stdDev,
			final double /* @DiscountFactor */discount) {

		return blackFormulaStdDevDerivative(payoff, forward, stdDev, discount,
				0.0);
	}

	/**
	 * Black 1976 formula for standard deviation derivative
	 * <p>
	 * 
	 * @Note Instead of volatility it uses standard deviation, i.e.
	 *       volatilitysqrt(timeToMaturity), and it returns the derivative with
	 *       respect to the standard deviation. If T is the time to maturity
	 *       Black vega would be blackStdDevDerivative(strike, forward,
	 *       stdDev)sqrt(T)
	 */
	public static double /* @Price */blackFormulaStdDevDerivative(
			final PlainVanillaPayoff payoff, final double /* @Price */forward,
			final double /* @StdDev */stdDev,
			final double /* @DiscountFactor */discount,
			final double /* @Real */displacement) {

		// TODO : complete
		throw new UnsupportedOperationException();
	}

	// ---
	// ---
	// ---

	/**
	 * Black style formula when forward is normal rather than log-normal. This
	 * is essentially the model of Bachelier.
	 * 
	 * @Note Bachelier model needs absolute volatility, not percentage
	 *       volatility. Standard deviation is
	 *       absoluteVolatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */bachelierBlackFormula(
			final double /* @Price */strike, double /* @Price */forward,
			final double /* @StdDev */stdDev) {
		// TODO : complete
		throw new UnsupportedOperationException();
	}

	/**
	 * Black style formula when forward is normal rather than log-normal. This
	 * is essentially the model of Bachelier.
	 * 
	 * @Note Bachelier model needs absolute volatility, not percentage
	 *       volatility. Standard deviation is
	 *       absoluteVolatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */bachelierBlackFormula(
			final Option.Type optionType, final double /* @Price */strike,
			double /* @Price */forward, final double /* @StdDev */stdDev,
			final double /* @DiscountFactor */discount) {
		return bachelierBlackFormula(optionType, strike, forward, stdDev, 0.0);

	}

	// ---
	// ---
	// ---

	/**
	 * Black style formula when forward is normal rather than log-normal. This
	 * is essentially the model of Bachelier.
	 * 
	 * @Note Bachelier model needs absolute volatility, not percentage
	 *       volatility. Standard deviation is
	 *       absoluteVolatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */bachelierBlackFormula(
			final PlainVanillaPayoff payoff, double /* @Price */forward,
			final double /* @StdDev */stdDev) {

		return bachelierBlackFormula(payoff, forward, stdDev, 0.0);
	}

	/**
	 * Black style formula when forward is normal rather than log-normal. This
	 * is essentially the model of Bachelier.
	 * 
	 * @Note Bachelier model needs absolute volatility, not percentage
	 *       volatility. Standard deviation is
	 *       absoluteVolatility*sqrt(timeToMaturity)
	 */
	public static double /* @Price */bachelierBlackFormula(
			final PlainVanillaPayoff payoff, double /* @Price */forward,
			final double /* @StdDev */stdDev,
			final double /* @DiscountFactor */discount) {
          throw new UnsupportedOperationException("Needs to be completed");
	}

	static class BlackImpliedStdDevHelper implements Derivative {

		public BlackImpliedStdDevHelper(Option.Type optionType, double strike,
				double forward, double undiscountedBlackPrice) {
			this(optionType, strike, forward, undiscountedBlackPrice, 0.0d);

		}

		public BlackImpliedStdDevHelper(Option.Type optionType, double strike,
				double forward, double undiscountedBlackPrice,
				double displacement) {
			this.halfOptionType_ = (0.5 * optionType.toInteger());
			this.signedStrike_ = (optionType.toInteger() * (strike + displacement));
			this.signedForward_ = (optionType.toInteger() * (forward + displacement));
			this.undiscountedBlackPrice_ = (undiscountedBlackPrice);
			checkParameters(strike, forward, displacement);
			if (undiscountedBlackPrice < 0.0)
				throw new IllegalArgumentException("undiscounted Black price ("
						+ undiscountedBlackPrice + ") must be non-negative");
			signedMoneyness_ = optionType.toInteger()
					* Math.log((forward + displacement)
							/ (strike + displacement));
		}

		public double evaluate(double stdDev) {
			// TODO: handle these checks
			// #if defined(QL_EXTRA_SAFETY_CHECKS)
			// QL_REQUIRE(stdDev>=0.0,
			// "stdDev (" << stdDev << ") must be non-negative");
			// #endif
			if (stdDev == 0.0)
				return Math.max(signedForward_ - signedStrike_, 0.0d)
						- undiscountedBlackPrice_;
			double temp = halfOptionType_ * stdDev;
			double d = signedMoneyness_ / stdDev;
			double signedD1 = d + temp;
			double signedD2 = d - temp;
			double result = signedForward_ * N_.evaluate(signedD1)
					- signedStrike_ * N_.evaluate(signedD2);
			// numerical inaccuracies can yield a negative answer
			return Math.max(0.0d, result) - undiscountedBlackPrice_;
		}

		public double derivative(double stdDev) {
			// TODO: handle these checks
			// #if defined(QL_EXTRA_SAFETY_CHECKS)
			// QL_REQUIRE(stdDev>=0.0,
			// "stdDev (" << stdDev << ") must be non-negative");
			// #endif
			double signedD1 = signedMoneyness_ / stdDev + halfOptionType_
					* stdDev;
			return signedForward_ * N_.derivative(signedD1);
		}

		private double halfOptionType_;
		private double signedStrike_, signedForward_;
		private double undiscountedBlackPrice_, signedMoneyness_;
		private CumulativeNormalDistribution N_;
	};

}
