package cern.colt.matrix.impl.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cern.colt.Utils;
import cern.colt.list.DoubleArrayList;
import cern.colt.list.IntArrayList;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.DoubleMatrix2DProcedure;
import cern.colt.matrix.DoubleMatrix3D;
import cern.colt.matrix.impl.DenseDoubleMatrix3D;
import cern.jet.math.DoubleFunctions;

public class Test_DenseDoubleMatrix3D {
	private static final int slices = 5;

	private static final int rows = 53;

	private static final int cols = 57;

	private static final double tol = 1e-10;

	private static final int nThreads = 3;

	private static final int nThreadsBegin = 1;

	private double[][][] a_3d, b_3d;

	private double[] a_1d, b_1d;

	private Random rand;

	@Before
	public void setUp() throws Exception {
		rand = new Random(0);
		a_1d = new double[slices * rows * cols];
		a_3d = new double[slices][rows][cols];
		int idx = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					a_3d[s][r][c] = rand.nextDouble();
					a_1d[idx++] = a_3d[s][r][c];
				}
			}
		}
		b_1d = new double[slices * rows * cols];
		b_3d = new double[slices][rows][cols];
		idx = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					b_3d[s][r][c] = rand.nextDouble();
					b_1d[idx++] = b_3d[s][r][c];
				}
			}
		}
	}

	@After
	public void tearDown() throws Exception {
		a_1d = null;
		a_3d = null;
		b_1d = null;
		b_3d = null;
		System.gc();
	}

	@Test
	public void testAggregateDoubleDoubleFunctionDoubleFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		double aSum = A.aggregate(DoubleFunctions.plus, DoubleFunctions.sqrt);
		double tmpSum = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					tmpSum += Math.sqrt(a_3d[s][r][c]);
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		aSum = A.aggregate(DoubleFunctions.plus, DoubleFunctions.sqrt);
		tmpSum = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					tmpSum += Math.sqrt(a_3d[s][r][c]);
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		aSum = Av.aggregate(DoubleFunctions.plus, DoubleFunctions.sqrt);
		tmpSum = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					tmpSum += Math.sqrt(a_3d[s][r][c]);
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		Av = A.viewDice(2, 1, 0);
		aSum = Av.aggregate(DoubleFunctions.plus, DoubleFunctions.sqrt);
		tmpSum = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					tmpSum += Math.sqrt(a_3d[s][r][c]);
				}
			}
		}

	}

	@Test
	public void testAggregateDoubleMatrix3DDoubleDoubleFunctionDoubleDoubleFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D B = new DenseDoubleMatrix3D(b_3d);
		double sumMult = A.aggregate(B, DoubleFunctions.plus, DoubleFunctions.mult);
		double tmpSumMult = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					tmpSumMult += a_3d[s][r][c] * b_3d[s][r][c];
				}
			}
		}
		assertEquals(tmpSumMult, sumMult, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = new DenseDoubleMatrix3D(b_3d);
		sumMult = A.aggregate(B, DoubleFunctions.plus, DoubleFunctions.mult);
		tmpSumMult = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					tmpSumMult += a_3d[s][r][c] * b_3d[s][r][c];
				}
			}
		}
		assertEquals(tmpSumMult, sumMult, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		B = new DenseDoubleMatrix3D(b_3d);
		DoubleMatrix3D Bv = B.viewDice(2, 1, 0);
		sumMult = Av.aggregate(Bv, DoubleFunctions.plus, DoubleFunctions.mult);
		tmpSumMult = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					tmpSumMult += a_3d[s][r][c] * b_3d[s][r][c];
				}
			}
		}
		assertEquals(tmpSumMult, sumMult, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		Av = A.viewDice(2, 1, 0);
		B = new DenseDoubleMatrix3D(b_3d);
		Bv = B.viewDice(2, 1, 0);
		sumMult = Av.aggregate(Bv, DoubleFunctions.plus, DoubleFunctions.mult);
		tmpSumMult = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					tmpSumMult += a_3d[s][r][c] * b_3d[s][r][c];
				}
			}
		}
		assertEquals(tmpSumMult, sumMult, tol);
	}

	@Test
	public void testAssignDouble() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(slices, rows, cols);
		double value = Math.random();
		A.assign(value);
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(value, A.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.assign(value);
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(value, A.getQuick(s, r, c), tol);
				}
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		value = Math.random();
		Av.assign(value);
		for (int s = 0; s < cols; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < slices; c++) {
					assertEquals(value, Av.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		Av = A.viewDice(2, 1, 0);
		value = Math.random();
		Av.assign(value);
		for (int s = 0; s < cols; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < slices; c++) {
					assertEquals(value, Av.getQuick(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testAssignDoubleArray() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.assign(a_1d);
		double[] aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.assign(a_1d);
		aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		Av.assign(a_1d);
		for (int s = 0; s < cols; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < slices; c++) {
					assertEquals(a_1d[s * rows * slices + r * slices + c], Av.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		Av = A.viewDice(2, 1, 0);
		Av.assign(a_1d);
		for (int s = 0; s < cols; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < slices; c++) {
					assertEquals(a_1d[s * rows * slices + r * slices + c], Av.getQuick(s, r, c), tol);
				}
			}
		}

	}

	@Test
	public void testAssignDoubleArrayArrayArray() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.assign(a_3d);
		double[] aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.assign(a_3d);
		aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		/* No view */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(cols, rows, slices);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		Av.assign(a_3d);
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(a_3d[s][r][c], Av.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(cols, rows, slices);
		Av = A.viewDice(2, 1, 0);
		Av.assign(a_3d);
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(a_3d[s][r][c], Av.getQuick(s, r, c), tol);
				}
			}
		}

	}

	@Test
	public void testAssignDoubleFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		A.assign(DoubleFunctions.acos);
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(Math.acos(a_3d[s][r][c]), A.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		A.assign(DoubleFunctions.acos);
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(Math.acos(a_3d[s][r][c]), A.getQuick(s, r, c), tol);
				}
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		Av.assign(DoubleFunctions.acos);
		for (int s = 0; s < cols; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < slices; c++) {
					assertEquals(Math.acos(a_3d[c][r][s]), Av.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		Av = A.viewDice(2, 1, 0);
		Av.assign(DoubleFunctions.acos);
		for (int s = 0; s < cols; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < slices; c++) {
					assertEquals(Math.acos(a_3d[c][r][s]), Av.getQuick(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testAssignDoubleMatrix3D() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(slices, rows, cols);
		DoubleMatrix3D B = new DenseDoubleMatrix3D(a_3d);
		A.assign(B);
		double[] aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		B = new DenseDoubleMatrix3D(a_3d);
		A.assign(B);
		aElts = (double[]) A.getElements();
		Utils.assertArrayEquals(a_1d, aElts, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		B = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D Bv = B.viewDice(2, 1, 0);
		Av.assign(Bv);
		for (int s = 0; s < cols; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < slices; c++) {
					assertEquals(a_3d[c][r][s], Av.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		Av = A.viewDice(2, 1, 0);
		B = new DenseDoubleMatrix3D(a_3d);
		Bv = B.viewDice(2, 1, 0);
		Av.assign(Bv);
		for (int s = 0; s < cols; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < slices; c++) {
					assertEquals(a_3d[c][r][s], Av.getQuick(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testAssignDoubleMatrix3DDoubleDoubleFunction() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D B = new DenseDoubleMatrix3D(b_3d);
		A.assign(B, DoubleFunctions.div);
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(a_3d[s][r][c] / b_3d[s][r][c], A.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = new DenseDoubleMatrix3D(b_3d);
		A.assign(B, DoubleFunctions.div);
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(a_3d[s][r][c] / b_3d[s][r][c], A.getQuick(s, r, c), tol);
				}
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		B = new DenseDoubleMatrix3D(b_3d);
		DoubleMatrix3D Bv = B.viewDice(2, 1, 0);
		Av.assign(Bv, DoubleFunctions.div);
		for (int s = 0; s < cols; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < slices; c++) {
					assertEquals(a_3d[c][r][s] / b_3d[c][r][s], Av.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		Av = A.viewDice(2, 1, 0);
		B = new DenseDoubleMatrix3D(b_3d);
		Bv = B.viewDice(2, 1, 0);
		Av.assign(Bv, DoubleFunctions.div);
		for (int s = 0; s < cols; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < slices; c++) {
					assertEquals(a_3d[c][r][s] / b_3d[c][r][s], Av.getQuick(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testCardinality() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		int card = A.cardinality();
		assertEquals(slices * rows * cols, card);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		card = A.cardinality();
		assertEquals(slices * rows * cols, card);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		card = Av.cardinality();
		assertEquals(slices * rows * cols, card);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		Av = A.viewDice(2, 1, 0);
		card = Av.cardinality();
		assertEquals(slices * rows * cols, card);
	}

	@Test
	public void testEqualsDouble() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(slices, rows, cols);
		double value = 1;
		A.assign(value);
		boolean eq = A.equals(value);
		assertEquals(true, eq);
		eq = A.equals(2);
		assertEquals(false, eq);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.assign(1);
		eq = A.equals(value);
		assertEquals(true, eq);
		eq = A.equals(2);
		assertEquals(false, eq);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		Av.assign(value);
		eq = Av.equals(value);
		assertEquals(true, eq);
		eq = Av.equals(2);
		assertEquals(false, eq);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		Av = A.viewDice(2, 1, 0);
		Av.assign(value);
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
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D B = new DenseDoubleMatrix3D(b_3d);
		boolean eq = A.equals(A);
		assertEquals(true, eq);
		eq = A.equals(B);
		assertEquals(false, eq);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = new DenseDoubleMatrix3D(b_3d);
		eq = A.equals(A);
		assertEquals(true, eq);
		eq = A.equals(B);
		assertEquals(false, eq);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		B = new DenseDoubleMatrix3D(b_3d);
		DoubleMatrix3D Bv = B.viewDice(2, 1, 0);
		eq = Av.equals(Av);
		assertEquals(true, eq);
		eq = Av.equals(Bv);
		assertEquals(false, eq);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		Av = A.viewDice(2, 1, 0);
		B = new DenseDoubleMatrix3D(b_3d);
		Bv = B.viewDice(2, 1, 0);
		eq = Av.equals(Av);
		assertEquals(true, eq);
		eq = Av.equals(Bv);
		assertEquals(false, eq);

	}

	@Test
	public void testGet() {
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(a_3d[s][r][c], A.get(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testGetMaxLocation() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.setQuick(slices / 3, rows / 3, cols / 3, 0.7);
		A.setQuick(slices / 3, rows / 2, cols / 2, 0.7);
		double[] maxAndLoc = A.getMaxLocation();
		assertEquals(0.7, maxAndLoc[0], tol);
		assertEquals(slices / 3, (int) maxAndLoc[1]);
		assertEquals(rows / 3, (int) maxAndLoc[2]);
		assertEquals(cols / 3, (int) maxAndLoc[3]);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.setQuick(slices / 3, rows / 3, cols / 3, 0.7);
		A.setQuick(slices / 3, rows / 2, cols / 2, 0.7);
		maxAndLoc = A.getMaxLocation();
		assertEquals(0.7, maxAndLoc[0], tol);
		assertEquals(slices / 3, (int) maxAndLoc[1]);
		assertEquals(rows / 3, (int) maxAndLoc[2]);
		assertEquals(cols / 3, (int) maxAndLoc[3]);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(cols, rows, slices);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		Av.setQuick(slices / 3, rows / 3, cols / 3, 0.7);
		Av.setQuick(slices / 3, rows / 2, cols / 2, 0.7);
		maxAndLoc = Av.getMaxLocation();
		assertEquals(0.7, maxAndLoc[0], tol);
		assertEquals(slices / 3, (int) maxAndLoc[1]);
		assertEquals(rows / 3, (int) maxAndLoc[2]);
		assertEquals(cols / 3, (int) maxAndLoc[3]);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(cols, rows, slices);
		Av = A.viewDice(2, 1, 0);
		Av.setQuick(slices / 3, rows / 3, cols / 3, 0.7);
		Av.setQuick(slices / 3, rows / 2, cols / 2, 0.7);
		maxAndLoc = Av.getMaxLocation();
		assertEquals(0.7, maxAndLoc[0], tol);
		assertEquals(slices / 3, (int) maxAndLoc[1]);
		assertEquals(rows / 3, (int) maxAndLoc[2]);
		assertEquals(cols / 3, (int) maxAndLoc[3]);

	}

	@Test
	public void testGetMinLocation() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.setQuick(slices / 3, rows / 3, cols / 3, -0.7);
		A.setQuick(slices / 3, rows / 2, cols / 2, -0.7);
		double[] minAndLoc = A.getMinLocation();
		assertEquals(-0.7, minAndLoc[0], tol);
		assertEquals(slices / 3, (int) minAndLoc[1]);
		assertEquals(rows / 3, (int) minAndLoc[2]);
		assertEquals(cols / 3, (int) minAndLoc[3]);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.setQuick(slices / 3, rows / 3, cols / 3, -0.7);
		A.setQuick(slices / 3, rows / 2, cols / 2, -0.7);
		minAndLoc = A.getMinLocation();
		assertEquals(-0.7, minAndLoc[0], tol);
		assertEquals(slices / 3, (int) minAndLoc[1]);
		assertEquals(rows / 3, (int) minAndLoc[2]);
		assertEquals(cols / 3, (int) minAndLoc[3]);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(cols, rows, slices);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		Av.setQuick(slices / 3, rows / 3, cols / 3, -0.7);
		Av.setQuick(slices / 3, rows / 2, cols / 2, -0.7);
		minAndLoc = Av.getMinLocation();
		assertEquals(-0.7, minAndLoc[0], tol);
		assertEquals(slices / 3, (int) minAndLoc[1]);
		assertEquals(rows / 3, (int) minAndLoc[2]);
		assertEquals(cols / 3, (int) minAndLoc[3]);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(cols, rows, slices);
		Av = A.viewDice(2, 1, 0);
		Av.setQuick(slices / 3, rows / 3, cols / 3, -0.7);
		Av.setQuick(slices / 3, rows / 2, cols / 2, -0.7);
		minAndLoc = Av.getMinLocation();
		assertEquals(-0.7, minAndLoc[0], tol);
		assertEquals(slices / 3, (int) minAndLoc[1]);
		assertEquals(rows / 3, (int) minAndLoc[2]);
		assertEquals(cols / 3, (int) minAndLoc[3]);

	}

	@Test
	public void testGetNegativeValues() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.setQuick(slices / 3, rows / 3, cols / 3, -0.7);
		A.setQuick(slices / 2, rows / 2, cols / 2, -0.1);
		ConcurrentHashMap<int[], Double> negatives = A.getNegativeValues();
		assertEquals(2, negatives.size());
		assertTrue(negatives.containsValue(-0.7));
		assertTrue(negatives.containsValue(-0.1));
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.setQuick(slices / 3, rows / 3, cols / 3, -0.7);
		A.setQuick(slices / 2, rows / 2, cols / 2, -0.1);
		negatives = A.getNegativeValues();
		assertEquals(2, negatives.size());
		assertTrue(negatives.containsValue(-0.7));
		assertTrue(negatives.containsValue(-0.1));
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(cols, rows, slices);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		Av.setQuick(slices / 3, rows / 3, cols / 3, -0.7);
		Av.setQuick(slices / 2, rows / 2, cols / 2, -0.1);
		negatives = Av.getNegativeValues();
		assertEquals(2, negatives.size());
		assertTrue(negatives.containsValue(-0.7));
		assertTrue(negatives.containsValue(-0.1));
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(cols, rows, slices);
		Av = A.viewDice(2, 1, 0);
		Av.setQuick(slices / 3, rows / 3, cols / 3, -0.7);
		Av.setQuick(slices / 2, rows / 2, cols / 2, -0.1);
		negatives = Av.getNegativeValues();
		assertEquals(2, negatives.size());
		assertTrue(negatives.containsValue(-0.7));
		assertTrue(negatives.containsValue(-0.1));
	}

	@Test
	public void testGetNonZeros() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.setQuick(slices / 4, rows / 4, cols / 4, 1);
		A.setQuick(slices / 2, rows / 2, cols / 2, 2);
		A.setQuick(slices - 1, rows - 1, cols - 1, 3);
		IntArrayList sliceList = new IntArrayList();
		IntArrayList rowList = new IntArrayList();
		IntArrayList colList = new IntArrayList();
		DoubleArrayList valueList = new DoubleArrayList();
		A.getNonZeros(sliceList, rowList, colList, valueList);
		assertEquals(3, sliceList.size());
		assertEquals(3, rowList.size());
		assertEquals(3, colList.size());
		assertEquals(3, valueList.size());
		assertEquals(slices / 4, sliceList.get(0));
		assertEquals(rows / 4, rowList.get(0));
		assertEquals(cols / 4, colList.get(0));
		assertEquals(slices / 2, sliceList.get(1));
		assertEquals(rows / 2, rowList.get(1));
		assertEquals(cols / 2, colList.get(1));
		assertEquals(slices - 1, sliceList.get(2));
		assertEquals(rows - 1, rowList.get(2));
		assertEquals(cols - 1, colList.get(2));
		assertEquals(1, valueList.get(0), tol);
		assertEquals(2, valueList.get(1), tol);
		assertEquals(3, valueList.get(2), tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.setQuick(slices / 4, rows / 4, cols / 4, 1);
		A.setQuick(slices / 2, rows / 2, cols / 2, 2);
		A.setQuick(slices - 1, rows - 1, cols - 1, 3);
		sliceList = new IntArrayList();
		rowList = new IntArrayList();
		colList = new IntArrayList();
		valueList = new DoubleArrayList();
		A.getNonZeros(sliceList, rowList, colList, valueList);
		assertEquals(3, sliceList.size());
		assertEquals(3, rowList.size());
		assertEquals(3, colList.size());
		assertEquals(3, valueList.size());
		assertEquals(slices / 4, sliceList.get(0));
		assertEquals(rows / 4, rowList.get(0));
		assertEquals(cols / 4, colList.get(0));
		assertEquals(slices / 2, sliceList.get(1));
		assertEquals(rows / 2, rowList.get(1));
		assertEquals(cols / 2, colList.get(1));
		assertEquals(slices - 1, sliceList.get(2));
		assertEquals(rows - 1, rowList.get(2));
		assertEquals(cols - 1, colList.get(2));
		assertEquals(1, valueList.get(0), tol);
		assertEquals(2, valueList.get(1), tol);
		assertEquals(3, valueList.get(2), tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(cols, rows, rows);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		Av.setQuick(slices / 4, rows / 4, cols / 4, 1);
		Av.setQuick(slices / 2, rows / 2, cols / 2, 2);
		Av.setQuick(slices - 1, rows - 1, cols - 1, 3);
		sliceList = new IntArrayList();
		rowList = new IntArrayList();
		colList = new IntArrayList();
		valueList = new DoubleArrayList();
		Av.getNonZeros(sliceList, rowList, colList, valueList);
		assertEquals(3, sliceList.size());
		assertEquals(3, rowList.size());
		assertEquals(3, colList.size());
		assertEquals(3, valueList.size());
		assertEquals(slices / 4, sliceList.get(0));
		assertEquals(rows / 4, rowList.get(0));
		assertEquals(cols / 4, colList.get(0));
		assertEquals(slices / 2, sliceList.get(1));
		assertEquals(rows / 2, rowList.get(1));
		assertEquals(cols / 2, colList.get(1));
		assertEquals(slices - 1, sliceList.get(2));
		assertEquals(rows - 1, rowList.get(2));
		assertEquals(cols - 1, colList.get(2));
		assertEquals(1, valueList.get(0), tol);
		assertEquals(2, valueList.get(1), tol);
		assertEquals(3, valueList.get(2), tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(cols, rows, rows);
		Av = A.viewDice(2, 1, 0);
		Av.setQuick(slices / 4, rows / 4, cols / 4, 1);
		Av.setQuick(slices / 2, rows / 2, cols / 2, 2);
		Av.setQuick(slices - 1, rows - 1, cols - 1, 3);
		sliceList = new IntArrayList();
		rowList = new IntArrayList();
		colList = new IntArrayList();
		valueList = new DoubleArrayList();
		Av.getNonZeros(sliceList, rowList, colList, valueList);
		assertEquals(3, sliceList.size());
		assertEquals(3, rowList.size());
		assertEquals(3, colList.size());
		assertEquals(3, valueList.size());
		assertEquals(slices / 4, sliceList.get(0));
		assertEquals(rows / 4, rowList.get(0));
		assertEquals(cols / 4, colList.get(0));
		assertEquals(slices / 2, sliceList.get(1));
		assertEquals(rows / 2, rowList.get(1));
		assertEquals(cols / 2, colList.get(1));
		assertEquals(slices - 1, sliceList.get(2));
		assertEquals(rows - 1, rowList.get(2));
		assertEquals(cols - 1, colList.get(2));
		assertEquals(1, valueList.get(0), tol);
		assertEquals(2, valueList.get(1), tol);
		assertEquals(3, valueList.get(2), tol);
	}

	@Test
	public void testGetPositiveValues() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.setQuick(slices / 3, rows / 3, cols / 3, 0.7);
		A.setQuick(slices / 2, rows / 2, cols / 2, 0.1);
		ConcurrentHashMap<int[], Double> positives = A.getPositiveValues();
		assertEquals(2, positives.size());
		assertTrue(positives.containsValue(0.7));
		assertTrue(positives.containsValue(0.1));
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(slices, rows, cols);
		A.setQuick(slices / 3, rows / 3, cols / 3, 0.7);
		A.setQuick(slices / 2, rows / 2, cols / 2, 0.1);
		positives = A.getPositiveValues();
		assertEquals(2, positives.size());
		assertTrue(positives.containsValue(0.7));
		assertTrue(positives.containsValue(0.1));
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(cols, rows, slices);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		Av.setQuick(slices / 3, rows / 3, cols / 3, 0.7);
		Av.setQuick(slices / 2, rows / 2, cols / 2, 0.1);
		positives = Av.getPositiveValues();
		assertEquals(2, positives.size());
		assertTrue(positives.containsValue(0.7));
		assertTrue(positives.containsValue(0.1));
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(cols, rows, slices);
		Av = A.viewDice(2, 1, 0);
		Av.setQuick(slices / 3, rows / 3, cols / 3, 0.7);
		Av.setQuick(slices / 2, rows / 2, cols / 2, 0.1);
		positives = Av.getPositiveValues();
		assertEquals(2, positives.size());
		assertTrue(positives.containsValue(0.7));
		assertTrue(positives.containsValue(0.1));
	}

	@Test
	public void testGetQuick() {
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(a_3d[s][r][c], A.getQuick(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testSet() {
		DoubleMatrix3D A = new DenseDoubleMatrix3D(slices, rows, cols);
		double elem = Math.random();
		A.set(slices / 2, rows / 2, cols / 2, elem);
		assertEquals(elem, A.getQuick(slices / 2, rows / 2, cols / 2), tol);
	}

	@Test
	public void testSetQuick() {
		DoubleMatrix3D A = new DenseDoubleMatrix3D(slices, rows, cols);
		double elem = Math.random();
		A.setQuick(slices / 2, rows / 2, cols / 2, elem);
		assertEquals(elem, A.getQuick(slices / 2, rows / 2, cols / 2), tol);
	}

	@Test
	public void testToArray() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		double[][][] array = A.toArray();
		Utils.assertArrayEquals(a_3d, array, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		array = A.toArray();
		Utils.assertArrayEquals(a_3d, array, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		array = Av.toArray();
		for (int s = 0; s < cols; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < slices; c++) {
					assertEquals(a_3d[c][r][s], Av.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		Av = A.viewDice(2, 1, 0);
		array = Av.toArray();
		for (int s = 0; s < cols; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < slices; c++) {
					assertEquals(a_3d[c][r][s], Av.getQuick(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testVectorize() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix1D B = A.vectorize();
		int idx = 0;
		for (int s = 0; s < slices; s++) {
			for (int c = 0; c < cols; c++) {
				for (int r = 0; r < rows; r++) {
					assertEquals(A.getQuick(s, r, c), B.getQuick(idx++), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = A.vectorize();
		idx = 0;
		for (int s = 0; s < slices; s++) {
			for (int c = 0; c < cols; c++) {
				for (int r = 0; r < rows; r++) {
					assertEquals(A.getQuick(s, r, c), B.getQuick(idx++), tol);
				}
			}
		}
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		B = Av.vectorize();
		idx = 0;
		for (int s = 0; s < cols; s++) {
			for (int c = 0; c < slices; c++) {
				for (int r = 0; r < rows; r++) {
					assertEquals(Av.getQuick(s, r, c), B.getQuick(idx++), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_2D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		Av = A.viewDice(2, 1, 0);
		B = Av.vectorize();
		idx = 0;
		for (int s = 0; s < cols; s++) {
			for (int c = 0; c < slices; c++) {
				for (int r = 0; r < rows; r++) {
					assertEquals(Av.getQuick(s, r, c), B.getQuick(idx++), tol);
				}
			}
		}
	}

	@Test
	public void testViewColumn() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix2D B = A.viewColumn(cols / 2);
		assertEquals(slices, B.rows());
		assertEquals(rows, B.columns());
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				assertEquals(a_3d[s][r][cols / 2], B.getQuick(s, r), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = A.viewColumn(cols / 2);
		assertEquals(slices, B.rows());
		assertEquals(rows, B.columns());
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				assertEquals(a_3d[s][r][cols / 2], B.getQuick(s, r), tol);
			}
		}

	}

	@Test
	public void testViewColumnFlip() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D B = A.viewColumnFlip();
		assertEquals(A.size(), B.size());
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(a_3d[s][r][cols - 1 - c], B.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = A.viewColumnFlip();
		assertEquals(A.size(), B.size());
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(a_3d[s][r][cols - 1 - c], B.getQuick(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testViewDice() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D B = A.viewDice(2, 1, 0);
		assertEquals(A.slices(), B.columns());
		assertEquals(A.rows(), B.rows());
		assertEquals(A.columns(), B.slices());
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(A.getQuick(s, r, c), B.getQuick(c, r, s), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = A.viewDice(2, 1, 0);
		assertEquals(A.slices(), B.columns());
		assertEquals(A.rows(), B.rows());
		assertEquals(A.columns(), B.slices());
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(A.getQuick(s, r, c), B.getQuick(c, r, s), tol);
				}
			}
		}
	}

	@Test
	public void testViewPart() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D B = A.viewPart(2, 15, 11, 2, 21, 27);
		for (int s = 0; s < 2; s++) {
			for (int r = 0; r < 21; r++) {
				for (int c = 0; c < 27; c++) {
					assertEquals(A.getQuick(2 + s, 15 + r, 11 + c), B.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = A.viewPart(2, 15, 11, 2, 21, 27);
		for (int s = 0; s < 2; s++) {
			for (int r = 0; r < 21; r++) {
				for (int c = 0; c < 27; c++) {
					assertEquals(A.getQuick(2 + s, 15 + r, 11 + c), B.getQuick(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testViewRow() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix2D B = A.viewRow(rows / 2);
		assertEquals(slices, B.rows());
		assertEquals(cols, B.columns());
		for (int s = 0; s < slices; s++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_3d[s][rows / 2][c], B.getQuick(s, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = A.viewRow(rows / 2);
		assertEquals(slices, B.rows());
		assertEquals(cols, B.columns());
		for (int s = 0; s < slices; s++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_3d[s][rows / 2][c], B.getQuick(s, c), tol);
			}
		}
	}

	@Test
	public void testViewRowFlip() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D B = A.viewRowFlip();
		assertEquals(A.size(), B.size());
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(a_3d[s][rows - 1 - r][c], B.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = A.viewRowFlip();
		assertEquals(A.size(), B.size());
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(a_3d[s][rows - 1 - r][c], B.getQuick(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testViewSelectionDoubleMatrix2DProcedure() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		final double value = 2;
		A.setQuick(3, rows / 4, 0, value);
		DoubleMatrix3D B = A.viewSelection(new DoubleMatrix2DProcedure() {
			public boolean apply(DoubleMatrix2D element) {
				if (Math.abs(element.getQuick(rows / 4, 0) - value) <= tol) {
					return true;
				} else {
					return false;
				}

			}
		});
		assertEquals(1, B.slices());
		assertEquals(A.rows(), B.rows());
		assertEquals(A.columns(), B.columns());
		assertEquals(A.getQuick(3, rows / 4, 0), B.getQuick(0, rows / 4, 0), tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		A.setQuick(3, rows / 4, 0, value);
		B = A.viewSelection(new DoubleMatrix2DProcedure() {
			public boolean apply(DoubleMatrix2D element) {
				if (Math.abs(element.getQuick(rows / 4, 0) - value) <= tol) {
					return true;
				} else {
					return false;
				}

			}
		});
		assertEquals(1, B.slices());
		assertEquals(A.rows(), B.rows());
		assertEquals(A.columns(), B.columns());
		assertEquals(A.getQuick(3, rows / 4, 0), B.getQuick(0, rows / 4, 0), tol);
	}

	@Test
	public void testViewSelectionIntArrayIntArrayIntArray() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		int[] sliceIndexes = new int[] { 2, 3 };
		int[] rowIndexes = new int[] { 5, 11, 22, 37 };
		int[] colIndexes = new int[] { 2, 17, 32, 47, 51 };
		DoubleMatrix3D B = A.viewSelection(sliceIndexes, rowIndexes, colIndexes);
		assertEquals(sliceIndexes.length, B.slices());
		assertEquals(rowIndexes.length, B.rows());
		assertEquals(colIndexes.length, B.columns());
		for (int s = 0; s < sliceIndexes.length; s++) {
			for (int r = 0; r < rowIndexes.length; r++) {
				for (int c = 0; c < colIndexes.length; c++) {
					assertEquals(A.getQuick(sliceIndexes[s], rowIndexes[r], colIndexes[c]), B.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		sliceIndexes = new int[] { 2, 3 };
		rowIndexes = new int[] { 5, 11, 22, 37 };
		colIndexes = new int[] { 2, 17, 32, 47, 51 };
		B = A.viewSelection(sliceIndexes, rowIndexes, colIndexes);
		assertEquals(sliceIndexes.length, B.slices());
		assertEquals(rowIndexes.length, B.rows());
		assertEquals(colIndexes.length, B.columns());
		for (int s = 0; s < sliceIndexes.length; s++) {
			for (int r = 0; r < rowIndexes.length; r++) {
				for (int c = 0; c < colIndexes.length; c++) {
					assertEquals(A.getQuick(sliceIndexes[s], rowIndexes[r], colIndexes[c]), B.getQuick(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testViewSlice() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix2D B = A.viewSlice(slices / 2);
		assertEquals(rows, B.rows());
		assertEquals(cols, B.columns());
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_3d[slices / 2][r][c], B.getQuick(r, c), tol);
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = A.viewSlice(slices / 2);
		assertEquals(rows, B.rows());
		assertEquals(cols, B.columns());
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				assertEquals(a_3d[slices / 2][r][c], B.getQuick(r, c), tol);
			}
		}
	}

	@Test
	public void testViewSliceFlip() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D B = A.viewSliceFlip();
		assertEquals(A.size(), B.size());
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(a_3d[slices - 1 - s][r][c], B.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = A.viewSliceFlip();
		assertEquals(A.size(), B.size());
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					assertEquals(a_3d[slices - 1 - s][r][c], B.getQuick(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testViewSorted() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D B = A.viewSorted(1, 1);
		for (int s = 0; s < slices - 1; s++) {
			if (B.getQuick(s, 1, 1) > B.getQuick(s + 1, 1, 1)) {
				fail();
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = A.viewSorted(1, 1);
		for (int s = 0; s < slices - 1; s++) {
			if (B.getQuick(s, 1, 1) > B.getQuick(s + 1, 1, 1)) {
				fail();
			}
		}
	}

	@Test
	public void testViewStrides() {
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		int sliceStride = 2;
		int rowStride = 3;
		int colStride = 5;
		DoubleMatrix3D B = A.viewStrides(sliceStride, rowStride, colStride);
		for (int s = 0; s < B.slices(); s++) {
			for (int r = 0; r < B.rows(); r++) {
				for (int c = 0; c < B.columns(); c++) {
					assertEquals(A.getQuick(s * sliceStride, r * rowStride, c * colStride), B.getQuick(s, r, c), tol);
				}
			}
		}
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		B = A.viewStrides(sliceStride, rowStride, colStride);
		for (int s = 0; s < B.slices(); s++) {
			for (int r = 0; r < B.rows(); r++) {
				for (int c = 0; c < B.columns(); c++) {
					assertEquals(A.getQuick(s * sliceStride, r * rowStride, c * colStride), B.getQuick(s, r, c), tol);
				}
			}
		}
	}

	@Test
	public void testZSum() {
		/* No view */
		// single thread
		Utils.setNP(1);
		DoubleMatrix3D A = new DenseDoubleMatrix3D(a_3d);
		double aSum = A.zSum();
		double tmpSum = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					tmpSum += a_3d[s][r][c];
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		aSum = A.zSum();
		tmpSum = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					tmpSum += a_3d[s][r][c];
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
		/* View */
		// single thread
		Utils.setNP(1);
		A = new DenseDoubleMatrix3D(a_3d);
		DoubleMatrix3D Av = A.viewDice(2, 1, 0);
		aSum = Av.zSum();
		tmpSum = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					tmpSum += a_3d[s][r][c];
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
		// multiple threads
		Utils.setNP(nThreads);
		Utils.setThreadsBeginN_3D(nThreadsBegin);
		A = new DenseDoubleMatrix3D(a_3d);
		Av = A.viewDice(2, 1, 0);
		aSum = Av.zSum();
		tmpSum = 0;
		for (int s = 0; s < slices; s++) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					tmpSum += a_3d[s][r][c];
				}
			}
		}
		assertEquals(tmpSum, aSum, tol);
	}

}
