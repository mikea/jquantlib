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
package org.jquantlib.indexes;

import java.util.Currency;

import org.jquantlib.Configuration;
import org.jquantlib.daycounters.DayCounter;
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
 */
//FIXME :: code review
public abstract class InterestRateIndex extends Index implements Observer {
	private String familyName;
	private Period tenor;
	private int fixingDays;
	private Calendar fixingCalendar;
	private Currency currency;
	private DayCounter dayCounter;

	public InterestRateIndex(String familyName, Period tenor, int fixingDays,
			Calendar fixingCalendar, Currency currency, DayCounter dayCounter) {
		this.familyName = familyName;
		this.tenor = tenor;
		this.fixingDays = fixingDays;
		this.fixingCalendar = fixingCalendar;
		this.currency = currency;
		this.dayCounter = dayCounter;

		if (fixingDays > 2)
			throw new IllegalArgumentException("wrong number (" + fixingDays
					+ ") of fixing days");
		
		// tenor.normalize(); //TODO
		Configuration.getSystemConfiguration(null)
		.getGlobalSettings().getEvaluationDate().addObserver(this);
		IndexManager.getInstance().notifier(getName()).addObserver(this);		
	}

	@Override
	public double fixing(Date fixingDate, boolean forecastTodaysFixing) {
		if (isValidFixingDate(fixingDate))
			throw new IllegalStateException("Fixing date " + fixingDate
					+ " is not valid");
		Date today = org.jquantlib.Configuration.getSystemConfiguration(null)
				.getGlobalSettings().getEvaluationDate();
		boolean enforceTodaysHistoricFixings = org.jquantlib.Configuration
				.getSystemConfiguration(null).isEnforcesTodaysHistoricFixings();
		if (fixingDate.le(today)
				|| (fixingDate.equals(today) && enforceTodaysHistoricFixings && !forecastTodaysFixing)) {
			// must have been fixed
			Double pastFixing = IndexManager.getInstance().get(getName()).find(
					fixingDate);
			if (pastFixing == null)
				throw new IllegalArgumentException("Missing " + getName() + " fixing for " + fixingDate);
			return pastFixing;
		}
		if ((fixingDate.equals(today)) && !forecastTodaysFixing) {
			// might have been fixed
			try {
				Double pastFixing = IndexManager.getInstance().get(getName())
						.find(fixingDate);
				if (pastFixing != null)
					return pastFixing;
				else
					; // fall through and forecast
			} catch (Exception e) {
				; // fall through and forecast
			}
		}
		// forecast
		return forecastFixing(fixingDate);
	}

	@Override
	public String getName() {
		StringBuilder builder = new StringBuilder(familyName);
		if (tenor.getUnits() == TimeUnit.DAYS) {
			if (fixingDays == 0)
				builder.append("ON");
			else if (fixingDays == 2)
				builder.append("SN");
			else
				builder.append("TN");
		} else
			builder.append(tenor.getShortFormat());
		builder.append(dayCounter.getName());
		return builder.toString();
	}

	public Date fixingDate(Date valueDate) {
		Date fixingDate = getFixingCalendar().advance(valueDate, (fixingDays),
				TimeUnit.DAYS);
		if (!(isValidFixingDate(fixingDate)))
			throw new IllegalArgumentException("fixing date " + fixingDate + " is not valid");
		return fixingDate;
	}

	@Override
	public boolean isValidFixingDate(Date fixingDate) {
		return fixingCalendar.isBusinessDay(fixingDate);
	}

	public String getFamilyName() {
		return familyName;
	}

	public Period getTenor() {
		return tenor;
	}

	public int getFixingDays() {
		return fixingDays;
	}

	public Calendar getFixingCalendar() {
		return fixingCalendar;
	}

	public Currency getCurrency() {
		return currency;
	}

	public DayCounter getDayCounter() {
		return dayCounter;
	}

	public abstract Handle<YieldTermStructure> getTermStructure();

	public Date valueDate(Date fixingDate) {
		if (!isValidFixingDate(fixingDate))
			throw new IllegalArgumentException("Fixing date " + fixingDate
					+ " is not valid");
		return getFixingCalendar().advance(fixingDate, fixingDays,
				TimeUnit.DAYS);
	}

	public abstract Date maturityDate(Date valueDate);

	protected abstract double forecastFixing(Date fixingDate);

	public void update(Observable o, Object arg) {
	    notifyObservers(arg);	
	}

}
