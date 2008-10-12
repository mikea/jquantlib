/*
 Copyright (C) 2008 Srinivas Hasti

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

package org.jquantlib.time;

/**
 * @author Srinivas Hasti
 * 
 */
public enum DateGenerationRule {
	/**
	 * Backward from termination date to effective date
	 */
	BACKWARD,
	/**
	 * Forward from effective date to termination date.
	 */
	FORWARD,
	/**
	 * No intermediate dates between effective date and termination date
	 */
	ZERO,
	/**
	 * All dates but effective date and termination date are taken to be on the
	 * third wednesday of their month (with forward calculation
	 */
	THIRD_WEDNESDAY;

	@Override
	public String toString() {
		String val = null;
		switch (this) {
		case BACKWARD:
			val = "Backward";
			break;
		case FORWARD:
			val = "Forward";
			break;
		case ZERO:
			val = "Zero";
			break;
		case THIRD_WEDNESDAY:
			val = "ThirdWednesday";
			break;
		default:
			val = super.toString();
			break;
		}
		return val;
	}
}
