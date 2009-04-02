/*
 Copyright (C) 2009 Ueli Hofstetter

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
package org.jquantlib.math.optimization;

import java.util.ArrayList;
import java.util.List;

import org.joda.primitives.list.impl.ArrayDoubleList;
import org.jquantlib.math.Array;
import org.jquantlib.math.optimization.EndCriteria.CriteriaType;

//! Levenberg-Marquardt optimization method
/*! This implementation is based on MINPACK
    (<http://www.netlib.org/minpack>,
    <http://www.netlib.org/cephes/linalg.tgz>)
*/

public class LevenbergMarquardt extends OptimizationMethod {
    
    private double epsfcn_, xtol_, gtol_;
    private Integer info_;
    
    public LevenbergMarquardt() {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.epsfcn_ = 1.0e-8;
        this.xtol_ = 1.0e-8;
        this.gtol_ = 1.0e-8;
    }
    
    public LevenbergMarquardt(double epsfcn, double xtol, double gtol){
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.epsfcn_ = epsfcn;
        this.xtol_ = xtol;
        this.gtol_ = gtol;
        
    }

    @Override
    public CriteriaType minimize(Problem P, EndCriteria endCriteria) {
        EndCriteria.CriteriaType ecType = EndCriteria.CriteriaType.None;
        P.reset();
        Array x_ = P.currentValue();
        // TODO: this is probably incorrect, check the consequences
        ProblemData.getProblemData().setProblem(P);
        ProblemData.getProblemData().setInitCostValues(P.costFunction().values(x_));
        
        int m = ProblemData.getProblemData().initCostValues_.size();
        int n = x_.size();
        
        Array  xx = new Array();
        //TODO: correct?
        xx.operatorAddCopy(x_);
        
        Array fvec;
        Array diag;
        
        int mode = 1;
        double factor = 1;
        int nprint = 0;
        int info = 0;
        int nfev = 0;
        
        Array fjac = new Array();
        int ldfjac = m;
        
        //TODO: to be completed....
        /*
        boost::scoped_array<int> ipvt(new int[n]);
        boost::scoped_array<double> qtf(new double[n]);
        boost::scoped_array<double> wa1(new double[n]);
        boost::scoped_array<double> wa2(new double[n]);
        boost::scoped_array<double> wa3(new double[n]);
        boost::scoped_array<double> wa4(new double[m]);
        // call lmdif to minimize the sum of the squares of m functions
        // in n variables by the Levenberg-Marquardt algorithm.
        QuantLib::MINPACK::lmdif(m, n, xx.get(), fvec.get(),
                                 static_cast<double>(endCriteria.functionEpsilon()),
                                 static_cast<double>(xtol_),
                                 static_cast<double>(gtol_),
                                 static_cast<int>(endCriteria.maxIterations()),
                                 static_cast<double>(epsfcn_),
                                 diag.get(), mode, factor,
                                 nprint, &info, &nfev, fjac.get(),
                                 ldfjac, ipvt.get(), qtf.get(),
                                 wa1.get(), wa2.get(), wa3.get(), wa4.get());
        info_ = info;
        // check requirements & endCriteria evaluation
        QL_REQUIRE(info != 0, "MINPACK: improper input parameters");
        //QL_REQUIRE(info != 6, "MINPACK: ftol is too small. no further "
        //                               "reduction in the sum of squares "
        //                               "is possible.");
        if (info != 6) ecType = QuantLib::EndCriteria::StationaryFunctionValue;
        //QL_REQUIRE(info != 5, "MINPACK: number of calls to fcn has "
        //                               "reached or exceeded maxfev.");
        endCriteria.checkMaxIterations(nfev, ecType);
        QL_REQUIRE(info != 7, "MINPACK: xtol is too small. no further "
                                       "improvement in the approximate "
                                       "solution x is possible.");
        QL_REQUIRE(info != 8, "MINPACK: gtol is too small. fvec is "
                                       "orthogonal to the columns of the "
                                       "jacobian to machine precision.");
        // set problem
        std::copy(xx.get(), xx.get()+n, x_.begin());
        P.setCurrentValue(x_);

        return ecType;
        */
        return ecType;
    }
    
    public void fcn(int x1, int n, double x2, double fvec, int x3) {
        /*
        void LevenbergMarquardt::fcn(int, int n, double* x, double* fvec, int*) {
        Array xt(n);
        std::copy(x, x+n, xt.begin());
        // constraint handling needs some improvement in the future:
        // starting point should not be close to a constraint violation
        if (ProblemData::instance().problem()->constraint().test(xt)) {
            const Array& tmp = ProblemData::instance().problem()->values(xt);
            std::copy(tmp.begin(), tmp.end(), fvec);
        } else {
            std::copy(ProblemData::instance().initCostValues().begin(),
                      ProblemData::instance().initCostValues().end(), fvec);
        }
        */
    }
    
    // TODO: this class is no longer used in newer releases, it seems there's a 
    // better approach to do this... to be investigated...
    // class is needed to make the Levenberg-Marquardt
    // algorithm sessionId() safe (or multi threading safe).
    /*
    class ProblemData : public Singleton<ProblemData> {
      public:
        Problem* & problem() { return thisP_; }
        Array& initCostValues()    { return initCostValues_; }
      private:
        Problem* thisP_;
        Array initCostValues_;
    };
    */
    static class ProblemData{
        
        static ProblemData p = null;
        public static ProblemData getProblemData(){
            if(p == null){
                p = new ProblemData();
            }
            return p;
        }
        
        private ProblemData(){}
        
        public Problem problem(){
            return thisP_;
        }
        public Array initCostValues(){
            return initCostValues_;
        }
        public void setProblem(Problem problem){
             this.thisP_ = problem;
        }
        public void setInitCostValues(Array initCostValues){
            this.initCostValues_ = initCostValues;
        }
        
        
        private Problem thisP_;
        private Array initCostValues_;
        
    }
}
