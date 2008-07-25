package org.jquantlib.util.stdlibc;

import java.util.NoSuchElementException;

/**
 * @see <a href="http://javadude.com/articles/passbyvalue.htm">Java is Pass-by-Value, Dammit!</a>
 * 
 * @author Richard Gomes
 */
public interface MutableDoubleForwardIterator {

  /**
  * Returns the next element in the iteration.
  * <p>
  * Calling this method repeatedly until the hasNext() method returns false will return each element in the underlying collection
  * exactly once.
  * 
  * @return a reference object which is able to change elements in the backing data structure
  */
 public DoubleReference nextReference() throws NoSuchElementException;

}
