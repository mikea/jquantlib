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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jquantlib.time.calendars.NullCalendar;
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

	public Schedule(List<Date> dates, Calendar calendar,
			BusinessDayConvention convention) {
		this(new Period(), calendar, convention, convention,
				DateGenerationRule.FORWARD, false, DateFactory.getFactory()
						.getTodaysDate(), DateFactory.getFactory()
						.getTodaysDate(), dates, true, false);

	}

	public Schedule(Date effectiveDate, Date terminationDate, Period tenor,
			Calendar calendar, BusinessDayConvention convention,
			BusinessDayConvention terminationDateConvention,
			DateGenerationRule rule, boolean endOfMonth, Date firstDate,
			Date nextToLastDate) {
		this(tenor, calendar, convention, terminationDateConvention, rule,
				endOfMonth, firstDate, nextToLastDate, null, true, true);
		if (effectiveDate == Date.NULL_DATE || effectiveDate == null)
			throw new IllegalArgumentException("Effective date is null");
		if (terminationDate == Date.NULL_DATE || terminationDate == null)
			throw new IllegalArgumentException("TerminationDate date is null");

		if (effectiveDate.ge(terminationDate))
			throw new IllegalArgumentException(
					"Effective date later than or equal to termination date ");

		if (tenor.getLength() == 0)
			rule = DateGenerationRule.ZERO;
		else if (tenor.getLength() < 0)
			throw new IllegalArgumentException("non positive tenor (" + tenor
					+ ") not allowed");

		if (firstDate != Date.NULL_DATE && firstDate != null) {
			switch (rule) {
			case BACKWARD:
			case FORWARD:
				if (firstDate.lt(effectiveDate)
						&& firstDate.ge(terminationDate))
					throw new IllegalStateException("first date (" + firstDate
							+ ") out of [effective (" + effectiveDate
							+ "), termination (" + terminationDate
							+ ")] date range");
				break;
			case THIRD_WEDNESDAY:
				if (!IMM.getDefaultIMM().isIMMdate(firstDate, false)) {
					throw new IllegalStateException("first date (" + firstDate
							+ ") is not an IMM date");
				}

				break;
			case ZERO:
				throw new IllegalStateException("first date incompatible with "
						+ rule + " date generation rule");
			default:
				throw new IllegalStateException("unknown Rule (" + rule + ")");
			}
		}

		if (nextToLastDate != Date.NULL_DATE && nextToLastDate != null) {
			switch (rule) {
			case BACKWARD:
			case FORWARD:
				if (nextToLastDate.le(effectiveDate)
						&& nextToLastDate.ge(terminationDate))
					throw new IllegalStateException("next to last date ("
							+ nextToLastDate + ") out of [effective ("
							+ effectiveDate + "), termination ("
							+ terminationDate + ")] date range");
				break;
			case THIRD_WEDNESDAY:
				if (!IMM.getDefaultIMM().isIMMdate(nextToLastDate, false)) {
					throw new IllegalStateException("first date (" + firstDate
							+ ") is not an IMM date");
				}
			case ZERO:
				throw new IllegalStateException(
						"next to last date incompatible with " + rule
								+ " date generation rule");
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
			dates.add(effectiveDate);
			dates.add(terminationDate);
			isRegular.add(true);
			break;

		case BACKWARD:

			dates.add(terminationDate);

			seed = terminationDate;
			if (nextToLastDate != Date.NULL_DATE && nextToLastDate != null) {
				// Add it after 1'st element
				dates.add(1, nextToLastDate);
				Date temp = nullCalendar.advance(seed, new Period(-periods
						* tenor.getLength(), tenor.getUnits()), convention,
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
				Date temp = nullCalendar.advance(seed, new Period(-periods
						* tenor.getLength(), tenor.getUnits()), convention,
						endOfMonth);
				if (temp.lt(exitDate))
					break;
				else {
					dates.add(0, temp);
					isRegular.add(0, true);
					++periods;
				}
			}

			if (endOfMonth && calendar.isEndOfMonth(seed))
				convention = BusinessDayConvention.PRECEDING;

			if (calendar.adjust(dates.get(0), convention) != calendar.adjust(
					effectiveDate, convention)) {
				dates.add(0, effectiveDate);
				isRegular.add(0, false);
			}
			break;

		case THIRD_WEDNESDAY:
			if (endOfMonth)
				throw new IllegalStateException(
						"endOfMonth convention incompatible with " + rule
								+ " date generation rule");
			// fall through
		case FORWARD:

			dates.add(effectiveDate);

			seed = effectiveDate;
			if (firstDate != Date.NULL_DATE && firstDate != null) {
				dates.add(firstDate);
				Date temp = nullCalendar.advance(seed, new Period(periods
						* tenor.getLength(), tenor.getUnits()), convention,
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
				Date temp = nullCalendar.advance(seed, new Period(periods
						* tenor.getLength(), tenor.getUnits()), convention,
						endOfMonth);
				if (temp.gt(exitDate))
					break;
				else {
					dates.add(temp);
					isRegular.add(true);
					++periods;
				}
			}

			if (endOfMonth && calendar.isEndOfMonth(seed))
				convention = BusinessDayConvention.PRECEDING;

			if (!calendar.adjust(dates.get(dates.size() - 1),
					terminationDateConvention)
					.equals(
							calendar.adjust(terminationDate,
									terminationDateConvention))) {
				dates.add(terminationDate);
				isRegular.add(false);
			}

			break;

		default:
			throw new IllegalStateException("unknown DateGeneration::Rule ("
					+ rule + ")");
		}

		// adjustments
		if (rule == DateGenerationRule.THIRD_WEDNESDAY)
			for (int i = 1; i < dates.size() - 1; ++i) {
				Date d = dates.get(i);
				Date adjDate = d.getNthWeekday(3, Weekday.WEDNESDAY);
				d.getUpdatable().update(adjDate);
			}

		for (int i = 0; i < dates.size() - 1; ++i) {
			dates.get(i).getUpdatable().update(
					calendar.adjust(dates.get(i), convention));
		}

		// termination date is NOT adjusted as per ISDA specifications,
		// unless otherwise specified in the confirmation of the deal
		if (terminationDateConvention != BusinessDayConvention.UNADJUSTED) {
			dates.get(dates.size() - 1).getUpdatable().update(
					calendar.adjust(dates.get(dates.size() - 1),
							terminationDateConvention));
		}

	}

	public Schedule() {
	}

	public Iterator<Date> getDatesAfter(Date date) {
		List<Date> ldates = new ArrayList<Date>();
		int index = -1;
		for (int i = 0; i < dates.size(); i++) {
			Date d = dates.get(i);
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
		return Collections.EMPTY_LIST.iterator();
	}

	public Date getNextDate(Date refDate) {
		int index = -1;
		for (int i = 0; i < dates.size(); i++) {
			Date d = dates.get(i);
			if (d.equals(refDate)) {
				index = i;
				break;
			}
		}
		if (index >= 0 && index != dates.size() - 1) {
			return dates.get(index + 1);
		}
		return null;
	}

	public Date getPreviousDate(Date refDate) {
		int index = -1;
		for (int i = 0; i < dates.size(); i++) {
			Date d = dates.get(i);
			if (d.equals(refDate)) {
				index = i;
				break;
			}
		}
		if (index > 0) {
			return dates.get(index - 1);
		}
		return null;
	}

	public boolean isRegular(int i) {
		if (!fullInterface)
			throw new IllegalStateException("full interface not available");

		if (i > isRegular.size() && i < 0)
			throw new IllegalArgumentException("index (" + i
					+ ") must be in [1, " + isRegular.size() + "]");
		return isRegular.get(i - 1);
	}

	/*
	 * Date Schedule::previousDate(const Date& refDate) const { std::vector<Date>::const_iterator
	 * res = lower_bound(refDate); if (res!=dates_.begin()) return *(--res);
	 * else return Date(); }
	 * 
	 * bool Schedule::isRegular(Size i) const { QL_REQUIRE(fullInterface_, "full
	 * interface not available"); QL_REQUIRE(i<=isRegular_.size() && i>0,
	 * "index (" << i << ") must be in [1, " << isRegular_.size() <<"]");
	 * return isRegular_[i-1]; }
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
