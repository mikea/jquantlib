/*
 Copyright (C) 2007 Srinivas Hasti

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

package org.jquantlib.indexes;

import java.util.concurrent.ConcurrentHashMap;

import org.jquantlib.util.Observable;
import org.jquantlib.util.ObservableValue;
import org.jquantlib.util.TimeSeriesDouble;

//FIXME: code review
public class IndexManager extends ConcurrentHashMap<String, TimeSeriesDouble> {

    private static final long serialVersionUID = 1L;
	/**
     * @see <a href="http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">The "Double-Checked Locking is Broken" Declaration </a>
     */
    private static volatile IndexManager instance;

	private IndexManager(){}
	
	public static IndexManager getInstance() {
		if (instance == null) {
			synchronized (IndexManager.class) {
				if (instance == null) {
					instance = new IndexManager();
				}
			}
		}
		return instance;
	}
	
	public Observable notifier(String name) {
		TimeSeriesDouble value = super.get(name);
		if(value == null){
			value = new TimeSeriesDouble();
			super.put(name, value);
		}
		return new ObservableValue<TimeSeriesDouble>(value);
	}

	public void clearHistory(String name){
		super.remove(name);
	}
      
	public void clearHistories(){
		super.clear();
	}

}
