package org.jquantlib.util;


/**
 * This interface defines the {@link Visitor} side of the Visitor design pattern 
 *
 * @author Richard Gomes
 *
 * @see Visitable
 * @see TypedVisitor
 * @see TypedVisitable
 * 
 * @see <a href="http://www.exciton.cs.rice.edu/JavaResources/DesignPatterns/VisitorPattern.htm">The Visitor Design Pattern</a>
 *
 * @param <T> defines de data structure to be visited
 */
public interface Visitor<T> {

	public void visit(T  o);
}
