
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jquantlib.examples;

import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.math.randomnumbers.MersenneTwisterUniformRng;
import org.jquantlib.math.randomnumbers.PseudoRandom;
import org.jquantlib.math.randomnumbers.RandomNumberGenerator;
import org.jquantlib.math.randomnumbers.RandomSequenceGenerator;
import org.jquantlib.math.statistics.Statistics;
import org.jquantlib.methods.montecarlo.MonteCarloModel;
import org.jquantlib.methods.montecarlo.Path;
import org.jquantlib.methods.montecarlo.PathPricer;
import org.jquantlib.methods.montecarlo.SingleVariate;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultDate;
/**
 *
 * @author  Q. Boiler
 */

// FIXME :: This class needs code review :: http://bugs.jquantlib.org/view.php?id=221
public class DiscreteHedging {



	public static void main(String args[]){

	    if (System.getProperty("EXPERIMENTAL")==null) {
	        throw new UnsupportedOperationException("Work in progress");
	      }


    try {

        //boost::timer timer;
		long startTime = System.currentTimeMillis();
		
        /*@Time*/ Number maturity = new Double(1.0/12.0);   // 1 month
        /*@Price*/ Number strike = new Double(100);
        /*@Price*/ Number underlying = new Double(100);
        /*@Volatility*/ Number volatility = new Double(0.20); // 20%
        /*@Rate*/ Number riskFreeRate = new Double(0.05); // 5%
		Option.Type Call = Option.Type.CALL;
		
        ReplicationError rp = new ReplicationError(Call, maturity, strike, underlying, volatility, riskFreeRate);

        int scenarios = 50000;
        int hedgesNum;

        hedgesNum = 21;
        rp.compute(hedgesNum, scenarios);

        hedgesNum = 84;
        rp.compute(hedgesNum, scenarios);

        long seconds = (long)(System.currentTimeMillis() - startTime)/1000;
        Integer hours = new Integer((int)(seconds/3600));
        seconds -= hours * 3600;

        Integer minutes = new Integer((int)(seconds/60));
        seconds -= minutes * 60;
        System.out.print(" \nRun completed in ");

        if (hours > 0)
            System.out.print(String.valueOf(hours)+ " h ");
        if (hours > 0 || minutes > 0)
			System.out.print(String.valueOf(minutes)+" m ");

	    System.out.print(String.valueOf(seconds)+" s ");

    } catch (Exception e) {
		e.printStackTrace();
    } catch (Throwable t) {
		t.printStackTrace();
    }


	}

}

class ReplicationError {

    ReplicationError(final Option.Type type,
                     final /*@Time*/ Number maturity,
                     final /*@Price*/ Number strike,
                     final /*@Price*/ Number s0,
                     final /*@Volatility*/ Number sigma,
                     final /*@Rate*/ Number r) {
		maturity_ = maturity;
		s0_=s0;
		sigma_=sigma;
		r_=r;
	}


    private /*@Time*/ Number maturity_;
    private PlainVanillaPayoff payoff_;
    private /*@Price*/ Number s0_;
    private /*@Volatility*/ Number sigma_;
    private /*@Rate*/ Number r_;
    private /*@Real*/ Number vega_;

    void compute(int nTimeSteps, int nSamples){
		
    //QL_REQUIRE(nTimeSteps>0, "the number of steps must be > 0");

    // hedging interval
    // Time tau = maturity_ / nTimeSteps;

    // Black-Scholes framework: the underlying stock price evolves
    //   lognormally with a fixed known volatility that stays constant
    //   throughout time.
    //
    Calendar calendar = Target.getCalendar();
	
    Date today = DefaultDate.getTodaysDate();
	
    DayCounter dayCount = Actual365Fixed.getDayCounter();

    Handle<Quote> stateVariable = new Handle(new SimpleQuote(s0_.doubleValue()));
                          //boost::shared_ptr<Quote>(new SimpleQuote(s0_)));

    Handle<YieldTermStructure> riskFreeRate = new Handle(new FlatForward(today,r_.doubleValue(),dayCount));
	
	
    Handle<YieldTermStructure> dividendYield  = new Handle(new FlatForward(today, 0.0, dayCount));

    Handle<BlackVolTermStructure> volatility = new Handle(new BlackConstantVol(today, calendar, sigma_.doubleValue(), dayCount));

    StochasticProcess1D diffusion = 
                   new BlackScholesMertonProcess(stateVariable, dividendYield,
                                                 riskFreeRate, volatility);
	

    // Black Scholes equation rules the path generator:
    // at each step the log of the stock
    // will have drift and sigma^2 variance
//    PseudoRandom rsg =
//        PseudoRandom.make_sequence_generator(nTimeSteps, 0);


    
//  
// TODO: code review (to be solved by Richard)
//               
         
//	RandomSequenceGenerator<MersenneTwisterUniformRng> rsg = new PseudoRandom().makeSequenceGenerator(nTimeSteps, 0L); 
//	System.out.println("rsg: "+rsg);
	
    boolean brownianBridge = false;
/*
    typedef SingleVariate<PseudoRandom>::path_generator_type generator_type;
    boost::shared_ptr<generator_type> myPathGenerator(new
        generator_type(diffusion, maturity_, nTimeSteps,
                       rsg, brownianBridge));

    // The replication strategy's Profit&Loss is computed for each path
    // of the stock. The path pricer knows how to price a path using its
    // value() method
    boost::shared_ptr<PathPricer<Path> > myPathPricer(new
        ReplicationPathPricer(payoff_.optionType(), payoff_.strike(),
                              r_, maturity_, sigma_));

    // a statistics accumulator for the path-dependant Profit&Loss values
    Statistics statisticsAccumulator;

    // The Monte Carlo model generates paths using myPathGenerator
    // each path is priced using myPathPricer
    // prices will be accumulated into statisticsAccumulator
    MonteCarloModel<SingleVariate,PseudoRandom>
        MCSimulation(myPathGenerator,
                     myPathPricer,
                     statisticsAccumulator,
                     false);
 */
	
    MonteCarloModel<SingleVariate, RandomNumberGenerator, Statistics> MCSimulation
	    = new MonteCarloModel<SingleVariate, RandomNumberGenerator, Statistics>();

    // the model simulates nSamples paths
    MCSimulation.addSamples(nSamples);

    // the sampleAccumulator method
    // gives access to all the methods of statisticsAccumulator
	Statistics s = MCSimulation.sampleAccumulator();

    /*@Real*/ double PLMean  = s.mean();
    /*@Real*/ double PLStDev = MCSimulation.sampleAccumulator().standardDeviation();
    /*@Real*/ double PLSkew  = MCSimulation.sampleAccumulator().skewness();
    /*@Real*/ double PLKurt  = MCSimulation.sampleAccumulator().kurtosis();

    // Derman and Kamal's formula
    /*@Real*/ double theorStD = Math.sqrt((Math.PI/4/nTimeSteps)*vega_.doubleValue()*sigma_.doubleValue());


	StringBuffer sb = new StringBuffer();
	sb.append(nSamples).append(" | ");
	sb.append(nTimeSteps).append(" | ");
	sb.append(PLMean).append(" | ");
	sb.append(PLStDev).append(" | ");
	sb.append(theorStD).append(" | ");
	sb.append(PLSkew).append(" | ");
	sb.append(PLKurt).append(" \n");

	System.out.println(sb.toString());
    //std::cout << std::fixed
    //          << std::setw(8) << nSamples << " | "
    //          << std::setw(8) << nTimeSteps << " | "
    //          << std::setw(8) << std::setprecision(3) << PLMean << " | "
    //          << std::setw(8) << std::setprecision(2) << PLStDev << " | "
    //          << std::setw(12) << std::setprecision(2) << theorStD << " | "
    //          << std::setw(8) << std::setprecision(2) << PLSkew << " | "
    //          << std::setw(8) << std::setprecision(2) << PLKurt << std::endl;

	}
}


class ReplicationPathPricer extends PathPricer<Path> {

    ReplicationPathPricer(final Option.Type type,
                          double /*@Price @NonNegative*/ strike,
                          double /*@Rate @NonNegative*/ r,
                          double /*@Time @NonNegative*/ maturity,
                          double /*@Volatility @NonNegative*/ sigma){
		type_=type;
		strike_=strike;
		r_=r;
		maturity_=maturity;
		sigma_=sigma;

		// XXX: These tests can be substituted by [future] JSR-308 annotation @NonNegative
		assert(strike_>0.0);
		assert(r_>0.0);
		assert(maturity_>0.0);
		assert(sigma_>0.0);


	}

    // The value() method encapsulates the pricing code

    private Number operator(Path path){
        if (System.getProperty("EXPERIMENTAL")==null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        return null;
	}

  private Option.Type type_;
  private /*@Price*/ double strike_;
  private /*@Rate*/ double r_;
  private   /*@Time*/ double maturity_;
  private   /*@Volatility*/ double sigma_;

	@Override
	public Double evaluate(Path path) {
        if (System.getProperty("EXPERIMENTAL")==null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        return null;
	}
}



/*
class ReplicationError
{
public:
    ReplicationError(Option::Type type,
                     Time maturity,
                     Real strike,
                     Real s0,
                     Volatility sigma,
                     Rate r)
    : maturity_(maturity), payoff_(type, strike), s0_(s0),
      sigma_(sigma), r_(r) {

        // value of the option
        DiscountFactor rDiscount = std::exp(-r_*maturity_);
        DiscountFactor qDiscount = 1.0;
        Real forward = s0_*qDiscount/rDiscount;
        Real stdDev = std::sqrt(sigma_*sigma_*maturity_);
        boost::shared_ptr<StrikedTypePayoff> payoff(
                                             new PlainVanillaPayoff(payoff_));
        BlackCalculator black(payoff,forward,stdDev,rDiscount);
        std::cout << "Option value: " << black.value() << std::endl;

        // store option's vega, since Derman and Kamal's formula needs it
        vega_ = black.vega(maturity_);

        std::cout << std::endl;

        std::cout << std::setw(8) << " " << " | "
                  << std::setw(8) << " " << " | "
                  << std::setw(8) << "P&L" << " | "
                  << std::setw(8) << "P&L" << " | "
                  << std::setw(12) << "Derman&Kamal" << " | "
                  << std::setw(8) << "P&L" << " | "
                  << std::setw(8) << "P&L" << std::endl;

        std::cout << std::setw(8) << "samples" << " | "
                  << std::setw(8) << "trades" << " | "
                  << std::setw(8) << "mean" << " | "
                  << std::setw(8) << "std.dev." << " | "
                  << std::setw(12) << "formula" << " | "
                  << std::setw(8) << "skewness" << " | "
                  << std::setw(8) << "kurtosis" << std::endl;

        std::cout << std::string(78, '-') << std::endl;
    }

    // the actual replication error computation
    void compute(Size nTimeSteps, Size nSamples);
private:
    Time maturity_;
    PlainVanillaPayoff payoff_;
    Real s0_;
    Volatility sigma_;
    Rate r_;
    Real vega_;
};

// The key for the MonteCarlo simulation is to have a PathPricer that
// implements a value(const Path& path) method.
// This method prices the portfolio for each Path of the random variable
class ReplicationPathPricer : public PathPricer<Path> {
  public:
    // real constructor
    ReplicationPathPricer(Option::Type type,
                          Real strike,
                          Rate r,
                          Time maturity,
                          Volatility sigma)
    : type_(type), strike_(strike),
      r_(r), maturity_(maturity), sigma_(sigma) {
        QL_REQUIRE(strike_ > 0.0, "strike must be positive");
        QL_REQUIRE(r_ >= 0.0,
                   "risk free rate (r) must be positive or zero");
        QL_REQUIRE(maturity_ > 0.0, "maturity must be positive");
        QL_REQUIRE(sigma_ >= 0.0,
                   "volatility (sigma) must be positive or zero");

    }
    // The value() method encapsulates the pricing code
    Real operator()(const Path& path) const;

  private:
    Option::Type type_;
    Real strike_;
    Rate r_;
    Time maturity_;
    Volatility sigma_;
};


// Compute Replication Error as in the Derman and Kamal's research note
int main(int, char* []) {

    try {

        boost::timer timer;
        std::cout << std::endl;

        Time maturity = 1.0/12.0;   // 1 month
        Real strike = 100;
        Real underlying = 100;
        Volatility volatility = 0.20; // 20%
        Rate riskFreeRate = 0.05; // 5%
        ReplicationError rp(Option::Call, maturity, strike, underlying,
                volatility, riskFreeRate);

        Size scenarios = 50000;
        Size hedgesNum;

        hedgesNum = 21;
        rp.compute(hedgesNum, scenarios);

        hedgesNum = 84;
        rp.compute(hedgesNum, scenarios);

        Real seconds = timer.elapsed();
        Integer hours = int(seconds/3600);
        seconds -= hours * 3600;
        Integer minutes = int(seconds/60);
        seconds -= minutes * 60;
        std::cout << " \nRun completed in ";
        if (hours > 0)
            std::cout << hours << " h ";
        if (hours > 0 || minutes > 0)
            std::cout << minutes << " m ";
        std::cout << std::fixed << std::setprecision(0)
                  << seconds << " s\n" << std::endl;

        return 0;
    } catch (std::exception& e) {
        std::cout << e.what() << std::endl;
        return 1;
    } catch (...) {
        std::cout << "unknown error" << std::endl;
        return 1;
    }
}


/// The actual computation of the Profit&Loss for each single path.
//
//   In each scenario N rehedging trades spaced evenly in time over
//   the life of the option are carried out, using the Black-Scholes
//   hedge ratio.
Real ReplicationPathPricer::operator()(const Path& path) const {

    Size n = path.length()-1;
    QL_REQUIRE(n>0, "the path cannot be empty");

    // discrete hedging interval
    Time dt = maturity_/n;

    // For simplicity, we assume the stock pays no dividends.
    Rate stockDividendYield = 0.0;

    // let's start
    Time t = 0;

    // stock value at t=0
    Real stock = path.front();

    // money account at t=0
    Real money_account = 0.0;

    // option fair price (Black-Scholes) at t=0
    DiscountFactor rDiscount = std::exp(-r_*maturity_);
    DiscountFactor qDiscount = std::exp(-stockDividendYield*maturity_);
    Real forward = stock*qDiscount/rDiscount;
    Real stdDev = std::sqrt(sigma_*sigma_*maturity_);
    boost::shared_ptr<StrikedTypePayoff> payoff(
                                       new PlainVanillaPayoff(type_,strike_));
    BlackCalculator black(payoff,forward,stdDev,rDiscount);
    // sell the option, cash in its premium
    money_account += black.value();
    // compute delta
    Real delta = black.delta(stock);
    // delta-hedge the option buying stock
    Real stockAmount = delta;
    money_account -= stockAmount*stock;

    /  **********************************  /
    /  *** hedging during option life ***  /
    /  **********************************  /
    for (Size step = 0; step < n-1; step++){

        // time flows
        t += dt;

        // accruing on the money account
        money_account *= std::exp( r_*dt );

        // stock growth:
        stock = path[step+1];

        // recalculate option value at the current stock value,
        // and the current time to maturity
        rDiscount = std::exp(-r_*(maturity_-t));
        qDiscount = std::exp(-stockDividendYield*(maturity_-t));
        forward = stock*qDiscount/rDiscount;
        stdDev = std::sqrt(sigma_*sigma_*(maturity_-t));
        BlackCalculator black(payoff,forward,stdDev,rDiscount);

        // recalculate delta
        delta = black.delta(stock);

        // re-hedging
        money_account -= (delta - stockAmount)*stock;
        stockAmount = delta;
    }

    /  ************************* /
    /  *** option expiration *** /
    /  ************************* /
    // last accrual on my money account
    money_account *= std::exp( r_*dt );
    // last stock growth
    stock = path[n];

    // the hedger delivers the option payoff to the option holder
    Real optionPayoff = PlainVanillaPayoff(type_, strike_)(stock);
    money_account -= optionPayoff;

    // and unwinds the hedge selling his stock position
    money_account += stockAmount*stock;

    // final Profit&Loss
    return money_account;
}


// The computation over nSamples paths of the P&L distribution
void ReplicationError::compute(Size nTimeSteps, Size nSamples)
{
    QL_REQUIRE(nTimeSteps>0, "the number of steps must be > 0");

    // hedging interval
    // Time tau = maturity_ / nTimeSteps;

    // Black-Scholes framework: the underlying stock price evolves
    //   lognormally with a fixed known volatility that stays constant
    //   throughout time.
    //
    Calendar calendar = TARGET();
    Date today = Date::todaysDate();
    DayCounter dayCount = Actual365Fixed();
    Handle<Quote> stateVariable(
                          boost::shared_ptr<Quote>(new SimpleQuote(s0_)));
    Handle<YieldTermStructure> riskFree(
                          boost::shared_ptr<YieldTermStructure>(
                                      new FlatForward(today, r_, dayCount)));
    Handle<YieldTermStructure> dividendYield(
                          boost::shared_ptr<YieldTermStructure>(
                                      new FlatForward(today, 0.0, dayCount)));
    Handle<BlackVolTermStructure> volatility(
                          boost::shared_ptr<BlackVolTermStructure>(
                               new BlackConstantVol(today, calendar, sigma_, dayCount)));
    boost::shared_ptr<StochasticProcess1D> diffusion(
                   new BlackScholesMertonProcess(stateVariable, dividendYield,
                                                 riskFreeRate, volatility));

    // Black Scholes equation rules the path generator:
    // at each step the log of the stock
    // will have drift and sigma^2 variance
    PseudoRandom::rsg_type rsg =
        PseudoRandom::make_sequence_generator(nTimeSteps, 0);

    bool brownianBridge = false;

    typedef SingleVariate<PseudoRandom>::path_generator_type generator_type;
    boost::shared_ptr<generator_type> myPathGenerator(new
        generator_type(diffusion, maturity_, nTimeSteps,
                       rsg, brownianBridge));

    // The replication strategy's Profit&Loss is computed for each path
    // of the stock. The path pricer knows how to price a path using its
    // value() method
    boost::shared_ptr<PathPricer<Path> > myPathPricer(new
        ReplicationPathPricer(payoff_.optionType(), payoff_.strike(),
                              r_, maturity_, sigma_));

    // a statistics accumulator for the path-dependant Profit&Loss values
    Statistics statisticsAccumulator;

    // The Monte Carlo model generates paths using myPathGenerator
    // each path is priced using myPathPricer
    // prices will be accumulated into statisticsAccumulator
    MonteCarloModel<SingleVariate,PseudoRandom>
        MCSimulation(myPathGenerator,
                     myPathPricer,
                     statisticsAccumulator,
                     false);

    // the model simulates nSamples paths
    MCSimulation.addSamples(nSamples);

    // the sampleAccumulator method
    // gives access to all the methods of statisticsAccumulator
    Real PLMean  = MCSimulation.sampleAccumulator().mean();
    Real PLStDev = MCSimulation.sampleAccumulator().standardDeviation();
    Real PLSkew  = MCSimulation.sampleAccumulator().skewness();
    Real PLKurt  = MCSimulation.sampleAccumulator().kurtosis();

    // Derman and Kamal's formula
    Real theorStD = std::sqrt(M_PI/4/nTimeSteps)*vega_*sigma_;


    std::cout << std::fixed
              << std::setw(8) << nSamples << " | "
              << std::setw(8) << nTimeSteps << " | "
              << std::setw(8) << std::setprecision(3) << PLMean << " | "
              << std::setw(8) << std::setprecision(2) << PLStDev << " | "
              << std::setw(12) << std::setprecision(2) << theorStD << " | "
              << std::setw(8) << std::setprecision(2) << PLSkew << " | "
              << std::setw(8) << std::setprecision(2) << PLKurt << std::endl;
}
 */