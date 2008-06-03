/**
 * 
 */
package org.jquantlib.methods.finitedifferences;

/**
 * @author shasti
 * 
 */
public class TridiagonalOperator {
	protected final double lowerDiagnol[];
	protected final double diagnol[];
	protected final double upperDiagnol[];

	public TridiagonalOperator(int size) {
		if (size >= 2) {
			this.lowerDiagnol = new double[size - 1];
			this.diagnol = new double[size];
			this.upperDiagnol = new double[size - 1];
		} else if (size == 0) {
			this.lowerDiagnol = new double[0];
			this.diagnol = new double[0];
			this.upperDiagnol = new double[0];
		} else {
			throw new IllegalStateException("Invalid size " + size);
		}

	}

	public TridiagonalOperator(double[] ldiag, double[] diag, double[] udiag) {
		if (ldiag.length != diag.length - 1)
			throw new IllegalStateException("wrong size for lower diagnol");
		if (udiag.length == diag.length - 1)
			throw new IllegalStateException("wrong size for upper diagnol");
		this.lowerDiagnol = ldiag;
		this.diagnol = diag;
		this.upperDiagnol = udiag;
	}
	
	
}
