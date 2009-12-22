/*
 * Copyright (C) 2008 Richard Gomes This source code is release under the BSD License. This file is
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
import org.jquantlib.Settings;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.instruments.Instrument;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;

/**
 * Abstract base class for Forwards
 * 
 * @author John Martin
 */
public abstract class Forward extends Instrument
{

    public static final String UNKNOWN_FORWARD_TYPE = "Unknown Forward Type";

    //! Abstract base forward class
    /*! Derived classes must implement the virtual functions
        spotValue() (NPV or spot price) and spotIncome() associated
        with the specific relevant underlying (e.g. bond, stock,
        commodity, loan/deposit). These functions must be used to set the
        protected member variables underlyingSpotValue_ and
        underlyingIncome_ within performCalculations() in the derived
        class before the base-class implementation is called.

        spotIncome() refers generically to the present value of
        coupons, dividends or storage costs.

        discountCurve_ is the curve used to discount forward contract
        cash flows back to the evaluation day, as well as to obtain
        forward values for spot values/prices.

        incomeDiscountCurve_, which for generality is not
        automatically set to the discountCurve_, is the curve used to
        discount future income/dividends/storage-costs etc back to the
        evaluation date.

        \todo Add preconditions and tests

        \warning This class still needs to be rigorously tested

        \ingroup instruments
    */

    /*
     * Forward Enum Type
     */
    public static enum ForwardType
    {
        FORWARDRATEAGREEMENT (1), 
        FIXEDRATEBONDFORWARD (2), 
        FORWARDVANILLAOPTION (3);
        // TODO other forward types

        private int value;

        private ForwardType (final int type)
        {
            this.value = type;
        }

        private final String UNKNOWN_FORWARD_TYPE = "unknown forward type";

        /**
         * This method returns the <i>mathematical signal</i> associated to an forward type.
         * 
         * @return 1 for FRA, 2 for FRBF, 3 for FVO
         */

        public int toInteger ()
        {
            return value;
        }

        @Override
        public String toString ()
        {
            if (value == 1)
            {
                return "ForwardRateAgreement";
            }
            else if (value == 2)
            {
                return "FixedRateBondForward";
            }
            else if (value == 3)
            {
                return "ForwardVanillaOption";
            }
            else
            {
                throw new LibraryException (UNKNOWN_FORWARD_TYPE);
            }
        }
    }

    //
    // protected member variables
    //

    // settlementDate
    protected int settlementDays;

    // adjusted maturityDate
    protected Date maturityDate;

    protected Date valueDate;

    protected Calendar calendar;

    protected DayCounter dayCounter;

    protected BusinessDayConvention businessDayConvention;

    protected Handle <YieldTermStructure> discountCurve;

    protected Handle <YieldTermStructure> incomeDiscountCurve;

    protected ForwardTypePayoff payoff;

    protected double underlyingSpotValue;

    protected double underlyingIncome;

    //
    // protected forward constructor, only descendant classes can instantiate
    //

    protected Forward (DayCounter dc, Calendar cal, BusinessDayConvention bdc,
                       int /* @Natural */settlementDays, Payoff payoff, Date valueDate, Date maturityDate)
    {
        this (dc, cal, bdc, settlementDays, payoff, valueDate, 
              maturityDate, new Handle <YieldTermStructure>());
    }

    protected Forward (DayCounter dc, Calendar cal, BusinessDayConvention bdc,
            int /* @Natural */settlementDays, Payoff payoff, Date valueDate, Date maturityDate,
            Handle <YieldTermStructure> discountCurve)
    {
        this.dayCounter = dc;
        this.calendar = cal;
        this.businessDayConvention = bdc;
        this.settlementDays = settlementDays;
        this.maturityDate = calendar.adjust (maturityDate, businessDayConvention);
        this.discountCurve = discountCurve;
        this.payoff = (ForwardTypePayoff) payoff;
        this.valueDate = valueDate;

        //registerWith(Settings::instance().evaluationDate());
        //registerWith(discountCurve_);

        new Settings().evaluationDate().addObserver (this);
        discountCurve.addObserver (this);
    }

    //
    // abstract member functions derived classes must implement
    //

    public abstract double spotValue ();

    public abstract double spotIncome (Handle <YieldTermStructure> incomeDiscountCurve);

    //
    // public member functions
    //

    public double forwardValue ()
    {
        calculate ();

        return (underlyingSpotValue - underlyingIncome)
                / (discountCurve.currentLink ().discount (maturityDate));
    }

    public void performCalculations ()
    {
        QL.require (discountCurve != null, " Discount Curve must be set for Forward");
        NPV = payoff.get (forwardValue ()) * discountCurve.currentLink ().discount (maturityDate);
    }

        /*! Simple yield calculation based on underlying spot and
            forward values, taking into account underlying income.
            When \f$ t>0 \f$, call with:
            underlyingSpotValue=spotValue(t),
            forwardValue=strikePrice, to get current yield. For a
            repo, if \f$ t=0 \f$, impliedYield should reproduce the
            spot repo rate. For FRA's, this should reproduce the
            relevant zero rate at the FRA's maturityDate_;
        */
    
    public InterestRate impliedYield (double underlyingSpotValue, double forwardValue,
            Date settlementDate, Compounding compoundingConvention, DayCounter dayCounter)
    {
        double tenor = dayCounter.yearFraction (settlementDate, maturityDate);
        double compoundingFactor = forwardValue
                / (underlyingSpotValue - spotIncome (incomeDiscountCurve));
        return InterestRate.impliedRate (compoundingFactor, tenor, dayCounter, compoundingConvention);
    }

    //
    // public member accessor functions
    //
    public BusinessDayConvention businessDayConvention ()
    {
        return this.businessDayConvention;
    }

    public Calendar calendar ()
    {
        return this.calendar;
    }

    public Date settlementDate ()
    {
        Period advance = new Period (settlementDays, TimeUnit.Days);
        Date settle = calendar.advance (new Settings ().evaluationDate (), advance);
        if (settle.gt (valueDate))
        {
            return settle;
        }
        return valueDate;
    }

    public DayCounter dayCounter ()
    {
        return this.dayCounter;
    }

    public Handle <YieldTermStructure> discountCurve ()
    {
        return this.discountCurve;
    }

    public Handle <YieldTermStructure> incomeDiscountCurve ()
    {
        return this.incomeDiscountCurve;
    }

    //
    // protected final fields
    //

    @Override
    public boolean isExpired ()
    {
        return valueDate.gt (maturityDate);
    }
}