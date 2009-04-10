package org.jquantlib.math.statistics;

public class SequenceStatistics {
    public SequenceStatistics(){
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }
}
