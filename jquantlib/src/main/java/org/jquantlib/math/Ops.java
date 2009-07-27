package org.jquantlib.math;

public interface Ops {

    /**
     * This is a interim interface which will be replaced in future by an interface of same name from JSR-166y
     * 
     * @see <a href="http://gee.cs.oswego.edu/dl/jsr166/dist/extra166ydocs/extra166y/Ops.DoubleOp.html">DoubleOp</a>
     * @author Richard Gomes
     */
    public interface DoubleOp {

        public double op(double x);
        //TODO: boolean isFailed() // TODO is error handling needed?
    }

    
    /**
     * This is a interim interface which will be replaced in future by an interface of same name from JSR-166y
     * 
     * @see <a href="http://gee.cs.oswego.edu/dl/jsr166/dist/extra166ydocs/extra166y/Ops.BinaryDoubleOp.html">BinaryDoubleOp</a>
     * @author Richard Gomes
     */
    //TODO : consider http://gee.cs.oswego.edu/dl/jsr166/dist/extra166ydocs/extra166y/Ops.BinaryDoubleOp.html
    public interface BinaryDoubleOp {
        public double op(double x, double y);
        //TODO: boolean isFailed() TODO error handling
    }

    
    /**
     * This is a interim interface which will be replaced in future by an interface of same name from JSR-166y
     * 
     * @see <a href="http://gee.cs.oswego.edu/dl/jsr166/dist/extra166ydocs/extra166y/Ops.DoublePredicate.html">DoublePredicate</a>
     * @author Richard Gomes
     */
    public static interface DoublePredicate {
        public boolean op(double a);
    }

    /**
     * This is a interim interface which will be replaced in future by an interface of same name from JSR-166y
     * 
     * @see <a href="http://gee.cs.oswego.edu/dl/jsr166/dist/extra166ydocs/extra166y/Ops.BinaryDoublePredicate.html">BinaryDoublePredicate</a>
     * @author Richard Gomes
     */
    public static interface BinaryDoublePredicate {
        public boolean op(double a, double b);
    }

}
