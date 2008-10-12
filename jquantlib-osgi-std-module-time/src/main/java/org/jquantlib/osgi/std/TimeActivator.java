package org.jquantlib.osgi.std;

import org.jquantlib.time.Calendar;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class TimeActivator implements BundleActivator {

	private ServiceRegistration timeRegistration;

	public void start(BundleContext context) throws Exception {
		System.out.println("start TimeActivator");
		TimeFactory timeFactory = new TimeFactory();
		timeRegistration = context.registerService(Calendar.class.getName(),
				timeFactory, null);
		System.out.println(timeRegistration);
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("stop TimeActivator");
		timeRegistration.unregister();
	}
}
