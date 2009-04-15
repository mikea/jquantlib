package org.jquantlib;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.termstructures.TermStructure;
import org.jquantlib.termstructures.volatilities.SmileSection;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Period;
import org.jquantlib.util.Date;
import org.jquantlib.util.Pair;

public abstract class SwaptionVolatilityStructure extends TermStructure {

    private BusinessDayConvention bdc_;

    public SwaptionVolatilityStructure(DayCounter dc, BusinessDayConvention bdc) {
        super(dc);
        this.bdc_ = bdc;
    }

    public SwaptionVolatilityStructure(Date referenceDate, Calendar calendar, DayCounter dc, BusinessDayConvention bdc) {
        super(referenceDate, calendar, dc);
        this.bdc_ = bdc;
    }

    public SwaptionVolatilityStructure(int settlementDays, Calendar calendar, DayCounter dc, BusinessDayConvention bdc) {
        super(settlementDays, calendar, dc);
        this.bdc_ = bdc;
    }

    // ! returns the volatility for a given option time and swapLength

    public double volatility(double optionTime, double swapLength, double strike) {
        return volatility(optionTime, swapLength, strike, false);
    }

    // ! returns the Black variance for a given option time and swapLength
    public abstract double blackVariance(double optionTime, double swapLength, double strike, boolean extrapolate);

    public double blackVariance(double optionTime, double swapLength, double strike) {

        return blackVariance(optionTime, swapLength, strike, false);
    }

    //FIXME:.....
    // overloaded (at least) in SwaptionVolCube2
    /*
     * public SmileSection smileSection( Date optionDate, Period swapTenor) {
     * 
     * } Pair<Double, Double> p = null;//convertDates(optionDate, swapTenor); return smileSectionImpl(p.first, p.second); }
     */
    
    // ! returns the volatility for a given option tenor and swap tenor
    public double volatility(Period optionTenor, Period swapTenor, double strike) {
        return volatility(optionTenor, swapTenor, strike, false);
    }

    // ! returns the Black variance for a given option tenor and swap tenor

    public double blackVariance(Period optionTenor, Period swapTenor, double strike) {
        return blackVariance(optionTenor, swapTenor, strike, false);
    }

    // @}
    // ! \name Limits
    // @{
    // ! the largest length for which the term structure can return vols
    public abstract Period maxSwapTenor();

    // ! the largest swapLength for which the term structure can return vols
    // ! the minimum strike for which the term structure can return vols
    public abstract double minStrike();

    // ! the maximum strike for which the term structure can return vols
    public abstract double maxStrike();

    // @}

    // ! the business day convention used for option date calculation
    public abstract BusinessDayConvention businessDayConvention();

    // ! implements the conversion between optionTenors and optionDates
    // public abstract Date optionDateFromTenor( Period optionTenor);

    // ! return smile section
    protected abstract SmileSection smileSectionImpl(double optionTime, double swapLength);

    protected abstract SmileSection smileSectionImpl(Date optionDate, Period swapTenor);

    // ! implements the actual volatility calculation in derived classes
    public abstract double volatilityImpl(double optionTime, double swapLength, double strike);

    protected double volatilityImpl(Date optionDate, Period swapTenor, double strike) {
        Pair<Double, Double> p = convertDates(optionDate, swapTenor);
        return volatilityImpl(p.getFirst(), p.getSecond(), strike);
    }

    public Date optionDateFromTenor(Period optionTenor) {
        return calendar().advance(referenceDate(), optionTenor, businessDayConvention());
    }

    public double volatility(double optionTime, double swapLength, double strike, boolean extrapolate) {
        checkRange(optionTime, swapLength, strike, extrapolate);
        return volatilityImpl(optionTime, swapLength, strike);
    }

    public double blackVariance(double optionTime, double swapLength, double strike, Boolean extrapolate) {
        checkRange(optionTime, swapLength, strike, extrapolate);
        double vol = volatilityImpl(optionTime, swapLength, strike);
        return vol * vol * optionTime;
    }

    public double volatility(Date optionDate, Period swapTenor, double strike, boolean extrapolate) {
        checkRange(optionDate, swapTenor, strike, extrapolate);
        return volatilityImpl(optionDate, swapTenor, strike);
    }

    public double blackVariance(Date optionDate, Period swapTenor, double strike, boolean extrapolate) {
        double vol = volatility(optionDate, swapTenor, strike, extrapolate);
        Pair<Double, Double> p = convertDates(optionDate, swapTenor);
        return vol * vol * p.getFirst();
    }

    public double volatility(Period optionTenor, Period swapTenor, double strike, boolean extrapolate) {
        Date optionDate = optionDateFromTenor(optionTenor);
        return volatility(optionDate, swapTenor, strike, extrapolate);
    }

    public double blackVariance(Period optionTenor, Period swapTenor, double strike, boolean extrapolate) {
        Date optionDate = optionDateFromTenor(optionTenor);
        double vol = volatility(optionDate, swapTenor, strike, extrapolate);
        Pair<Double, Double> p = convertDates(optionDate, swapTenor);
        return vol * vol * p.getFirst();
    }

    public SmileSection smileSection(Period optionTenor, Period swapTenor) {
        Date optionDate = optionDateFromTenor(optionTenor);
        return smileSectionImpl(optionDate, swapTenor);
    }

    public void checkRange(double optionTime, double swapLength, double k, boolean extrapolate) {
        super.checkRange(optionTime, extrapolate);
        if (swapLength < 0.0) {
            throw new IllegalArgumentException("negative swapLength (" + swapLength + ") given");
        }
        if (!extrapolate && !allowsExtrapolation() && swapLength > maxSwapLength()) {
            throw new IllegalArgumentException("swapLength (" + swapLength + ") is past max curve swapLength (" + maxSwapLength()
                    + ")");
        }
        if (!extrapolate && !allowsExtrapolation() && (k < minStrike() || k > maxStrike())) {
            throw new IllegalArgumentException("strike (" + k + ") is outside the curve domain [" + minStrike() + "," + maxStrike()
                    + "]");
        }
    }

    public double maxSwapLength() {
        return timeFromReference(referenceDate().increment(maxSwapTenor()));
    }

    public Pair<Double, Double> convertDates(Date optionDate, Period swapTenor) {
        Date end = optionDate.increment(swapTenor);
        if (end.le(optionDate)) {
            throw new IllegalArgumentException("negative swap tenor (" + swapTenor + ") given");
        }
        double optionTime = timeFromReference(optionDate);
        double timeLength = dayCounter().yearFraction(optionDate, end);
        return new Pair<Double, Double>(optionTime, timeLength);
    }

    protected void checkRange(Date optionDate, Period swapTenor, double k, boolean extrapolate) {
        super.checkRange(timeFromReference(optionDate), extrapolate);
        if (swapTenor.length() <= 0) {
            throw new IllegalArgumentException("negative swap tenor (" + swapTenor + ") given");
        }
        if (!extrapolate && !allowsExtrapolation() && swapTenor.gt(maxSwapTenor())) {
            throw new IllegalArgumentException("swap tenor (" + swapTenor + ") is past max tenor (" + maxSwapTenor() + ")");
        }
        //TODO: review
        if (!extrapolate && !allowsExtrapolation() && (k >= minStrike() && k <= maxStrike())) {
            throw new IllegalArgumentException("strike (" + k + ") is outside the curve domain [" + minStrike() + "," + maxStrike()
                    + "]");
        }

    }
}
