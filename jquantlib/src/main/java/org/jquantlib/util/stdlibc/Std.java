/*
 Copyright (C) 2007 Richard Gomes

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

package org.jquantlib.util.stdlibc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.jquantlib.math.Array;
import org.jquantlib.math.E_IBinaryFunction;
import org.jquantlib.math.E_IUnaryFunction;
import org.jquantlib.math.E_UnaryFunction;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.functions.DoubleFunction;
import org.jquantlib.util.Date;

/**
 * @see <a href="http://javadude.com/articles/passbyvalue.htm">Java is
 *      Pass-by-Value, Dammit!</a>
 * 
 * @author Dominik Holenstein
 * @author Richard Gomes
 * @author Srinivas Hasti
 */

@SuppressWarnings("PMD.TooManyMethods")
public final class Std {

	//
	// static public methods
	//

	/**
	 * Java implementation of std::adjacent_difference
	 * <p>
	 * @param inputList
	 * @param begin
	 * @param diffList
	 * @return diffList
	 * @see <a href="http://acm.cs.uct.ac.za/docs/libstdc++-3.4/namespacestd.html#a558">Functional description: std::adjacent_difference</a> 
	 * @see <a href="http://acm.cs.uct.ac.za/docs/libstdc++-3.4/stl__numeric_8h-source.html#l00268">Source code: std::adjacent_difference</a> 
	 *
	 */
	// FIXME: this method needs code review
	@SuppressWarnings("PMD")
	static public List<Double> adjacent_difference(final List<Double> inputList,
			final int begin, List<Double> diffList) {
		for (int i = begin; i < inputList.size(); i++) {
			final double curr = inputList.get(i); 
			if (i == 0) {
				diffList.add(inputList.get(i));
			} else {
				final double prev = inputList.get(i - 1);
				diffList.add(curr - prev);
			}
		}
		return diffList;
	}


	public static void apply(final Array array, final DoubleFunction func) {
		apply(array, 0, array.size(), func);
	}

	public static void apply(final Array array, final int startIndex, final int endIndex, final DoubleFunction func) {
		for(int i=0; i<array.size(); i++){
        	array.set(i, func.apply(array.at(i)));
        }
	}
	
	public static<ParameterType, ReturnType> E_IUnaryFunction<ParameterType, ReturnType> 
	bind2nd(E_IBinaryFunction<ParameterType,  ReturnType> binaryFunction, ParameterType bounded){   
        E_IUnaryFunction<ParameterType, ReturnType> ret = new E_UnaryFunction<ParameterType, ReturnType>(){
            private E_IBinaryFunction<ParameterType, ReturnType> binary;
            private ParameterType bounded;
            @Override
            public ReturnType evaluate(ParameterType x) {
                return binary.evaluate(x, bounded);
            }
        };
        ret.setBinaryFunction(binaryFunction);
        ret.setBoundedValue(bounded);
        return ret;
	}
	
	
	public static<ParameterType, ReturnType> E_IUnaryFunction<ParameterType, ReturnType> 
	    bind1st(E_IBinaryFunction<ParameterType,  ReturnType> binaryFunction, ParameterType bounded){   
	        E_IUnaryFunction<ParameterType, ReturnType> ret = new E_UnaryFunction<ParameterType, ReturnType>(){
	            private E_IBinaryFunction<ParameterType, ReturnType> binary;
	            private ParameterType bounded;
	            @Override
	            public ReturnType evaluate(ParameterType x) {
	                return binary.evaluate(bounded, x);
	            }
	        };
	        ret.setBinaryFunction(binaryFunction);
	        ret.setBoundedValue(bounded);
	        return ret;
	    }
	
	   /**
     * Implements std::min_element
     * @param from
     * @param to
     * @param list
     * @return
     */
    
    public static double min_element(int from, int to, List<Double>  list){
        double value = Double.POSITIVE_INFINITY;
        for(double temp: list){
            if(temp<value){
                value = temp;
            }
        }
        return value;
    }
 
    public static double min_element(int from, int to, double []  list){
        double value = Double.POSITIVE_INFINITY;
        for(int i = from; i<to; i++){
            if(list[i]<value){
                value = list[i];
            }
        }
        return value;
    }
    
    public static double max_element(int from, int to, double []  list){
        double value = Double.NEGATIVE_INFINITY;
        for(int i = from; i<to; i++){
            if(list[i]>value){
                value = list[i];
            }
        }
        return value;
    }

	
    /**
     * Java implementation of std::lower_bound
     * <p>
     * This method signature is specific for <i>int</i> values.
     * Other method signatures may be available for other types.
     * <p> 
     * The original function signature is
     * <pre>
     * template<typename _ForwardIterator, typename _Tp>
     *  _ForwardIterator lower_bound(_ForwardIterator __first, _ForwardIterator __last, const _Tp& __val)
     * </pre>
     * <p>
     * In this implementation we provide as many as needed type-specific method signatures. In particular
     * we use an array intended to mimic the semantic of <i>elements located between __first and __last</i>.
     *  
     * @param list is an array of values which implicitly defines it's begin (zero) and end (it's own size)
     * @param val is the value to be located
     * @return see upper_bound doc
     * 
     * @see <a href="http://acm.cs.uct.ac.za/docs/libstdc++-3.4/stl__algo_8h-source.html#l02597">Source code: std::lower_bound</a> 
     */
    @SuppressWarnings("PMD.MethodNamingConventions")
    public static int lower_bound(final double[] list, final double val) {
        return lower_bound(list, 0, list.length-1, val);
    }

    @SuppressWarnings("PMD")
    private static int lower_bound(final double[] list, int first, int last, final double val) {
        int len = last - first;
        int half;
        int middle;
        
        while (len > 0) {
            half = len >> 1;
            middle = first;
            middle = middle + half;
            
            if (list[middle] < val) {
                first = middle;
                first++;
                len = len - half -1;
            } else {
                len = half;
            }
        }
        return first;
    }

    
	/**
	 * Java implementation of std::upper_bound
	 * <p>
	 * This method signature is specific for <i>int</i> values.
	 * Other method signatures may be available for other types.
	 * <p> 
	 * The original function signature is
	 * <pre>
	 * template<typename _ForwardIterator, typename _Tp>
     *  _ForwardIterator upper_bound(_ForwardIterator __first, _ForwardIterator __last, const _Tp& __val)
	 * </pre>
	 * <p>
	 * In this implementation we provide as many as needed type-specific method signatures. In particular
	 * we use an array intended to mimic the semantic of <i>elements located between __first and __last</i>.
	 *  
	 * @param list is an array of values which implicitly defines it's begin (zero) and end (it's own size)
	 * @param val is the value to be located
	 * @return see upper_bound doc
	 * 
	 * @see <a href="http://acm.cs.uct.ac.za/docs/libstdc++-3.4/stl__algo_8h-source.html#l02699">Source code: std::upper_bound</a> 
	 */
	@SuppressWarnings("PMD.MethodNamingConventions")
	public static int upper_bound(final double[] list, final double val) {
		return upper_bound(list, 0, list.length-1, val);
	}

	@SuppressWarnings("PMD")
	private static int upper_bound(final double[] list, int first, int last, final double val) {
		int len = last - first;
		int half;
		int middle;
		
		while (len > 0) {
			half = len >> 1;
			middle = first;
			middle = middle + half;
			
			if (val < list[middle]){
				len = half;
			} else {
				first = middle;
				first++;
				len = len - half -1;
			}
		}
		return first;
	}

	
	//TODO: needs code review
	public static final void transform(final Array array, final Array result, final UnaryFunctionDouble func) {
		double[] a = array.getData();
		double[] r = result.getData();
		
		for (int i=0; i<a.length; i++) {
			r[i] = func.evaluate(a[i]);
		}
	}
	
	
	
	
	//
	// static public methods ::: unmutable iterators
	//

	static public IntForwardIterator forwardIterator(final int[] list) {
		return new IntForwardIteratorImpl(list);
	}

	static public IntReverseIterator reverseIterator(final int[] list) {
		return new IntReverseIteratorImpl(list);
	}

	static public DoubleForwardIterator forwardIterator(final double[] list) {
		return new DoubleForwardIteratorImpl(list);
	}

	static public DoubleReverseIterator reverseIterator(final double[] list) {
		return new DoubleReverseIteratorImpl(list);
	}

	static public ObjectForwardIterator forwardIterator(final Object[] list) {
		return new ObjectForwardIteratorImpl(list);
	}

	
	//
	// static public methods ::: mutable iterators
	//

	static public MutableIntForwardIterator mutableForwardIterator(final int[] list) {
		return new MutableIntForwardIteratorImpl(list);
	}

	static public MutableIntReverseIterator mutableReverseIterator(final int[] list) {
		return new MutableIntReverseIteratorImpl(list);
	}

	static public MutableDoubleForwardIterator mutableForwardIterator(final double[] list) {
		return new MutableDoubleForwardIteratorImpl(list);
	}

	static public MutableDoubleReverseIterator mutableReverseIterator(final double[] list) {
		return new MutableDoubleReverseIteratorImpl(list);
	}

	
	// 
	// static private abstract inner classes
	//
	@SuppressWarnings("PMD.AbstractNaming")
	static private abstract class ForwardIteratorImpl implements ForwardIterator {
		private final int size;
		protected int index;

		protected ForwardIteratorImpl(final int size) {
			this.size = size;
			this.index = 0;
		}

		@Override
		public boolean hasNext() {
			return (index < size);
		}

		@Override
		@SuppressWarnings("PMD.AvoidFinalLocalVariable")
		public int skip(final int skipsize) {
			final int jump = (index + skipsize < size) ? skipsize : (size - index - 1);
			index += jump;
			return jump;
		}
	}

	@SuppressWarnings("PMD.AbstractNaming")
	static private abstract class ReverseIteratorImpl implements ReverseIterator {
		protected int index;

		protected ReverseIteratorImpl(final int size) {
			this.index = size - 1;
		}

		@Override
		public boolean hasPrevious() {
			return (index >= 0);
		}

		@Override
		@SuppressWarnings("PMD.AvoidFinalLocalVariable")
		public int back(final int backstep) {
			final int jump = (index - backstep >= 0) ? backstep : index;
			index -= jump;
			return jump;
		}
	}

	
	// 
	// static private inner classes
	//
	// These classes implement unmutable iterators
	//

	static private class IntForwardIteratorImpl extends ForwardIteratorImpl 
			implements IntForwardIterator {

		protected int[] list;

		public IntForwardIteratorImpl(final int[] list) {
			super((list == null) ? 0 : list.length);
			this.list = list;
		}

		@Override
		public int next() throws NoSuchElementException {
			if (hasNext())
				return list[index++];
			throw new NoSuchElementException();
		}
	}

	static private class IntReverseIteratorImpl extends ReverseIteratorImpl
			implements IntReverseIterator {

		protected int[] list;

		public IntReverseIteratorImpl(final int[] list) {
			super((list == null) ? 0 : list.length);
			this.list = list;
		}

		@Override
		public int previous() throws NoSuchElementException {
			if (hasPrevious())
				return list[index++];
			throw new NoSuchElementException();
		}
	}

	static private class DoubleForwardIteratorImpl extends ForwardIteratorImpl
			implements DoubleForwardIterator {

		protected double[] list;

		public DoubleForwardIteratorImpl(final double[] list) {
			super((list == null) ? 0 : list.length);
			this.list = list;
		}

		@Override
		public double next() throws NoSuchElementException {
			if (hasNext())
				return list[index++];
			throw new NoSuchElementException();
		}

	}

	static private class DoubleReverseIteratorImpl extends ReverseIteratorImpl
			implements DoubleReverseIterator {

		protected double[] list;

		public DoubleReverseIteratorImpl(final double[] list) {
			super((list == null) ? 0 : list.length);
			this.list = list;
		}

		@Override
		public double previous() throws NoSuchElementException {
			if (hasPrevious())
				return list[index++];
			throw new NoSuchElementException();
		}

	}

	static private class ObjectForwardIteratorImpl extends ForwardIteratorImpl
			implements ObjectForwardIterator {

		protected Object[] list;

		public ObjectForwardIteratorImpl(final Object[] list) {
			super((list == null) ? 0 : list.length);
			this.list = list;
		}

		@Override
		public Object next() throws NoSuchElementException {
			if (hasNext())
				return list[index++]; // FIXME :: make a clone
			throw new NoSuchElementException();
		}

	}

	
	// 
	// static private inner classes
	//
	// These classes implement mutable iterators
	//

	static private class MutableIntForwardIteratorImpl extends 
			IntForwardIteratorImpl implements MutableIntForwardIterator {

		public MutableIntForwardIteratorImpl(final int[] list) {
			super(list);
		}

		@Override
		public IntReference nextReference() throws NoSuchElementException {
			return new IntReference(list, index);
		}

	}

	static private class MutableIntReverseIteratorImpl extends
			IntReverseIteratorImpl implements MutableIntReverseIterator {

		public MutableIntReverseIteratorImpl(final int[] list) {
			super(list);
		}

		@Override
		public IntReference previousReference() throws NoSuchElementException {
			return new IntReference(list, index);
		}

	}

	static private class MutableDoubleForwardIteratorImpl extends
			DoubleForwardIteratorImpl implements MutableDoubleForwardIterator {

		public MutableDoubleForwardIteratorImpl(final double[] list) {
			super(list);
		}

		@Override
		public DoubleReference nextReference() throws NoSuchElementException {
			return new DoubleReference(list, index);
		}

	}

	static private class MutableDoubleReverseIteratorImpl extends
			DoubleReverseIteratorImpl implements MutableDoubleReverseIterator {

		public MutableDoubleReverseIteratorImpl(final double[] list) {
			super(list);
		}

		@Override
		public DoubleReference previousReference()
				throws NoSuchElementException {
			return new DoubleReference(list, index);
		}
	}
	
	public static double accumulate(int start, int end, double [] list, double init){
	    double sum = init;
	    for(int i = start; i<end; i++){
	        sum += list[i];
	    }
	    return sum;
	}
	
	public static double accumulate(double [] list, double init){
	    return accumulate(0, list.length, list, init);
	}
	
	public static <T extends Comparable<T>> T min(T ... t){
	    List<T> t_ = Arrays.asList(t);
	    Collections.sort(t_);
	    return  t_.get(0);
	}
	
	public static <T extends Comparable<T>> T max(T... t) {
        List<T> t_ = Arrays.asList(t);
        Collections.sort(t_);
        return t_.get(t_.size() - 1);
    }
}
