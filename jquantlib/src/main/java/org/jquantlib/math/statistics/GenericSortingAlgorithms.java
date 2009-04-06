/*
 Copyright (C) 2008 Praneet Tiwari

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
package org.jquantlib.math.statistics;

//~--- JDK imports ------------------------------------------------------------
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Praneet Tiwari
 */
public class GenericSortingAlgorithms {

    public static <T extends Number, C extends Collection<T>> Number[] insertionSort(C unsortedCollection) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        Number[] array = unsortedCollection.toArray(new Number[0]);
        Number t;    // =  Number.;

        for (int i = 1; i < array.length; i++) {
            int index;

            t = array[i];
            index = i;

            // now search for the appropriate place of this element
            while ((index > 0) && (array[index - 1].doubleValue() > t.doubleValue())) {
                array[index] = array[index - 1];
                index--;
            }

            // now place the selected element
            array[index] = t;
        }

        return array;

    // return new Collection<>();
    }

    public static <T extends Number, C extends Collection<T>> Number[] heapSort(C unsortedCollection) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        Number[] array = unsortedCollection.toArray(new Number[0]);
        Number t;    // =  Number.;

        heapify(array);

        for (int end = array.length - 1; end > 0; end--) {
            swap(array, 0, end);
            heapSortWork(array, 0, end - 1);
        }

        return array;
    }

    private static void heapify(Number[] array) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        for (int start = array.length / 2 - 1; start >= 0; start--) {
            heapSortWork(array, start, array.length - 1);
        }
    }

    private static void heapSortWork(Number[] array, int root, int end) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        while (root * 2 + 1 <= end) {
            int child = root * 2 + 1;

            if ((child < end) && (array[child].doubleValue() < (array[child + 1].doubleValue()))) {
                child += 1;
            }

            if (array[root].doubleValue() < (array[child]).doubleValue()) {
                swap(array, root, child);
                root = child;
            } else {
                break;
            }
        }
    }

    private static void swap(Number[] array, int i1, int i2) {    // swap items in array
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        Number tmp = array[i1];

        array[i1] = array[i2];
        array[i2] = tmp;
    }

    // private static void swap()
    public static <T extends Number, C extends Collection<T>> Number[] selectionSort(C unsortedCollection) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        Number[] array = unsortedCollection.toArray(new Number[0]);
        Number t;    // =  Number.;
        int indexSmallest;

        for (int i = 0; i < array.length - 1; i++) {
            indexSmallest = i;

            for (int index = i + 1; index < array.length; index++) {
                if (array[index].doubleValue() < array[indexSmallest].doubleValue()) {
                    indexSmallest = index;
                }
            }

            swap(array, i, indexSmallest);
        }

        return array;
    }

    public static <T extends Number, C extends Collection<T>> Number[] quickSort(C unsortedCollection) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        Number[] array = unsortedCollection.toArray(new Number[0]);
        Number t;    // =  Number.;

        quicksort(array, 0, array.length - 1);

        return array;
    }

    private static void quicksort(Number[] array, int left0, int right0) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        int left, right;
        Number pivot, temp;

        left = left0;
        right = right0 + 1;
        pivot = array[left0];
        left++;
        right--;

        do {
            while ((left <= right0) && (array[left].doubleValue() < (pivot).doubleValue())) {
                left++;
            }

            while (array[right].doubleValue() > (pivot).doubleValue()) {
                right--;
            }

            if (left < right) {
                temp = array[left];
                array[left] = array[right];
                array[right] = temp;
            }
        } while (left <= right);

        temp = array[left0];
        array[left0] = array[right];
        array[right] = temp;

        if (left0 < right) {
            quicksort(array, left0, right);
        }

        if (left < right0) {
            quicksort(array, left, right0);
        }
    }

    public static void main(String args[]) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        ArrayList<Number> al = new ArrayList<Number>();
        ArrayList<Number> resArrayList = new ArrayList<Number>();

        al.add(2);
        al.add(3);
        al.add(4.2);
        al.add(-102.21);
        al.add(143.781);

        Number[] resNum = insertionSort(al);

        // quicksort(al);/*
        for (int i = 0; i < resNum.length; i++) {
            System.out.println("Testing insertion sort - element " + resNum[i] + " at position " + i);
        }

        // heap sort test..
        Number[] res = heapSort(al);

        for (int i = 0; i < res.length; i++) {
            System.out.println("Testing heap sort - element " + res[i] + " at position " + i);
        }

        Number[] resSel = selectionSort(al);

        for (int i = 0; i < resSel.length; i++) {
            System.out.println("Testing selection sort - element " + resSel[i] + " at position " + i);
        }

        Number[] resQuick = quickSort(al);

        for (int i = 0; i < resQuick.length; i++) {
            System.out.println("Testing quick sort - element " + resQuick[i] + " at position " + i);
        }
    }
}

 
