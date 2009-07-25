package org.jquantlib.cashflow;

import java.util.ArrayList;

public class CashFlowVectors {
    
    public static class Leg extends ArrayList<CashFlow>{}
    
    //NOTE: two implementations, one in cpp and one in hpp with different constructors
    public static class FloatingLeg extends Leg{
        public FloatingLeg(String aLongSignature){
            
        }
        
        public FloatingLeg(String aLongSignature, String aLongSignature2){
            
        }
    }
    
    public static class FloatingDigitalLeg extends Leg{
        
    }
    
    public static class FixedRateLeg extends Leg{
        
    }
    
    public static class IborLeg extends Leg{
        
    }
    
    public static class CmsLeg extends Leg{
        
    }
    
    public static class FloatingZeroLeg extends Leg{
        
    }
    
    public static class IborZeroLeg extends Leg{
        
    }
    
    public static class CmsZeroLeg extends Leg{
        
    }
    
    public static class RangeAccrualLeg extends Leg{
        
    }
    


}
