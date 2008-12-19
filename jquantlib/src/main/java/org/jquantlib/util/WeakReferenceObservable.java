/*
 Copyright (C) 2007 Richard Gomes

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

package org.jquantlib.util;

import java.lang.ref.WeakReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of Observable that holds references to Observers as
 * WeakReferences.
 * 
 * @note This implementation notifies the observers in a synchronous
 * fashion. Note that this can cause trouble if you notify the observers while
 * in a transactional context because the notification is then done also in the
 * transaction.
 * 
 * <p>
 * This class is based on the work done by Martin Fischer. See references below.
 * 
 * @see <a
 *      href="http://www.jroller.com/martin_fischer/entry/a_generic_java_observer_pattern">
 *      Martin Fischer: Observer and Observable interfaces</a>
 * @see <a href="http://jdj.sys-con.com/read/35878.htm">Improved Observer/Observable</a>
 * 
 * @see Observable
 * @see Observer
 * @see DefaultObservable
 * 
 * @author Martin Fischer (original author)
 * @author Richard Gomes
 * @author Srinivas Hasti
 */
public class WeakReferenceObservable extends DefaultObservable {
	
	//
	// logger
	//
	private final static Logger logger = LoggerFactory.getLogger(WeakReferenceObservable.class);

    public WeakReferenceObservable(Observable observable) {
        super(observable);
    }

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(new WeakReferenceObserver(observer));
    }

    @Override
    public void deleteObserver(Observer observer) {
        // Also deletes weak references whose referents got gc'ed
        for (Observer weakObserver : getObservers()) {
            WeakReferenceObserver weakReference = (WeakReferenceObserver) weakObserver;
            Observer o = weakReference.get();
            if (o == null || o.equals(observer)) {
                deleteWeakReference(weakReference);
            }
        }
    }
    
    private void deleteWeakReference(WeakReferenceObserver observer){
        super.deleteObserver(observer);
    }
    

    //
    // inner classes
    //
    
    private class WeakReferenceObserver extends WeakReference<Observer> implements Observer {
    	
        public WeakReferenceObserver(Observer referent) {
            super(referent);
        }

        public void update(Observable o, Object arg) {
            Observer referent = get();
            if (referent != null)
                referent.update(o, arg);  
            else //delete the weak reference from the list if underlying gc'ed
                deleteWeakReference(this);
        }
    }

}
