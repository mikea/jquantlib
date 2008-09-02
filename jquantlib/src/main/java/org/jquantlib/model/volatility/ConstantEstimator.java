/*
 Copyright (C) 2008 Richard Gomes
 
 This source code is release under the BSD License.
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

/*
 Copyright (C) 2006 Joseph Wang

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
*/

package org.jquantlib.model.volatility;

import org.jquantlib.util.Date;
import org.jquantlib.util.TimeSeries;

/**
 * Constant-estimator volatility model
 * <p>
 * Volatilities are assumed to be expressed on an annual basis.
 * 
 * @author Richard Gomes
 */
//TODO : Test cases
public class ConstantEstimator implements VolatilityCompositor {

    private /*@NonNegative*/ int size_;
    
    public ConstantEstimator(final /*@NonNegative*/ int size) {
        this.size_ = size;
    }
    
    @Override
    public void calibrate(final TimeSeries</*@Volatility*/ Double> timeSeries) {
        // nothing
    }

    
    //FIXME: PERFORMANCE:: We should use (maybe!) a specialized TimeSeries backed by a double[] instead of a Double[]
    @Override
    public TimeSeries</*@Volatility*/ Double> calculate(final TimeSeries</*@Volatility*/ Double> volatilitySeries) {
        final Date[] dates = volatilitySeries.dates();
        final /*@Volatility*/ Double[] values = volatilitySeries.values();
        TimeSeries</*@Volatility*/ Double> retval = new TimeSeries</*@Volatility*/ Double>();
        
        for (int i=size_; i < volatilitySeries.size(); i++) {
            double sumu2 = 0.0, sumu = 0.0;
            for (int j = i-size_; j < i; j++) {
                double uj = values[j];
                sumu += uj;
                sumu2 += uj*uj;
            }
            double dsize = (double) size_;
            double s = Math.sqrt(sumu2/dsize - sumu*sumu / dsize / (dsize+1));
            retval.add(dates[i], s);
        }
        return retval;
    }

}