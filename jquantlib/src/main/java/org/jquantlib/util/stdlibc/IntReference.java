package org.jquantlib.util.stdlibc;

/**
 * @see <a href="http://javadude.com/articles/passbyvalue.htm">Java is Pass-by-Value, Dammit!</a>
 * 
 * @author Richard Gomes
 * 
 * @deprecated
 */
public class IntReference {

    private final int[] list;
    private final int index;
    
    public IntReference(final int[] list, final int index) {
        this.list = list;
        this.index = index;
    }
    
    public int getValue() {
        return list[index];
    }
    
    public void setValue(final int value) {
        list[index] = value;
    }

}
