package org.jquantlib.methods.finitedifferences;

import org.jquantlib.math.TransformedGrid;

public class PdeOperator<T> extends TridiagonalOperator {

	public PdeOperator(TransformedGrid grid, PdeSecondOrderParabolic process,
			double residualTime) {
		super(grid.size());
		timeSetter = new GenericTimeSetter(grid, process);
		setTime(residualTime);
	}

}
