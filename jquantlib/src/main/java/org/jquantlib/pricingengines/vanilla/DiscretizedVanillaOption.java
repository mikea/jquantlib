/*
 Copyright (C) 2007 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */
package org.jquantlib.pricingengines.vanilla;

import java.util.List;

import org.joda.primitives.list.impl.ArrayDoubleList;
import org.jquantlib.assets.DiscretizedAsset;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.pricingengines.arguments.OneAssetOptionArguments;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.time.TimeGrid;

/**
 * @author Srinivas Hasti
 *
 */
public class DiscretizedVanillaOption extends DiscretizedAsset {

    // private VanillaOption.Arguments arguments_;
    private final OneAssetOptionArguments arguments;
    private final List<Double> stoppingTimes;

    public DiscretizedVanillaOption(final OneAssetOptionArguments args, final StochasticProcess process, final TimeGrid grid) {
        this.arguments = args;
        final int size = args.exercise.size();
        this.stoppingTimes = new ArrayDoubleList();
        for (int i = 0; i < size; ++i) {
            stoppingTimes.add(i, process.time(args.exercise.date(i)));
            if (!grid.empty()) {
                // adjust to the given grid
                stoppingTimes.add(i, grid.closestTime(stoppingTimes.get(i)));
            }
        }
    }

    @Override
    public void reset(final int size) {
        values = new Array(size);
        adjustValues();
    }

    @Override
    protected void postAdjustValuesImpl() {

        final double now = time();
        switch (arguments.exercise.type()) {
            case American:
                if (now <= stoppingTimes.get(1) && now >= stoppingTimes.get(0)) {
                    applySpecificCondition();
                }
                break;
            case European:
                if (isOnTime(stoppingTimes.get(0))) {
                    applySpecificCondition();
                }
                break;
            case Bermudan:
                for (int i=0; i<stoppingTimes.size(); i++) {
                    if (isOnTime(stoppingTimes.get(i))) {
                        applySpecificCondition();
                    }
                }
                break;
            default:
                throw new LibraryException("invalid option type"); // QA:[RG]::verified
        }
    }

    private void applySpecificCondition() {
        final Array grid = method().grid(time());
        for (int j=0; j<values.size(); j++) {
            values.set(j, Math.max(values.get(j), arguments.payoff.valueOf(grid.get(j))));
        }
    }



    /* (non-Javadoc)
     * @see org.jquantlib.assets.DiscretizedAsset#mandatoryTimes()
     */
    @Override
    public List<Double> mandatoryTimes() {
        return stoppingTimes;
    }
}
