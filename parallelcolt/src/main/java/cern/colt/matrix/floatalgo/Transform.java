/*
Copyright © 1999 CERN - European Organization for Nuclear Research.
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
is hereby granted without fee, provided that the above copyright notice appear in all copies and 
that both that copyright notice and this permission notice appear in supporting documentation. 
CERN makes no representations about the suitability of this software for any purpose. 
It is provided "as is" without expressed or implied warranty.
 */
package cern.colt.matrix.floatalgo;

import cern.colt.matrix.FloatMatrix1D;
import cern.colt.matrix.FloatMatrix2D;

/**
 * Deprecated; Basic element-by-element transformations on
 * {@link cern.colt.matrix.FloatMatrix1D} and
 * {@link cern.colt.matrix.FloatMatrix2D}. All transformations modify the first
 * argument matrix to hold the result of the transformation. Use idioms like
 * <tt>result = mult(matrix.copy(),5)</tt> to leave source matrices
 * unaffected.
 * <p>
 * If your favourite transformation is not provided by this class, consider
 * using method <tt>assign</tt> in combination with prefabricated function
 * objects of {@link cern.jet.math.FloatFunctions}, using idioms like <table>
 * <td class="PRE">
 * 
 * <pre>
 * cern.jet.math.Functions F = cern.jet.math.Functions.functions; // alias
 * matrix.assign(F.square);
 * matrix.assign(F.sqrt);
 * matrix.assign(F.sin);
 * matrix.assign(F.log);
 * matrix.assign(F.log(b));
 * matrix.assign(otherMatrix, F.min);
 * matrix.assign(otherMatrix, F.max);
 * </pre>
 * 
 * </td>
 * </table> Here are some <a href="../doc-files/functionObjects.html">other
 * examples</a>.
 * <p>
 * Implementation: Performance optimized for medium to very large matrices. In
 * fact, there is now nomore a performance advantage in using this class; The
 * assign (transform) methods directly defined on matrices are now just as fast.
 * Thus, this class will soon be removed altogether.
 * 
 * @deprecated
 * @author wolfgang.hoschek@cern.ch
 * @version 1.0, 09/24/99
 */
public class Transform extends cern.colt.PersistentObject {
	/**
	 * Little trick to allow for "aliasing", that is, renaming this class.
	 * Normally you would write
	 * 
	 * <pre>
	 * Transform.mult(myMatrix, 2);
	 * Transform.plus(myMatrix, 5);
	 * </pre>
	 * 
	 * Since this class has only static methods, but no instance methods you can
	 * also shorten the name "FloatTransform" to a name that better suits you,
	 * for example "Trans".
	 * 
	 * <pre>
	 * Transform T = Transform.transform; // kind of &quot;alias&quot;
	 * T.mult(myMatrix, 2);
	 * T.plus(myMatrix, 5);
	 * </pre>
	 */
	public static final Transform transform = new Transform();

	private static final cern.jet.math.FloatFunctions F = cern.jet.math.FloatFunctions.functions; // alias

	/**
	 * Makes this class non instantiable, but still let's others inherit from
	 * it.
	 */
	protected Transform() {
	}

	/**
	 * <tt>A[i] = Math.abs(A[i])</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D abs(FloatMatrix1D A) {
		return A.assign(F.abs);
	}

	/**
	 * <tt>A[row,col] = Math.abs(A[row,col])</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D abs(FloatMatrix2D A) {
		return A.assign(F.abs);
	}

	/**
	 * <tt>A = A / s <=> A[i] = A[i] / s</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D div(FloatMatrix1D A, float s) {
		return A.assign(F.div(s));
	}

	/**
	 * <tt>A = A / B <=> A[i] = A[i] / B[i]</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D div(FloatMatrix1D A, FloatMatrix1D B) {
		return A.assign(B, F.div);
	}

	/**
	 * <tt>A = A / s <=> A[row,col] = A[row,col] / s</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D div(FloatMatrix2D A, float s) {
		return A.assign(F.div(s));
	}

	/**
	 * <tt>A = A / B <=> A[row,col] = A[row,col] / B[row,col]</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D div(FloatMatrix2D A, FloatMatrix2D B) {
		return A.assign(B, F.div);
	}

	/**
	 * <tt>A[row,col] = A[row,col] == s ? 1 : 0</tt>; ignores tolerance.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D equals(FloatMatrix2D A, float s) {
		return A.assign(F.equals(s));
	}

	/**
	 * <tt>A[row,col] = A[row,col] == B[row,col] ? 1 : 0</tt>; ignores
	 * tolerance.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D equals(FloatMatrix2D A, FloatMatrix2D B) {
		return A.assign(B, F.equals);
	}

	/**
	 * <tt>A[row,col] = A[row,col] > s ? 1 : 0</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D greater(FloatMatrix2D A, float s) {
		return A.assign(F.greater(s));
	}

	/**
	 * <tt>A[row,col] = A[row,col] > B[row,col] ? 1 : 0</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D greater(FloatMatrix2D A, FloatMatrix2D B) {
		return A.assign(B, F.greater);
	}

	/**
	 * <tt>A[row,col] = A[row,col] < s ? 1 : 0</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D less(FloatMatrix2D A, float s) {
		return A.assign(F.less(s));
	}

	/**
	 * <tt>A[row,col] = A[row,col] < B[row,col] ? 1 : 0</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D less(FloatMatrix2D A, FloatMatrix2D B) {
		return A.assign(B, F.less);
	}

	/**
	 * <tt>A = A - s <=> A[i] = A[i] - s</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D minus(FloatMatrix1D A, float s) {
		return A.assign(F.minus(s));
	}

	/**
	 * <tt>A = A - B <=> A[i] = A[i] - B[i]</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D minus(FloatMatrix1D A, FloatMatrix1D B) {
		return A.assign(B, F.minus);
	}

	/**
	 * <tt>A = A - s <=> A[row,col] = A[row,col] - s</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D minus(FloatMatrix2D A, float s) {
		return A.assign(F.minus(s));
	}

	/**
	 * <tt>A = A - B <=> A[row,col] = A[row,col] - B[row,col]</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D minus(FloatMatrix2D A, FloatMatrix2D B) {
		return A.assign(B, F.minus);
	}

	/**
	 * <tt>A = A - B*s <=> A[i] = A[i] - B[i]*s</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D minusMult(FloatMatrix1D A, FloatMatrix1D B, float s) {
		return A.assign(B, F.minusMult(s));
	}

	/**
	 * <tt>A = A - B*s <=> A[row,col] = A[row,col] - B[row,col]*s</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D minusMult(FloatMatrix2D A, FloatMatrix2D B, float s) {
		return A.assign(B, F.minusMult(s));
	}

	/**
	 * <tt>A = A * s <=> A[i] = A[i] * s</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D mult(FloatMatrix1D A, float s) {
		return A.assign(F.mult(s));
	}

	/**
	 * <tt>A = A * B <=> A[i] = A[i] * B[i]</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D mult(FloatMatrix1D A, FloatMatrix1D B) {
		return A.assign(B, F.mult);
	}

	/**
	 * <tt>A = A * s <=> A[row,col] = A[row,col] * s</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D mult(FloatMatrix2D A, float s) {
		return A.assign(F.mult(s));
	}

	/**
	 * <tt>A = A * B <=> A[row,col] = A[row,col] * B[row,col]</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D mult(FloatMatrix2D A, FloatMatrix2D B) {
		return A.assign(B, F.mult);
	}

	/**
	 * <tt>A = -A <=> A[i] = -A[i]</tt> for all cells.
	 * 
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D negate(FloatMatrix1D A) {
		return A.assign(F.mult(-1));
	}

	/**
	 * <tt>A = -A <=> A[row,col] = -A[row,col]</tt>.
	 * 
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D negate(FloatMatrix2D A) {
		return A.assign(F.mult(-1));
	}

	/**
	 * <tt>A = A + s <=> A[i] = A[i] + s</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D plus(FloatMatrix1D A, float s) {
		return A.assign(F.plus(s));
	}

	/**
	 * <tt>A = A + B <=> A[i] = A[i] + B[i]</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D plus(FloatMatrix1D A, FloatMatrix1D B) {
		return A.assign(B, F.plus);
	}

	/**
	 * <tt>A = A + s <=> A[row,col] = A[row,col] + s</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D plus(FloatMatrix2D A, float s) {
		return A.assign(F.plus(s));
	}

	/**
	 * <tt>A = A + B <=> A[row,col] = A[row,col] + B[row,col]</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D plus(FloatMatrix2D A, FloatMatrix2D B) {
		return A.assign(B, F.plus);
	}

	/**
	 * <tt>A = A + B*s<=> A[i] = A[i] + B[i]*s</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D plusMult(FloatMatrix1D A, FloatMatrix1D B, float s) {
		return A.assign(B, F.plusMult(s));
	}

	/**
	 * <tt>A = A + B*s <=> A[row,col] = A[row,col] + B[row,col]*s</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D plusMult(FloatMatrix2D A, FloatMatrix2D B, float s) {
		return A.assign(B, F.plusMult(s));
	}

	/**
	 * <tt>A = A<sup>s</sup> <=> A[i] = Math.pow(A[i], s)</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D pow(FloatMatrix1D A, float s) {
		return A.assign(F.pow(s));
	}

	/**
	 * <tt>A = A<sup>B</sup> <=> A[i] = Math.pow(A[i], B[i])</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix1D pow(FloatMatrix1D A, FloatMatrix1D B) {
		return A.assign(B, F.pow);
	}

	/**
	 * <tt>A = A<sup>s</sup> <=> A[row,col] = Math.pow(A[row,col], s)</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param s
	 *            the scalar; can have any value.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D pow(FloatMatrix2D A, float s) {
		return A.assign(F.pow(s));
	}

	/**
	 * <tt>A = A<sup>B</sup> <=> A[row,col] = Math.pow(A[row,col], B[row,col])</tt>.
	 * 
	 * @param A
	 *            the matrix to modify.
	 * @param B
	 *            the matrix to stay unaffected.
	 * @return <tt>A</tt> (for convenience only).
	 */
	public static FloatMatrix2D pow(FloatMatrix2D A, FloatMatrix2D B) {
		return A.assign(B, F.pow);
	}
}
