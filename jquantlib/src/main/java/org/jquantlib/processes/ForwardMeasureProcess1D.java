package org.jquantlib.processes;

public abstract class ForwardMeasureProcess1D extends StochasticProcess1D {
    
  //! forward-measure 1-D stochastic process
    /*! 1-D stochastic process whose dynamics are expressed in the
        forward measure.

        \ingroup processes
    */
    
    protected double T_;
    
    public ForwardMeasureProcess1D(){
    }
    
    protected  ForwardMeasureProcess1D(double T){
        this.T_ = T;
    }

    //TODO: why linear discretization?
    protected ForwardMeasureProcess1D(LinearDiscretization disc){
        super(disc);
    }
    
    public void setForwardMeasureTime(double T){
        T_ = T;
        notifyObservers();
    }

}
