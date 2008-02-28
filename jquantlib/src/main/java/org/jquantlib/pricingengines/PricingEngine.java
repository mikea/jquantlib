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
 Copyright (C) 2002, 2003 Ferdinando Ametrano
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2007 StatPro Italia srl

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

import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

public abstract class PricingEngine implements Observable {
	
	public abstract PricingEngineArguments getArguments();
	public abstract PricingEngineResults getResults();

    /**
	 * Implements multiple inheritance via delegate pattern to an inner class
	 * 
	 * @see Observable
	 * @see DefaultObservable
	 */
    private Observable delegatedObservable = new DefaultObservable();

	public void addObserver(Observer observer) {
		delegatedObservable.addObserver(observer);
	}
	
	public void deleteObserver(Observer observer) {
		delegatedObservable.deleteObserver(observer);
	}
	
	public void notifyObservers() {
		delegatedObservable.notifyObservers();
	}
	
	public void notifyObservers(Object arg) {
		delegatedObservable.notifyObservers(arg);
	}

}
