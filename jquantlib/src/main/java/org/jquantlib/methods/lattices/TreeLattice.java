/*
 Copyright (C) 2008 Srinivas Hasti

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
package org.jquantlib.methods.lattices;

import java.util.Vector;

import org.jquantlib.assets.DiscretizedAsset;
import org.jquantlib.math.Array;
import org.jquantlib.math.Closeness;
import org.jquantlib.time.TimeGrid;

/**
 * @author Srinivas Hasti
 */
public abstract class TreeLattice extends Lattice {

	private int n;
	private int statePricesLimit;
	protected Vector<Array> statePrices;

	public TreeLattice(TimeGrid t, int n) {
		super(t);
		this.n = n;
		if (n <= 0)
			throw new IllegalStateException("there is no zeronomial lattice!");
		statePrices = new Vector<Array>();
		statePrices.add(new Array().fill( 1.0 ));
		statePricesLimit = 0;
	}
	
	 public abstract double discount(int i, int index);
	 
     public abstract int descendant(int i, int index, int branch);
     
     public abstract double probability(int i, int index, int branch);
     
     public abstract int size(int i);

	 protected void computeStatePrices(int until)  {
	        for (int i=statePricesLimit; i<until; i++) {
	            statePrices.add(new Array(size(i+1)));
	            for (int j=0; j<size(i); j++) {
	                double disc = discount(i,j);
	                double statePrice = statePrices.get(i).get(j);
	                for (int l=0; l<n; l++) {
	                	Array array = statePrices.get(i+1);
	                	int index = descendant(i,j,l);
	                	double oldValue = array.get(index);
	                	array.set(index,oldValue+(statePrice*disc*probability(i,j,l)));	                   
	                }
	            }
	        }
	        statePricesLimit = until;
	    }

	    public Array statePrices(int i)  {
	        if (i>statePricesLimit)
	            computeStatePrices(i);
	        return statePrices.get(i);
	    }

	    public double presentValue(DiscretizedAsset asset)  {
	        int i = t.index(asset.time());
	        return asset.values().dotProduct(statePrices(i));
	    }

	    public void initialize(DiscretizedAsset asset, double ti)  {
	        int i = t.index(ti);
	        asset.setTime(ti);
	        asset.reset(size(i));
	    }

	    
	    public void rollback(DiscretizedAsset asset, double to)  {
	        partialRollback(asset,to);
	        asset.adjustValues();
	    }

	    public void partialRollback(DiscretizedAsset asset, double to)  {

	        double from = asset.time();

	        if (Closeness.isClose(from,to))
	            return;

	        if (from <= to)
	                throw new RuntimeException("cannot roll the asset back to " + to
	                   + " (it is already at t = " + from + ")");

	        int iFrom = t.index(from);
	        int iTo = t.index(to);

	        for (int i=iFrom-1; i>=iTo; --i) {
	            Array newValues = new Array(size(i));
	            stepback(i, asset.values(), newValues);
	            asset.setTime(t.get(i));
	            asset.setValues(newValues);
	            // skip the very last adjustment
	            if (i != iTo)
	                asset.adjustValues();
	        }
	    }

	   public void stepback(int i, Array values, Array newValues)  {
	        for (int j=0; j<size(i); j++) {
	            double value = 0.0;
	            for (int l=0; l<n; l++) {
	                value += probability(i,j,l) * values.get(descendant(i,j,l));
	            }
	            value *= discount(i,j);
	            newValues.set(j,value);
	        }
	    }
}
