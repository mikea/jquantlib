package cern.colt.matrix.impl.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cern.colt.Utils;
import cern.colt.function.DoubleProcedure;
import cern.colt.function.IntIntDoubleFunction;
import cern.colt.list.DoubleArrayList;
import cern.colt.list.IntArrayList;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix1DProcedure;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.DoubleFunctions;

public class Test_DenseDoubleMatrix2D {
	private static final int rows = 113;

	private static final int cols = 117;

	private static final double tol = 1e-10;

	private static final int nThreads = 3;

	private static final int nThreadsBegin = 1;

	private double[][] a_2d, b_2d;

	private double[] a_1d, b_1d;

	private Random rand;

	@Before
	public void setUp() throws Exception {
		rand = new Random(0);

		a_1d = new double[rows * cols];
		a_2d = new double[rows][cols];
		int idx = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				a_2d[r][c] = rand.nextDouble();
				a_1d[idx++] = a_2d[r][c];
			}
		}

		b_1d = new double[rows * cols];
		b_2d = new double[rows][cols];
		idx = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				b_2d[r][c] = rand.nextDouble();
				b_1d[idx++] = b_2d[r][c];
			}
		}
	}

	@After
	public void tearDown() throws Exception {
		a_1d = null;
		a_2d = null;
		b_1d = null;
		b_2d = null;
		System.gc();
	}

	@Test
	public void testAggregateDoubleDoubleFunctionDoubleFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		double aSum = A.aggregate(DoubleFunctions.plus, DoubleFunctions.square);
		double tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSum += a_2d[r][c] * a_2d[r][c];
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		aSum = A.aggregate(DoubleFunctions.plus, DoubleFunctions.square);
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSum += a_2d[r][c] * a_2d[r][c];
			}
		}
		assertEquals(tmpSum, aSum, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		aSum = Av.aggregate(DoubleFunctions.plus, DoubleFunctions.square);
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSum += a_2d[r][c] * a_2d[r][c];
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		aSum = Av.aggregate(DoubleFunctions.plus, DoubleFunctions.square);
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSum += a_2d[r][c] * a_2d[r][c];
			}
		}
		assertEquals(tmpSum, aSum, tol);
	}

	@Test
	public void testAggregateDoubleDoubleFunctionDoubleFunctionDoubleProcedure() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		double aSum = A.aggregate(DoubleFunctions.plus, DoubleFunctions.square, new DoubleProcedure() {

			public boolean apply(double element) {
				if (Math.abs(element) > 0.2) {
					return true;
				} else {
					return false;
				}
			}

		});
		double tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(a_2d[r][c]) > 0.2) {
					tmpSum += a_2d[r][c] * a_2d[r][c];
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		aSum = A.aggregate(DoubleFunctions.plus, DoubleFunctions.square, new DoubleProcedure() {

			public boolean apply(double element) {
				if (Math.abs(element) > 0.2) {
					return true;
				} else {
					return false;
				}
			}

		});
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(a_2d[r][c]) > 0.2) {
					tmpSum += a_2d[r][c] * a_2d[r][c];
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		aSum = Av.aggregate(DoubleFunctions.plus, DoubleFunctions.square, new DoubleProcedure() {

			public boolean apply(double element) {
				if (Math.abs(element) > 0.2) {
					return true;
				} else {
					return false;
				}
			}

		});
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(a_2d[r][c]) > 0.2) {
					tmpSum += a_2d[r][c] * a_2d[r][c];
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		aSum = Av.aggregate(DoubleFunctions.plus, DoubleFunctions.square, new DoubleProcedure() {

			public boolean apply(double element) {
				if (Math.abs(element) > 0.2) {
					return true;
				} else {
					return false;
				}
			}

		});
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(a_2d[r][c]) > 0.2) {
					tmpSum += a_2d[r][c] * a_2d[r][c];
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
	}

	@Test
	public void testAggregateDoubleDoubleFunctionDoubleFunctionSet() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		ConcurrentHashMap<int[], Double> positives = A.getPositiveValues();
		double aSum = A.aggregate(DoubleFunctions.plus, DoubleFunctions.square, positives.keySet());
		double tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (a_2d[r][c] > 0) {
					tmpSum += a_2d[r][c] * a_2d[r][c];
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		positives = A.getPositiveValues();
		aSum = A.aggregate(DoubleFunctions.plus, DoubleFunctions.square, positives.keySet());
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (a_2d[r][c] > 0) {
					tmpSum += a_2d[r][c] * a_2d[r][c];
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		positives = Av.getPositiveValues();
		aSum = Av.aggregate(DoubleFunctions.plus, DoubleFunctions.square, positives.keySet());
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (a_2d[r][c] > 0) {
					tmpSum += a_2d[r][c] * a_2d[r][c];
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		positives = Av.getPositiveValues();
		aSum = Av.aggregate(DoubleFunctions.plus, DoubleFunctions.square, positives.keySet());
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (a_2d[r][c] > 0) {
					tmpSum += a_2d[r][c] * a_2d[r][c];
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
	}

	@Test
	public void testAggregateDoubleMatrix2DDoubleDoubleFunctionDoubleDoubleFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D B = new DenseDoubleMatrix2D(b_2d);
		double sumMult = A.aggregate(B, DoubleFunctions.plus, DoubleFunctions.mult);
		double tmpSumMult = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSumMult += a_2d[r][c] * b_2d[r][c];
			}
		}
		assertEquals(tmpSumMult, sumMult, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = new DenseDoubleMatrix2D(b_2d);
		sumMult = A.aggregate(B, DoubleFunctions.plus, DoubleFunctions.mult);
		tmpSumMult = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSumMult += a_2d[r][c] * b_2d[r][c];
			}
		}
		assertEquals(tmpSumMult, sumMult, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		B = new DenseDoubleMatrix2D(b_2d);
		DoubleMatrix2D Bv = B.viewDice();
		sumMult = Av.aggregate(Bv, DoubleFunctions.plus, DoubleFunctions.mult);
		tmpSumMult = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSumMult += a_2d[r][c] * b_2d[r][c];
			}
		}
		assertEquals(tmpSumMult, sumMult, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		B = new DenseDoubleMatrix2D(b_2d);
		Bv = B.viewDice();
		sumMult = Av.aggregate(Bv, DoubleFunctions.plus, DoubleFunctions.mult);
		tmpSumMult = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSumMult += a_2d[r][c] * b_2d[r][c];
			}
		}
		assertEquals(tmpSumMult, sumMult, tol);

	}

	@Test
	public void testAssignDouble() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		double value = Math.random();
		A.assign(value);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(value, A.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		A.assign(value);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(value, A.getQuick(r, c), tol);
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(rows, cols);
		DoubleMatrix2D Av = A.viewDice();
		value = Math.random();
		Av.assign(value);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(value, Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		Av = A.viewDice();
		value = Math.random();
		Av.assign(value);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(value, Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignDoubleProcedureDoubleFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D B = A.copy();
		A.assign(new DoubleProcedure() {
			public boolean apply(double element) {
				if (Math.abs(element) > 0.1) {
					return true;
				} else {
					return false;
				}
			}
		}, DoubleFunctions.tan);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(B.getQuick(r, c)) > 0.1) {
					B.setQuick(r, c, Math.tan(B.getQuick(r, c)));
				}
			}
		}
		Utils.assertArrayEquals((double[]) B.getElements(), (double[]) A.getElements(), tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = A.copy();
		A.assign(new DoubleProcedure() {
			public boolean apply(double element) {
				if (Math.abs(element) > 0.1) {
					return true;
				} else {
					return false;
				}
			}
		}, DoubleFunctions.tan);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(B.getQuick(r, c)) > 0.1) {
					B.setQuick(r, c, Math.tan(B.getQuick(r, c)));
				}
			}
		}
		Utils.assertArrayEquals((double[]) B.getElements(), (double[]) A.getElements(), tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		B = A.copy();
		DoubleMatrix2D Bv = B.viewDice();
		Av.assign(new DoubleProcedure() {
			public boolean apply(double element) {
				if (Math.abs(element) > 0.1) {
					return true;
				} else {
					return false;
				}
			}
		}, DoubleFunctions.tan);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				if (Math.abs(Bv.getQuick(r, c)) > 0.1) {
					Bv.setQuick(r, c, Math.tan(Bv.getQuick(r, c)));
				}
			}
		}
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(Bv.getQuick(r, c), Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		B = A.copy();
		Bv = B.viewDice();
		Av.assign(new DoubleProcedure() {
			public boolean apply(double element) {
				if (Math.abs(element) > 0.1) {
					return true;
				} else {
					return false;
				}
			}
		}, DoubleFunctions.tan);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				if (Math.abs(Bv.getQuick(r, c)) > 0.1) {
					Bv.setQuick(r, c, Math.tan(Bv.getQuick(r, c)));
				}
			}
		}
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(Bv.getQuick(r, c), Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignDoubleProcedureDouble() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D B = A.copy();
		A.assign(new DoubleProcedure() {
			public boolean apply(double element) {
				if (Math.abs(element) > 0.1) {
					return true;
				} else {
					return false;
				}
			}
		}, -1);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(B.getQuick(r, c)) > 0.1) {
					B.setQuick(r, c, -1);
				}
			}
		}
		Utils.assertArrayEquals((double[]) B.getElements(), (double[]) A.getElements(), tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = A.copy();
		A.assign(new DoubleProcedure() {
			public boolean apply(double element) {
				if (Math.abs(element) > 0.1) {
					return true;
				} else {
					return false;
				}
			}
		}, -1);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(B.getQuick(r, c)) > 0.1) {
					B.setQuick(r, c, -1);
				}
			}
		}
		Utils.assertArrayEquals((double[]) B.getElements(), (double[]) A.getElements(), tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		B = A.copy();
		DoubleMatrix2D Bv = B.viewDice();
		Av.assign(new DoubleProcedure() {
			public boolean apply(double element) {
				if (Math.abs(element) > 0.1) {
					return true;
				} else {
					return false;
				}
			}
		}, -1);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				if (Math.abs(Bv.getQuick(r, c)) > 0.1) {
					Bv.setQuick(r, c, -1);
				}
			}
		}
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(Bv.getQuick(r, c), Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		B = A.copy();
		Bv = B.viewDice();
		Av.assign(new DoubleProcedure() {
			public boolean apply(double element) {
				if (Math.abs(element) > 0.1) {
					return true;
				} else {
					return false;
				}
			}
		}, -1);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				if (Math.abs(Bv.getQuick(r, c)) > 0.1) {
					Bv.setQuick(r, c, -1);
				}
			}
		}
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(Bv.getQuick(r, c), Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignDoubleArray() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		A.assign(a_1d);
		double[] aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		A.assign(a_1d);
		aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(rows, cols);
		DoubleMatrix2D Av = A.viewDice();
		Av.assign(a_1d);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_1d[r * rows + c], Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		Av = A.viewDice();
		Av.assign(a_1d);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_1d[r * rows + c], Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignDoubleArrayArray() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		A.assign(a_2d);
		double[] aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		A.assign(a_2d);
		aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(cols, rows);
		DoubleMatrix2D Av = A.viewDice();
		Av.assign(a_2d);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][c], Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(cols, rows);
		Av = A.viewDice();
		Av.assign(a_2d);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][c], Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignDoubleFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		A.assign(DoubleFunctions.acos);
		double tmp;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmp = a_2d[r][c];
				tmp = Math.acos(tmp);
				assertEquals(tmp, A.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		A.assign(DoubleFunctions.acos);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmp = a_2d[r][c];
				tmp = Math.acos(tmp);
				assertEquals(tmp, A.getQuick(r, c), tol);
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		Av.assign(DoubleFunctions.acos);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				tmp = a_2d[c][r];
				tmp = Math.acos(tmp);
				assertEquals(tmp, Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		Av.assign(DoubleFunctions.acos);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				tmp = a_2d[c][r];
				tmp = Math.acos(tmp);
				assertEquals(tmp, Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignDoubleMatrix2D() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		DoubleMatrix2D B = new DenseDoubleMatrix2D(a_2d);
		A.assign(B);
		double[] aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		B = new DenseDoubleMatrix2D(a_2d);
		A.assign(B);
		aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(rows, cols);
		DoubleMatrix2D Av = A.viewDice();
		B = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Bv = B.viewDice();
		Av.assign(Bv);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_2d[c][r], Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		Av = A.viewDice();
		B = new DenseDoubleMatrix2D(a_2d);
		Bv = B.viewDice();
		Av.assign(Bv);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_2d[c][r], Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignDoubleMatrix2DDoubleDoubleFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D B = new DenseDoubleMatrix2D(b_2d);
		A.assign(B, DoubleFunctions.div);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][c] / b_2d[r][c], A.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = new DenseDoubleMatrix2D(b_2d);
		A.assign(B, DoubleFunctions.div);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][c] / b_2d[r][c], A.getQuick(r, c), tol);
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		B = new DenseDoubleMatrix2D(b_2d);
		DoubleMatrix2D Bv = B.viewDice();
		Av.assign(Bv, DoubleFunctions.div);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_2d[c][r] / b_2d[c][r], Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		B = new DenseDoubleMatrix2D(b_2d);
		Bv = B.viewDice();
		Av.assign(Bv, DoubleFunctions.div);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_2d[c][r] / b_2d[c][r], Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignFloatArray() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		float[] a_1d_float = new float[rows * cols];
		double[] a_1d_double = new double[rows * cols];
		for (int i = 0; i < rows * cols; i++) {
			a_1d_float[i] = rand.nextFloat();
			a_1d_double[i] = a_1d_float[i];
		}
		A.assign(a_1d_float);
		double[] aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d_double, aElts, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		a_1d_float = new float[rows * cols];
		a_1d_double = new double[rows * cols];
		for (int i = 0; i < rows * cols; i++) {
			a_1d_float[i] = rand.nextFloat();
			a_1d_double[i] = a_1d_float[i];
		}
		A.assign(a_1d_float);
		aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d_double, aElts, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(rows, cols);
		DoubleMatrix2D Av = A.viewDice();
		a_1d_float = new float[rows * cols];
		for (int i = 0; i < rows * cols; i++) {
			a_1d_float[i] = rand.nextFloat();
		}
		Av.assign(a_1d_float);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_1d_float[r * rows + c], Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		Av = A.viewDice();
		a_1d_float = new float[rows * cols];
		for (int i = 0; i < rows * cols; i++) {
			a_1d_float[i] = rand.nextFloat();
		}
		Av.assign(a_1d_float);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_1d_float[r * rows + c], Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testCardinality() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		int card = A.cardinality();
		assertEquals(rows * cols, card);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		card = A.cardinality();
		assertEquals(rows * cols, card);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		card = Av.cardinality();
		assertEquals(rows * cols, card);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		card = Av.cardinality();
		assertEquals(rows * cols, card);
	}

	@Test
	public void testEqualsDouble() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		double value = 1;
		A.assign(value);
		boolean eq = A.equals(value);
		assertEquals(true, eq);
		eq = A.equals(2);
		assertEquals(false, eq);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		A.assign(value);
		eq = A.equals(value);
		assertEquals(true, eq);
		eq = A.equals(2);
		assertEquals(false, eq);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(rows, cols);
		DoubleMatrix2D Av = A.viewDice();
		A.assign(value);
		eq = Av.equals(value);
		assertEquals(true, eq);
		eq = Av.equals(2);
		assertEquals(false, eq);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		Av = A.viewDice();
		A.assign(value);
		eq = Av.equals(value);
		assertEquals(true, eq);
		eq = Av.equals(2);
		assertEquals(false, eq);
	}

	@Test
	public void testEqualsObject() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D B = new DenseDoubleMatrix2D(b_2d);
		boolean eq = A.equals(A);
		assertEquals(true, eq);
		eq = A.equals(B);
		assertEquals(false, eq);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = new DenseDoubleMatrix2D(b_2d);
		eq = A.equals(A);
		assertEquals(true, eq);
		eq = A.equals(B);
		assertEquals(false, eq);
		/* No view */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		B = new DenseDoubleMatrix2D(b_2d);
		DoubleMatrix2D Bv = B.viewDice();
		eq = Av.equals(Av);
		assertEquals(true, eq);
		eq = Av.equals(Bv);
		assertEquals(false, eq);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		B = new DenseDoubleMatrix2D(b_2d);
		Bv = B.viewDice();
		eq = Av.equals(Av);
		assertEquals(true, eq);
		eq = Av.equals(Bv);
		assertEquals(false, eq);
	}

	@Test
	public void testForEachNonZero() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		double value = 1.5;
		A.setQuick(0, 0, value);
		value = -3.3;
		A.setQuick(3, 5, value);
		value = 222.3;
		A.setQuick(11, 22, value);
		value = 0.1123;
		A.setQuick(89, 67, value);
		double[] aElts = new double[rows * cols];
		System.arraycopy((double[]) A.getElements(), 0, aElts, 0, rows * cols);
		A.forEachNonZero(new IntIntDoubleFunction() {
			public double apply(int first, int second, double third) {
				return Math.sqrt(third);
			}
		});
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(Math.sqrt(aElts[r * cols + c]), A.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		value = 1.5;
		A.setQuick(0, 0, value);
		value = -3.3;
		A.setQuick(3, 5, value);
		value = 222.3;
		A.setQuick(11, 22, value);
		value = 0.1123;
		A.setQuick(89, 67, value);
		aElts = new double[rows * cols];
		System.arraycopy((double[]) A.getElements(), 0, aElts, 0, rows * cols);
		A.forEachNonZero(new IntIntDoubleFunction() {
			public double apply(int first, int second, double third) {
				return Math.sqrt(third);
			}
		});
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(Math.sqrt(aElts[r * cols + c]), A.getQuick(r, c), tol);
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(rows, cols);
		DoubleMatrix2D Av = A.viewDice();
		value = 1.5;
		Av.setQuick(0, 0, value);
		value = -3.3;
		Av.setQuick(3, 5, value);
		value = 222.3;
		Av.setQuick(11, 22, value);
		value = 0.1123;
		Av.setQuick(89, 67, value);
		aElts = new double[rows * cols];
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				aElts[r * rows + c] = Av.getQuick(r, c);
			}
		}
		Av.forEachNonZero(new IntIntDoubleFunction() {
			public double apply(int first, int second, double third) {
				return Math.sqrt(third);
			}
		});
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(Math.sqrt(aElts[r * rows + c]), Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		Av = A.viewDice();
		value = 1.5;
		Av.setQuick(0, 0, value);
		value = -3.3;
		Av.setQuick(3, 5, value);
		value = 222.3;
		Av.setQuick(11, 22, value);
		value = 0.1123;
		Av.setQuick(89, 67, value);
		aElts = new double[rows * cols];
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				aElts[r * rows + c] = Av.getQuick(r, c);
			}
		}
		Av.forEachNonZero(new IntIntDoubleFunction() {
			public double apply(int first, int second, double third) {
				return Math.sqrt(third);
			}
		});
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(Math.sqrt(aElts[r * rows + c]), Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testGet() {
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][c], A.get(r, c), tol);
			}
		}
	}

	@Test
	public void testGetNonZeros() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		A.setQuick(rows / 4, cols / 4, 1);
		A.setQuick(rows / 2, cols / 2, 2);
		A.setQuick(rows - 1, cols - 1, 3);
		IntArrayList rowList = new IntArrayList();
		IntArrayList colList = new IntArrayList();
		DoubleArrayList valueList = new DoubleArrayList();
		A.getNonZeros(rowList, colList, valueList);
		assertEquals(3, rowList.size());
		assertEquals(3, colList.size());
		assertEquals(3, valueList.size());
		assertEquals(rows / 4, rowList.get(0));
		assertEquals(cols / 4, colList.get(0));
		assertEquals(rows / 2, rowList.get(1));
		assertEquals(cols / 2, colList.get(1));
		assertEquals(rows - 1, rowList.get(2));
		assertEquals(cols - 1, colList.get(2));
		assertEquals(1, valueList.get(0), tol);
		assertEquals(2, valueList.get(1), tol);
		assertEquals(3, valueList.get(2), tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		A.setQuick(rows / 4, cols / 4, 1);
		A.setQuick(rows / 2, cols / 2, 2);
		A.setQuick(rows - 1, cols - 1, 3);
		rowList = new IntArrayList();
		colList = new IntArrayList();
		valueList = new DoubleArrayList();
		A.getNonZeros(rowList, colList, valueList);
		assertEquals(3, rowList.size());
		assertEquals(3, colList.size());
		assertEquals(3, valueList.size());
		assertEquals(rows / 4, rowList.get(0));
		assertEquals(cols / 4, colList.get(0));
		assertEquals(rows / 2, rowList.get(1));
		assertEquals(cols / 2, colList.get(1));
		assertEquals(rows - 1, rowList.get(2));
		assertEquals(cols - 1, colList.get(2));
		assertEquals(1, valueList.get(0), tol);
		assertEquals(2, valueList.get(1), tol);
		assertEquals(3, valueList.get(2), tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(cols, rows);
		DoubleMatrix2D Av = A.viewDice();
		Av.setQuick(rows / 4, cols / 4, 1);
		Av.setQuick(rows / 2, cols / 2, 2);
		Av.setQuick(rows - 1, cols - 1, 3);
		rowList = new IntArrayList();
		colList = new IntArrayList();
		valueList = new DoubleArrayList();
		Av.getNonZeros(rowList, colList, valueList);
		assertEquals(3, rowList.size());
		assertEquals(3, colList.size());
		assertEquals(3, valueList.size());
		assertEquals(rows / 4, rowList.get(0));
		assertEquals(cols / 4, colList.get(0));
		assertEquals(rows / 2, rowList.get(1));
		assertEquals(cols / 2, colList.get(1));
		assertEquals(rows - 1, rowList.get(2));
		assertEquals(cols - 1, colList.get(2));
		assertEquals(1, valueList.get(0), tol);
		assertEquals(2, valueList.get(1), tol);
		assertEquals(3, valueList.get(2), tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(cols, rows);
		Av = A.viewDice();
		Av.setQuick(rows / 4, cols / 4, 1);
		Av.setQuick(rows / 2, cols / 2, 2);
		Av.setQuick(rows - 1, cols - 1, 3);
		rowList = new IntArrayList();
		colList = new IntArrayList();
		valueList = new DoubleArrayList();
		Av.getNonZeros(rowList, colList, valueList);
		assertEquals(3, rowList.size());
		assertEquals(3, colList.size());
		assertEquals(3, valueList.size());
		assertEquals(rows / 4, rowList.get(0));
		assertEquals(cols / 4, colList.get(0));
		assertEquals(rows / 2, rowList.get(1));
		assertEquals(cols / 2, colList.get(1));
		assertEquals(rows - 1, rowList.get(2));
		assertEquals(cols - 1, colList.get(2));
		assertEquals(1, valueList.get(0), tol);
		assertEquals(2, valueList.get(1), tol);
		assertEquals(3, valueList.get(2), tol);
	}

	@Test
	public void testGetQuick() {
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][c], A.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testGetMaxLocation() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, 0.7);
		A.setQuick(rows / 2, cols / 2, 0.7);
		double[] maxAndLoc = A.getMaxLocation();
		assertEquals(0.7, maxAndLoc[0], tol);
		assertEquals(rows / 3, (int) maxAndLoc[1]);
		assertEquals(cols / 3, (int) maxAndLoc[2]);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, 0.7);
		A.setQuick(rows / 2, cols / 2, 0.7);
		maxAndLoc = A.getMaxLocation();
		assertEquals(0.7, maxAndLoc[0], tol);
		assertEquals(rows / 3, (int) maxAndLoc[1]);
		assertEquals(cols / 3, (int) maxAndLoc[2]);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(cols, rows);
		DoubleMatrix2D Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, 0.7);
		Av.setQuick(rows / 2, cols / 2, 0.7);
		maxAndLoc = Av.getMaxLocation();
		assertEquals(0.7, maxAndLoc[0], tol);
		assertEquals(rows / 3, (int) maxAndLoc[1]);
		assertEquals(cols / 3, (int) maxAndLoc[2]);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(cols, rows);
		Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, 0.7);
		Av.setQuick(rows / 2, cols / 2, 0.7);
		maxAndLoc = Av.getMaxLocation();
		assertEquals(0.7, maxAndLoc[0], tol);
		assertEquals(rows / 3, (int) maxAndLoc[1]);
		assertEquals(cols / 3, (int) maxAndLoc[2]);
	}

	@Test
	public void testGetMinLocation() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, -0.7);
		A.setQuick(rows / 2, cols / 2, -0.7);
		double[] minAndLoc = A.getMinLocation();
		assertEquals(-0.7, minAndLoc[0], tol);
		assertEquals(rows / 3, (int) minAndLoc[1]);
		assertEquals(cols / 3, (int) minAndLoc[2]);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, -0.7);
		A.setQuick(rows / 2, cols / 2, -0.7);
		minAndLoc = A.getMinLocation();
		assertEquals(-0.7, minAndLoc[0], tol);
		assertEquals(rows / 3, (int) minAndLoc[1]);
		assertEquals(cols / 3, (int) minAndLoc[2]);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(cols, rows);
		DoubleMatrix2D Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, -0.7);
		Av.setQuick(rows / 2, cols / 2, -0.7);
		minAndLoc = Av.getMinLocation();
		assertEquals(-0.7, minAndLoc[0], tol);
		assertEquals(rows / 3, (int) minAndLoc[1]);
		assertEquals(cols / 3, (int) minAndLoc[2]);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(cols, rows);
		Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, -0.7);
		Av.setQuick(rows / 2, cols / 2, -0.7);
		minAndLoc = Av.getMinLocation();
		assertEquals(-0.7, minAndLoc[0], tol);
		assertEquals(rows / 3, (int) minAndLoc[1]);
		assertEquals(cols / 3, (int) minAndLoc[2]);
	}

	@Test
	public void testGetNegativeValues() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, -0.7);
		A.setQuick(rows / 2, cols / 2, -0.1);
		ConcurrentHashMap<int[], Double> negatives = A.getNegativeValues();
		assertEquals(2, negatives.size());
		assertTrue(negatives.containsValue(-0.7));
		assertTrue(negatives.containsValue(-0.1));
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, -0.7);
		A.setQuick(rows / 2, cols / 2, -0.1);
		negatives = A.getNegativeValues();
		assertEquals(2, negatives.size());
		assertTrue(negatives.containsValue(-0.7));
		assertTrue(negatives.containsValue(-0.1));
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(cols, rows);
		DoubleMatrix2D Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, -0.7);
		Av.setQuick(rows / 2, cols / 2, -0.1);
		negatives = Av.getNegativeValues();
		assertEquals(2, negatives.size());
		assertTrue(negatives.containsValue(-0.7));
		assertTrue(negatives.containsValue(-0.1));
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(cols, rows);
		Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, -0.7);
		Av.setQuick(rows / 2, cols / 2, -0.1);
		negatives = Av.getNegativeValues();
		assertEquals(2, negatives.size());
		assertTrue(negatives.containsValue(-0.7));
		assertTrue(negatives.containsValue(-0.1));
	}

	@Test
	public void testGetPositiveValues() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, 0.7);
		A.setQuick(rows / 2, cols / 2, 0.1);
		ConcurrentHashMap<int[], Double> positives = A.getPositiveValues();
		assertEquals(2, positives.size());
		assertTrue(positives.containsValue(0.7));
		assertTrue(positives.containsValue(0.1));
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, 0.7);
		A.setQuick(rows / 2, cols / 2, 0.1);
		positives = A.getPositiveValues();
		assertEquals(2, positives.size());
		assertTrue(positives.containsValue(0.7));
		assertTrue(positives.containsValue(0.1));
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(cols, rows);
		DoubleMatrix2D Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, 0.7);
		Av.setQuick(rows / 2, cols / 2, 0.1);
		positives = Av.getPositiveValues();
		assertEquals(2, positives.size());
		assertTrue(positives.containsValue(0.7));
		assertTrue(positives.containsValue(0.1));
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(cols, rows);
		Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, 0.7);
		Av.setQuick(rows / 2, cols / 2, 0.1);
		positives = Av.getPositiveValues();
		assertEquals(2, positives.size());
		assertTrue(positives.containsValue(0.7));
		assertTrue(positives.containsValue(0.1));
	}

	@Test
	public void testGetValuesSuchThat() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		ConcurrentHashMap<int[], Double> values = A.getValuesSuchThat(new DoubleProcedure() {
			public boolean apply(double element) {
				if (Math.abs(element) > 0.5) {
					return true;
				} else {
					return false;
				}
			}
		});
		Set<int[]> set = values.keySet();
		double[] result1 = new double[set.size()];
		int i = 0;
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			int[] is = (int[]) iterator.next();
			result1[i] = values.get(is);
			i++;
		}
		double[] result2 = new double[set.size()];
		i = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(a_2d[r][c]) > 0.5) {
					result2[i] = a_2d[r][c];
					i++;
				}
			}
		}
		Arrays.sort(result1);
		Arrays.sort(result2);
		Utils.assertArrayEquals(result1, result2, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		values = A.getValuesSuchThat(new DoubleProcedure() {
			public boolean apply(double element) {
				if (Math.abs(element) > 0.5) {
					return true;
				} else {
					return false;
				}
			}
		});
		set = values.keySet();
		result1 = new double[set.size()];
		i = 0;
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			int[] is = (int[]) iterator.next();
			result1[i] = values.get(is);
			i++;
		}
		result2 = new double[set.size()];
		i = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(a_2d[r][c]) > 0.5) {
					result2[i] = a_2d[r][c];
					i++;
				}
			}
		}
		Arrays.sort(result1);
		Arrays.sort(result2);
		Utils.assertArrayEquals(result1, result2, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		values = Av.getValuesSuchThat(new DoubleProcedure() {
			public boolean apply(double element) {
				if (Math.abs(element) > 0.5) {
					return true;
				} else {
					return false;
				}
			}
		});
		set = values.keySet();
		result1 = new double[set.size()];
		i = 0;
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			int[] is = (int[]) iterator.next();
			result1[i] = values.get(is);
			i++;
		}
		result2 = new double[set.size()];
		i = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(a_2d[r][c]) > 0.5) {
					result2[i] = a_2d[r][c];
					i++;
				}
			}
		}
		Arrays.sort(result1);
		Arrays.sort(result2);
		Utils.assertArrayEquals(result1, result2, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		values = Av.getValuesSuchThat(new DoubleProcedure() {
			public boolean apply(double element) {
				if (Math.abs(element) > 0.5) {
					return true;
				} else {
					return false;
				}
			}
		});
		set = values.keySet();
		result1 = new double[set.size()];
		i = 0;
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			int[] is = (int[]) iterator.next();
			result1[i] = values.get(is);
			i++;
		}
		result2 = new double[set.size()];
		i = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(a_2d[r][c]) > 0.5) {
					result2[i] = a_2d[r][c];
					i++;
				}
			}
		}
		Arrays.sort(result1);
		Arrays.sort(result2);
		Utils.assertArrayEquals(result1, result2, tol);
	}

	@Test
	public void testSet() {
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		double elem = Math.random();
		A.set(rows / 2, cols / 2, elem);
		assertEquals(elem, A.getQuick(rows / 2, cols / 2), tol);
	}

	@Test
	public void testSetQuick() {
		DoubleMatrix2D A = new DenseDoubleMatrix2D(rows, cols);
		double elem = Math.random();
		A.setQuick(rows / 2, cols / 2, elem);
		assertEquals(elem, A.getQuick(rows / 2, cols / 2), tol);
	}

	@Test
	public void testToArray() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		double[][] array = A.toArray();
		Utils.assertArrayEquals(a_2d, array, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		array = A.toArray();
		Utils.assertArrayEquals(a_2d, array, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		array = Av.toArray();
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_2d[c][r], array[r][c], tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		array = Av.toArray();
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_2d[c][r], array[r][c], tol);
			}
		}

	}

	@Test
	public void testVectorize() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix1D B = A.vectorize();
		int idx = 0;
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r < rows; r++) {
				assertEquals(A.getQuick(r, c), B.getQuick(idx++), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = A.vectorize();
		idx = 0;
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r < rows; r++) {
				assertEquals(A.getQuick(r, c), B.getQuick(idx++), tol);
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		B = Av.vectorize();
		idx = 0;
		for (int c = 0; c < rows; c++) {
			for (int r = 0; r < cols; r++) {
				assertEquals(Av.getQuick(r, c), B.getQuick(idx++), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		B = Av.vectorize();
		idx = 0;
		for (int c = 0; c < rows; c++) {
			for (int r = 0; r < cols; r++) {
				assertEquals(Av.getQuick(r, c), B.getQuick(idx++), tol);
			}
		}
	}

	@Test
	public void testViewColumn() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix1D B = A.viewColumn(cols / 2);
		assertEquals(rows, B.size());
		for (int i = 0; i < rows; i++) {
			assertEquals(a_2d[i][cols / 2], B.getQuick(i), tol);
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = A.viewColumn(cols / 2);
		assertEquals(rows, B.size());
		for (int i = 0; i < rows; i++) {
			assertEquals(a_2d[i][cols / 2], B.getQuick(i), tol);
		}
	}

	@Test
	public void testViewColumnFlip() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D B = A.viewColumnFlip();
		assertEquals(A.size(), B.size());
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][cols - 1 - c], B.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = A.viewColumnFlip();
		assertEquals(A.size(), B.size());
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][cols - 1 - c], B.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testViewDice() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D B = A.viewDice();
		assertEquals(A.rows(), B.columns());
		assertEquals(A.columns(), B.rows());
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(A.getQuick(r, c), B.getQuick(c, r), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = A.viewDice();
		assertEquals(A.rows(), B.columns());
		assertEquals(A.columns(), B.rows());
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(A.getQuick(r, c), B.getQuick(c, r), tol);
			}
		}
	}

	@Test
	public void testViewPart() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D B = A.viewPart(15, 11, 21, 27);
		for (int r = 0; r < 21; r++) {
			for (int c = 0; c < 27; c++) {
				assertEquals(A.getQuick(15 + r, 11 + c), B.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = A.viewPart(15, 11, 21, 27);
		for (int r = 0; r < 21; r++) {
			for (int c = 0; c < 27; c++) {
				assertEquals(A.getQuick(15 + r, 11 + c), B.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testViewRow() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix1D B = A.viewRow(rows / 2);
		assertEquals(cols, B.size());
		for (int i = 0; i < cols; i++) {
			assertEquals(a_2d[rows / 2][i], B.getQuick(i), tol);
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = A.viewRow(rows / 2);
		assertEquals(cols, B.size());
		for (int i = 0; i < cols; i++) {
			assertEquals(a_2d[rows / 2][i], B.getQuick(i), tol);
		}

	}

	@Test
	public void testViewRowFlip() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D B = A.viewRowFlip();
		assertEquals(A.size(), B.size());
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[rows - 1 - r][c], B.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = A.viewRowFlip();
		assertEquals(A.size(), B.size());
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[rows - 1 - r][c], B.getQuick(r, c), tol);
			}
		}

	}

	@Test
	public void testViewSelectionDoubleMatrix1DProcedure() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		final double value = 2;
		A.setQuick(rows / 4, 0, value);
		A.setQuick(rows / 2, 0, value);
		DoubleMatrix2D B = A.viewSelection(new DoubleMatrix1DProcedure() {
			public boolean apply(DoubleMatrix1D element) {
				if (Math.abs(element.getQuick(0) - value) < tol) {
					return true;
				} else {
					return false;
				}
			}
		});
		assertEquals(2, B.rows());
		assertEquals(A.columns(), B.columns());
		assertEquals(A.getQuick(rows / 4, 0), B.getQuick(0, 0), tol);
		assertEquals(A.getQuick(rows / 2, 0), B.getQuick(1, 0), tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		A.setQuick(rows / 4, 0, value);
		A.setQuick(rows / 2, 0, value);
		B = A.viewSelection(new DoubleMatrix1DProcedure() {
			public boolean apply(DoubleMatrix1D element) {
				if (Math.abs(element.getQuick(0) - value) < tol) {
					return true;
				} else {
					return false;
				}
			}
		});
		assertEquals(2, B.rows());
		assertEquals(A.columns(), B.columns());
		assertEquals(A.getQuick(rows / 4, 0), B.getQuick(0, 0), tol);
		assertEquals(A.getQuick(rows / 2, 0), B.getQuick(1, 0), tol);
	}

	@Test
	public void testViewSelectionIntArrayIntArray() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		int[] rowIndexes = new int[] { 5, 11, 22, 37, 101 };
		int[] colIndexes = new int[] { 2, 17, 32, 47, 99, 111 };
		DoubleMatrix2D B = A.viewSelection(rowIndexes, colIndexes);
		assertEquals(rowIndexes.length, B.rows());
		assertEquals(colIndexes.length, B.columns());
		for (int r = 0; r < rowIndexes.length; r++) {
			for (int c = 0; c < colIndexes.length; c++) {
				assertEquals(A.getQuick(rowIndexes[r], colIndexes[c]), B.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		rowIndexes = new int[] { 5, 11, 22, 37, 101 };
		colIndexes = new int[] { 2, 17, 32, 47, 99, 111 };
		B = A.viewSelection(rowIndexes, colIndexes);
		assertEquals(rowIndexes.length, B.rows());
		assertEquals(colIndexes.length, B.columns());
		for (int r = 0; r < rowIndexes.length; r++) {
			for (int c = 0; c < colIndexes.length; c++) {
				assertEquals(A.getQuick(rowIndexes[r], colIndexes[c]), B.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testViewSorted() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix1D Col1 = A.viewColumn(1).copy();
		double[] col1 = (double[]) ((DenseDoubleMatrix1D) Col1).getElements();
		Arrays.sort(col1);
		DoubleMatrix2D B = A.viewSorted(1);
		for (int r = 0; r < rows; r++) {
			assertEquals(col1[r], B.getQuick(r, 1), tol);
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Col1 = A.viewColumn(1).copy();
		col1 = (double[]) ((DenseDoubleMatrix1D) Col1).getElements();
		Arrays.sort(col1);
		B = A.viewSorted(1);
		for (int r = 0; r < rows; r++) {
			assertEquals(col1[r], B.getQuick(r, 1), tol);
		}
	}

	@Test
	public void testViewStrides() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		int rowStride = 3;
		int colStride = 5;
		DoubleMatrix2D B = A.viewStrides(rowStride, colStride);
		for (int r = 0; r < B.rows(); r++) {
			for (int c = 0; c < B.columns(); c++) {
				assertEquals(A.getQuick(r * rowStride, c * colStride), B.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = A.viewStrides(rowStride, colStride);
		for (int r = 0; r < B.rows(); r++) {
			for (int c = 0; c < B.columns(); c++) {
				assertEquals(A.getQuick(r * rowStride, c * colStride), B.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testZMultDoubleMatrix1DDoubleMatrix1D() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix1D y = new DenseDoubleMatrix1D(A.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextDouble());
		}
		DoubleMatrix1D z = new DenseDoubleMatrix1D(A.rows());
		A.zMult(y, z);
		double[] tmpMatVec = new double[A.rows()];
		for (int r = 0; r < A.rows(); r++) {
			for (int c = 0; c < A.columns(); c++) {
				tmpMatVec[r] += A.getQuick(r, c) * y.getQuick(c);
			}
		}
		for (int r = 0; r < A.rows(); r++) {
			assertEquals(tmpMatVec[r], z.getQuick(r), tol);
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		y = new DenseDoubleMatrix1D(A.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextDouble());
		}
		z = new DenseDoubleMatrix1D(A.rows());
		A.zMult(y, z);
		tmpMatVec = new double[A.rows()];
		for (int r = 0; r < A.rows(); r++) {
			for (int c = 0; c < A.columns(); c++) {
				tmpMatVec[r] += A.getQuick(r, c) * y.getQuick(c);
			}
		}
		for (int r = 0; r < A.rows(); r++) {
			assertEquals(tmpMatVec[r], z.getQuick(r), tol);
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		y = new DenseDoubleMatrix1D(Av.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextDouble());
		}
		z = new DenseDoubleMatrix1D(Av.rows());
		Av.zMult(y, z);
		tmpMatVec = new double[Av.rows()];
		for (int r = 0; r < Av.rows(); r++) {
			for (int c = 0; c < Av.columns(); c++) {
				tmpMatVec[r] += Av.getQuick(r, c) * y.getQuick(c);
			}
		}
		for (int r = 0; r < A.rows(); r++) {
			assertEquals(tmpMatVec[r], z.getQuick(r), tol);
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		y = new DenseDoubleMatrix1D(Av.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextDouble());
		}
		z = new DenseDoubleMatrix1D(Av.rows());
		Av.zMult(y, z);
		tmpMatVec = new double[Av.rows()];
		for (int r = 0; r < Av.rows(); r++) {
			for (int c = 0; c < Av.columns(); c++) {
				tmpMatVec[r] += Av.getQuick(r, c) * y.getQuick(c);
			}
		}
		for (int r = 0; r < A.rows(); r++) {
			assertEquals(tmpMatVec[r], z.getQuick(r), tol);
		}
	}

	@Test
	public void testZMultDoubleMatrix1DDoubleMatrix1DDoubleDoubleBoolean() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix1D y = new DenseDoubleMatrix1D(A.columns());
		double alpha = 3;
		double beta = 5;
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextDouble());
		}
		DoubleMatrix1D z = new DenseDoubleMatrix1D(A.rows());
		A.zMult(y, z, alpha, beta, false);
		double[] tmpMatVec = new double[A.rows()];
		double s;
		for (int r = 0; r < rows; r++) {
			s = 0;
			for (int c = 0; c < cols; c++) {
				s += A.getQuick(r, c) * y.getQuick(c);
			}
			tmpMatVec[r] = s * alpha + tmpMatVec[r] * beta;
		}
		for (int r = 0; r < A.rows(); r++) {
			assertEquals(tmpMatVec[r], z.getQuick(r), tol);
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		y = new DenseDoubleMatrix1D(A.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextDouble());
		}
		z = new DenseDoubleMatrix1D(A.rows());
		A.zMult(y, z, alpha, beta, false);
		tmpMatVec = new double[A.rows()];
		for (int r = 0; r < rows; r++) {
			s = 0;
			for (int c = 0; c < cols; c++) {
				s += A.getQuick(r, c) * y.getQuick(c);
			}
			tmpMatVec[r] = s * alpha + tmpMatVec[r] * beta;
		}
		for (int r = 0; r < A.rows(); r++) {
			assertEquals(tmpMatVec[r], z.getQuick(r), tol);
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		y = new DenseDoubleMatrix1D(Av.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextDouble());
		}
		z = new DenseDoubleMatrix1D(Av.rows());
		Av.zMult(y, z, alpha, beta, false);
		tmpMatVec = new double[Av.rows()];
		for (int r = 0; r < cols; r++) {
			s = 0;
			for (int c = 0; c < rows; c++) {
				s += Av.getQuick(r, c) * y.getQuick(c);
			}
			tmpMatVec[r] = s * alpha + tmpMatVec[r] * beta;
		}
		for (int r = 0; r < Av.rows(); r++) {
			assertEquals(tmpMatVec[r], z.getQuick(r), tol);
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		y = new DenseDoubleMatrix1D(Av.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextDouble());
		}
		z = new DenseDoubleMatrix1D(Av.rows());
		Av.zMult(y, z, alpha, beta, false);
		tmpMatVec = new double[Av.rows()];
		for (int r = 0; r < cols; r++) {
			s = 0;
			for (int c = 0; c < rows; c++) {
				s += Av.getQuick(r, c) * y.getQuick(c);
			}
			tmpMatVec[r] = s * alpha + tmpMatVec[r] * beta;
		}
		for (int r = 0; r < Av.rows(); r++) {
			assertEquals(tmpMatVec[r], z.getQuick(r), tol);
		}
	}

	@Test
	public void testZMultDoubleMatrix2DDoubleMatrix2D() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D B = new DenseDoubleMatrix2D(b_2d);
		B = B.viewDice().copy();
		DoubleMatrix2D C = new DenseDoubleMatrix2D(rows, rows);
		A.zMult(B, C);
		double[][] tmpMatMat = new double[rows][rows];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < rows; c++) {
				for (int k = 0; k < cols; k++) {
					tmpMatMat[c][r] += A.getQuick(c, k) * B.getQuick(k, r);
				}
			}
		}
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(tmpMatMat[r][c], C.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = new DenseDoubleMatrix2D(b_2d);
		B = B.viewDice().copy();
		C = new DenseDoubleMatrix2D(rows, rows);
		A.zMult(B, C);
		tmpMatMat = new double[rows][rows];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < rows; c++) {
				for (int k = 0; k < cols; k++) {
					tmpMatMat[c][r] += A.getQuick(c, k) * B.getQuick(k, r);
				}
			}
		}
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(tmpMatMat[r][c], C.getQuick(r, c), tol);
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		B = new DenseDoubleMatrix2D(b_2d);
		B = B.viewDice().copy();
		DoubleMatrix2D Bv = B.viewDice();
		C = new DenseDoubleMatrix2D(cols, cols);
		DoubleMatrix2D Cv = C.viewDice();
		Av.zMult(Bv, Cv);
		tmpMatMat = new double[cols][cols];
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < cols; c++) {
				for (int k = 0; k < rows; k++) {
					tmpMatMat[c][r] += Av.getQuick(c, k) * Bv.getQuick(k, r);
				}
			}
		}
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(tmpMatMat[r][c], Cv.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		B = new DenseDoubleMatrix2D(b_2d);
		B = B.viewDice().copy();
		Bv = B.viewDice();
		C = new DenseDoubleMatrix2D(cols, cols);
		Cv = C.viewDice();
		Av.zMult(Bv, Cv);
		tmpMatMat = new double[cols][cols];
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < cols; c++) {
				for (int k = 0; k < rows; k++) {
					tmpMatMat[c][r] += Av.getQuick(c, k) * Bv.getQuick(k, r);
				}
			}
		}
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(tmpMatMat[r][c], Cv.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testZMultDoubleMatrix2DDoubleMatrix2DDoubleDoubleBooleanBoolean() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D B = new DenseDoubleMatrix2D(b_2d);
		B = B.viewDice().copy();
		DoubleMatrix2D C = new DenseDoubleMatrix2D(rows, rows);
		double alpha = 3;
		double beta = 5;
		A.zMult(B, C, alpha, beta, false, false);
		double[][] tmpMatMat = new double[rows][rows];
		double s;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < rows; c++) {
				s = 0;
				for (int k = 0; k < cols; k++) {
					s += A.getQuick(c, k) * B.getQuick(k, r);
				}
				tmpMatMat[c][r] = s * alpha + tmpMatMat[c][r] * beta;
			}
		}
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(tmpMatMat[r][c], C.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		B = new DenseDoubleMatrix2D(b_2d);
		B = B.viewDice().copy();
		C = new DenseDoubleMatrix2D(rows, rows);
		A.zMult(B, C, alpha, beta, false, false);
		tmpMatMat = new double[rows][rows];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < rows; c++) {
				s = 0;
				for (int k = 0; k < cols; k++) {
					s += A.getQuick(c, k) * B.getQuick(k, r);
				}
				tmpMatMat[c][r] = s * alpha + tmpMatMat[c][r] * beta;
			}
		}
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(tmpMatMat[r][c], C.getQuick(r, c), tol);
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		B = new DenseDoubleMatrix2D(b_2d);
		B = B.viewDice().copy();
		DoubleMatrix2D Bv = B.viewDice();
		C = new DenseDoubleMatrix2D(cols, cols);
		DoubleMatrix2D Cv = C.viewDice();
		Av.zMult(Bv, Cv, alpha, beta, false, false);
		tmpMatMat = new double[cols][cols];
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < cols; c++) {
				s = 0;
				for (int k = 0; k < rows; k++) {
					s += Av.getQuick(c, k) * Bv.getQuick(k, r);
				}
				tmpMatMat[c][r] = s * alpha + tmpMatMat[c][r] * beta;
			}
		}
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(tmpMatMat[r][c], Cv.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		B = new DenseDoubleMatrix2D(b_2d);
		B = B.viewDice().copy();
		Bv = B.viewDice();
		C = new DenseDoubleMatrix2D(cols, cols);
		Cv = C.viewDice();
		Av.zMult(Bv, Cv, alpha, beta, false, false);
		tmpMatMat = new double[cols][cols];
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < cols; c++) {
				s = 0;
				for (int k = 0; k < rows; k++) {
					s += Av.getQuick(c, k) * Bv.getQuick(k, r);
				}
				tmpMatMat[c][r] = s * alpha + tmpMatMat[c][r] * beta;
			}
		}
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(tmpMatMat[r][c], Cv.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testZSum() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix2D A = new DenseDoubleMatrix2D(a_2d);
		double aSum = A.zSum();
		double tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSum += a_2d[r][c];
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		aSum = A.zSum();
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSum += a_2d[r][c];
			}
		}
		assertEquals(tmpSum, aSum, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix2D(a_2d);
		DoubleMatrix2D Av = A.viewDice();
		aSum = Av.zSum();
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSum += a_2d[r][c];
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix2D(a_2d);
		Av = A.viewDice();
		aSum = Av.zSum();
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSum += a_2d[r][c];
			}
		}
		assertEquals(tmpSum, aSum, tol);
	}

}
