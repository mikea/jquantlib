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
