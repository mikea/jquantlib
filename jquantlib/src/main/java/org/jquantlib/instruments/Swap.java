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
import java.util.Iterator;
import org.jquantlib.Settings;
import org.jquantlib.cashflow.CashFlow;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.DefaultDate;

/**
 * 
 * @author Praneet Tiwari
 */

// ! Interest rate swap
/*
 * ! The cash flows belonging to the first leg are paid; the ones belonging to the second leg are received.
 * 
 * \ingroup instruments
 */
public class Swap extends Instrument {

    // data members
    ArrayList<ArrayList<CashFlow>> legs_;
    ArrayList<Double /* @Real */> payer_;
    /* mutable */
    ArrayList<Double /* @Real */> legNPV_;
    /* mutable */ArrayList<Double /* @Real */> legBPS_;
    // arguments
    private VanillaSwap swap_;
    // Handle<YieldTermStructure> termStructure_;
    private Settlement.Type settlementType_;

    // ! \name Constructors
    // @{
    /*
     * ! The cash flows belonging to the first leg are paid; the ones belonging to the second leg are received.
     */
    // typedef std::vector<boost::shared_ptr<CashFlow> > Leg;
    public Swap(final ArrayList<CashFlow> /* @Leg */firstLeg, final ArrayList<CashFlow> /* @Leg */secondLeg) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }

        legs_ = new ArrayList<ArrayList<CashFlow>>(2);
        payer_ = new ArrayList<Double /* @Real */>(2);
        legNPV_ = new ArrayList<Double>(2)/* , 0.0) */;
        legBPS_ = new ArrayList<Double>(2)/* , 0.0) */;
        /*
         * legs_[0] = firstLeg; legs_[1] = secondLeg; payer_[0] = -1.0; payer_[1] = 1.0; for (Leg::iterator i = legs_[0].begin();
         * i!= legs_[0].end(); ++i) registerWith(*i); for (Leg::iterator i = legs_[1].begin(); i!= legs_[1].end(); ++i)
         * registerWith(*i);
         */
        legs_.set(0, firstLeg);
        legs_.set(1, secondLeg);
        payer_.set(0, -1.0);
        payer_.set(1, 1.0);

        // TODO: Register with part
    }

    // erasure causing name clash
    /*
     * ! Multi leg constructor. public Swap(final ArrayList<ArrayList<CashFlow>> legs, final ArrayList<Boolean> payer) {
     * 
     * }
     */
    public Swap(int /* @Size */legs) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        legs_ = new ArrayList<ArrayList<CashFlow>>(legs);
        payer_ = new ArrayList<Double /* @Real */>(legs);

        legNPV_ = new ArrayList<Double>(legs)/* , 0.0) */;
        legBPS_ = new ArrayList<Double>(legs)/* , 0.0) */;
    }

    @Override
    public boolean isExpired() {
        // problem we are outside package, can't get evaluation date
        // Date today = Settings.instance().evaluationDate();
        Date today = DateFactory.getFactory().getTodaysDate();

        for (int /* @Size */j = 0; j < legs_.size(); ++j) {

            /*
             * for (Leg::const_iterator i = legs_[j].begin(); i!= legs_[j].end(); ++i) if (!(*i)->hasOccurred(today)) return false;
             */
            Iterator it = legs_.get(j).iterator();

            // FIXME: use for each here
            while (it.hasNext()) {
                CashFlow cf = (CashFlow) it.next();
                if (cf.hasOccurred(today)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void setupExpired() {
        // Instrument::setupExpired();
        super.setupExpired();
        /**
         * std::fill(legBPS_.begin(), legBPS_.end(), 0.0); std::fill(legNPV_.begin(), legNPV_.end(), 0.0);
         * **/
        for (int index = 0; index < legBPS_.size() - 1; index++) {
            legBPS_.set(index, 0.0);
        }

        for (int index = 0; index < legNPV_.size() - 1; index++) {
            legNPV_.set(index, 0.0);
        }

    }

    public void setupArguments(/* PricingEngine */Arguments args) {
        Swap.Arguments arguments = (Swap.Arguments) args;
        // QL_REQUIRE(arguments != 0, "wrong argument type");

        arguments.legs = legs_;
        arguments.payer = payer_;
    }

    @Override
    protected void performCalculations() throws ArithmeticException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void fetchResults(final Results r) {
        // Instrument.fetchResults(r);
        // Instrument as yet does not have fetch results method

        Swap.Results results = (Swap.Results) r;
        // QL_REQUIRE(results != 0, "wrong result type");

        if (!results.legNPV.isEmpty()) {
            // QL_REQUIRE(results->legNPV.size() == legNPV_.size(),
            // "wrong number of leg NPV returned");
            if (results.legNPV.size() != legNPV_.size()) {
                throw new IllegalArgumentException("wrong number of leg NPV returned");
            }
            legNPV_ = results.legNPV;
        } else {
            // std::fill(legNPV_.begin(), legNPV_.end(), Null<Real>());
            // do we need to fill the nulls?
        }

        if (!results.legBPS.isEmpty()) {
            // QL_REQUIRE(results->legBPS.size() == legBPS_.size(),
            // "wrong number of leg BPS returned");
            if (results.legBPS.size() != legBPS_.size()) {
                throw new IllegalArgumentException("wrong number of leg BPS returned");
            }
            legBPS_ = results.legBPS;
        } else {
            // std::fill(legBPS_.begin(), legBPS_.end(), Null<Real>());
        }
    }

    public Date startDate() {
        // QL_REQUIRE(!legs_.empty(), "no legs given");
        if (legs_.isEmpty()) {
            throw new IllegalArgumentException("no legs given");
        }
        // TODO:CashFlows class does not exist
        /*
         * Date d = CashFlows.startDate(legs_.get(0)); for (Size j=1; j<legs_.size(); ++j) d = std::min(d,
         * CashFlows::startDate(legs_[j])); return d;
         */
        // return something
        return DateFactory.getFactory().getTodaysDate();
    }

    public Date maturityDate() {
        if (legs_.isEmpty()) {
            throw new IllegalArgumentException("no legs given");
        }
        // TODO:CashFlows class does not exist
        /*
         * Date d = CashFlows::maturityDate(legs_[0]); for (Size j=1; j<legs_.size(); ++j) d = std::max(d,
         * CashFlows::maturityDate(legs_[j])); return d;
         */
        // return something
        return DateFactory.getFactory().getTodaysDate();
    }

    // inner classes to represent arguments and results
    public class Arguments extends org.jquantlib.pricingengines.arguments.Arguments { // there is no PricingEngine.arguments to
                                                                                      // extend from
        // : public virtual PricingEngine::arguments {

        public ArrayList<ArrayList<CashFlow>> legs;
        public ArrayList<Double /* @Real */> payer;

        @Override
        public void validate() {
            // QL_REQUIRE(legs.size() == payer.size(),
            // "number of legs and multipliers differ");
            if (legs.size() != payer.size()) {
                throw new IllegalArgumentException("number of legs and multipliers differ");
            }
        }
    }

    public Double /* @Real */legBPS(int /* @Size */j) {
        // QL_REQUIRE(j<legs_.size(), "leg# " << j << " doesn't exist!");
        if (j > legs_.size()) {
            throw new IllegalArgumentException("leg# " + j + " doesn't exist!");
        }
        calculate();
        return legBPS_.get(j);
    }

    public Double /* @Real */legNPV(int /* @Size */j) {
        // QL_REQUIRE(j<legs_.size(), "leg #" << j << " doesn't exist!");
        if (j > legs_.size()) {
            throw new IllegalArgumentException("leg# " + j + " doesn't exist!");
        }
        calculate();
        return legNPV_.get(j);
    }

    public ArrayList<CashFlow> leg(int /* @Size */j) {
        // QL_REQUIRE(j<legs_.size(), "leg #" << j << " doesn't exist!");
        if (j > legs_.size()) {
            throw new IllegalArgumentException("leg# " + j + " doesn't exist!");
        }
        return legs_.get(j);
    }

    public class Results extends org.jquantlib.pricingengines.results.Results {
        // : public Instrument::results {

        public ArrayList<Double /* @Real */> legNPV;
        public ArrayList<Double /* @Real */> legBPS;

        @Override
        public void reset() {
            legNPV.clear();
            legBPS.clear();

        }
    }
    // The engine inner class not written at the moment
    // does nothing substantial
}
