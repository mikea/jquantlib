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

	//
    // private fields
    //

    private final Handle<YieldTermStructure> originalCurve;
	private final Handle<Quote> spread;
	private final Compounding comp;
	private final Frequency freq;


	//
	// public constructors
	//

	public ZeroSpreadedTermStructure(
	        final Handle<YieldTermStructure> h,
			final Handle<Quote> spread, final Compounding comp , final Frequency freq,
			final DayCounter dc){
		this.originalCurve = h;
		this.spread = spread;
		this.comp = comp;
		this.freq = freq;

        registerWith(this.originalCurve);
        registerWith(this.spread);
	}


	//
	// public methods
	//

	public double forwardImpl(final double t){
        return originalCurve.getLink().
        forwardRate(t, t, comp, freq, true).rate()
        + spread.getLink().op();
    }


	//
	// overrides ZeroYieldStructure
	//

	@Override
	protected double zeroYieldImpl(final double t) {
		//org.comment: to be fixed: user-defined daycounter should be used
		final InterestRate zeroRate = originalCurve.getLink().
		zeroRate(t, comp, freq, true);
		final InterestRate spreadedRate =
			new InterestRate(zeroRate.rate() + spread.getLink().op(),
				zeroRate.dayCounter(),
				zeroRate.compounding(),
				zeroRate.frequency());
		return spreadedRate.equivalentRate(t, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
	}


	//
	// overrides TermStructure
	//

    @Override
    public Calendar calendar() {
        return originalCurve.getLink().calendar();
    }

    @Override
    public Date referenceDate() {
        return originalCurve.getLink().referenceDate();
    }

    @Override
    public Date maxDate() {
        return originalCurve.getLink().maxDate();
    }

    @Override
    public/* @Time */double maxTime() {
        return originalCurve.getLink().maxTime();
    }

}