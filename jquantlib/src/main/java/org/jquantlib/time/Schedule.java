/*
 Copyright (C) 2008 Srinivas Hasti
 Copyright (C) 2008 Dominik Holenstein
 
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

package org.jquantlib.time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.joda.primitives.list.impl.ArrayBooleanList;
import org.jquantlib.time.calendars.NullCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;

/**
 * @author Srinivas Hasti
 * 
 */
// FIXME: this class needs code review :: http://bugs.jquantlib.org/view.php?id=59
public class Schedule {
    
    private static final String full_interface_not_available = "full interface not available";
    
    
    
    private final boolean fullInterface;
    private final Period tenor;
    private final Calendar calendar;
    private final BusinessDayConvention convention;
    private final BusinessDayConvention terminationDateConvention;
    private final DateGenerationRule rule;
    private final boolean endOfMonth;
    private final Date firstDate;
    private final Date nextToLastDate;
    private final boolean finalIsRegular;
    private final List<Date> dates_;
    private final List<Boolean> isRegular;
    

    private Schedule(final Period tenor, 
            final Calendar calendar, 
            final BusinessDayConvention convention,
            final BusinessDayConvention terminationDateConvention, 
            final DateGenerationRule rule, 
            final boolean endOfMonth, 
            final Date firstDate,
            final Date nextToLastDate, 
            final List<Date> dates, 
            final boolean finalIsRegular, 
            final boolean fullInterface) {
        this.tenor = tenor;
        this.calendar = calendar;
        this.convention = convention;
        this.terminationDateConvention = terminationDateConvention;
        this.rule = rule;
        this.endOfMonth = endOfMonth;
        this.firstDate = firstDate;
        this.nextToLastDate = nextToLastDate;
        this.finalIsRegular = finalIsRegular;
        this.fullInterface = fullInterface;
        if (dates == null) {
            this.dates_ = new ArrayList<Date>();
        } else {
            this.dates_ = dates;
        }
        this.isRegular = new ArrayBooleanList();
    }

    public Schedule(final List<Date> dates, final Calendar calendar, final BusinessDayConvention convention) {
        this(new Period(), calendar, convention, convention, DateGenerationRule.FORWARD, false, 
        		DateFactory.getFactory().getTodaysDate(), DateFactory.getFactory().getTodaysDate(), dates, true, false);

    }

    public Schedule(final Date effectiveDate, 
            final Date terminationDate, 
            final Period periodTenor, 
            final Calendar calendar, 
            final BusinessDayConvention businessDayConvention,
            final BusinessDayConvention terminationDateConvention, 
            final DateGenerationRule dateGenerationRule, 
            final boolean endOfMonth, final Date firstDate,
            final Date nextToLastDate) {
    	
        this(periodTenor, calendar, businessDayConvention, terminationDateConvention, dateGenerationRule, endOfMonth, firstDate, nextToLastDate, null, true, true);
        
        if (effectiveDate == Date.NULL_DATE || effectiveDate == null) throw new IllegalArgumentException("Effective date is null");
        if (terminationDate == Date.NULL_DATE || terminationDate == null) throw new IllegalArgumentException("TerminationDate date is null");
        if (effectiveDate.ge(terminationDate)) throw new IllegalArgumentException("Effective date later than or equal to termination date ");

        //
        // Suspicious code
        //
        // If you put "final" in front of rule, tenor and convention, you will see that new valus are assigned to these variables.
        // It's necessary to understand if it is intentional or an error and how the eventually new value is used.
        // -- Richard
        //
        DateGenerationRule rule = dateGenerationRule;
        Period tenor = periodTenor;
        BusinessDayConvention convention = businessDayConvention;
        
        if (tenor.length() == 0)
            rule = DateGenerationRule.ZERO;
        else if (tenor.length() < 0)
            throw new IllegalArgumentException("non positive tenor (" + tenor + ") not allowed");

        if (firstDate != Date.NULL_DATE && firstDate != null) {
            switch (rule) {
            case BACKWARD:
            case FORWARD:
                if (firstDate.lt(effectiveDate) && firstDate.ge(terminationDate))
                    throw new IllegalStateException("first date (" + firstDate + ") out of [effective (" + effectiveDate
                            + "), termination (" + terminationDate + ")] date range");
                break;
            case THIRD_WEDNESDAY:
                if (!IMM.getDefaultIMM().isIMMdate(firstDate, false)) {
                    throw new IllegalStateException("first date (" + firstDate + ") is not an IMM date");
                }

                break;
            case ZERO:
                throw new IllegalStateException("first date incompatible with " + rule + " date generation rule");
            default:
                throw new IllegalStateException("unknown Rule (" + rule + ")");
            }
        }

        if (nextToLastDate != Date.NULL_DATE && nextToLastDate != null) {
            switch (rule) {
            case BACKWARD:
            case FORWARD:
                if (nextToLastDate.le(effectiveDate) && nextToLastDate.ge(terminationDate))
                    throw new IllegalStateException("next to last date (" + nextToLastDate + ") out of [effective ("
                            + effectiveDate + "), termination (" + terminationDate + ")] date range");
                break;
            case THIRD_WEDNESDAY:
                if (!IMM.getDefaultIMM().isIMMdate(nextToLastDate, false)) {
                    throw new IllegalStateException("first date (" + firstDate + ") is not an IMM date");
                }
            case ZERO:
                throw new IllegalStateException("next to last date incompatible with " + rule + " date generation rule");
            default:
                throw new IllegalStateException("unknown Rule (" + rule + ")");
            }
        }

        // calendar needed for endOfMonth adjustment
        Calendar nullCalendar = new NullCalendar();
        int periods = 1;
        Date seed, exitDate;
        switch (rule) {

        case ZERO:
            tenor = new Period(0, TimeUnit.DAYS);
            dates_.add(effectiveDate);
            dates_.add(terminationDate);
            isRegular.add(true);
            break;

        case BACKWARD:

            dates_.add(terminationDate);

            seed = terminationDate;
            if (nextToLastDate != Date.NULL_DATE && nextToLastDate != null) {
                // Add it after 1'st element
                dates_.add(1, nextToLastDate);
                Date temp = nullCalendar.advance(seed, new Period(-periods * tenor.length(), tenor.units()), convention,
                        endOfMonth);
                if (!temp.equals(nextToLastDate))
                    isRegular.add(0, false);
                else
                    isRegular.add(0, true);
                seed = nextToLastDate;
            }

            exitDate = effectiveDate;
            if (firstDate != Date.NULL_DATE && firstDate != null)
                exitDate = firstDate;

            while (true) {
                Date temp = nullCalendar.advance(seed, new Period(-periods * tenor.length(), tenor.units()), convention,
                        endOfMonth);
                if (temp.lt(exitDate))
                    break;
                else {
                    dates_.add(0, temp);
                    isRegular.add(0, true);
                    ++periods;
                }
            }

            if (endOfMonth && calendar.isEndOfMonth(seed))
                convention = BusinessDayConvention.PRECEDING;

            if (!calendar.adjust(dates_.get(0), convention).eq(calendar.adjust(effectiveDate, convention))) {
                dates_.add(0, effectiveDate);
                isRegular.add(0, false);
            }
            break;

        case THIRD_WEDNESDAY:
            if (endOfMonth)
                throw new IllegalStateException("endOfMonth convention incompatible with " + rule + " date generation rule");
            // fall through
        case FORWARD:

            dates_.add(effectiveDate);

            seed = effectiveDate;
            if (firstDate != Date.NULL_DATE && firstDate != null) {
                dates_.add(firstDate);
                Date temp = nullCalendar.advance(seed, new Period(periods * tenor.length(), tenor.units()), convention,
                        endOfMonth);
                if (temp != firstDate)
                    isRegular.add(false);
                else
                    isRegular.add(true);
                seed = firstDate;
            }

            exitDate = terminationDate;
            if (nextToLastDate != Date.NULL_DATE && nextToLastDate != null)
                exitDate = nextToLastDate;

            while (true) {
                Date temp = nullCalendar.advance(seed, new Period(periods * tenor.length(), tenor.units()), convention,
                        endOfMonth);
                if (temp.gt(exitDate))
                    break;
                else {
                    dates_.add(temp);
                    isRegular.add(true);
                    ++periods;
                }
            }

            if (endOfMonth && calendar.isEndOfMonth(seed))
                convention = BusinessDayConvention.PRECEDING;

            if (!calendar.adjust(dates_.get(dates_.size() - 1), terminationDateConvention).equals(
                    calendar.adjust(terminationDate, terminationDateConvention))) {
                dates_.add(terminationDate);
                isRegular.add(false);
            }

            break;

        default:
            throw new IllegalStateException("unknown DateGeneration::Rule (" + rule + ")");
        }

        // adjustments
        if (rule == DateGenerationRule.THIRD_WEDNESDAY)
            for (int i = 1; i < dates_.size() - 1; ++i) {
                Date d = dates_.get(i);
                Date adjDate = d.getNthWeekday(3, Weekday.WEDNESDAY);
                d.getUpdatable().update(adjDate);
            }

        for (int i = 0; i < dates_.size() - 1; ++i) {
            dates_.get(i).getUpdatable().update(calendar.adjust(dates_.get(i), convention));
        }

        // termination date is NOT adjusted as per ISDA specifications,
        // unless otherwise specified in the confirmation of the deal
        if (terminationDateConvention != BusinessDayConvention.UNADJUSTED) {
            dates_.get(dates_.size() - 1).getUpdatable().update(
                    calendar.adjust(dates_.get(dates_.size() - 1), terminationDateConvention));
        }

    }

    public Schedule(Date startDate, 
            Date maturity, 
            Period tenor, 
            Calendar fixingCalendar, 
            BusinessDayConvention convention,
            BusinessDayConvention terminationDateConvention, 
            boolean backward, 
            boolean endOfMonth) {
        throw new UnsupportedOperationException("Diverging from 0.8.1 - needs review");
        /*
        this(startDate, maturity, tenor, fixingCalendar, convention, terminationDateConvention, backward, 
                endOfMonth, DateFactory.getFactory().getDate(0, 0, 0), DateFactory.getFactory().getDate(0,0,0));
        */
        
    }

    public Iterator<Date> getDatesAfter(Date date) {
        if (dates_.size() > 0) {
            List<Date> ldates = new ArrayList<Date>();
            int index = -1;
            for (int i = 0; i < dates_.size(); i++) {
                Date d = dates_.get(i);
                if (d.equals(date)) {
                    index = i;
                    break;
                }
            }
            if (index > 0) {
                for (int i = index; i < dates_.size(); i++) {
                    ldates.add(dates_.get(i));
                }
                return ldates.iterator();
            }
        }
        return Collections.EMPTY_LIST.iterator();
    }

    public Date getNextDate(Date refDate) {
        if (dates_.size() > 0) {
            int index = -1;
            for (int i = 0; i < dates_.size(); i++) {
                Date d = dates_.get(i);
                if (d.equals(refDate)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0 && index != dates_.size() - 1) {
                return dates_.get(index + 1);
            }
        }
        return null;
    }

    public Date getPreviousDate(Date refDate) {
        if (dates_.size() > 0) {
            int index = -1;
            for (int i = 0; i < dates_.size(); i++) {
                Date d = dates_.get(i);
                if (d.equals(refDate)) {
                    index = i;
                    break;
                }
            }
            if (index > 0) {
                return dates_.get(index - 1);
            }
        }
        return null;
    }

    public boolean isRegular(int i) {
        if (!fullInterface)
            throw new IllegalStateException("full interface not available");
        if (isRegular.size() > 0) {

            if (i > isRegular.size() && i < 0)
                throw new IllegalArgumentException("index (" + i + ") must be in [1, " + isRegular.size() + "]");
            return isRegular.get(i - 1);
        }
        return false;
    }
    
    public Calendar getCalendar(){
        return calendar;
    }
    
    
    public Date date(int i){
        return dates_.get(i);
    }
    
    public Period tenor(){
        if(!fullInterface){
            throw new IllegalArgumentException(full_interface_not_available);
        }
        return tenor;
    }
    
    public BusinessDayConvention businessDayConvention(){
        return convention;
    }
    
    public int size(){
        return dates_.size();
    }
    

    /*
     * Date Schedule::previousDate(const Date& refDate) const { std::vector<Date>::const_iterator res = lower_bound(refDate); if
     * (res!=dates_.begin()) return *(--res); else return Date(); }
     * 
     * bool Schedule::isRegular(Size i) const { QL_REQUIRE(fullInterface_, "full interface not available"); QL_REQUIRE(i<=isRegular_.size() &&
     * i>0, "index (" << i << ") must be in [1, " << isRegular_.size() <<"]"); return isRegular_[i-1]; }
     * 
     * 
     * MakeSchedule::MakeSchedule(const Date& effectiveDate, const Date& terminationDate, const Period& tenor, const Calendar&
     * calendar, BusinessDayConvention convention) : calendar_(calendar), effectiveDate_(effectiveDate),
     * terminationDate_(terminationDate), tenor_(tenor), convention_(convention), terminationDateConvention_(convention),
     * rule_(DateGeneration::Backward), endOfMonth_(false), firstDate_(Date()), nextToLastDate_(Date()) {}
     * 
     * MakeSchedule& MakeSchedule::withTerminationDateConvention( BusinessDayConvention conv) { terminationDateConvention_ = conv;
     * return *this; }
     * 
     * MakeSchedule& MakeSchedule::withRule(DateGeneration::Rule r) { rule_ = r; return *this; }
     * 
     * MakeSchedule& MakeSchedule::forwards() { rule_ = DateGeneration::Forward; return *this; }
     * 
     * MakeSchedule& MakeSchedule::backwards() { rule_ = DateGeneration::Backward; return *this; }
     * 
     * MakeSchedule& MakeSchedule::endOfMonth(bool flag) { endOfMonth_ = flag; return *this; }
     * 
     * MakeSchedule& MakeSchedule::withFirstDate(const Date& d) { firstDate_ = d; return *this; }
     * 
     * MakeSchedule& MakeSchedule::withNextToLastDate(const Date& d) { nextToLastDate_ = d; return *this; }
     * 
     * MakeSchedule::operator Schedule() const { return Schedule(effectiveDate_, terminationDate_, tenor_, calendar_, convention_,
     * terminationDateConvention_, rule_, endOfMonth_, firstDate_, nextToLastDate_); }
     */

}
