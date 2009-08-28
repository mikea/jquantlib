package org.jquantlib.util.stdlibc;

import java.util.NoSuchElementException;

/**
 * @see <a href="http://javadude.com/articles/passbyvalue.htm">Java is Pass-by-Value, Dammit!</a>
 *
 * @author Richard Gomes
 *
 * @deprecated
 */
@Deprecated
public interface MutableIntReverseIterator {

  /**
  * Returns the next element in the iteration.
  * <p>
  * Calling this method repeatedly until the hasPrevious() method returns false will return each element in the underlying
  * collection exactly once.
  *
  * @return a reference object which is able to change elements in the backing data structure
  */
 public IntReference previousReference() throws NoSuchElementException;

}
