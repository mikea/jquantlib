/*
 Copyright (C) 2008 Daniel Kong
 
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

package org.jquantlib.examples;

import org.jquantlib.Configuration;
import org.jquantlib.time.AbstractCalendar;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;
import org.jquantlib.util.StopClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.jquantlib.util.Month.*;

/**
 * This example prices a few bermudan swaptions using different short-rate models calibrated to market swaptions.
 * 
 * @see http://quantlib.org/reference/_bermudan_swaption_8cpp-example.html
 * 
 * @author Daniel Kong
 */
//TODO: Work in progress
public class BermudanSwaption {
	
	private final static Logger logger = LoggerFactory.getLogger(BermudanSwaption.class);
	
	public BermudanSwaption(){
        if (System.getProperty("EXPERIMENTAL")==null) {
            throw new UnsupportedOperationException("Work in progress");
        }
		logger.info("\n\n::::: "+BermudanSwaption.class.getSimpleName()+" :::::");		
	}
	
	public void run() throws Exception{
		StopClock clock = new StopClock();
		clock.startClock();
		
		Date todaysDate = DateFactory.getFactory().getDate(15, FEBRUARY, 2002);
		
		Calendar calendar = new AbstractCalendar(){
			                    public String getName(){return "";}
			                    public boolean isWeekend(Weekday w){throw new UnsupportedOperationException();}};
		
		Date settlementDate = DateFactory.getFactory().getDate(19, FEBRUARY, 2002);
		Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(todaysDate);
		
		//TODO: Work in progress
		
		
		
		clock.stopClock();
		clock.log();
	}
	
	public static void main (String [] args){
		try{
			new BermudanSwaption().run();			
		}catch(Exception e){
			logger.info(e.getMessage());
		}
	}

}
