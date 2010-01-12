/**
 *
 */
package org.jquantlib.lang.iterators;

import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Matrix;


/**
 * An iterator intended to provide read-only access to underlying data kept by classes Matrix and Array
 * <p>
 * Operations add and remove are not implemented
 *
 * @see Matrix
 * @see Array
 *
 * @author Richard Gomes
 *
 * @deprecated
 */
@Deprecated
public interface ConstIterator extends Iterator {
    // this is a tagging interface
}
