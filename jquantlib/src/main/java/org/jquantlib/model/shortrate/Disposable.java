/*
 Copyright (C) 2008 Praneet Tiwari

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
package org.jquantlib.model.shortrate;

//this is incomplete, skeleton here to let the code compile

/**
 * 
 * @author Praneet Tiwari
 */
// ! generic disposable object with move semantics
/*
 * ! This class can be used for returning a value by copy. It relies on the returned object exposing a <tt>swap(T\&)</tt> method
 * through which the copy constructor and assignment operator are implemented, thus resulting in actual move semantics. Typical use
 * of this class is along the following lines: \code Disposable<Foo> bar(Integer i) { Foo f(i*2); return f; } \endcode
 * 
 * \warning In order to avoid copies in code such as shown above, the conversion from <tt>T</tt> to <tt>Disposable\<T\></tt> is
 * destructive, i.e., it does <b>not</b> preserve the state of the original object. Therefore, it is necessary for the developer to
 * avoid code such as \code Disposable<Foo> bar(Foo& f) { return f; } \endcode which would likely render the passed object unusable.
 * The correct way to obtain the desired behavior would be: \code Disposable<Foo> bar(Foo& f) { Foo temp = f; return temp; }
 * \endcode
 */
// TODO: code review :: Consider removal of this class :S
public class Disposable<T> {

    public Disposable() {
        throw new UnsupportedOperationException();
    }

    // public Disposable(T t);
    // public Disposable(final Disposable<T>& t);
    // public Disposable<T>& operator=(const Disposable<T>& t);
    // inline definitions
    /*
     * template <class T> inline Disposable<T>::Disposable(T& t) { this->swap(t); }
     * 
     * template <class T> inline Disposable<T>::Disposable(const Disposable<T>& t) : T() {
     * this->swap(const_cast<Disposable<T>&>(t)); }
     * 
     * template <class T> inline Disposable<T>& Disposable<T>::operator=(const Disposable<T>& t) {
     * this->swap(const_cast<Disposable<T>&>(t)); return *this; }
     */
}
