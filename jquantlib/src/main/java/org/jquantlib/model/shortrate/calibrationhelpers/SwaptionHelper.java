package org.jquantlib.model.shortrate.calibrationhelpers;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.lang.annotation.Time;
import org.jquantlib.model.CalibrationHelper;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

public class SwaptionHelper extends CalibrationHelper {

    public SwaptionHelper(Handle<Quote> volatility, Handle<YieldTermStructure> termStructure, boolean calibrateVolatility) {
        super(volatility, termStructure, calibrateVolatility);
        // TODO Auto-generated constructor stub
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
    public double calibrationError() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double impliedVolatility(double targetValue, double accuracy, int maxEvaluations, double minVol, double maxVol) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double modelValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addObserver(Observer observer) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int countObservers() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void deleteObserver(Observer observer) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteObservers() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Observer> getObservers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void notifyObservers() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void notifyObservers(Object arg) {
        // TODO Auto-generated method stub
        
    }

}
