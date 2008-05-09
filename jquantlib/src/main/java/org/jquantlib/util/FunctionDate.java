package org.jquantlib.util;

/**
 * Represents a function of one variable; f(x)
 * 
 * @author <Richard Gomes>
 */
public interface FunctionDate {

	/**
	 * Mimics the operator overloading <pre>operator()</pre>
	 * 
	 * @return a double
	 */
	public int dateValue() /* @ReadOnly */;
}
