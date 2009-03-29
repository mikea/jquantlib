package org.jquantlib.examples;

public class Repo {
    
    public static void main(String[] args) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

}
