package org.jquantlib.util;


/**
 * This interface defines the {@link Visitor} side of the Visitor design pattern 
 *
 * @author Richard Gomes
 *
 * @see <a href="http://www.exciton.cs.rice.edu/JavaResources/DesignPatterns/VisitorPattern.htm">The Visitor Design Pattern</a>
 */
public interface Visitor<T> {

	public void visit(T  o);
	
}
