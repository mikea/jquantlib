/*
 Copyright (C) 2009 Zahid Hussain

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

package org.jquantlib.time;

import org.jquantlib.QL;
import org.jquantlib.lang.exceptions.LibraryException;


/**
 * @author Zahid Hussain
 */

public class DateGeneration {
	
    public static enum Rule {
        Backward,       /*!< Backward from termination date to
                             effective date. */
        Forward,        /*!< Forward from effective date to
                             termination date. */
        Zero,           /*!< No intermediate dates between effective date
                             and termination date. */
        ThirdWednesday, /*!< All dates but effective date and termination
                             date are taken to be on the third wednesday
                             of their month (with forward calculation.) */
        Twentieth,      /*!< All dates but the effective date are
                             taken to be the twentieth of their
                             month (used for CDS schedules in
                             emerging markets.)  The termination
                             date is also modified. */
        TwentiethIMM    /*!< All dates but the effective date are
                             taken to be the twentieth of an IMM
                             month (used for CDS schedules.)  The
                             termination date is also modified. */
    }
    
    public String toString(DateGeneration.Rule r) {
        switch (r) {
	        case Backward:
	        case Forward:
	          return "Forward";
	        case Zero:
	          return "Zero";
	        case ThirdWednesday:
	          return "ThirdWednesday";
	        case Twentieth:
	          return "Twentieth";
	        case TwentiethIMM:
	          return "TwentiethIMM";
	        default:
	          QL.error("unknown DateGeneration.Rule (" + r.name() + ")");
	          throw new LibraryException("Unknown DateGeneration.Rule");
      } 
    }
}
