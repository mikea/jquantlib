package org.jquantlib.util;

import it.unimi.dsi.fastutil.BidirectionalIterator;

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
