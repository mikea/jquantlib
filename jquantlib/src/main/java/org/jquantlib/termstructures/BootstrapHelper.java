
/*
Copyright (C) 2009 John Martin

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

package org.jquantlib.termstructures;

import java.util.Comparator;
import java.util.List;
import org.jquantlib.QL;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.time.Date;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;
import org.jquantlib.util.Visitor;

// ! Base helper class for bootstrapping
/*
 * ! This class provides an abstraction for the instruments used to bootstrap a term structure. It
 * is advised that a bootstrap helper for an instrument contains an instance of the actual
 * instrument class to ensure consistancy between the algorithms used during bootstrapping and later
 * instrument pricing. This is not yet fully enforced in the available rate helpers.
 */

public abstract class BootstrapHelper <Curve extends TermStructure> implements Observer, Observable
{
    protected Handle <Quote> quote;

    protected Curve termStructure;

    protected Date earliestDate;

    protected Date latestDate;

    public BootstrapHelper (Handle <Quote> quote)
    {
        this.quote = quote;
        this.quote.addObserver (this);
    }

    public BootstrapHelper (double quote)
    {
        this.quote = new Handle <Quote> (new SimpleQuote (quote));
    }

    public abstract double impliedQuote ();

    public double quoteError ()
    {
        return quote.currentLink ().value () - impliedQuote ();
    }

    public boolean quoteIsValid ()
    {
        return quote.currentLink ().isValid ();
    }

    public void setTermStructure (Curve c)
    {
        QL.ensure (c != null, "TermStructure cannot be null");
        this.termStructure = c;
    }

    public Date earliestDate ()
    {
        return this.earliestDate;
    }

    public Date latestDate ()
    {
        return this.latestDate;
    }

    public void update ()
    {
        this.notifyObservers ();
    }

    // FIXME
    public void accept (Visitor <BootstrapHelper <Curve>> v)
    {
        if (v != null)
        {
            v.visit (this);
        }
        QL.error ("not a bootstrap helper visitor");
    }

    //
    // implements Observable
    //

    /**
     * Implements multiple inheritance via delegate pattern to an inner class
     * 
     * @see Observable
     */
    private final Observable delegatedObservable = new DefaultObservable (this);

    @Override
    public final void addObserver (final Observer observer)
    {
        delegatedObservable.addObserver (observer);
    }

    @Override
    public final int countObservers ()
    {
        return delegatedObservable.countObservers ();
    }

    @Override
    public final void deleteObserver (final Observer observer)
    {
        delegatedObservable.deleteObserver (observer);
    }

    @Override
    public final void notifyObservers ()
    {
        delegatedObservable.notifyObservers ();
    }

    @Override
    public final void notifyObservers (final Object arg)
    {
        delegatedObservable.notifyObservers (arg);
    }

    @Override
    public final void deleteObservers ()
    {
        delegatedObservable.deleteObservers ();
    }

    @Override
    public final List <Observer> getObservers ()
    {
        return delegatedObservable.getObservers ();
    }

}