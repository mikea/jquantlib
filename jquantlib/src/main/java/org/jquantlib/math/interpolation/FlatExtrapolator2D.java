/*
 Copyright (C) 2008 Anand Mani

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

package org.jquantlib.math.interpolation;

public class FlatExtrapolator2D extends AbstractInterpolation2D {

	// FIXME: should be Interpolation2D, but the xMin(), xMax() etc are not in the interface.
	public static Interpolation2D getInterpolation2D(AbstractInterpolation2D decorator) {
		FlatExtrapolator2D flatExtrapolation2D = new FlatExtrapolator2D(decorator);
		return flatExtrapolation2D;
	}

	// FIXME: should be Interpolation2D, but the xMin(), xMax() etc are not in the interface.
	private AbstractInterpolation2D decorator;

	private FlatExtrapolator2D(AbstractInterpolation2D decorator) {
		this.decorator = decorator;
	}

	@Override
	public void reload() {
		decorator.reload();
	}

	//
	// static methods
	//

	@Override
	public void update() {
		decorator.update();
	}

	@Override
	public double xMax() {
		return decorator.xMax();
	}

	@Override
	public double xMin() {
		return decorator.xMin();
	}

	@Override
	public double yMax() {
		return decorator.yMax();
	}

	@Override
	public double yMin() {
		return decorator.yMin();
	}

	private double bindX(double x) /* @ReadOnly */{
		if (x < xMin())
			return xMin();
		if (x > xMax())
			return xMax();
		return x;
	}

	private double bindY(double y) /* @ReadOnly */{
		if (y < yMin())
			return yMin();
		if (y > yMax())
			return yMax();
		return y;
	}

	@Override
	protected double evaluateImpl(double x, double y) {
		x = bindX(x);
		y = bindY(y);
		return decorator.evaluateImpl(x, y);
	}

	@Override
	protected int locateX(double x) {
		return decorator.locateX(x);
	}

	@Override
	protected int locateY(double y) {
		return decorator.locateY(y);
	}

	// TODO: need to override isInRange, but its final in parent
	// protected boolean isInRange(final double x, final double y) {
	// return decorator.isInRange(x, y);
	// }

}
