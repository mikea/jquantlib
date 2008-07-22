/*
 Copyright (C) 2008 Richard Gomes

 This source code is release under the BSD License.
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.methods.finitedifferences;

import org.jquantlib.math.Array;

/**
 * @author Srinivas Hasti
 * @author Tim Swetonic
 */
public class TridiagonalOperator implements Operator {

	public static interface TimeSetter {
		public void setTime(double t, TridiagonalOperator l);
	}
 
	protected TimeSetter timeSetter;
	protected Array lowerDiagonal;
	protected Array diagonal;
	protected Array upperDiagonal;

	public TridiagonalOperator(int size) {
		if (size >= 2) {
			this.lowerDiagonal = new Array(size - 1);
			this.diagonal = new Array(size);
			this.upperDiagonal = new Array(size - 1);
		} else if (size == 0) {
			this.lowerDiagonal = new Array(0);
			this.diagonal = new Array(0);
			this.upperDiagonal = new Array(0);
		} else {
			throw new IllegalStateException("Invalid size " + size);
		}

	}

	public TridiagonalOperator(Array ldiag, Array diag, Array udiag) {
		if (ldiag.size() != diag.size() - 1)
			throw new IllegalStateException("wrong size for lower diagonal");
		if (udiag.size() != diag.size() - 1)
			throw new IllegalStateException("wrong size for upper diagonal");
		this.lowerDiagonal = ldiag;
		this.diagonal = diag;
		this.upperDiagonal = udiag;
	}

    public TridiagonalOperator(TridiagonalOperator t) {
    	this.diagonal = t.diagonal();
    	this.upperDiagonal = t.upperDiagonal();
    	this.lowerDiagonal = t.lowerDiagonal();
    	this.timeSetter = t.getTimeSetter();
    }
    
	public void setFirstRow(double b, double c) {
		diagonal.set(0, b);
		upperDiagonal.set(0, c);
 	}

	public void setMidRow(int size, double a, double b, double c) {
		if(size >= 1 && size <= size()-2)
			throw new IllegalStateException("out of range in setMidRow");
		
		lowerDiagonal.set(size-1, a);
		diagonal.set(size, b);
		upperDiagonal.set(size, c);

	}

	public void setMidRows(double a, double b, double c) {
        for (int i = 1; i <= size()-2; i++) {
    		lowerDiagonal.set(i-1, a);
    		diagonal.set(i, b);
    		upperDiagonal.set(i, c);
        }
	}

	public void setLastRow(double a, double b) {
        lowerDiagonal.set(size() - 2, a);
        diagonal.set(size() -1, b);
	}

	public void setTime(double t) {
        if(timeSetter != null) {
        	timeSetter.setTime(t, this);
        }
	}
	
    public int size()  {
        return diagonal.size();
    }


    //default scope in c++? public?
    // unary operators
    public TridiagonalOperator add(final TridiagonalOperator D) {
	    TridiagonalOperator D1 = D;
	    return D1;
    }
    
    public TridiagonalOperator subtract(final TridiagonalOperator D) {
        Array low = this.lowerDiagonal.quickOperatorSubtractCopy(D.lowerDiagonal()); 
        Array mid = this.diagonal.quickOperatorSubtractCopy(D.diagonal());
        Array high = this.upperDiagonal.quickOperatorSubtractCopy(D.upperDiagonal()); 
        
        return new TridiagonalOperator(low,mid,high);

    }
    
    // binary operators
    public TridiagonalOperator add(final TridiagonalOperator D1, final TridiagonalOperator D2) {

        Array low = D1.lowerDiagonal.quickOperatorAddCopy(D2.lowerDiagonal());
        Array mid = D1.diagonal.quickOperatorAddCopy(D2.diagonal());
        Array high = D1.upperDiagonal.quickOperatorAddCopy(D2.upperDiagonal());
        
        return new TridiagonalOperator(low,mid,high);
    }
    
    public TridiagonalOperator subtract(final  TridiagonalOperator D1, final TridiagonalOperator D2) {
    	
    	Array low = D1.lowerDiagonal.quickOperatorSubtractCopy(D2.lowerDiagonal);
    	Array mid = D1.diagonal.quickOperatorSubtractCopy(D2.diagonal);
    	Array high = D1.upperDiagonal.quickOperatorSubtractCopy(D2.upperDiagonal);
            
        return new TridiagonalOperator(low,mid,high);
    }
    
    public TridiagonalOperator multiply(double a, final TridiagonalOperator D) {
    	Array low = D.lowerDiagonal;
    	low.operatorMultiply(a);
    	Array mid = D.diagonal;
    	mid.operatorMultiply(a);
    	Array high = D.upperDiagonal;
    	high.operatorMultiply(a);
    	
        return new TridiagonalOperator(low,mid,high);
    }
    
    public TridiagonalOperator multiply(final TridiagonalOperator D, double a) {
    	return multiply(a, D);
    }

    public TridiagonalOperator divide(final TridiagonalOperator D, double a) {
    	Array low = D.lowerDiagonal;
    	low.operatorDivide(a);
    	Array mid = D.diagonal;
    	mid.operatorDivide(a);
    	Array high = D.upperDiagonal;
    	low.operatorDivide(a);
    	
        return new TridiagonalOperator(low,mid,high);
    }
    	
  //public:
  //  typedef Array array_type;
    
    //! \name Operator interface
    //@{
    //! apply operator to a given array
    
    
    
    //! solve linear system with SOR approach
    public final Array SOR(final Array rhs, int tol) throws Exception {
        if(rhs.size() != size()) 
        	throw new IllegalStateException("rhs has the wrong size");

        // initial guess
        Array result = rhs;

        // solve tridiagonal system with SOR technique
        int sorIteration, i;
        double omega = 1.5;
        double err = 2.0*tol;
        double temp;
        for (sorIteration=0; err>tol ; sorIteration++) {
            if(sorIteration>100000) {
            	throw new Exception("tolerance (" + tol + ") not reached in "
                       + sorIteration + " iterations. "
                       + "The error still is " + err);
            }

            temp = omega * (rhs.get(0)     -
                            upperDiagonal.get(0)   * result.get(1) -
                            diagonal.get(0)        * result.get(0))/diagonal.get(0);
            err = temp*temp;
            result.set(0, result.get(0) + temp);

            for (i=1; i<size()-1 ; i++) {
                temp = omega *(rhs.get(i)     -
                               upperDiagonal.get(i)   * result.get(i+1) -
                               diagonal.get(i)        * result.get(i) -
                               lowerDiagonal.get(i-1) * result.get(i-1))/diagonal.get(i);
                err += temp * temp;
                result.set(i, result.get(i) + temp);
            }

            temp = omega * (rhs.get(i)     -
                            diagonal.get(i)        * result.get(i) -
                            lowerDiagonal.get(i-1) * result.get(i-1))/diagonal.get(i);
            err += temp*temp;
            result.set(i, result.get(i) + temp);
        }
        
        return result;
        
    }
    
    //! identity instance
   public TridiagonalOperator identity(int size) {
        TridiagonalOperator I = new TridiagonalOperator(
        		new Array(size-1, 0.0),     // lower diagonal
                new Array(size,   1.0),     // diagonal
                new Array(size-1, 0.0));    // upper diagonal
        return I;
    }



//TODO: test assignment
/*inline TridiagonalOperator& TridiagonalOperator::operator=(
                            const Disposable<TridiagonalOperator>& from) {
    swap(const_cast<Disposable<TridiagonalOperator>&>(from));
    return *this;
}*/


	public boolean isTimeDependent() {
	    return timeSetter != null;
	}
	
	public final Array lowerDiagonal() {
	    return lowerDiagonal;
	}
	
	public final Array diagonal() {
	    return diagonal;
	}
	
	public final Array upperDiagonal() {
	    return upperDiagonal;
	}
	
	
	public void swap(TridiagonalOperator from) {
		this.diagonal.swap(from.diagonal);
	    this.lowerDiagonal.swap(from.lowerDiagonal);
	    this.upperDiagonal.swap(from.upperDiagonal);
	    this.timeSetter = from.getTimeSetter();
	}

	public TimeSetter getTimeSetter() {
		return this.timeSetter;
	}

	public void swap(TridiagonalOperator L1, TridiagonalOperator L2) {
		TridiagonalOperator temp = L1;
	    L1.swap(L2);
	    L2.swap(temp);
	}

    public Array applyTo(Array v) {
    	if(v.size() != size())
    		throw new IllegalStateException("vector of the wrong size (" + v.size() + "instead of " + size() + ")");
    		
		Array result = this.diagonal;
		//multiply result values by (diagonal * v)
		result.operatorMultiply(v);
		
		 // matricial product
		 result.set(0, 
				 result.get(0) + (this.upperDiagonal.get(0)*v.get(1)));
		
		 for (int j=1; j<=size()-2; j++) {
		     result.set(j,
		    		 result.get(j) + (lowerDiagonal.get(j-1)*v.get(j-1)) 
		    		 + (upperDiagonal.get(j)*v.get(j+1)));
		 }

	     result.set(size()-1, 
	    		 result.get(size()-1) + (lowerDiagonal.get(size()-2)*v.get(size()-2)));
		
		 return result;
    }


    //! solve linear system for a given right-hand side
    public final Array solveFor(final Array rhs) {
	    Array result = new Array(size());
	    Array tmp = new Array(size());
	
	    double bet = diagonal.get(0);
	    
	    if(bet == 0.0)
	    	throw new IllegalStateException("division by zero");
	    
	    result.set(0, rhs.get(0)/bet);
	    int j;
	    for (j=1; j<=size()-1; j++){
	        tmp.set(j, upperDiagonal.get(j-1)/bet);
	        bet = diagonal.get(j) - lowerDiagonal.get(j-1) * tmp.get(j);
	        if(bet == 0.0)
	        	throw new IllegalStateException("division by zero");
	        result.set(j, (rhs.get(j) - lowerDiagonal.get(j-1)*result.get(j-1)) / bet);
	    }
	    
	    // cannot be j>=0 with Size j
	    for(j=size()-2; j>0; --j)
	    	result.set(j, result.get(j) - (tmp.get(j+1)*result.get(j+1)));
	
	    result.set(0, result.get(0) - (tmp.get(1)*result.get(1)));
	    return result;
    }
}
