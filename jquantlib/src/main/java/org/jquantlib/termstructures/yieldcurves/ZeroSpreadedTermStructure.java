package org.jquantlib.termstructures.yieldcurves;


import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;

public class ZeroSpreadedTermStructure extends ZeroYieldStructure  {
	
	private Handle<YieldTermStructure> originalCurve_;
	private Handle<Quote> spread_;
	private Compounding comp_;
	private Frequency freq_;

	public ZeroSpreadedTermStructure(final Handle<YieldTermStructure> h, 
			final Handle<Quote> spread, Compounding comp , Frequency freq,
			final DayCounter dc){
		this.originalCurve_ = h;
		this.spread_ = spread;
		this.comp_ = comp;
		this.freq_ = freq;
		//registerWith(originalCurve_);
	    //registerWith(spread_);
	}
	
	@Override
	protected double zeroYieldImpl(double t) {
		//org.comment: to be fixed: user-defined daycounter should be used
		InterestRate zeroRate = originalCurve_.getLink().
		zeroRate(t, comp_, freq_, true);
		InterestRate spreadedRate = 
			new InterestRate(zeroRate.rate() + spread_.getLink().evaluate(), 
				zeroRate.dayCounter(),
				zeroRate.compounding(),
				zeroRate.frequency());
		return spreadedRate.equivalentRate(t, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
	}
	
	public double forwardImpl(final double t){
		return originalCurve_.getLink().
		forwardRate(t, t, comp_, freq_, true).rate()
		+ spread_.getLink().evaluate();
	}
	
	@Override
	public Calendar calendar(){
		return originalCurve_.getLink().calendar();
	}
	
	@Override
	public Date referenceDate(){
		return originalCurve_.getLink().referenceDate();
	}
	
	@Override
	public Date maxDate(){
		return originalCurve_.getLink().maxDate();
	}
	
	@Override
	public /*@Time*/ double maxTime(){
		return originalCurve_.getLink().maxTime();
	}
}