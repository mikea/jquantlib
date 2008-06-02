package org.jquantlib.util;

/**
 * This interface works together to {@link TypedVisitor} in order to provide
 * the functionality of obtaining a specific {@link Visitor}.
 * 
 * <p>This functionality is needed every time a class acts as a Visitor of more
 * than one data structure or when a class acting as a Visitable requires a
 * certain kind of Visitor.
 * 
 * @note A class which implements {@link TypedVisitable} probably does not need
 * to implement {@link Visitable}
 * 
 * @author Richard Gomes
 * 
 * @see Visitor
 * @see Visitable
 * @see TypedVisitor
 * 
 * @see <a href="http://www.exciton.cs.rice.edu/JavaResources/DesignPatterns/VisitorPattern.htm">The Visitor Design Pattern</a>
 *
 * @param <T> defines the data structure to be visited
 */
public interface TypedVisitable<T> {

	/**
	 * 
	 * @param v
	 */
	public void accept(TypedVisitor<T> v);

}
