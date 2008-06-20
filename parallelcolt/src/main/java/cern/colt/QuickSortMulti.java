/*
Copyright © 1999 CERN - European Organization for Nuclear Research.
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
is hereby granted without fee, provided that the above copyright notice appear in all copies and 
that both that copyright notice and this permission notice appear in supporting documentation. 
CERN makes no representations about the suitability of this software for any purpose. 
It is provided "as is" without expressed or implied warranty.
 */
package cern.colt;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import cern.colt.function.IntComparator;

/**
 * Multithreaded implementation of quicksort.
 * 
 * @author wolfgang.hoschek@cern.ch
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * 
 * @version 1.0, 08/22/2007
 * 
 */
public class QuickSortMulti {
	private static final int SMALL = 7;

	private static final int MEDIUM = 40;

	/**
	 * 
	 * Multithreaded quicksort of integers.
	 * 
	 * @param x
	 *            array of integers
	 * @param off
	 *            first index of subarray
	 * @param len
	 *            length of subarray
	 * @param comp
	 *            comparator
	 * @param nThreads
	 *            number of threads
	 */
	public static void qsort(final int[] x, final int off, int len, final IntComparator comp, final int nThreads) {
		// Insertion sort on smallest arrays
		if (len < SMALL) {
			for (int i = off; i < len + off; i++)
				for (int j = i; j > off && comp.compare(x[j - 1], x[j]) > 0; j--)
					swap(x, j, j - 1);
			return;
		}

		// Choose a partition element, v
		int m = off + len / 2; // Small arrays, middle element
		if (len > SMALL) {
			int l = off;
			int n = off + len - 1;
			if (len > MEDIUM) { // Big arrays, pseudomedian of 9
				int s = len / 8;
				l = med3(x, l, l + s, l + 2 * s, comp);
				m = med3(x, m - s, m, m + s, comp);
				n = med3(x, n - 2 * s, n - s, n, comp);
			}
			m = med3(x, l, m, n, comp); // Mid-size, med of 3
		}
		int v = x[m];

		// Establish Invariant: v* (<v)* (>v)* v*
		int a = off, b = a, c = off + len - 1, d = c;
		while (true) {
			int comparison;
			while (b <= c && (comparison = comp.compare(x[b], v)) <= 0) {
				if (comparison == 0)
					swap(x, a++, b);
				b++;
			}
			while (c >= b && (comparison = comp.compare(x[c], v)) >= 0) {
				if (comparison == 0)
					swap(x, c, d--);
				c--;
			}
			if (b > c)
				break;
			swap(x, b++, c--);
		}

		// Swap partition elements back to middle
		int s, n = off + len;
		s = Math.min(a - off, b - a);
		vecswap(x, off, b - s, s);
		s = Math.min(d - c, n - d - 1);
		vecswap(x, b, n - s, s);

		Future other = null;
		if (nThreads > 1) {
			// Recursively sort non-partition-elements
			if ((s = b - a) > 1) {
				final int s_f = s;
				other = Utils.threadPool.submit(new Runnable() {
					public void run() {
						qsort(x, off, s_f, comp, nThreads / 2);
					}
				});
			}
			if ((s = d - c) > 1) {
				if (other == null) {
					final int s_f = s;
					final int ns_f = n - s;
					other = Utils.threadPool.submit(new Runnable() {
						public void run() {
							qsort(x, ns_f, s_f, comp, nThreads / 2);
						}
					});
				} else {
					qsort(x, n - s, s, comp, nThreads / 2);
				}
			}
			try {
				other.get();
			} catch (InterruptedException e) {
				throw new RuntimeException("thread interrupted");
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			if ((s = b - a) > 1)
				qsort(x, off, s, comp, 1);
			if ((s = d - c) > 1)
				qsort(x, n - s, s, comp, 1);
		}

	}

	/**
	 * Swaps x[a] with x[b].
	 */
	private static void swap(int x[], int a, int b) {
		int t = x[a];
		x[a] = x[b];
		x[b] = t;
	}

	/**
	 * Swaps x[a .. (a+n-1)] with x[b .. (b+n-1)].
	 */
	private static void vecswap(int x[], int a, int b, int n) {
		for (int i = 0; i < n; i++, a++, b++)
			swap(x, a, b);
	}

	/**
	 * Returns the index of the median of the three indexed chars.
	 */
	private static int med3(int x[], int a, int b, int c, IntComparator comp) {
		int ab = comp.compare(x[a], x[b]);
		int ac = comp.compare(x[a], x[c]);
		int bc = comp.compare(x[b], x[c]);
		return (ab < 0 ? (bc < 0 ? b : ac < 0 ? c : a) : (bc > 0 ? b : ac > 0 ? c : a));
	}

}