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

package org.jquantlib.time.calendars;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * United States calendars <br>
 * Public holidays (see: http://www.opm.gov/fedhol/):
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st (possibly moved to Monday if actually on
 * Sunday, or to Friday if on Saturday)</li>
 * <li>Martin Luther King's birthday, third Monday in January</li>
 * <li>Presidents' Day (a.k.a. Washington's birthday), third Monday in February</li>
 * <li>Memorial Day, last Monday in May</li>
 * <li>Independence Day, July 4th (moved to Monday if Sunday or Friday if
 * Saturday)</li>
 * <li>Labor Day, first Monday in September</li>
 * <li>Columbus Day, second Monday in October</li>
 * <li>Veterans' Day, November 11th (moved to Monday if Sunday or Friday if
 * Saturday)</li>
 * <li>Thanksgiving Day, fourth Thursday in November</li>
 * <li>Christmas, December 25th (moved to Monday if Sunday or Friday if
 * Saturday)</li>
 * </ul>
 * 
 * Holidays for the stock exchange (data from http://www.nyse.com):
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st (possibly moved to Monday if actually on
 * Sunday)</li>
 * <li>Martin Luther King's birthday, third Monday in January (since 1998)</li>
 * <li>Presidents' Day (a.k.a. Washington's birthday), third Monday in February</li>
 * <li>Good Friday</li>
 * <li>Memorial Day, last Monday in May</li>
 * <li>Independence Day, July 4th (moved to Monday if Sunday or Friday if
 * Saturday)</li>
 * <li>Labor Day, first Monday in September</li>
 * <li>Thanksgiving Day, fourth Thursday in November</li>
 * <li>Presidential election day, first Tuesday in November of election years
 * (until 1980)</li>
 * <li>Christmas, December 25th (moved to Monday if Sunday or Friday if
 * Saturday)</li>
 * <li>Special historic closings (see http://www.nyse.com/pdfs/closings.pdf)</li>
 * </ul>
 * 
 * Holidays for the government bond market (data from
 * http://www.bondmarkets.com):
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st (possibly moved to Monday if actually on
 * Sunday)</li>
 * <li>Martin Luther King's birthday, third Monday in January</li>
 * <li>Presidents' Day (a.k.a. Washington's birthday), third Monday in February</li>
 * <li>Good Friday</li>
 * <li>Memorial Day, last Monday in May</li>
 * <li>Independence Day, July 4th (moved to Monday if Sunday or Friday if
 * Saturday)</li>
 * <li>Labor Day, first Monday in September</li>
 * <li>Columbus Day, second Monday in October</li>
 * <li>Veterans' Day, November 11th (moved to Monday if Sunday or Friday if
 * Saturday)</li>
 * <li>Thanksgiving Day, fourth Thursday in November</li>
 * <li>Christmas, December 25th (moved to Monday if Sunday or Friday if
 * Saturday)</li>
 * </ul>
 * 
 * Holidays for the North American Energy Reliability Council (data from
 * http://www.nerc.com/~oc/offpeaks.html):
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st (possibly moved to Monday if actually on
 * Sunday)</li>
 * <li>Memorial Day, last Monday in May</li>
 * <li>Independence Day, July 4th (moved to Monday if Sunday)</li>
 * <li>Labor Day, first Monday in September</li>
 * <li>Thanksgiving Day, fourth Thursday in November</li>
 * <li>Christmas, December 25th (moved to Monday if Sunday)</li>
 * </ul>
 * 
 * @author Srinivas Hasti
 */

public class UnitedStates extends DelegateCalendar {
    public static enum Market {
        SETTLEMENT, // !< generic settlement calendar
        NYSE, // !< New York stock exchange calendar
        GOVERNMENTBOND, // !< government-bond calendar
        NERC   // !< off-peak days for NERC
    };

    private final static UnitedStates SETTLEMENT_CALENDAR = new UnitedStates(Market.SETTLEMENT);
    private final static UnitedStates NYSE_CALENDAR       = new UnitedStates(Market.NYSE);
    private final static UnitedStates GOVBOND_CALENDAR    = new UnitedStates(Market.GOVERNMENTBOND);
    private final static UnitedStates NERC_CALENDAR       = new UnitedStates(Market.NERC);

    private UnitedStates(Market market) {
        Calendar delegate;
        switch (market) {
            case SETTLEMENT:
                delegate = new SettlementCalendar();
                break;
            case NYSE:
                delegate = new NyseCalendar();
                break;
            case GOVERNMENTBOND:
                delegate = new GovernmentBondCalendar();
                break;
            case NERC:
                delegate = new NercCalendar();
                break;
            default:
                throw new IllegalArgumentException("unknown market");
        }
        setDelegate(delegate);
    }

    public static UnitedStates getCalendar(Market market) {
        switch (market) {
            case SETTLEMENT:
                return SETTLEMENT_CALENDAR;
            case NYSE:
                return NYSE_CALENDAR;
            case GOVERNMENTBOND:
                return GOVBOND_CALENDAR;
            case NERC:
                return NERC_CALENDAR;
            default:
                throw new IllegalArgumentException("unknown market");
        }
    }

    private final class SettlementCalendar extends WesternCalendar {

        public String getName() {
            return "US settlement";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth();
            int m = date.getMonth();
            if (isWeekend(w)
            // New Year's Day (possibly moved to Monday if on Sunday)
                    || ((d == 1 || (d == 2 && w == Weekday.MONDAY)) && m == Month.JANUARY.toInteger())
                    // (or to Friday if on Saturday)
                    || (d == 31 && w == Weekday.FRIDAY && m == Month.DECEMBER.toInteger())
                    // Martin Luther King's birthday (third Monday in January)
                    || ((d >= 15 && d <= 21) && w == Weekday.MONDAY && m == Month.JANUARY.toInteger())
                    // Washington's birthday (third Monday in February)
                    || ((d >= 15 && d <= 21) && w == Weekday.MONDAY && m == Month.FEBRUARY.toInteger())
                    // Memorial Day (last Monday in May)
                    || (d >= 25 && w == Weekday.MONDAY && m == Month.MAY.toInteger())
                    // Independence Day (Monday if Sunday or Friday if Saturday)
                    || ((d == 4 || (d == 5 && w == Weekday.MONDAY) || (d == 3 && w == Weekday.FRIDAY)) && m == Month.JULY
                            .toInteger())
                    // Labor Day (first Monday in September)
                    || (d <= 7 && w == Weekday.MONDAY && m == Month.SEPTEMBER.toInteger())
                    // Columbus Day (second Monday in October)
                    || ((d >= 8 && d <= 14) && w == Weekday.MONDAY && m == Month.OCTOBER.toInteger())
                    // Veteran's Day (Monday if Sunday or Friday if Saturday)
                    || ((d == 11 || (d == 12 && w == Weekday.MONDAY) || (d == 10 && w == Weekday.FRIDAY)) && m == Month.NOVEMBER
                            .toInteger())
                    // Thanksgiving Day (fourth Thursday in November)
                    || ((d >= 22 && d <= 28) && w == Weekday.THURSDAY && m == Month.NOVEMBER.toInteger())
                    // Christmas (Monday if Sunday or Friday if Saturday)
                    || ((d == 25 || (d == 26 && w == Weekday.MONDAY) || (d == 24 && w == Weekday.FRIDAY)) && m == Month.DECEMBER
                            .toInteger()))
                return false;
            return true;
        }
    }

    private final class NyseCalendar extends WesternCalendar {

    	public String getName() {
            return "New York stock exchange";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            int m = date.getMonth();
            int y = date.getYear();
            int em = easterMonday(y);
            if (isWeekend(w)
            // New Year's Day (possibly moved to Monday if on Sunday)
                    || ((d == 1 || (d == 2 && w == Weekday.MONDAY)) && m == Month.JANUARY.toInteger())
                    // Washington's birthday (third Monday in February)
                    || ((d >= 15 && d <= 21) && w == Weekday.MONDAY && m == Month.FEBRUARY.toInteger())
                    // Good Friday
                    || (dd == em - 3)
                    // Memorial Day (last Monday in May)
                    || (d >= 25 && w == Weekday.MONDAY && m == Month.MAY.toInteger())
                    // Independence Day (Monday if Sunday or Friday if Saturday)
                    || ((d == 4 || (d == 5 && w == Weekday.MONDAY) || (d == 3 && w == Weekday.FRIDAY)) && m == Month.JULY
                            .toInteger())
                    // Labor Day (first Monday in September)
                    || (d <= 7 && w == Weekday.MONDAY && m == Month.SEPTEMBER.toInteger())
                    // Thanksgiving Day (fourth Thursday in November)
                    || ((d >= 22 && d <= 28) && w == Weekday.THURSDAY && m == Month.NOVEMBER.toInteger())
                    // Christmas (Monday if Sunday or Friday if Saturday)
                    || ((d == 25 || (d == 26 && w == Weekday.MONDAY) || (d == 24 && w == Weekday.FRIDAY)) && m == Month.DECEMBER
                            .toInteger()))
                return false;

            if (y >= 1998) {
                if (// Martin Luther King's birthday (third Monday in January)
                ((d >= 15 && d <= 21) && w == Weekday.MONDAY && m == Month.JANUARY.toInteger())
                // President Reagan's funeral
                        || (y == 2004 && m == Month.JUNE.toInteger() && d == 11)
                        // September 11, 2001
                        || (y == 2001 && m == Month.SEPTEMBER.toInteger() && (11 <= d && d <= 14))
                        // President Ford's funeral
                        || (y == 2007 && m == Month.JANUARY.toInteger() && d == 2))
                    return false;
            } else if (y <= 1980) {
                if (// Presidential election days
                ((y % 4 == 0) && m == Month.NOVEMBER.toInteger() && d <= 7 && w == Weekday.TUESDAY)
                // 1977 Blackout
                        || (y == 1977 && m == Month.JULY.toInteger() && d == 14)
                        // Funeral of former President Lyndon B. Johnson.
                        || (y == 1973 && m == Month.JANUARY.toInteger() && d == 25)
                        // Funeral of former President Harry S. Truman
                        || (y == 1972 && m == Month.DECEMBER.toInteger() && d == 28)
                        // National Day of Participation for the lunar
                        // exploration.
                        || (y == 1969 && m == Month.JULY.toInteger() && d == 21)
                        // Funeral of former President Eisenhower.
                        || (y == 1969 && m == Month.MARCH.toInteger() && d == 31)
                        // Closed all day - heavy snow.
                        || (y == 1969 && m == Month.FEBRUARY.toInteger() && d == 10)
                        // Day after Independence Day.
                        || (y == 1968 && m == Month.JULY.toInteger() && d == 5)
                        // June 12-Dec. 31, 1968
                        // Four day week (closed on Wednesdays) - Paperwork
                        // Crisis
                        || (y == 1968 && dd >= 163 && w == Weekday.WEDNESDAY))
                    return false;
            } else {
                if (// Nixon's funeral
                (y == 1994 && m == Month.APRIL.toInteger() && d == 27))
                    return false;
            }
            return true;
        }
    }

    private  final class GovernmentBondCalendar extends WesternCalendar {

        public String getName() {
            return "US government bond market";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth(), dd = date.getDayOfYear();
            int m = date.getMonth();
            int y = date.getYear();
            int em = easterMonday(y);
            if (isWeekend(w)
            // New Year's Day (possibly moved to Monday if on Sunday)
                    || ((d == 1 || (d == 2 && w == Weekday.MONDAY)) && m == Month.JANUARY.toInteger())
                    // Martin Luther King's birthday (third Monday in January)
                    || ((d >= 15 && d <= 21) && w == Weekday.MONDAY && m == Month.JANUARY.toInteger())
                    // Washington's birthday (third Monday in February)
                    || ((d >= 15 && d <= 21) && w == Weekday.MONDAY && m == Month.FEBRUARY.toInteger())
                    // Good Friday
                    || (dd == em - 3)
                    // Memorial Day (last Monday in May)
                    || (d >= 25 && w == Weekday.MONDAY && m == Month.MAY.toInteger())
                    // Independence Day (Monday if Sunday or Friday if Saturday)
                    || ((d == 4 || (d == 5 && w == Weekday.MONDAY) || (d == 3 && w == Weekday.FRIDAY)) && m == Month.JULY
                            .toInteger())
                    // Labor Day (first Monday in September)
                    || (d <= 7 && w == Weekday.MONDAY && m == Month.SEPTEMBER.toInteger())
                    // Columbus Day (second Monday in October)
                    || ((d >= 8 && d <= 14) && w == Weekday.MONDAY && m == Month.OCTOBER.toInteger())
                    // Veteran's Day (Monday if Sunday or Friday if Saturday)
                    || ((d == 11 || (d == 12 && w == Weekday.MONDAY) || (d == 10 && w == Weekday.FRIDAY)) && m == Month.NOVEMBER
                            .toInteger())
                    // Thanksgiving Day (fourth Thursday in November)
                    || ((d >= 22 && d <= 28) && w == Weekday.THURSDAY && m == Month.NOVEMBER.toInteger())
                    // Christmas (Monday if Sunday or Friday if Saturday)
                    || ((d == 25 || (d == 26 && w == Weekday.MONDAY) || (d == 24 && w == Weekday.FRIDAY)) && m == Month.DECEMBER
                            .toInteger()))
                return false;
            return true;
        }
    }

    private final class NercCalendar extends WesternCalendar {

        public String getName() {
            return "North American Energy Reliability Council";
        }

        public boolean isBusinessDay(Date date) {
            Weekday w = date.getWeekday();
            int d = date.getDayOfMonth();
            int m = date.getMonth();
            if (isWeekend(w)
            // New Year's Day (possibly moved to Monday if on Sunday)
                    || ((d == 1 || (d == 2 && w == Weekday.MONDAY)) && m == Month.JANUARY.toInteger())
                    // Memorial Day (last Monday in May)
                    || (d >= 25 && w == Weekday.MONDAY && m == Month.MAY.toInteger())
                    // Independence Day (Monday if Sunday)
                    || ((d == 4 || (d == 5 && w == Weekday.MONDAY)) && m == Month.JULY.toInteger())
                    // Labor Day (first Monday in September)
                    || (d <= 7 && w == Weekday.MONDAY && m == Month.SEPTEMBER.toInteger())
                    // Thanksgiving Day (fourth Thursday in November)
                    || ((d >= 22 && d <= 28) && w == Weekday.THURSDAY && m == Month.NOVEMBER.toInteger())
                    // Christmas (Monday if Sunday)
                    || ((d == 25 || (d == 26 && w == Weekday.MONDAY)) && m == Month.DECEMBER.toInteger()))
                return false;
            return true;
        }
    }
}
