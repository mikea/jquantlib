/*
 Copyright (C) 2008 Srinivas Hasti

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
package org.jquantlib.time.calendars;

import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.Weekday;

/*
 * Copyright (C) 2008 Srinivas Hasti
 * 
 * This file is part of JQuantLib, a free-software/open-source library for
 * financial quantitative analysts and developers - http://jquantlib.org/
 * 
 * JQuantLib is free software: you can redistribute it and/or modify it under
 * the terms of the QuantLib license. You should have received a copy of the
 * license along with this program; if not, please email
 * <jquantlib-dev@lists.sf.net>. The license is also available online at
 * <http://jquantlib.org/license.shtml>.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the license for more details.
 * 
 * JQuantLib is based on QuantLib. http://quantlib.org/ When applicable, the
 * originating copyright notice follows below.
 */
import org.jquantlib.util.Date;

/**
 * 
 * @author Srinivas Hasti
 */
public abstract class DelegateCalendar implements Calendar {

    private Calendar delegate;

    protected DelegateCalendar() {
    }

    protected void setDelegate(Calendar calendar) {
        this.delegate = calendar;
    }

    public Date advance(Date d, int n, TimeUnit unit, BusinessDayConvention convention, boolean endOfMonth) {
        return delegate.advance(d, n, unit, convention, endOfMonth);
    }

    public Date advance(Date d, int n, TimeUnit unit) {
        return delegate.advance(d, n, unit);
    }

    public Date advance(Date date, Period period, BusinessDayConvention convention, boolean endOfMonth) {
        return delegate.advance(date, period, convention, endOfMonth);
    }

    public long businessDaysBetween(Date from, Date to, boolean includeFirst, boolean includeLast) {
        return delegate.businessDaysBetween(from, to, includeFirst, includeLast);
    }

    public Date getEndOfMonth(Date d) {
        return delegate.getEndOfMonth(d);
    }

    public String getName() {
        return delegate.getName();
    }

    public boolean isBusinessDay(Date d) {
        return delegate.isBusinessDay(d);
    }

    public boolean isEndOfMonth(Date d) {
        return delegate.isEndOfMonth(d);
    }

    public boolean isHoliday(Date d) {
        return delegate.isHoliday(d);
    }

    public boolean isWeekend(Weekday w) {
        return delegate.isWeekend(w);
    }
}
