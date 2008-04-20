package org.jquantlib.math;

/**
 * Represents a function of one variable; f(x)
 * 
 * @author <Richard Gomes>
 */
public interface BinaryFunctionDouble {

	/**
	 * Computes the value of the function; f(x, y)
	 * 
	 * @param x
	 * @param y
	 * @return f(x, y)
	 */
	//FIXME Generics on argument return?
	public double evaluate(double x, double y);
	
	//boolean isFailed() TODO error handling
}
