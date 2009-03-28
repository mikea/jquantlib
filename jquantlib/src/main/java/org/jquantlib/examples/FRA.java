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


/*!
 Copyright (C) 2006 Allen Kuo

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

/*  This example shows how to set up a term structure and price a simple
    forward-rate agreement.
*/


package org.jquantlib.examples;


import org.jquantlib.Configuration;
import org.jquantlib.Settings;
import org.jquantlib.indexes.Euribor;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;

public class FRA {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        System.out.println("\n\n::::: "+FRA.class.getSimpleName()+" :::::");

        /*********************
         ***  MARKET DATA  ***
         *********************/
        
        //FIXME: What kind of yieldtermstructure, how to initialize?
        RelinkableHandle<YieldTermStructure> euriborTermStructure = null;//new RelinkableHandle<YieldTermStructure>(new YieldTermStructure());
        Handle<IborIndex> euribor3m = new Handle<IborIndex>(new Euribor.Euribor365_3M(euriborTermStructure));
        
        Date todaysDate = DateFactory.getFactory().getDate(23, Month.MAY, 2006);
        Settings settings = Configuration.newConfiguration(null).newSettings();
        settings.setEvaluationDate(todaysDate);
        
        Calendar calendar = euribor3m.getLink().getFixingCalendar();
        int fixingDays = euribor3m.getLink().getFixingDays();
        Date settlementDate = calendar.advance(todaysDate, fixingDays, TimeUnit.MONTHS );
        
        System.out.println("Today: "+ todaysDate.getWeekday() + "," + todaysDate);
        
        System.out.println("Settlement date: " + settlementDate.getWeekday() + "," + settlementDate);
        
        // 3 month term FRA quotes (index refers to monthsToStart)
        
        
        
        
        

    }

}
