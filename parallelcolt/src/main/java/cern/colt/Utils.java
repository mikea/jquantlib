/*
Copyright © 1999 CERN - European Organization for Nuclear Research
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
is hereby granted without fee, provided that the above copyright notice appear in all copies and 
that both that copyright notice and this permission notice appear in supporting documentation. 
CERN makes no representations about the suitability of this software for any purpose. 
It is provided "as is" without expressed or implied warranty.
 */
package cern.colt;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.junit.Assert;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.DoubleMatrix2DMatrix2DFunction;

/**
 * Utility methods.
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * @author wolfgang.hoschek@cern.ch
 * 
 * @version 1.0 08/22/2007
 */
public class Utils {
	public static ExecutorService threadPool = Executors.newCachedThreadPool(new ColtThreadFactory(new ColtExceptionHandler()));

	private static int np = concurrency();

	private static int THREADS_BEGIN_N_1D = 32768;

	private static int THREADS_BEGIN_N_2D = 65536;

	private static int THREADS_BEGIN_N_3D = 65536;

	private static final String FF = "%.4f";

	private static class ColtExceptionHandler implements Thread.UncaughtExceptionHandler {

		public void uncaughtException(Thread t, Throwable e) {
			e.printStackTrace();
		}

	}

	private static class ColtThreadFactory implements ThreadFactory {
		private static final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

		private final Thread.UncaughtExceptionHandler handler;

		ColtThreadFactory(Thread.UncaughtExceptionHandler handler) {
			this.handler = handler;
		}

		public Thread newThread(Runnable r) {
			Thread t = defaultFactory.newThread(r);
			t.setUncaughtExceptionHandler(handler);
			return t;
		}
	};

	/**
	 * Returns the number of available processors
	 * 
	 * @return number of available processors
	 */
	public static int concurrency() {
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		if (availableProcessors > 1) {
			return prevPow2(availableProcessors);
		} else {
			return 1;
		}
	}

	/**
	 * Returns the number of available processors ( = number of threads used in
	 * calculations).
	 * 
	 * @return the number of available processors
	 */
	public static int getNP() {
		return np;
	}

	public static int getThreadsBeginN_1D() {
		return THREADS_BEGIN_N_1D;
	}

	public static int getThreadsBeginN_2D() {
		return THREADS_BEGIN_N_2D;
	}

	public static int getThreadsBeginN_3D() {
		return THREADS_BEGIN_N_3D;
	}

	public static void setThreadsBeginN_1D(int n) {
		THREADS_BEGIN_N_1D = n;
	}

	public static void setThreadsBeginN_2D(int n) {
		THREADS_BEGIN_N_2D = n;
	}

	public static void setThreadsBeginN_3D(int n) {
		THREADS_BEGIN_N_3D = n;
	}

	/**
	 * Sets the number of available processors ( = number of threads used in
	 * calculations). If n is not a power of 2, then the number of available
	 * processors is set to the closest power of two less than n.
	 * 
	 * @param n
	 * @return the number of available processors
	 */
	public static int setNP(int n) {
		if (isPowerOf2(n)) {
			np = n;
		} else {
			np = prevPow2(n);
		}
		// if (n > 0) {
		// np = n;
		// }
		// else {
		// np = concurrency();
		// }
		return np;
	}

	/**
	 * Returns the closest power of two greater than or equal to x.
	 * 
	 * @param x
	 * @return the closest power of two greater than or equal to x
	 */
	public static int nextPow2(int x) {
		if (x < 1)
			throw new IllegalArgumentException("x must be greater or equal 1");
		x |= (x >>> 1);
		x |= (x >>> 2);
		x |= (x >>> 4);
		x |= (x >>> 8);
		x |= (x >>> 16);
		x |= (x >>> 32);
		return x + 1;
	}

	public static int extendDimension(int x) {
		if (x < 1)
			throw new IllegalArgumentException("x must be greater or equal 1");
		int nextExp = nextExp2(x);
		int nextPow = nextExp + 1;
		int extDim = (int) Math.round(Math.pow(2.0, (double) nextPow));
		return extDim;
	}

	public static int nextExp2(int n) {

		double e = Math.log((double) n) / Math.log(2.0);
		int p = (int) Math.ceil(e);
		double f = n / Math.pow(2.0, (double) p);
		if (f == 0.5) {
			p = p - 1;
		}
		return p;
	}

	/**
	 * Returns the closest power of two less than or equal to x
	 * 
	 * @param x
	 * @return the closest power of two less then or equal to x
	 */
	public static int prevPow2(int x) {
		if (x < 1)
			throw new IllegalArgumentException("x must be greater or equal 1");
		return (int) Math.pow(2, Math.floor(Math.log(x) / Math.log(2)));
	}

	/**
	 * Checks if n is power of 2
	 * 
	 * @param n
	 * @return true if n is power of 2
	 */
	public static boolean isPowerOf2(int n) {
		if (n <= 0)
			return false;
		else
			return (n & (n - 1)) == 0;
	}

	/**
	 * Fill 1D matrix with random numbers.
	 * 
	 * @param N
	 *            size
	 * @param m
	 *            1D matrix
	 */
	public static void fillMatrix_1D(int N, double[] m) {
		Random r = new Random(2);
		for (int i = 0; i < N; i++) {
			m[i] = r.nextDouble();
		}
	}

	/**
	 * Fills 2D matrix with random numbers.
	 * 
	 * @param n1
	 *            rows
	 * @param n2
	 *            columns
	 * @param m
	 *            2D matrix
	 */
	public static void fillMatrix_2D(int n1, int n2, double[] m) {
		Random r = new Random(2);
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				m[i * n2 + j] = r.nextDouble();
			}
		}
	}

	/**
	 * Fills 3D matrix with random numbers.
	 * 
	 * @param n1
	 *            slices
	 * @param n2
	 *            rows
	 * @param n3
	 *            columns
	 * @param m
	 *            3D matrix
	 */
	public static void fillMatrix_3D(int n1, int n2, int n3, double[] m) {
		Random r = new Random(2);
		int sliceStride = n2 * n3;
		int rowStride = n3;
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				for (int k = 0; k < n3; k++) {
					m[i * sliceStride + j * rowStride + k] = r.nextDouble();
				}
			}
		}
	}

	/**
	 * Displays elements of <code>x</code>, assuming that it is 1D complex
	 * array. Complex data is represented by 2 double values in sequence: the
	 * real and imaginary parts.
	 * 
	 * @param x
	 * @param title
	 */
	public static void showComplex_1D(double[] x, String title) {
		System.out.println(title);
		System.out.println("-------------------");
		for (int i = 0; i < x.length; i = i + 2) {
			if (x[i + 1] == 0) {
				System.out.println(String.format(FF, x[i]));
				continue;
			}
			if (x[i] == 0) {
				System.out.println(String.format(FF, x[i + 1]) + "i");
				continue;
			}
			if (x[i + 1] < 0) {
				System.out.println(String.format(FF, x[i]) + " - " + (String.format(FF, -x[i + 1])) + "i");
				continue;
			}
			System.out.println(String.format(FF, x[i]) + " + " + (String.format(FF, x[i + 1])) + "i");
		}
		System.out.println();
	}

	/**
	 * Displays elements of <code>x</code>, assuming that it is 2D complex
	 * array. Complex data is represented by 2 double values in sequence: the
	 * real and imaginary parts.
	 * 
	 * @param rows
	 * @param columns
	 * @param x
	 * @param title
	 */
	public static void showComplex_2D(int rows, int columns, double[] x, String title) {
		StringBuffer s = new StringBuffer(String.format(title + ": complex array 2D: %d rows, %d columns\n\n", rows, columns));
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < 2 * columns; c = c + 2) {
				if (x[r * 2 * columns + c + 1] == 0) {
					s.append(String.format(FF + "\t", x[r * 2 * columns + c]));
					continue;
				}
				if (x[r * 2 * columns + c] == 0) {
					s.append(String.format(FF + "i\t", x[r * 2 * columns + c + 1]));
					continue;
				}
				if (x[r * 2 * columns + c + 1] < 0) {
					s.append(String.format(FF + " - " + FF + "i\t", x[r * 2 * columns + c], -x[r * 2 * columns + c + 1]));
					continue;
				}
				s.append(String.format(FF + " + " + FF + "i\t", x[r * 2 * columns + c], x[r * 2 * columns + c + 1]));
			}
			s.append("\n");
		}
		System.out.println(s.toString());
	}

	/**
	 * Displays elements of <code>x</code>, assuming that it is 3D complex
	 * array. Complex data is represented by 2 double values in sequence: the
	 * real and imaginary parts.
	 * 
	 * @param n1
	 * @param n2
	 * @param n3
	 * @param x
	 * @param title
	 */
	public static void showComplex_3D(int n1, int n2, int n3, double[] x, String title) {
		int sliceStride = n2 * 2 * n3;
		int rowStride = 2 * n3;

		System.out.println(title);
		System.out.println("-------------------");

		for (int k = 0; k < 2 * n3; k = k + 2) {
			System.out.println("(:,:," + k / 2 + ")=\n");
			for (int i = 0; i < n1; i++) {
				for (int j = 0; j < n2; j++) {
					if (x[i * sliceStride + j * rowStride + k + 1] == 0) {
						System.out.print(String.format(FF, x[i * sliceStride + j * rowStride + k]) + "\t");
						continue;
					}
					if (x[i * sliceStride + j * rowStride + k] == 0) {
						System.out.print(String.format(FF, x[i * sliceStride + j * rowStride + k + 1]) + "i\t");
						continue;
					}
					if (x[i * sliceStride + j * rowStride + k + 1] < 0) {
						System.out.print(String.format(FF, x[i * sliceStride + j * rowStride + k]) + " - " + String.format(FF, -x[i * sliceStride + j * rowStride + k + 1]) + "i\t");
						continue;
					}
					System.out.print(String.format(FF, x[i * sliceStride + j * rowStride + k]) + " + " + String.format(FF, x[i * sliceStride + j * rowStride + k + 1]) + "i\t");
				}
				System.out.println("");
			}
		}
		System.out.println("");
	}

	/**
	 * Displays elements of <code>x</code>, assuming that it is 1D real
	 * array.
	 * 
	 * @param x
	 * @param title
	 */
	public static void showReal_1D(double[] x, String title) {
		System.out.println(title);
		System.out.println("-------------------");
		for (int j = 0; j < x.length; j++) {
			System.out.println(String.format(FF, x[j]));
		}
		System.out.println();
	}

	/**
	 * Displays elements of <code>x</code>, assuming that it is 2D real
	 * array.
	 * 
	 * @param n1
	 * @param n2
	 * @param x
	 * @param title
	 */
	public static void showReal_2D(int n1, int n2, double[] x, String title) {
		System.out.println(title);
		System.out.println("-------------------");
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				if (Math.abs(x[i * n2 + j]) < 5e-5) {
					System.out.print("0\t");
				} else {
					System.out.print(String.format(FF, x[i * n2 + j]) + "\t");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * Displays elements of <code>x</code>, assuming that it is 3D real
	 * array.
	 * 
	 * @param n1
	 * @param n2
	 * @param n3
	 * @param x
	 * @param title
	 */
	public static void showReal_3D(int n1, int n2, int n3, double[] x, String title) {
		int sliceStride = n2 * n3;
		int rowStride = n3;

		System.out.println(title);
		System.out.println("-------------------");

		for (int k = 0; k < 2 * n3; k = k + 2) {
			System.out.println();
			System.out.println("(:,:," + k / 2 + ")=\n");
			for (int i = 0; i < n1; i++) {
				for (int j = 0; j < n2; j++) {
					if (x[i * sliceStride + j * rowStride + k + 1] == 0) {
						System.out.print(String.format(FF, x[i * sliceStride + j * rowStride + k]) + "\t");
						continue;
					}
					if (x[i * sliceStride + j * rowStride + k] == 0) {
						System.out.print(String.format(FF, x[i * sliceStride + j * rowStride + k + 1]) + "i\t");
						continue;
					}
					if (x[i * sliceStride + j * rowStride + k + 1] < 0) {
						System.out.print(String.format(FF, x[i * sliceStride + j * rowStride + k]) + " - " + String.format(FF, -x[i * sliceStride + j * rowStride + k + 1]) + "i\t");
						continue;
					}
					System.out.print(String.format(FF, x[i * sliceStride + j * rowStride + k]) + " + " + String.format(FF, x[i * sliceStride + j * rowStride + k + 1]) + "i\t");
				}
				System.out.println();
			}
		}
		System.out.println();
	}

	/**
	 * Saves elements of <code>x</code> in a file <code>filename</code>,
	 * assuming that it is 1D complex array. Complex data is represented by 2
	 * double values in sequence: the real and imaginary parts.
	 * 
	 * @param x
	 * @param filename
	 */
	public static void writeToFileComplex_1D(double[] x, String filename) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			for (int i = 0; i < x.length; i = i + 2) {
				if (x[i + 1] == 0) {
					out.write(String.format(FF, x[i]));
					out.newLine();
					continue;
				}
				if (x[i] == 0) {
					out.write(String.format(FF, x[i + 1]) + "i");
					out.newLine();
					continue;
				}
				if (x[i + 1] < 0) {
					out.write(String.format(FF, x[i]) + " - " + String.format(FF, -x[i + 1]) + "i");
					out.newLine();
					continue;
				}
				out.write(String.format(FF, x[i]) + " + " + String.format(FF, x[i + 1]) + "i");
				out.newLine();
			}
			out.newLine();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves elements of <code>x</code> in a file <code>filename</code>,
	 * assuming that it is 2D complex array. Complex data is represented by 2
	 * double values in sequence: the real and imaginary parts.
	 * 
	 * @param n1
	 * @param n2
	 * @param x
	 * @param filename
	 */
	public static void writeToFileComplex_2D(int n1, int n2, double[] x, String filename) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			for (int i = 0; i < n1; i++) {
				for (int j = 0; j < 2 * n2; j = j + 2) {
					if ((Math.abs(x[i * 2 * n2 + j]) < 5e-5) && (Math.abs(x[i * 2 * n2 + j + 1]) < 5e-5)) {
						if (x[i * 2 * n2 + j + 1] >= 0.0) {
							out.write("0 + 0i\t");
						} else {
							out.write("0 - 0i\t");
						}
						continue;
					}

					if (Math.abs(x[i * 2 * n2 + j + 1]) < 5e-5) {
						if (x[i * 2 * n2 + j + 1] >= 0.0) {
							out.write(String.format(FF, x[i * 2 * n2 + j]) + " + 0i\t");
						} else {
							out.write(String.format(FF, x[i * 2 * n2 + j]) + " - 0i\t");
						}
						continue;
					}
					if (Math.abs(x[i * 2 * n2 + j]) < 5e-5) {
						if (x[i * 2 * n2 + j + 1] >= 0.0) {
							out.write("0 + " + String.format(FF, x[i * 2 * n2 + j + 1]) + "i\t");
						} else {
							out.write("0 - " + String.format(FF, -x[i * 2 * n2 + j + 1]) + "i\t");
						}
						continue;
					}
					if (x[i * 2 * n2 + j + 1] < 0) {
						out.write(String.format(FF, x[i * 2 * n2 + j]) + " - " + String.format(FF, -x[i * 2 * n2 + j + 1]) + "i\t");
						continue;
					}
					out.write(String.format(FF, x[i * 2 * n2 + j]) + " + " + String.format(FF, x[i * 2 * n2 + j + 1]) + "i\t");
				}
				out.newLine();
			}

			out.newLine();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves elements of <code>x</code> in a file <code>filename</code>,
	 * assuming that it is 3D complex array. Complex data is represented by 2
	 * double values in sequence: the real and imaginary parts.
	 * 
	 * @param n1
	 * @param n2
	 * @param n3
	 * @param x
	 * @param filename
	 */
	public static void writeToFileComplex_3D(int n1, int n2, int n3, double[] x, String filename) {
		int sliceStride = n2 * n3 * 2;
		int rowStride = n3 * 2;
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			for (int k = 0; k < 2 * n3; k = k + 2) {
				out.newLine();
				out.write("(:,:," + k / 2 + ")=");
				out.newLine();
				out.newLine();
				for (int i = 0; i < n1; i++) {
					for (int j = 0; j < n2; j++) {
						if (x[i * sliceStride + j * rowStride + k + 1] == 0) {
							out.write(String.format(FF, x[i * sliceStride + j * rowStride + k]) + "\t");
							continue;
						}
						if (x[i * sliceStride + j * rowStride + k] == 0) {
							out.write(String.format(FF, x[i * sliceStride + j * rowStride + k + 1]) + "i\t");
							continue;
						}
						if (x[i * sliceStride + j * rowStride + k + 1] < 0) {
							out.write(String.format(FF, x[i * sliceStride + j * rowStride + k]) + " - " + String.format(FF, -x[i * sliceStride + j * rowStride + k + 1]) + "i\t");
							continue;
						}
						out.write(String.format(FF, x[i * sliceStride + j * rowStride + k]) + " + " + String.format(FF, x[i * sliceStride + j * rowStride + k + 1]) + "i\t");
					}
					out.newLine();
				}
			}
			out.newLine();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves elements of <code>x</code> in a file <code>filename</code>,
	 * assuming that it is 2D real array.
	 * 
	 * @param x
	 * @param filename
	 */
	public static void writeToFileReal_1D(double[] x, String filename) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			for (int j = 0; j < x.length; j++) {
				out.write(String.format(FF, x[j]));
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves elements of <code>x</code> in a file <code>filename</code>,
	 * assuming that it is 2D real array.
	 * 
	 * @param n1
	 * @param n2
	 * @param x
	 * @param filename
	 */
	public static void writeToFileReal_2D(int n1, int n2, double[] x, String filename, boolean integers) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			for (int i = 0; i < n1; i++) {
				for (int j = 0; j < n2; j++) {
					if (Math.abs(x[i * n2 + j]) < 5e-5) {
						out.write("0\t");
					} else {
						if (integers) {
							out.write(String.format("%.0f", x[i * n2 + j]) + "\t");
						} else {
							out.write(String.format(FF, x[i * n2 + j]) + "\t");
						}
					}
				}
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves elements of <code>x</code> in a file <code>filename</code>,
	 * assuming that it is 3D real array.
	 * 
	 * @param n1
	 * @param n2
	 * @param n3
	 * @param x
	 * @param filename
	 */
	public static void writeToFileReal_3D(int n1, int n2, int n3, double[] x, String filename) {
		int sliceStride = n2 * n3;
		int rowStride = n3;

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			for (int k = 0; k < n3; k = k + 2) {
				out.newLine();
				out.write("(:,:," + k / 2 + ")=");
				out.newLine();
				out.newLine();
				for (int i = 0; i < n1; i++) {
					for (int j = 0; j < n2; j++) {
						out.write(String.format(FF, x[i * sliceStride + j * rowStride + k]) + "\t");
						out.write(String.format(FF, x[i * sliceStride + j * rowStride + k + 1]) + "\t");
					}
					out.newLine();
				}
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves results of the benchmark in a file <code>filename</code>.
	 * 
	 * @param filename
	 * @param sizes
	 * @param times
	 */
	public static void writeResultsToFile(String filename, int[] sizes, double[] times) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			out.write(new Date().toString());
			out.newLine();
			out.write("Number of processors: " + Utils.np);
			out.newLine();
			out.write("sizes=[");
			for (int i = 0; i < sizes.length; i++) {
				out.write(Integer.toString(sizes[i]));
				if (i < sizes.length - 1) {
					out.write(", ");
				} else {
					out.write("]");
				}
			}
			out.newLine();
			out.write("times(in msec)=[");
			for (int i = 0; i < times.length; i++) {
				out.write(String.format("%.2f", times[i]));
				if (i < times.length - 1) {
					out.write(", ");
				} else {
					out.write("]");
				}
			}
			out.newLine();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void assertArrayEquals(double[] a, double[] b, double tol) {
		if (a.length != b.length)
			Assert.fail("a.length != b.length");
		for (int i = 0; i < a.length; i++) {
			assertEquals(a[i], b[i], tol);
		}
	}

	public static void assertArrayEquals(double[][] a, double[][] b, double tol) {
		if ((a.length != b.length) || (a[0].length != b[0].length))
			Assert.fail("a.length != b.length");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				assertEquals(a[i][j], b[i][j], tol);
			}
		}
	}

	public static void assertArrayEquals(double[][][] a, double[][][] b, double tol) {
		if ((a.length != b.length) || (a[0].length != b[0].length) || (a[0][0].length != b[0][0].length))
			Assert.fail("a.length != b.length");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				for (int k = 0; k < a[0][0].length; k++) {
					assertEquals(a[i][j][k], b[i][j][k], tol);
				}
			}
		}
	}

	public static void assertArrayEquals(float[] a, float[] b, float tol) {
		if (a.length != b.length)
			Assert.fail("a.length != b.length");
		for (int i = 0; i < a.length; i++) {
			assertEquals(a[i], b[i], tol);
		}
	}

	public static void assertArrayEquals(float[][] a, float[][] b, float tol) {
		if ((a.length != b.length) || (a[0].length != b[0].length))
			Assert.fail("a.length != b.length");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				assertEquals(a[i][j], b[i][j], tol);
			}
		}
	}

	public static void assertArrayEquals(float[][][] a, float[][][] b, float tol) {
		if ((a.length != b.length) || (a[0].length != b[0].length) || (a[0][0].length != b[0][0].length))
			Assert.fail("a.length != b.length");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				for (int k = 0; k < a[0][0].length; k++) {
					assertEquals(a[i][j][k], b[i][j][k], tol);
				}
			}
		}
	}

	public static void run(final DoubleMatrix2D[] blocksA, final DoubleMatrix2D[] blocksB, final double[] results, final DoubleMatrix2DMatrix2DFunction function) {
		final Future[] subTasks = new Future[blocksA.length];
		for (int i = 0; i < blocksA.length; i++) {
			final int k = i;
			subTasks[i] = threadPool.submit(new Runnable() {
				public void run() {
					double result = function.apply(blocksA[k], blocksB != null ? blocksB[k] : null);
					if (results != null)
						results[k] = result;
				}
			});
		}

		try {
			for (int j = 0; j < blocksA.length; j++) {
				subTasks[j].get();
			}
		} catch (ExecutionException ex) {
			ex.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static DoubleMatrix2D[] splitBlockedNN(DoubleMatrix2D A, int threshold, long flops) {
		/*
		 * determine how to split and parallelize best into blocks if more
		 * B.columns than tasks --> split B.columns, as follows:
		 * 
		 * xx|xx|xxx B xx|xx|xxx xx|xx|xxx A xxx xx|xx|xxx C xxx xx|xx|xxx xxx
		 * xx|xx|xxx xxx xx|xx|xxx xxx xx|xx|xxx
		 * 
		 * if less B.columns than tasks --> split A.rows, as follows:
		 * 
		 * xxxxxxx B xxxxxxx xxxxxxx A xxx xxxxxxx C xxx xxxxxxx --- ------- xxx
		 * xxxxxxx xxx xxxxxxx --- ------- xxx xxxxxxx
		 * 
		 */
		// long flops = 2L*A.rows()*A.columns()*A.columns();
		int noOfTasks = (int) Math.min(flops / threshold, np); // each thread
		// should
		// process at
		// least 30000
		// flops
		boolean splitHoriz = (A.columns() < noOfTasks);
		// boolean splitHoriz = (A.columns() >= noOfTasks);
		int p = splitHoriz ? A.rows() : A.columns();
		noOfTasks = Math.min(p, noOfTasks);

		if (noOfTasks < 2) { // parallelization doesn't pay off (too much
			// start up overhead)
			return null;
		}

		// set up concurrent tasks
		int span = p / noOfTasks;
		final DoubleMatrix2D[] blocks = new DoubleMatrix2D[noOfTasks];
		for (int i = 0; i < noOfTasks; i++) {
			final int offset = i * span;
			if (i == noOfTasks - 1)
				span = p - span * i; // last span may be a bit larger

			final DoubleMatrix2D AA, BB, CC;
			if (!splitHoriz) { // split B along columns into blocks
				blocks[i] = A.viewPart(0, offset, A.rows(), span);
			} else { // split A along rows into blocks
				blocks[i] = A.viewPart(offset, 0, span, A.columns());
			}
		}
		return blocks;
	}

	public static DoubleMatrix2D[][] splitBlockedNN(DoubleMatrix2D A, DoubleMatrix2D B, int threshold, long flops) {
		DoubleMatrix2D[] blocksA = splitBlockedNN(A, threshold, flops);
		if (blocksA == null)
			return null;
		DoubleMatrix2D[] blocksB = splitBlockedNN(B, threshold, flops);
		if (blocksB == null)
			return null;
		DoubleMatrix2D[][] blocks = { blocksA, blocksB };
		return blocks;
	}

	public static DoubleMatrix2D[] splitStridedNN(DoubleMatrix2D A, int threshold, long flops) {
		/*
		 * determine how to split and parallelize best into blocks if more
		 * B.columns than tasks --> split B.columns, as follows:
		 * 
		 * xx|xx|xxx B xx|xx|xxx xx|xx|xxx A xxx xx|xx|xxx C xxx xx|xx|xxx xxx
		 * xx|xx|xxx xxx xx|xx|xxx xxx xx|xx|xxx
		 * 
		 * if less B.columns than tasks --> split A.rows, as follows:
		 * 
		 * xxxxxxx B xxxxxxx xxxxxxx A xxx xxxxxxx C xxx xxxxxxx --- ------- xxx
		 * xxxxxxx xxx xxxxxxx --- ------- xxx xxxxxxx
		 * 
		 */
		// long flops = 2L*A.rows()*A.columns()*A.columns();
		int noOfTasks = (int) Math.min(flops / threshold, np); // each thread
		// should
		// process at
		// least 30000
		// flops
		boolean splitHoriz = (A.columns() < noOfTasks);
		// boolean splitHoriz = (A.columns() >= noOfTasks);
		int p = splitHoriz ? A.rows() : A.columns();
		noOfTasks = Math.min(p, noOfTasks);

		if (noOfTasks < 2) { // parallelization doesn't pay off (too much
			// start up overhead)
			return null;
		}

		// set up concurrent tasks
		int span = p / noOfTasks;
		final DoubleMatrix2D[] blocks = new DoubleMatrix2D[noOfTasks];
		for (int i = 0; i < noOfTasks; i++) {
			final int offset = i * span;
			if (i == noOfTasks - 1)
				span = p - span * i; // last span may be a bit larger

			final DoubleMatrix2D AA, BB, CC;
			if (!splitHoriz) {
				// split B along columns into blocks
				blocks[i] = A.viewPart(0, i, A.rows(), A.columns() - i).viewStrides(1, noOfTasks);
			} else {
				// split A along rows into blocks
				blocks[i] = A.viewPart(i, 0, A.rows() - i, A.columns()).viewStrides(noOfTasks, 1);
			}
		}
		return blocks;
	}
}
