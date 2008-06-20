/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is JTransforms.
 *
 * The Initial Developer of the Original Code is
 * Piotr Wendykier, Emory University.
 * Portions created by the Initial Developer are Copyright (C) 2007
 * the Initial Developer. All Rights Reserved.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

package edu.emory.mathcs.jtransforms.dst;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import cern.colt.Utils;
import edu.emory.mathcs.jtransforms.dct.DoubleDCT_1D;

/**
 * Computes 1D Discrete Sine Transform (DST) of double precision data where
 * dimension is an integer power of 2. It uses DCT algorithm. This is a parallel
 * implementation optimized for SMP systems. <br>
 * <br>
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * @version 1.1 01/01/2008
 * 
 */
public class DoubleDST_1D {

	private int n;

	private static final long DST1D_THREADS_BEGIN_N = 8192;

	private DoubleDCT_1D dct;

	/**
	 * Creates new instance of DoubleDST_1D.
	 * 
	 * @param n
	 *            dimension - must be power of 2
	 */
	public DoubleDST_1D(int n) {
		this.n = n;
		dct = new DoubleDCT_1D(n);
	}

	protected DoubleDST_1D(int n, int[] ip, double[] w) {
		this.n = n;
		dct = new DoubleDCT_1D(n, ip, w);
	}

	/**
	 * Computes 1D forward DST (DST-II) leaving the result in <code>a</code>.
	 * 
	 * @param a
	 *            data to transform
	 * @param scale
	 *            if true then scaling is performed
	 */
	public void forward(double[] a, boolean scale) {
		forward(a, 0, scale);
	}

	protected void forward(final double[] a, final int starta, boolean scale) {
		double tmp;
		int np = Utils.getNP();
		if ((np > 1) && (n > DST1D_THREADS_BEGIN_N)) {
			final int k = n / np;
			Future[] futures = new Future[np];
			for (int j = 0; j < np; j++) {
				final int loc_starta = starta + j * k + 1;
				final int loc_stopa;
				if (j == np - 1) {
					loc_stopa = n;
				} else {
					loc_stopa = loc_starta + k;
				}
				futures[j] = Utils.threadPool.submit(new Runnable() {
					public void run() {
						for (int i = loc_starta; i < loc_stopa; i += 2) {
							a[i] = -a[i];
						}
					}
				});
			}
			try {
				for (int j = 0; j < np; j++) {
					futures[j].get();
				}
			} catch (ExecutionException ex) {
				ex.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			int idx;
			for (int i = 1; i < n; i += 2) {
				idx = starta + i;
				a[idx] = -a[idx];
			}
		}
		dct.forward(a, starta, scale);
		if ((np > 1) && (n > DST1D_THREADS_BEGIN_N)) {
			final int k = n / 2 / np;
			Future[] futures = new Future[np];
			for (int j = 0; j < np; j++) {
				final int loc_starta = starta + j * k;
				final int loc_stopa;
				if (j == np - 1) {
					loc_stopa = n / 2;
				} else {
					loc_stopa = loc_starta + k;
				}
				futures[j] = Utils.threadPool.submit(new Runnable() {
					public void run() {
						double tmp;
						int idx0 = starta + n - 1;
						int idx1;
						for (int i = loc_starta; i < loc_stopa; i++) {
							tmp = a[i];
							idx1 = idx0 - i;
							a[i] = a[idx1];
							a[idx1] = tmp;
						}
					}
				});
			}
			try {
				for (int j = 0; j < np; j++) {
					futures[j].get();
				}
			} catch (ExecutionException ex) {
				ex.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			int idx0 = starta + n - 1;
			for (int i = 0; i < n / 2; i++) {
				tmp = a[starta + i];
				a[starta + i] = a[idx0 - i];
				a[idx0 - i] = tmp;
			}
		}
	}

	/**
	 * Computes 1D inverse DST (DST-III) leaving the result in <code>a</code>.
	 * 
	 * @param a
	 *            data to transform
	 * @param scale
	 *            if true then scaling is performed
	 */
	public void inverse(double[] a, boolean scale) {
		inverse(a, 0, scale);
	}

	protected void inverse(final double[] a, final int starta, boolean scale) {
		double tmp;
		int np = Utils.getNP();
		if ((np > 1) && (n > DST1D_THREADS_BEGIN_N)) {
			final int k = n / 2 / np;
			Future[] futures = new Future[np];
			for (int j = 0; j < np; j++) {
				final int loc_starta = starta + j * k;
				final int loc_stopa;
				if (j == np - 1) {
					loc_stopa = n / 2;
				} else {
					loc_stopa = loc_starta + k;
				}
				futures[j] = Utils.threadPool.submit(new Runnable() {
					public void run() {
						double tmp;
						int idx0 = starta + n - 1;
						int idx1;
						for (int i = loc_starta; i < loc_stopa; i++) {
							tmp = a[i];
							idx1 = idx0 - i;
							a[i] = a[idx1];
							a[idx1] = tmp;
						}
					}
				});
			}
			try {
				for (int j = 0; j < np; j++) {
					futures[j].get();
				}
			} catch (ExecutionException ex) {
				ex.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			int idx0 = starta + n - 1;
			for (int i = 0; i < n / 2; i++) {
				tmp = a[starta + i];
				a[starta + i] = a[idx0 - i];
				a[idx0 - i] = tmp;
			}
		}
		dct.inverse(a, starta, scale);
		if ((np > 1) && (n > DST1D_THREADS_BEGIN_N)) {
			final int k = n / np;
			Future[] futures = new Future[np];
			for (int j = 0; j < np; j++) {
				final int loc_starta = starta + j * k + 1;
				final int loc_stopa;
				if (j == np - 1) {
					loc_stopa = n;
				} else {
					loc_stopa = loc_starta + k;
				}
				futures[j] = Utils.threadPool.submit(new Runnable() {
					public void run() {
						for (int i = loc_starta; i < loc_stopa; i += 2) {
							a[i] = -a[i];
						}
					}
				});
			}
			try {
				for (int j = 0; j < np; j++) {
					futures[j].get();
				}
			} catch (ExecutionException ex) {
				ex.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			int idx;
			for (int i = 1; i < n; i += 2) {
				idx = starta + i;
				a[idx] = -a[idx];
			}
		}
	}
}
