/*
 Copyright (C) 2008 Srinivas Hasti

 This source code is release under the BSD License.

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */
package org.jquantlib.assets;

import java.util.List;

import org.jquantlib.exercise.Exercise;
import org.jquantlib.math.Array;

/**
 * //! Discretized option on a given asset /*! \warning it is advised that
 * derived classes take care of creating and initializing themselves an instance
 * of the underlying.
 *
 * @author Srinivas Hasti
 *
 */
// TODO: complete mandatoryTimes
public class DiscretizedOption extends DiscretizedAsset {

	protected Exercise.Type exerciseType;
	protected List<Double> exerciseTimes;
	protected DiscretizedAsset underlying;

	public DiscretizedOption(final DiscretizedAsset underlying,
			final Exercise.Type exerciseType, final List<Double> exerciseTimes) {
		this.underlying = underlying;
		this.exerciseType = exerciseType;
		this.exerciseTimes = exerciseTimes;
	}

	@Override
    public void reset(final int size) {
	    assert method() == underlying.method() : "option and underlying were initialized on different methods";
		values = new Array(size);
		adjustValues();
	}

	@Override
    public List</* Time */Double> mandatoryTimes() {
		final List</* Time */Double> times = underlying.mandatoryTimes();
		// discard negative times...
		/** TODO: ** */
		/*
		 * List<Double>::const_iterator i =
		 * std::find_if(exerciseTimes_.begin(),exerciseTimes_.end(),
		 * std::bind2nd(std::greater_equal<Time>(),0.0)); // and add the
		 * positive ones times.insert(times.end(), i, exerciseTimes_.end());
		 */
		return times;
	}

	protected void applyExerciseCondition() {
		for (int i = 0; i < values.length; i++)
			values.set(i, Math.max(underlying.values().get(i), values.get(i)));
	}

	@Override
    public void postAdjustValuesImpl() {
		/*
		 * In the real world, with time flowing forward, first any payment is
		 * settled and only after options can be exercised. Here, with time
		 * flowing backward, options must be exercised before performing the
		 * adjustment.
		 */
		underlying.partialRollback(time());
		underlying.preAdjustValues();
		int i;
		switch (exerciseType) {
		case AMERICAN:
			if (time >= exerciseTimes.get(0) && time <= exerciseTimes.get(1))
				applyExerciseCondition();
			break;
		case BERMUDAN:
		case EUROPEAN:
			for (i = 0; i < exerciseTimes.size(); i++) {
				/* Time */final double t = exerciseTimes.get(i);
				if (t >= 0.0 && isOnTime(t))
					applyExerciseCondition();
			}
			break;
		default:
			assert false : "invalid exercise type";
		}
		underlying.postAdjustValues();
	}

}
