package org.jquantlib.osgi.std;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Australia;
import org.jquantlib.time.calendars.NewZealand;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;

public class TimeFactory implements ServiceFactory {
	private int usageCounter = 0;

	public Object getService(Bundle bundle, ServiceRegistration registration) {
		System.out.println("Create object of Calendar for "
				+ bundle.getSymbolicName());
		usageCounter++;
		System.out.println("Number of bundles using service " + usageCounter);
		// Calendar c = Canada.getCalendar(Canada.Market.SETTLEMENT);
		Calendar c = null;
		try {
			// Change the calendar and refresh the bundle
			c = Australia.getCalendar();
//			c = NewZealand.getCalendar();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(c);
		return c;
	}

	public void ungetService(Bundle bundle, ServiceRegistration registration,
			Object service) {
		System.out.println("Release object of Calendar for "
				+ bundle.getSymbolicName());
		usageCounter--;
		System.out.println("Number of bundles using service " + usageCounter);
	}
}
