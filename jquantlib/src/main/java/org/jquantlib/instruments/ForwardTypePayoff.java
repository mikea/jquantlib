/*
 * Copyright (C) 2008 John Martin This source code is release under the BSD License. This file is
 * part of JQuantLib, a free-software/open-source library for financial quantitative analysts and
 * developers - http://jquantlib.org/ JQuantLib is free software: you can redistribute it and/or
 * modify it under the terms of the JQuantLib license. You should have received a copy of the
 * license along with this program; if not, please email <jquant-devel@lists.sourceforge.net>. The
 * license is also available online at <http://www.jquantlib.org/index.php/LICENSE.TXT>. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license for
 * more details. JQuantLib is based on QuantLib. http://quantlib.org/ When applicable, the original
 * copyright notice follows this notice.
 */

package org.jquantlib.instruments;

import org.jquantlib.QL;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.instruments.PositionType;

// import org.jquantlib.lang.exceptions.LibraryException;

/**
 * Intermediate class for forward type payoffs
 */
public class ForwardTypePayoff extends Payoff
{

    //
    // protected fields
    //

    /**
     * This field represents the {@link Forward}'s strike price
     */
    protected/* @Price */double strike;

    protected PositionType type;

    //
    // public constructors
    //

    /**
     * Constructs a typed {@link Payoff} with a fixed strike price
     * 
     * @param type
     *            is an {@link ForwardType}
     * @param strike
     *            is the strike price
     */
    public ForwardTypePayoff (PositionType positionType, final/* @Price */double strike)
    {
        super ();
        QL.require (strike >= 0.0, "negative strike given");
        this.strike = strike;
        this.type = positionType;
    }

    //
    // public final methods
    //

    /**
     * @return the strike value
     */
    public final/* @Strike */double strike ()
    {
        return strike;
    }

    //
    // overrides Payoff
    //
    @Override
    public String description () /* @ReadOnly */
    {
        StringBuilder sb = new StringBuilder();
        sb.append (name());
        // Review, added this because it will be more helpful for debugging purposes. 
        sb.append (this.type.toString());
        sb.append (" ");
        sb.append (this.strike);
        return sb.toString();
    }

    public final double get (final double price)
    {
        if (type == PositionType.LONG)
        {
            return price - strike;
        }
        else if (type == PositionType.SHORT)
        {
            return strike - price;
        }
        else 
        { 
            throw new LibraryException (" Unknown Forward Type "); 
        }
    }

    @Override
    public String name ()
    {
        return "Forward";
    }
}
