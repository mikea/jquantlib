package org.jquantlib.model.shortrate.calibrationhelpers;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.lang.annotation.Rate;
import org.jquantlib.lang.annotation.Time;
import org.jquantlib.model.CalibrationHelper;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;
import org.jquantlib.time.Schedule;
import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

public class CapHelper extends CalibrationHelper {
    
    public CapHelper(Period length, 
            Handle<Quote> volatility, 
            IborIndex index, 
            // data for ATM swap-rate calculation
            Frequency fixedLegFrequency,
            DayCounter fixedLegDayCounter,
            boolean includeFirstSwaplet,
            Handle<YieldTermStructure> termStructure){
        this(length, volatility, index, fixedLegFrequency, fixedLegDayCounter, 
                includeFirstSwaplet,termStructure, false);
    }
    
    public CapHelper(Period length, 
            Handle<Quote> volatility, 
            IborIndex index, 
            // data for ATM swap-rate calculation
            Frequency fixedLegFrequency,
            DayCounter fixedLegDayCounter,
            boolean includeFirstSwaplet,
            Handle<YieldTermStructure> termStructure,
            boolean calibrateVolatility){
        super(volatility, termStructure, calibrateVolatility);
       
        Period indexTenor = index.getTenor();
        double fixedRate = 0.04; //dummy value
        Date startDate, maturity;
        if(includeFirstSwaplet){
            startDate = termStructure.getLink().referenceDate();
            maturity = termStructure.getLink().referenceDate().increment(length);
        }
        else{
            startDate = termStructure.getLink().referenceDate().increment(indexTenor);
            maturity = termStructure.getLink().referenceDate().increment(length);
        }
        
        IborIndex dummyIndex = new IborIndex("dummy", 
                indexTenor, 
                index.getFixingDays(),
                index.fixingCalendar(),
                index.getCurrency(),
                index.getConvention(),
                index.isEndOfMonth(),
                termStructure.getLink().dayCounter(),
                termStructure);
                
        double [] nominals = {1,1.0};
        
        Schedule floatSchedule = new Schedule(startDate, maturity,
                index.getTenor(), index.fixingCalendar(), 
                index.getConvention(),
                index.getConvention(), false, false);
        
        /*
        Leg floatingLeg = new IborL
        
         Leg fixedLeg = FixedRateLeg(nominals,
                                    fixedSchedule,
                                    std::vector<Rate>(1, fixedRate),
                                    fixedLegDayCounter,
                                    index->businessDayConvention());

        boost::shared_ptr<Swap> swap(
            new Swap(termStructure, floatingLeg, fixedLeg));
        Rate fairRate = fixedRate - swap->NPV()/(swap->legBPS(1)/1.0e-4);
        engine_  = boost::shared_ptr<PricingEngine>();
        cap_ = boost::shared_ptr<Cap>(new Cap(floatingLeg,
                                              std::vector<Rate>(1, fairRate),
                                              termStructure, engine_));
        marketValue_ = blackPrice(volatility_->value());*/
        
        
    }

    @Override
    public void addTimesTo(ArrayList<Time> times) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public double blackPrice(double volatility) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double modelValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void addObserver(Observer observer) {
        delegatedObservable.addObserver(observer);
        
    }

    @Override
    public int countObservers() {
        return delegatedObservable.countObservers();
    }

    @Override
    public void deleteObserver(Observer observer) {
        delegatedObservable.deleteObservers();
        
    }

    @Override
    public void deleteObservers() {
        delegatedObservable.deleteObservers();
        
    }

    @Override
    public List<Observer> getObservers() {
        return delegatedObservable.getObservers();
    }

    @Override
    public void notifyObservers() {
        delegatedObservable.notifyObservers();
        
    }

    @Override
    public void notifyObservers(Object arg) {
        delegatedObservable.notifyObservers(arg);
        
    }

    @Override
    public void update(Observable o, Object arg) {
        
        
    }
    private Observable delegatedObservable = new DefaultObservable(this);

   

}
