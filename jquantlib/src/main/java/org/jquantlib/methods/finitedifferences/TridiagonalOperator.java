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

/**
 * @author Srinivas Hasti
 * @author Tim Swetonic
 */
public class TridiagonalOperator {

	public static interface TimeSetter {
		public void setTime(double t, TridiagonalOperator l);
	}
 
	protected TimeSetter timeSetter;
	protected double lowerDiagonal[];
	protected double diagonal[];
	protected double upperDiagonal[];

	public TridiagonalOperator(int size) {
		if (size >= 2) {
			this.lowerDiagonal = new double[size - 1];
			this.diagonal = new double[size];
			this.upperDiagonal = new double[size - 1];
		} else if (size == 0) {
			this.lowerDiagonal = new double[0];
			this.diagonal = new double[0];
			this.upperDiagonal = new double[0];
		} else {
			throw new IllegalStateException("Invalid size " + size);
		}

	}

	public TridiagonalOperator(double[] ldiag, double[] diag, double[] udiag) {
		if (ldiag.length != diag.length - 1)
			throw new IllegalStateException("wrong size for lower diagonal");
		if (udiag.length == diag.length - 1)
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
       diagonal[0]      = b;
       upperDiagonal[0] = c;
 	}

	public void setMidRow(int size, double a, double b, double c) {
		if(size >= 1 && size <= size()-2)
			throw new IllegalStateException("out of range in setMidRow");
		
		lowerDiagonal[size-1] = a;
		diagonal[size]        = b;
		upperDiagonal[size]   = c;
	}

	public void setMidRows(double a, double b, double c) {
        for (int i = 1; i <= size()-2; i++) {
            lowerDiagonal[i-1] = a;
            diagonal[i]        = b;
            upperDiagonal[i]   = c;
        }
	}

	public void setLastRow(double a, double b) {
        lowerDiagonal[size() - 2] = a;
        diagonal[size() -1 ]      = b;
	}

	public void setTime(double t) {
        if(timeSetter != null) {
        	timeSetter.setTime(t, this);
        }
	}
	
    public int size()  {
        return diagonal.length;
    }


    //default scope in c++? public?
    // unary operators
    public TridiagonalOperator add(final TridiagonalOperator D) {
	    TridiagonalOperator D1 = D;
	    return D1;
    }
    
    public TridiagonalOperator subtract(final TridiagonalOperator D) {
    	//TODO: not sure if this is the right way to subtract
    	
    	double[] low = this.lowerDiagonal;
        for(int i = 0; i < low.length; i++) {
        	low[i] -= D.lowerDiagonal()[i]; 
        }

        double[] mid = this.diagonal;
        for(int i = 0; i < mid.length; i++) {
        	mid[i] -= D.diagonal()[i]; 
        }

        double[] high = this.upperDiagonal;
        for(int i = 0; i < high.length; i++) {
        	high[i] -= D.upperDiagonal()[i]; 
        }
        
        return new TridiagonalOperator(low,mid,high);

    }
    
    // binary operators
    public TridiagonalOperator add(final TridiagonalOperator D1, final TridiagonalOperator D2) {
    	double[] low = D1.lowerDiagonal();
        for(int i = 0; i < low.length; i++) {
        	low[i] += D2.lowerDiagonal()[i]; 
        }

        double[] mid = D1.diagonal();
        for(int i = 0; i < mid.length; i++) {
        	mid[i] += D2.diagonal()[i]; 
        }

        double[] high = D1.upperDiagonal();
        for(int i = 0; i < high.length; i++) {
        	high[i] += D2.upperDiagonal()[i]; 
        }
        
        return new TridiagonalOperator(low,mid,high);
    }
    
    public TridiagonalOperator subtract(final  TridiagonalOperator D1, final TridiagonalOperator D2) {
    	double[] low = D1.lowerDiagonal();
        for(int i = 0; i < low.length; i++) {
        	low[i] -= D2.lowerDiagonal()[i]; 
        }

        double[] mid = D1.diagonal();
        for(int i = 0; i < mid.length; i++) {
        	mid[i] -= D2.diagonal()[i]; 
        }

        double[] high = D1.upperDiagonal();
        for(int i = 0; i < high.length; i++) {
        	high[i] -= D2.upperDiagonal()[i]; 
        }
        
        return new TridiagonalOperator(low,mid,high);
    }
    
    public TridiagonalOperator multiply(double a, final TridiagonalOperator D) {
        double[] low = D.lowerDiagonal();
        for(int i = 0; i < low.length; i++) {
        	low[i] *= a;
        }

        double[] mid = D.diagonal();
        for(int i = 0; i < mid.length; i++) {
        	mid[i] *= a;
        }

        double[] high = D.upperDiagonal();
        for(int i = 0; i < high.length; i++) {
        	high[i] *= a;
        }
	    
        return new TridiagonalOperator(low,mid,high);

    }
    
    public TridiagonalOperator multiply(final TridiagonalOperator D, double a) {
    	return multiply(a, D);
    }

    public TridiagonalOperator divide(final TridiagonalOperator D, double a) {
        double[] low = D.lowerDiagonal();
        for(int i = 0; i < low.length; i++) {
        	low[i] /= a;
        }

        double[] mid = D.diagonal();
        for(int i = 0; i < mid.length; i++) {
        	mid[i] /= a;
        }

        double[] high = D.upperDiagonal();
        for(int i = 0; i < high.length; i++) {
        	high[i] /= a;
        }
	    
        return new TridiagonalOperator(low,mid,high);

    }
    	
  //public:
  //  typedef Array array_type;
    
    //! \name Operator interface
    //@{
    //! apply operator to a given array
    
    //Disposable<Array> applyTo(const Array& v) const;
    public final double[] applyTo() {
    	//TODO: figure out return type
    	//TODO: implement
    	return null;
    }
    
    //! solve linear system for a given right-hand side
    public final double[] solveFor(final double[] rhs) {
    	//TODO: implement
    	return null;
    }
    
    //! solve linear system with SOR approach
    public final double[] SOR(double[] rhs, int tol) {
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
	
	public final double[] lowerDiagonal() {
	    return lowerDiagonal;
	}
	
	public final double[] diagonal() {
	    return diagonal;
	}
	
	public final double[] upperDiagonal() {
	    return upperDiagonal;
	}
	
	
	public void swap(TridiagonalOperator from) {
		this.diagonal = from.diagonal();
	    this.lowerDiagonal = from.lowerDiagonal();
	    this.upperDiagonal = from.upperDiagonal();
	    this.timeSetter = from.getTimeSetter();
	}

	public TimeSetter getTimeSetter() {
		return this.timeSetter;
	}

	public void swap(TridiagonalOperator L1, TridiagonalOperator L2) {
		TridiagonalOperator temp = L1;
	    L1 = L2;
	    L2 = temp;
	}

}
