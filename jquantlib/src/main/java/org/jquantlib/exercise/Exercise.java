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
 Copyright (C) 2003 Ferdinando Ametrano
 Copyright (C) 2001, 2002, 2003 Sadruddin Rejeb
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

package org.jquantlib.exercise;

import org.jquantlib.util.Date;

import cern.colt.list.ObjectArrayList;






/**
 * Base exercise class
 * 
 * @author Richard Gomes
 */
public abstract class Exercise {

	/**
	 * Defines the exercise type. It can be American, Bermudan or European
	 * 
	 * @author Richard Gomes
	 */
	public enum Type {
		AMERICAN, BERMUDAN, EUROPEAN;
	}

	private Exercise.Type type;
	private ObjectArrayList dates;
	
	/**
	 * Constructs an exercise and defines the exercise type
	 * 
	 * @param type is the type of exercise
	 * 
	 * @see Exercise.Type
	 */
	protected Exercise(Exercise.Type type) {
		this.type = type;
		this.dates = new ObjectArrayList(5); // some reasonable prime number
	}

	/**
	 * Returns the exercise type
	 * 
	 * @return the exercise type
	 * 
	 * @see Exercise.Type
	 */
	public Exercise.Type getType() {
		return type;
	}
	
	/**
	 * This method is only used by extended classes on the very special cases 
	 * when the type of the exercise must be changed.
	 * 
	 * @param type is the exercise type
	 * 
	 * @see Exercise.Type
	 * @see BermudanExercise
	 */
	protected void setType(Exercise.Type type) {
		this.type = type;
	}

	public int size() {
		return dates.size();
	}
	
    public void addDate(final Date date) {
    	dates.add(date);
    }
	
	public Date getDate(final int index) /* @ReadOnly */ {
		return (Date)dates.get(index);
	}
	
	public Date getLastDate() /* @ReadOnly */ {
		return getDate(dates.size()-1);
	}
	
}
