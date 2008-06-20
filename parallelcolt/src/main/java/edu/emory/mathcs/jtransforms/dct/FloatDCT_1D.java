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

package edu.emory.mathcs.jtransforms.dct;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import cern.colt.Utils;

/**
 * Computes 1D Discrete Cosine Transform (DCT) of single precision data where
 * dimension is an integer power of 2. This is a parallel implementation of
 * split-radix algorithm optimized for SMP systems. <br>
 * <br>
 * This code is derived from General Purpose FFT Package written by Takuya Ooura
 * (http://www.kurims.kyoto-u.ac.jp/~ooura/fft.html)
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * @version 1.1 01/01/2008
 * 
 */
public class FloatDCT_1D {
	private static final long DCT1D_2THREADS_BEGIN_N = 8192;

	private static final long DCT1D_4THREADS_BEGIN_N = 65536;

	private int n;

	private int[] ip;

	private float[] w;

	/**
	 * Creates new instance of FloatDCT_1D.
	 * 
	 * @param n
	 *            dimension - must be power of 2
	 */
	public FloatDCT_1D(int n) {
		if (!Utils.isPowerOf2(n))
			throw new IllegalArgumentException("Dimension must be power of two");
		this.n = n;
		this.ip = new int[(int) Math.ceil(2 + (1 << (int) (Math.log(n / 2 + 0.5) / Math.log(2)) / 2))];
		this.w = new float[n * 5 / 4];
	}

	public FloatDCT_1D(int n, int[] ip, float[] w) {
		if (!Utils.isPowerOf2(n))
			throw new IllegalArgumentException("Dimension must be power of two");
		this.n = n;
		this.ip = ip;
		this.w = w;
	}

	/**
	 * Computes 1D forward DCT (DCT-II) leaving the result in <code>a</code>.
	 * 
	 * @param a
	 *            data to transform
	 * @param scale
	 *            if true then scaling is performed
	 */
	public void forward(float[] a, boolean scale) {
		forward(a, 0, scale);
	}

	public void forward(float[] a, int starta, boolean scale) {
		int j, nw, nc;
		float xr;

		nw = ip[0];
		if (n > (nw << 2)) {
			nw = n >> 2;
			makewt(nw);
		}
		nc = ip[1];
		if (n > nc) {
			nc = n;
			makect(nc, w, nw);
		}
		xr = a[starta + n - 1];
		for (j = n - 2; j >= 2; j -= 2) {
			a[starta + j + 1] = a[starta + j] - a[starta + j - 1];
			a[starta + j] += a[starta + j - 1];
		}
		a[starta + 1] = a[starta] - xr;
		a[starta] += xr;
		if (n > 4) {
			rftbsub(n, a, starta, nc, w, nw);
			cftbsub(n, a, starta, ip, nw, w);
		} else if (n == 4) {
			cftbsub(n, a, starta, ip, nw, w);
		}
		dctsub(n, a, starta, nc, w, nw);

		if (scale) {
			scale(a, starta);
			a[starta] = a[starta] / (float) Math.sqrt(2.0);
		}
	}

	/**
	 * Computes 1D inverse DCT (DCT-III) leaving the result in <code>a</code>.
	 * 
	 * @param a
	 *            data to transform
	 * @param scale
	 *            if true then scaling is performed
	 */
	public void inverse(float[] a, boolean scale) {
		inverse(a, 0, scale);
	}

	public void inverse(float[] a, int starta, boolean scale) {
		int j, nw, nc;
		float xr;

		nw = ip[0];
		if (n > (nw << 2)) {
			nw = n >> 2;
			makewt(nw);
		}
		nc = ip[1];
		if (n > nc) {
			nc = n;
			makect(nc, w, nw);
		}
		if (scale) {
			scale(a, starta);
			a[starta] = a[starta] / (float) Math.sqrt(2.0);
		}
		dctsub(n, a, starta, nc, w, nw);
		if (n > 4) {
			cftfsub(n, a, starta, ip, nw, w);
			rftfsub(n, a, starta, nc, w, nw);
		} else if (n == 4) {
			cftfsub(n, a, starta, ip, nw, w);
		}
		xr = a[starta] - a[starta + 1];
		a[starta] += a[starta + 1];
		for (j = 2; j < n; j += 2) {
			a[starta + j - 1] = a[starta + j] - a[starta + j + 1];
			a[starta + j] += a[starta + j + 1];
		}
		a[starta + n - 1] = xr;
	}

	/* -------- initializing routines -------- */

	private void makewt(int nw) {
		int j, nwh, nw0, nw1;
		float delta, wn4r, wk1r, wk1i, wk3r, wk3i;

		ip[0] = nw;
		ip[1] = 1;
		if (nw > 2) {
			nwh = nw >> 1;
			delta = (float) (Math.atan(1.0) / nwh);
			wn4r = (float) Math.cos(delta * nwh);
			w[0] = 1;
			w[1] = wn4r;
			if (nwh == 4) {
				w[2] = (float) Math.cos(delta * 2);
				w[3] = (float) Math.sin(delta * 2);
			} else if (nwh > 4) {
				makeipt(nw);
				w[2] = (float) (0.5 / Math.cos(delta * 2));
				w[3] = (float) (0.5 / Math.cos(delta * 6));
				for (j = 4; j < nwh; j += 4) {
					w[j] = (float) Math.cos(delta * j);
					w[j + 1] = (float) Math.sin(delta * j);
					w[j + 2] = (float) Math.cos(3 * delta * j);
					w[j + 3] = (float) -Math.sin(3 * delta * j);
				}
			}
			nw0 = 0;
			while (nwh > 2) {
				nw1 = nw0 + nwh;
				nwh >>= 1;
				w[nw1] = 1;
				w[nw1 + 1] = wn4r;
				if (nwh == 4) {
					wk1r = w[nw0 + 4];
					wk1i = w[nw0 + 5];
					w[nw1 + 2] = wk1r;
					w[nw1 + 3] = wk1i;
				} else if (nwh > 4) {
					wk1r = w[nw0 + 4];
					wk3r = w[nw0 + 6];
					w[nw1 + 2] = (float) (0.5 / wk1r);
					w[nw1 + 3] = (float) (0.5 / wk3r);
					for (j = 4; j < nwh; j += 4) {
						int idx1 = nw0 + 2 * j;
						int idx2 = nw1 + j;
						wk1r = w[idx1];
						wk1i = w[idx1 + 1];
						wk3r = w[idx1 + 2];
						wk3i = w[idx1 + 3];
						w[idx2] = wk1r;
						w[idx2 + 1] = wk1i;
						w[idx2 + 2] = wk3r;
						w[idx2 + 3] = wk3i;
					}
				}
				nw0 = nw1;
			}
		}
	}

	private void makeipt(int nw) {
		int j, l, m, m2, p, q;

		ip[2] = 0;
		ip[3] = 16;
		m = 2;
		for (l = nw; l > 32; l >>= 2) {
			m2 = m << 1;
			q = m2 << 3;
			for (j = m; j < m2; j++) {
				p = ip[j] << 2;
				ip[m + j] = p;
				ip[m2 + j] = p + q;
			}
			m = m2;
		}
	}

	private void makect(int nc, float[] c, int startc) {
		int j, nch;
		float delta;

		ip[1] = nc;
		if (nc > 1) {
			nch = nc >> 1;
			delta = (float) Math.atan(1.0) / nch;
			c[startc] = (float) Math.cos(delta * nch);
			c[startc + nch] = 0.5f * c[startc];
			for (j = 1; j < nch; j++) {
				c[startc + j] = (float) (0.5 * Math.cos(delta * j));
				c[startc + nc - j] = (float) (0.5 * Math.sin(delta * j));
			}
		}
	}

	/* -------- child routines -------- */

	private void cftfsub(int n, float[] a, int starta, int[] ip, int nw, float[] w) {
		if (n > 8) {
			if (n > 32) {
				cftf1st(n, a, starta, w, nw - (n >> 2));
				if ((Utils.getNP() > 1) && (n > DCT1D_2THREADS_BEGIN_N)) {
					cftrec4_th(n, a, starta, nw, w);
				} else if (n > 512) {
					cftrec4(n, a, starta, nw, w);
				} else if (n > 128) {
					cftleaf(n, 1, a, starta, nw, w);
				} else {
					cftfx41(n, a, starta, nw, w);
				}
				bitrv2(n, ip, a, starta);
			} else if (n == 32) {
				cftf161(a, starta, w, nw - 8);
				bitrv216(a, starta);
			} else {
				cftf081(a, starta, w, 0);
				bitrv208(a, starta);
			}
		} else if (n == 8) {
			cftf040(a, starta);
		} else if (n == 4) {
			cftx020(a, starta);
		}
	}

	private void cftbsub(int n, float[] a, int starta, int[] ip, int nw, float[] w) {
		if (n > 8) {
			if (n > 32) {
				cftb1st(n, a, starta, w, nw - (n >> 2));
				if ((Utils.getNP() > 1) && (n > DCT1D_2THREADS_BEGIN_N)) {
					cftrec4_th(n, a, starta, nw, w);
				} else if (n > 512) {
					cftrec4(n, a, starta, nw, w);
				} else if (n > 128) {
					cftleaf(n, 1, a, starta, nw, w);
				} else {
					cftfx41(n, a, starta, nw, w);
				}
				bitrv2conj(n, ip, a, starta);
			} else if (n == 32) {
				cftf161(a, starta, w, nw - 8);
				bitrv216neg(a, starta);
			} else {
				cftf081(a, starta, w, 0);
				bitrv208neg(a, starta);
			}
		} else if (n == 8) {
			cftb040(a, starta);
		} else if (n == 4) {
			cftx020(a, starta);
		}
	}

	private void bitrv2(int n, int[] ip, float[] a, int starta) {
		int j, j1, k, k1, l, m, nh, nm;
		float xr, xi, yr, yi;
		int idx1, idx2;

		m = 1;
		for (l = n >> 2; l > 8; l >>= 2) {
			m <<= 1;
		}
		nh = n >> 1;
		nm = 4 * m;
		if (l == 8) {
			for (k = 0; k < m; k++) {
				for (j = 0; j < k; j++) {
					j1 = 4 * j + 2 * ip[m + k];
					k1 = 4 * k + 2 * ip[m + j];
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 += 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 -= nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 += 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nh;
					k1 += 2;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 -= 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 += nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 -= 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += 2;
					k1 += nh;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 += 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 -= nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 += 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nh;
					k1 -= 2;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 -= 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 += nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 -= 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
				}
				k1 = 4 * k + 2 * ip[m + k];
				j1 = k1 + 2;
				k1 += nh;
				idx1 = starta + j1;
				idx2 = starta + k1;
				xr = a[idx1];
				xi = a[idx1 + 1];
				yr = a[idx2];
				yi = a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				j1 += nm;
				k1 += 2 * nm;
				idx1 = starta + j1;
				idx2 = starta + k1;
				xr = a[idx1];
				xi = a[idx1 + 1];
				yr = a[idx2];
				yi = a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				j1 += nm;
				k1 -= nm;
				idx1 = starta + j1;
				idx2 = starta + k1;
				xr = a[idx1];
				xi = a[idx1 + 1];
				yr = a[idx2];
				yi = a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				j1 -= 2;
				k1 -= nh;
				idx1 = starta + j1;
				idx2 = starta + k1;
				xr = a[idx1];
				xi = a[idx1 + 1];
				yr = a[idx2];
				yi = a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				j1 += nh + 2;
				k1 += nh + 2;
				idx1 = starta + j1;
				idx2 = starta + k1;
				xr = a[idx1];
				xi = a[idx1 + 1];
				yr = a[idx2];
				yi = a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				j1 -= nh - nm;
				k1 += 2 * nm - 2;
				idx1 = starta + j1;
				idx2 = starta + k1;
				xr = a[idx1];
				xi = a[idx1 + 1];
				yr = a[idx2];
				yi = a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
			}
		} else {
			for (k = 0; k < m; k++) {
				for (j = 0; j < k; j++) {
					j1 = 4 * j + ip[m + k];
					k1 = 4 * k + ip[m + j];
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 += nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nh;
					k1 += 2;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 -= nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += 2;
					k1 += nh;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 += nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nh;
					k1 -= 2;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 -= nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = a[idx1 + 1];
					yr = a[idx2];
					yi = a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
				}
				k1 = 4 * k + ip[m + k];
				j1 = k1 + 2;
				k1 += nh;
				idx1 = starta + j1;
				idx2 = starta + k1;
				xr = a[idx1];
				xi = a[idx1 + 1];
				yr = a[idx2];
				yi = a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				j1 += nm;
				k1 += nm;
				idx1 = starta + j1;
				idx2 = starta + k1;
				xr = a[idx1];
				xi = a[idx1 + 1];
				yr = a[idx2];
				yi = a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
			}
		}
	}

	private void bitrv2conj(int n, int[] ip, float[] a, int starta) {
		int j, j1, k, k1, l, m, nh, nm;
		float xr, xi, yr, yi;
		int idx1, idx2;

		m = 1;
		for (l = n >> 2; l > 8; l >>= 2) {
			m <<= 1;
		}
		nh = n >> 1;
		nm = 4 * m;
		if (l == 8) {
			for (k = 0; k < m; k++) {
				for (j = 0; j < k; j++) {
					j1 = 4 * j + 2 * ip[m + k];
					k1 = 4 * k + 2 * ip[m + j];
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 += 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 -= nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 += 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nh;
					k1 += 2;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 -= 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 += nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 -= 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += 2;
					k1 += nh;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 += 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 -= nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 += 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nh;
					k1 -= 2;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 -= 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 += nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 -= 2 * nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
				}
				k1 = 4 * k + 2 * ip[m + k];
				j1 = k1 + 2;
				k1 += nh;
				idx1 = starta + j1;
				idx2 = starta + k1;
				a[idx1 - 1] = -a[idx1 - 1];
				xr = a[idx1];
				xi = -a[idx1 + 1];
				yr = a[idx2];
				yi = -a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				a[idx2 + 3] = -a[idx2 + 3];
				j1 += nm;
				k1 += 2 * nm;
				idx1 = starta + j1;
				idx2 = starta + k1;
				xr = a[idx1];
				xi = -a[idx1 + 1];
				yr = a[idx2];
				yi = -a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				j1 += nm;
				k1 -= nm;
				idx1 = starta + j1;
				idx2 = starta + k1;
				xr = a[idx1];
				xi = -a[idx1 + 1];
				yr = a[idx2];
				yi = -a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				j1 -= 2;
				k1 -= nh;
				idx1 = starta + j1;
				idx2 = starta + k1;
				xr = a[idx1];
				xi = -a[idx1 + 1];
				yr = a[idx2];
				yi = -a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				j1 += nh + 2;
				k1 += nh + 2;
				idx1 = starta + j1;
				idx2 = starta + k1;
				xr = a[idx1];
				xi = -a[idx1 + 1];
				yr = a[idx2];
				yi = -a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				j1 -= nh - nm;
				k1 += 2 * nm - 2;
				idx1 = starta + j1;
				idx2 = starta + k1;
				a[idx1 - 1] = -a[idx1 - 1];
				xr = a[idx1];
				xi = -a[idx1 + 1];
				yr = a[idx2];
				yi = -a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				a[idx2 + 3] = -a[idx2 + 3];
			}
		} else {
			for (k = 0; k < m; k++) {
				for (j = 0; j < k; j++) {
					j1 = 4 * j + ip[m + k];
					k1 = 4 * k + ip[m + j];
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 += nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nh;
					k1 += 2;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 -= nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += 2;
					k1 += nh;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 += nm;
					k1 += nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nh;
					k1 -= 2;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
					j1 -= nm;
					k1 -= nm;
					idx1 = starta + j1;
					idx2 = starta + k1;
					xr = a[idx1];
					xi = -a[idx1 + 1];
					yr = a[idx2];
					yi = -a[idx2 + 1];
					a[idx1] = yr;
					a[idx1 + 1] = yi;
					a[idx2] = xr;
					a[idx2 + 1] = xi;
				}
				k1 = 4 * k + ip[m + k];
				j1 = k1 + 2;
				k1 += nh;
				idx1 = starta + j1;
				idx2 = starta + k1;
				a[idx1 - 1] = -a[idx1 - 1];
				xr = a[idx1];
				xi = -a[idx1 + 1];
				yr = a[idx2];
				yi = -a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				a[idx2 + 3] = -a[idx2 + 3];
				j1 += nm;
				k1 += nm;
				idx1 = starta + j1;
				idx2 = starta + k1;
				a[idx1 - 1] = -a[idx1 - 1];
				xr = a[idx1];
				xi = -a[idx1 + 1];
				yr = a[idx2];
				yi = -a[idx2 + 1];
				a[idx1] = yr;
				a[idx1 + 1] = yi;
				a[idx2] = xr;
				a[idx2 + 1] = xi;
				a[idx2 + 3] = -a[idx2 + 3];
			}
		}
	}

	private void bitrv216(float[] a, int starta) {
		float x1r, x1i, x2r, x2i, x3r, x3i, x4r, x4i, x5r, x5i, x7r, x7i, x8r, x8i, x10r, x10i, x11r, x11i, x12r, x12i, x13r, x13i, x14r, x14i;

		x1r = a[starta + 2];
		x1i = a[starta + 3];
		x2r = a[starta + 4];
		x2i = a[starta + 5];
		x3r = a[starta + 6];
		x3i = a[starta + 7];
		x4r = a[starta + 8];
		x4i = a[starta + 9];
		x5r = a[starta + 10];
		x5i = a[starta + 11];
		x7r = a[starta + 14];
		x7i = a[starta + 15];
		x8r = a[starta + 16];
		x8i = a[starta + 17];
		x10r = a[starta + 20];
		x10i = a[starta + 21];
		x11r = a[starta + 22];
		x11i = a[starta + 23];
		x12r = a[starta + 24];
		x12i = a[starta + 25];
		x13r = a[starta + 26];
		x13i = a[starta + 27];
		x14r = a[starta + 28];
		x14i = a[starta + 29];
		a[starta + 2] = x8r;
		a[starta + 3] = x8i;
		a[starta + 4] = x4r;
		a[starta + 5] = x4i;
		a[starta + 6] = x12r;
		a[starta + 7] = x12i;
		a[starta + 8] = x2r;
		a[starta + 9] = x2i;
		a[starta + 10] = x10r;
		a[starta + 11] = x10i;
		a[starta + 14] = x14r;
		a[starta + 15] = x14i;
		a[starta + 16] = x1r;
		a[starta + 17] = x1i;
		a[starta + 20] = x5r;
		a[starta + 21] = x5i;
		a[starta + 22] = x13r;
		a[starta + 23] = x13i;
		a[starta + 24] = x3r;
		a[starta + 25] = x3i;
		a[starta + 26] = x11r;
		a[starta + 27] = x11i;
		a[starta + 28] = x7r;
		a[starta + 29] = x7i;
	}

	private void bitrv216neg(float[] a, int starta) {
		float x1r, x1i, x2r, x2i, x3r, x3i, x4r, x4i, x5r, x5i, x6r, x6i, x7r, x7i, x8r, x8i, x9r, x9i, x10r, x10i, x11r, x11i, x12r, x12i, x13r, x13i, x14r, x14i, x15r, x15i;

		x1r = a[starta + 2];
		x1i = a[starta + 3];
		x2r = a[starta + 4];
		x2i = a[starta + 5];
		x3r = a[starta + 6];
		x3i = a[starta + 7];
		x4r = a[starta + 8];
		x4i = a[starta + 9];
		x5r = a[starta + 10];
		x5i = a[starta + 11];
		x6r = a[starta + 12];
		x6i = a[starta + 13];
		x7r = a[starta + 14];
		x7i = a[starta + 15];
		x8r = a[starta + 16];
		x8i = a[starta + 17];
		x9r = a[starta + 18];
		x9i = a[starta + 19];
		x10r = a[starta + 20];
		x10i = a[starta + 21];
		x11r = a[starta + 22];
		x11i = a[starta + 23];
		x12r = a[starta + 24];
		x12i = a[starta + 25];
		x13r = a[starta + 26];
		x13i = a[starta + 27];
		x14r = a[starta + 28];
		x14i = a[starta + 29];
		x15r = a[starta + 30];
		x15i = a[starta + 31];
		a[starta + 2] = x15r;
		a[starta + 3] = x15i;
		a[starta + 4] = x7r;
		a[starta + 5] = x7i;
		a[starta + 6] = x11r;
		a[starta + 7] = x11i;
		a[starta + 8] = x3r;
		a[starta + 9] = x3i;
		a[starta + 10] = x13r;
		a[starta + 11] = x13i;
		a[starta + 12] = x5r;
		a[starta + 13] = x5i;
		a[starta + 14] = x9r;
		a[starta + 15] = x9i;
		a[starta + 16] = x1r;
		a[starta + 17] = x1i;
		a[starta + 18] = x14r;
		a[starta + 19] = x14i;
		a[starta + 20] = x6r;
		a[starta + 21] = x6i;
		a[starta + 22] = x10r;
		a[starta + 23] = x10i;
		a[starta + 24] = x2r;
		a[starta + 25] = x2i;
		a[starta + 26] = x12r;
		a[starta + 27] = x12i;
		a[starta + 28] = x4r;
		a[starta + 29] = x4i;
		a[starta + 30] = x8r;
		a[starta + 31] = x8i;
	}

	private void bitrv208(float[] a, int starta) {
		float x1r, x1i, x3r, x3i, x4r, x4i, x6r, x6i;

		x1r = a[starta + 2];
		x1i = a[starta + 3];
		x3r = a[starta + 6];
		x3i = a[starta + 7];
		x4r = a[starta + 8];
		x4i = a[starta + 9];
		x6r = a[starta + 12];
		x6i = a[starta + 13];
		a[starta + 2] = x4r;
		a[starta + 3] = x4i;
		a[starta + 6] = x6r;
		a[starta + 7] = x6i;
		a[starta + 8] = x1r;
		a[starta + 9] = x1i;
		a[starta + 12] = x3r;
		a[starta + 13] = x3i;
	}

	private void bitrv208neg(float[] a, int starta) {
		float x1r, x1i, x2r, x2i, x3r, x3i, x4r, x4i, x5r, x5i, x6r, x6i, x7r, x7i;

		x1r = a[starta + 2];
		x1i = a[starta + 3];
		x2r = a[starta + 4];
		x2i = a[starta + 5];
		x3r = a[starta + 6];
		x3i = a[starta + 7];
		x4r = a[starta + 8];
		x4i = a[starta + 9];
		x5r = a[starta + 10];
		x5i = a[starta + 11];
		x6r = a[starta + 12];
		x6i = a[starta + 13];
		x7r = a[starta + 14];
		x7i = a[starta + 15];
		a[starta + 2] = x7r;
		a[starta + 3] = x7i;
		a[starta + 4] = x3r;
		a[starta + 5] = x3i;
		a[starta + 6] = x5r;
		a[starta + 7] = x5i;
		a[starta + 8] = x1r;
		a[starta + 9] = x1i;
		a[starta + 10] = x6r;
		a[starta + 11] = x6i;
		a[starta + 12] = x2r;
		a[starta + 13] = x2i;
		a[starta + 14] = x4r;
		a[starta + 15] = x4i;
	}

	private void cftf1st(int n, float[] a, int starta, float[] w, int startw) {
		int j, j0, j1, j2, j3, k, m, mh;
		float wn4r, csc1, csc3, wk1r, wk1i, wk3r, wk3i, wd1r, wd1i, wd3r, wd3i;
		float x0r, x0i, x1r, x1i, x2r, x2i, x3r, x3i, y0r, y0i, y1r, y1i, y2r, y2i, y3r, y3i;
		int idx0, idx1, idx2, idx3, idx4, idx5;
		mh = n >> 3;
		m = 2 * mh;
		j1 = m;
		j2 = j1 + m;
		j3 = j2 + m;
		idx1 = starta + j1;
		idx2 = starta + j2;
		idx3 = starta + j3;
		x0r = a[starta] + a[idx2];
		x0i = a[starta + 1] + a[idx2 + 1];
		x1r = a[starta] - a[idx2];
		x1i = a[starta + 1] - a[idx2 + 1];
		x2r = a[idx1] + a[idx3];
		x2i = a[idx1 + 1] + a[idx3 + 1];
		x3r = a[idx1] - a[idx3];
		x3i = a[idx1 + 1] - a[idx3 + 1];
		a[starta] = x0r + x2r;
		a[starta + 1] = x0i + x2i;
		a[idx1] = x0r - x2r;
		a[idx1 + 1] = x0i - x2i;
		a[idx2] = x1r - x3i;
		a[idx2 + 1] = x1i + x3r;
		a[idx3] = x1r + x3i;
		a[idx3 + 1] = x1i - x3r;
		wn4r = w[startw + 1];
		csc1 = w[startw + 2];
		csc3 = w[startw + 3];
		wd1r = 1;
		wd1i = 0;
		wd3r = 1;
		wd3i = 0;
		k = 0;
		for (j = 2; j < mh - 2; j += 4) {
			k += 4;
			idx4 = startw + k;
			wk1r = csc1 * (wd1r + w[idx4]);
			wk1i = csc1 * (wd1i + w[idx4 + 1]);
			wk3r = csc3 * (wd3r + w[idx4 + 2]);
			wk3i = csc3 * (wd3i + w[idx4 + 3]);
			wd1r = w[idx4];
			wd1i = w[idx4 + 1];
			wd3r = w[idx4 + 2];
			wd3i = w[idx4 + 3];
			j1 = j + m;
			j2 = j1 + m;
			j3 = j2 + m;
			idx1 = starta + j1;
			idx2 = starta + j2;
			idx3 = starta + j3;
			idx5 = starta + j;
			x0r = a[idx5] + a[idx2];
			x0i = a[idx5 + 1] + a[idx2 + 1];
			x1r = a[idx5] - a[idx2];
			x1i = a[idx5 + 1] - a[idx2 + 1];
			y0r = a[idx5 + 2] + a[idx2 + 2];
			y0i = a[idx5 + 3] + a[idx2 + 3];
			y1r = a[idx5 + 2] - a[idx2 + 2];
			y1i = a[idx5 + 3] - a[idx2 + 3];
			x2r = a[idx1] + a[idx3];
			x2i = a[idx1 + 1] + a[idx3 + 1];
			x3r = a[idx1] - a[idx3];
			x3i = a[idx1 + 1] - a[idx3 + 1];
			y2r = a[idx1 + 2] + a[idx3 + 2];
			y2i = a[idx1 + 3] + a[idx3 + 3];
			y3r = a[idx1 + 2] - a[idx3 + 2];
			y3i = a[idx1 + 3] - a[idx3 + 3];
			a[idx5] = x0r + x2r;
			a[idx5 + 1] = x0i + x2i;
			a[idx5 + 2] = y0r + y2r;
			a[idx5 + 3] = y0i + y2i;
			a[idx1] = x0r - x2r;
			a[idx1 + 1] = x0i - x2i;
			a[idx1 + 2] = y0r - y2r;
			a[idx1 + 3] = y0i - y2i;
			x0r = x1r - x3i;
			x0i = x1i + x3r;
			a[idx2] = wk1r * x0r - wk1i * x0i;
			a[idx2 + 1] = wk1r * x0i + wk1i * x0r;
			x0r = y1r - y3i;
			x0i = y1i + y3r;
			a[idx2 + 2] = wd1r * x0r - wd1i * x0i;
			a[idx2 + 3] = wd1r * x0i + wd1i * x0r;
			x0r = x1r + x3i;
			x0i = x1i - x3r;
			a[idx3] = wk3r * x0r + wk3i * x0i;
			a[idx3 + 1] = wk3r * x0i - wk3i * x0r;
			x0r = y1r + y3i;
			x0i = y1i - y3r;
			a[idx3 + 2] = wd3r * x0r + wd3i * x0i;
			a[idx3 + 3] = wd3r * x0i - wd3i * x0r;
			j0 = m - j;
			j1 = j0 + m;
			j2 = j1 + m;
			j3 = j2 + m;
			idx0 = starta + j0;
			idx1 = starta + j1;
			idx2 = starta + j2;
			idx3 = starta + j3;
			x0r = a[idx0] + a[idx2];
			x0i = a[idx0 + 1] + a[idx2 + 1];
			x1r = a[idx0] - a[idx2];
			x1i = a[idx0 + 1] - a[idx2 + 1];
			y0r = a[idx0 - 2] + a[idx2 - 2];
			y0i = a[idx0 - 1] + a[idx2 - 1];
			y1r = a[idx0 - 2] - a[idx2 - 2];
			y1i = a[idx0 - 1] - a[idx2 - 1];
			x2r = a[idx1] + a[idx3];
			x2i = a[idx1 + 1] + a[idx3 + 1];
			x3r = a[idx1] - a[idx3];
			x3i = a[idx1 + 1] - a[idx3 + 1];
			y2r = a[idx1 - 2] + a[idx3 - 2];
			y2i = a[idx1 - 1] + a[idx3 - 1];
			y3r = a[idx1 - 2] - a[idx3 - 2];
			y3i = a[idx1 - 1] - a[idx3 - 1];
			a[idx0] = x0r + x2r;
			a[idx0 + 1] = x0i + x2i;
			a[idx0 - 2] = y0r + y2r;
			a[idx0 - 1] = y0i + y2i;
			a[idx1] = x0r - x2r;
			a[idx1 + 1] = x0i - x2i;
			a[idx1 - 2] = y0r - y2r;
			a[idx1 - 1] = y0i - y2i;
			x0r = x1r - x3i;
			x0i = x1i + x3r;
			a[idx2] = wk1i * x0r - wk1r * x0i;
			a[idx2 + 1] = wk1i * x0i + wk1r * x0r;
			x0r = y1r - y3i;
			x0i = y1i + y3r;
			a[idx2 - 2] = wd1i * x0r - wd1r * x0i;
			a[idx2 - 1] = wd1i * x0i + wd1r * x0r;
			x0r = x1r + x3i;
			x0i = x1i - x3r;
			a[idx3] = wk3i * x0r + wk3r * x0i;
			a[idx3 + 1] = wk3i * x0i - wk3r * x0r;
			x0r = y1r + y3i;
			x0i = y1i - y3r;
			a[starta + j3 - 2] = wd3i * x0r + wd3r * x0i;
			a[starta + j3 - 1] = wd3i * x0i - wd3r * x0r;
		}
		wk1r = csc1 * (wd1r + wn4r);
		wk1i = csc1 * (wd1i + wn4r);
		wk3r = csc3 * (wd3r - wn4r);
		wk3i = csc3 * (wd3i - wn4r);
		j0 = mh;
		j1 = j0 + m;
		j2 = j1 + m;
		j3 = j2 + m;
		idx0 = starta + j0;
		idx1 = starta + j1;
		idx2 = starta + j2;
		idx3 = starta + j3;
		x0r = a[idx0 - 2] + a[idx2 - 2];
		x0i = a[idx0 - 1] + a[idx2 - 1];
		x1r = a[idx0 - 2] - a[idx2 - 2];
		x1i = a[idx0 - 1] - a[idx2 - 1];
		x2r = a[idx1 - 2] + a[idx3 - 2];
		x2i = a[idx1 - 1] + a[idx3 - 1];
		x3r = a[idx1 - 2] - a[idx3 - 2];
		x3i = a[idx1 - 1] - a[idx3 - 1];
		a[idx0 - 2] = x0r + x2r;
		a[idx0 - 1] = x0i + x2i;
		a[idx1 - 2] = x0r - x2r;
		a[idx1 - 1] = x0i - x2i;
		x0r = x1r - x3i;
		x0i = x1i + x3r;
		a[idx2 - 2] = wk1r * x0r - wk1i * x0i;
		a[idx2 - 1] = wk1r * x0i + wk1i * x0r;
		x0r = x1r + x3i;
		x0i = x1i - x3r;
		a[idx3 - 2] = wk3r * x0r + wk3i * x0i;
		a[idx3 - 1] = wk3r * x0i - wk3i * x0r;
		x0r = a[idx0] + a[idx2];
		x0i = a[idx0 + 1] + a[idx2 + 1];
		x1r = a[idx0] - a[idx2];
		x1i = a[idx0 + 1] - a[idx2 + 1];
		x2r = a[idx1] + a[idx3];
		x2i = a[idx1 + 1] + a[idx3 + 1];
		x3r = a[idx1] - a[idx3];
		x3i = a[idx1 + 1] - a[idx3 + 1];
		a[idx0] = x0r + x2r;
		a[idx0 + 1] = x0i + x2i;
		a[idx1] = x0r - x2r;
		a[idx1 + 1] = x0i - x2i;
		x0r = x1r - x3i;
		x0i = x1i + x3r;
		a[idx2] = wn4r * (x0r - x0i);
		a[idx2 + 1] = wn4r * (x0i + x0r);
		x0r = x1r + x3i;
		x0i = x1i - x3r;
		a[idx3] = -wn4r * (x0r + x0i);
		a[idx3 + 1] = -wn4r * (x0i - x0r);
		x0r = a[idx0 + 2] + a[idx2 + 2];
		x0i = a[idx0 + 3] + a[idx2 + 3];
		x1r = a[idx0 + 2] - a[idx2 + 2];
		x1i = a[idx0 + 3] - a[idx2 + 3];
		x2r = a[idx1 + 2] + a[idx3 + 2];
		x2i = a[idx1 + 3] + a[idx3 + 3];
		x3r = a[idx1 + 2] - a[idx3 + 2];
		x3i = a[idx1 + 3] - a[idx3 + 3];
		a[idx0 + 2] = x0r + x2r;
		a[idx0 + 3] = x0i + x2i;
		a[idx1 + 2] = x0r - x2r;
		a[idx1 + 3] = x0i - x2i;
		x0r = x1r - x3i;
		x0i = x1i + x3r;
		a[idx2 + 2] = wk1i * x0r - wk1r * x0i;
		a[idx2 + 3] = wk1i * x0i + wk1r * x0r;
		x0r = x1r + x3i;
		x0i = x1i - x3r;
		a[idx3 + 2] = wk3i * x0r + wk3r * x0i;
		a[idx3 + 3] = wk3i * x0i - wk3r * x0r;
	}

	private void cftb1st(int n, float[] a, int starta, float[] w, int startw) {
		int j, j0, j1, j2, j3, k, m, mh;
		float wn4r, csc1, csc3, wk1r, wk1i, wk3r, wk3i, wd1r, wd1i, wd3r, wd3i;
		float x0r, x0i, x1r, x1i, x2r, x2i, x3r, x3i, y0r, y0i, y1r, y1i, y2r, y2i, y3r, y3i;
		int idx0, idx1, idx2, idx3, idx4, idx5;
		mh = n >> 3;
		m = 2 * mh;
		j1 = m;
		j2 = j1 + m;
		j3 = j2 + m;
		idx1 = starta + j1;
		idx2 = starta + j2;
		idx3 = starta + j3;

		x0r = a[starta] + a[idx2];
		x0i = -a[starta + 1] - a[idx2 + 1];
		x1r = a[starta] - a[idx2];
		x1i = -a[starta + 1] + a[idx2 + 1];
		x2r = a[idx1] + a[idx3];
		x2i = a[idx1 + 1] + a[idx3 + 1];
		x3r = a[idx1] - a[idx3];
		x3i = a[idx1 + 1] - a[idx3 + 1];
		a[starta] = x0r + x2r;
		a[starta + 1] = x0i - x2i;
		a[idx1] = x0r - x2r;
		a[idx1 + 1] = x0i + x2i;
		a[idx2] = x1r + x3i;
		a[idx2 + 1] = x1i + x3r;
		a[idx3] = x1r - x3i;
		a[idx3 + 1] = x1i - x3r;
		wn4r = w[startw + 1];
		csc1 = w[startw + 2];
		csc3 = w[startw + 3];
		wd1r = 1;
		wd1i = 0;
		wd3r = 1;
		wd3i = 0;
		k = 0;
		for (j = 2; j < mh - 2; j += 4) {
			k += 4;
			idx4 = startw + k;
			wk1r = csc1 * (wd1r + w[idx4]);
			wk1i = csc1 * (wd1i + w[idx4 + 1]);
			wk3r = csc3 * (wd3r + w[idx4 + 2]);
			wk3i = csc3 * (wd3i + w[idx4 + 3]);
			wd1r = w[idx4];
			wd1i = w[idx4 + 1];
			wd3r = w[idx4 + 2];
			wd3i = w[idx4 + 3];
			j1 = j + m;
			j2 = j1 + m;
			j3 = j2 + m;
			idx1 = starta + j1;
			idx2 = starta + j2;
			idx3 = starta + j3;
			idx5 = starta + j;
			x0r = a[idx5] + a[idx2];
			x0i = -a[idx5 + 1] - a[idx2 + 1];
			x1r = a[idx5] - a[starta + j2];
			x1i = -a[idx5 + 1] + a[idx2 + 1];
			y0r = a[idx5 + 2] + a[idx2 + 2];
			y0i = -a[idx5 + 3] - a[idx2 + 3];
			y1r = a[idx5 + 2] - a[idx2 + 2];
			y1i = -a[idx5 + 3] + a[idx2 + 3];
			x2r = a[idx1] + a[idx3];
			x2i = a[idx1 + 1] + a[idx3 + 1];
			x3r = a[idx1] - a[idx3];
			x3i = a[idx1 + 1] - a[idx3 + 1];
			y2r = a[idx1 + 2] + a[idx3 + 2];
			y2i = a[idx1 + 3] + a[idx3 + 3];
			y3r = a[idx1 + 2] - a[idx3 + 2];
			y3i = a[idx1 + 3] - a[idx3 + 3];
			a[idx5] = x0r + x2r;
			a[idx5 + 1] = x0i - x2i;
			a[idx5 + 2] = y0r + y2r;
			a[idx5 + 3] = y0i - y2i;
			a[idx1] = x0r - x2r;
			a[idx1 + 1] = x0i + x2i;
			a[idx1 + 2] = y0r - y2r;
			a[idx1 + 3] = y0i + y2i;
			x0r = x1r + x3i;
			x0i = x1i + x3r;
			a[idx2] = wk1r * x0r - wk1i * x0i;
			a[idx2 + 1] = wk1r * x0i + wk1i * x0r;
			x0r = y1r + y3i;
			x0i = y1i + y3r;
			a[idx2 + 2] = wd1r * x0r - wd1i * x0i;
			a[idx2 + 3] = wd1r * x0i + wd1i * x0r;
			x0r = x1r - x3i;
			x0i = x1i - x3r;
			a[idx3] = wk3r * x0r + wk3i * x0i;
			a[idx3 + 1] = wk3r * x0i - wk3i * x0r;
			x0r = y1r - y3i;
			x0i = y1i - y3r;
			a[idx3 + 2] = wd3r * x0r + wd3i * x0i;
			a[idx3 + 3] = wd3r * x0i - wd3i * x0r;
			j0 = m - j;
			j1 = j0 + m;
			j2 = j1 + m;
			j3 = j2 + m;
			idx0 = starta + j0;
			idx1 = starta + j1;
			idx2 = starta + j2;
			idx3 = starta + j3;
			x0r = a[idx0] + a[idx2];
			x0i = -a[idx0 + 1] - a[idx2 + 1];
			x1r = a[idx0] - a[idx2];
			x1i = -a[idx0 + 1] + a[idx2 + 1];
			y0r = a[idx0 - 2] + a[idx2 - 2];
			y0i = -a[idx0 - 1] - a[idx2 - 1];
			y1r = a[idx0 - 2] - a[idx2 - 2];
			y1i = -a[idx0 - 1] + a[idx2 - 1];
			x2r = a[idx1] + a[idx3];
			x2i = a[idx1 + 1] + a[idx3 + 1];
			x3r = a[idx1] - a[idx3];
			x3i = a[idx1 + 1] - a[idx3 + 1];
			y2r = a[idx1 - 2] + a[idx3 - 2];
			y2i = a[idx1 - 1] + a[idx3 - 1];
			y3r = a[idx1 - 2] - a[idx3 - 2];
			y3i = a[idx1 - 1] - a[idx3 - 1];
			a[idx0] = x0r + x2r;
			a[idx0 + 1] = x0i - x2i;
			a[idx0 - 2] = y0r + y2r;
			a[idx0 - 1] = y0i - y2i;
			a[idx1] = x0r - x2r;
			a[idx1 + 1] = x0i + x2i;
			a[idx1 - 2] = y0r - y2r;
			a[idx1 - 1] = y0i + y2i;
			x0r = x1r + x3i;
			x0i = x1i + x3r;
			a[idx2] = wk1i * x0r - wk1r * x0i;
			a[idx2 + 1] = wk1i * x0i + wk1r * x0r;
			x0r = y1r + y3i;
			x0i = y1i + y3r;
			a[idx2 - 2] = wd1i * x0r - wd1r * x0i;
			a[idx2 - 1] = wd1i * x0i + wd1r * x0r;
			x0r = x1r - x3i;
			x0i = x1i - x3r;
			a[idx3] = wk3i * x0r + wk3r * x0i;
			a[idx3 + 1] = wk3i * x0i - wk3r * x0r;
			x0r = y1r - y3i;
			x0i = y1i - y3r;
			a[idx3 - 2] = wd3i * x0r + wd3r * x0i;
			a[idx3 - 1] = wd3i * x0i - wd3r * x0r;
		}
		wk1r = csc1 * (wd1r + wn4r);
		wk1i = csc1 * (wd1i + wn4r);
		wk3r = csc3 * (wd3r - wn4r);
		wk3i = csc3 * (wd3i - wn4r);
		j0 = mh;
		j1 = j0 + m;
		j2 = j1 + m;
		j3 = j2 + m;
		idx0 = starta + j0;
		idx1 = starta + j1;
		idx2 = starta + j2;
		idx3 = starta + j3;
		x0r = a[idx0 - 2] + a[idx2 - 2];
		x0i = -a[idx0 - 1] - a[idx2 - 1];
		x1r = a[idx0 - 2] - a[idx2 - 2];
		x1i = -a[idx0 - 1] + a[idx2 - 1];
		x2r = a[idx1 - 2] + a[idx3 - 2];
		x2i = a[idx1 - 1] + a[idx3 - 1];
		x3r = a[idx1 - 2] - a[idx3 - 2];
		x3i = a[idx1 - 1] - a[idx3 - 1];
		a[idx0 - 2] = x0r + x2r;
		a[idx0 - 1] = x0i - x2i;
		a[idx1 - 2] = x0r - x2r;
		a[idx1 - 1] = x0i + x2i;
		x0r = x1r + x3i;
		x0i = x1i + x3r;
		a[idx2 - 2] = wk1r * x0r - wk1i * x0i;
		a[idx2 - 1] = wk1r * x0i + wk1i * x0r;
		x0r = x1r - x3i;
		x0i = x1i - x3r;
		a[idx3 - 2] = wk3r * x0r + wk3i * x0i;
		a[idx3 - 1] = wk3r * x0i - wk3i * x0r;
		x0r = a[idx0] + a[idx2];
		x0i = -a[idx0 + 1] - a[idx2 + 1];
		x1r = a[idx0] - a[idx2];
		x1i = -a[idx0 + 1] + a[idx2 + 1];
		x2r = a[idx1] + a[idx3];
		x2i = a[idx1 + 1] + a[idx3 + 1];
		x3r = a[idx1] - a[idx3];
		x3i = a[idx1 + 1] - a[idx3 + 1];
		a[idx0] = x0r + x2r;
		a[idx0 + 1] = x0i - x2i;
		a[idx1] = x0r - x2r;
		a[idx1 + 1] = x0i + x2i;
		x0r = x1r + x3i;
		x0i = x1i + x3r;
		a[idx2] = wn4r * (x0r - x0i);
		a[idx2 + 1] = wn4r * (x0i + x0r);
		x0r = x1r - x3i;
		x0i = x1i - x3r;
		a[idx3] = -wn4r * (x0r + x0i);
		a[idx3 + 1] = -wn4r * (x0i - x0r);
		x0r = a[idx0 + 2] + a[idx2 + 2];
		x0i = -a[idx0 + 3] - a[idx2 + 3];
		x1r = a[idx0 + 2] - a[idx2 + 2];
		x1i = -a[idx0 + 3] + a[idx2 + 3];
		x2r = a[idx1 + 2] + a[idx3 + 2];
		x2i = a[idx1 + 3] + a[idx3 + 3];
		x3r = a[idx1 + 2] - a[idx3 + 2];
		x3i = a[idx1 + 3] - a[idx3 + 3];
		a[idx0 + 2] = x0r + x2r;
		a[idx0 + 3] = x0i - x2i;
		a[idx1 + 2] = x0r - x2r;
		a[idx1 + 3] = x0i + x2i;
		x0r = x1r + x3i;
		x0i = x1i + x3r;
		a[idx2 + 2] = wk1i * x0r - wk1r * x0i;
		a[idx2 + 3] = wk1i * x0i + wk1r * x0r;
		x0r = x1r - x3i;
		x0i = x1i - x3r;
		a[idx3 + 2] = wk3i * x0r + wk3r * x0i;
		a[idx3 + 3] = wk3i * x0i - wk3r * x0r;
	}

	private void cftrec4_th(final int n, final float[] a, final int starta, final int nw, final float[] w) {
		int i;
		int idiv4, m, nthread;
		int idx = 0;
		nthread = 2;
		idiv4 = 0;
		m = n >> 1;
		if (n > DCT1D_4THREADS_BEGIN_N) {
			nthread = 4;
			idiv4 = 1;
			m >>= 1;
		}
		Future[] futures = new Future[nthread];
		final int glob_m = m;
		for (i = 0; i < nthread; i++) {
			final int loc_starta = starta + i * m;
			if (i != idiv4) {
				futures[idx++] = Utils.threadPool.submit(new Runnable() {
					public void run() {
						int isplt, j, k, m;
						int idx1 = loc_starta + glob_m;
						m = n;
						while (m > 512) {
							m >>= 2;
							cftmdl1(m, a, idx1 - m, w, nw - (m >> 1));
						}
						cftleaf(m, 1, a, idx1 - m, nw, w);
						k = 0;
						int idx2 = loc_starta - m;
						for (j = glob_m - m; j > 0; j -= m) {
							k++;
							isplt = cfttree(m, j, k, a, loc_starta, nw, w);
							cftleaf(m, isplt, a, idx2 + j, nw, w);
						}
					}
				});
			} else {
				futures[idx++] = Utils.threadPool.submit(new Runnable() {
					public void run() {
						int isplt, j, k, m;
						int idx1 = loc_starta + glob_m;
						k = 1;
						m = n;
						while (m > 512) {
							m >>= 2;
							k <<= 2;
							cftmdl2(m, a, idx1 - m, w, nw - m);
						}
						cftleaf(m, 0, a, idx1 - m, nw, w);
						k >>= 1;
						int idx2 = loc_starta - m;
						for (j = glob_m - m; j > 0; j -= m) {
							k++;
							isplt = cfttree(m, j, k, a, loc_starta, nw, w);
							cftleaf(m, isplt, a, idx2 + j, nw, w);
						}
					}
				});
			}
		}
		try {
			for (int j = 0; j < nthread; j++) {
				futures[j].get();
			}
		} catch (ExecutionException ex) {
			ex.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void cftrec4(int n, float[] a, int starta, int nw, float[] w) {
		int isplt, j, k, m;
		int idx1 = starta + n;
		m = n;
		while (m > 512) {
			m >>= 2;
			cftmdl1(m, a, idx1 - m, w, nw - (m >> 1));
		}
		cftleaf(m, 1, a, idx1 - m, nw, w);
		k = 0;
		int idx2 = starta - m;
		for (j = n - m; j > 0; j -= m) {
			k++;
			isplt = cfttree(m, j, k, a, starta, nw, w);
			cftleaf(m, isplt, a, idx2 + j, nw, w);
		}
	}

	private int cfttree(int n, int j, int k, float[] a, int starta, int nw, float[] w) {
		int i, isplt, m;
		int idx1 = starta - n;
		if ((k & 3) != 0) {
			isplt = k & 1;
			if (isplt != 0) {
				cftmdl1(n, a, idx1 + j, w, nw - (n >> 1));
			} else {
				cftmdl2(n, a, idx1 + j, w, nw - n);
			}
		} else {
			m = n;
			for (i = k; (i & 3) == 0; i >>= 2) {
				m <<= 2;
			}
			isplt = i & 1;
			int idx2 = starta + j;
			if (isplt != 0) {
				while (m > 128) {
					cftmdl1(m, a, idx2 - m, w, nw - (m >> 1));
					m >>= 2;
				}
			} else {
				while (m > 128) {
					cftmdl2(m, a, idx2 - m, w, nw - m);
					m >>= 2;
				}
			}
		}
		return isplt;
	}

	private void cftleaf(int n, int isplt, float[] a, int starta, int nw, float[] w) {
		if (n == 512) {
			cftmdl1(128, a, starta, w, nw - 64);
			cftf161(a, starta, w, nw - 8);
			cftf162(a, starta + 32, w, nw - 32);
			cftf161(a, starta + 64, w, nw - 8);
			cftf161(a, starta + 96, w, nw - 8);
			cftmdl2(128, a, starta + 128, w, nw - 128);
			cftf161(a, starta + 128, w, nw - 8);
			cftf162(a, starta + 160, w, nw - 32);
			cftf161(a, starta + 192, w, nw - 8);
			cftf162(a, starta + 224, w, nw - 32);
			cftmdl1(128, a, starta + 256, w, nw - 64);
			cftf161(a, starta + 256, w, nw - 8);
			cftf162(a, starta + 288, w, nw - 32);
			cftf161(a, starta + 320, w, nw - 8);
			cftf161(a, starta + 352, w, nw - 8);
			if (isplt != 0) {
				cftmdl1(128, a, starta + 384, w, nw - 64);
				cftf161(a, starta + 480, w, nw - 8);
			} else {
				cftmdl2(128, a, starta + 384, w, nw - 128);
				cftf162(a, starta + 480, w, nw - 32);
			}
			cftf161(a, starta + 384, w, nw - 8);
			cftf162(a, starta + 416, w, nw - 32);
			cftf161(a, starta + 448, w, nw - 8);
		} else {
			cftmdl1(64, a, starta, w, nw - 32);
			cftf081(a, starta, w, nw - 8);
			cftf082(a, starta + 16, w, nw - 8);
			cftf081(a, starta + 32, w, nw - 8);
			cftf081(a, starta + 48, w, nw - 8);
			cftmdl2(64, a, starta + 64, w, nw - 64);
			cftf081(a, starta + 64, w, nw - 8);
			cftf082(a, starta + 80, w, nw - 8);
			cftf081(a, starta + 96, w, nw - 8);
			cftf082(a, starta + 112, w, nw - 8);
			cftmdl1(64, a, starta + 128, w, nw - 32);
			cftf081(a, starta + 128, w, nw - 8);
			cftf082(a, starta + 144, w, nw - 8);
			cftf081(a, starta + 160, w, nw - 8);
			cftf081(a, starta + 176, w, nw - 8);
			if (isplt != 0) {
				cftmdl1(64, a, starta + 192, w, nw - 32);
				cftf081(a, starta + 240, w, nw - 8);
			} else {
				cftmdl2(64, a, starta + 192, w, nw - 64);
				cftf082(a, starta + 240, w, nw - 8);
			}
			cftf081(a, starta + 192, w, nw - 8);
			cftf082(a, starta + 208, w, nw - 8);
			cftf081(a, starta + 224, w, nw - 8);
		}
	}

	private void cftmdl1(int n, float[] a, int starta, float[] w, int startw) {
		int j, j0, j1, j2, j3, k, m, mh;
		float wn4r, wk1r, wk1i, wk3r, wk3i;
		float x0r, x0i, x1r, x1i, x2r, x2i, x3r, x3i;
		int idx0, idx1, idx2, idx3, idx4, idx5;

		mh = n >> 3;
		m = 2 * mh;
		j1 = m;
		j2 = j1 + m;
		j3 = j2 + m;
		idx1 = starta + j1;
		idx2 = starta + j2;
		idx3 = starta + j3;
		x0r = a[starta] + a[idx2];
		x0i = a[starta + 1] + a[idx2 + 1];
		x1r = a[starta] - a[idx2];
		x1i = a[starta + 1] - a[idx2 + 1];
		x2r = a[idx1] + a[idx3];
		x2i = a[idx1 + 1] + a[idx3 + 1];
		x3r = a[idx1] - a[idx3];
		x3i = a[idx1 + 1] - a[idx3 + 1];
		a[starta] = x0r + x2r;
		a[starta + 1] = x0i + x2i;
		a[idx1] = x0r - x2r;
		a[idx1 + 1] = x0i - x2i;
		a[idx2] = x1r - x3i;
		a[idx2 + 1] = x1i + x3r;
		a[idx3] = x1r + x3i;
		a[idx3 + 1] = x1i - x3r;
		wn4r = w[startw + 1];
		k = 0;
		for (j = 2; j < mh; j += 2) {
			k += 4;
			idx4 = startw + k;
			wk1r = w[idx4];
			wk1i = w[idx4 + 1];
			wk3r = w[idx4 + 2];
			wk3i = w[idx4 + 3];
			j1 = j + m;
			j2 = j1 + m;
			j3 = j2 + m;
			idx1 = starta + j1;
			idx2 = starta + j2;
			idx3 = starta + j3;
			idx5 = starta + j;
			x0r = a[idx5] + a[idx2];
			x0i = a[idx5 + 1] + a[idx2 + 1];
			x1r = a[idx5] - a[idx2];
			x1i = a[idx5 + 1] - a[idx2 + 1];
			x2r = a[idx1] + a[idx3];
			x2i = a[idx1 + 1] + a[idx3 + 1];
			x3r = a[idx1] - a[idx3];
			x3i = a[idx1 + 1] - a[idx3 + 1];
			a[idx5] = x0r + x2r;
			a[idx5 + 1] = x0i + x2i;
			a[idx1] = x0r - x2r;
			a[idx1 + 1] = x0i - x2i;
			x0r = x1r - x3i;
			x0i = x1i + x3r;
			a[idx2] = wk1r * x0r - wk1i * x0i;
			a[idx2 + 1] = wk1r * x0i + wk1i * x0r;
			x0r = x1r + x3i;
			x0i = x1i - x3r;
			a[idx3] = wk3r * x0r + wk3i * x0i;
			a[idx3 + 1] = wk3r * x0i - wk3i * x0r;
			j0 = m - j;
			j1 = j0 + m;
			j2 = j1 + m;
			j3 = j2 + m;
			idx0 = starta + j0;
			idx1 = starta + j1;
			idx2 = starta + j2;
			idx3 = starta + j3;
			x0r = a[idx0] + a[idx2];
			x0i = a[idx0 + 1] + a[idx2 + 1];
			x1r = a[idx0] - a[idx2];
			x1i = a[idx0 + 1] - a[idx2 + 1];
			x2r = a[idx1] + a[idx3];
			x2i = a[idx1 + 1] + a[idx3 + 1];
			x3r = a[idx1] - a[idx3];
			x3i = a[idx1 + 1] - a[idx3 + 1];
			a[idx0] = x0r + x2r;
			a[idx0 + 1] = x0i + x2i;
			a[idx1] = x0r - x2r;
			a[idx1 + 1] = x0i - x2i;
			x0r = x1r - x3i;
			x0i = x1i + x3r;
			a[idx2] = wk1i * x0r - wk1r * x0i;
			a[idx2 + 1] = wk1i * x0i + wk1r * x0r;
			x0r = x1r + x3i;
			x0i = x1i - x3r;
			a[idx3] = wk3i * x0r + wk3r * x0i;
			a[idx3 + 1] = wk3i * x0i - wk3r * x0r;
		}
		j0 = mh;
		j1 = j0 + m;
		j2 = j1 + m;
		j3 = j2 + m;
		idx0 = starta + j0;
		idx1 = starta + j1;
		idx2 = starta + j2;
		idx3 = starta + j3;
		x0r = a[idx0] + a[idx2];
		x0i = a[idx0 + 1] + a[idx2 + 1];
		x1r = a[idx0] - a[idx2];
		x1i = a[idx0 + 1] - a[idx2 + 1];
		x2r = a[idx1] + a[idx3];
		x2i = a[idx1 + 1] + a[idx3 + 1];
		x3r = a[idx1] - a[idx3];
		x3i = a[idx1 + 1] - a[idx3 + 1];
		a[idx0] = x0r + x2r;
		a[idx0 + 1] = x0i + x2i;
		a[idx1] = x0r - x2r;
		a[idx1 + 1] = x0i - x2i;
		x0r = x1r - x3i;
		x0i = x1i + x3r;
		a[idx2] = wn4r * (x0r - x0i);
		a[idx2 + 1] = wn4r * (x0i + x0r);
		x0r = x1r + x3i;
		x0i = x1i - x3r;
		a[idx3] = -wn4r * (x0r + x0i);
		a[idx3 + 1] = -wn4r * (x0i - x0r);
	}

	private void cftmdl2(int n, float[] a, int starta, float[] w, int startw) {
		int j, j0, j1, j2, j3, k, kr, m, mh;
		float wn4r, wk1r, wk1i, wk3r, wk3i, wd1r, wd1i, wd3r, wd3i;
		float x0r, x0i, x1r, x1i, x2r, x2i, x3r, x3i, y0r, y0i, y2r, y2i;
		int idx0, idx1, idx2, idx3, idx4, idx5, idx6;

		mh = n >> 3;
		m = 2 * mh;
		wn4r = w[startw + 1];
		j1 = m;
		j2 = j1 + m;
		j3 = j2 + m;
		idx1 = starta + j1;
		idx2 = starta + j2;
		idx3 = starta + j3;
		x0r = a[starta] - a[idx2 + 1];
		x0i = a[starta + 1] + a[idx2];
		x1r = a[starta] + a[idx2 + 1];
		x1i = a[starta + 1] - a[idx2];
		x2r = a[idx1] - a[idx3 + 1];
		x2i = a[idx1 + 1] + a[idx3];
		x3r = a[idx1] + a[idx3 + 1];
		x3i = a[idx1 + 1] - a[idx3];
		y0r = wn4r * (x2r - x2i);
		y0i = wn4r * (x2i + x2r);
		a[starta] = x0r + y0r;
		a[starta + 1] = x0i + y0i;
		a[idx1] = x0r - y0r;
		a[idx1 + 1] = x0i - y0i;
		y0r = wn4r * (x3r - x3i);
		y0i = wn4r * (x3i + x3r);
		a[idx2] = x1r - y0i;
		a[idx2 + 1] = x1i + y0r;
		a[idx3] = x1r + y0i;
		a[idx3 + 1] = x1i - y0r;
		k = 0;
		kr = 2 * m;
		for (j = 2; j < mh; j += 2) {
			k += 4;
			idx4 = startw + k;
			wk1r = w[idx4];
			wk1i = w[idx4 + 1];
			wk3r = w[idx4 + 2];
			wk3i = w[idx4 + 3];
			kr -= 4;
			idx5 = startw + kr;
			wd1i = w[idx5];
			wd1r = w[idx5 + 1];
			wd3i = w[idx5 + 2];
			wd3r = w[idx5 + 3];
			j1 = j + m;
			j2 = j1 + m;
			j3 = j2 + m;
			idx1 = starta + j1;
			idx2 = starta + j2;
			idx3 = starta + j3;
			idx6 = starta + j;
			x0r = a[idx6] - a[idx2 + 1];
			x0i = a[idx6 + 1] + a[idx2];
			x1r = a[idx6] + a[idx2 + 1];
			x1i = a[idx6 + 1] - a[idx2];
			x2r = a[idx1] - a[idx3 + 1];
			x2i = a[idx1 + 1] + a[idx3];
			x3r = a[idx1] + a[idx3 + 1];
			x3i = a[idx1 + 1] - a[idx3];
			y0r = wk1r * x0r - wk1i * x0i;
			y0i = wk1r * x0i + wk1i * x0r;
			y2r = wd1r * x2r - wd1i * x2i;
			y2i = wd1r * x2i + wd1i * x2r;
			a[idx6] = y0r + y2r;
			a[idx6 + 1] = y0i + y2i;
			a[idx1] = y0r - y2r;
			a[idx1 + 1] = y0i - y2i;
			y0r = wk3r * x1r + wk3i * x1i;
			y0i = wk3r * x1i - wk3i * x1r;
			y2r = wd3r * x3r + wd3i * x3i;
			y2i = wd3r * x3i - wd3i * x3r;
			a[idx2] = y0r + y2r;
			a[idx2 + 1] = y0i + y2i;
			a[idx3] = y0r - y2r;
			a[idx3 + 1] = y0i - y2i;
			j0 = m - j;
			j1 = j0 + m;
			j2 = j1 + m;
			j3 = j2 + m;
			idx0 = starta + j0;
			idx1 = starta + j1;
			idx2 = starta + j2;
			idx3 = starta + j3;
			x0r = a[idx0] - a[idx2 + 1];
			x0i = a[idx0 + 1] + a[idx2];
			x1r = a[idx0] + a[idx2 + 1];
			x1i = a[idx0 + 1] - a[idx2];
			x2r = a[idx1] - a[idx3 + 1];
			x2i = a[idx1 + 1] + a[idx3];
			x3r = a[idx1] + a[idx3 + 1];
			x3i = a[idx1 + 1] - a[idx3];
			y0r = wd1i * x0r - wd1r * x0i;
			y0i = wd1i * x0i + wd1r * x0r;
			y2r = wk1i * x2r - wk1r * x2i;
			y2i = wk1i * x2i + wk1r * x2r;
			a[idx0] = y0r + y2r;
			a[idx0 + 1] = y0i + y2i;
			a[idx1] = y0r - y2r;
			a[idx1 + 1] = y0i - y2i;
			y0r = wd3i * x1r + wd3r * x1i;
			y0i = wd3i * x1i - wd3r * x1r;
			y2r = wk3i * x3r + wk3r * x3i;
			y2i = wk3i * x3i - wk3r * x3r;
			a[idx2] = y0r + y2r;
			a[idx2 + 1] = y0i + y2i;
			a[idx3] = y0r - y2r;
			a[idx3 + 1] = y0i - y2i;
		}
		wk1r = w[startw + m];
		wk1i = w[startw + m + 1];
		j0 = mh;
		j1 = j0 + m;
		j2 = j1 + m;
		j3 = j2 + m;
		idx0 = starta + j0;
		idx1 = starta + j1;
		idx2 = starta + j2;
		idx3 = starta + j3;
		x0r = a[idx0] - a[idx2 + 1];
		x0i = a[idx0 + 1] + a[idx2];
		x1r = a[idx0] + a[idx2 + 1];
		x1i = a[idx0 + 1] - a[idx2];
		x2r = a[idx1] - a[idx3 + 1];
		x2i = a[idx1 + 1] + a[idx3];
		x3r = a[idx1] + a[idx3 + 1];
		x3i = a[idx1 + 1] - a[idx3];
		y0r = wk1r * x0r - wk1i * x0i;
		y0i = wk1r * x0i + wk1i * x0r;
		y2r = wk1i * x2r - wk1r * x2i;
		y2i = wk1i * x2i + wk1r * x2r;
		a[idx0] = y0r + y2r;
		a[idx0 + 1] = y0i + y2i;
		a[idx1] = y0r - y2r;
		a[idx1 + 1] = y0i - y2i;
		y0r = wk1i * x1r - wk1r * x1i;
		y0i = wk1i * x1i + wk1r * x1r;
		y2r = wk1r * x3r - wk1i * x3i;
		y2i = wk1r * x3i + wk1i * x3r;
		a[idx2] = y0r - y2r;
		a[idx2 + 1] = y0i - y2i;
		a[idx3] = y0r + y2r;
		a[idx3 + 1] = y0i + y2i;
	}

	private void cftfx41(int n, float[] a, int starta, int nw, float[] w) {
		if (n == 128) {
			cftf161(a, starta, w, nw - 8);
			cftf162(a, starta + 32, w, nw - 32);
			cftf161(a, starta + 64, w, nw - 8);
			cftf161(a, starta + 96, w, nw - 8);
		} else {
			cftf081(a, starta, w, nw - 8);
			cftf082(a, starta + 16, w, nw - 8);
			cftf081(a, starta + 32, w, nw - 8);
			cftf081(a, starta + 48, w, nw - 8);
		}
	}

	private void cftf161(float[] a, int starta, float[] w, int startw) {
		float wn4r, wk1r, wk1i, x0r, x0i, x1r, x1i, x2r, x2i, x3r, x3i, y0r, y0i, y1r, y1i, y2r, y2i, y3r, y3i, y4r, y4i, y5r, y5i, y6r, y6i, y7r, y7i, y8r, y8i, y9r, y9i, y10r, y10i, y11r, y11i, y12r, y12i, y13r, y13i, y14r, y14i, y15r, y15i;

		wn4r = w[startw + 1];
		wk1r = w[startw + 2];
		wk1i = w[startw + 3];
		x0r = a[starta] + a[starta + 16];
		x0i = a[starta + 1] + a[starta + 17];
		x1r = a[starta] - a[starta + 16];
		x1i = a[starta + 1] - a[starta + 17];
		x2r = a[starta + 8] + a[starta + 24];
		x2i = a[starta + 9] + a[starta + 25];
		x3r = a[starta + 8] - a[starta + 24];
		x3i = a[starta + 9] - a[starta + 25];
		y0r = x0r + x2r;
		y0i = x0i + x2i;
		y4r = x0r - x2r;
		y4i = x0i - x2i;
		y8r = x1r - x3i;
		y8i = x1i + x3r;
		y12r = x1r + x3i;
		y12i = x1i - x3r;
		x0r = a[starta + 2] + a[starta + 18];
		x0i = a[starta + 3] + a[starta + 19];
		x1r = a[starta + 2] - a[starta + 18];
		x1i = a[starta + 3] - a[starta + 19];
		x2r = a[starta + 10] + a[starta + 26];
		x2i = a[starta + 11] + a[starta + 27];
		x3r = a[starta + 10] - a[starta + 26];
		x3i = a[starta + 11] - a[starta + 27];
		y1r = x0r + x2r;
		y1i = x0i + x2i;
		y5r = x0r - x2r;
		y5i = x0i - x2i;
		x0r = x1r - x3i;
		x0i = x1i + x3r;
		y9r = wk1r * x0r - wk1i * x0i;
		y9i = wk1r * x0i + wk1i * x0r;
		x0r = x1r + x3i;
		x0i = x1i - x3r;
		y13r = wk1i * x0r - wk1r * x0i;
		y13i = wk1i * x0i + wk1r * x0r;
		x0r = a[starta + 4] + a[starta + 20];
		x0i = a[starta + 5] + a[starta + 21];
		x1r = a[starta + 4] - a[starta + 20];
		x1i = a[starta + 5] - a[starta + 21];
		x2r = a[starta + 12] + a[starta + 28];
		x2i = a[starta + 13] + a[starta + 29];
		x3r = a[starta + 12] - a[starta + 28];
		x3i = a[starta + 13] - a[starta + 29];
		y2r = x0r + x2r;
		y2i = x0i + x2i;
		y6r = x0r - x2r;
		y6i = x0i - x2i;
		x0r = x1r - x3i;
		x0i = x1i + x3r;
		y10r = wn4r * (x0r - x0i);
		y10i = wn4r * (x0i + x0r);
		x0r = x1r + x3i;
		x0i = x1i - x3r;
		y14r = wn4r * (x0r + x0i);
		y14i = wn4r * (x0i - x0r);
		x0r = a[starta + 6] + a[starta + 22];
		x0i = a[starta + 7] + a[starta + 23];
		x1r = a[starta + 6] - a[starta + 22];
		x1i = a[starta + 7] - a[starta + 23];
		x2r = a[starta + 14] + a[starta + 30];
		x2i = a[starta + 15] + a[starta + 31];
		x3r = a[starta + 14] - a[starta + 30];
		x3i = a[starta + 15] - a[starta + 31];
		y3r = x0r + x2r;
		y3i = x0i + x2i;
		y7r = x0r - x2r;
		y7i = x0i - x2i;
		x0r = x1r - x3i;
		x0i = x1i + x3r;
		y11r = wk1i * x0r - wk1r * x0i;
		y11i = wk1i * x0i + wk1r * x0r;
		x0r = x1r + x3i;
		x0i = x1i - x3r;
		y15r = wk1r * x0r - wk1i * x0i;
		y15i = wk1r * x0i + wk1i * x0r;
		x0r = y12r - y14r;
		x0i = y12i - y14i;
		x1r = y12r + y14r;
		x1i = y12i + y14i;
		x2r = y13r - y15r;
		x2i = y13i - y15i;
		x3r = y13r + y15r;
		x3i = y13i + y15i;
		a[starta + 24] = x0r + x2r;
		a[starta + 25] = x0i + x2i;
		a[starta + 26] = x0r - x2r;
		a[starta + 27] = x0i - x2i;
		a[starta + 28] = x1r - x3i;
		a[starta + 29] = x1i + x3r;
		a[starta + 30] = x1r + x3i;
		a[starta + 31] = x1i - x3r;
		x0r = y8r + y10r;
		x0i = y8i + y10i;
		x1r = y8r - y10r;
		x1i = y8i - y10i;
		x2r = y9r + y11r;
		x2i = y9i + y11i;
		x3r = y9r - y11r;
		x3i = y9i - y11i;
		a[starta + 16] = x0r + x2r;
		a[starta + 17] = x0i + x2i;
		a[starta + 18] = x0r - x2r;
		a[starta + 19] = x0i - x2i;
		a[starta + 20] = x1r - x3i;
		a[starta + 21] = x1i + x3r;
		a[starta + 22] = x1r + x3i;
		a[starta + 23] = x1i - x3r;
		x0r = y5r - y7i;
		x0i = y5i + y7r;
		x2r = wn4r * (x0r - x0i);
		x2i = wn4r * (x0i + x0r);
		x0r = y5r + y7i;
		x0i = y5i - y7r;
		x3r = wn4r * (x0r - x0i);
		x3i = wn4r * (x0i + x0r);
		x0r = y4r - y6i;
		x0i = y4i + y6r;
		x1r = y4r + y6i;
		x1i = y4i - y6r;
		a[starta + 8] = x0r + x2r;
		a[starta + 9] = x0i + x2i;
		a[starta + 10] = x0r - x2r;
		a[starta + 11] = x0i - x2i;
		a[starta + 12] = x1r - x3i;
		a[starta + 13] = x1i + x3r;
		a[starta + 14] = x1r + x3i;
		a[starta + 15] = x1i - x3r;
		x0r = y0r + y2r;
		x0i = y0i + y2i;
		x1r = y0r - y2r;
		x1i = y0i - y2i;
		x2r = y1r + y3r;
		x2i = y1i + y3i;
		x3r = y1r - y3r;
		x3i = y1i - y3i;
		a[starta] = x0r + x2r;
		a[starta + 1] = x0i + x2i;
		a[starta + 2] = x0r - x2r;
		a[starta + 3] = x0i - x2i;
		a[starta + 4] = x1r - x3i;
		a[starta + 5] = x1i + x3r;
		a[starta + 6] = x1r + x3i;
		a[starta + 7] = x1i - x3r;
	}

	private void cftf162(float[] a, int starta, float[] w, int startw) {
		float wn4r, wk1r, wk1i, wk2r, wk2i, wk3r, wk3i, x0r, x0i, x1r, x1i, x2r, x2i, y0r, y0i, y1r, y1i, y2r, y2i, y3r, y3i, y4r, y4i, y5r, y5i, y6r, y6i, y7r, y7i, y8r, y8i, y9r, y9i, y10r, y10i, y11r, y11i, y12r, y12i, y13r, y13i, y14r, y14i, y15r, y15i;

		wn4r = w[startw + 1];
		wk1r = w[startw + 4];
		wk1i = w[startw + 5];
		wk3r = w[startw + 6];
		wk3i = -w[startw + 7];
		wk2r = w[startw + 8];
		wk2i = w[startw + 9];
		x1r = a[starta] - a[starta + 17];
		x1i = a[starta + 1] + a[starta + 16];
		x0r = a[starta + 8] - a[starta + 25];
		x0i = a[starta + 9] + a[starta + 24];
		x2r = wn4r * (x0r - x0i);
		x2i = wn4r * (x0i + x0r);
		y0r = x1r + x2r;
		y0i = x1i + x2i;
		y4r = x1r - x2r;
		y4i = x1i - x2i;
		x1r = a[starta] + a[starta + 17];
		x1i = a[starta + 1] - a[starta + 16];
		x0r = a[starta + 8] + a[starta + 25];
		x0i = a[starta + 9] - a[starta + 24];
		x2r = wn4r * (x0r - x0i);
		x2i = wn4r * (x0i + x0r);
		y8r = x1r - x2i;
		y8i = x1i + x2r;
		y12r = x1r + x2i;
		y12i = x1i - x2r;
		x0r = a[starta + 2] - a[starta + 19];
		x0i = a[starta + 3] + a[starta + 18];
		x1r = wk1r * x0r - wk1i * x0i;
		x1i = wk1r * x0i + wk1i * x0r;
		x0r = a[starta + 10] - a[starta + 27];
		x0i = a[starta + 11] + a[starta + 26];
		x2r = wk3i * x0r - wk3r * x0i;
		x2i = wk3i * x0i + wk3r * x0r;
		y1r = x1r + x2r;
		y1i = x1i + x2i;
		y5r = x1r - x2r;
		y5i = x1i - x2i;
		x0r = a[starta + 2] + a[starta + 19];
		x0i = a[starta + 3] - a[starta + 18];
		x1r = wk3r * x0r - wk3i * x0i;
		x1i = wk3r * x0i + wk3i * x0r;
		x0r = a[starta + 10] + a[starta + 27];
		x0i = a[starta + 11] - a[starta + 26];
		x2r = wk1r * x0r + wk1i * x0i;
		x2i = wk1r * x0i - wk1i * x0r;
		y9r = x1r - x2r;
		y9i = x1i - x2i;
		y13r = x1r + x2r;
		y13i = x1i + x2i;
		x0r = a[starta + 4] - a[starta + 21];
		x0i = a[starta + 5] + a[starta + 20];
		x1r = wk2r * x0r - wk2i * x0i;
		x1i = wk2r * x0i + wk2i * x0r;
		x0r = a[starta + 12] - a[starta + 29];
		x0i = a[starta + 13] + a[starta + 28];
		x2r = wk2i * x0r - wk2r * x0i;
		x2i = wk2i * x0i + wk2r * x0r;
		y2r = x1r + x2r;
		y2i = x1i + x2i;
		y6r = x1r - x2r;
		y6i = x1i - x2i;
		x0r = a[starta + 4] + a[starta + 21];
		x0i = a[starta + 5] - a[starta + 20];
		x1r = wk2i * x0r - wk2r * x0i;
		x1i = wk2i * x0i + wk2r * x0r;
		x0r = a[starta + 12] + a[starta + 29];
		x0i = a[starta + 13] - a[starta + 28];
		x2r = wk2r * x0r - wk2i * x0i;
		x2i = wk2r * x0i + wk2i * x0r;
		y10r = x1r - x2r;
		y10i = x1i - x2i;
		y14r = x1r + x2r;
		y14i = x1i + x2i;
		x0r = a[starta + 6] - a[starta + 23];
		x0i = a[starta + 7] + a[starta + 22];
		x1r = wk3r * x0r - wk3i * x0i;
		x1i = wk3r * x0i + wk3i * x0r;
		x0r = a[starta + 14] - a[starta + 31];
		x0i = a[starta + 15] + a[starta + 30];
		x2r = wk1i * x0r - wk1r * x0i;
		x2i = wk1i * x0i + wk1r * x0r;
		y3r = x1r + x2r;
		y3i = x1i + x2i;
		y7r = x1r - x2r;
		y7i = x1i - x2i;
		x0r = a[starta + 6] + a[starta + 23];
		x0i = a[starta + 7] - a[starta + 22];
		x1r = wk1i * x0r + wk1r * x0i;
		x1i = wk1i * x0i - wk1r * x0r;
		x0r = a[starta + 14] + a[starta + 31];
		x0i = a[starta + 15] - a[starta + 30];
		x2r = wk3i * x0r - wk3r * x0i;
		x2i = wk3i * x0i + wk3r * x0r;
		y11r = x1r + x2r;
		y11i = x1i + x2i;
		y15r = x1r - x2r;
		y15i = x1i - x2i;
		x1r = y0r + y2r;
		x1i = y0i + y2i;
		x2r = y1r + y3r;
		x2i = y1i + y3i;
		a[starta] = x1r + x2r;
		a[starta + 1] = x1i + x2i;
		a[starta + 2] = x1r - x2r;
		a[starta + 3] = x1i - x2i;
		x1r = y0r - y2r;
		x1i = y0i - y2i;
		x2r = y1r - y3r;
		x2i = y1i - y3i;
		a[starta + 4] = x1r - x2i;
		a[starta + 5] = x1i + x2r;
		a[starta + 6] = x1r + x2i;
		a[starta + 7] = x1i - x2r;
		x1r = y4r - y6i;
		x1i = y4i + y6r;
		x0r = y5r - y7i;
		x0i = y5i + y7r;
		x2r = wn4r * (x0r - x0i);
		x2i = wn4r * (x0i + x0r);
		a[starta + 8] = x1r + x2r;
		a[starta + 9] = x1i + x2i;
		a[starta + 10] = x1r - x2r;
		a[starta + 11] = x1i - x2i;
		x1r = y4r + y6i;
		x1i = y4i - y6r;
		x0r = y5r + y7i;
		x0i = y5i - y7r;
		x2r = wn4r * (x0r - x0i);
		x2i = wn4r * (x0i + x0r);
		a[starta + 12] = x1r - x2i;
		a[starta + 13] = x1i + x2r;
		a[starta + 14] = x1r + x2i;
		a[starta + 15] = x1i - x2r;
		x1r = y8r + y10r;
		x1i = y8i + y10i;
		x2r = y9r - y11r;
		x2i = y9i - y11i;
		a[starta + 16] = x1r + x2r;
		a[starta + 17] = x1i + x2i;
		a[starta + 18] = x1r - x2r;
		a[starta + 19] = x1i - x2i;
		x1r = y8r - y10r;
		x1i = y8i - y10i;
		x2r = y9r + y11r;
		x2i = y9i + y11i;
		a[starta + 20] = x1r - x2i;
		a[starta + 21] = x1i + x2r;
		a[starta + 22] = x1r + x2i;
		a[starta + 23] = x1i - x2r;
		x1r = y12r - y14i;
		x1i = y12i + y14r;
		x0r = y13r + y15i;
		x0i = y13i - y15r;
		x2r = wn4r * (x0r - x0i);
		x2i = wn4r * (x0i + x0r);
		a[starta + 24] = x1r + x2r;
		a[starta + 25] = x1i + x2i;
		a[starta + 26] = x1r - x2r;
		a[starta + 27] = x1i - x2i;
		x1r = y12r + y14i;
		x1i = y12i - y14r;
		x0r = y13r - y15i;
		x0i = y13i + y15r;
		x2r = wn4r * (x0r - x0i);
		x2i = wn4r * (x0i + x0r);
		a[starta + 28] = x1r - x2i;
		a[starta + 29] = x1i + x2r;
		a[starta + 30] = x1r + x2i;
		a[starta + 31] = x1i - x2r;
	}

	private void cftf081(float[] a, int starta, float[] w, int startw) {
		float wn4r, x0r, x0i, x1r, x1i, x2r, x2i, x3r, x3i, y0r, y0i, y1r, y1i, y2r, y2i, y3r, y3i, y4r, y4i, y5r, y5i, y6r, y6i, y7r, y7i;

		wn4r = w[startw + 1];
		x0r = a[starta] + a[starta + 8];
		x0i = a[starta + 1] + a[starta + 9];
		x1r = a[starta] - a[starta + 8];
		x1i = a[starta + 1] - a[starta + 9];
		x2r = a[starta + 4] + a[starta + 12];
		x2i = a[starta + 5] + a[starta + 13];
		x3r = a[starta + 4] - a[starta + 12];
		x3i = a[starta + 5] - a[starta + 13];
		y0r = x0r + x2r;
		y0i = x0i + x2i;
		y2r = x0r - x2r;
		y2i = x0i - x2i;
		y1r = x1r - x3i;
		y1i = x1i + x3r;
		y3r = x1r + x3i;
		y3i = x1i - x3r;
		x0r = a[starta + 2] + a[starta + 10];
		x0i = a[starta + 3] + a[starta + 11];
		x1r = a[starta + 2] - a[starta + 10];
		x1i = a[starta + 3] - a[starta + 11];
		x2r = a[starta + 6] + a[starta + 14];
		x2i = a[starta + 7] + a[starta + 15];
		x3r = a[starta + 6] - a[starta + 14];
		x3i = a[starta + 7] - a[starta + 15];
		y4r = x0r + x2r;
		y4i = x0i + x2i;
		y6r = x0r - x2r;
		y6i = x0i - x2i;
		x0r = x1r - x3i;
		x0i = x1i + x3r;
		x2r = x1r + x3i;
		x2i = x1i - x3r;
		y5r = wn4r * (x0r - x0i);
		y5i = wn4r * (x0r + x0i);
		y7r = wn4r * (x2r - x2i);
		y7i = wn4r * (x2r + x2i);
		a[starta + 8] = y1r + y5r;
		a[starta + 9] = y1i + y5i;
		a[starta + 10] = y1r - y5r;
		a[starta + 11] = y1i - y5i;
		a[starta + 12] = y3r - y7i;
		a[starta + 13] = y3i + y7r;
		a[starta + 14] = y3r + y7i;
		a[starta + 15] = y3i - y7r;
		a[starta] = y0r + y4r;
		a[starta + 1] = y0i + y4i;
		a[starta + 2] = y0r - y4r;
		a[starta + 3] = y0i - y4i;
		a[starta + 4] = y2r - y6i;
		a[starta + 5] = y2i + y6r;
		a[starta + 6] = y2r + y6i;
		a[starta + 7] = y2i - y6r;
	}

	private void cftf082(float[] a, int starta, float[] w, int startw) {
		float wn4r, wk1r, wk1i, x0r, x0i, x1r, x1i, y0r, y0i, y1r, y1i, y2r, y2i, y3r, y3i, y4r, y4i, y5r, y5i, y6r, y6i, y7r, y7i;

		wn4r = w[startw + 1];
		wk1r = w[startw + 2];
		wk1i = w[startw + 3];
		y0r = a[starta] - a[starta + 9];
		y0i = a[starta + 1] + a[starta + 8];
		y1r = a[starta] + a[starta + 9];
		y1i = a[starta + 1] - a[starta + 8];
		x0r = a[starta + 4] - a[starta + 13];
		x0i = a[starta + 5] + a[starta + 12];
		y2r = wn4r * (x0r - x0i);
		y2i = wn4r * (x0i + x0r);
		x0r = a[starta + 4] + a[starta + 13];
		x0i = a[starta + 5] - a[starta + 12];
		y3r = wn4r * (x0r - x0i);
		y3i = wn4r * (x0i + x0r);
		x0r = a[starta + 2] - a[starta + 11];
		x0i = a[starta + 3] + a[starta + 10];
		y4r = wk1r * x0r - wk1i * x0i;
		y4i = wk1r * x0i + wk1i * x0r;
		x0r = a[starta + 2] + a[starta + 11];
		x0i = a[starta + 3] - a[starta + 10];
		y5r = wk1i * x0r - wk1r * x0i;
		y5i = wk1i * x0i + wk1r * x0r;
		x0r = a[starta + 6] - a[starta + 15];
		x0i = a[starta + 7] + a[starta + 14];
		y6r = wk1i * x0r - wk1r * x0i;
		y6i = wk1i * x0i + wk1r * x0r;
		x0r = a[starta + 6] + a[starta + 15];
		x0i = a[starta + 7] - a[starta + 14];
		y7r = wk1r * x0r - wk1i * x0i;
		y7i = wk1r * x0i + wk1i * x0r;
		x0r = y0r + y2r;
		x0i = y0i + y2i;
		x1r = y4r + y6r;
		x1i = y4i + y6i;
		a[starta] = x0r + x1r;
		a[starta + 1] = x0i + x1i;
		a[starta + 2] = x0r - x1r;
		a[starta + 3] = x0i - x1i;
		x0r = y0r - y2r;
		x0i = y0i - y2i;
		x1r = y4r - y6r;
		x1i = y4i - y6i;
		a[starta + 4] = x0r - x1i;
		a[starta + 5] = x0i + x1r;
		a[starta + 6] = x0r + x1i;
		a[starta + 7] = x0i - x1r;
		x0r = y1r - y3i;
		x0i = y1i + y3r;
		x1r = y5r - y7r;
		x1i = y5i - y7i;
		a[starta + 8] = x0r + x1r;
		a[starta + 9] = x0i + x1i;
		a[starta + 10] = x0r - x1r;
		a[starta + 11] = x0i - x1i;
		x0r = y1r + y3i;
		x0i = y1i - y3r;
		x1r = y5r + y7r;
		x1i = y5i + y7i;
		a[starta + 12] = x0r - x1i;
		a[starta + 13] = x0i + x1r;
		a[starta + 14] = x0r + x1i;
		a[starta + 15] = x0i - x1r;
	}

	private void cftf040(float[] a, int starta) {
		float x0r, x0i, x1r, x1i, x2r, x2i, x3r, x3i;

		x0r = a[starta] + a[starta + 4];
		x0i = a[starta + 1] + a[starta + 5];
		x1r = a[starta] - a[starta + 4];
		x1i = a[starta + 1] - a[starta + 5];
		x2r = a[starta + 2] + a[starta + 6];
		x2i = a[starta + 3] + a[starta + 7];
		x3r = a[starta + 2] - a[starta + 6];
		x3i = a[starta + 3] - a[starta + 7];
		a[starta] = x0r + x2r;
		a[starta + 1] = x0i + x2i;
		a[starta + 2] = x1r - x3i;
		a[starta + 3] = x1i + x3r;
		a[starta + 4] = x0r - x2r;
		a[starta + 5] = x0i - x2i;
		a[starta + 6] = x1r + x3i;
		a[starta + 7] = x1i - x3r;
	}

	private void cftb040(float[] a, int starta) {
		float x0r, x0i, x1r, x1i, x2r, x2i, x3r, x3i;

		x0r = a[starta] + a[starta + 4];
		x0i = a[starta + 1] + a[starta + 5];
		x1r = a[starta] - a[starta + 4];
		x1i = a[starta + 1] - a[starta + 5];
		x2r = a[starta + 2] + a[starta + 6];
		x2i = a[starta + 3] + a[starta + 7];
		x3r = a[starta + 2] - a[starta + 6];
		x3i = a[starta + 3] - a[starta + 7];
		a[starta] = x0r + x2r;
		a[starta + 1] = x0i + x2i;
		a[starta + 2] = x1r + x3i;
		a[starta + 3] = x1i - x3r;
		a[starta + 4] = x0r - x2r;
		a[starta + 5] = x0i - x2i;
		a[starta + 6] = x1r - x3i;
		a[starta + 7] = x1i + x3r;
	}

	private void cftx020(float[] a, int starta) {
		float x0r, x0i;

		x0r = a[starta] - a[starta + 2];
		x0i = a[starta + 1] - a[starta + 3];
		a[starta] += a[starta + 2];
		a[starta + 1] += a[starta + 3];
		a[starta + 2] = x0r;
		a[starta + 3] = x0i;
	}

	private void rftfsub(int n, float[] a, int starta, int nc, float[] c, int startc) {
		int j, k, kk, ks, m;
		float wkr, wki, xr, xi, yr, yi;
		int idx1, idx2;
		m = n >> 1;
		ks = 2 * nc / m;
		kk = 0;
		for (j = 2; j < m; j += 2) {
			k = n - j;
			kk += ks;
			wkr = 0.5f - c[startc + nc - kk];
			wki = c[startc + kk];
			idx1 = starta + j;
			idx2 = starta + k;
			xr = a[idx1] - a[idx2];
			xi = a[idx1 + 1] + a[idx2 + 1];
			yr = wkr * xr - wki * xi;
			yi = wkr * xi + wki * xr;
			a[idx1] -= yr;
			a[idx1 + 1] -= yi;
			a[idx2] += yr;
			a[idx2 + 1] -= yi;
		}
	}

	private void rftbsub(int n, float[] a, int starta, int nc, float[] c, int startc) {
		int j, k, kk, ks, m;
		float wkr, wki, xr, xi, yr, yi;
		int idx1, idx2;
		m = n >> 1;
		ks = 2 * nc / m;
		kk = 0;
		for (j = 2; j < m; j += 2) {
			k = n - j;
			kk += ks;
			wkr = 0.5f - c[startc + nc - kk];
			wki = c[startc + kk];
			idx1 = starta + j;
			idx2 = starta + k;
			xr = a[idx1] - a[idx2];
			xi = a[idx1 + 1] + a[idx2 + 1];
			yr = wkr * xr + wki * xi;
			yi = wkr * xi - wki * xr;
			a[idx1] -= yr;
			a[idx1 + 1] -= yi;
			a[idx2] += yr;
			a[idx2 + 1] -= yi;
		}
	}

	private void dctsub(int n, float[] a, int starta, int nc, float[] c, int startc) {
		int j, k, kk, ks, m;
		float wkr, wki, xr;
		int idx0, idx1, idx2;

		m = n >> 1;
		ks = nc / n;
		kk = 0;
		for (j = 1; j < m; j++) {
			k = n - j;
			kk += ks;
			idx0 = startc + kk;
			idx1 = starta + j;
			idx2 = starta + k;
			wkr = c[idx0] - c[startc + nc - kk];
			wki = c[idx0] + c[startc + nc - kk];
			xr = wki * a[idx1] - wkr * a[idx2];
			a[idx1] = wkr * a[idx1] + wki * a[idx2];
			a[idx2] = xr;
		}
		a[starta + m] *= c[startc];
	}

	private void scale(final float[] a, int starta) {
		int np = Utils.getNP();
		if ((np > 1) && (n > DCT1D_2THREADS_BEGIN_N)) {
			final int k = n / np;
			Future[] futures = new Future[np];
			for (int i = 0; i < np; i++) {
				final int loc_starta = starta + i * k;
				futures[i] = Utils.threadPool.submit(new Runnable() {

					public void run() {
						for (int i = loc_starta; i < loc_starta + k; i++) {
							a[i] = a[i] * (float) Math.sqrt(2.0 / n);
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
			int stopa = starta + n;
			for (int i = starta; i < stopa; i++) {
				a[i] = a[i] * (float) Math.sqrt(2.0 / n);
			}
		}
	}
}
