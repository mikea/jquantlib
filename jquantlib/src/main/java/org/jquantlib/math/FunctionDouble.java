package org.jquantlib.math;

/**
 * Represents a function of one variable; f(x)
 * 
 * @author <Richard Gomes>
 */
public interface FunctionDouble {

	/**
	 * Mimics the operator overloading <pre>operator()</pre>
	 * 
	 * @return a double
	 */
	public double doubleValue() /* @ReadOnly */;
}
