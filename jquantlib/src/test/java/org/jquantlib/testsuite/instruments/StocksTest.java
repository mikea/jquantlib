package org.jquantlib.testsuite.instruments;

import static org.junit.Assert.fail;

import org.jquantlib.instruments.Instrument;
import org.jquantlib.instruments.Stock;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.testsuite.util.Flag;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StocksTest {
    private final static Logger logger = LoggerFactory.getLogger(IntrumentsTest.class);

    public StocksTest() {
        logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
    }

    @Test
    public void testStocks() {
        logger.info("Testing price valuation of Stocks...");

        final double iniPrice = 3.56;
        final double newPrice = iniPrice*1.0214; // changed +2.14%

        // define Stock
        final RelinkableHandle<Quote> h = new RelinkableHandle<Quote>(new SimpleQuote(iniPrice));
        final Instrument s = new Stock(h);

        // attach an Observer to Stock
        final Flag priceChange = new Flag();
        s.addObserver(priceChange);

        // verify initial price
        if (iniPrice != s.getNPV()) {
            fail("stock quote valuation failed");
        }

        // set a new price
        h.setLink(new SimpleQuote(newPrice));

        // Observer must detect price change
        if (!priceChange.isUp()) {
            fail("Observer was not notified of instrument change");
        }

        if (newPrice != s.getNPV()) {
            fail("stock quote havent changed value");
        }

    }


}