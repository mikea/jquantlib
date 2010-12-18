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
/*
 Copyright (C) 2005, 2006 Theo Boafo
 Copyright (C) 2006, 2007 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
*/

package org.jquantlib.pricingengines.hybrid;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.cashflow.Callability;
import org.jquantlib.cashflow.Dividend;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.DiscretizedAsset;
import org.jquantlib.instruments.bonds.ConvertibleBondOption;
import org.jquantlib.math.Closeness;
import org.jquantlib.math.Constants;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.time.Date;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.TimeGrid;
import org.jquantlib.util.Std;

//TODO: complete this class
public class DiscretizedConvertible extends DiscretizedAsset {

    // protected fields
    protected Array conversionProbability_;
    protected Array spreadAdjustedRate_;
    protected Array dividendValues_;

    // private fields
    private ConvertibleBondOption.ArgumentsImpl arguments_;
    private GeneralizedBlackScholesProcess process_;
    private List< /* @Time */ Double > stoppingTimes_;
    private List< /* @Time */ Double > callabilityTimes_;
    private List< /* @Time */ Double > couponTimes_;
    private List< /* @Time */ Double > dividendTimes_;


    //
    // public methods
    //
    public DiscretizedConvertible(final ConvertibleBondOption.ArgumentsImpl args,
            					  final GeneralizedBlackScholesProcess process,
            					  final TimeGrid grid) {
    	this.arguments_ = args;
    	this.process_ = process; 

       dividendValues_ = new Array(arguments_.dividends.size()).fill(0.0);

       Date settlementDate = process_.riskFreeRate().currentLink().referenceDate();
       for (int i=0; i<arguments_.dividends.size(); i++) {
           if (arguments_.dividends.get(i).date().ge(settlementDate)) {
               double value =  arguments_.dividends.get(i).amount() *
                   				process_.riskFreeRate().currentLink().discount(
                                            arguments_.dividends.get(i).date());
               dividendValues_.set(i, value);
           }
       }

       DayCounter dayCounter = process_.riskFreeRate().currentLink().dayCounter();
       Date bondSettlement = arguments_.settlementDate;

       stoppingTimes_ = new ArrayList<Double>(arguments_.exercise.dates().size());
       for (int i=0; i<arguments_.exercise.dates().size(); ++i)
           stoppingTimes_.add(dayCounter.yearFraction(bondSettlement,
                                       arguments_.exercise.date(i)));

       callabilityTimes_ = new ArrayList<Double>(arguments_.callabilityDates.size());
       for (int i=0; i<arguments_.callabilityDates.size(); ++i)
           callabilityTimes_.add(dayCounter.yearFraction(bondSettlement,
                                       arguments_.callabilityDates.get(i)));

       couponTimes_ = new ArrayList<Double>(arguments_.couponDates.size());
       for (int i=0; i<arguments_.couponDates.size(); ++i)
           couponTimes_.add(dayCounter.yearFraction(bondSettlement,
                                       arguments_.couponDates.get(i)));

       dividendTimes_ = new ArrayList<Double>(arguments_.dividendDates.size());
       for (int i=0; i<arguments_.dividendDates.size(); ++i)
           dividendTimes_.add(dayCounter.yearFraction(bondSettlement,
                                       arguments_.dividendDates.get(i)));

       if (!grid.empty()) {
           // adjust times to grid
           for (int i=0; i<stoppingTimes_.size(); i++)
               stoppingTimes_.set(i, grid.closestTime(stoppingTimes_.get(i)));
           for (int i=0; i<couponTimes_.size(); i++)
               couponTimes_.set(i, grid.closestTime(couponTimes_.get(i)));
           for (int i=0; i<callabilityTimes_.size(); i++)
               callabilityTimes_.set(i, grid.closestTime(callabilityTimes_.get(i)));
           for (int i=0; i<dividendTimes_.size(); i++)
               dividendTimes_.set(i,grid.closestTime(dividendTimes_.get(i)));
       }
   }


    /**
     * <p>This method should initialize the asset values to an {@link Array} of the given size and with values depending on the particular asset. </p>
     */
    @Override
    public void reset(final int size) {
            // Set to bond redemption values
            values_ = new Array(size).fill(arguments_.redemption);

            // coupon amounts should be added when adjusting
            // values_ = Array(size, arguments_.cashFlows.back()->amount());

            conversionProbability_ = new Array(size).fill(0.0);
            spreadAdjustedRate_ = new Array(size).fill(0.0);

            DayCounter rfdc  = process_.riskFreeRate().currentLink().dayCounter();

            // this takes care of convertibility and conversion probabilities
            adjustValues();

            double creditSpread = arguments_.creditSpread.currentLink().value();

            Date exercise = arguments_.exercise.lastDate();

            double riskFreeRate =
                process_.riskFreeRate().currentLink().zeroRate(exercise, rfdc,
                                                   Compounding.Continuous, Frequency.NoFrequency).rate();

            // Calculate blended discount rate to be used on roll back.
            for (int j=0; j<values_.size(); j++) {
               spreadAdjustedRate_.set(j,
                   conversionProbability_.get(j) * riskFreeRate +
                   (1-conversionProbability_.get(j))*(riskFreeRate + creditSpread));
            }
    }

    public Array conversionProbability() { return conversionProbability_; }
    public Array spreadAdjustedRate()    { return spreadAdjustedRate_; }
    public Array dividendValues()        { return dividendValues_; }
    public  void setConversionProbability(Array a) {conversionProbability_ = a; }
    public  void setSpreadAdjustedRate(Array a) {spreadAdjustedRate_ = a; }
    public  void setDividendValues(Array a) {dividendValues_ = a; }
    
    public List<Double> mandatoryTimes() {
    	
        List<Double> result = new ArrayList<Double>();
        Std.copy(stoppingTimes_, 0, stoppingTimes_.size(), result);
        Std.copy(callabilityTimes_, 0, callabilityTimes_.size(),result);
        Std.copy(couponTimes_, 0, couponTimes_.size(), result);
        return result;
    }

    
    protected void postAdjustValuesImpl() {
    	Exercise.Type American = Exercise.Type.American;
    	Exercise.Type European = Exercise.Type.European;
    	Exercise.Type Bermudan = Exercise.Type.Bermudan;
    	
        boolean convertible = false;
        switch (arguments_.exercise.type()) {
          case American:
            if (time() <= stoppingTimes_.get(1) && time() >= stoppingTimes_.get(0))
                convertible = true;
            break;
          case European:
            if (isOnTime(stoppingTimes_.get(0)))
                convertible = true;
            break;
          case Bermudan:
            for (int i=0; i<stoppingTimes_.size(); ++i) {
                if (isOnTime(stoppingTimes_.get(i)))
                    convertible = true;
            }
            break;
          default:
            QL.error("invalid option type");
        }

        for (int i=0; i<callabilityTimes_.size(); i++) {
            if (isOnTime(callabilityTimes_.get(i)))
                applyCallability(i,convertible);
        }

        for (int i=0; i<couponTimes_.size(); i++) {
            if (isOnTime(couponTimes_.get(i)))
                addCoupon(i);
        }

        if (convertible)
            applyConvertibility();
    }

    public void applyConvertibility() {
        Array grid = adjustedGrid();
        for (int j=0; j<values_.size(); j++) {
            double payoff = arguments_.conversionRatio * grid.get(j);
            if (values_.get(j) <= payoff) {
                values_.set(j, payoff);
                conversionProbability_.set(j, 1.0);
            }
        }
    }

    public void applyCallability(int i, boolean convertible) {
        int  j;
        Array grid = adjustedGrid();
        Callability.Type Call = Callability.Type.Call;
        Callability.Type Put = Callability.Type.Put;
        switch (arguments_.callabilityTypes.get(i)) {
          case Call:
            if (arguments_.callabilityTriggers.get(i) != Constants.NULL_REAL) {
                double conversionValue =
                    arguments_.redemption/arguments_.conversionRatio;
                double trigger =
                    conversionValue*arguments_.callabilityTriggers.get(i);
                for (j=0; j<values_.size(); j++) {
                    // the callability is conditioned by the trigger...
                    if (grid.get(j) >= trigger) {
                        // ...and might trigger conversion
                        values_.set(j, 
                            Math.min(Math.max(arguments_.callabilityPrices.get(i),
                                          	  arguments_.conversionRatio*grid.get(j)),
                                     values_.get(j)));
                    }
                }
            } else if (convertible) {
                for (j=0; j<values_.size(); j++) {
                    // exercising the callability might trigger conversion
                    values_.set(j, 
                        Math.min(Math.max(arguments_.callabilityPrices.get(i),
                                          arguments_.conversionRatio*grid.get(j)),
                                 values_.get(j)));
                }
            } else {
                for (j=0; j<values_.size(); j++) {
                    values_.set(j, Math.min(arguments_.callabilityPrices.get(j),
                                          values_.get(j)));
                }
            }
            break;
          case Put:
            for (j=0; j<values_.size(); j++) {
                values_.set(j, Math.max(values_.get(j),
                                      arguments_.callabilityPrices.get(i)));
            }
            break;
          default:
            QL.error("unknown callability type");
        }
    }

    public void addCoupon(int i) {
        values_.addAssign(arguments_.couponAmounts.get(i));
    }

    public Array adjustedGrid() {
        double t = time();
        Array grid = method().grid(t);
        // add back all dividend amounts in the future
        for (int i=0; i<arguments_.dividends.size(); i++) {
            double dividendTime = dividendTimes_.get(i);
            if (dividendTime >= t || Closeness.isCloseEnough(dividendTime,t)) {
               Dividend d = arguments_.dividends.get(i);
                for (int j=0; j<grid.size(); j++) {
                	double v = grid.get(j);
                    v += d.amount(v);
                    grid.set(j,v);// += d->amount(grid[j]);
                }
            }
        }
        return grid;
    }


//    public final Array conversionProbability() /* @ReadOnly */ {
//        throw new UnsupportedOperationException();
//    }
//
//    public final Array  spreadAdjustedRate() /* @ReadOnly */ {
//        throw new UnsupportedOperationException();
//    }
//
//    public final Array  dividendValues() /* @ReadOnly */ {
//        throw new UnsupportedOperationException();
//    }


    // TODO: code review :: verify how these set* methods are defined in QuantLib/C++

//    public final void setConversionProbability(final Array array) {
//        throw new UnsupportedOperationException();
//    }
//
//    public final void setSpreadAdjustedRate(final Array array) {
//        throw new UnsupportedOperationException();
//    }
//
//    public final void setDividendValues(final Array array) {
//        throw new UnsupportedOperationException();
//    }



//    /**
//     * <p>This method returns the times at which the numerical method should stop while rolling back the asset. Typical examples include payment times, exercise times and such.</p>
//     * @note <p>The returned values are not guaranteed to be sorted. </p>
//     */
//    @Override
//    public List< /* @Time */ Double > mandatoryTimes() /* @ReadOnly */ {
//        throw new UnsupportedOperationException();
//    }



    //
    // protected methods
    //

    /**
     * <p>This method performs the actual post-adjustment </p>
     */
//    @Override
//    protected void postAdjustValuesImpl() {
//        throw new UnsupportedOperationException();
//    }



    //
    // private methods
    //


}
