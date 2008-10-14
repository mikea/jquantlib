package org.jquantlib.xmlrpc.services;

import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.AbstractDateFactory;
import org.jquantlib.util.Date;
import org.jquantlib.xmlrpc.services.interfaces.CalendarWidget;

public class Calendar implements CalendarWidget {

    /* (non-Javadoc)
     * @see org.jquantlib.xmlrpc.services.CalendarWidget#getWeekday(int, int, int)
     */
    public final String getWeekday(final int year, final int month, final int day) {
        Date date = AbstractDateFactory.getFactory().getDate(day, month, year);
        return date.getWeekday().toString();
    }

    /* (non-Javadoc)
     * @see org.jquantlib.xmlrpc.services.CalendarWidget#isBusinessDay(int, int, int)
     */
    public final boolean isBusinessDay(final int year, final int month, final int day) {
        Date date = AbstractDateFactory.getFactory().getDate(day, month, year);
        return Target.getCalendar().isBusinessDay(date);
    }
    
}




//
//
//In the financial world dates are of great importance. So are holiday calendars, day counting conventions and rolling conventions. QuantLib for Mathematica makes the calendar and date tools from QuantLib available.
//
//What day was Christmas Day 2002?
//
//     DayOfWeek[{2002, 12, 25}]
//
//     Wednesday
//
//Was this a business day?
//
//     BusinessDayQ[{2002, 12, 25}]
//
//     False
//
//No, it wasn't, but that's because BusinessDayQ by default uses the trans-european holiday calendar TARGET.
//
//     Options[BusinessDayQ]
//
//     {HolidayCalendar -> TARGETCalendar}
//
//     ? TARGETCalendar
//
//     "TARGETCalendar is a holiday calendar representing  the\
//      
//       Trans-European Automated Real-time Gross Express-settlement \
//      
//       Transfer system calendar.\
//      
//       http://www.ecb.int/press/00/pr001214_4.htm"
//
//Instead in Japan, they don't fool around having holidays because of Christmas.
//
//     BusinessDayQ[{2002, 12, 25}, HolidayCalendar -> TokyoCalendar]
//
//     True
//
//So "10 days later" mean different thing, depending on, whether you think of calendar days, business days in London or business days in Tokyo.
//
//     Options[DaysPlus]
//
//     {HolidayCalendar -> None}
//
//     DaysPlus[{2002, 12, 20}, 10]
//
//     {2002, 12, 30}
//
//     DaysPlus[{2002, 12, 20}, 10, HolidayCalendar -> LondonCalendar]
//
//     {2003, 1, 8}
//
//     DaysPlus[{2002, 12, 20}, 10, HolidayCalendar -> TokyoCalendar]
//
//     {2003, 1, 10}
//
//When counting days different day count conventions can be used.
//
//     $DayCountConventions
//
//     {Actual365, Actual360, Thirty360American, Thirty360European, 
//      
//        Thirty360Italian, ActualActualISMA, ActualActualAFB, 
//      
//        ActualActualISDA}
//
//     Options[DaysBetween]
//
//     {DayCountConvention -> Actual365}
//
//So when counting days between two days, it depends on the day counting convention:
//
//     date1 = {2002, 1, 1} ; date2 = {2003, 1, 2} ;
//
//     DaysBetween[date1, date2]
//
//     366
//
//     DaysBetween[date1, date2, DayCountConvention -> Thirty360European]
//
//     361
//
//This also affects calculations of the fraction of years between the dates:
//
//     YearsBetween[date1, date2]
//
//     1.0027397260273974`
//
//     YearsBetween[date1, date2, DayCountConvention -> Thirty360European]
//
//     1.0027777777777778`
//
//Rolling conventions decides in which direction to change ("roll") e.g. a settlement date, when it falls on a holiday.
//
//     HolidayQ[{{2003, 1, 1}, {2003, 1, 2}}, HolidayCalendar -> TokyoCalendar]
//
//     {True, True}
//
//In Japan New Years Day is a holiday and January 2nd is also. So if a settlement day falls on this date is must be rolled. The default is Following.
//
//     ? Following
//
//     "Following is a rolling convention, which adjust to the first\
//      
//       business day  after the given holiday."
//
//     RollDate[{2003, 1, 1}, HolidayCalendar -> TokyoCalendar]
//
//     {2003, 1, 6}
//
//     RollDate[{2003, 1, 1}, HolidayCalendar -> TokyoCalendar, RollingConvention -> Preceding]
//
//     {2002, 12, 30}
//
//Also you can check if a certain year is a leap year.
//
//     LeapYearQ[{1800, 1804, 2000, 2002}]
//
//     LeapYearQ[{1800, 1804, 2000, 2002}]
//               