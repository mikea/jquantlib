/*
 Copyright (C) 2008 Srinivas Hasti
 
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

package org.jquantlib.time.calendars;

import org.jquantlib.time.AbstractCalendar;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;

/**
 * 
 * @author Srinivas Hasti
 */
public abstract class DelegateCalendar extends AbstractCalendar {

    private Calendar delegate;

    protected DelegateCalendar() {
    }

    protected void setDelegate(Calendar calendar) {
        this.delegate = calendar;
    }


    public String getName() {
        return delegate.getName();
    }

    public boolean isBusinessDay(Date d) {
        return delegate.isBusinessDay(d);
    }

    public boolean isWeekend(Weekday w) {
        return delegate.isWeekend(w);
    }

}
