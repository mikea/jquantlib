package org.jquantlib.testsuite.termstructures.volatilities;

import junit.framework.TestCase;

import org.jquantlib.termstructures.volatilities.Sabr;
import org.junit.Test;


/**
 * @author <Richard Gomes>
 */
public class SabrTest extends TestCase {

	@Test
	public void testAgainstKnownValues() {
		
		double strike = 0.0398;
        double forward = 0.0398;
        double expiryTime = 5.0;
        double alpha = 0.0305473;
        double beta = 0.5;
        double nu = 0.34;
        double rho = -0.11;
        
        Sabr sabr = new Sabr();
        double sabrVol = sabr.sabrVolatility(strike, forward, expiryTime, alpha, beta, nu, rho);
        assertEquals(0.16,sabrVol, 1.0e-6);
        
        strike = 0.0598;
        sabrVol = sabr.sabrVolatility(strike, forward, expiryTime, alpha, beta, nu, rho);
        assertEquals(0.15755519,sabrVol, 1.0e-6);
        
        strike = 0.0198;
        sabrVol = sabr.sabrVolatility(strike, forward, expiryTime, alpha, beta, nu, rho);
        assertEquals(0.2373848,sabrVol, 1.0e-6);
    
	}
}
