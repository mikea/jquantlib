

package org.jquantlib.math.statistics;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Praneet Tiwari
 */
//TODO: code review :: please verify against original QL/C++ code
//class not found on QL 0.9.7
public class SortingAlgorithms {//<T extends Number, C extends Collection> {

    public static <T extends Number, C extends Collection<T>> Number[] insertionSort(final C unsortedCollection) {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        final Number[] array = unsortedCollection.toArray(new Number[0]);
        Number t;// =  Number.;

        for (int i = 1; i < array.length; i++) {
            int index;
            t = array[i];
            index = i;

            // now search for the appropriate place of this element

            while (index > 0 && array[index - 1].doubleValue() > t.doubleValue()) {
                array[index] = array[index - 1];
                index--;
            }

            // now place the selected element
            array[index] = t;

        }
        return array;
        //return new Collection<>();
    }


    public static <T extends Number, C extends Collection<T>> Number[] heapSort(final C unsortedCollection) {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        final Number[] array = unsortedCollection.toArray(new Number[0]);
        final Number t;// =  Number.;
        heapify(array);
        for (int end = array.length - 1; end > 0; end--) {
            swap(array, 0, end);
            heapSortWork(array, 0, end - 1);
        }

        return array;
    }

    private static void heapify(final Number[] array) {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");

        for (int start = array.length / 2 - 1; start >= 0; start--)
            heapSortWork(array, start, array.length - 1);

    }

    private static void heapSortWork(final Number[] array, int root, final int end) {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        while (root * 2 + 1 <= end) {
            int child = root * 2 + 1;
            if (child < end && array[child].doubleValue() < (array[child + 1].doubleValue()))
                child += 1;
            if (array[root].doubleValue() < (array[child]).doubleValue()) {
                swap(array, root, child);
                root = child;
            } else
                break;
        }
    }

    private static void swap(final Number[] array, final int i1, final int i2) {				// swap items in array
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        final Number tmp = array[i1];
        array[i1] = array[i2];
        array[i2] = tmp;
    }

    //private static void swap()
    public static <T extends Number, C extends Collection<T>> Number[] selectionSort(final C unsortedCollection) {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        final Number[] array = unsortedCollection.toArray(new Number[0]);
        final Number t;// =  Number.;

        int indexSmallest;

        for (int i = 0; i < array.length - 1; i++) {
            indexSmallest = i;

            for (int index = i + 1; index < array.length - 1; index++)
                if (array[index].doubleValue() < array[indexSmallest].doubleValue())
                    indexSmallest = index;
            swap(array, i, indexSmallest);
        }

        return array;
    }

    public static <T extends Number, C extends Collection<T>> Number[] quickSort(final C unsortedCollection) {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        final Number[] array = unsortedCollection.toArray(new Number[0]);
        final Number t;// =  Number.;
        quicksort(array, 0, array.length - 1);

        return array;
    }

    private static void quicksort(final Number[] array, final int left0, final int right0) {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        int left, right;
        Number pivot, temp;
        left = left0;
        right = right0 + 1;
        pivot = array[left0];
        left++;
        right--;
        do {
            while (left <= right0 && array[left].doubleValue() < (pivot).doubleValue())
                left++;
            while (array[right].doubleValue() > (pivot).doubleValue())
                right--;


            if (left < right) {
                temp = array[left];
                array[left] = array[right];
                array[right] = temp;
            }

        } while (left <= right);
        temp = array[left0];
        array[left0] = array[right];
        array[right] = temp;

        if (left0 < right)
            quicksort(array, left0, right);
        if (left < right0)
            quicksort(array, left, right0);

    }

    public static void main(final String args[]) {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        final ArrayList<Number> al = new ArrayList<Number>();
        final ArrayList<Number> resArrayList = new ArrayList<Number>();

        al.add(2);
        al.add(3);
        al.add(4.2);
        al.add(-102.21);
        al.add(143.781);

        final Number[] resNum = insertionSort(al);
        //quicksort(al);/*
        for (int i = 0; i < resNum.length; i++)
            System.out.println("Testing insertion sort - element " + resNum[i] + " at position " + i);

        //heap sort test..

        final Number[] res = heapSort(al);

        for (int i = 0; i < res.length; i++)
            System.out.println("Testing heap sort - element " + res[i] + " at position " + i);

        final Number[] resSel = selectionSort(al);

        for (int i = 0; i < resSel.length; i++)
            System.out.println("Testing selection sort - element " + resSel[i] + " at position " + i);

        final Number[] resQuick = quickSort(al);

        for (int i = 0; i < resQuick.length; i++)
            System.out.println("Testing quick sort - element " + resQuick[i] + " at position " + i);

    }

}
