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

/* -*- mode: c++; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*- */

/*
 Copyright (C) 2006 Ferdinando Ametrano
 Copyright (C) 2001, 2002, 2003 Nicolas Di Césaré

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

/*! \file conjugategradient.hpp
    \brief Conjugate gradient optimization method
*/
package org.jquantlib.math.optimization;

import org.jquantlib.math.optimization.EndCriteria.CriteriaType;

//! Multi-dimensional Conjugate Gradient class.
/*! User has to provide line-search method and
    optimization end criteria.
    Search direction \f$ d_i = - f'(x_i) + c_i*d_{i-1} \f$
    where \f$ c_i = ||f'(x_i)||^2/||f'(x_{i-1})||^2 \f$
    and \f$ d_1 = - f'(x_1) \f$
*/

public class ConjugateGradient extends LineSearchBasedMethod {

    public ConjugateGradient(LineSearch lineSearch) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        if(lineSearch == null){
            
        }
    }

    @Override
    public CriteriaType minimize(Problem P, EndCriteria endCriteria) {
        // TODO Auto-generated method stub
        return null;
    }

}
