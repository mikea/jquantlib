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

import org.jquantlib.instruments.Option;
import org.jquantlib.util.StopClock;

/**
 * This example evaluates convertible bond prices.
 * 
 * @see http://quantlib.org/reference/_convertible_bonds_8cpp-example.html
 * 
 * @author Daniel Kong
 */

public class ConvertibleBonds {
	
	public ConvertibleBonds(){
		System.out.println("\n\n::::: "+ConvertibleBonds.class.getSimpleName()+" :::::");		
	}

	public void run(){
		StopClock clock = new StopClock();
		clock.startClock();
		
		Option.Type type = Option.Type.PUT;
		
		double underlying = 36.0;
        double spreadRate = 0.005;

        double dividendYield = 0.02;
        double riskFreeRate = 0.06;
        double volatility = 0.20;

        Integer settlementDays = 3;
        Integer length = 5;
        double redemption = 100.0;
        double conversionRatio = redemption/underlying; 
        
        
        //TODO: working
        
        
        
        
		
		clock.stopClock();
		clock.log();
	}
	
	public static void main (String [] args){
		new BermudanSwaption().run();
	}
	
}
