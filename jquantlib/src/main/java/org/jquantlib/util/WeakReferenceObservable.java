/*
 Copyright (C) 2007

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

package org.jquantlib.util;

import java.lang.ref.WeakReference;

/**
 * Implementation of Observable that holds references to Observers as
 * WeakReferences.
 * 
 * @see Observable
 * @see Observer
 * 
 * @author Srinivas Hasti
 */
public class WeakReferenceObservable extends DefaultObservable {

    public WeakReferenceObservable(Observable observable) {
        super(observable);
    }

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(new WeakReferenceObserver(observer));
    }

    @Override
    public void deleteObserver(Observer observer) {
        //Also deletes weak references whose referents got gc'ed
        for (Observer wObserver : getObservers()) {
            WeakReferenceObserver wReference = (WeakReferenceObserver) wObserver;
            Observer o = wReference.get();
            if (o == null || o.equals(observer)) {
                deleteWeakReference(wReference);
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
