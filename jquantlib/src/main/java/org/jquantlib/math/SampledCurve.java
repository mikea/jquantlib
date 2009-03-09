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

import org.jquantlib.math.functions.DoubleFunction;
import org.jquantlib.math.interpolations.CubicSplineInterpolation;
import org.jquantlib.math.interpolations.factories.NaturalCubicSpline;
import org.jquantlib.util.stdlibc.Std;
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
	
	private Array grid_;
	private Array values_;

	//
	// Constructors
	//
	
	public SampledCurve(int gridSize) {
		grid_ = new Array(gridSize);
		values_ = new Array(gridSize);
	}

	public SampledCurve(final Array grid) {
		grid_ = new Array(grid);
		values_ = new Array(grid.size());
	}

	/**
	 * Copy constructor
	 * 
	 * @param that
	 */
	public SampledCurve(SampledCurve that)
	{
		this.grid_ = new Array(that.grid_);
		this.values_ = new Array(that.values_);
	}
	
	
	//
	// public methods
	//
	
	public double valueAtCenter() /* @Readonly */{
			if (empty()) throw new ArithmeticException("empty sampled curve");
		int jmid = size() / 2;
		if (size() % 2 != 0)
			return values_.at(jmid);
		else
			return (values_.at(jmid) + values_.at(jmid - 1)) / 2.0;
	}

	public double firstDerivativeAtCenter() /* @Readonly */{
		if (size() < 3) throw new ArithmeticException("the size of the curve must be at least 3");
		int jmid = size() / 2;
		if (size() % 2 != 0) {
			return (values_.at(jmid + 1) - values_.at(jmid - 1)) / (grid_.at(jmid + 1) - grid_.at(jmid - 1));
		} else {
			return (values_.at(jmid) - values_.at(jmid - 1)) / (grid_.at(jmid) - grid_.at(jmid - 1));
		}
	}

	public double secondDerivativeAtCenter() /* @Readonly */{
		if (size() < 4) throw new ArithmeticException("the size of the curve must be at least 4");
		int jmid = size() / 2;
		if (size() % 2 != 0) {
			double deltaPlus = (values_.at(jmid + 1) - values_.at(jmid) / (grid_.at(jmid + 1) - grid_.at(jmid)));
			double deltaMinus = (values_.at(jmid) - values_.at(jmid - 1) / (grid_.at(jmid) - grid_.at(jmid - 1)));
			double dS = (grid_.at(jmid + 1) - grid_.at(jmid - 1)) / 2.0;
			return (deltaPlus - deltaMinus) / dS;
		} else {
			double deltaPlus = (values_.at(jmid + 1) - values_.at(jmid - 1) / (grid_.at(jmid + 1) - grid_.at(jmid - 1)));
			double deltaMinus = (values_.at(jmid) - values_.at(jmid - 2)) / (grid_.at(jmid) - grid_.at(jmid - 2));
			return (deltaPlus - deltaMinus) / (grid_.at(jmid) - grid_.at(jmid - 1));
		}
	}


	
	// TODO: needs code review
    public void regrid(final Array newGrid) {
    	double[] gridData  = grid_.getData();
    	double[] valueData = values_.getData(); 
        CubicSplineInterpolation priceSpline = new NaturalCubicSpline().interpolate(gridData, valueData);
        priceSpline.reload();

        final Array newValues = new Array(newGrid.size());
        for (int i=0; i<newGrid.size(); i++) {
        	newValues.set(i, priceSpline.evaluate(gridData[i],true) );
        }
        
        values_.swap(newValues);
        grid_ = newGrid;
    }

    
	// TODO: needs code review
    public void regrid(final Array newGrid, final UnaryFunctionDouble func) {
        final Array transformedGrid = new Array(grid_.size());
        Std.transform(grid_, transformedGrid, func);
        
    	double[] gridData  = transformedGrid.getData();
    	double[] valueData = values_.getData(); 
        CubicSplineInterpolation priceSpline = new NaturalCubicSpline().interpolate(gridData, valueData);
        priceSpline.reload();

        final Array newValues = newGrid;
        Std.transform(newValues, newValues, func);
        
        
        for (int i=0; i<newValues.size(); i++) {
        	newValues.set(i, priceSpline.evaluate(newValues.get(i), true) );
        }
        
        values_.swap(newValues);
        grid_ = newGrid;
    }
    
    
	public Array grid() /* @Readonly */{
		return grid_;
	}

	public Array values() /* @Readonly */{
		return values_;
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

	public double value(int i) {
		return values_.at(i);
	}

	public int size() {
		return grid_.size();
	}

	private boolean empty() /* @Readonly */{
		int sizeOfGrid = grid_.size();
		if (sizeOfGrid == 0)
			return true;
		else
			return false;
	}

	public void setValues(Array array) {
		this.values_ = new Array(array);
	}

	public void setLogGrid(final double min, final double max) {
		setGrid(Grid.BoundedLogGrid(min, max, size() - 1));
	}

	public <T extends DoubleFunction> void sample(final T value) {
		for (int i = 0; i < this.grid_.size(); i++) {
			values_.set(i, value.apply(grid_.at(i)));
		}
	}


	//
    // private methods
    //
    
    private void shiftGrid(double s) {
        grid_.operatorAdd(s);
    }

    private void scaleGrid(final double s) {
        grid_.operatorMultiply(s);
    }

    private void setGrid(final Array g) {
        grid_ = g;
    }

}
