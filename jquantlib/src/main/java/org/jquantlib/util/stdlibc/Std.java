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

import org.jquantlib.math.Array;
import org.jquantlib.math.E_IBinaryFunction;
import org.jquantlib.math.E_IUnaryFunction;
import org.jquantlib.math.E_UnaryFunction;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.functions.DoubleFunction;

/**
 * Mimics library libstdc++ from C++ language which exposes top level functions to <code>std:: namespace</code>
 * 
 * @note all fields, methods and inner classes are static
 * 
 * @see <a http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/index.html">libstdc++ Source Documentation</a>
 * @see <a href="http://javadude.com/articles/passbyvalue.htm">Java is Pass-by-Value, Dammit!</a>
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
     * Return differences between adjacent values. 
     * 
     * @note Mimics std::adjacent_difference
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#d7df62eaf265ba5c859998b1673fd427">std::adjacent_difference</a>
     */
    @SuppressWarnings("PMD")
    static public final double[] adjacent_difference(final double[] array) {
        return adjacent_difference(array, 0);
    }
    
    /**
     * Return differences between adjacent values. 
     * 
     * @note Mimics std::adjacent_difference
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#d7df62eaf265ba5c859998b1673fd427">std::adjacent_difference</a>
     */
    @SuppressWarnings("PMD")
    static public final double[] adjacent_difference(final double[] array, final int from) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();
        if (from>=array.length) throw new IndexOutOfBoundsException();
        
        final double[] diff = new double[array.length-from];
        for (int i = from; i < array.length; i++) {
            final double curr = array[i]; 
            if (i == 0) {
                diff[i] = array[i];
            } else {
                final double prev = array[i - 1];
                diff[i] = curr - prev;
            }
        }
        return diff;
    }
    

    /**
	 * Apply all <i>typelist</i> types to generator functor. 
	 * 
	 * @note Mimics std::apply
	 * 
	 * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00960.html#c173ae39df0e242021655f0f02eb381a">__gnu_cxx::typelist Namespace Reference</a>
	 */
	public static void apply(final Array array, final DoubleFunction func) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null || func==null) throw new NullPointerException();
        
		apply(array, 0, array.size(), func);
	}


	/**
     * Apply all <i>typelist</i> types to generator functor. 
     * 
     * @note Mimics std::apply
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00960.html#c173ae39df0e242021655f0f02eb381a">__gnu_cxx::typelist Namespace Reference</a>
     */
	public static void apply(final Array array, final int from, final int to, final DoubleFunction func) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null || func==null) throw new NullPointerException();
        if (from>to || to>array.size()) throw new IndexOutOfBoundsException();
        
		for(int i=from; i<to; i++) {
        	array.set(i, func.apply(array.at(i)));
        }
	}
	

	//FIXME :: needs code review.
	// The use of E_IUnaryFunction and E_IBinaryFunction are pretty confusing.
	// It's not clear what these interfaces/classes do (where are the comments and references, etc?)
	// and, in particular, E_IBinaryFunction should have 3 generic arguments: 2 parameter types and 1 result type.
	public static <ParameterType, ReturnType> E_IUnaryFunction<ParameterType, ReturnType> bind1st(
            E_IBinaryFunction<ParameterType, ReturnType> binaryFunction, ParameterType bounded) {
        
        final E_IUnaryFunction<ParameterType, ReturnType> ret = new E_UnaryFunction<ParameterType, ReturnType>() {
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
    
    
    //FIXME :: needs code review.
    // The use of E_IUnaryFunction and E_IBinaryFunction are pretty confusing.
    // It's not clear what these interfaces/classes do (where are the comments and references, etc?)
    // and, in particular, E_IBinaryFunction should have 3 generic arguments: 2 parameter types and 1 result type.
	public static <ParameterType, ReturnType> E_IUnaryFunction<ParameterType, ReturnType> bind2nd(
            E_IBinaryFunction<ParameterType, ReturnType> binaryFunction, ParameterType bounded) {
        
        final E_IUnaryFunction<ParameterType, ReturnType> ret = new E_UnaryFunction<ParameterType, ReturnType>() {
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
	
	
	/**
     * Return the minimum element in a range using comparison functor. 
     * 
     * @note Mimics std::min_element
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01014.html#g09af772609c56f01dd33891d51340baf">std::min_element</a>
     * 
     * @deprecated
     */
    public static double min_element(final int from, final int to, final List<Double> array) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();
        if (from>to || to>=array.size()) throw new IndexOutOfBoundsException();

        double value = Double.POSITIVE_INFINITY;
        for (int i=from; i<to; i++) {
            double temp = array.get(i); 
            if (temp < value) value = temp;
        }
        return value;
    }
 
    /**
     * Return the minimum element in a range using comparison functor. 
     * 
     * @note Mimics std::min_element
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01014.html#g09af772609c56f01dd33891d51340baf">std::min_element</a>
     */
    public static double min_element(final int from, final int to, final double []  array) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();
        if (from>to || to>=array.length) throw new IndexOutOfBoundsException();
        
        double value = Double.POSITIVE_INFINITY;
        for (int i = from; i<to; i++) {
            if (array[i] < value) value = array[i];
        }
        return value;
    }
    
    
    /**
     * Return the maximum element in a range using comparison functor. 
     * 
     * @note Mimics std::max_element
     * list
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01014.html#g595f12feaa16ea8aac6e5bd51782e123">std::max_element</a>
     *  
     * @deprecated
     */
    public static double max_element(final int from, final int to, final List<Double> array) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();
        if (from>to || to>=array.size()) throw new IndexOutOfBoundsException();
        
        double value = Double.NEGATIVE_INFINITY;
        for (int i=from; i<to; i++) {
            double temp = array.get(i); 
            if (temp > value) value = temp;
        }
        return value;
    }

    
    /**
     * Return the maximum element in a range using comparison functor. 
     * 
     * @note Mimics std::max_element
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01014.html#g595f12feaa16ea8aac6e5bd51782e123">std::max_element</a> 
     */
    public static double max_element(final int from, final int to, double [] array) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();
        if (from>to || to>=array.length) throw new IndexOutOfBoundsException();
        
        double value = Double.NEGATIVE_INFINITY;
        for(int i = from; i<to; i++){
            if (array[i] > value) value = array[i];
        }
        return value;
    }

	
    /**
     * Finds the first position in which val could be inserted without changing the ordering.
     * 
     * @note Mimics std::lower_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g0ff3b53e875d75731ff8361958fac68f">std::lower_bound</a>
     */
    @SuppressWarnings("PMD.MethodNamingConventions")
    public static int lower_bound(final double[] array, final double val) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();

        return lower_bound(array, 0, array.length-1, val);
    }


    /**
     * Finds the first position in which val could be inserted without changing the ordering.
     * 
     * @note Mimics std::lower_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g0ff3b53e875d75731ff8361958fac68f">std::lower_bound</a>
     */
    @SuppressWarnings("PMD")
    private static int lower_bound(final double[] array, int from, int to, final double val) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();
        if (from>to || to>=array.length) throw new IndexOutOfBoundsException();

        int len = to - from;
        int half;
        int middle;
        
        while (len > 0) {
            half = len >> 1;
            middle = from;
            middle = middle + half;
            
            if (array[middle] < val) {
                from = middle;
                from++;
                len = len - half -1;
            } else {
                len = half;
            }
        }
        return from;
    }

    
    /**
     * Finds the first position in which val could be inserted without changing the ordering.
     * 
     * @note Mimics std::lower_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g0ff3b53e875d75731ff8361958fac68f">std::lower_bound</a>
     */
	@SuppressWarnings("PMD.MethodNamingConventions")
	public static int upper_bound(final double[] array, final double val) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();

		return upper_bound(array, 0, array.length-1, val);
	}

	
	/**
	 * Finds the last position in which val could be inserted without changing the ordering. 
	 * 
     * @note Mimics std::upper_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g9bf525d5276b91ff6441e27386034a75">std::upper_bound</a>
	 */
	@SuppressWarnings("PMD.MethodNamingConventions")
	public static int upper_bound(final Double[] array, final double val) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();

		double[] d = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			d[i] = array[i];
		}
		return upper_bound(d, 0, array.length - 1, val);
	}	


	/**
     * Finds the last position in which val could be inserted without changing the ordering. 
     * 
     * @note Mimics std::upper_bound
     *
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01016.html#g9bf525d5276b91ff6441e27386034a75">std::upper_bound</a>
     */
	@SuppressWarnings("PMD.MethodNamingConventions")
	private static int upper_bound(final double[] array, int from, int to, final double val) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();
        if (from>to || to>=array.length) throw new IndexOutOfBoundsException();

		int len = to - from;
		int half;
		int middle;
		
		while (len > 0) {
			half = len >> 1;
			middle = from;
			middle = middle + half;
			
			if (val < array[middle]){
				len = half;
			} else {
				from = middle;
				from++;
				len = len - half -1;
			}
		}
		return from;
	}

	
	/**
	 * Perform an operation on corresponding elements of two sequences. 
	 * 
	 * @note Mimics std::transform
	 * 
	 * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01012.html#gaf771a08ae2322b42640bb14fc342c5d">std::transform</a>
	 */
	public static final void transform(final Array array, final Array result, final UnaryFunctionDouble func) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null || result==null || func==null) throw new NullPointerException();

		double[] a = array.getData();
		double[] r = result.getData();
		
		for (int i=0; i<a.length; i++) {
			r[i] = func.evaluate(a[i]);
		}
	}
	

	/**
     * Perform an operation on corresponding elements of two sequences. 
     * 
     * @note Mimics std::transform
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01012.html#gaf771a08ae2322b42640bb14fc342c5d">std::transform</a>
     */
	public static final<ParameterType, ReturnType>
	        void transform(final double[] array, final double[] result, final E_UnaryFunction<Double, Double> func) {

	    // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null || result==null || func==null) throw new NullPointerException();
	    
        for(int i=0; i<array.length; i++){
            result[i] = func.evaluate(array[i]);
        }
    }

	
    /**
     * Return the minimum element in a range.
     * 
     * @note Mimics std::min
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01014.html#g49f0c87cb0e1bf950f5c2d49aa106573">std::min</a>
     */
	// TODO: consider the parallel version of std::min (probably implementing in class GnuParallel)
	// http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00964.html#0d0e5aa5b83e8ffa90d57714f03d73bf
    public static <T extends Comparable<T>> T min(T... t) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (t==null) throw new NullPointerException();

        List<T> list = Arrays.asList(t);
        Collections.sort(list);
        return list.get(0);
    }

    
    /**
     * Return the maximum element in a range.
     * 
     * @note Mimics std::max
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a01014.html#gacf2fd7d602b70d56279425df06bd02c">std::max</a>
     */
    // TODO: consider the parallel version of std::max (probably implementing in class GnuParallel)
    // http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00964.html#992b78d1946c7c02e46bc3509637f12d
    public static <T extends Comparable<T>> T max(T... t) {
        
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (t==null) throw new NullPointerException();
        
        List<T> list = Arrays.asList(t);
        Collections.sort(list);
        return list.get(list.size() - 1);
    }


    /**
     * Accumulate values in a range. 
     * 
     * @note Mimics std::accumulate
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#3e6040dba097b64311fce39fa87d1b29">std::accumulate</a>
     */
    public static double accumulate(final int from, final int to, final double[] array, final double init) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();
        if (from>to || to>=array.length) throw new IndexOutOfBoundsException();

        double sum = init;
        for (int i = from; i < to; i++) {
            sum += array[i];
        }
        return sum;
    }

    
    /**
     * Accumulate values in a range. 
     * 
     * @note Mimics std::accumulate
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#3e6040dba097b64311fce39fa87d1b29">std::accumulate</a>
     */
    public static double accumulate(final double[] array, final double init) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();

        return accumulate(0, array.length, array, init);
    }

    
    /**
     * Accumulate values in a range. 
     * 
     * @note Mimics std::accumulate
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#3e6040dba097b64311fce39fa87d1b29">std::accumulate</a>
     * 
     * @deprecated
     */
    public static double accumulate(final List<Double> array, final double init) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (array==null) throw new NullPointerException();

        double sum = 0.0;
        for (int i = 0; i < array.size(); i++) {
            sum += array.get(i);
        }
        return sum;
    }


    // FIXME: This method must be refactored.
    // There's no such "std::multiplies"
    // This class returns an object which is extended from an abstract class from math package, which
    // is something weird because 2 very related concepts which could not be apart are located in
    // two very different places.
    public static E_UnaryFunction<Double, Double> multiplies(double multiplier) {
        
        E_UnaryFunction<Double, Double> ret = new E_UnaryFunction<Double, Double>() {
            @Override
            public Double evaluate(Double x) {
                return x * params[0];
            }
        };
        ret.setParams(multiplier);
        return ret;
    }

    
    /**
     * Compute inner product of two ranges. 
     * 
     * @note Mimics std::inner_product
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#50185519487fc7981622fde2df2b78da">std::inner_product</a>
     */
    public static double inner_product(final Array arrayA, final Array arrayB) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (arrayA==null || arrayB==null) throw new NullPointerException();

        return inner_product(arrayA.getData(), arrayB.getData(), 0.0);
    }

    
    /**
     * Compute inner product of two ranges. 
     * 
     * @note Mimics std::inner_product
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#50185519487fc7981622fde2df2b78da">std::inner_product</a>
     */
    public static double inner_product(final Array arrayA, final Array arrayB, final double init) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (arrayA==null || arrayB==null) throw new NullPointerException();

        return inner_product(arrayA.getData(), arrayB.getData(), init);
    }

    
    /**
     * Compute inner product of two ranges. 
     * 
     * @note Mimics std::inner_product
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#50185519487fc7981622fde2df2b78da">std::inner_product</a>
     */
    public static double inner_product(final Array arrayA, final int fromA, final Array arrayB, final int fromB, final int length, final double init) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (arrayA==null || arrayB==null) throw new NullPointerException();
        if (fromA<0 || fromA>=arrayA.size() || fromA<0 || fromA>=arrayA.size()) throw new IllegalArgumentException();
        if (fromA+length>=arrayA.size() || fromB+length>=arrayB.size()) throw new IllegalArgumentException();

        return inner_product(arrayA.getData(), fromA, arrayB.getData(), fromB, length, init);
    }
    
    
    /**
     * Compute inner product of two ranges. 
     * 
     * @note Mimics std::inner_product
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#50185519487fc7981622fde2df2b78da">std::inner_product</a>
     */
    public static double inner_product(final double[] arrayA, final double[] arrayB, final double init) {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (arrayA==null || arrayB==null) throw new NullPointerException();
        if (arrayA.length!=arrayB.length) throw new IllegalArgumentException();

        double innerProduct = init;
        for (int i = 0; i < arrayA.length; i++) {
            innerProduct += arrayA[i] * arrayB[i];
        }
        return innerProduct;
    }

    
    /**
     * Compute inner product of two ranges. 
     * 
     * @note Mimics std::inner_product
     * 
     * @see <a href="http://gcc.gnu.org/onlinedocs/libstdc++/latest-doxygen/a00969.html#50185519487fc7981622fde2df2b78da">std::inner_product</a>
     */
    public static double inner_product(
            final double[] arrayA, int fromA, 
            final double[] arrayB, int fromB, 
            final int length, final double init) {
        
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (arrayA==null || arrayB==null) throw new NullPointerException();
        if (fromA<0 || fromA>=arrayA.length || fromA<0 || fromA>=arrayA.length) throw new IllegalArgumentException();
        if (fromA+length>=arrayA.length || fromB+length>=arrayB.length) throw new IllegalArgumentException();

        double innerProduct = init;
        for (int i = 0; i < length; i++) {
            innerProduct += arrayA[fromA] * arrayB[fromB];
            fromA++;
            fromB++;
        }
        return innerProduct;
    }
    
}
