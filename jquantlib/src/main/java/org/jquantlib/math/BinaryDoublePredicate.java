package org.jquantlib.math;

/**
 * This is a interim interface which will be replaced in future by
 * an interface of same name from JSR-166y
 * 
 * @see <a href="http://gee.cs.oswego.edu/dl/jsr166/dist/extra166ydocs/extra166y/Ops.BinaryDoublePredicate.html">BinaryDoublePredicate</a>
 * @author Richard Gomes
 */
//TODO: http://bugs.jquantlib.org/view.php?id=315
// adopt instead :: http://gee.cs.oswego.edu/dl/jsr166/dist/extra166ydocs/extra166y/Ops.BinaryDoublePredicate.html
public interface BinaryDoublePredicate {

    public boolean op(double a, double b);
    
}
