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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jquantlib.cashflow.CashFlow;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;

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

//FIXME: use arrays instead of lists

public class Swap extends Instrument {
    
    private final static  String payer_leg_mismatch = "payer/leg mismatch";
    private final static String no_discounting_termstructure = "no discounting term structure set to Swap";

    // data members
    List<List<CashFlow>> legs_;
    //CashFlow [][] legs_; 
    List<Double /* @Real */> payer_;
    //double[] payer_;
    /* mutable */
    List<Double /* @Real */> legNPV_;
    /* mutable */List<Double /* @Real */> legBPS_;
    // arguments
    private VanillaSwap swap_;
    protected Handle<YieldTermStructure> termStructure_;
    private Settlement.Type settlementType_;

    // ! \name Constructors
    // @{
    /*
     * ! The cash flows belonging to the first leg are paid; the ones belonging to the second leg are received.
     */
    // typedef std::vector<boost::shared_ptr<CashFlow> > Leg;
    public Swap(final Handle<YieldTermStructure> termStructure, final List<CashFlow> /* @Leg */firstLeg, final List<CashFlow> /* @Leg */secondLeg) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        termStructure_ = termStructure; 
        legs_ = new ArrayList<List<CashFlow>>(2);
        payer_ = new ArrayList<Double /* @Real */>(2);
        legNPV_ = new ArrayList<Double>(2)/* , 0.0) */;
        Collections.fill(legNPV_, 0.0);
        legBPS_ = new ArrayList<Double>(2)/* , 0.0) */;
        Collections.fill(legBPS_, 0.0);
        
        legs_.set(0, firstLeg);
        legs_.set(1, secondLeg);
        payer_.set(0, -1.0);
        payer_.set(1, 1.0);
        
        //TODO: review this!!!
        
        for(int i = 0;i<legs_.get(0).size(); i++){
            legs_.get(0).get(i).addObserver(this);
        }
        
        for(int i = 0;i<legs_.get(1).size(); i++){
            legs_.get(1).get(i).addObserver(this);
        }
    }
    
    //FIXME: erasure problem
    public static Swap Swap(final Handle<YieldTermStructure> termStructure, final List<List<CashFlow>>legs,
            final List<Boolean> payer){
        Swap swap = new Swap();
        swap.termStructure_ = termStructure;
        swap.legs_ = legs;
        swap.payer_ = new ArrayList<Double>(legs.size());
        Collections.fill(swap.payer_, 1.0);
        swap.legNPV_ = new ArrayList<Double>(legs.size());
        Collections.fill(swap.legNPV_, 0.0);
        swap.legBPS_ = new ArrayList<Double>(legs.size());
        Collections.fill(swap.legBPS_, 0.0);
        if(payer.size() != legs.size()){
            throw new IllegalArgumentException(payer_leg_mismatch);
        }
        swap.termStructure_.addObserver(swap);
        for(int j=0; j<swap.legs_.size(); j++){
            if(swap.payer_.get(j)==-1.0){
                for(int i = 0; i<legs.get(j).size(); i++){
                    legs.get(j).get(i).addObserver(swap);
                }
            }
        }
        
        return swap;
    }
    
    //helper used above
    protected Swap(){};
            
    

    // erasure causing name clash
    /*
     * ! Multi leg constructor. public Swap(final ArrayList<ArrayList<CashFlow>> legs, final ArrayList<Boolean> payer) {
     * 
     * }
     */
    @Deprecated
    public Swap(int /* @Size */legs) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        legs_ = new ArrayList<List<CashFlow>>(legs);
        payer_ = new ArrayList<Double /* @Real */>(legs);

        legNPV_ = new ArrayList<Double>(legs)/* , 0.0) */;
        legBPS_ = new ArrayList<Double>(legs)/* , 0.0) */;
    }

    @Override
    public boolean isExpired() {
        // problem we are outside package, can't get evaluation date
        // Date today = Settings.instance().evaluationDate();
        //Date today = DateFactory.getFactory().getTodaysDate();
        Date settlement = termStructure_.getLink().referenceDate();
        for (int /* @Size */j = 0; j < legs_.size(); ++j) {
            //Iterator it = legs_.get(j).iterator();
            // FIXME: use for each here
            List<CashFlow> temp = legs_.get(j);
            for(int i = 0; i<temp.size(); i++){
                if(temp.get(i).hasOccurred(settlement)){
                    return false;
                }
            }
            /*
            while (it.hasNext()) {
                CashFlow cf = (CashFlow) it.next();
                if (cf.hasOccurred(today)) {
                    return false;
                }
            }
            */
        }
        return true;
    }

    @Override
    public void setupExpired() {
        super.setupExpired();
        legBPS_ = new ArrayList<Double>(legs_.size());
        legNPV_ = new ArrayList<Double>(legs_.size());
        Collections.fill(legBPS_, 0.0);
        Collections.fill(legNPV_, 0.0);
        
        /**
         * std::fill(legBPS_.begin(), legBPS_.end(), 0.0); std::fill(legNPV_.begin(), legNPV_.end(), 0.0);
         * **/
        /*for (int index = 0; index < legBPS_.size() - 1; index++) {
            legBPS_.set(index, 0.0);
        }

        for (int index = 0; index < legNPV_.size() - 1; index++) {
            legNPV_.set(index, 0.0);
        }
        */

    }
    
    @Override
    protected void performCalculations() {
        if(termStructure_.isEmpty()){
            throw new IllegalArgumentException(no_discounting_termstructure);
        }
        Date d = termStructure_.getLink().referenceDate();
        
        errorEstimate = 0.0;
        NPV = 0.0;
        //for(int j = 0; j<legs_.size()){
           //Date settlement = calendar_[j].advance(d, settlementDays_[j], Days);
           // Date settlement = d;
            //legNPV_.set(j, payer_.get(j)*CashFlow.settlement);
            //NPV_ += legNPV_[j] ;
            //legBPS_[j] = payer_[j]*CashFlows::bps(legs_[j], termStructure_,
                                                  //settlement);
        }
    //}

    public void setupArguments(/* PricingEngine */Arguments args) {
        Swap.Arguments arguments = (Swap.Arguments) args;
        // QL_REQUIRE(arguments != 0, "wrong argument type");

        arguments.legs = legs_;
        //arguments.payer = payer_;
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

        public List<List<CashFlow>> legs;
        public List<Double /* @Real */> payer;

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
        if (j >= legs_.size()) {
            throw new IllegalArgumentException("leg# " + j + " doesn't exist!");
        }
        calculate();
        return legBPS_.get(j);
    }

    public Double /* @Real */legNPV(int /* @Size */j) {
        if (j >= legs_.size()) {
            throw new IllegalArgumentException("leg# " + j + " doesn't exist!");
        }
        calculate();
        return legNPV_.get(j);
    }

    public List<CashFlow> leg(int /* @Size */j) {
        if (j >= legs_.size()) {
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
