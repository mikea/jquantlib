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

import org.jquantlib.math.optimization.BoundaryConstraint;
import org.jquantlib.math.optimization.PositiveConstraint;
import org.jquantlib.model.ConstantParameter;
import org.jquantlib.processes.HestonProcess;

/**
 * 
 * @author Ueli Hofstetter
 *
 */
public class BatesDoubleExpModel extends HestonModel {

    public BatesDoubleExpModel(HestonProcess process) {
        this(process, 0.1, 0.1, 0.1, 0.5);
    }

    public BatesDoubleExpModel(HestonProcess process, double lambda, double nuUp, double nuDown, double p) {
        super(process);
        arguments_.set(5, new ConstantParameter(p, new BoundaryConstraint(0.0, 1.0)));
        arguments_.set(6, new ConstantParameter(nuDown, new PositiveConstraint()));
        arguments_.set(7, new ConstantParameter(nuUp, new PositiveConstraint()));
        arguments_.set(8, new ConstantParameter(lambda, new PositiveConstraint()));
        
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
    }

    public double p() {
        return arguments_.get(5).getOperatorEq(0.0);
    }

    public double nuDown() {
        return arguments_.get(6).getOperatorEq(0.0);
    }

    public double nuUp() {
        return arguments_.get(7).getOperatorEq(0.0);
    }

    public double lambda() {
        return arguments_.get(8).getOperatorEq(0.0);
    }

    public static class BatesDoubleExpDetJumpModel extends BatesDoubleExpModel {
        public BatesDoubleExpDetJumpModel(HestonProcess process, double lambda, double nuUp, double nuDown, double p,
                double kappaLambda, double thetaLambda) {
            super(process);
            arguments_.set(9, new ConstantParameter(kappaLambda, new PositiveConstraint()));
            arguments_.set(10, new ConstantParameter(thetaLambda, new PositiveConstraint()));

        }

        public BatesDoubleExpDetJumpModel(HestonProcess process) {
            this(process, 0.1, 0.1, 0.1, 0.5, 1.0, 0.1);
        }

        public double kappaLambda() {
            return arguments_.get(9).getOperatorEq(0.0);
        }

        public double thetaLambda() {
            return arguments_.get(10).getOperatorEq(0.0);
        }
    };

}
