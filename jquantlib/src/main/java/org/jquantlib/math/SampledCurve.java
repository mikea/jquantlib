/*
 Copyright (C) 2008 Richard Gomes

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

/*
 Copyright (C) 2003 Ferdinando Ametrano
 Copyright (C) 2004 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */

package org.jquantlib.math;

import org.jquantlib.math.interpolations.CubicSplineInterpolation;
import org.jquantlib.math.interpolations.factories.NaturalCubicSpline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains a sampled curve.
 * <p>
 * Initially the class will contain one indexed curve
 * 
 * @author Dominik Holenstein
 */
public class SampledCurve {

	private final static Logger logger = LoggerFactory.getLogger(SampledCurve.class);

	//
	// private fields
	//
	
	private Array grid;
	private Array values;

	//
	// Constructors
	//
	
	public SampledCurve(int gridSize) {
		this.grid = new Array(gridSize);
		this.values = new Array(gridSize);
	}

	public SampledCurve(final Array grid) {
	    // TODO: RICHARD:: code review :: use of clone()
	    this.grid = grid;
	    this.values = new Array(this.grid.length);
	}

	/**
	 * Copy constructor
	 * 
	 * @param that
	 */
	public SampledCurve(final SampledCurve that) {
	    // TODO: code review :: use of clone()
		this.grid   = that.grid.clone();
		this.values = that.values.clone();
	}
	
	
	//
	// public methods
	//
	
	public double valueAtCenter() /* @Readonly */{
			if (empty()) throw new ArithmeticException("empty sampled curve");
		int jmid = size() / 2;
		if (size() % 2 != 0)
			return values.get(jmid);
		else
			return (values.get(jmid) + values.get(jmid - 1)) / 2.0;
	}

	public double firstDerivativeAtCenter() /* @Readonly */{
		if (size() < 3) throw new ArithmeticException("the size of the curve must be at least 3");
		int jmid = size() / 2;
		if (size() % 2 != 0) {
			return (values.get(jmid+1) - values.get(jmid-1)) / (grid.get(jmid+1) - grid.get(jmid-1));
		} else {
			return (values.get(jmid) - values.get(jmid-1)) / (grid.get(jmid) - grid.get(jmid-1));
		}
	}

	public double secondDerivativeAtCenter() /* @Readonly */{
		if (size() < 4) throw new ArithmeticException("the size of the curve must be at least 4");
		int jmid = size() / 2;
        if (size() % 2 != 0) {
            double deltaPlus = (values.get(jmid + 1) - values.get(jmid)) / ((grid.get(jmid + 1) - grid.get(jmid)));
            double deltaMinus = (values.get(jmid) - values.get(jmid - 1)) / ((grid.get(jmid) - grid.get(jmid - 1)));
            double dS = (grid.get(jmid + 1) - grid.get(jmid - 1)) / 2.0;
            return (deltaPlus - deltaMinus) / dS;
        } else {
            double deltaPlus = (values.get(jmid + 1) - values.get(jmid - 1)) / ((grid.get(jmid + 1) - grid.get(jmid - 1)));
            double deltaMinus = (values.get(jmid) - values.get(jmid - 2)) / (grid.get(jmid) - grid.get(jmid - 2));
            return (deltaPlus - deltaMinus) / (grid.get(jmid) - grid.get(jmid - 1));
        }
	}


	
	// TODO: needs code review
    public void regrid(final Array newGrid) {
    	Array gridData  = this.grid;
    	Array valueData = this.values; 
        CubicSplineInterpolation priceSpline = new NaturalCubicSpline().interpolate(gridData, valueData);
        priceSpline.reload();

        final Array newValues = new Array(newGrid.length);
        for (int i=0; i<newGrid.length; i++) {
        	newValues.set(i, priceSpline.evaluate(gridData.get(i), true) );
        }
        
        // TODO: code review :: use of clone()
        this.values = newValues;
        this.grid = newGrid;
    }

    
	// TODO: needs code review
    public void regrid(final Array newGrid, final UnaryFunctionDouble func) {
        CubicSplineInterpolation priceSpline = new NaturalCubicSpline().interpolate(grid.transform(func), values);

        priceSpline.reload();

        final Array newValues = newGrid.transform(func);
        for (int i=0; i<newValues.length; i++) {
        	newValues.set(i, priceSpline.evaluate(newValues.get(i), true) );
        }
        
        // TODO: code review :: use of clone()
        this.values= newValues;
        this.grid = newGrid;
    }
    
    
	public Array grid() /* @Readonly */{
		return grid;
	}

	public Array values() /* @Readonly */{
		return values;
	}

	// TODO: Check what we have to translate: the const version or the other?
	/*
	 * inline Array& SampledCurve::grid() { return grid_; }
	 * 
	 * inline const Array& SampledCurve::grid() const { return grid_; }
	 * 
	 * inline const Array& SampledCurve::values() const { return values_; }
	 * 
	 * inline Array& SampledCurve::values() { return values_; }
	 */

    public double gridValue(int i) {
        return grid.get(i);
    }

	public double value(int i) {
		return values.get(i);
	}

	public int size() {
		return grid.length;
	}

	private boolean empty() /* @Readonly */{
		return this.grid.empty();
	}

	public void setValues(final Array array) {
	    // TODO: RICHARD:: code review :: use of clone()
		this.values = array;
	}

	public void setLogGrid(final double min, final double max) {
		setGrid(Grid.BoundedLogGrid(min, max, size() - 1));
	}

	public <T extends UnaryFunctionDouble> void sample(final T value) {
		for (int i = 0; i < this.grid.length; i++) {
			this.values.set(i, value.evaluate(grid.get(i)));
		}
	}

    public void shiftGrid(double s) {
        this.grid.addAssign(s);
    }

    public void scaleGrid(final double s) {
        this.grid.mulAssign(s);
    }

    public void setGrid(final Array g) {
        // TODO: RICHARD:: code review :: use of clone()
        this.grid = g;
    }

}
