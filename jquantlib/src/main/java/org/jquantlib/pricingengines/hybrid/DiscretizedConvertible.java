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
package org.jquantlib.pricingengines.hybrid;

import java.util.List;

import org.jquantlib.assets.DiscretizedAsset;
import org.jquantlib.instruments.ConvertibleBondOption;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.time.TimeGrid;

//TODO: complete this class
public class DiscretizedConvertible extends DiscretizedAsset {

    //
    // protected fields
    //

    protected Array conversionProbability_;
    protected Array spreadAdjustedRate_;
    protected Array dividendValues_;


    //
    // private fields
    //

    private GeneralizedBlackScholesProcess process_;
    private ConvertibleBondOption.ArgumentsImpl arguments_;
    private List< /* @Time */ Double > stoppingTimes_;
    private List< /* @Time */ Double > callabilityTimes_;
    private List< /* @Time */ Double > couponTimes_;
    private List< /* @Time */ Double > dividendTimes_;


    //
    // public methods
    //

    public DiscretizedConvertible(
            final ConvertibleBondOption.Arguments args,
            final GeneralizedBlackScholesProcess process,
            final TimeGrid grid) {
        throw new UnsupportedOperationException();
    }

    /**
     * <p>This method should initialize the asset values to an {@link Array} of the given size and with values depending on the particular asset. </p>
     */
    @Override
    public void reset(final int size) {
        throw new UnsupportedOperationException();
    }

    public final Array conversionProbability() /* @ReadOnly */ {
        throw new UnsupportedOperationException();
    }

    public final Array  spreadAdjustedRate() /* @ReadOnly */ {
        throw new UnsupportedOperationException();
    }

    public final Array  dividendValues() /* @ReadOnly */ {
        throw new UnsupportedOperationException();
    }


    // TODO: code review :: verify how these set* methods are defined in QuantLib/C++

    public final void setConversionProbability(final Array array) {
        throw new UnsupportedOperationException();
    }

    public final void setSpreadAdjustedRate(final Array array) {
        throw new UnsupportedOperationException();
    }

    public final void setDividendValues(final Array array) {
        throw new UnsupportedOperationException();
    }



    /**
     * <p>This method returns the times at which the numerical method should stop while rolling back the asset. Typical examples include payment times, exercise times and such.</p>
     * @note <p>The returned values are not guaranteed to be sorted. </p>
     */
    @Override
    public List< /* @Time */ Double > mandatoryTimes() /* @ReadOnly */ {
        throw new UnsupportedOperationException();
    }



    //
    // protected methods
    //

    /**
     * <p>This method performs the actual post-adjustment </p>
     */
    @Override
    protected void postAdjustValuesImpl() {
        throw new UnsupportedOperationException();
    }



    //
    // private methods
    //

    private Array adjustedGrid() /* @ReadOnly */ {
        throw new UnsupportedOperationException();
    }

    private void applyConvertibility() {
        throw new UnsupportedOperationException();
    }

    private void applyCallability(final int i, final boolean convertible) {
        throw new UnsupportedOperationException();
    }

    private void addCoupon(final int i) {
        throw new UnsupportedOperationException();
    }

}
