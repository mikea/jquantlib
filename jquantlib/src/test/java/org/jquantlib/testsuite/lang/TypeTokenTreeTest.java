package org.jquantlib.testsuite.lang;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.jquantlib.QL;
import org.jquantlib.lang.reflect.TypeNode;
import org.jquantlib.lang.reflect.TypeTokenTree;
import org.junit.Test;

public class TypeTokenTreeTest {

    private final MyClass testClass;

    public TypeTokenTreeTest() {
        QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");

        // notice the usage of an anonymous class denoted by "{ }"
        this.testClass = new MyClass<HashMap<String, Double>, TreeMap<String, LinkedList<List<Double>>>, List<Integer>>() { };
    }

    @Test
    public void testFirstGenericParameter() {
        final TypeNode node = testClass.get(0);
        assertTrue("First generic parameter should be a HashMap", node.getElement().isAssignableFrom(HashMap.class));

        TypeNode subnode;
        subnode = testClass.get(node, 0);
        assertTrue("Inner first generic parameter should be a String", subnode.getElement().isAssignableFrom(String.class));

        subnode = testClass.get(node, 1);
        assertTrue("Inner second generic parameter should be a Double", subnode.getElement().isAssignableFrom(Double.class));
    }


    @Test
    public void testSecondGenericParameter() {
        final TypeNode node = testClass.get(1);
        assertTrue("First generic parameter should be a TreeMap", node.getElement().isAssignableFrom(TreeMap.class));

        TypeNode subnode;
        subnode = testClass.get(node, 0);
        assertTrue("Inner first generic parameter should be a String", subnode.getElement().isAssignableFrom(String.class));

        subnode = testClass.get(node, 1);
        assertTrue("Inner second generic parameter should be a LinkedList", subnode.getElement().isAssignableFrom(LinkedList.class));
        subnode = testClass.get(subnode, 0);
        assertTrue("Inner generic parameter should be a List", subnode.getElement().isAssignableFrom(List.class));
        subnode = testClass.get(subnode, 0);
        assertTrue("Inner generic parameter should be a Double", subnode.getElement().isAssignableFrom(Double.class));
    }

    @Test
    public void testThirdGenericParameter() {
        final TypeNode node = testClass.get(2);
        assertTrue("First generic parameter should be a List", node.getElement().isAssignableFrom(List.class));

        TypeNode subnode;
        subnode = testClass.get(node, 0);
        assertTrue("Inner first generic parameter should be a Integer", subnode.getElement().isAssignableFrom(Integer.class));
    }

    private class MyClass<X, Y, Z> {

        private final TypeNode root;

        public MyClass() {
            this.root = new TypeTokenTree(this.getClass()).getRoot();
        }

        public TypeNode get(final int index) {
            return root.get(index);
        }

        public TypeNode get(final TypeNode node, final int index) {
            return node.get(index);
        }

    }

}
