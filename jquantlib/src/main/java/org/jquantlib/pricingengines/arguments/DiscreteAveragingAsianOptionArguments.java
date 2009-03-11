/*
 Copyright (C) 2007 Richard Gomes

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

package org.jquantlib.pricingengines.arguments;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.instruments.AverageType;
import org.jquantlib.util.Date;

/**
 * Description of the terms and conditions of a discrete average out fixed strike
 * option.
 * 
 * @author <Richard Gomes>
 */
public class DiscreteAveragingAsianOptionArguments extends OneAssetOptionArguments {
    
	private final static double NULLREAL = Double.MAX_VALUE;
	private final static int NULLSIZE = Integer.MAX_VALUE;
	public DiscreteAveragingAsianOptionArguments() {
        //averageType(Average::Type(-1)),
		averageType = AverageType.Arithmetic;//FIXME check this default...
        runningAccumulator = NULLREAL;//FIXME is there central values?
        pastFixings = NULLSIZE;//FIXME is there central values?
        fixingDates = new ArrayList<Date>();
		
	}
	
	@Override
	public void validate() /*/@ReadOnly*/{
        super.validate();
        
        //FIXME what is the convention enum = -1;???

        //QL_REQUIRE(Integer(averageType) != -1, "unspecified average type");
        if (averageType==null) 
        	throw new IllegalArgumentException("unspecified average type");
        
        //QL_REQUIRE(pastFixings != Null<Size>(), "null past-fixing number");
        if (pastFixings==NULLSIZE) throw new IllegalArgumentException("null past-fixing number");
        
        //QL_REQUIRE(runningAccumulator != Null<Real>(), "null running product");
        if (runningAccumulator==NULLREAL) throw new IllegalArgumentException("null running product");
        
        switch (averageType) {
            case Arithmetic:
                //QL_REQUIRE(runningAccumulator >= 0.0,
                //           "non negative running sum required: "
                //           << runningAccumulator << " not allowed");
                if (runningAccumulator>=0.0){
                	throw new IllegalArgumentException("non negative running sum required: "
                            	+ runningAccumulator + " not allowed");
                }
                break;
            case Geometric:
                //QL_REQUIRE(runningAccumulator > 0.0,
               //            "positive running product required: "
               //            << runningAccumulator << " not allowed");
               if (!(runningAccumulator>0.0)){
            	   throw new IllegalArgumentException("positive running product required: "
            			   	+ runningAccumulator + " not allowed");
               }
            	break;
            default:
                throw new IllegalArgumentException("invalid average type");
        }

	}
	//TODO encapsulate?
	public AverageType averageType;
	public /*Real*/ double runningAccumulator;
	public /*Size*/ int pastFixings;
	public List<Date> fixingDates;


}
