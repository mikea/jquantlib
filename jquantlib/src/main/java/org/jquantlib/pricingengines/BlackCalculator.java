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

/*
 Copyright (C) 2003, 2004, 2005, 2006 Ferdinando Ametrano
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

import org.jquantlib.instruments.AssetOrNothingPayoff;
import org.jquantlib.instruments.CashOrNothingPayoff;
import org.jquantlib.instruments.GapPayoff;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.math.Constants;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 * Black 1976 calculator class
 * 
 * @note <b>bug:</b> When the variance is null, division by zero occur during
 *       the calculation of delta, delta forward, gamma, gamma forward, rho,
 *       dividend rho, vega, and strike sensitivity.
 * 
 * @author Richard Gomes
 */
// FIXME When the variance is null, division by zero occur during...
public class BlackCalculator {

	//
	// FIXME: code review: can these variables be 'private' ?
	//
	protected/* @Price */double strike;
	protected/* @Price */double forward;
	protected/* @StdDev */double stdDev;
	protected/* @DiscountFactor */double discount;
	protected/* @Variance */double variance;
	protected double D1, D2, alpha, beta, dAlpha_dD1, dBeta_dD2;
	protected double n_d1, cum_d1, n_d2, cum_d2;
	protected double X, DXDs, DXDstrike;

	public BlackCalculator(final StrikedTypePayoff payoff, final double forward, final double stdDev) {
		this(payoff, forward, stdDev, 1.0);
	}

	public BlackCalculator(final StrikedTypePayoff payoff, final double forward, final double stdDev, final double discount) {

		this.strike = payoff.getStrike();
		this.forward = forward;
		this.stdDev = stdDev;
		this.discount = discount;
		this.variance = stdDev * stdDev;

		if (forward <= 0.0)
			throw new IllegalArgumentException("positive forward value required: " + forward + " not allowed");
		if (stdDev < 0.0)
			throw new IllegalArgumentException("non-negative standard deviation required: " + stdDev + " not allowed");
		if (discount <= 0.0)
			throw new IllegalArgumentException("positive discount required: " + discount + " not allowed");

		if (stdDev >= Constants.QL_EPSILON) {
			if (strike == 0.0) {
				n_d1 = 0.0;
				n_d2 = 0.0;
				cum_d1 = 1.0;
				cum_d2 = 1.0;
			} else {
				D1 = Math.log(forward / strike) / stdDev + 0.5 * stdDev;
				D2 = D1 - stdDev;
				CumulativeNormalDistribution f = new CumulativeNormalDistribution();
				cum_d1 = f.evaluate(D1);
				cum_d2 = f.evaluate(D2);
				n_d1 = f.derivative(D1);
				n_d2 = f.derivative(D2);
			}
		} else {
			if (forward > strike) {
				cum_d1 = 1.0;
				cum_d2 = 1.0;
			} else {
				cum_d1 = 0.0;
				cum_d2 = 0.0;
			}
			n_d1 = 0.0;
			n_d2 = 0.0;
		}

		X = strike;
		DXDstrike = 1.0;

		// the following one will probably disappear as soon as
		// super-share will be properly handled
		DXDs = 0.0;

		// this part is always executed.
		// in case of plain-vanilla payoffs, it is also the only part
		// which is executed.
		Option.Type optionType = payoff.getOptionType();
		if (optionType == Option.Type.CALL) {
			alpha = cum_d1;// N(d1)
			dAlpha_dD1 = n_d1;// n(d1)
			beta = -cum_d2;// -N(d2)
			dBeta_dD2 = -n_d2;// -n(d2)
		} else if (optionType == Option.Type.PUT) {
			alpha = -1.0 + cum_d1;// -N(-d1)
			dAlpha_dD1 = n_d1;// n( d1)
			beta = 1.0 - cum_d2;// N(-d2)
			dBeta_dD2 = -n_d2;// -n( d2)
		} else {
			throw new IllegalArgumentException("invalid option type");
		}

		// now dispatch on type.

		Calculator calc = new Calculator(this);
		payoff.accept(calc);
	}

	public/* @Price */double value() /* @ReadOnly */{
		/* @Price */double result = discount * (forward * alpha + X * beta);
		return result;
	}

	/**
	 * Sensitivity to change in the underlying spot price.
	 */
	public/* @Price */double delta(final double spot) /* @ReadOnly */{

		if (spot <= 0.0)
			throw new IllegalArgumentException("positive spot value required: " + spot + " not allowed");

		double DforwardDs = forward / spot;

		double temp = stdDev * spot;
		double DalphaDs = dAlpha_dD1 / temp;
		double DbetaDs = dBeta_dD2 / temp;
		double temp2 = DalphaDs * forward + alpha * DforwardDs + DbetaDs * X + beta * DXDs;

		return discount * temp2;
	}

	/**
	 * Sensitivity to change in the underlying forward price.
	 */
	public/* @Price */double deltaForward() /* @ReadOnly */{

		double temp = stdDev * forward;
		double DalphaDforward = dAlpha_dD1 / temp;
		double DbetaDforward = dBeta_dD2 / temp;
		double temp2 = DalphaDforward * forward + alpha + DbetaDforward * X;
		// DXDforward = 0.0; // commented in the source QuantLib

		return discount * temp2;
	}

	/**
	 * Sensitivity in percent to a percent change in the underlying spot price.
	 */
	public double elasticity(final double spot) /* @ReadOnly */{
		double val = value();
		double del = delta(spot);
		if (val > Constants.QL_EPSILON)
			return del / val * spot;
		else if (Math.abs(del) < Constants.QL_EPSILON)
			return 0.0;
		else if (del > 0.0)
			return Double.MAX_VALUE;
		else
			return Double.MIN_VALUE;
	}

	/**
	 * Sensitivity in percent to a percent change in the underlying forward
	 * price.
	 */
	public double elasticityForward() /* @ReadOnly */{
		double val = value();
		double del = deltaForward();
		if (val > Constants.QL_EPSILON)
			return del / val * forward;
		else if (Math.abs(del) < Constants.QL_EPSILON)
			return 0.0;
		else if (del > 0.0)
			return Double.MAX_VALUE;
		else
			return Double.MIN_VALUE;
	}

	/**
	 * Second order derivative with respect to change in the underlying spot
	 * price.
	 */
	public double gamma(final double spot) /* @ReadOnly */{

		if (spot <= 0.0)
			throw new IllegalArgumentException("positive spot value required: " + spot + " not allowed");

		double DforwardDs = forward / spot;

		double temp = stdDev * spot;
		double DalphaDs = dAlpha_dD1 / temp;
		double DbetaDs = dBeta_dD2 / temp;

		double D2alphaDs2 = -DalphaDs / spot * (1 + D1 / stdDev);
		double D2betaDs2 = -DbetaDs / spot * (1 + D2 / stdDev);

		double temp2 = D2alphaDs2 * forward + 2.0 * DalphaDs * DforwardDs + D2betaDs2 * X + 2.0 * DbetaDs * DXDs;

		return discount * temp2;
	}

	/**
	 * Second order derivative with respect to change in the underlying forward
	 * price.
	 */
	public double gammaForward() /* @ReadOnly */{

		double temp = stdDev * forward;
		double DalphaDforward = dAlpha_dD1 / temp;
		double DbetaDforward = dBeta_dD2 / temp;

		double D2alphaDforward2 = -DalphaDforward / forward * (1 + D1 / stdDev);
		double D2betaDforward2 = -DbetaDforward / forward * (1 + D2 / stdDev);

		double temp2 = D2alphaDforward2 * forward + 2.0 * DalphaDforward + D2betaDforward2 * X;

		// DXDforward = 0.0; // commented in the source QuantLib

		return discount * temp2;
	}

	/**
	 * Sensitivity to time to maturity.
	 */
	public double theta(final double spot, final/* @Time */double maturity) /* @ReadOnly */{

		if (maturity == 0.0)
			return 0.0;
		if (maturity <= 0.0)
			throw new IllegalArgumentException("non negative maturity required: " + maturity + " not allowed");

		// =====================================================================
		//
		// *** The following code is commented in the source QuantLib ***
		//
		// vol = stdDev_ / std::sqrt(maturity);
		// rate = -std::log(discount_)/maturity;
		// dividendRate = -std::log(forward_ / spot * discount_)/maturity;
		// return rate*value() - (rate-dividendRate)*spot*delta(spot)
		// - 0.5*vol*vol*spot*spot*gamma(spot);
		// =====================================================================

		return -(Math.log(discount) * value() + Math.log(forward / spot) * spot * delta(spot) + 0.5 * variance * spot * spot * gamma(spot)) / maturity;
	}

	/**
	 * Sensitivity to time to maturity per day, assuming 365 day per year.
	 */
	public double thetaPerDay(final double spot, /* @Time */double maturity) /* @ReadOnly */{
		return theta(spot, maturity) / 365.0;
	}

	/**
	 * Sensitivity to volatility.
	 */
	public double vega(final/* @Time */double maturity) /* @ReadOnly */{
		if (maturity < 0.0)
			throw new IllegalArgumentException("negative maturity not allowed");

		double temp = Math.log(strike / forward) / variance;
		// actually DalphaDsigma / SQRT(T)
		double DalphaDsigma = dAlpha_dD1 * (temp + 0.5);
		double DbetaDsigma = dBeta_dD2 * (temp - 0.5);

		double temp2 = DalphaDsigma * forward + DbetaDsigma * X;

		return discount * Math.sqrt(maturity) * temp2;

	}

	/**
	 * Sensitivity to discounting rate.
	 */
	public double rho(final/* @Time */double maturity) /* @ReadOnly */{
		if (maturity < 0.0)
			throw new IllegalArgumentException("negative maturity not allowed");

		// actually DalphaDr / T
		double DalphaDr = dAlpha_dD1 / stdDev;
		double DbetaDr = dBeta_dD2 / stdDev;
		double temp = DalphaDr * forward + alpha * forward + DbetaDr * X;

		return maturity * (discount * temp - value());
	}

	/**
	 * Sensitivity to dividend/growth rate.
	 */
	public double dividendRho(final/* @Time */double maturity) /* @ReadOnly */{
		if (maturity < 0.0)
			throw new IllegalArgumentException("negative maturity not allowed");

		// actually DalphaDq / T
		double DalphaDq = -dAlpha_dD1 / stdDev;
		double DbetaDq = -dBeta_dD2 / stdDev;

		double temp = DalphaDq * forward - alpha * forward + DbetaDq * X;

		return maturity * discount * temp;
	}

	/**
	 * Probability of being in the money in the bond martingale measure, i.e.
	 * N(d2).
	 * 
	 * <p>
	 * It is a risk-neutral probability, not the real world one.
	 */
	public double itmCashProbability() /* @ReadOnly */{
		return cum_d2;
	}

	/**
	 * Probability of being in the money in the asset martingale measure, i.e.
	 * N(d1).
	 * 
	 * <p>
	 * It is a risk-neutral probability, not the real world one.
	 */
	public double itmAssetProbability() /* @ReadOnly */{
		return cum_d1;
	}

	/**
	 * Sensitivity to strike.
	 */
	public double strikeSensitivity() /* @ReadOnly */{

		double temp = stdDev * strike;
		double DalphaDstrike = -dAlpha_dD1 / temp;
		double DbetaDstrike = -dBeta_dD2 / temp;

		double temp2 = DalphaDstrike * forward + DbetaDstrike * X + beta * DXDstrike;

		return discount * temp2;
	}

	public double alpha() /* @ReadOnly */{
		return alpha;
	}

	public double beta() /* @ReadOnly */{
		return beta;
	}

	
	
	//
	// inner classes
	//
	
	private class Calculator implements TypedVisitor<Payoff> {

		private static final String INVALID_OPTION_TYPE = "invalid option type";
		private static final String INVALID_PAYOFF_TYPE = "invalid payoff type";
		
		private BlackCalculator black;

		public Calculator(final BlackCalculator black) {
			this.black = black;
		}

		//
		// implements TypedVisitor
		//
		
		
		@Override
		public Visitor<Payoff> getVisitor(Class<? extends Payoff> klass) {
			if (klass==PlainVanillaPayoff.class) {
				return plainVanillaPayoffVisitor;
			} else if (klass==CashOrNothingPayoff.class) {
				return cashOrNothingPayoffVisitor;
			} else if (klass==AssetOrNothingPayoff.class) {
				return assetOrNothingPayoffVisitor;
			} else if (klass==GapPayoff.class) {
				return gapPayoffVisitor;
			} else {
				throw new UnsupportedOperationException(INVALID_PAYOFF_TYPE + klass);
			}
		}
		
		//
		// composition pattern to an inner Visitor<Number>
		//
		
		private PlainVanillaPayoffVisitor plainVanillaPayoffVisitor = new PlainVanillaPayoffVisitor();
		
		private class PlainVanillaPayoffVisitor implements Visitor<Payoff> {
			@Override
			public void visit(Payoff o) {
				// nothing
			}
		}


		private CashOrNothingPayoffVisitor cashOrNothingPayoffVisitor = new CashOrNothingPayoffVisitor();
		
		private class CashOrNothingPayoffVisitor implements Visitor<Payoff> {
			@Override
			public void visit(Payoff o) {
				CashOrNothingPayoff payoff = (CashOrNothingPayoff)o;
				black.alpha = black.dAlpha_dD1 = 0.0;
				black.X = payoff.getCashPayoff();
				black.DXDstrike = 0.0;
				Option.Type optionType = payoff.getOptionType();
				if (optionType == Option.Type.CALL) {
					black.beta = black.cum_d2;
					black.dBeta_dD2 = black.n_d2;
				} else if (optionType == Option.Type.PUT) {
					black.beta = 1.0 - black.cum_d2;
					black.dBeta_dD2 = -black.n_d2;
				} else {
					throw new IllegalArgumentException(INVALID_OPTION_TYPE);
				}
			}
		}


		private AssetOrNothingPayoffVisitor assetOrNothingPayoffVisitor = new AssetOrNothingPayoffVisitor();
		
		private class AssetOrNothingPayoffVisitor implements Visitor<Payoff> {
			@Override
			public void visit(Payoff o) {
				AssetOrNothingPayoff payoff = (AssetOrNothingPayoff)o;
				black.beta = black.dBeta_dD2 = 0.0;
				Option.Type optionType = payoff.getOptionType();
				if (optionType == Option.Type.CALL) {
					black.alpha = black.cum_d1;
					black.dAlpha_dD1 = black.n_d1;
				} else if (optionType == Option.Type.PUT) {
					black.alpha = 1.0 - black.cum_d1;
					black.dAlpha_dD1 = -black.n_d1;
				} else {
					throw new IllegalArgumentException(INVALID_OPTION_TYPE);
				}
			}
		}


		private GapPayoffVisitor gapPayoffVisitor = new GapPayoffVisitor();
		
		private class GapPayoffVisitor implements Visitor<Payoff> {
			@Override
			public void visit(Payoff o) {
				GapPayoff payoff = (GapPayoff)o;
				black.X = payoff.getSecondStrike();
				black.DXDstrike = 0.0;
			}
		}

	}

}
