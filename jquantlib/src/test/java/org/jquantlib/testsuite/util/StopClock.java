package org.jquantlib.testsuite.util;

public class StopClock {
	public static enum Unit {
		MICRO, NANO;
	}

	private Unit units;
	private long startTime;
	private long stopTime;

	public StopClock() {
		this.units = Unit.MICRO;
	}

	public StopClock(Unit unit) {
		this.units = unit;
	}

	public void startClock() {
		if (units == Unit.MICRO)
			startTime = System.currentTimeMillis();
		else
			startTime = System.nanoTime();
	}

	public void stopClock() {
		if (units == Unit.MICRO)
			stopTime = System.currentTimeMillis();
		else
			stopTime = System.nanoTime();
	}

	public long timeElapsed() {
		return stopTime - startTime;
	}

	public Unit getUnit() {
		return units;
	}

	public void reset() {
		startTime = 0;
		stopTime = 0;
	}
	
	public String toString(){
		return ("Time taken: "+timeElapsed()+" "+units);
	}
	
	public void log(){
		System.out.println(toString());
	}
}
