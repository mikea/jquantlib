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

import org.jquantlib.math.Array;
import org.jquantlib.math.Grid;
import org.jquantlib.math.interpolations.CubicSpline;
import org.jquantlib.math.interpolations.factories.Cubic;

import it.unimi.dsi.fastutil.doubles.DoubleArrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Dominik Holenstein
 *
 */

public class SampledCurve {
	
	
	//
	// private fields
	//
	private final static Logger logger = LoggerFactory.getLogger(CubicSpline.class);
	
	private Array grid_;
	private Array values_;
	
	
	//
	// Constuctors
	//
	public SampledCurve(int gridSize){
		
	}
	
	public SampledCurve(final Array grid){
		
	}
	
	
	
	
	double valueAtCenter() /* @Read-only */ {
		if (empty()) throw new ArithmeticException("empty sampled curve");
		int jmid = size()/2;
		if (size() % 2 == 1)
			return values_.at(jmid);
		else
			return (values_.at(jmid)+values_.at(jmid-1)/2.0);
	}
	
	double firstDerivativeAtCenter() /* Read-only */ {
		if(size() >= 3) throw new ArithmeticException("the size of the curve must be at least 3");
		int jmid = size()/2;
		if (size() % 2 == 1){
			return (values_.at(jmid+1)-values_.at(jmid-1)) / (grid_.at(jmid+1)-grid_.at(jmid-1));
		}
		else {
			return (values_.at(jmid)-values_.at(jmid-1))) / (grid_.at(jmid)-grid_.at(jmid-1));
		}
	}
	
	/*
	Real SampledCurve::secondDerivativeAtCenter() const {
        QL_REQUIRE(size()>=4,
                   "the size of the curve must be at least 4");
        Size jmid = size()/2;
        if (size() % 2 == 1) {
            Real deltaPlus = (values_[jmid+1]-values_[jmid])/
                (grid_[jmid+1]-grid_[jmid]);
            Real deltaMinus = (values_[jmid]-values_[jmid-1])/
                (grid_[jmid]-grid_[jmid-1]);
            Real dS = (grid_[jmid+1]-grid_[jmid-1])/2.0;
            return (deltaPlus-deltaMinus)/dS;
        } else {
            Real deltaPlus = (values_[jmid+1]-values_[jmid-1])/
                (grid_[jmid+1]-grid_[jmid-1]);
            Real deltaMinus = (values_[jmid]-values_[jmid-2])/
                (grid_[jmid]-grid_[jmid-2]);
            return (deltaPlus-deltaMinus)/
                (grid_[jmid]-grid_[jmid-1]);
        }
    }

    void SampledCurve::regrid(const Array &new_grid) {
        NaturalCubicSpline priceSpline(grid_.begin(), grid_.end(),
                                       values_.begin());
        priceSpline.update();
        Array newValues(new_grid.size());
        Array::iterator val;
        Array::const_iterator grid;
        for (val = newValues.begin(), grid = new_grid.begin() ;
             grid != new_grid.end();
             val++, grid++) {
            *val = priceSpline(*grid, true);
        }
        values_.swap(newValues);
        grid_ = new_grid;
    }
    */
	
	
	void shiftGrid(double s){
		grid_.operatorAdd(s); 
	}
	
	void scaleGrid(double s){
		grid_.operatorMultiply(s);
	}
	
	
	
	
	Array grid() /* @Read-only */{
		return grid_;
	}
	
	Array values() /*@Read-only */ {
		return values_;
	}
	
	//TODO: Check what we have to translate: the const version or the other?
	/*
	inline Array& SampledCurve::grid() {
        return grid_;
    }

    inline const Array& SampledCurve::grid() const {
        return grid_;
    }

    inline const Array& SampledCurve::values() const {
        return values_;
    }

    inline Array& SampledCurve::values() {
        return values_;
    }
	
	*/
	
	double value(int i){
		return values_.at(i);
	}
	
	int size(){
		return grid_.size();
	}
	
	boolean empty() /* Read-only */ {
		int sizeOfGrid = grid_.size();
		if (sizeOfGrid == 0)
			return true;
		else
			return false;
	}
	


}
