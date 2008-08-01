/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

/*
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006 StatPro Italia srl
 Copyright (C) 2004 Ferdinando Ametrano

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */

package org.jquantlib.math;

import org.jquantlib.util.stdlibc.DoubleForwardIterator;
import org.jquantlib.util.stdlibc.DoubleReference;
import org.jquantlib.util.stdlibc.DoubleReverseIterator;
import org.jquantlib.util.stdlibc.MutableDoubleForwardIterator;
import org.jquantlib.util.stdlibc.MutableDoubleReverseIterator;
import org.jquantlib.util.stdlibc.Std;

/**
 * 1-D array used in linear algebra.
 * <p>
 * This class implements the concept of vector as used in linear algebra. As such, it is <b>not</b> meant to be used as a container
 * java.util.List should be used instead.
 * 
 * @author Richard Gomes
 * @author Q.Boiler
 */
//TEST construction of arrays is checked in a number of cases
//PERFORMANCE:: This class is a good candidate to become an OSGi bundle
//FIXME code review
public class Array {
    
    //
    // private fields
    //
    
    private double[] data;
    private int size;

    //  
    // public constructors
    //

    public Array(int s) {
        this.data = new double[s];
        this.size = s;
    }

    public Array(int s, double value) {
        this(s);
        for (int i = 0; i < s; ++i) {
            data[i] = value;
        }
    }

    /**
     * Creates the array and fills it according to \f$ a_{0} = value, a_{i}=a_{i-1}+increment \f$
     */
    public Array(int s, double value, double increment) {
        this(s);
        for (int i = 0; i < size; ++i) {
            data[i] = i * increment;
        }
    }

    public Array(double[] d) {
        this.data = d;
        this.size = d.length;
    }

    //
    
    // overridden public methods
    //
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(data.toString());
        return sb.toString();
    }

    //
    // public methods
    //
    
    /**
     * Calculates the dotProduct of vectorA and vectorB.
     * 
     * @throws Exception if vectorA or vectorB is null, or if vectorA.length is not equal to vectorB.length.
     */
    public static double dotProduct(final Array vectorA, final Array vectorB) {
        return dotProduct(vectorA.data, vectorB.data);
    }

    /**
     * Calulates the dotProduct of vectorA, and vectorB.
     * 
     * @throws Exception if vectorA or vectorB is null, or if vectorA.length is not equal to vectorB.length.
     */
    public static double dotProduct(final double[] vectorA, final double[] vectorB) {

        // Done as a local calc.
        if (vectorA != null && vectorB != null && vectorA.length == vectorB.length) {
            return quickDotProduct(vectorA, vectorB);
        } else {
            // TODO make this a JQuantLib Specific Checked Exception.
            throw new RuntimeException("VectorA and VectorB must both be non-null and the same length.");
        }

    }

    /**
     * If both arrays are null true will be returned, this may be confusing. if both are identical false is returned.
     * 
     * @param paramArray
     * @return
     */
    public boolean operatorNotEquals(final Array paramArray) {
        return !operatorEquals(paramArray);
    }

    public boolean operatorEquals(final Array paramArray) {
        if (this.data == null || paramArray == null || paramArray.data == null) {
            return false;
        } else if (data.length != paramArray.data.length) {
            return false;
        } else {
            return operatorEquals(paramArray.data);
        }
    }

    /**
     * returns true or false or throws an exception. NO BOUNDS CHECKING OR NULL CHECKING is done here.
     * 
     * @param paramData
     * @return
     */
    public boolean operatorEquals(final double[] paramData) {
        for (int i = 0; i < data.length; ++i) {
            if (data[i] != paramData[i]) {
                return false;
            }
        }
        return true;
    }

    //
    // Methods and Operators on this instance.
    //
    public void operatorDivide(final double scale) {
        data = quickOperatorDivideReplace(data, scale);
    }

    public void operatorMultiply(final double scale) {
        data = quickOperatorMultiplyReplace(data, scale);
    }

    public void operatorSubtract(final double scale) {
        data = quickOperatorSubtractReplace(data, scale);
    }

    public void operatorAdd(final double scale) {
        data = quickOperatorAddReplace(data, scale);
    }

    public void operatorDivide(final Array paramArray) {
        vectorOperationValidation(paramArray);
        data = quickOperatorDivideReplace(data, paramArray.data);
    }

    public void operatorMultiply(final Array paramArray) {
        vectorOperationValidation(paramArray);
        data = quickOperatorMultiplyReplace(data, paramArray.data);
    }

    public void operatorSubtract(final Array paramArray) throws Exception {
        vectorOperationValidation(paramArray);
        data = quickOperatorSubtractReplace(data, paramArray.data);
    }

    public void operatorAdd(final Array paramArray) {
        vectorOperationValidation(paramArray);
        data = quickOperatorAddReplace(data, paramArray.data);
    }

    public Array operatorDivideCopy(final Array paramArray) {
        vectorOperationValidation(paramArray);
        return quickOperatorDivideCopy(paramArray);
    }

    public Array operatorMultiplyCopy(final Array paramArray) {
        vectorOperationValidation(paramArray);
        return quickOperatorMultiplyCopy(paramArray);
    }

    public Array operatorSubtractCopy(final Array paramArray) {
        vectorOperationValidation(paramArray);
        return quickOperatorSubtractCopy(paramArray);
    }

    public Array operatorAddCopy(final Array paramArray) {
        vectorOperationValidation(paramArray);
        return quickOperatorAddCopy(paramArray);
    }

    public boolean empty() /* @ReadOnly */{
        return size == 0;
    }

    public int size() /* @ReadOnly */ {
        return data.length;
    }

    public void set(int index, double value) {
        if (data.length > index) {
            data[index] = value;
        } else {
            throw new RuntimeException("Illegal Argument, index must be less than the Array size.");
        }
    }

    public static void swap(final Array vectorA, final Array vectorB) {
        vectorOperationValidation(vectorA, vectorB);
        double storage;
        double[] dataA = vectorA.data;
        double[] dataB = vectorB.data;
        for (int i = 0; i < dataA.length; ++i) {
            storage = dataA[i];
            dataA[i] = dataB[i];
            dataB[i] = storage;
        }
    }

    public static void shallowSwap(final Array vectorA, final Array vectorB) {
        double[] swapArray = vectorA.data;
        vectorA.data = vectorB.data;
        vectorB.data = swapArray;
    }

    public void swap(final Array paramVector) {
        Array.swap(this, paramVector);
    }

    public void shallowSwap(final Array paramVector) {
        Array.shallowSwap(this, paramVector);
    }

    public Array absCopy() {
        double[] da = new double[data.length];
        for (int i = 0; i < data.length; ++i) {
            da[i] = Math.abs(data[i]);
        }
        return new Array(da);
    }

    public void abs() {
        for (int i = 0; i < data.length; ++i) {
            data[i] = Math.abs(data[i]);
        }
    }

    public Array sqrtCopy() {
        double[] da = new double[data.length];
        for (int i = 0; i < data.length; ++i) {
            da[i] = Math.sqrt(data[i]);
        }
        return new Array(da);
    }

    public void sqrt() {
        for (int i = 0; i < data.length; ++i) {
            data[i] = Math.sqrt(data[i]);
        }
    }

    public Array logCopy() {
        double[] da = new double[data.length];
        for (int i = 0; i < data.length; ++i) {
            da[i] = Math.log(data[i]);
        }
        return new Array(da);
    }

    public void log() {
        for (int i = 0; i < data.length; ++i) {
            data[i] = Math.log(data[i]);
        }
    }

    public Array expCopy() {
        double[] da = new double[data.length];
        for (int i = 0; i < data.length; ++i) {
            da[i] = Math.exp(data[i]);
        }
        return new Array(da);
    }

    public void exp() {
        for (int i = 0; i < data.length; ++i) {
            data[i] = Math.exp(data[i]);
        }
    }

    public DoubleForwardIterator forwardIterator() {
        return Std.forwardIterator(data);
    }

    public MutableDoubleForwardIterator mutableForwardIterator() {
        return Std.mutableForwardIterator(data);
    }

    public DoubleReverseIterator reverseIterator() {
        return Std.reverseIterator(data);
    }

    public MutableDoubleReverseIterator mutableReverseIterator() {
        return Std.mutableReverseIterator(data);
    }

    
    //
    // read-only versions of get, at, front and back
    //
    
    public double get(int i) { //XXX: candidate to be removed
        validateData(i);
        return data[i];
    }

    public double at(int i) {
        return get(i);
    }

    public double front() {
        return get(0);
    }

    public double back() {
        return get(size);
    }

    //
    // read-write versions of get, at, front and back
    //
    
    public DoubleReference getReference(int i) { //XXX: candidate to be removed
        validateData(i);
        return new DoubleReference(data, i);
    }

    public DoubleReference atReference(int i) {
        validateData(i);
        return new DoubleReference(data, i);
    }

    public DoubleReference frontReference() {
        validateData(0);
        return new DoubleReference(data, 0);
    }

    public DoubleReference backReference() {
        validateData(size);
        return new DoubleReference(data, size);
    }

    
    
    //
    // private methods
    //
    // These methods are optimised to operate directly on an array of primitive types
    //
    
    private void quickOperatorDivide(final Array paramArray) {
        data = quickOperatorDivideReplace(data, paramArray.data);
    }

    private void quickOperatorMultiply(final Array paramArray) {
        data = quickOperatorMultiplyReplace(data, paramArray.data);
    }

    private void quickOperatorSubtract(final Array paramArray) {
        data = quickOperatorSubtractReplace(data, paramArray.data);
    }

    private void quickOperatorAdd(final Array paramArray) {
        data = quickOperatorAddReplace(data, paramArray.data);
    }

    private Array quickOperatorDivideCopy(final Array paramArray) {
        double[] dataCopy = quickOperatorDivide(data, paramArray.data);
        return new Array(dataCopy);
    }

    private Array quickOperatorMultiplyCopy(final Array paramArray) {
        double[] dataCopy = quickOperatorMultiply(data, paramArray.data);
        return new Array(dataCopy);
    }

    private Array quickOperatorSubtractCopy(final Array paramArray) {
        double[] dataCopy = quickOperatorSubtract(data, paramArray.data);
        return new Array(dataCopy);
    }

    private Array quickOperatorAddCopy(final Array paramArray) {
        double[] dataCopy = quickOperatorAdd(data, paramArray.data);
        return new Array(dataCopy);
    }

    private void validateData(int s) {
        if (data == null || data.length < s + 1) {
            throw new RuntimeException("data is not properly conditioned.");
        }
    }

    private void quickSwap(final Array paramVector) {
        Array.quickSwap(this, paramVector);
    }

    private void vectorOperationValidation(final Array paramArray) {

        if (data == null) {
            throw new RuntimeException("the underlying array must not be null");
        } else if (paramArray == null) {
            throw new RuntimeException("the param array must not be null");
        } else if (paramArray.data == null) {
            throw new RuntimeException("the param array's underlying must not be null");
        } else if (data.length != paramArray.data.length) {
            throw new RuntimeException("the two arrays must be the same length");
        }
    }

    private static void vectorOperationValidation(final Array vectorA, final Array vectorB) {
        if (vectorA != null) {
            vectorA.vectorOperationValidation(vectorB);
        }
    }

    //
    // private static methods
    //
    
    /*
     * +,-,,/ Real Array
     */
    private static double[] quickOperatorAdd(final double[] vectorA, final double[] vectorB) {
        double[] outputData = new double[vectorA.length];
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] + vectorB[i];
        }
        return outputData;
    }

    private static double[] quickOperatorSubtract(final double[] vectorA, final double[] vectorB) {
        double[] outputData = new double[vectorA.length];
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] - vectorB[i];
        }
        return outputData;
    }

    private static double[] quickOperatorMultiply(final double[] vectorA, final double[] vectorB) {
        double[] outputData = new double[vectorA.length];
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] * vectorB[i];
        }
        return outputData;
    }

    private static double[] quickOperatorDivide(final double[] vectorA, final double[] vectorB) {
        double[] outputData = new double[vectorA.length];
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] / vectorB[i];
        }
        return outputData;
    }

    private static double[] quickOperatorAdd(final double[] vectorA, final double scale) {
        double[] outputData = new double[vectorA.length];
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] + scale;
        }
        return outputData;
    }

    private static double[] quickOperatorSubtract(final double[] vectorA, final double scale) {
        double[] outputData = new double[vectorA.length];
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] - scale;
        }
        return outputData;
    }

    private static double[] quickOperatorMultiply(final double[] vectorA, final double scale) {
        double[] outputData = new double[vectorA.length];
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] * scale;
        }
        return outputData;
    }

    private static double[] quickOperatorDivide(final double[] vectorA, final double scale) {
        double[] outputData = new double[vectorA.length];
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] / scale;
        }
        return outputData;
    }

    private static double[] quickOperatorAddReplace(final double[] vectorA, final double[] vectorB) {
        double[] outputData = vectorA;
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] + vectorB[i];
        }
        return outputData;
    }

    private static double[] quickOperatorSubtractReplace(final double[] vectorA, final double[] vectorB) {
        double[] outputData = vectorA;
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] - vectorB[i];
        }
        return outputData;
    }

    private static double[] quickOperatorMultiplyReplace(final double[] vectorA, final double[] vectorB) {
        double[] outputData = vectorA;
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] * vectorB[i];
        }
        return outputData;
    }

    private static double[] quickOperatorDivideReplace(final double[] vectorA, final double[] vectorB) {
        double[] outputData = vectorA;
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] / vectorB[i];
        }
        return outputData;
    }

    private static double[] quickOperatorAddReplace(final double[] vectorA, final double scale) {
        double[] outputData = vectorA;
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] + scale;
        }
        return outputData;
    }

    private static double[] quickOperatorSubtractReplace(final double[] vectorA, final double scale) {
        double[] outputData = vectorA;
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] - scale;
        }
        return outputData;
    }

    private static double[] quickOperatorMultiplyReplace(final double[] vectorA, final double scale) {
        double[] outputData = vectorA;
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] * scale;
        }
        return outputData;
    }

    private static double[] quickOperatorDivideReplace(final double[] vectorA, final double scale) {
        double[] outputData = vectorA;
        for (int i = 0; i < vectorA.length; ++i) {
            outputData[i] = vectorA[i] / scale;
        }
        return outputData;
    }

    /**
     * Calculates the dotProduct of vectorA and vectorB, No null checks or bounds checks are performed
     * 
     * @param vectorA has the precondition that it is a non-null double[] the same size as vectorB
     * @param vectorB has the precondition that it is a non-null double[] the same size as vectorA
     * @return the dotProduct/InnerProduct of vectorA and vectorB.
     */
    private static double quickDotProduct(final double[] vectorA, final double[] vectorB) {
        // This will only throw un-checked exceptions.
        double result = 0.0d;
        for (int i = 0; i < vectorA.length; ++i) {
            result += vectorA[i] * vectorB[i];
        }
        return result;
    }

    /**
     * Calculates the dotProduct of vectorA and vectorB, No null checks or bounds checks are performed
     * 
     * @param vectorA has the precondition that it is a non-null Array the same size as vectorB
     * @param vectorB has the precondition that it is a non-null Array the same size as vectorA
     * @return the dotProduct/InnerProduct of vectorA and vectorB.
     */
    private static double quickDotProduct(final Array vectorA, final Array vectorB) {
        // This will only throw un-checked exceptions.
        return quickDotProduct(vectorA.data, vectorB.data);
    }

    private static void quickSwap(final Array vectorA, final Array vectorB) {
        double[] swapArray = new double[vectorA.data.length];
        System.arraycopy(vectorA.data, 0, swapArray, 0, vectorA.size);
        System.arraycopy(vectorB.data, 0, vectorA.data, 0, vectorB.size);
        System.arraycopy(swapArray, 0, vectorB.data, 0, swapArray.length);
    }

}
