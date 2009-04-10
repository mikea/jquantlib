package org.jquantlib.math.statistics;

public class IncrementalStatistics {
    
    public IncrementalStatistics(){
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

}
