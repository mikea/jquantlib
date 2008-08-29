/*
 Copyright (C) 2006 Joseph Wang

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

import java.util.HashMap;
import java.util.Map;

/**
 * Interval Price
 * 
 * @author Anand Mani
 */

// TODO: This class is incomplete.
// Need to fully translate from prices.hpp-->IntervalPrice
public class IntervalPrice {
	
	public static enum Type {
		Open, Close, High, Low
	}

	@SuppressWarnings("serial")
	private Map<Type, /* @Real */Double> typeValues = new HashMap<Type, /* @Real */Double>() {
		{
			put(Type.Open, null);
			put(Type.Close, null);
			put(Type.High, null);
			put(Type.Low, null);
		}
	};

	public IntervalPrice(/* @Real */Double open, /* @Real */Double close, /* @Real */Double high, /* @Real */
	Double low) {
		typeValues.put(Type.Open, open);
		typeValues.put(Type.Close, close);
		typeValues.put(Type.High, high);
		typeValues.put(Type.Low, low);
	}

	public/*@Real*/Double getClose() {
		return getValue(Type.Close);
	}

	public/*@Real*/Double getHigh() {
		return getValue(Type.High);
	}

	public/*@Real*/Double getLow() {
		return getValue(Type.Low);
	}

	public/*@Real*/Double getOpen() {
		return getValue(Type.Open);
	}

	public/*@Real*/Double getValue(Type type) {
		return typeValues.get(type);
	}

	public void setClose(/*@Real*/Double close) {
		setValue(Type.Close, close);
	}

	public void setHigh(/*@Real*/Double high) {
		setValue(Type.High, high);
	}

	public void setLow(/*@Real*/Double low) {
		setValue(Type.Low, low);
	}

	public void setOpen(/*@Real*/Double open) {
		setValue(Type.Open, open);
	}

	public void setValue(Type type, /*@Real*/Double value) {
		typeValues.put(type, value);
	}

	public void setValues(/* @Real */Double open, /* @Real */Double close, /* @Real */Double high, /* @Real */
	Double low) {
		typeValues.put(Type.Open, open);
		typeValues.put(Type.Close, close);
		typeValues.put(Type.High, high);
		typeValues.put(Type.Low, low);
	}

}
