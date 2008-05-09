package org.jquantlib.util;

/**
 * This is a helper class intended to serve as a simple {@link Observer}.
 */
public class Flag implements Observer {

	private boolean	up;

	public Flag() {
		up = false;
	}

	public void raise() {
		up = true;
	}

	public void lower() {
		up = false;
	}

	public boolean isUp() /* @ReadOnly */{
		return up;
	}

	public void update(Observable observable, Object o) {
		raise();
	}

}