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

import org.jquantlib.util.StopClock;

/**
 * This example prices a few bermudan swaptions using different short-rate models calibrated to market swaptions.
 * 
 * @see http://quantlib.org/reference/_bermudan_swaption_8cpp-example.html
 * 
 * @author Daniel Kong
 */
public class BermudanSwaption {
	
	public BermudanSwaption(){
		System.out.println("\n\n::::: "+BermudanSwaption.class.getSimpleName()+" :::::");		
	}
	
	public void run(){
		StopClock clock = new StopClock();
		clock.startClock();
		
		//TODO: working..
		
		
		
		clock.stopClock();
		clock.log();
	}
	
	public static void main (String [] args){
		new BermudanSwaption().run();
	}

}
