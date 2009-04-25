package org.jquantlib.cashflow;

import java.util.List;

import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.stdlibc.Std;

/**
 * 
 * @author goovy
 * 
 * WORK IN PROGRESS
 *
 */

public class CashFlows {
    
    private static final String not_enough_information_available = "not enough information available";
    private static final String no_cashflows = "no cashflows";
    private static double basisPoint_ = 1.0e-4;
    
    
   //! %duration type
    enum Duration { Simple, Macaulay, Modified };
    
    private CashFlows(){};
    
    public static Date startDate(final List<CashFlow> cashflows){
        Date d = DateFactory.getFactory().getMaxDate();
        for (int i=0; i<cashflows.size(); ++i) {
            Coupon c = (Coupon)cashflows.get(i);
            if(c!=null){
                d = Std.min(c.accrualStartDate(), d);
            }
        }
        if(d==DateFactory.getFactory().getMaxDate()){
            throw new IllegalArgumentException(not_enough_information_available);
        }
        return d;
    }
    
    public static Date maturityDate(final List<CashFlow> cashflows){
        Date d = DateFactory.getFactory().getMinDate();
        for(int i = 0; i<cashflows.size(); i++){
            d=Std.max(d, cashflows.get(i).date());
        }
        if(d==DateFactory.getFactory().getMinDate()){
            throw new IllegalArgumentException(no_cashflows);
        }
        return d;
    }
}
