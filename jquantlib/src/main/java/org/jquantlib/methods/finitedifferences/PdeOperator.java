package org.jquantlib.methods.finitedifferences;

import org.jquantlib.math.TransformedGrid;

public class PdeOperator<T> extends TridiagonalOperator {

	public PdeOperator(TransformedGrid grid, T process,
			double residualTime) {
		super(grid.size());
		timeSetter = new GenericTimeSetter<T>(grid, process);
		setTime(residualTime);
	}

}
