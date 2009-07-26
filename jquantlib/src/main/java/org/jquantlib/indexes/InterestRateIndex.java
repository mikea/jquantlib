/*
 Copyright (C) 
 2007 Srinivas Hasti

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

package org.jquantlib.indexes;

import java.util.Currency;

import org.jquantlib.Configuration;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.Constants;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

/**
 * 
 * @author Srinivas Hasti
 * 
 *
 */
//FIXME: code review 1 done by HUE
public abstract class InterestRateIndex extends Index implements Observer {
	private String familyName_;
	private Period tenor_;
	private int fixingDays_;
	private Calendar fixingCalendar_;
	private Currency currency_;
	protected DayCounter dayCounter_;

	@Deprecated
	public InterestRateIndex(final String familyName, 
	                             final Period tenor, 
	                             /*@Natural*/ int fixingDays,
	                             final Calendar fixingCalendar, 
	                             final Currency currency, 
	                             final DayCounter dayCounter) {
        
        if (System.getProperty("EXPERIMENTAL")==null){
            throw new UnsupportedOperationException("not implemented yet");
        }
        
		this.familyName_ = familyName;
		this.tenor_ = tenor;
		this.fixingDays_ = fixingDays;
		this.fixingCalendar_ = fixingCalendar;
		this.currency_ = currency;
		this.dayCounter_ = dayCounter;
		
		tenor_.normalize();

// v.0.8.1
//		if (fixingDays > 2)
//			throw new IllegalArgumentException("wrong number (" + fixingDays
//					+ ") of fixing days");
		
		Configuration.getSystemConfiguration(null)
		.getGlobalSettings().getEvaluationDate().addObserver(this);
		IndexManager.getInstance().notifier(name()).addObserver(this);		
	}

	//0.9.7 interface
	public InterestRateIndex(String familyName, Period tenor, /*Natural*/int fixingDays, Currency currency, Calendar fixingCalendar,
            DayCounter fixedLegDayCounter) {
	        familyName_=(familyName);
	        tenor_=(tenor);
	        fixingDays_=(fixingDays);
	        fixingCalendar_=(fixingCalendar);
	        currency_=(currency);
	        dayCounter_=(fixedLegDayCounter); 
	        tenor_.normalize();
	        //FIXME: review
	        Configuration.getSystemConfiguration(null)
	        .getGlobalSettings().getEvaluationDate().addObserver(this);
	        IndexManager.getInstance().notifier(name()).addObserver(this);      
	       
    }

    @Override
	public double fixing(final Date fixingDate, boolean forecastTodaysFixing) {
		if (isValidFixingDate(fixingDate)){
			throw new IllegalStateException("Fixing date " + fixingDate
					+ " is not valid");
		}
		Date today = org.jquantlib.Configuration.getSystemConfiguration(null)
				.getGlobalSettings().getEvaluationDate();
		boolean enforceTodaysHistoricFixings = org.jquantlib.Configuration
				.getSystemConfiguration(null).isEnforcesTodaysHistoricFixings();
		if (fixingDate.le(today)
				|| (fixingDate.equals(today) && enforceTodaysHistoricFixings && !forecastTodaysFixing)) {
			// must have been fixed
		    //FIXME: suspicious ... getHistory()???
		    /*@Rate*/ double pastFixing = IndexManager.getInstance().get(name()).find(fixingDate);
			if (pastFixing == Constants.NULL_Double){
				throw new IllegalArgumentException("Missing " + name() + " fixing for " + fixingDate);
			}
			return pastFixing;
		}
		if ((fixingDate.equals(today)) && !forecastTodaysFixing) {
			// might have been fixed
			try {
				/*@Rate*/ double pastFixing = IndexManager.getInstance().get(name())
						.find(fixingDate);
				if (pastFixing != Constants.NULL_Double){
					return pastFixing;
				}
				else{
					; // fall through and forecast
				}
			} catch (Exception e) {
				; // fall through and forecast
			}
		}
		// forecast
		return forecastFixing(fixingDate);
	}
	
	   public double fixing(Date fixingDate) {
	       return fixing(fixingDate, false);
	    }

	@Override
	public String name() {
		StringBuilder builder = new StringBuilder(familyName_);
		if (tenor_.units() == TimeUnit.DAYS) {
			if (fixingDays_ == 0){
				builder.append("ON");
			}
			else if (fixingDays_==1){
			    builder.append("TN");
			}
			else if (fixingDays_ == 2){
				builder.append("SN");
			}
			else{
			    builder.append(tenor_.getShortFormat());
			}
		}
		 else{
		    builder.append(tenor_.getShortFormat());
		 }
		builder.append(" ").append(dayCounter_.name());
		return builder.toString();
	}

	public Date fixingDate(Date valueDate) {
	                                                          //FIXME: -!!!!!!!!!
		Date fixingDate = fixingCalendar().advance(valueDate, (-fixingDays_),
				TimeUnit.DAYS);
		if (!(isValidFixingDate(fixingDate)))
			throw new IllegalArgumentException("fixing date " + fixingDate + " is not valid");
		return fixingDate;
	}

	@Override
	public boolean isValidFixingDate(Date fixingDate) {
		return fixingCalendar_.isBusinessDay(fixingDate);
	}

	public String getFamilyName() {
		return familyName_;
	}

	public Period getTenor() {
		return tenor_;
	}

	public int getFixingDays() {
		return fixingDays_;
	}

	public Calendar fixingCalendar() {
		return fixingCalendar_;
	}

	public Currency getCurrency() {
		return currency_;
	}

	public DayCounter getDayCounter() {
		return dayCounter_;
	}

	public abstract Handle<YieldTermStructure> getTermStructure();

	public Date valueDate(Date fixingDate) {
		if (!isValidFixingDate(fixingDate)){
			throw new IllegalArgumentException("Fixing date " + fixingDate
					+ " is not valid");
		}
		return fixingCalendar().advance(fixingDate, fixingDays_,
				TimeUnit.DAYS);
	}

	public abstract Date maturityDate(Date valueDate);

	protected abstract double forecastFixing(Date fixingDate);

	public void update(Observable o, Object arg) {
	    notifyObservers(arg);	
	}
	

}
