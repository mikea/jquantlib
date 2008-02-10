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

import org.jquantlib.util.Date;

public class Settings {
	
	static private Settings singleton = null;
	
	static public Settings getInstance() {
		if (singleton==null) {
			singleton = new Settings();
		}
		return singleton; 
	}

	private boolean extraSafetyChecks;
	private boolean enforcesTodaysHistoricFixings;
	private Date evaluationDate;
	
	private Settings() {
		this.extraSafetyChecks = false;
		this.enforcesTodaysHistoricFixings = false;
		this.evaluationDate = Date.getTodaysDate();
	}

	public boolean isExtraSafetyChecks() {
		return extraSafetyChecks;
	}

	public void setExtraSafetyChecks(boolean extraSafetyChecks) {
		this.extraSafetyChecks = extraSafetyChecks;
	}

	public Date getEvaluationDate() {
		return evaluationDate;
	}

	public void setEvaluationDate(Date evaluationDate) {
		this.evaluationDate = evaluationDate;
	}

	public boolean isEnforcesTodaysHistoricFixings() {
		return enforcesTodaysHistoricFixings;
	}

	public void setEnforcesTodaysHistoricFixings(boolean enforcesTodaysHistoricFixings) {
		this.enforcesTodaysHistoricFixings = enforcesTodaysHistoricFixings;
	}
	
}
