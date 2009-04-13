package org.jquantlib.math.statistics;

public class ConvergenceStatistics {
    
    private IStatistics statistics;
    
    public ConvergenceStatistics(IStatistics statisitics){
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.statistics = statistics;
    }
    
    public void add(){
        throw new UnsupportedOperationException("Work in progress");
        /*
        #ifndef __DOXYGEN__
        template <class T, class U>
        void ConvergenceStatistics<T,U>::add(
                     const typename ConvergenceStatistics<T,U>::value_type& value,
                     Real weight) {
            T::add(value,weight);
            if (this->samples() == nextSampleSize_) {
                table_.push_back(std::make_pair(this->samples(),this->mean()));
                nextSampleSize_ = samplingRule_.nextSamples(nextSampleSize_);
            }
        }
        */
    }
/*
    public void reset(){
        
    }
    

      template <class T, class U>
      void ConvergenceStatistics<T,U>::reset() {
          T::reset();
          nextSampleSize_ = samplingRule_.initialSamples();
          table_.clear();
      }

      template <class T, class U>
      const typename ConvergenceStatistics<T,U>::table_type&
      ConvergenceStatistics<T,U>::convergenceTable() const {
          return table_;
      }

  }

//TODO: Implement this one!
/*
class DoublingConvergenceSteps {
    public:
      Size initialSamples() const { return 1; }
      Size nextSamples(Size current) { return 2 * current + 1; }
  };

  //! statistics class with convergence table
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
/*
  template <class T, class U = DoublingConvergenceSteps>
  class ConvergenceStatistics : public T {
    public:
      typedef typename T::value_type value_type;
      typedef std::vector<std::pair<Size,value_type> > table_type;
      ConvergenceStatistics(const T& stats,
                            const U& rule = U());
      ConvergenceStatistics(const U& rule = U());
      void add(const value_type& value, Real weight = 1.0);
      template <class DataIterator>
      void addSequence(DataIterator begin, DataIterator end) {
          for (; begin != end; ++begin)
              add(*begin);
      }
      template <class DataIterator, class WeightIterator>
      void addSequence(DataIterator begin, DataIterator end,
                       WeightIterator wbegin) {
          for (; begin != end; ++begin, ++wbegin)
              add(*begin,*wbegin);
      }
      void reset();
      const std::vector<std::pair<Size,value_type> >& convergenceTable()
                                                                      const;
    private:
      table_type table_;
      U samplingRule_;
      Size nextSampleSize_;
  };
*/

}
