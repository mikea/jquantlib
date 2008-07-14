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
public class TridiagonalOperator {

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
		if (udiag.size() == diag.size() - 1)
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
    
    //public TridiagonalOperator operator=(const Disposable<TridiagonalOperator>&);
	//TODO: implement?

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
    	//TODO: not sure if this is the right way to subtract
    	//TODO: test this assisgnment
        Array low = this.lowerDiagonal.quickOperatorSubtractCopy(D.lowerDiagonal()); 
        Array mid = this.diagonal.quickOperatorSubtractCopy(D.diagonal());
        Array high = this.upperDiagonal.quickOperatorSubtractCopy(D.upperDiagonal()); 
        
        return new TridiagonalOperator(low,mid,high);

    }
    
    // binary operators
    public TridiagonalOperator add(final TridiagonalOperator D1, final TridiagonalOperator D2) {

    	//TODO: test this assisgnment
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
    
    //Disposable<Array> applyTo(const Array& v) const;
    public final Array applyTo() {
    	//TODO: figure out return type
    	//TODO: implement
    	return null;
    }
    
    //! solve linear system for a given right-hand side
    public final Array solveFor(final double[] rhs) {
    	//TODO: implement
    	return null;
    }
    
    //! solve linear system with SOR approach
    public final Array SOR(double[] rhs, int tol) {
    	//TODO: implement
    	return null;
    }
    
    //! identity instance
    public static TridiagonalOperator identity(int size) {
    	//TODO: implement
    	return null;
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

}
