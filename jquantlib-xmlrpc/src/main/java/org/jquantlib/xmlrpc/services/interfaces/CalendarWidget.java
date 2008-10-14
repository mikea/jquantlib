package org.jquantlib.xmlrpc.services.interfaces;

import org.jquantlib.time.calendars.Target;

/**
 * This is an experimental interface for Calendar functions
 * 
 * @see <a href="http://www.nielses.dk/quantlib/mma/QuantLibMma-demo.html">QuantLib for Mathematica</a>
 *
 * @author Richard Gomes
 */
public interface CalendarWidget {

    /**
     * 
     * @param year
     * @param month
     * @param day
     * @return the weekday relative to a given date YYYY/MM/DD
     * 
     * @category widgets
     */
    public abstract String getWeekday(final int year, final int month, final int day);

    /**
     * 
     * @param year
     * @param month
     * @param day
     * @return <code>true</code> if a given date YYYY/MM/DD is a business day in the {@link Target} calendar.
     * 
     * @category widgets
     */
    public abstract boolean isBusinessDay(final int year, final int month, final int day);

}