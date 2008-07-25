package org.jquantlib.util.stdlibc;

/**
 * @see <a href="http://javadude.com/articles/passbyvalue.htm">Java is Pass-by-Value, Dammit!</a>
 * 
 * @author Richard Gomes
 */
public class DoubleReference {

    private final double[] list;
    private final int index;
    
    public DoubleReference(final double[] list, final int index) {
        this.list = list;
        this.index = index;
    }
    
    public double getValue() {
        return list[index];
    }
    
    public void setValue(final double value) {
        list[index] = value;
    }

}
