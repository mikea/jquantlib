package org.jquantlib.examples.utils;

import org.jquantlib.instruments.Option;
import org.jquantlib.methods.montecarlo.Path;
import org.jquantlib.methods.montecarlo.PathPricer;

public class ReplicationPathPricer extends PathPricer<Path> {

	ReplicationPathPricer(final Option.Type type,
			double /* @Price @NonNegative */strike, double /*
															 * @Rate
															 * @NonNegative
															 */r,
			double /* @Time @NonNegative */maturity, double /*
															 * @Volatility
															 * @NonNegative
															 */sigma) {
		type_ = type;
		strike_ = strike;
		r_ = r;
		maturity_ = maturity;
		sigma_ = sigma;

		// XXX: These tests can be substituted by [future] JSR-308 annotation
		// @NonNegative
		assert (strike_ > 0.0);
		assert (r_ > 0.0);
		assert (maturity_ > 0.0);
		assert (sigma_ > 0.0);

	}

	// The value() method encapsulates the pricing code

	private Number operator(Path path) {
		if (System.getProperty("EXPERIMENTAL") == null) {
			throw new UnsupportedOperationException("Work in progress");
		}
		return null;
	}

	private Option.Type type_;
	private/* @Price */double strike_;
	private/* @Rate */double r_;
	private/* @Time */double maturity_;
	private/* @Volatility */double sigma_;

	@Override
	public Double evaluate(Path path) {
		if (System.getProperty("EXPERIMENTAL") == null) {
			throw new UnsupportedOperationException("Work in progress");
		}
		return null;
	}
}

