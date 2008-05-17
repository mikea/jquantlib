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
package org.jquantlib.time;

import java.util.List;

import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;

/**
 * @author Srinivas Hasti
 * 
 */
public class Schedule {
	private boolean fullInterface;
	private Period tenor;
	private Calendar calendar;
	private BusinessDayConvention convention;
	private BusinessDayConvention terminationDateConvention;
	private DateGenerationRule rule;
	private boolean endOfMonth;
	private Date firstDate;
	private Date nextToLastDate;
	private boolean finalIsRegular;
	private List<Date> dates;
	private List<Boolean> isRegular;
	
	private Schedule(Period tenor, Calendar calendar,
			BusinessDayConvention convention,
			BusinessDayConvention terminationDateConvention,
			DateGenerationRule rule, boolean endOfMonth, Date firstDate,
			Date nextToLastDate, List<Date> dates, boolean finalIsRegular, 
			boolean fullInterface) {
		super();
		this.tenor = tenor;
		this.calendar = calendar;
		this.convention = convention;
		this.terminationDateConvention = terminationDateConvention;
		this.rule = rule;
		this.endOfMonth = endOfMonth;
		this.firstDate = firstDate;
		this.nextToLastDate = nextToLastDate;
		this.dates = dates;
		this.finalIsRegular = finalIsRegular;
		this.fullInterface = fullInterface;
	}

	public Schedule(List<Date> dates,
             Calendar calendar,
             BusinessDayConvention convention){
		this(new Period(),calendar,convention,convention,DateGenerationRule.FORWARD,false,DateFactory.getFactory().getTodaysDate(),DateFactory.getFactory().getTodaysDate(),
				dates,true,false);
		
	}


	public Schedule(Date effectiveDate,
            Date terminationDate, Period tenor, Calendar calendar,
			BusinessDayConvention convention,
			BusinessDayConvention terminationDateConvention,
			DateGenerationRule rule, boolean endOfMonth, 
			Date firstDate,
			Date nextToLastDate) {
		this(tenor,calendar,convention,terminationDateConvention,rule,endOfMonth,firstDate,nextToLastDate,
				null,true,true);
	    if(effectiveDate == Date.NULL_DATE || effectiveDate == null)
	    	throw new IllegalArgumentException("Effective date is null");
	    if(terminationDate == Date.NULL_DATE || terminationDate == null)
	    	throw new IllegalArgumentException("TerminationDate date is null");
	    
	    if(effectiveDate.ge(terminationDate))
	    	throw new IllegalArgumentException("Effective date later than or equal to termination date ");
	    
	    if (tenor.getLength()==0)
            rule = DateGenerationRule.ZERO;
        else
            if(tenor.getLength()<0)
            	throw new IllegalArgumentException("non positive tenor (" + tenor + ") not allowed");

        if (firstDate != Date.NULL_DATE && firstDate != null) {
            switch (rule) {
              case BACKWARD:
              case FORWARD:
                 if(firstDate.lt(effectiveDate) &&
                           firstDate.ge(terminationDate))
                	 throw new IllegalStateException(
                           "first date (" + firstDate +
                           ") out of [effective (" + effectiveDate +
                           "), termination (" + terminationDate +
                           ")] date range");
                break;
              case THIRD_WEDNESDAY:
                  if(!IMM.getDefaultIMM().isIMMdate(firstDate, false)){
                      throw new IllegalStateException("first date (" + firstDate +
                      ") is not an IMM date");
                  }
                      
                break;
              case ZERO:
                 throw new IllegalStateException("first date incompatible with " + rule+
                        " date generation rule");
              default:
                 throw new IllegalStateException("unknown Rule (" + rule + ")");
            }
        }
        
        if (nextToLastDate != Date.NULL_DATE && nextToLastDate != null) {
            switch (rule) {
              case BACKWARD:
              case FORWARD:
                if(nextToLastDate.le(effectiveDate) &&
                           nextToLastDate.ge(terminationDate))
                	throw new IllegalStateException(
                           "next to last date (" + nextToLastDate +
                           ") out of [effective (" + effectiveDate +
                           "), termination (" + terminationDate +
                           ")] date range");
                break;
              case THIRD_WEDNESDAY:
                  if(!IMM.getDefaultIMM().isIMMdate(nextToLastDate, false)){
                      throw new IllegalStateException("first date (" + firstDate +
                             ") is not an IMM date");
                  }
              case ZERO:
                throw new IllegalStateException("next to last date incompatible with " + rule +
                        " date generation rule");
              default:
                throw new IllegalStateException("unknown Rule (" + rule + ")");
            }
        }

        /*

        // calendar needed for endOfMonth adjustment
        Calendar nullCalendar = NullCalendar();
        Integer periods = 1;
        Date seed, exitDate;
        switch (rule_) {

          case DateGeneration::Zero:
            tenor_ = 0*Days;
            dates_.push_back(effectiveDate);
            dates_.push_back(terminationDate);
            isRegular_.push_back(true);
            break;

          case DateGeneration::Backward:

            dates_.push_back(terminationDate);

            seed = terminationDate;
            if (nextToLastDate != Date()) {
                dates_.insert(dates_.begin(), nextToLastDate);
                Date temp = nullCalendar.advance(seed,
                    -periods*tenor_, convention, endOfMonth);
                if (temp!=nextToLastDate)
                    isRegular_.insert(isRegular_.begin(), false);
                else
                    isRegular_.insert(isRegular_.begin(), true);
                seed = nextToLastDate;
            }

            exitDate = effectiveDate;
            if (firstDate != Date())
                exitDate = firstDate;

            while (true) {
                Date temp = nullCalendar.advance(seed,
                    -periods*tenor_, convention, endOfMonth);
                if (temp < exitDate)
                    break;
                else {
                    dates_.insert(dates_.begin(), temp);
                    isRegular_.insert(isRegular_.begin(), true);
                    ++periods;
                }
            }

            if (endOfMonth && calendar.isEndOfMonth(seed))
                convention=Preceding;

            if (calendar.adjust(dates_.front(),convention)!=
                calendar.adjust(effectiveDate, convention)) {
                dates_.insert(dates_.begin(), effectiveDate);
                isRegular_.insert(isRegular_.begin(), false);
            }
            break;

          case DateGeneration::ThirdWednesday:
            QL_REQUIRE(!endOfMonth,
                       "endOfMonth convention incompatible with " << rule_ <<
                       " date generation rule");
          // fall through
          case DateGeneration::Forward:

            dates_.push_back(effectiveDate);

            seed = effectiveDate;
            if (firstDate!=Date()) {
                dates_.push_back(firstDate);
                Date temp = nullCalendar.advance(seed,
                    periods*tenor_, convention, endOfMonth);
                if (temp!=firstDate)
                    isRegular_.push_back(false);
                else
                    isRegular_.push_back(true);
                seed = firstDate;
            }

            exitDate = terminationDate;
            if (nextToLastDate != Date())
                exitDate = nextToLastDate;

            while (true) {
                Date temp = nullCalendar.advance(seed,
                    periods*tenor_, convention, endOfMonth);
                if (temp > exitDate)
                    break;
                else {
                    dates_.push_back(temp);
                    isRegular_.push_back(true);
                    ++periods;
                }
            }

            if (endOfMonth && calendar.isEndOfMonth(seed))
                convention=Preceding;

            if (calendar.adjust(dates_.back(),terminationDateConvention)!=
                calendar.adjust(terminationDate, terminationDateConvention)) {
                dates_.push_back(terminationDate);
                isRegular_.push_back(false);
            }

            break;

          default:
            QL_FAIL("unknown DateGeneration::Rule (" << Integer(rule_) << ")");
        }

        // adjustments
        if (rule_==DateGeneration::ThirdWednesday)
            for (Size i=1; i<dates_.size()-1; ++i)
                dates_[i] = Date::nthWeekday(3, Wednesday,
                                             dates_[i].month(),
                                             dates_[i].year());

        for (Size i=0; i<dates_.size()-1; ++i)
            dates_[i]=calendar.adjust(dates_[i], convention);

        // termination date is NOT adjusted as per ISDA specifications,
        // unless otherwise specified in the confirmation of the deal
        if (terminationDateConvention!=Unadjusted) {
            dates_[dates_.size()-1]=calendar.adjust(dates_[dates_.size()-1],
                                                    terminationDateConvention);
        }
	     */
                
	    
	}
	
	

	public Schedule() {
	}

}
