

package org.jquantlib.math;


/**
 * Represents a function of one variable; y = f(x)
 * 
 * @author Ueli Hofstetter
 * @author Richard Gomes
 */
public interface E_IUnaryFunction<X, Y> extends UnaryFunction<X, Y> {
    
	public void setParams(X ... params);
    void setBinaryFunction(E_IBinaryFunction<X, Y> binaryFunction);
    void setBoundedValue(X x);

}
