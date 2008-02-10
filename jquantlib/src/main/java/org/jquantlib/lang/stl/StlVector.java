/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */

package org.jquantlib.lang.stl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javolution.util.FastTable;

import org.jscience.mathematics.structure.Field;

/**
 * This class mimics a subset of the <i>vector</i> class implemented by
 *  STL (C++ Standard Template Library).
 * 
 * <p>
 * <b>NOTE:</b> This class is provided only as a helper class for translation
 * purposes from C++ to Java. Please keep its use as restricted as possible.
 * You can obtain the contents stored in this class by calling toList method.
 * 
 * <p><b>NOTE:</b> This class is deprecated in order to lead developers
 * to avoid or even completely abandon its use.
 * 
 * @param <T>
 *            is the type class of the elements which are meant to be stored in
 *            this Container
 * 
 * @see <a href="http://www.sgi.com/tech/stl/Vector.html">vector&lt;T, Alloc&gt;</a>
 * @see <a href="http://www.cplusplus.com/reference/stl/vector/">vector</a>
 * @see <a
 *      href="http://www.linuxselfhelp.com/HOWTO/C++Programming-HOWTO-17.html">17.3
 *      The Container Classes Interface</a>
 * 
 * @author Richard Gomes
 * 
 * @deprecated
 */
public class StlVector<T extends Field<T>> {

	private FastTable<T> list;
	

	public StlVector() {
		list = new FastTable<T>();
	}

	/**
	 * Clear the entire vector
	 */
	public void clear() {
		list.clear();
	}

	/**
	 * @return the number of elements stored in this container
	 * @see List#size()
	 */
	public int size() {
		return list.size();
	}

	/**
	 * Tests if this container is empty
	 * 
	 * @return <code>true</code> if this container is empty,
	 *         <code>false</code> otherwise.
	 * @see List#isEmpty()
	 */
	public boolean empty() {
		return list.isEmpty();
	}

	/**
	 * Sets a new element at a certain position
	 * 
	 * @param index
	 *            is the position to store a new element at
	 * @param e
	 *            is the element to be stored
	 * @see List#set(int, Object)
	 */
	public T set(int index, final T e) {
		return list.set(index, e);
	}

	/**
	 * Inserts elements at a certain position
	 * type filter text
	 * @param index
	 *            is the position to start to insert elements at
	 * @param c
	 *            is a collection of elements to be inserted
	 * @see List#addAll(int, java.util.Collection)
	 */
	public void insert(int index, final StlVector<? extends T> c) {
		list.addAll(index, ((StlVector<? extends T>) c).list);
	}

	/**
	 * Inserts elements at a certain position
	 * 
	 * @param index
	 *            is the position to start to insert elements at
	 * @param c
	 *            is a collection of elements to be inserted
	 * @see List#addAll(int, java.util.Collection)
	 */
	public void insert(int index, final Collection<? extends T> c) {
		list.addAll(index, c);
	}

	/**
	 * Inserts elements at a certain position
	 * 
	 * @param index
	 *            is the position to start to insert elements at
	 * @param elements
	 *            is a variable list of elements to be inserted
	 * @see List#addAll(int, java.util.Collection)
	 */
	public void insert(int index, final T... elements) {
		list.addAll(index, Arrays.asList(elements));
	}

	/**
	 * Inserts a new last element
	 * 
	 * @param e
	 *            is the element to be inserted in the end
	 * @see List#add(Object)
	 */
	public void push_back(final T e) {
		list.add(e);
	}

	/**
	 * Inserts a new first element
	 * 
	 * @param e
	 *            is the element to be inserted in the front
	 * @see List#add(int, Object)
	 */
	public void push_front(final T e) {
		list.add(0, e);
	}

	/**
	 * Test if this container contains a certain element
	 * 
	 * @param e
	 *            is the element in question
	 * @return <code>true</code> if found, <code>false</code> otherwise
	 * @see List#contains(Object)
	 */
	public boolean find(final T e) {
		return list.contains(e);
	}

	/**
	 * Removes a certain element from this container
	 * 
	 * @param e
	 *            is the element to be removed
	 * @see List#remove(Object)
	 */
	public void erase(final T e) {
		list.remove(e);
	}

	/**
	 * Gets the first element
	 * 
	 * @return the first element
	 * @see List#get(int)
	 */
	public T front() {
		return list.get(0);
	}

	/**
	 * Gets the last element
	 * 
	 * @return the last element
	 * @see List#get(int)
	 */
	public T back() {
		return list.get(list.size() - 1);
	}

	/**
	 * Gets the element at a certain position
	 * 
	 * @param index
	 *            is the position to get the element from
	 * @return the element at positi(Real)on <code>index</code>, if any.
	 *         Throws an exception otherwise
	 * @see List#get(int)
	 */
	public T get(int index) {
		return list.get(index);
	}

	/**
	 * Removes the last element
	 * 
	 * @return the last element
	 * @see List#remove(int)
	 */
	public T pop_back() {
		return list.remove(list.size() - 1);
	}

	/**
	 * Remove the first element
	 * 
	 * @return the first element
	 * @see List#remove(int)
	 */
	public T pop_front() {
		return list.remove(0);
	}

//	/**
//	 * Returns this vector as a T[]
//	 */
//	public T[] toArray(T[] a) {
//		return (T[]) list.toArray(a);
//	}

	/**
	 * Returns this vector as a List<T>
	 */
	public List<T> toList() {
		return list;
	}

}