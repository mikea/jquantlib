/*
Copyright (C) 2008 Praneet Tiwari

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
package org.jquantlib.instruments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jquantlib.Configuration;
import org.jquantlib.cashflow.CashFlow;
import org.jquantlib.cashflow.CashFlows;
import org.jquantlib.cashflow.Leg;
import org.jquantlib.math.Constants;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.arguments.SwapArguments;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.pricingengines.results.SwapResults;
import org.jquantlib.util.Date;
import org.jquantlib.util.stdlibc.Std;

/**
 * Interest rate swap
 * <p>
 * The cash flows belonging to the first leg are paid; the ones belonging to the second leg are received.
 *
 * @category instruments
 *
 * @author Richard Gomes
 */
// FIXME: use arrays instead of lists
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class Swap extends NewInstrument {

    protected List<Leg> legs;
    protected double[] payer;
    protected double[] legNPV;
    protected double[] legBPS;


    //
    // public constructors
    //

    public Swap(final Leg firstLeg, final Leg secondLeg) {
        this.legs = new ArrayList<Leg>();
        this.payer = new double[2];
        this.legNPV = new double[2];
        this.legBPS = new double[2];
        legs.add(firstLeg);
        legs.add(secondLeg);
        payer[0] = -1.0;
        payer[1] = +1.0;

        for (int i = 0; i < legs.size(); i++)
            for (final CashFlow item : legs.get(i))
                item.addObserver(this);
    }

    public Swap(final List<Leg> legs, final boolean[] payer) {
        this.legs = legs;
        this.payer = new double[legs.size()];
        Arrays.fill(this.payer, 1.0);
        this.legNPV = new double[legs.size()];
        this.legBPS = new double[legs.size()];

        for (int j = 0; j < this.legs.size(); j++) {
            if (payer[j])
                this.payer[j] = -1.0;
            for (int i = 0; i < legs.size(); i++)
                for (final CashFlow item : legs.get(i))
                    item.addObserver(this);
        }
    }


    //
    // protected constructors
    //

    protected Swap(final int legs) {
        this.legs   = new ArrayList<Leg>();
        this.payer  = new double[legs];
        this.legNPV = new double[legs];
        this.legBPS = new double[legs];
    }


    //
    // public methods
    //

    public Date startDate() /* @ReadOnly */{
        assert legs.size() > 0 : "no legs given";
        Date d = CashFlows.getInstance().startDate(this.legs.get(0));
        for (int j = 1; j < this.legs.size(); j++)
            d = Std.getInstance().min(d, CashFlows.getInstance().startDate(this.legs.get(j)));
        return d;
    }

    public Date maturityDate() /* @ReadOnly */{
        assert legs.size() > 0 : "no legs given";
        Date d = CashFlows.getInstance().maturityDate(this.legs.get(0));
        for (int j = 1; j < this.legs.size(); j++)
            d = Std.getInstance().max(d, CashFlows.getInstance().maturityDate(this.legs.get(j)));
        return d;
    }


    //
    // overrides Instrument
    //

    @Override
    public boolean isExpired() /* @ReadOnly */{
        final Date today = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();
        for (int i = 0; i < legs.size(); i++)
            for (final CashFlow item : legs.get(i))
                if (!item.hasOccurred(today))
                    return false;
        return true;
    }


    //
    // overrides NewInstrument
    //

    @Override
    protected void setupExpired() /* @ReadOnly */{
        super.setupExpired();
        Arrays.fill(legBPS, 0.0);
        Arrays.fill(legNPV, 0.0);
    }

    @Override
    public void setupArguments(final Arguments args) /* @ReadOnly */{
        final SwapArguments arguments = (SwapArguments) args;

        arguments.legs = legs;
        arguments.payer = payer;
    }

    @Override
    public void fetchResults(final Results r) /* @ReadOnly */{
        super.fetchResults(r);

        final SwapResults results = (SwapResults) r;

        if (results.legNPV.length > 0) {
            assert results.legNPV.length == legNPV.length : "wrong number of leg NPV returned";
            legNPV = results.legNPV;
        } else
            Arrays.fill(legNPV, Constants.NULL_REAL);

        if (results.legBPS.length > 0) {
            assert results.legBPS.length == legBPS.length : "wrong number of leg BPS returned";
            legBPS = results.legBPS;
        } else
            Arrays.fill(legBPS, Constants.NULL_REAL);
    }

}
