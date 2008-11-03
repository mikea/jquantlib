package jquantlib.osgi.module.client;

import org.jquantlib.time.Calendar;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ClientActivator implements BundleActivator {
	private ServiceReference timeReference;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("start ClientActivator");
		timeReference = context.getServiceReference(Calendar.class.getName());
		System.out.println(timeReference);
		if (null == timeReference) {
			System.out
					.println("timeReference Service may not have been started yet :(");
		} else {
			Object c = context.getService(timeReference);
			// Calendar c = (Calendar) context.getService(timeReference);
			System.out.println(c);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("stop ClientActivator");
		if (null == timeReference) {
			System.out
					.println("timeReference Service may not have been started yet :(");
		} else {
			context.ungetService(timeReference);
		}
	}

}
