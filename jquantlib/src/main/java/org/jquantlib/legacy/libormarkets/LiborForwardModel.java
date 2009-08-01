package org.jquantlib.legacy.libormarkets;

import java.util.List;

import org.jquantlib.instruments.Option.Type;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.model.AffineModel;
import org.jquantlib.model.CalibratedModel;

//! %Libor forward model
/*! References:

Stefan Weber, 2005, Efficient Calibration for Libor Market Models,
(<http://workshop.mathfinance.de/2005/papers/weber/slides.pdf>)

Damiano Brigo, Fabio Mercurio, Massimo Morini, 2003,
Different Covariance Parameterizations of Libor Market Model and Joint
Caps/Swaptions Calibration,
(<http://www.business.uts.edu.au/qfrc/conferences/qmf2001/Brigo_D.pdf>

\test the correctness is tested using Monte-Carlo Simulation to
      reproduce swaption npvs, model calibration and exact cap pricing
*/
public class LiborForwardModel extends CalibratedModel implements AffineModel {

    //protected Array w_0(int alpha, int beta){};

    protected List<Double> f_;
    protected List</*@Time*/Double> accrualPeriod_;

    //protected final LfmCovarianceProxy covarProxy_;
    //protected final LiborForwardModelProcess  process_;

    //protected SwaptionVolatilityMatrix  swaptionVola;
    
    
    
    
    public LiborForwardModel() {
        //TODO: Code review :: incomplete code
        if (true)
            throw new UnsupportedOperationException("Work in progress");
    }
    
    
    @Override
    public double discount(double t) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double discountBond(double now, double maturity, Array factors) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double discountBondOption(Type type, double strike, double maturity, double bondMaturity) {
        // TODO Auto-generated method stub
        return 0;
    }

}
