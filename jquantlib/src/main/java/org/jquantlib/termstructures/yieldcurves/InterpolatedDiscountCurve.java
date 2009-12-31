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

package org.jquantlib.termstructures.yieldcurves;

import java.util.ArrayList;
import java.util.List;
import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.termstructures.AbstractYieldTermStructure;
import org.jquantlib.termstructures.Bootstrapable;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;
import org.jquantlib.util.Observer;
import org.jquantlib.util.Pair;

public class InterpolatedDiscountCurve extends AbstractYieldTermStructure implements Bootstrapable
{

    private Date [] dates;

    private Array data;
    
    private Array time;

    private Interpolation interpolation;
    
    private Interpolator interpolator;


    public InterpolatedDiscountCurve (Date [] dates,
                                      Array discounts,
                                      DayCounter dc,
                                      Calendar calendar,
                                      Interpolator interpolator)
    {
        super (dates[0], calendar, dc);
        QL.require (dates.length != 0, " Dates cannot be empty");
        QL.require (discounts.size() != 0, "Discounts cannot be empty");
        QL.require (dates.length == data.size(), "Dates must be the same size as Discounts");
        QL.require (data.get (0) == 1.0, "Initial discount factor must be 1.0");
        

        this.dates = dates; 
        this.data = discounts;
        this.interpolator = interpolator;
        this.time = new Array (dates.length);
        time.set (0, 0.0);

        for (int i = 1; i < dates.length; ++ i)
        {
            QL.require (dates [i].gt (dates[i - 1]), "Dates must be in ascending order");
            QL.require (data.get(i) > 0, "Negative Discount");
            time.set (i, dc.yearFraction (dates[0], dates[i]));
            //FIXME
            //QL_REQUIRE(!close(times_[i],times_[i-1]),
            //"two dates correspond to the same time "
            //           "under this curve's day count convention");
        }

        interpolation = interpolator.interpolate (time.size(), time.constIterator(), data.constIterator ());
        interpolation.update();
    }

    public InterpolatedDiscountCurve (DayCounter dc, Interpolator interpolator)
    {
        super (dc);
        this.interpolator = interpolator;
    }
    
    //FIXME
    // we need a way to tell the data sets an appropriate size, therfore we have created this constructor
    public InterpolatedDiscountCurve (int instruments, Date referenceDate, DayCounter dc, Interpolator interpolator)
    {
        this (referenceDate, dc, interpolator);
        dates = new Date [instruments];
        data = new Array (instruments);
        time = new Array (instruments);
        interpolation = interpolator.interpolate (time.size(), time.constIterator(), data.constIterator ());
        interpolation.update();
    }
    public InterpolatedDiscountCurve (Date referenceDate, DayCounter dc, Interpolator interpolator)
    {
        // FIXME default calendar
        super (referenceDate, new Calendar(), dc);
        this.interpolator = interpolator;
    }

    public InterpolatedDiscountCurve (int settlementDays, Calendar calendar, 
                                      DayCounter dc, Interpolator interpolator)
    {
        super (settlementDays, calendar, dc);
        this.interpolator = interpolator;
    }


    public Array getTimes()
    {
        return time;
    }

    public Array getData ()
    {
        return data;
    }

    public Date [] getDates()
    {
        return dates;
    }

    public List <Pair <Date, Double> > nodes ()
    {
        List <Pair <Date, Double> > nodes = new ArrayList < Pair <Date, Double> > ();
        for (int i = 0; i < dates.length; ++ i)
        {
            nodes.add (new Pair<Date, Double> (dates[i], data.get(i)));
        }
        return nodes;
    }

    @Override
    protected double discountImpl (double t)
    {
        return interpolation.evaluate (t, true);
    }

    @Override
    public Date maxDate ()
    {
        return dates[dates.length - 1];
    }

    @Override
    public Interpolation getInterpolation ()
    {
        return interpolation;
    }

    @Override
    public void resetData (int size)
    {
        data = new Array (size);
    }

    @Override
    public void resetDates (int size)
    {
        dates = new Date [size];
    }

    @Override
    public void resetTime (int size)
    {
        time = new Array (size);
    }

    @Override
    public Interpolator getInterpolator ()
    {
        return interpolator;
    }

    @Override
    public void setInterpolation (Interpolation interpolation)
    {
        this.interpolation = interpolation;        
    }
}