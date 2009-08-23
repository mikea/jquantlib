package org.jquantlib.lang.iterators;

public interface RandomAccessDouble {

    public abstract double first();

    public abstract double last();

    public abstract int size();

    public abstract double get(int offset);

    public abstract void set(int offset, double value);

}