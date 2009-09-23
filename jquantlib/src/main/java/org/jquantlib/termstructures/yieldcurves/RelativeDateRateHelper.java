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

package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.Settings;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.termstructures.TermStructure;
import org.jquantlib.time.Date;
import org.jquantlib.util.Observable;

/**
 * Rate helper with date schedule relative to the global evaluation date
 *
 * <p>
 * This class takes care of rebuilding the date schedule when the global
 * evaluation date changes
 *
 * @author Srinivas Hasti
 */
// TODO: code review :: please verify against QL/C++ code
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public abstract class RelativeDateRateHelper<T extends TermStructure> extends RateHelper<T> {

    //
    // protected fields
    //

    protected Date evaluationDate;


    //
    // protected constructors
    //

    protected RelativeDateRateHelper() {
        super();

        // TODO: code review :: please verify against QL/C++ code
        this.evaluationDate = new Settings().getEvaluationDate();
        this.evaluationDate.addObserver(this);
        // XXX:registerWith
        //registerWith(this.evaluationDate);
    }


    //
    // public constructors
    //

    public RelativeDateRateHelper(/*@Price*/ final double d) {
        super(d);
        this.evaluationDate = new Settings().getEvaluationDate();
        this.evaluationDate.addObserver(this);
        // XXX:registerWith
        //registerWith(this.evaluationDate);
    }

    public RelativeDateRateHelper(final Handle<Quote> quote) {
        super(quote);
        this.evaluationDate = new Settings().getEvaluationDate();
        this.evaluationDate.addObserver(this);
        // XXX:registerWith
        //registerWith(this.evaluationDate);
    }


    public RelativeDateRateHelper(final Handle<Quote> quote, final T termStructure, final Date earliestDate, final Date latestDate) {
        super(quote, termStructure, earliestDate, latestDate);
        this.evaluationDate = new Settings().getEvaluationDate();
        this.evaluationDate.addObserver(this);
        // XXX:registerWith
        //registerWith(this.evaluationDate);
    }


    //
    // protected abstract methods
    //

    protected abstract void initializeDates();


    //
    // overrides RateHelper
    //

    @Override
    public void update(final Observable o, final Object arg) {
        final Date newEvaluationDate = new Settings().getEvaluationDate();
        if (!evaluationDate.equals(newEvaluationDate)) {
            evaluationDate = newEvaluationDate;
            initializeDates();
        }
        super.update(o, arg);
    }

}
