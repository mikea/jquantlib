package org.jquantlib.util;

import java.util.Iterator;

/**
 * This class implements uses the Decorator Pattern in order to implement
 * an unmutable Iterator. 
 *
 * @param <T>
 * 
 * @author Richard Gomes
 */
public class UnmodifiableIterator<T> implements Iterator<T> {

	private final Iterator<T> it;
	
	public UnmodifiableIterator(final Iterator<T> it) {
		this.it = it;
	}
	
	@Override
	public boolean hasNext() {
		return it.hasNext();
	}

	@Override
	public T next() {
		return it.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
