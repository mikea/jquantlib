package org.jquantlib.instruments;

import java.util.Comparator;

import org.jquantlib.cashflow.CashFlow;
import org.jquantlib.util.Date;

public class EarlierThanCashFlowComparator implements Comparator<CashFlow> {
	
	/**
	 * Compares its two arguments for order. 
	 * Returns a negative integer, zero, or a positive integer as the 
	 * first argument is less than, equal to, or greater than the second.
	 */
	@Override
	public int compare(CashFlow o1, CashFlow o2) {
		if(o1.date().le(o2.date())){
			return -1;
		}
		if(o2.date().le(o1.date())){
			return 1;
		}
		return 0;
	}
	

}
