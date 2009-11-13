package org.jquantlib.samples;

public class Repo implements Runnable {

    public static void main(final String[] args) {
        new Repo().run();
    }

    public void run() {
        throw new UnsupportedOperationException("Work in progress");
        // QL.info("::::: " + this.getClass().getSimpleName() + " :::::");
    }

}
