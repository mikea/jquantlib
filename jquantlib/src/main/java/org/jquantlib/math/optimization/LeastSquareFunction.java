package org.jquantlib.math.optimization;

import org.jquantlib.lang.annotation.Real;
import org.jquantlib.math.Array;
import org.jquantlib.math.Matrix;

public class LeastSquareFunction extends CostFunction  {
    
    //! least square problem
    protected LeastSquareProblem lsp_;
    
    //! Default constructor
    LeastSquareFunction(LeastSquareProblem lsp) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.lsp_ = lsp;
    }
    
    public double value(final Array x)  {
        // size of target and function to fit vectors
        Array target = new Array(lsp_.size());
        Array fct2fit = new Array(lsp_.size());
        // compute its values
        lsp_.targetAndValue(x, target, fct2fit);
        // do the difference
        Array diff = target.operatorSubtractCopy(fct2fit);
        // and compute the scalar product (square of the norm)
        return Array.dotProduct(diff, diff);
    }

    public void gradient(Array grad_f, final Array x) {
        // size of target and function to fit vectors
        Array target = new Array(lsp_.size());
        Array fct2fit = new Array(lsp_.size());
        // size of gradient matrix
        Matrix grad_fct2fit  = new Matrix(lsp_.size (), x.size ());
        // compute its values
        lsp_.targetValueAndGradient(x, grad_fct2fit, target, fct2fit);
        // do the difference
        Array diff = target.operatorSubtractCopy(fct2fit);
        // compute derivative
        // FIXME: transpost implementation ?!!!!!!!!!!!!!!
        // grad_f = -2.0*(transpose(grad_fct2fit)*diff);
    }

    public double valueAndGradient(Array grad_f, Array x) {
        // size of target and function to fit vectors
        Array target = new Array(lsp_.size());
        Array fct2fit = new Array(lsp_.size());
        // size of gradient matrix
        Matrix grad_fct2fit  = new Matrix(lsp_.size (), x.size ());
        // compute its values
        lsp_.targetValueAndGradient(x, grad_fct2fit, target, fct2fit);
        // do the difference
        Array diff = target.operatorSubtractCopy(fct2fit);
        // compute derivative
        // FIXME: transpost implementation ?!!!!!!!!!!!!!!
        // grad_f = -2.0*(transpose(grad_fct2fit)*diff);
        // and compute the scalar product (square of the norm)
        return Array.dotProduct(diff, diff);
    }

    @Override
    public Array values(Array x) {
        throw new UnsupportedOperationException("To be implemented");
    }

}
