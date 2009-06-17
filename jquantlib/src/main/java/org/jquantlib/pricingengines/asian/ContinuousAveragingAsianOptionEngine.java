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
 Copyright (C) 2003, 2004 Ferdinando Ametrano
 Copyright (C) 2004, 2007 StatPro Italia srl

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

package org.jquantlib.pricingengines.asian;

import org.jquantlib.pricingengines.GenericEngine;
import org.jquantlib.pricingengines.arguments.ContinuousAveragingAsianOptionArguments;
import org.jquantlib.pricingengines.arguments.DiscreteAveragingAsianOptionArguments;
import org.jquantlib.pricingengines.results.OneAssetOptionResults;

/**
 * Asian option on a single asset
 * <p>
 * Description of the terms and conditions of a continuous average out fixed strike option.
 * 
 * @author <Richard Gomes>
 */
@SuppressWarnings("PMD.AbstractNaming")
abstract public class ContinuousAveragingAsianOptionEngine extends
        GenericEngine<ContinuousAveragingAsianOptionArguments, OneAssetOptionResults> {

    /**
     * Extra arguments for single-asset continuous-average Asian option
     */
    protected ContinuousAveragingAsianOptionEngine() {
        super(new ContinuousAveragingAsianOptionArguments(), new OneAssetOptionResults());
    }

}
