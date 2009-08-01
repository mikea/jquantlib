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
package org.jquantlib.testsuite.currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jquantlib.Currency;
import org.jquantlib.currencies.Europe.CHFCurrency;
import org.jquantlib.currencies.Europe.EURCurrency;
import org.jquantlib.math.Rounding;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CurrencyTest {
    
    private final static Logger logger = LoggerFactory.getLogger(CurrencyTest.class);
    
    public CurrencyTest() {
        logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
    }
    
    @Test
    public void testCurrencies(){
        logger.info("testing currencies...");
        //Sample Currency - CHF
        CHFCurrency chf = new CHFCurrency();
        
        logger.info("testing correct initialization...");
        assertTrue(chf.name().equalsIgnoreCase("Swiss franc"));
        assertTrue(chf.code().equalsIgnoreCase("CHF"));
        assertEquals(chf.numericCode(),756);
        assertTrue(chf.symbol().equalsIgnoreCase("SwF"));
        assertTrue(chf.fractionSymbol().equalsIgnoreCase(""));
        assertEquals(chf.fractionsPerUnit(),100);
        assertEquals(chf.rounding().type(), Rounding.Type.None);
        //Note: the initialization of the triangulated currency is a little bit suspicious...
        assertTrue(chf.triangulationCurrency().getClass() == Currency.class);    
        assertTrue(chf.triangulationCurrency().empty());
        logger.info("testing overloaded operators....(only class based)");
        EURCurrency euro = new EURCurrency();
        CHFCurrency chf2 = new CHFCurrency();
        assertFalse(euro.equals(chf));
        assertTrue(euro.notEquals(chf));
        assertFalse(chf2.notEquals(chf));
        assertTrue(chf2.equals(chf));
        
    }
    
    //Note: the initialization of the triangulated currency is a little bit suspicious...data_ not initialized!!
    @Test(expected = NullPointerException.class)
    public void testLeakyCurrencyInitialization(){
        CHFCurrency chf = new CHFCurrency();
        chf.triangulationCurrency().code();
    }
    
    

}
