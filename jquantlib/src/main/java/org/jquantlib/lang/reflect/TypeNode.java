package org.jquantlib.lang.reflect;

import java.util.AbstractSequentialList;
import java.util.LinkedList;

/**
 * TypeNode keeps a Class information and a list of children classes.
 * <p>
 * Each node holds a Class information and contains a list of children nodes. A typical usage is in the context of
 * retrieving actual generic parameters.
 * <code>
 * 
 * </code>
 * 
 * @see <a href="http://www.jquantlib.org/index.php/Using_TypeTokens_to_retrieve_generic_parameters">Using TypeTokens to retrieve generic parameters</a>
 * @see TypeNodeTree
 * 
 * @author Richard Gomes 
 */
public class TypeNode {

    private final Class<?> element;
    
    public Class<?> getElement() {
        return element;
    }
    
    public TypeNode(final Class<?> klass) {
        this.element = klass;
        this.children = new LinkedList<TypeNode>();
    }

    public TypeNode add(final Class<?> klass) {
        TypeNode node = new TypeNode(klass);
        children.add(node);
        return node;
    }
    
    public TypeNode add(final TypeNode node) {
        children.add(node);
        return node;
    }
    
    private final AbstractSequentialList<TypeNode> children;
    
    public TypeNode get(final int index) {
        return children.get(index);
    }
    
    public Iterable<TypeNode> children() {
        return children;
    }
    
    public Object newInstance() {
        try {
            return element.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
