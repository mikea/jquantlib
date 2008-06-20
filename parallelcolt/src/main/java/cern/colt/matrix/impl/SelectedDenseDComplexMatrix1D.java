/*
Copyright © 1999 CERN - European Organization for Nuclear Research.
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
is hereby granted without fee, provided that the above copyright notice appear in all copies and 
that both that copyright notice and this permission notice appear in supporting documentation. 
CERN makes no representations about the suitability of this software for any purpose. 
It is provided "as is" without expressed or implied warranty.
 */
package cern.colt.matrix.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import cern.colt.Utils;
import cern.colt.matrix.DComplexMatrix1D;
import cern.colt.matrix.DComplexMatrix2D;
import cern.colt.matrix.DComplexMatrix3D;
import cern.colt.matrix.DoubleMatrix1D;

/**
 * Selection view on dense 1-d matrices holding <tt>complex</tt> elements.
 * <b>Implementation:</b>
 * <p>
 * Objects of this class are typically constructed via <tt>viewIndexes</tt>
 * methods on some source matrix. The interface introduced in abstract super
 * classes defines everything a user can do. From a user point of view there is
 * nothing special about this class; it presents the same functionality with the
 * same signatures and semantics as its abstract superclass(es) while
 * introducing no additional functionality. Thus, this class need not be visible
 * to users.
 * <p>
 * This class uses no delegation. Its instances point directly to the data. Cell
 * addressing overhead is 1 additional array index access per get/set.
 * <p>
 * Note that this implementation is not synchronized.
 * <p>
 * <b>Memory requirements:</b>
 * <p>
 * <tt>memory [bytes] = 4*indexes.length</tt>. Thus, an index view with 1000
 * indexes additionally uses 4 KB.
 * <p>
 * <b>Time complexity:</b>
 * <p>
 * Depends on the parent view holding cells.
 * <p>
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * @version 1.0, 11/25/2007
 */
class SelectedDenseDComplexMatrix1D extends DComplexMatrix1D {

	private static final long serialVersionUID = 1358904244890406059L;

	/**
	 * The elements of this matrix.
	 */
	protected double[] elements;

	/**
	 * The offsets of visible indexes of this matrix.
	 */
	protected int[] offsets;

	/**
	 * The offset.
	 */
	protected int offset;

	/**
	 * Constructs a matrix view with the given parameters.
	 * 
	 * @param elements
	 *            the cells.
	 * @param indexes
	 *            The indexes of the cells that shall be visible.
	 */
	protected SelectedDenseDComplexMatrix1D(double[] elements, int[] offsets) {
		this(offsets.length, elements, 0, 1, offsets, 0);
	}

	/**
	 * Constructs a matrix view with the given parameters.
	 * 
	 * @param size
	 *            the number of cells the matrix shall have.
	 * @param elements
	 *            the cells.
	 * @param zero
	 *            the index of the first element.
	 * @param stride
	 *            the number of indexes between any two elements, i.e.
	 *            <tt>index(i+1)-index(i)</tt>.
	 * @param offsets
	 *            the offsets of the cells that shall be visible.
	 * @param offset
	 */
	protected SelectedDenseDComplexMatrix1D(int size, double[] elements, int zero, int stride, int[] offsets, int offset) {
		setUp(size, zero, stride);

		this.elements = elements;
		this.offsets = offsets;
		this.offset = offset;
		this.isNoView = false;
	}

	/**
	 * Returns the position of the given absolute rank within the (virtual or
	 * non-virtual) internal 1-dimensional array. Default implementation.
	 * Override, if necessary.
	 * 
	 * @param rank
	 *            the absolute rank of the element.
	 * @return the position.
	 */
	protected int _offset(int absRank) {
		return offsets[absRank];
	}

	public void fft() {
		throw new IllegalArgumentException("fft() is not supported yet");
	}

	public void ifft(boolean scale) {
		throw new IllegalArgumentException("ifft() is not supported yet");
	}

	/**
	 * Returns the matrix cell value at coordinate <tt>index</tt>.
	 * 
	 * <p>
	 * Provided with invalid parameters this method may return invalid objects
	 * without throwing any exception. <b>You should only use this method when
	 * you are absolutely sure that the coordinate is within bounds.</b>
	 * Precondition (unchecked): <tt>index&lt;0 || index&gt;=size()</tt>.
	 * 
	 * @param index
	 *            the index of the cell.
	 * @return the value of the specified cell.
	 */
	public double[] getQuick(int index) {
		int idx = zero + index * stride;
		return new double[] { elements[offset + offsets[idx]], elements[offset + offsets[idx] + 1] };
	}

	/**
	 * Returns the real part of this matrix
	 * 
	 * @return the real part
	 */
	public DoubleMatrix1D getRealPart() {
		final DenseDoubleMatrix1D R = new DenseDoubleMatrix1D(size);
		int np = Utils.getNP();
		if ((np > 1) && (size >= Utils.getThreadsBeginN_1D())) {
			Future[] futures = new Future[np];
			int k = size / np;
			for (int j = 0; j < np; j++) {
				final int startidx = j * k;
				final int stopidx;
				if (j == np - 1) {
					stopidx = size;
				} else {
					stopidx = startidx + k;
				}
				futures[j] = Utils.threadPool.submit(new Runnable() {
					double[] tmp;

					public void run() {
						for (int k = startidx; k < stopidx; k++) {
							tmp = getQuick(k);
							R.setQuick(k, tmp[0]);
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
			double[] tmp;
			for (int i = 0; i < size; i++) {
				tmp = getQuick(i);
				R.setQuick(i, tmp[0]);
			}
		}
		return R;
	}

	/**
	 * Returns the imaginary part of this matrix
	 * 
	 * @return the imaginary part
	 */
	public DoubleMatrix1D getImaginaryPart() {
		final DenseDoubleMatrix1D Im = new DenseDoubleMatrix1D(size);
		int np = Utils.getNP();
		if ((np > 1) && (size >= Utils.getThreadsBeginN_1D())) {
			Future[] futures = new Future[np];
			int k = size / np;
			for (int j = 0; j < np; j++) {
				final int startidx = j * k;
				final int stopidx;
				if (j == np - 1) {
					stopidx = size;
				} else {
					stopidx = startidx + k;
				}
				futures[j] = Utils.threadPool.submit(new Runnable() {
					double[] tmp;

					public void run() {
						for (int k = startidx; k < stopidx; k++) {
							tmp = getQuick(k);
							Im.setQuick(k, tmp[1]);
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
			double[] tmp;
			for (int i = 0; i < size; i++) {
				tmp = getQuick(i);
				Im.setQuick(i, tmp[1]);
			}
		}

		return Im;
	}

	/**
	 * This method is not supported for SelectedDenseComplexMatrix1D.
	 * 
	 * @return
	 * @throws IllegalAccessException
	 *             always.
	 */
	public double[] getElements() {
		throw new IllegalAccessError("getElements() is not supported for SelectedDenseComplexMatrix1D.");
	}

	/**
	 * Returns <tt>true</tt> if both matrices share at least one identical
	 * cell.
	 * 
	 * @param other
	 *            matrix
	 * @return <tt>true</tt> if both matrices share at least one identical
	 *         cell.
	 */
	protected boolean haveSharedCellsRaw(DComplexMatrix1D other) {
		if (other instanceof SelectedDenseDComplexMatrix1D) {
			SelectedDenseDComplexMatrix1D otherMatrix = (SelectedDenseDComplexMatrix1D) other;
			return this.elements == otherMatrix.elements;
		} else if (other instanceof DenseDComplexMatrix1D) {
			DenseDComplexMatrix1D otherMatrix = (DenseDComplexMatrix1D) other;
			return this.elements == otherMatrix.elements;
		}
		return false;
	}

	/**
	 * Returns the position of the element with the given relative rank within
	 * the (virtual or non-virtual) internal 1-dimensional array. You may want
	 * to override this method for performance.
	 * 
	 * @param rank
	 *            the rank of the element.
	 */
	protected int index(int rank) {
		return offset + offsets[zero + rank * stride];
	}

	/**
	 * Construct and returns a new empty matrix <i>of the same dynamic type</i>
	 * as the receiver, having the specified size. For example, if the receiver
	 * is an instance of type <tt>DenseComplexMatrix1D</tt> the new matrix
	 * must also be of type <tt>DenseComplexMatrix1D</tt>. In general, the
	 * new matrix should have internal parametrization as similar as possible.
	 * 
	 * @param size
	 *            the number of cell the matrix shall have.
	 * @return a new empty matrix of the same dynamic type.
	 */
	public DComplexMatrix1D like(int size) {
		return new DenseDComplexMatrix1D(size);
	}

	/**
	 * Construct and returns a new 2-d matrix <i>of the corresponding dynamic
	 * type</i>, entirelly independent of the receiver. For example, if the
	 * receiver is an instance of type <tt>DenseComplexMatrix1D</tt> the new
	 * matrix must be of type <tt>DenseComplexMatrix2D</tt>.
	 * 
	 * @param rows
	 *            the number of rows the matrix shall have.
	 * @param columns
	 *            the number of columns the matrix shall have.
	 * @return a new matrix of the corresponding dynamic type.
	 */
	public DComplexMatrix2D like2D(int rows, int columns) {
		return new DenseDComplexMatrix2D(rows, columns);
	}

	public DComplexMatrix2D reshape(int rows, int cols) {
		throw new IllegalAccessError("reshape is not supported for SelectedDenseDoubleMatrix1D.");
	}

	public DComplexMatrix3D reshape(int slices, int rows, int cols) {
		throw new IllegalAccessError("reshape is not supported for SelectedDenseDoubleMatrix1D.");
	}

	/**
	 * Sets the matrix cell at coordinate <tt>index</tt> to the specified
	 * value.
	 * 
	 * <p>
	 * Provided with invalid parameters this method may access illegal indexes
	 * without throwing any exception. <b>You should only use this method when
	 * you are absolutely sure that the coordinate is within bounds.</b>
	 * Precondition (unchecked): <tt>index&lt;0 || index&gt;=size()</tt>.
	 * 
	 * @param index
	 *            the index of the cell.
	 * @param value
	 *            the value to be filled into the specified cell.
	 */
	public void setQuick(int index, double[] value) {
		int idx = zero + index * stride;
		elements[offset + offsets[idx]] = value[0];
		elements[offset + offsets[idx] + 1] = value[1];
	}

	/**
	 * Sets the matrix cell at coordinate <tt>index</tt> to the specified
	 * value.
	 * 
	 * <p>
	 * Provided with invalid parameters this method may access illegal indexes
	 * without throwing any exception. <b>You should only use this method when
	 * you are absolutely sure that the coordinate is within bounds.</b>
	 * Precondition (unchecked): <tt>index&lt;0 || index&gt;=size()</tt>.
	 * 
	 * @param index
	 *            the index of the cell.
	 * @param re
	 *            the real part of the value to be filled into the specified
	 *            cell.
	 * @param im
	 *            the imaginary part of the value to be filled into the
	 *            specified cell.
	 * 
	 */
	public void setQuick(int index, double re, double im) {
		int idx = zero + index * stride;
		elements[offset + offsets[idx]] = re;
		elements[offset + offsets[idx] + 1] = im;
	}

	/**
	 * Sets up a matrix with a given number of cells.
	 * 
	 * @param size
	 *            the number of cells the matrix shall have.
	 */
	protected void setUp(int size) {
		super.setUp(size, 0, 1);
		this.offset = 0;
	}

	/**
	 * Construct and returns a new selection view.
	 * 
	 * @param offsets
	 *            the offsets of the visible elements.
	 * @return a new view.
	 */
	protected DComplexMatrix1D viewSelectionLike(int[] offsets) {
		return new SelectedDenseDComplexMatrix1D(this.elements, offsets);
	}

}
