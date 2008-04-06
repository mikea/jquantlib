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

/**
 * This class employs the Singleton Design Pattern in order 
 * to keeps global configuration.
 *
 * <p>
 * Global configurations are intended to be constant during 
 * the entire life cycle of the application.
 * 
 * @note In heavily multi-threaded environments threads must cache 
 * configurations from this singleton.
 */
// CODE REVIEW DONE by Richard Gomes
public class Configuration {
	
	/**
	 * Default value for <i>ExtraSafetyChecks</i> property
	 * 
	 * @see #extraSafetyChecks
	 */
	private static boolean defaultExtraSafefyChecks = true;
	
	/**
	 * Default value for <i>EnforcesTodayHistoricFixings</i> property
	 * 
	 * @see #enforcesTodaysHistoricFixings
	 */
	private static boolean defaultEnforcesTodaysHistoricFixings = false;
	
	
	// EXPLAIN its meaning
	private boolean extraSafetyChecks;
	
	
	// EXPLAIN its meaning
	private boolean enforcesTodaysHistoricFixings;
	
	static private String lock = "lock";
	static private Configuration singleton = null;

	
	/**
	 * Returns a singleton of this class
	 * 
	 * @return a singleton of this class
	 * @see http://www.ibm.com/developerworks/java/library/j-dcl.html
	 */
	static public Configuration getInstance() {
		if (singleton==null) {
			synchronized (lock) {
				if (singleton==null) {
					singleton = new Configuration();					
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
	private Configuration() {
		this.extraSafetyChecks = defaultExtraSafefyChecks;
		this.enforcesTodaysHistoricFixings = defaultEnforcesTodaysHistoricFixings;
	}


	/**
	 * This constructor is provided so that application settings
	 * can be initialized via implementation independent Preferences.
	 * 
	 * @param prefs is a Preferences object
	 */
	public Configuration(final Preferences prefs) {
		if (prefs!=null) {
			synchronized (lock) {
				if (singleton!=null) throw new IllegalStateException("Singleton already initialized");
				this.extraSafetyChecks = prefs.getBoolean("ExtraSafetyChecks", defaultExtraSafefyChecks);
				this.enforcesTodaysHistoricFixings = prefs.getBoolean("EnforcesTodaysHistoricFixings", defaultEnforcesTodaysHistoricFixings);
			}
		}
	}
	
	public boolean isExtraSafetyChecks() {
		return extraSafetyChecks;
	}

	public boolean isEnforcesTodaysHistoricFixings() {
		return enforcesTodaysHistoricFixings;
	}

}
