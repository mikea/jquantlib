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
package org.jquantlib.testsuite.money;

import static org.junit.Assert.*;

import org.jquantlib.currencies.Currency;
import org.jquantlib.currencies.ExchangeRate;
import org.jquantlib.currencies.ExchangeRateManager;
import org.jquantlib.currencies.Money;
import org.jquantlib.currencies.America.USDCurrency;
import org.jquantlib.currencies.Europe.EURCurrency;
import org.jquantlib.currencies.Europe.GBPCurrency;
import org.jquantlib.math.Closeness;
import org.jquantlib.math.Rounding;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoneyTest {
    private final static Logger logger = LoggerFactory.getLogger(MoneyTest.class);
   
    public static void main(String [] args){
        MoneyTest m = new MoneyTest();
        m.testBaseCurrency();
        m.testNone();
    }
    
    public MoneyTest() {
        logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
        logger.info("see testsuite.money.cpp/hpp");
    }
    
    @Test
    public void testBaseCurrency(){
        logger.info("Testing money arithmetic with conversion to base currency...");

        Currency EUR = new EURCurrency();
        Currency GBP = new GBPCurrency();
        Currency USD = new USDCurrency();

        Money m1 = Money.multiple(50000.0,GBP);
        Money m2 = Money.multiple(100000.0 , EUR);
        Money m3 = Money.multiple(500000.0 , USD);
        
        
        ExchangeRateManager.getInstance().clear();
        ExchangeRate eur_usd = new  ExchangeRate(EUR, USD, 1.2042);
        ExchangeRate eur_gbp = new ExchangeRate(EUR, GBP, 0.6612);
        ExchangeRateManager.getInstance().add(eur_usd);
        ExchangeRateManager.getInstance().add(eur_gbp);
        
        
        Money.conversionType = Money.ConversionType.BaseCurrencyConversion;
        Money.baseCurrency = EUR;
      
        //divided the steps for tracing...
        Money calculated0 = m1.mul(3.0);
        Money calculated1 = (m2.mul(2.5));
        Money calculated2 = m3.div(5.0);
       
        Money calculated3 = calculated0.add(calculated1).sub(calculated2);
        
        logger.info("Calculated value: " + calculated3.value());
        
      
        Rounding round = Money.baseCurrency.rounding();
        /*Decimal*/double x = round.operator(m1.value()*3.0/eur_gbp.rate()) + 2.5*m2.value() - 
        round.operator(m3.value()/(5.0*eur_usd.rate()));
        logger.info("Expected value: " + x);
        
        Money expected = new Money(x, EUR);
        
        assertTrue(Closeness.isClose(calculated3.value(),expected.value()));
        if(!calculated3.equals(expected)){
            fail("Wrong result: \n"
                    + "    expected:   " + expected + "\n"
                    + "    calculated: " + calculated3);
        }
        logger.info("testBaseCurrency done!");
    }

    @Test
    public void testNone() {
        logger.info("Testing money arithmetic without conversions...");
        Currency EUR = new EURCurrency();
        Money m1 = Money.multiple(50000.0, EUR);
        Money m2 = Money.multiple(100000.0, EUR);
        Money m3 = Money.multiple(500000.0, EUR);

        Money.conversionType = Money.ConversionType.NoConversion;

        //divided the steps for tracing...
        Money calculated0 = m1.mul(3.0);
        Money calculated1 = (m2.mul(2.5));
        Money calculated2 = m3.div(5.0);
       
        Money calculated3 = calculated0.add(calculated1).sub(calculated2);
        
        logger.info("Calculated value: " + calculated3.value());
        
        /*Decimal*/double x =  m1.value()*3.0 + 2.5*m2.value() - m3.value()/5.0;
        logger.info("Expected value: " + x);
        
        Money expected = new Money(x, EUR);

        if(!calculated3.equals(expected)){
            fail("Wrong result: \n"
                    + "    expected:   " + expected + "\n"
                    + "    calculated: " + calculated3);
        }
        logger.info("testNone done!");
    }
}
