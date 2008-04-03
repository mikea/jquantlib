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
// CODE REVIEW DONE by Richard Gomes
public class Settings {

	/**
	 * Default value for <i>TodaysPayments</i> property
	 */
	private static boolean defaultTodaysPayments = false;

	private boolean todaysPayments;
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

	public boolean isTodaysPayments() {
		return todaysPayments;
	}

	public Date getEvaluationDate() {
		return evaluationDate;
	}

	public void setTodaysPayments(final boolean todaysPayments) {
		this.todaysPayments = todaysPayments;
	}

	public void setEvaluationDate(final Date evaluationDate) {
		if (evaluationDate==null) throw new NullPointerException();
		
// Srinivas: Could you please suggest an implementation for this?		
//		// obtain Observers and remove from current Observable Date
//		Observer[] observers = this.evaluationDate.getObservers();
//		this.evaluationDate.deleteObservers();

		
		// assigns a new value and assign previous Observers
		this.evaluationDate = evaluationDate;
		
		
// Srinivas: Could you please suggest an implementation for this?		
//		this.evaluationDate.addObservers(observers);
		
		
		// notify Observers, if any, about date change
		this.evaluationDate.notifyObservers();
	}

}
