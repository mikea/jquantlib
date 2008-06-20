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
import cern.colt.function.FloatProcedure;
import cern.colt.function.IntIntFloatFunction;
import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;
import cern.colt.matrix.FloatMatrix1D;
import cern.colt.matrix.FloatMatrix1DProcedure;
import cern.colt.matrix.FloatMatrix2D;
import cern.colt.matrix.impl.DenseFloatMatrix1D;
import cern.colt.matrix.impl.DenseFloatMatrix2D;
import cern.jet.math.FloatFunctions;

public class Test_DenseFloatMatrix2D {
	private static final int rows = 113;

	private static final int cols = 117;

	private static final float tol = 1e-1f;

	private static final int nThreads = 3;

	private static final int nThreadsBegin = 1;

	private float[][] a_2d, b_2d;

	private float[] a_1d, b_1d;

	private Random rand;

	@Before
	public void setUp() throws Exception {
		rand = new Random(0);

		a_1d = new float[rows * cols];
		a_2d = new float[rows][cols];
		int idx = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				a_2d[r][c] = rand.nextFloat();
				a_1d[idx++] = a_2d[r][c];
			}
		}

		b_1d = new float[rows * cols];
		b_2d = new float[rows][cols];
		idx = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				b_2d[r][c] = rand.nextFloat();
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
	public void testAggregateFloatFloatFunctionFloatFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		float aSum = A.aggregate(FloatFunctions.plus, FloatFunctions.square);
		float tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSum += a_2d[r][c] * a_2d[r][c];
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		aSum = A.aggregate(FloatFunctions.plus, FloatFunctions.square);
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
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		aSum = Av.aggregate(FloatFunctions.plus, FloatFunctions.square);
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
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		aSum = Av.aggregate(FloatFunctions.plus, FloatFunctions.square);
		tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSum += a_2d[r][c] * a_2d[r][c];
			}
		}
		assertEquals(tmpSum, aSum, tol);
	}

	@Test
	public void testAggregateFloatFloatFunctionFloatFunctionFloatProcedure() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		float aSum = A.aggregate(FloatFunctions.plus, FloatFunctions.square, new FloatProcedure() {

			public boolean apply(float element) {
				if (Math.abs(element) > 0.2) {
					return true;
				} else {
					return false;
				}
			}

		});
		float tmpSum = 0;
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
		A = new DenseFloatMatrix2D(a_2d);
		aSum = A.aggregate(FloatFunctions.plus, FloatFunctions.square, new FloatProcedure() {

			public boolean apply(float element) {
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
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		aSum = Av.aggregate(FloatFunctions.plus, FloatFunctions.square, new FloatProcedure() {

			public boolean apply(float element) {
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
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		aSum = Av.aggregate(FloatFunctions.plus, FloatFunctions.square, new FloatProcedure() {

			public boolean apply(float element) {
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
	public void testAggregateFloatFloatFunctionFloatFunctionSet() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		ConcurrentHashMap<int[], Float> positives = A.getPositiveValues();
		float aSum = A.aggregate(FloatFunctions.plus, FloatFunctions.square, positives.keySet());
		float tmpSum = 0;
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
		A = new DenseFloatMatrix2D(a_2d);
		positives = A.getPositiveValues();
		aSum = A.aggregate(FloatFunctions.plus, FloatFunctions.square, positives.keySet());
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
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		positives = Av.getPositiveValues();
		aSum = Av.aggregate(FloatFunctions.plus, FloatFunctions.square, positives.keySet());
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
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		positives = Av.getPositiveValues();
		aSum = Av.aggregate(FloatFunctions.plus, FloatFunctions.square, positives.keySet());
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
	public void testAggregateFloatMatrix2DFloatFloatFunctionFloatFloatFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D B = new DenseFloatMatrix2D(b_2d);
		float sumMult = A.aggregate(B, FloatFunctions.plus, FloatFunctions.mult);
		float tmpSumMult = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSumMult += a_2d[r][c] * b_2d[r][c];
			}
		}
		assertEquals(tmpSumMult, sumMult, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		B = new DenseFloatMatrix2D(b_2d);
		sumMult = A.aggregate(B, FloatFunctions.plus, FloatFunctions.mult);
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
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		B = new DenseFloatMatrix2D(b_2d);
		FloatMatrix2D Bv = B.viewDice();
		sumMult = Av.aggregate(Bv, FloatFunctions.plus, FloatFunctions.mult);
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
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		B = new DenseFloatMatrix2D(b_2d);
		Bv = B.viewDice();
		sumMult = Av.aggregate(Bv, FloatFunctions.plus, FloatFunctions.mult);
		tmpSumMult = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSumMult += a_2d[r][c] * b_2d[r][c];
			}
		}
		assertEquals(tmpSumMult, sumMult, tol);

	}

	@Test
	public void testAssignFloat() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		float value = (float) Math.random();
		A.assign(value);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(value, A.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
		A.assign(value);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(value, A.getQuick(r, c), tol);
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(rows, cols);
		FloatMatrix2D Av = A.viewDice();
		value = (float) Math.random();
		Av.assign(value);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(value, Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
		Av = A.viewDice();
		value = (float) Math.random();
		Av.assign(value);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(value, Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignFloatProcedureFloatFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D B = A.copy();
		A.assign(new FloatProcedure() {
			public boolean apply(float element) {
				if (Math.abs(element) > 0.1) {
					return true;
				} else {
					return false;
				}
			}
		}, FloatFunctions.tan);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(B.getQuick(r, c)) > 0.1) {
					B.setQuick(r, c, (float) Math.tan(B.getQuick(r, c)));
				}
			}
		}
		Utils.assertArrayEquals((float[]) B.getElements(), (float[]) A.getElements(), tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		B = A.copy();
		A.assign(new FloatProcedure() {
			public boolean apply(float element) {
				if (Math.abs(element) > 0.1) {
					return true;
				} else {
					return false;
				}
			}
		}, FloatFunctions.tan);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (Math.abs(B.getQuick(r, c)) > 0.1) {
					B.setQuick(r, c, (float) Math.tan(B.getQuick(r, c)));
				}
			}
		}
		Utils.assertArrayEquals((float[]) B.getElements(), (float[]) A.getElements(), tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		B = A.copy();
		FloatMatrix2D Bv = B.viewDice();
		Av.assign(new FloatProcedure() {
			public boolean apply(float element) {
				if (Math.abs(element) > 0.1) {
					return true;
				} else {
					return false;
				}
			}
		}, FloatFunctions.tan);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				if (Math.abs(Bv.getQuick(r, c)) > 0.1) {
					Bv.setQuick(r, c, (float) Math.tan(Bv.getQuick(r, c)));
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
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		B = A.copy();
		Bv = B.viewDice();
		Av.assign(new FloatProcedure() {
			public boolean apply(float element) {
				if (Math.abs(element) > 0.1) {
					return true;
				} else {
					return false;
				}
			}
		}, FloatFunctions.tan);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				if (Math.abs(Bv.getQuick(r, c)) > 0.1) {
					Bv.setQuick(r, c, (float) Math.tan(Bv.getQuick(r, c)));
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
	public void testAssignFloatProcedureFloat() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D B = A.copy();
		A.assign(new FloatProcedure() {
			public boolean apply(float element) {
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
		Utils.assertArrayEquals((float[]) B.getElements(), (float[]) A.getElements(), tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		B = A.copy();
		A.assign(new FloatProcedure() {
			public boolean apply(float element) {
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
		Utils.assertArrayEquals((float[]) B.getElements(), (float[]) A.getElements(), tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		B = A.copy();
		FloatMatrix2D Bv = B.viewDice();
		Av.assign(new FloatProcedure() {
			public boolean apply(float element) {
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
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		B = A.copy();
		Bv = B.viewDice();
		Av.assign(new FloatProcedure() {
			public boolean apply(float element) {
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
	public void testAssignFloatArray() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		A.assign(a_1d);
		float[] aElts = (float[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
		A.assign(a_1d);
		aElts = (float[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(rows, cols);
		FloatMatrix2D Av = A.viewDice();
		Av.assign(a_1d);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_1d[r * rows + c], Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
		Av = A.viewDice();
		Av.assign(a_1d);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_1d[r * rows + c], Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignFloatArrayArray() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		A.assign(a_2d);
		float[] aElts = (float[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
		A.assign(a_2d);
		aElts = (float[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(cols, rows);
		FloatMatrix2D Av = A.viewDice();
		Av.assign(a_2d);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][c], Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(cols, rows);
		Av = A.viewDice();
		Av.assign(a_2d);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][c], Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignFloatFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		A.assign(FloatFunctions.acos);
		float tmp;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmp = a_2d[r][c];
				tmp = (float) Math.acos(tmp);
				assertEquals(tmp, A.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		A.assign(FloatFunctions.acos);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmp = a_2d[r][c];
				tmp = (float) Math.acos(tmp);
				assertEquals(tmp, A.getQuick(r, c), tol);
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		Av.assign(FloatFunctions.acos);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				tmp = a_2d[c][r];
				tmp = (float) Math.acos(tmp);
				assertEquals(tmp, Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		Av.assign(FloatFunctions.acos);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				tmp = a_2d[c][r];
				tmp = (float) Math.acos(tmp);
				assertEquals(tmp, Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignFloatMatrix2D() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		FloatMatrix2D B = new DenseFloatMatrix2D(a_2d);
		A.assign(B);
		float[] aElts = (float[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
		B = new DenseFloatMatrix2D(a_2d);
		A.assign(B);
		aElts = (float[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(rows, cols);
		FloatMatrix2D Av = A.viewDice();
		B = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Bv = B.viewDice();
		Av.assign(Bv);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_2d[c][r], Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
		Av = A.viewDice();
		B = new DenseFloatMatrix2D(a_2d);
		Bv = B.viewDice();
		Av.assign(Bv);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_2d[c][r], Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testAssignFloatMatrix2DFloatFloatFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D B = new DenseFloatMatrix2D(b_2d);
		A.assign(B, FloatFunctions.div);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][c] / b_2d[r][c], A.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		B = new DenseFloatMatrix2D(b_2d);
		A.assign(B, FloatFunctions.div);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][c] / b_2d[r][c], A.getQuick(r, c), tol);
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		B = new DenseFloatMatrix2D(b_2d);
		FloatMatrix2D Bv = B.viewDice();
		Av.assign(Bv, FloatFunctions.div);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_2d[c][r] / b_2d[c][r], Av.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		B = new DenseFloatMatrix2D(b_2d);
		Bv = B.viewDice();
		Av.assign(Bv, FloatFunctions.div);
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_2d[c][r] / b_2d[c][r], Av.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testCardinality() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		int card = A.cardinality();
		assertEquals(rows * cols, card);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		card = A.cardinality();
		assertEquals(rows * cols, card);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		card = Av.cardinality();
		assertEquals(rows * cols, card);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		card = Av.cardinality();
		assertEquals(rows * cols, card);
	}

	@Test
	public void testEqualsFloat() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		float value = 1;
		A.assign(value);
		boolean eq = A.equals(value);
		assertEquals(true, eq);
		eq = A.equals(2);
		assertEquals(false, eq);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
		A.assign(value);
		eq = A.equals(value);
		assertEquals(true, eq);
		eq = A.equals(2);
		assertEquals(false, eq);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(rows, cols);
		FloatMatrix2D Av = A.viewDice();
		A.assign(value);
		eq = Av.equals(value);
		assertEquals(true, eq);
		eq = Av.equals(2);
		assertEquals(false, eq);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D B = new DenseFloatMatrix2D(b_2d);
		boolean eq = A.equals(A);
		assertEquals(true, eq);
		eq = A.equals(B);
		assertEquals(false, eq);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		B = new DenseFloatMatrix2D(b_2d);
		eq = A.equals(A);
		assertEquals(true, eq);
		eq = A.equals(B);
		assertEquals(false, eq);
		/* No view */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		B = new DenseFloatMatrix2D(b_2d);
		FloatMatrix2D Bv = B.viewDice();
		eq = Av.equals(Av);
		assertEquals(true, eq);
		eq = Av.equals(Bv);
		assertEquals(false, eq);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		B = new DenseFloatMatrix2D(b_2d);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		float value = 1.5f;
		A.setQuick(0, 0, value);
		value = -3.3f;
		A.setQuick(3, 5, value);
		value = 222.3f;
		A.setQuick(11, 22, value);
		value = 0.1123f;
		A.setQuick(89, 67, value);
		float[] aElts = new float[rows * cols];
		System.arraycopy((float[]) A.getElements(), 0, aElts, 0, rows * cols);
		A.forEachNonZero(new IntIntFloatFunction() {
			public float apply(int first, int second, float third) {
				return (float) Math.sqrt(third);
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
		A = new DenseFloatMatrix2D(rows, cols);
		value = 1.5f;
		A.setQuick(0, 0, value);
		value = -3.3f;
		A.setQuick(3, 5, value);
		value = 222.3f;
		A.setQuick(11, 22, value);
		value = 0.1123f;
		A.setQuick(89, 67, value);
		aElts = new float[rows * cols];
		System.arraycopy((float[]) A.getElements(), 0, aElts, 0, rows * cols);
		A.forEachNonZero(new IntIntFloatFunction() {
			public float apply(int first, int second, float third) {
				return (float) Math.sqrt(third);
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
		A = new DenseFloatMatrix2D(rows, cols);
		FloatMatrix2D Av = A.viewDice();
		value = 1.5f;
		Av.setQuick(0, 0, value);
		value = -3.3f;
		Av.setQuick(3, 5, value);
		value = 222.3f;
		Av.setQuick(11, 22, value);
		value = 0.1123f;
		Av.setQuick(89, 67, value);
		aElts = new float[rows * cols];
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				aElts[r * rows + c] = Av.getQuick(r, c);
			}
		}
		Av.forEachNonZero(new IntIntFloatFunction() {
			public float apply(int first, int second, float third) {
				return (float) Math.sqrt(third);
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
		A = new DenseFloatMatrix2D(rows, cols);
		Av = A.viewDice();
		value = 1.5f;
		Av.setQuick(0, 0, value);
		value = -3.3f;
		Av.setQuick(3, 5, value);
		value = 222.3f;
		Av.setQuick(11, 22, value);
		value = 0.1123f;
		Av.setQuick(89, 67, value);
		aElts = new float[rows * cols];
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				aElts[r * rows + c] = Av.getQuick(r, c);
			}
		}
		Av.forEachNonZero(new IntIntFloatFunction() {
			public float apply(int first, int second, float third) {
				return (float) Math.sqrt(third);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		A.setQuick(rows / 4, cols / 4, 1);
		A.setQuick(rows / 2, cols / 2, 2);
		A.setQuick(rows - 1, cols - 1, 3);
		IntArrayList rowList = new IntArrayList();
		IntArrayList colList = new IntArrayList();
		FloatArrayList valueList = new FloatArrayList();
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
		A = new DenseFloatMatrix2D(rows, cols);
		A.setQuick(rows / 4, cols / 4, 1);
		A.setQuick(rows / 2, cols / 2, 2);
		A.setQuick(rows - 1, cols - 1, 3);
		rowList = new IntArrayList();
		colList = new IntArrayList();
		valueList = new FloatArrayList();
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
		A = new DenseFloatMatrix2D(cols, rows);
		FloatMatrix2D Av = A.viewDice();
		Av.setQuick(rows / 4, cols / 4, 1);
		Av.setQuick(rows / 2, cols / 2, 2);
		Av.setQuick(rows - 1, cols - 1, 3);
		rowList = new IntArrayList();
		colList = new IntArrayList();
		valueList = new FloatArrayList();
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
		A = new DenseFloatMatrix2D(cols, rows);
		Av = A.viewDice();
		Av.setQuick(rows / 4, cols / 4, 1);
		Av.setQuick(rows / 2, cols / 2, 2);
		Av.setQuick(rows - 1, cols - 1, 3);
		rowList = new IntArrayList();
		colList = new IntArrayList();
		valueList = new FloatArrayList();
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, 0.7f);
		A.setQuick(rows / 2, cols / 2, 0.7f);
		float[] maxAndLoc = A.getMaxLocation();
		assertEquals(0.7, maxAndLoc[0], tol);
		assertEquals(rows / 3, (int) maxAndLoc[1]);
		assertEquals(cols / 3, (int) maxAndLoc[2]);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, 0.7f);
		A.setQuick(rows / 2, cols / 2, 0.7f);
		maxAndLoc = A.getMaxLocation();
		assertEquals(0.7, maxAndLoc[0], tol);
		assertEquals(rows / 3, (int) maxAndLoc[1]);
		assertEquals(cols / 3, (int) maxAndLoc[2]);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(cols, rows);
		FloatMatrix2D Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, 0.7f);
		Av.setQuick(rows / 2, cols / 2, 0.7f);
		maxAndLoc = Av.getMaxLocation();
		assertEquals(0.7, maxAndLoc[0], tol);
		assertEquals(rows / 3, (int) maxAndLoc[1]);
		assertEquals(cols / 3, (int) maxAndLoc[2]);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(cols, rows);
		Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, 0.7f);
		Av.setQuick(rows / 2, cols / 2, 0.7f);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, -0.7f);
		A.setQuick(rows / 2, cols / 2, -0.7f);
		float[] minAndLoc = A.getMinLocation();
		assertEquals(-0.7, minAndLoc[0], tol);
		assertEquals(rows / 3, (int) minAndLoc[1]);
		assertEquals(cols / 3, (int) minAndLoc[2]);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, -0.7f);
		A.setQuick(rows / 2, cols / 2, -0.7f);
		minAndLoc = A.getMinLocation();
		assertEquals(-0.7, minAndLoc[0], tol);
		assertEquals(rows / 3, (int) minAndLoc[1]);
		assertEquals(cols / 3, (int) minAndLoc[2]);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(cols, rows);
		FloatMatrix2D Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, -0.7f);
		Av.setQuick(rows / 2, cols / 2, -0.7f);
		minAndLoc = Av.getMinLocation();
		assertEquals(-0.7, minAndLoc[0], tol);
		assertEquals(rows / 3, (int) minAndLoc[1]);
		assertEquals(cols / 3, (int) minAndLoc[2]);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(cols, rows);
		Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, -0.7f);
		Av.setQuick(rows / 2, cols / 2, -0.7f);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, -0.7f);
		A.setQuick(rows / 2, cols / 2, -0.1f);
		ConcurrentHashMap<int[], Float> negatives = A.getNegativeValues();
		assertEquals(2, negatives.size());
		assertTrue(negatives.containsValue(-0.7f));
		assertTrue(negatives.containsValue(-0.1f));
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, -0.7f);
		A.setQuick(rows / 2, cols / 2, -0.1f);
		negatives = A.getNegativeValues();
		assertEquals(2, negatives.size());
		assertTrue(negatives.containsValue(-0.7f));
		assertTrue(negatives.containsValue(-0.1f));
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(cols, rows);
		FloatMatrix2D Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, -0.7f);
		Av.setQuick(rows / 2, cols / 2, -0.1f);
		negatives = Av.getNegativeValues();
		assertEquals(2, negatives.size());
		assertTrue(negatives.containsValue(-0.7f));
		assertTrue(negatives.containsValue(-0.1f));
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(cols, rows);
		Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, -0.7f);
		Av.setQuick(rows / 2, cols / 2, -0.1f);
		negatives = Av.getNegativeValues();
		assertEquals(2, negatives.size());
		assertTrue(negatives.containsValue(-0.7f));
		assertTrue(negatives.containsValue(-0.1f));
	}

	@Test
	public void testGetPositiveValues() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, 0.7f);
		A.setQuick(rows / 2, cols / 2, 0.1f);
		ConcurrentHashMap<int[], Float> positives = A.getPositiveValues();
		assertEquals(2, positives.size());
		assertTrue(positives.containsValue(0.7f));
		assertTrue(positives.containsValue(0.1f));
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(rows, cols);
		A.setQuick(rows / 3, cols / 3, 0.7f);
		A.setQuick(rows / 2, cols / 2, 0.1f);
		positives = A.getPositiveValues();
		assertEquals(2, positives.size());
		assertTrue(positives.containsValue(0.7f));
		assertTrue(positives.containsValue(0.1f));
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(cols, rows);
		FloatMatrix2D Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, 0.7f);
		Av.setQuick(rows / 2, cols / 2, 0.1f);
		positives = Av.getPositiveValues();
		assertEquals(2, positives.size());
		assertTrue(positives.containsValue(0.7f));
		assertTrue(positives.containsValue(0.1f));
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(cols, rows);
		Av = A.viewDice();
		Av.setQuick(rows / 3, cols / 3, 0.7f);
		Av.setQuick(rows / 2, cols / 2, 0.1f);
		positives = Av.getPositiveValues();
		assertEquals(2, positives.size());
		assertTrue(positives.containsValue(0.7f));
		assertTrue(positives.containsValue(0.1f));
	}

	@Test
	public void testGetValuesSuchThat() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		ConcurrentHashMap<int[], Float> values = A.getValuesSuchThat(new FloatProcedure() {
			public boolean apply(float element) {
				if (Math.abs(element) > 0.5) {
					return true;
				} else {
					return false;
				}
			}
		});
		Set<int[]> set = values.keySet();
		float[] result1 = new float[set.size()];
		int i = 0;
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			int[] is = (int[]) iterator.next();
			result1[i] = values.get(is);
			i++;
		}
		float[] result2 = new float[set.size()];
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
		A = new DenseFloatMatrix2D(a_2d);
		values = A.getValuesSuchThat(new FloatProcedure() {
			public boolean apply(float element) {
				if (Math.abs(element) > 0.5) {
					return true;
				} else {
					return false;
				}
			}
		});
		set = values.keySet();
		result1 = new float[set.size()];
		i = 0;
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			int[] is = (int[]) iterator.next();
			result1[i] = values.get(is);
			i++;
		}
		result2 = new float[set.size()];
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
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		values = Av.getValuesSuchThat(new FloatProcedure() {
			public boolean apply(float element) {
				if (Math.abs(element) > 0.5) {
					return true;
				} else {
					return false;
				}
			}
		});
		set = values.keySet();
		result1 = new float[set.size()];
		i = 0;
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			int[] is = (int[]) iterator.next();
			result1[i] = values.get(is);
			i++;
		}
		result2 = new float[set.size()];
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
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		values = Av.getValuesSuchThat(new FloatProcedure() {
			public boolean apply(float element) {
				if (Math.abs(element) > 0.5) {
					return true;
				} else {
					return false;
				}
			}
		});
		set = values.keySet();
		result1 = new float[set.size()];
		i = 0;
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			int[] is = (int[]) iterator.next();
			result1[i] = values.get(is);
			i++;
		}
		result2 = new float[set.size()];
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
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		float elem = (float) Math.random();
		A.set(rows / 2, cols / 2, elem);
		assertEquals(elem, A.getQuick(rows / 2, cols / 2), tol);
	}

	@Test
	public void testSetQuick() {
		FloatMatrix2D A = new DenseFloatMatrix2D(rows, cols);
		float elem = (float) Math.random();
		A.setQuick(rows / 2, cols / 2, elem);
		assertEquals(elem, A.getQuick(rows / 2, cols / 2), tol);
	}

	@Test
	public void testToArray() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		float[][] array = A.toArray();
		Utils.assertArrayEquals(a_2d, array, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		array = A.toArray();
		Utils.assertArrayEquals(a_2d, array, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		array = Av.toArray();
		for (int r = 0; r < cols; r++) {
			for (int c = 0; c < rows; c++) {
				assertEquals(a_2d[c][r], array[r][c], tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix1D B = A.vectorize();
		int idx = 0;
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r < rows; r++) {
				assertEquals(A.getQuick(r, c), B.getQuick(idx++), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
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
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
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
		A = new DenseFloatMatrix2D(a_2d);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix1D B = A.viewColumn(cols / 2);
		assertEquals(rows, B.size());
		for (int i = 0; i < rows; i++) {
			assertEquals(a_2d[i][cols / 2], B.getQuick(i), tol);
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D B = A.viewColumnFlip();
		assertEquals(A.size(), B.size());
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[r][cols - 1 - c], B.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D B = A.viewDice();
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
		A = new DenseFloatMatrix2D(a_2d);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D B = A.viewPart(15, 11, 21, 27);
		for (int r = 0; r < 21; r++) {
			for (int c = 0; c < 27; c++) {
				assertEquals(A.getQuick(15 + r, 11 + c), B.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix1D B = A.viewRow(rows / 2);
		assertEquals(cols, B.size());
		for (int i = 0; i < cols; i++) {
			assertEquals(a_2d[rows / 2][i], B.getQuick(i), tol);
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D B = A.viewRowFlip();
		assertEquals(A.size(), B.size());
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[rows - 1 - r][c], B.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		B = A.viewRowFlip();
		assertEquals(A.size(), B.size());
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_2d[rows - 1 - r][c], B.getQuick(r, c), tol);
			}
		}

	}

	@Test
	public void testViewSelectionFloatMatrix1DProcedure() {
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		final float value = 2;
		A.setQuick(rows / 4, 0, value);
		A.setQuick(rows / 2, 0, value);
		FloatMatrix2D B = A.viewSelection(new FloatMatrix1DProcedure() {
			public boolean apply(FloatMatrix1D element) {
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
		A = new DenseFloatMatrix2D(a_2d);
		A.setQuick(rows / 4, 0, value);
		A.setQuick(rows / 2, 0, value);
		B = A.viewSelection(new FloatMatrix1DProcedure() {
			public boolean apply(FloatMatrix1D element) {
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		int[] rowIndexes = new int[] { 5, 11, 22, 37, 101 };
		int[] colIndexes = new int[] { 2, 17, 32, 47, 99, 111 };
		FloatMatrix2D B = A.viewSelection(rowIndexes, colIndexes);
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
		A = new DenseFloatMatrix2D(a_2d);
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix1D Col1 = A.viewColumn(1).copy();
		float[] col1 = (float[]) ((DenseFloatMatrix1D) Col1).getElements();
		Arrays.sort(col1);
		FloatMatrix2D B = A.viewSorted(1);
		for (int r = 0; r < rows; r++) {
			assertEquals(col1[r], B.getQuick(r, 1), tol);
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		Col1 = A.viewColumn(1).copy();
		col1 = (float[]) ((DenseFloatMatrix1D) Col1).getElements();
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		int rowStride = 3;
		int colStride = 5;
		FloatMatrix2D B = A.viewStrides(rowStride, colStride);
		for (int r = 0; r < B.rows(); r++) {
			for (int c = 0; c < B.columns(); c++) {
				assertEquals(A.getQuick(r * rowStride, c * colStride), B.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
		B = A.viewStrides(rowStride, colStride);
		for (int r = 0; r < B.rows(); r++) {
			for (int c = 0; c < B.columns(); c++) {
				assertEquals(A.getQuick(r * rowStride, c * colStride), B.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testZMultFloatMatrix1DFloatMatrix1D() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix1D y = new DenseFloatMatrix1D(A.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextFloat());
		}
		FloatMatrix1D z = new DenseFloatMatrix1D(A.rows());
		A.zMult(y, z);
		float[] tmpMatVec = new float[A.rows()];
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
		A = new DenseFloatMatrix2D(a_2d);
		y = new DenseFloatMatrix1D(A.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextFloat());
		}
		z = new DenseFloatMatrix1D(A.rows());
		A.zMult(y, z);
		tmpMatVec = new float[A.rows()];
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
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		y = new DenseFloatMatrix1D(Av.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextFloat());
		}
		z = new DenseFloatMatrix1D(Av.rows());
		Av.zMult(y, z);
		tmpMatVec = new float[Av.rows()];
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
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		y = new DenseFloatMatrix1D(Av.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextFloat());
		}
		z = new DenseFloatMatrix1D(Av.rows());
		Av.zMult(y, z);
		tmpMatVec = new float[Av.rows()];
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
	public void testZMultFloatMatrix1DFloatMatrix1DFloatFloatBoolean() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix1D y = new DenseFloatMatrix1D(A.columns());
		float alpha = 3;
		float beta = 5;
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextFloat());
		}
		FloatMatrix1D z = new DenseFloatMatrix1D(A.rows());
		A.zMult(y, z, alpha, beta, false);
		float[] tmpMatVec = new float[A.rows()];
		float s;
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
		A = new DenseFloatMatrix2D(a_2d);
		y = new DenseFloatMatrix1D(A.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextFloat());
		}
		z = new DenseFloatMatrix1D(A.rows());
		A.zMult(y, z, alpha, beta, false);
		tmpMatVec = new float[A.rows()];
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
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		y = new DenseFloatMatrix1D(Av.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextFloat());
		}
		z = new DenseFloatMatrix1D(Av.rows());
		Av.zMult(y, z, alpha, beta, false);
		tmpMatVec = new float[Av.rows()];
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
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		y = new DenseFloatMatrix1D(Av.columns());
		for (int i = 0; i < y.size(); i++) {
			y.set(i, rand.nextFloat());
		}
		z = new DenseFloatMatrix1D(Av.rows());
		Av.zMult(y, z, alpha, beta, false);
		tmpMatVec = new float[Av.rows()];
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
	public void testZMultFloatMatrix2DFloatMatrix2D() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D B = new DenseFloatMatrix2D(b_2d);
		B = B.viewDice().copy();
		FloatMatrix2D C = new DenseFloatMatrix2D(rows, rows);
		A.zMult(B, C);
		float[][] tmpMatMat = new float[rows][rows];
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
		A = new DenseFloatMatrix2D(a_2d);
		B = new DenseFloatMatrix2D(b_2d);
		B = B.viewDice().copy();
		C = new DenseFloatMatrix2D(rows, rows);
		A.zMult(B, C);
		tmpMatMat = new float[rows][rows];
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
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		B = new DenseFloatMatrix2D(b_2d);
		B = B.viewDice().copy();
		FloatMatrix2D Bv = B.viewDice();
		C = new DenseFloatMatrix2D(cols, cols);
		FloatMatrix2D Cv = C.viewDice();
		Av.zMult(Bv, Cv);
		tmpMatMat = new float[cols][cols];
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
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		B = new DenseFloatMatrix2D(b_2d);
		B = B.viewDice().copy();
		Bv = B.viewDice();
		C = new DenseFloatMatrix2D(cols, cols);
		Cv = C.viewDice();
		Av.zMult(Bv, Cv);
		tmpMatMat = new float[cols][cols];
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
	public void testZMultFloatMatrix2DFloatMatrix2DFloatFloatBooleanBoolean() {
		/* No view */
		// single thread
		Utils.setNP(1);
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D B = new DenseFloatMatrix2D(b_2d);
		B = B.viewDice().copy();
		FloatMatrix2D C = new DenseFloatMatrix2D(rows, rows);
		float alpha = 3;
		float beta = 5;
		A.zMult(B, C, alpha, beta, false, false);
		float[][] tmpMatMat = new float[rows][rows];
		float s;
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
		A = new DenseFloatMatrix2D(a_2d);
		B = new DenseFloatMatrix2D(b_2d);
		B = B.viewDice().copy();
		C = new DenseFloatMatrix2D(rows, rows);
		A.zMult(B, C, alpha, beta, false, false);
		tmpMatMat = new float[rows][rows];
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
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
		B = new DenseFloatMatrix2D(b_2d);
		B = B.viewDice().copy();
		FloatMatrix2D Bv = B.viewDice();
		C = new DenseFloatMatrix2D(cols, cols);
		FloatMatrix2D Cv = C.viewDice();
		Av.zMult(Bv, Cv, alpha, beta, false, false);
		tmpMatMat = new float[cols][cols];
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
		A = new DenseFloatMatrix2D(a_2d);
		Av = A.viewDice();
		B = new DenseFloatMatrix2D(b_2d);
		B = B.viewDice().copy();
		Bv = B.viewDice();
		C = new DenseFloatMatrix2D(cols, cols);
		Cv = C.viewDice();
		Av.zMult(Bv, Cv, alpha, beta, false, false);
		tmpMatMat = new float[cols][cols];
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
		FloatMatrix2D A = new DenseFloatMatrix2D(a_2d);
		float aSum = A.zSum();
		float tmpSum = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				tmpSum += a_2d[r][c];
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseFloatMatrix2D(a_2d);
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
		A = new DenseFloatMatrix2D(a_2d);
		FloatMatrix2D Av = A.viewDice();
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
		A = new DenseFloatMatrix2D(a_2d);
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
