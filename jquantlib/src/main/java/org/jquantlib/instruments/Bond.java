/*
 Copyright (C) 2007 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */
package org.jquantlib.instruments;

import java.util.List;

import org.jquantlib.cashflow.CashFlow;
import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;

/**
 * @author Srinivas Hasti
 *
 */
//TODO: Complete impl
public class Bond extends NewInstrument {
	  protected int settlementDays;
	  protected Calendar calendar;
	  protected double faceAmount;
	  protected List<CashFlow> cashFlows;
	  protected Date maturityDate;
	  protected Date issueDate;
      

	public int getSettlementDays() {
		return settlementDays;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public double getFaceAmount() {
		return faceAmount;
	}

	public CashFlow getRedemption() {
		return cashFlows.get(cashFlows.size()-1);
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	@Override
	protected void setupArguments(Arguments arguments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * The returned List includes the redemption as
	 * the last cash flow.
	 *            
	 */
	public List<CashFlow> getCashFlows() {
		return cashFlows;
	}
	
    //@}
    //! \name Calculations
    //@{
    //! theoretical clean price
    /*! The default bond settlement is used for calculation.

        \warning the theoretical price calculated from a flat term
                 structure might differ slightly from the price
                 calculated from the corresponding yield by means
                 of the other overload of this function. If the
                 price from a constant yield is desired, it is
                 advisable to use such other overload.
    */
	//TODO: Implement
    public double getCleanPrice() {
    	return 0.0; 
    }
    //! theoretical dirty price
    /*! The default bond settlement is used for calculation.

        \warning the theoretical price calculated from a flat term
                 structure might differ slightly from the price
                 calculated from the corresponding yield by means
                 of the other overload of this function. If the
                 price from a constant yield is desired, it is
                 advisable to use such other overload.
    */
    //TODO: Implement
    public double getDirtyPrice() {
    	return 0.0;
    }
    //! theoretical bond yield
    /*! The default bond settlement and theoretical price are used
        for calculation.
    */
    /*TODO: implement
    //private static final double accuracy = 1.0e-8;
    public double getYield(DayCounter dc,
               Compounding comp,
               Frequency freq,
               //Real accuracy = 1.0e-8,
               //Size maxEvaluations = 100
               ){
    	return 0.0;
    }*/

    //! clean price given a yield and settlement date
    /*! The default bond settlement is used if no date is given. */
    /*public double getCleanPrice(double yield,
                    DayCounter dc,
                    Compounding comp,
                    Frequency freq,
                    Date settlementDate = Date()) const;
    */
    //! dirty price given a yield and settlement date
    /*! The default bond settlement is used if no date is given. */
    /*Real dirtyPrice(Rate yield,
                    const DayCounter& dc,
                    Compounding comp,
                    Frequency freq,
                    Date settlementDate = Date()) const;
    */
    //! yield given a (clean) price and settlement date
    /*! The default bond settlement is used if no date is given. */
    /*Rate yield(Real cleanPrice,
               const DayCounter& dc,
               Compounding comp,
               Frequency freq,
               Date settlementDate = Date(),
               Real accuracy = 1.0e-8,
               Size maxEvaluations = 100) const;
     */
    //! clean price given Z-spread
    /*! Z-spread compounding, frequency, daycount are taken into account
        The default bond settlement is used if no date is given.
        For details on Z-spread refer to:
        "Credit Spreads Explained", Lehman Brothers European Fixed
        Income Research - March 2004, D. O'Kane*/
    /*Real cleanPriceFromZSpread(Spread zSpread,
                               const DayCounter& dc,
                               Compounding comp,
                               Frequency freq,
                               Date settlementDate = Date()) const;
     */
    //! dirty price given Z-spread
    /*! Z-spread compounding, frequency, daycount are taken into account
        The default bond settlement is used if no date is given.
        For details on Z-spread refer to:
        "Credit Spreads Explained", Lehman Brothers European Fixed
        Income Research - March 2004, D. O'Kane*/
    /*Real dirtyPriceFromZSpread(Spread zSpread,
                               const DayCounter& dc,
                               Compounding comp,
                               Frequency freq,
                               Date settlementDate = Date()) const;
    */
    //! accrued amount at a given date
    /*! The default bond settlement is used if no date is given. */
    /*virtual Real accruedAmount(Date d = Date()) const; */
}
