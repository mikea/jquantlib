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
 Copyright (C) 2003, 2004 Neil Firth
 Copyright (C) 2003, 2004 Ferdinando Ametrano
 Copyright (C) 2003, 2004, 2007 StatPro Italia srl

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

package org.jquantlib.pricingengines.barrier;

import org.jquantlib.pricingengines.GenericEngine;
import org.jquantlib.pricingengines.arguments.BarrierOptionArguments;
import org.jquantlib.pricingengines.results.OneAssetOptionResults;

/**
 * 
 * Ported from 
 * <ul>
 * <li>ql/instruments/barrieroption.hpp</li>
 * <li>ql/instruments/barrieroption.cpp</li>
 * </ul>
 * 
 * @author <Richard Gomes>
 *
 */
@SuppressWarnings("PMD.AbstractNaming")
public abstract class BarrierOptionEngine  extends GenericEngine<BarrierOptionArguments, OneAssetOptionResults>{

	
	protected BarrierOptionEngine() {
		super(new BarrierOptionArguments(), new OneAssetOptionResults());
	}

	@Override
	abstract public void calculate();

}
