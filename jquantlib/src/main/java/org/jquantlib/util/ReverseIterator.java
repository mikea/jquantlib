package org.jquantlib.util;

import it.unimi.dsi.fastutil.BidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleBidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterators;

import java.util.Iterator;

/**
 * This class implements uses the Decorator Pattern in order to implement
 * a reverse Iterator. 
 *
 * @param <T>
 * 
 * @author Richard Gomes
 */
public class ReverseIterator<T> implements Iterator<T> {

	private final BidirectionalIterator<T> it;
	
	/**
	 * Constructs a ReverseIterator given a BidirectionalIterator.
	 * <p>
	 * The constructor assumes a BidirectionalIterator <b>positioned in the end</b>. 
	 * The following example is a typical scenario when a ReverseIterator is obtained:
	 * <pre>
     *  public Iterator<Double> reverseIterator() { 
     *  	DoubleBidirectionalIterator it = ((DoubleArrayList)times_).listIterator();
     *  	it.skip(times_.size());
     *  	return new ReverseIterator<Double>(DoubleIterators.unmodifiable(it));
     *  }
	 * </pre>
	 * @param it
	 */
	public ReverseIterator(final BidirectionalIterator<T> it) {
		this.it = it;
	}
	
	@Override
	public boolean hasNext() {
		return it.hasPrevious();
	}

	@Override
	public T next() {
		return it.previous();
	}

	@Override
	public void remove() {
		it.remove();
	}

}
