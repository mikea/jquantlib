/*
Copyright (C) 
2009 Ueli Hofstetter

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

package org.jquantlib.model.equity;

import org.jquantlib.math.optimization.NoConstraint;
import org.jquantlib.math.optimization.PositiveConstraint;
import org.jquantlib.model.ConstantParameter;
import org.jquantlib.processes.HestonProcess;

/**
 * 
 * @author Ueli Hofstetter
 *
 */
public class BatesModel extends HestonModel {

    public BatesModel(HestonProcess process, double lambda, double nu, double delta) {
        super(process);
        arguments_.set(5, new ConstantParameter(nu, new NoConstraint()));
        arguments_.set(6, new ConstantParameter(delta, new PositiveConstraint()));
        arguments_.set(7, new ConstantParameter(lambda, new PositiveConstraint()));
    }
    public BatesModel(HestonProcess process) {
        this(process, 0.1, 0.0, 0.1); 
    }
    
    
    public double nu(){
        return arguments_.get(5).getOperatorEq(0.0);
    }
    public double delta(){
        return arguments_.get(6).getOperatorEq(0.0);
    }
    public double lambda(){
        return arguments_.get(7).getOperatorEq(0.0);
    }
    
    static class BatesDetJumpModel extends BatesModel{

        public BatesDetJumpModel(HestonProcess process) {
            this(process, 0.1, 0.0, 0.1, 1.0, 0.1);
        }
        
        public BatesDetJumpModel(HestonProcess process,
                double lambda,
                double nu,
                double delta,
                double kappaLambda,
                double thetaLambda) {
            super(process);
            arguments_.set(8, new ConstantParameter(kappaLambda, new PositiveConstraint()));
            arguments_.set(9,  new ConstantParameter(thetaLambda, new PositiveConstraint()));
        }
        
        public double kappaLambda(){
            return arguments_.get(8).getOperatorEq(0.0);
        }
        public double thethaLambda(){
            return arguments_.get(9).getOperatorEq(0.0);
        }
        
    }

}
