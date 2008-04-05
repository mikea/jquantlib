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

package org.jquantlib;

import java.util.prefs.Preferences;

import org.jquantlib.util.Date;

/**
 * This class employs the Singleton Design Pattern in order to keep application
 * global settings.
 * 
 * <p>
 * Global settings are mutable values which have a lyfe cycle of a certain
 * operation of sequence of operations defined by the application.
 * 
 * @note In heavily multi-threaded environments threads must cache settings from
 *       this singleton.
 */
// REVIEWED by Richard Gomes
public class Settings {

	private static boolean defaultTodaysPayments = false;

	/**
	 * This field determines whether payments expected to happen at
	 * the <i>current evaluation date</i> are considered.
	 * 
	 * @see #evaluationDate
	 */
	private boolean todaysPayments;

	/**
	 * This field keeps the current evaluation date.
	 * 
	 * <p>
	 * Notice that the current evaluation date <b>is not necessarily</b> the
	 * current date or today's date in other words. In the specific situation
	 * when the evaluation date is never defined explicitly, then today's date
	 * is assume by default.
	 */
	private Date evaluationDate;

	static private String lock = "lock";
	static private Settings singleton = null;

	/**
	 * Returns a singleton of this class
	 * 
	 * @return a singleton of this class
	 * @see http://www.ibm.com/developerworks/java/library/j-dcl.html
	 */
	static public Settings getInstance() {
		if (singleton == null) {
			synchronized (lock) {
				if (singleton == null) {
					singleton = new Settings();
				}
			}

		}
		return singleton;
	}

	/**
	 * Default constructor
	 * 
	 * @see defaultExtraSafefyChecks
	 * @see defaultEnforcesTodaysHistoricFixings
	 * @see defaultTodaysPayments
	 */
	private Settings() {
		this.todaysPayments = defaultTodaysPayments;
		this.evaluationDate = Date.getTodaysDate();
	}

	/**
	 * This constructor is provided so that application settings can be
	 * initialized via implementation independent Preferences.
	 * 
	 * @param prefs
	 *            is a Preferences object
	 */
	public Settings(final Preferences prefs) {
		if (prefs != null) {
			synchronized (lock) {
				if (singleton != null)
					throw new IllegalStateException("Singleton already initialized");
				this.todaysPayments = prefs.getBoolean("TodaysPayments", defaultTodaysPayments);
				this.evaluationDate = Date.getTodaysDate();
			}
		}
	}

	/**
	 * @return the value of field todaysPayments
	 * 
	 * @see #todaysPayments
	 */
	public boolean isTodaysPayments() {
		return todaysPayments;
	}

	/**
	 * @return the value of field evaluationDate
	 * 
	 * @see #evaluationDate
	 */
	public Date getEvaluationDate() {
		return evaluationDate;
	}

	/**
	 * Changes the value of field todaysPayments
	 * 
	 * @see #todaysPayments
	 */
	public void setTodaysPayments(final boolean todaysPayments) {
		this.todaysPayments = todaysPayments;
	}

	/**
	 * Changes the value of field evaluationDate.
	 * 
	 * <p>
	 * Notice that a successful change of evaluationDate notifies
	 * all its listeners.
	 * 
	 * @see #evaluationDate
	 */
	public void setEvaluationDate(final Date evaluationDate) {
		this.evaluationDate.getUpdatable().update(evaluationDate);
		this.evaluationDate.notifyObservers();
	}

}
