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

import org.jquantlib.QL;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.calendars.NullCalendar;

/**
 * The payment schedule of financial instruments defines
 * the dates at which payments are made by one party to another on for
 * example a bond or derivative. It can be either customised or
 * parameterised.
 * <p>
 * The schedule is generated based on a set of rules and
 * market conventions to define the frequencies of the payments. These
 * parameters include: - Payment Frequency (Annually, Semi Annually,
 * Quarterly, Monthly, Weekly, Daily, Continuous) - Payment Day - Day of
 * the month the payment is made - Date rolling - Rule used to adjust
 * the payment date if the schedule date is not a Business Day - Start
 * Date - Date of the first Payment - End Date - Also known as the
 * Maturity date. The date of the last payment
 * 
 * @author Srinivas Hasti
 */
// FIXME: this class needs code review ::
// http://bugs.jquantlib.org/view.php?id=59
public class Schedule {

    private static final String full_interface_not_available = "full interface not available";

    private final boolean fullInterface;
    private final Period tenor;
    private final Calendar calendar;
    private final BusinessDayConvention convention;
    private final BusinessDayConvention terminationDateConvention;
    private final DateGenerationRule rule;
    private final boolean endOfMonth;
    private Date firstDate;
    private Date nextToLastDate;
    private final boolean finalIsRegular;
    private final List<Date> dates;
    private List<Boolean> isRegular;




    //  @Deprecated
    //  private Schedule(final Period tenor, final Calendar calendar, final BusinessDayConvention convention,
    //          final BusinessDayConvention terminationDateConvention, final DateGenerationRule rule, final boolean endOfMonth,
    //          final Date firstDate, final Date nextToLastDate, final List<Date> dates, final boolean finalIsRegular,
    //          final boolean fullInterface) {
    //      this.tenor = tenor;
    //      this.calendar = calendar;
    //      this.convention = convention;
    //      this.terminationDateConvention = terminationDateConvention;
    //      this.rule = rule;
    //      this.endOfMonth = endOfMonth;
    //      this.firstDate = firstDate;
    //      this.nextToLastDate = nextToLastDate;
    //      this.finalIsRegular = finalIsRegular;
    //      this.fullInterface = fullInterface;
    //      if (dates == null) {
    //          this.dates_ = new ArrayList<Date>();
    //      } else {
    //          this.dates_ = dates;
    //      }
    //      this.isRegular = new ArrayBooleanList();
    //  }





    // TODO: code review :: please verify against QL/C++ code
    // remove as soon as possible

    //    @Deprecated
    //    public Schedule(final List<Date> dates, final Calendar calendar, final BusinessDayConvention convention) {
    //        this(new Period(), calendar, convention, convention, DateGenerationRule.FORWARD, false,
    //                new Date().todaysDateAssign(), new Date().todaysDateAssign(), dates, true, false);
    //
    //    }
    //
    //    @Deprecated
    //    public Schedule(final Date startDate, final Date maturity, final Period tenor, final Calendar fixingCalendar,
    //            final BusinessDayConvention convention, final BusinessDayConvention terminationDateConvention, final boolean backward,
    //            final boolean endOfMonth) {
    //        throw new UnsupportedOperationException("Work in progress");
    //    }


    // public Schedule() { }

    public Schedule(final List<Date> dates) {
        this(dates, new NullCalendar(), BusinessDayConvention.UNADJUSTED);
    }

    public Schedule(final List<Date> dates, final Calendar calendar) {
        this(dates, calendar, BusinessDayConvention.UNADJUSTED);
    }

    public Schedule(final List<Date> dates, final Calendar calendar, final BusinessDayConvention convention) {
        this.fullInterface = false;
        this.tenor = new Period();
        this.calendar = calendar;
        this.convention = convention;
        this.terminationDateConvention = convention;
        this.rule = DateGenerationRule.FORWARD;
        this.endOfMonth = false;
        this.finalIsRegular = true;
        this.dates = dates;
    }

    //XXX
    //    public Schedule(final List<Date>  dates, final Calendar  calendar, final BusinessDayConvention convention, final boolean dummy) {
    //        this.fullInterface=(false);
    //        this.tenor=(new Period());
    //        this.calendar=(calendar);
    //        this.convention = (convention);
    //        this.terminationDateConvention=(convention);
    //        this.rule =(DateGenerationRule.FORWARD);
    //        this.endOfMonth =(false);
    //        this.finalIsRegular=(true);
    //        this.dates = new ArrayList<Date>();
    //        for (final Date d: dates) {
    //            this.dates.add(d.clone());
    //        }
    //    }


    public Schedule(
            final Date     effectiveDate,
            final Date     terminationDate,
            final Period   periodTenor,
            final Calendar calendar,
            final BusinessDayConvention businessDayConvention,
            final BusinessDayConvention terminationDateConvention,
            final DateGenerationRule dateGenerationRule,
            final boolean endOfMonth) {
        this(
                effectiveDate, terminationDate,
                periodTenor, calendar,
                businessDayConvention, terminationDateConvention,
                dateGenerationRule, endOfMonth,
                new Date(), new Date());
    }

    public Schedule(
            final Date     effectiveDate,
            final Date     terminationDate,
            final Period   periodTenor,
            final Calendar calendar,
            final BusinessDayConvention businessDayConvention,
            final BusinessDayConvention terminationDateConvention,
            final DateGenerationRule dateGenerationRule,
            final boolean endOfMonth,
            final Date  firstDate) {
        this(
                effectiveDate, terminationDate,
                periodTenor, calendar,
                businessDayConvention, terminationDateConvention,
                dateGenerationRule, endOfMonth,
                firstDate, new Date());
    }


    /**
     * 
     * @param effectiveDate
     * @param terminationDate
     * @param periodTenor
     * @param calendar
     * @param businessDayConvention
     * @param terminationDateConvention
     * @param dateGenerationRule
     * @param endOfMonth
     * @param firstDate default: Date()
     * @param nextToLastDate default: Date()
     */
    public Schedule(
            final Date     effectiveDate,
            final Date     terminationDate,
            final Period   periodTenor,
            final Calendar calendar,
            final BusinessDayConvention businessDayConvention,
            final BusinessDayConvention terminationDateConvention,
            final DateGenerationRule dateGenerationRule,
            final boolean endOfMonth,
            final Date  firstDate,
            final Date  nextToLastDate ) {
        this.fullInterface = true;
        this.tenor = periodTenor;
        this.calendar = calendar;
        this.convention = businessDayConvention;
        this.terminationDateConvention = terminationDateConvention;
        this.rule = dateGenerationRule;
        this.endOfMonth = endOfMonth;
        this.firstDate = firstDate;
        this.nextToLastDate = nextToLastDate;
        this.finalIsRegular = true;
        this.dates = new ArrayList<Date>();

        QL.require(effectiveDate != null && !effectiveDate.isNull(), "effective date is null"); // QA:[RG]::verified // TODO: message
        QL.require(terminationDate != null && !terminationDate.isNull(), "terminationDate date is null"); // QA:[RG]::verified // TODO: message
        QL.require(effectiveDate.lt(terminationDate), "effective date later than or equal to termination date "); // QA:[RG]::verified // TODO: message

        // TODO: code review :: please verify against QL/C++ code

        //
        // Suspicious code
        //
        // If you put "final" in front of rule, tenor and convention, you will
        // see that new valus are assigned to these variables.
        // It's necessary to understand if it is intentional or an error and how
        // the eventually new value is used.
        // -- Richard
        //
        DateGenerationRule rule = dateGenerationRule;
        Period tenor = periodTenor;
        BusinessDayConvention convention = businessDayConvention;

        if (tenor.length() == 0) {
            rule = DateGenerationRule.ZERO;
        } else {
            QL.require(tenor.length() >= 0, "non positive tenor"); // QA:[RG]::verified // TODO: message
        }

        if (firstDate != null && !firstDate.isNull()) {
            switch (rule) {
            case BACKWARD:
            case FORWARD:
                QL.require(firstDate.ge(effectiveDate) || firstDate.lt(terminationDate), "first date out of date range"); // QA:[RG]::verified // TODO: message
                break;
            case THIRD_WEDNESDAY:
                QL.require(new IMM().isIMMdate(firstDate, false), "first date is not an IMM date"); // QA:[RG]::verified // TODO: message
                break;
            case ZERO:
                throw new LibraryException("first date incompatible with date generation rule"); // QA:[RG]::verified // TODO: message
            default:
                throw new LibraryException("unknown rule"); // QA:[RG]::verified // TODO: message
            }
        }

        if (nextToLastDate != null && !nextToLastDate.isNull()) {
            switch (rule) {
            case BACKWARD:
            case FORWARD:
                QL.require(nextToLastDate.gt(effectiveDate) || nextToLastDate.lt(terminationDate),
                "next to last date out of date range"); // QA:[RG]::verified // TODO: message
                break;
            case THIRD_WEDNESDAY:
                QL.require(new IMM().isIMMdate(nextToLastDate, false), "first date is not an IMM date"); // QA:[RG]::verified // TODO: message
            case ZERO:
                throw new LibraryException("next to last date incompatible with date generation rule"); // QA:[RG]::verified // TODO: message
            default:
                throw new LibraryException("unknown rule"); // QA:[RG]::verified // TODO: message
            }
        }

        // calendar needed for endOfMonth adjustment
        final Calendar nullCalendar = new NullCalendar();
        int periods = 1;
        Date seed, exitDate;
        switch (rule) {

        case ZERO:
            tenor = new Period(0, TimeUnit.DAYS);
            dates.add(effectiveDate);
            dates.add(terminationDate);
            isRegular.add(true);
            break;

        case BACKWARD:

            dates.add(terminationDate);

            seed = terminationDate;
            if (nextToLastDate != null && !nextToLastDate.isNull()) {
                // Add it after 1'st element
                dates.add(1, nextToLastDate);
                final Date temp = nullCalendar.advance(seed, new Period(-periods * tenor.length(), tenor.units()), convention, endOfMonth);
                if (!temp.equals(nextToLastDate)) {
                    isRegular.add(0, false);
                } else {
                    isRegular.add(0, true);
                }
                seed = nextToLastDate;
            }

            exitDate = effectiveDate;
            if (firstDate != null && !firstDate.isNull()) {
                exitDate = firstDate;
            }

            while (true) {
                final Date temp = nullCalendar.advance(seed, new Period(-periods * tenor.length(), tenor.units()), convention, endOfMonth);
                if (temp.lt(exitDate)) {
                    break;
                } else {
                    dates.add(0, temp);
                    isRegular.add(0, true);
                    ++periods;
                }
            }

            if (endOfMonth && calendar.isEndOfMonth(seed)) {
                convention = BusinessDayConvention.PRECEDING;
            }

            if (!calendar.adjust(dates.get(0), convention).eq(calendar.adjust(effectiveDate, convention))) {
                dates.add(0, effectiveDate);
                isRegular.add(0, false);
            }
            break;

        case THIRD_WEDNESDAY:
            QL.require(!endOfMonth, "endOfMonth convention incompatible with date generation rule"); // QA:[RG]::verified // TODO: message

            // fall through
        case FORWARD:
            dates.add(effectiveDate);
            seed = effectiveDate;
            if (firstDate != null && !firstDate.isNull()) {
                dates.add(firstDate);
                final Date temp = nullCalendar.advance(seed, new Period(periods * tenor.length(), tenor.units()), convention, endOfMonth);
                if (temp != firstDate) {
                    isRegular.add(false);
                } else {
                    isRegular.add(true);
                }
                seed = firstDate;
            }

            exitDate = terminationDate;
            if (nextToLastDate != null && !nextToLastDate.isNull()) {
                exitDate = nextToLastDate;
            }

            while (true) {
                final Date temp = nullCalendar.advance(seed, new Period(periods * tenor.length(), tenor.units()), convention, endOfMonth);
                if (temp.gt(exitDate)) {
                    break;
                } else {
                    dates.add(temp);
                    isRegular.add(true);
                    ++periods;
                }
            }

            if (endOfMonth && calendar.isEndOfMonth(seed)) {
                convention = BusinessDayConvention.PRECEDING;
            }

            if (!calendar.adjust(dates.get(dates.size() - 1), terminationDateConvention).equals(
                    calendar.adjust(terminationDate, terminationDateConvention))) {
                dates.add(terminationDate);
                isRegular.add(false);
            }

            break;

        default:
            throw new LibraryException("unknown DateGeneration"); // QA:[RG]::verified
            // // TODO:
            // message
        }

        // adjustments
        if (rule == DateGenerationRule.THIRD_WEDNESDAY) {
            for (int i = 1; i < dates.size() - 1; ++i) {
                final Month m = dates.get(i).month();
                final int y = dates.get(i).year();
                final Date d = Date.nthWeekday(3, Weekday.WEDNESDAY, m, y);
                dates.set(i, d);
            }
        }

        for (int i = 0; i < dates.size() - 1; ++i) {
            final Date d = calendar.adjust(dates.get(i), convention);
            dates.set(i, d);
        }

        // termination date is NOT adjusted as per ISDA specifications,
        // unless otherwise specified in the confirmation of the deal
        if (terminationDateConvention != BusinessDayConvention.UNADJUSTED) {
            final int len = dates.size() - 1;
            final Date d = calendar.adjust(dates.get(len), terminationDateConvention);
            dates.set(len, d);
        }

    }

    public Iterator<Date> getDatesAfter(final Date date) {
        if (dates.size() > 0) {
            final List<Date> ldates = new ArrayList<Date>();
            int index = -1;
            for (int i = 0; i < dates.size(); i++) {
                final Date d = dates.get(i);
                if (d.equals(date)) {
                    index = i;
                    break;
                }
            }
            if (index > 0) {
                for (int i = index; i < dates.size(); i++) {
                    ldates.add(dates.get(i));
                }
                return ldates.iterator();
            }
        }
        return Collections.EMPTY_LIST.iterator();
    }

    public Date nextDate(final Date refDate) {
        if (dates.size() > 0) {
            int index = -1;
            for (int i = 0; i < dates.size(); i++) {
                final Date d = dates.get(i);
                if (d.equals(refDate)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0 && index != dates.size() - 1) {
                return dates.get(index + 1);
            }
        }
        return null;
    }

    public Date previousDate(final Date refDate) {
        if (dates.size() > 0) {
            int index = -1;
            for (int i = 0; i < dates.size(); i++) {
                final Date d = dates.get(i);
                if (d.equals(refDate)) {
                    index = i;
                    break;
                }
            }
            if (index > 0) {
                return dates.get(index - 1);
            }
        }
        return null;
    }

    // TODO: code review :: please verify against QL/C++ code
    public boolean isRegular(final int i) {
        QL.require(fullInterface, "full interface not available"); // QA:[RG]::verified  // TODO: message
        if (isRegular.size() > 0) {
            QL.require(i <= isRegular.size() || i >= 0, "index must be in [1, regularSize]"); // QA:[RG]::verified // TODO: message
            return isRegular.get(i - 1);
        }
        return false;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public Date date(final int i) {
        return dates.get(i);
    }

    public Period tenor() {
        QL.require(fullInterface, full_interface_not_available); // QA:[RG]::verified // TODO: message
        return tenor;
    }

    public BusinessDayConvention businessDayConvention() {
        return convention;
    }

    public int size() {
        return dates.size();
    }

    /*
     * Date Schedule::previousDate(const Date& refDate) const {
     * std::vector<Date>::const_iterator res = lower_bound(refDate); if
     * (res!=dates.begin()) return *(--res); else return Date(); }
     * 
     * bool Schedule::isRegular(Size i) const { QL_REQUIRE(fullInterface_,
     * "full interface not available"); QL_REQUIRE(i<=isRegular_.size() && i>0,
     * "index (" << i << ") must be in [1, " << isRegular_.size() <<"]"); return
     * isRegular_[i-1]; }
     * 
     * 
     * MakeSchedule::MakeSchedule(const Date& effectiveDate, const Date&
     * terminationDate, const Period& tenor, const Calendar& calendar,
     * BusinessDayConvention convention) : calendar_(calendar),
     * effectiveDate_(effectiveDate), terminationDate_(terminationDate),
     * tenor_(tenor), convention_(convention),
     * terminationDateConvention_(convention), rule_(DateGeneration::Backward),
     * endOfMonth_(false), firstDate_(Date()), nextToLastDate_(Date()) {}
     * 
     * MakeSchedule& MakeSchedule::withTerminationDateConvention(
     * BusinessDayConvention conv) { terminationDateConvention_ = conv; return
     * *this; }
     * 
     * MakeSchedule& MakeSchedule::withRule(DateGeneration::Rule r) { rule_ = r;
     * return *this; }
     * 
     * MakeSchedule& MakeSchedule::forwards() { rule_ = DateGeneration::Forward;
     * return *this; }
     * 
     * MakeSchedule& MakeSchedule::backwards() { rule_ =
     * DateGeneration::Backward; return *this; }
     * 
     * MakeSchedule& MakeSchedule::endOfMonth(bool flag) { endOfMonth_ = flag;
     * return *this; }
     * 
     * MakeSchedule& MakeSchedule::withFirstDate(const Date& d) { firstDate_ =
     * d; return *this; }
     * 
     * MakeSchedule& MakeSchedule::withNextToLastDate(const Date& d) {
     * nextToLastDate_ = d; return *this; }
     * 
     * MakeSchedule::operator Schedule() const { return Schedule(effectiveDate_,
     * terminationDate_, tenor_, calendar_, convention_,
     * terminationDateConvention_, rule_, endOfMonth_, firstDate_,
     * nextToLastDate_); }
     */

}
