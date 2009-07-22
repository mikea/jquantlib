package org.jquantlib.termstructures;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;

public abstract interface ITermStructure {

    /**
     * @return the latest date for which the curve can return values
     */
    public abstract Date maxDate();

    /**
     * Return the calendar used for reference date calculation
     * 
     * @category Dates and Time
     * @return the calendar used for reference date calculation
     */
    public abstract Calendar calendar();

    /**
     * This method performs a date to double conversion which represents
     * the fraction of the year between the reference date and the date passed as parameter.
     * 
     * @category Dates and Time
     * @param date
     * @return the fraction of the year as a double
     */
    public abstract /*@Time*/ double timeFromReference(final Date date);

    /**
     * Return the day counter used for date/double conversion
     * 
     * @category Dates and Time
     * @return the day counter used for date/double conversion
     * 
     * @see #dayCounter
     */
    public abstract DayCounter dayCounter();

    /**
     * Returns the latest double for which the curve can return values
     * 
     * @category Dates and Time
     * @return the latest double for which the curve can return values
     */
    public abstract /*@Time*/ double maxTime();

    /**
     * Returns the Date at which discount = 1.0 and/or variance = 0.0
     * 
     * @note Term structures initialized by means of this
     * constructor must manage their own reference date 
     * by overriding the getReferenceDate() method.
     *  
     * @category Dates and Time
     * @returns the Date at which discount = 1.0 and/or variance = 0.0
     */
    public abstract Date referenceDate();

}