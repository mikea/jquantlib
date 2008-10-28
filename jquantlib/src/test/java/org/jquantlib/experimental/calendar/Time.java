package org.jquantlib.experimental.calendar;

public interface Time {
   public int getHourOfDay();
   public int getMinuteOfHour();
   public int getSecondOfMinute();
   public int getNanoOfSecond();
}
