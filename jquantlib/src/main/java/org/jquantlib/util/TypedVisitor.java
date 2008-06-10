/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
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
 * This interface provides the functionality of obtaining a specific {@link Visitor}
 * 
 * <p>This functionality is needed every time a class acts as a Visitor of more
 * than one data structure or when a class acting as a Visitable requires a
 * certain kind of Visitor. 
 * 
 * @note A class which implements {@link TypedVisitor} probably provides one or
 * more {@link Visitor} implementations.
 * 
 * @author Richard Gomes
 * 
 * @see Visitor
 * @see Visitable
 * @see TypedVisitable
 * 
 * @see <a href="http://www.exciton.cs.rice.edu/JavaResources/DesignPatterns/VisitorPattern.htm">The Visitor Design Pattern</a>
 *
 * @param <T> defines de data structure to be visited
 */
public interface TypedVisitor<T> {

	/**
	 * Requests a {@link Visitor} responsible for visiting a specific data structure type
	 * 
	 * @param klass specified the data structure type
	 * @return a Visitor responsible for visiting a certain data structure type
	 */
	public Visitor<T> getVisitor(Class<? extends T> klass);
	
}
