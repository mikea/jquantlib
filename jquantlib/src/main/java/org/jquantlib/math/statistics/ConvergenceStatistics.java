package org.jquantlib.math.statistics;

import java.util.List;

import org.jquantlib.util.Pair;


/*! This class decorates another statistics class adding a
convergence table calculation. The table tracks the
convergence of the mean.

It is possible to specify the number of samples at which the
mean should be stored by mean of the second template
parameter; the default is to store \f$ 2^{n-1} \f$ samples at
the \f$ n \f$-th step. Any passed class must implement the
following interface:
\code
Size initialSamples() const;
Size nextSamples(Size currentSamples) const;
\endcode
as well as a copy constructor.

\test results are tested against known good values.
*/
public class ConvergenceStatistics {
    
    private IStatistics statistics;
    private /*samplingRule*/ DoublingConvergenceSteps samplingRule_; 
    private List<Pair<Integer,Double>> table_;
    private int nextSampleSize_;
    
    private int sampleSize;
    
    public ConvergenceStatistics(IStatistics T, DoublingConvergenceSteps rule){
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.statistics = T;
        this.samplingRule_ = rule;
        reset();
    }
    
    public ConvergenceStatistics(DoublingConvergenceSteps rule){
        this.samplingRule_ = rule;
        reset();
    }
    
    public void add(IStatistics stats, double weight){
        this.statistics.add(stats, weight);
        if(this.statistics.samples() == nextSampleSize_){
            table_.add(new Pair<Integer, Double>(statistics.samples(), statistics.mean()));
            nextSampleSize_ = samplingRule_.nextSamples(nextSampleSize_);
        }
    }
    
    public void reset(){
        statistics.reset();
        nextSampleSize_ = samplingRule_.initialSamples();
        table_.clear();
    }
    
    public List<Pair<Integer,Double>> convergenceTable(){
        return table_;
    }
    
    class DoublingConvergenceSteps {
         public int initialSamples() { return 1; }
         public int nextSamples(int current) { return 2 * current + 1; }
      };
    


}
