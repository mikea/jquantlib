package org.jquantlib.util.stdlibc;

import java.util.NoSuchElementException;

/**
 * @see <a href="http://javadude.com/articles/passbyvalue.htm">Java is Pass-by-Value, Dammit!</a>
 * 
 * @author Richard Gomes
 */
public final class Iterators {
    
    //
    // static public methods  ::: unmutable iterators
    //
    
    static public final IntForwardIterator forwardIterator(final int[] list) {
        return new IntForwardIteratorImpl(list);
    }

    static public final IntReverseIterator reverseIterator(final int[] list) {
        return new IntReverseIteratorImpl(list);
    }

    static public final DoubleForwardIterator forwardIterator(final double[] list) {
        return new DoubleForwardIteratorImpl(list);
    }

    static public final DoubleReverseIterator reverseIterator(final double[] list) {
        return new DoubleReverseIteratorImpl(list);
    }

    static public final ObjectForwardIterator forwardIterator(final Object[] list) {
        return new ObjectForwardIteratorImpl(list);
    }

    
    //
    // static public methods  ::: mutable iterators
    //
    
    static public final MutableIntForwardIterator mutableForwardIterator(final int[] list) {
        return new MutableIntForwardIteratorImpl(list);
    }

    static public final MutableIntReverseIterator mutableReverseIterator(final int[] list) {
        return new MutableIntReverseIteratorImpl(list);
    }

    static public final MutableDoubleForwardIterator mutableForwardIterator(final double[] list) {
        return new MutableDoubleForwardIteratorImpl(list);
    }

    static public final MutableDoubleReverseIterator mutableReverseIterator(final double[] list) {
        return new MutableDoubleReverseIteratorImpl(list);
    }

    

    // 
    // static private abstract inner classes
    //
    
    static private abstract class ForwardIteratorImpl implements ForwardIterator {
        private final int size;
        protected int index;
        
        protected ForwardIteratorImpl(final int size) {
            this.size = size;
            this.index = 0;
        }
        
        @Override
        public boolean hasNext() {
            return (index < size);
        }

        @Override
        public int skip(final int n) {
            final int jump = (index+n < size) ? n : (size - index -1);
            index += jump;
            return jump;
        }
        
    }

    
    static private abstract class ReverseIteratorImpl implements ReverseIterator {
        protected int index;
        
        protected ReverseIteratorImpl(final int size) {
            this.index = size-1;
        }
        
        @Override
        public boolean hasPrevious() {
            return (index >= 0);
        }

        @Override
        public int back(final int n) {
            final int jump = (index-n >= 0) ? n : index;
            index -= jump;
            return jump;
        }
        
    }

    
    // 
    // static private inner classes  ::: These classes implement unmutable iterators
    //
    
    static private class IntForwardIteratorImpl extends ForwardIteratorImpl implements IntForwardIterator {
        
        protected int[] list;
        
        public IntForwardIteratorImpl(final int[] list) {
            super((list==null) ? 0 : list.length);
            this.list = list;
        }
        
        
        @Override
        public int next() throws NoSuchElementException {
            if (hasNext()) return list[index++];
            throw new NoSuchElementException();
        }
        
    }
    

    static private class IntReverseIteratorImpl extends ReverseIteratorImpl implements IntReverseIterator {
        
        protected int[] list;
        
        public IntReverseIteratorImpl(final int[] list) {
            super((list==null) ? 0 : list.length);
            this.list = list;
        }
        
        @Override
        public int previous() throws NoSuchElementException {
            if (hasPrevious()) return list[index++];
            throw new NoSuchElementException();
        }

    }

    static private class DoubleForwardIteratorImpl extends ForwardIteratorImpl implements DoubleForwardIterator {
        
        protected double[] list;
        
        public DoubleForwardIteratorImpl(final double[] list) {
            super((list==null) ? 0 : list.length);
            this.list = list;
        }
        
        @Override
        public double next() throws NoSuchElementException {
            if (hasNext()) return list[index++];
            throw new NoSuchElementException();
        }

    }
    

    static private class DoubleReverseIteratorImpl extends ReverseIteratorImpl implements DoubleReverseIterator {
        
        protected double[] list;
        
        public DoubleReverseIteratorImpl(final double[] list) {
            super((list==null) ? 0 : list.length);
            this.list = list;
        }
        
        
        @Override
        public double previous() throws NoSuchElementException {
            if (hasPrevious()) return list[index++];
            throw new NoSuchElementException();
        }

    }


    static private class ObjectForwardIteratorImpl extends ForwardIteratorImpl implements ObjectForwardIterator {
        
        protected Object[] list;
        
        public ObjectForwardIteratorImpl(final Object[] list) {
            super((list==null) ? 0 : list.length);
            this.list = list;
        }
        
        
        @Override
        public Object next() throws NoSuchElementException {
            if (hasNext()) return list[index++]; // FIXME :: make a clone
            throw new NoSuchElementException();
        }

    }
    


    // 
    // static private inner classes  ::: These classes implement mutable iterators
    //
    

    static private class MutableIntForwardIteratorImpl extends IntForwardIteratorImpl implements MutableIntForwardIterator {
        
        public MutableIntForwardIteratorImpl(final int[] list) {
            super(list);
        }
        
        @Override
        public IntReference nextReference() throws NoSuchElementException {
            return new IntReference(list, index);
        }
        
    }
    

    static private class MutableIntReverseIteratorImpl extends IntReverseIteratorImpl implements MutableIntReverseIterator {
        
        public MutableIntReverseIteratorImpl(final int[] list) {
            super(list);
        }
        
        @Override
        public IntReference previousReference() throws NoSuchElementException {
            return new IntReference(list, index);
        }
        
    }
    
    static private class MutableDoubleForwardIteratorImpl extends DoubleForwardIteratorImpl implements MutableDoubleForwardIterator {
        
        public MutableDoubleForwardIteratorImpl(final double[] list) {
            super(list);
        }
        
        @Override
        public DoubleReference nextReference() throws NoSuchElementException {
            return new DoubleReference(list, index);
        }
        
    }
    

    static private class MutableDoubleReverseIteratorImpl extends DoubleReverseIteratorImpl implements MutableDoubleReverseIterator {
        
        public MutableDoubleReverseIteratorImpl(final double[] list) {
            super(list);
        }
        
        @Override
        public DoubleReference previousReference() throws NoSuchElementException {
            return new DoubleReference(list, index);
        }
        
    }
    


}
