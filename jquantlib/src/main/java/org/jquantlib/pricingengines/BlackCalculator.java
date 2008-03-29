/*
 Copyright (C) 2007 Richard Gomes

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

import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.math.Closeness;
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
// FIXME: code review
public class BlackCalculator {

	protected/* @Price */double strike_;
	protected/* @Price */double forward_;
	protected/* @StdDev */double stdDev_;
	protected/* @DiscountFactor */double discount_;
	protected/* @Variance */double variance_;
	protected double D1_, D2_, alpha_, beta_, DalphaDd1_, DbetaDd2_;
	protected double n_d1_, cum_d1_, n_d2_, cum_d2_;
	protected double X_, DXDs_, DXDstrike_;

	public BlackCalculator(final StrikedTypePayoff payoff, final double forward, final double stdDev) {
		this(payoff, forward, stdDev, 1.0);
	}

	public BlackCalculator(final StrikedTypePayoff payoff, final double forward, final double stdDev, final double discount) {

		this.strike_ = payoff.getStrike();
		this.forward_ = forward;
		this.stdDev_ = stdDev;
		this.discount_ = discount;
		this.variance_ = stdDev * stdDev;

		if (forward <= 0.0)
			throw new IllegalArgumentException("positive forward value required: " + forward + " not allowed");
		if (stdDev < 0.0)
			throw new IllegalArgumentException("non-negative standard deviation required: " + stdDev + " not allowed");
		if (discount <= 0.0)
			throw new IllegalArgumentException("positive discount required: " + discount + " not allowed");

		if (stdDev_ >= Closeness.epsilon) {
			if (strike_ == 0.0) {
				n_d1_ = 0.0;
				n_d2_ = 0.0;
				cum_d1_ = 1.0;
				cum_d2_ = 1.0;
			} else {
				D1_ = Math.log(forward / strike_) / stdDev_ + 0.5 * stdDev_;
				D2_ = D1_ - stdDev_;
				CumulativeNormalDistribution f = new CumulativeNormalDistribution();
				cum_d1_ = f(D1_);
				cum_d2_ = f(D2_);
				n_d1_ = f.derivative(D1_);
				n_d2_ = f.derivative(D2_);
			}
		} else {
			if (forward > strike_) {
				cum_d1_ = 1.0;
				cum_d2_ = 1.0;
			} else {
				cum_d1_ = 0.0;
				cum_d2_ = 0.0;
			}
			n_d1_ = 0.0;
			n_d2_ = 0.0;
		}

		X_ = strike_;
		DXDstrike_ = 1.0;

		// the following one will probably disappear as soon as
		// super-share will be properly handled
		DXDs_ = 0.0;

		// this part is always executed.
		// in case of plain-vanilla payoffs, it is also the only part
		// which is executed.
		Option.Type optionType = payoff.getOptionType();
		if (optionType == Option.Type.Call) {
			alpha_ = cum_d1_;// N(d1)
			DalphaDd1_ = n_d1_;// n(d1)
			beta_ = -cum_d2_;// -N(d2)
			DbetaDd2_ = -n_d2_;// -n(d2)
		} else if (optionType == Option.Type.Put) {
			alpha_ = -1.0 + cum_d1_;// -N(-d1)
			DalphaDd1_ = n_d1_;// n( d1)
			beta_ = 1.0 - cum_d2_;// N(-d2)
			DbetaDd2_ = -n_d2_;// -n( d2)
		} else {
			throw new IllegalArgumentException("invalid option type");
		}

		// now dispatch on type.

		Calculator calc = new Calculator(this);
		payoff.accept(calc);
	}

	public/* @Price */double value() /* @ReadOnly */{
		/* @Price */double result = discount_ * (forward_ * alpha_ + X_ * beta_);
		return result;
	}

	/**
	 * Sensitivity to change in the underlying spot price.
	 */
	public/* @Price */double delta(final double spot) /* @ReadOnly */{

		if (spot <= 0.0)
			throw new IllegalArgumentException("positive spot value required: " + spot + " not allowed");

		double DforwardDs = forward_ / spot;

		double temp = stdDev_ * spot;
		double DalphaDs = DalphaDd1_ / temp;
		double DbetaDs = DbetaDd2_ / temp;
		double temp2 = DalphaDs * forward_ + alpha_ * DforwardDs + DbetaDs * X_ + beta_ * DXDs_;

		return discount_ * temp2;
	}

	/**
	 * Sensitivity to change in the underlying forward price.
	 */
	public/* @Price */double deltaForward() /* @ReadOnly */{

		double temp = stdDev_ * forward_;
		double DalphaDforward = DalphaDd1_ / temp;
		double DbetaDforward = DbetaDd2_ / temp;
		double temp2 = DalphaDforward * forward_ + alpha_ + DbetaDforward * X_;
		// DXDforward = 0.0; // commented in the source QuantLib

		return discount_ * temp2;
	}

	/**
	 * Sensitivity in percent to a percent change in the underlying spot price.
	 */
	public double elasticity(final double spot) /* @ReadOnly */{
		double val = value();
		double del = delta(spot);
		if (val > Closeness.epsilon)
			return del / val * spot;
		else if (Math.abs(del) < Closeness.epsilon)
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
		if (val > Closeness.epsilon)
			return del / val * forward_;
		else if (Math.abs(del) < Closeness.epsilon)
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

		double DforwardDs = forward_ / spot;

		double temp = stdDev_ * spot;
		double DalphaDs = DalphaDd1_ / temp;
		double DbetaDs = DbetaDd2_ / temp;

		double D2alphaDs2 = -DalphaDs / spot * (1 + D1_ / stdDev_);
		double D2betaDs2 = -DbetaDs / spot * (1 + D2_ / stdDev_);

		double temp2 = D2alphaDs2 * forward_ + 2.0 * DalphaDs * DforwardDs + D2betaDs2 * X_ + 2.0 * DbetaDs * DXDs_;

		return discount_ * temp2;
	}

	/**
	 * Second order derivative with respect to change in the underlying forward
	 * price.
	 */
	public double gammaForward() /* @ReadOnly */{

		double temp = stdDev_ * forward_;
		double DalphaDforward = DalphaDd1_ / temp;
		double DbetaDforward = DbetaDd2_ / temp;

		double D2alphaDforward2 = -DalphaDforward / forward_ * (1 + D1_ / stdDev_);
		double D2betaDforward2 = -DbetaDforward / forward_ * (1 + D2_ / stdDev_);

		double temp2 = D2alphaDforward2 * forward_ + 2.0 * DalphaDforward + D2betaDforward2 * X_;

		// DXDforward = 0.0; // commented in the source QuantLib

		return discount_ * temp2;
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

		return -(Math.log(discount_) * value() + Math.log(forward_ / spot) * spot * delta(spot) + 0.5 * variance_ * spot * spot * gamma(spot)) / maturity;
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

		double temp = Math.log(strike_ / forward_) / variance_;
		// actually DalphaDsigma / SQRT(T)
		double DalphaDsigma = DalphaDd1_ * (temp + 0.5);
		double DbetaDsigma = DbetaDd2_ * (temp - 0.5);

		double temp2 = DalphaDsigma * forward_ + DbetaDsigma * X_;

		return discount_ * Math.sqrt(maturity) * temp2;

	}

	/**
	 * Sensitivity to discounting rate.
	 */
	public double rho(final/* @Time */double maturity) /* @ReadOnly */{
		if (maturity < 0.0)
			throw new IllegalArgumentException("negative maturity not allowed");

		// actually DalphaDr / T
		double DalphaDr = DalphaDd1_ / stdDev_;
		double DbetaDr = DbetaDd2_ / stdDev_;
		double temp = DalphaDr * forward_ + alpha_ * forward_ + DbetaDr * X_;

		return maturity * (discount_ * temp - value());
	}

	/**
	 * Sensitivity to dividend/growth rate.
	 */
	public double dividendRho(final/* @Time */double maturity) /* @ReadOnly */{
		if (maturity < 0.0)
			throw new IllegalArgumentException("negative maturity not allowed");

		// actually DalphaDq / T
		double DalphaDq = -DalphaDd1_ / stdDev_;
		double DbetaDq = -DbetaDd2_ / stdDev_;

		double temp = DalphaDq * forward_ - alpha_ * forward_ + DbetaDq * X_;

		return maturity * discount_ * temp;
	}

	/**
	 * Probability of being in the money in the bond martingale measure, i.e.
	 * N(d2).
	 * 
	 * <p>
	 * It is a risk-neutral probability, not the real world one.
	 */
	public double itmCashProbability() /* @ReadOnly */{
		return cum_d2_;
	}

	/**
	 * Probability of being in the money in the asset martingale measure, i.e.
	 * N(d1).
	 * 
	 * <p>
	 * It is a risk-neutral probability, not the real world one.
	 */
	public double itmAssetProbability() /* @ReadOnly */{
		return cum_d1_;
	}

	/**
	 * Sensitivity to strike.
	 */
	public double strikeSensitivity() /* @ReadOnly */{

		double temp = stdDev_ * strike_;
		double DalphaDstrike = -DalphaDd1_ / temp;
		double DbetaDstrike = -DbetaDd2_ / temp;

		double temp2 = DalphaDstrike * forward_ + DbetaDstrike * X_ + beta_ * DXDstrike_;

		return discount_ * temp2;
	}

	public double alpha() /* @ReadOnly */{
		return alpha_;
	}

	public double beta() /* @ReadOnly */{
		return beta_;
	}

	
	
	//
	// inner classes
	//
	
	private class Calculator implements Visitor<Payoff>, Visitor<PlainVanillaPayoff>, Visitor<CashOrNothingPayoff>, Visitor<AssetOrNothingPayoff>, Visitor<GapPayoff> {

		private BlackCalculator black_;

		public Calculator(final BlackCalculator black) {
			this.black_ = black;
		}

		public void visit(final Payoff payoff) {
			throw new UnsupportedOperationException("unsupported payoff type: " + payoff.getClass().getName());
		}

		public void visit(final PlainVanillaPayoff payoff) {
			// nothing
		}

		public void visit(final CashOrNothingPayoff payoff) {
			black_.alpha_ = black_.DalphaDd1_ = 0.0;
			black_.X_ = payoff.cashPayoff();
			black_.DXDstrike_ = 0.0;
			Option.Type optionType = payoff.optionType();
			if (optionType == Option.Type.Call) {
				black_.beta_ = black_.cum_d2_;
				black_.DbetaDd2_ = black_.n_d2_;
			} else if (optionType == Option.Type.Put) {
				black_.beta_ = 1.0 - black_.cum_d2_;
				black_.DbetaDd2_ = -black_.n_d2_;
			} else {
				throw new IllegalArgumentException("invalid option type");
			}
		}

		public void visit(final AssetOrNothingPayoff payoff) {
			black_.beta_ = black_.DbetaDd2_ = 0.0;
			Option.Type optionType = payoff.optionType();
			if (optionType == Option.Type.Call) {
				black_.alpha_ = black_.cum_d1_;
				black_.DalphaDd1_ = black_.n_d1_;
			} else if (optionType == Option.Type.Put) {
				black_.alpha_ = 1.0 - black_.cum_d1_;
				black_.DalphaDd1_ = -black_.n_d1_;
			} else {
				throw new IllegalArgumentException("invalid option type");
			}
		}

		public void visit(final GapPayoff payoff) {
			black_.X_ = payoff.secondStrike();
			black_.DXDstrike_ = 0.0;
		}

	}

}
