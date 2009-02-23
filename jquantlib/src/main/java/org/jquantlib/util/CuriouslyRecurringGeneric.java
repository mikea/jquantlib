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
package org.jquantlib.util;


/**
 * @author Srinivas Hasti
 * 
 */
//
// FIXME: [Richard] Could this class be removed?
// Seems like it is only necessary in C++ because there's no instance associated to a template.
// Our translation to Java uses Generics which always has a class (and an instance) associated to it.
//
public abstract class CuriouslyRecurringGeneric<T extends CuriouslyRecurringGeneric<T>> {

    public T impl() {
        return (T) this;
    }

}
