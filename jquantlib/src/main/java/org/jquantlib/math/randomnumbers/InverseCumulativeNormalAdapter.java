package org.jquantlib.math.randomnumbers;

import org.jquantlib.math.distributions.InverseCumulativeNormal;

public class InverseCumulativeNormalAdapter implements InverseCumulative {

    private InverseCumulativeNormal delegate;
    
    private InverseCumulativeNormalAdapter() {
        this.delegate = new InverseCumulativeNormal();
    }
    
    @Override
    public Double evaluate(Double x) {
        return delegate.evaluate(x);
    }
    

}
