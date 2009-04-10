package org.jquantlib.math.statistics;

public class DiscrepancyStatistics {

    public DiscrepancyStatistics() {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }
}
