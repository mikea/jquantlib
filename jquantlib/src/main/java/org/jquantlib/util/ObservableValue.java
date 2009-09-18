package org.jquantlib.util;

import java.util.List;


public class ObservableValue<T> implements Observable {

    //
    // private fields
    //

    private T value;


    //
    // public constructors
    //

    public ObservableValue(final T value) {
        this.value = value;
    }

    public ObservableValue(final ObservableValue<T> observable) {
        this.value = observable.value;
    }


    //
    // public methods
    //

    public void assign(final T value) {
        this.value = value;
        delegatedObservable.notifyObservers();
    }

    public void assign(final ObservableValue<T> observable) {
        this.value = observable.value;
        delegatedObservable.notifyObservers();
    }

    public T value() {
        return value;
    }


    //
    // implements Observable
    //

    /**
     * Implements multiple inheritance via delegate pattern to an inner class
     */
    private final Observable delegatedObservable = new DefaultObservable(this);

    @Override
    public void addObserver(final Observer observer) {
        delegatedObservable.addObserver(observer);
    }

    @Override
    public int countObservers() {
        return delegatedObservable.countObservers();
    }

    @Override
    public void deleteObserver(final Observer observer) {
        delegatedObservable.deleteObserver(observer);
    }

    @Override
    public void notifyObservers() {
        delegatedObservable.notifyObservers();
    }

    @Override
    public void notifyObservers(final Object arg) {
        delegatedObservable.notifyObservers(arg);
    }

    @Override
    public void deleteObservers() {
        delegatedObservable.deleteObservers();
    }

    @Override
    public List<Observer> getObservers() {
        return delegatedObservable.getObservers();
    }

}
