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


//FIXME: move to org.jquantlib.termstructures.yieldcurves



import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.BootstrapHelper;
import org.jquantlib.util.Date;
import org.jquantlib.util.Observable;

/**
 * @author Srinivas Hasti
 * 
 */
// ! Rate helper with date schedule relative to the global evaluation date
/*
 * ! This class takes care of rebuilding the date schedule when the global
 * evaluation date changes
 */
public abstract class RelativeDateRateHelper extends BootstrapHelper {

	private Date evaluationDate;

	public RelativeDateRateHelper(double d) {
		super(d);
		// TODO
		// registerWith(Settings::instance().evaluationDate());
		// evaluationDate_ = Settings::instance().evaluationDate();
	}

	public RelativeDateRateHelper(Handle quote, Object termStructure,
			Date earliestDate, Date latestDate) {
		super(quote, termStructure, earliestDate, latestDate);
		// TODO
		// registerWith(Settings::instance().evaluationDate());
		// evaluationDate_ = Settings::instance().evaluationDate();
	}

	public void update(Observable o, Object arg) {
		// TODO:
		// if (evaluationDate_ != Settings::instance().evaluationDate()) {
		// evaluationDate_ = Settings::instance().evaluationDate();
		// initializeDates();
		// }
		// RateHelper::update();
	}

	protected abstract void initializeDates();
}
