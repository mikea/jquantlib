/*
 Copyright (C) 2007 Srinivas Hasti

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
package org.jquantlib.termstructures.yield;

// FIXME: move to org.jquantlib.termstructures.yieldcurves

import org.jquantlib.Configuration;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.termstructures.TermStructure;
import org.jquantlib.util.Date;
import org.jquantlib.util.Observable;

/**
 * Rate helper with date schedule relative to the global evaluation date
 * 
 * <p>
 * This class takes care of rebuilding the date schedule when the global
 * evaluation date changes
 * 
 * @author Srinivas Hasti
 */ 
public abstract class RelativeDateRateHelper<T extends TermStructure> extends RateHelper<T> {

	protected Date evaluationDate;

	public RelativeDateRateHelper(double d) {
		super(d);
		Configuration.getSystemConfiguration(null).getGlobalSettings()
				.getEvaluationDate().addObserver(this);
		evaluationDate = Configuration.getSystemConfiguration(null)
				.getGlobalSettings().getEvaluationDate();
	}
	
	protected RelativeDateRateHelper(){
		super();
	}

	public RelativeDateRateHelper(Handle<Quote> quote, T termStructure,
			Date earliestDate, Date latestDate) {
		super(quote, termStructure, earliestDate, latestDate);
		Configuration.getSystemConfiguration(null).getGlobalSettings()
				.getEvaluationDate().addObserver(this);
		evaluationDate = Configuration.getSystemConfiguration(null)
				.getGlobalSettings().getEvaluationDate();
	}

	public void update(Observable o, Object arg) {
		if (!evaluationDate.equals(Configuration.getSystemConfiguration(null)
				.getGlobalSettings().getEvaluationDate())) {
			evaluationDate = Configuration.getSystemConfiguration(null)
					.getGlobalSettings().getEvaluationDate();
			initializeDates();
		}
		super.update(o, arg);
	}

	protected abstract void initializeDates();
}
