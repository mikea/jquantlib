package org.jquantlib.lang.reflect;

import java.util.AbstractSequentialList;
import java.util.LinkedList;

public class TypeNode {

    private final Class<?> klass;
    
    public TypeNode(final Class<?> klass) {
        this.klass = klass;
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
        Class c = children.get(index).klass;
        String s = c == null ? "null" : c.getSimpleName();
        System.out.println(s);
        return children.get(index);
    }
    
    public Object newInstance() {
        try {
            return klass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
