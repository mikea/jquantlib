/*
 Copyright (C) 2008 Richard Gomes

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

/*
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006, 2007 StatPro Italia srl

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

package org.jquantlib.quotes;

import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;
import org.jquantlib.util.WeakReferenceObservable;

/**
 * Shared handle to an observable
 * <p>
 * All copies of an instance of this class refer to the same observable by means
 * of a relinkable weak reference. When such pointer is relinked to another
 * observable, the change will be propagated to all the copies.
 *
 * @author Richard Gomes
 */
// TODO: code review :: please verify against QL/C++ code
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override

//TODO: ideally T should extend Ops.DoubleOp, Observable
// and we should offer a method called "value()" which does T.evaluate()

public class Handle<T extends Observable> implements Observable {

    protected Link link;

    public Handle(final Class<T> klass) {
        this.link = new Link(null, true);
    }

    public Handle(final T observable) {
        this.link = new Link(observable, true);
    }

    public Handle(final T observable, final boolean registerAsObserver) {
        this.link = new Link(observable, registerAsObserver);
    }

    public Handle(final Handle<T> another) {
        this.link = another.link;
    }

    public final boolean empty() /* @ReadOnly */ {
        return link.isEmpty();
    }

    public final T currentLink() {
        return link.currentLink();
    }

    public void linkTo(final T observable) {
        this.linkTo(observable, true);
    }

    public void linkTo(final T observable, final boolean registerAsObserver) {
        link.linkTo(observable, registerAsObserver);
    }

    //
    // overrides Object
    //

    @Override
    public String toString() {
        return link.toString();
    }


    //
    // implements Observable interface
    //

    private final Observable delegatedObservable = new WeakReferenceObservable(this);

    @Override
    public final void addObserver(final Observer observer) {
        delegatedObservable.addObserver(observer);
    }

    @Override
    public final int countObservers() {
        return delegatedObservable.countObservers();
    }

    @Override
    public final void deleteObserver(final Observer observer) {
        delegatedObservable.deleteObserver(observer);
    }

    @Override
    public final void notifyObservers() {
        delegatedObservable.notifyObservers();
    }

    @Override
    public final void notifyObservers(final Object arg) {
        delegatedObservable.notifyObservers(arg);
    }

    @Override
    public final void deleteObservers() {
        delegatedObservable.deleteObservers();
    }

    @Override
    public final List<Observer> getObservers() {
        return delegatedObservable.getObservers();
    }


    //
    // inner classes
    //

    private class Link implements Observable, Observer {

        static private final String EMPTY_HANDLE = "empty Handle cannot be dereferenced"; // TODO: message

        //
        // private fields
        //

        private T observable	   = null;
        private boolean isObserver = false;


        //
        // public constructors
        //

        //XXX
        //		public Link() {
        //			this(null);
        //		}
        //
        //		public Link(T observable) {
        //			setLink(observable);
        //		}

        public Link(final T observable, final boolean registerAsObserver) {
            linkTo(observable, registerAsObserver);
        }


        //
        // public methods
        //

        // TODO: code review :: please verify against QL/C++ code
        public final boolean isEmpty() /* @ReadOnly */ {
            return (this.observable==null);
        }

        public final T currentLink() /* @ReadOnly */ {
            return this.observable;
        }

        //XXX
        //		public final void setLink(final T observable) {
        //			setLink(observable, true);
        //		}

        public final void linkTo(final T observable, final boolean registerAsObserver) {
            // remove this from observable
            if ((this.observable!=observable) || (this.isObserver!=registerAsObserver)) {
                if (this.observable!=null && this.isObserver) {
                    this.observable.deleteObserver(this);
                }
                this.observable = observable;
                this.isObserver = registerAsObserver;
                if (this.observable!=null && this.isObserver) {
                    this.observable.addObserver(this);
                }
                if (this.observable!=null) {
                    this.observable.notifyObservers();
                }
            }
        }


        //
        // overrides Object
        //

        @Override
        public String toString() {
            return observable==null ? "none" : observable.toString();
        }


        //
        // Implements Observer
        //

        //XXX:registerWith
        //        @Override
        //        public void registerWith(final Observable o) {
        //            o.addObserver(this);
        //        }
        //
        //        @Override
        //        public void unregisterWith(final Observable o) {
        //            o.deleteObserver(this);
        //        }

        @Override
        public final void update(final Observable o, final Object arg) {
            delegatedObservable.notifyObservers(arg);
        }


        //
        // implements Observable
        //

        @Override
        public final void addObserver(final Observer observer) {
            QL.require(observable!=null , EMPTY_HANDLE); // QA:[RG]::verified // TODO: message
            observable.addObserver(observer);
        }

        @Override
        public final int countObservers() {
            QL.require(observable!=null , EMPTY_HANDLE); // QA:[RG]::verified // TODO: message
            return observable.countObservers();
        }

        @Override
        public final void deleteObserver(final Observer observer) {
            QL.require(observable!=null , EMPTY_HANDLE); // QA:[RG]::verified // TODO: message
            observable.deleteObserver(observer);
        }

        @Override
        public final void notifyObservers() {
            QL.require(observable!=null , EMPTY_HANDLE); // QA:[RG]::verified // TODO: message
            observable.notifyObservers();
        }

        @Override
        public final void notifyObservers(final Object arg) {
            QL.require(observable!=null , EMPTY_HANDLE); // QA:[RG]::verified // TODO: message
            observable.notifyObservers(arg);
        }

        @Override
        public final void deleteObservers() {
            QL.require(observable!=null , EMPTY_HANDLE); // QA:[RG]::verified // TODO: message
            observable.deleteObservers();
        }

        @Override
        public final List<Observer> getObservers() {
            QL.require(observable!=null , EMPTY_HANDLE); // QA:[RG]::verified // TODO: message
            return observable.getObservers();
        }

    }

}
