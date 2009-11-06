/*
 Copyright (C) 2007 Richard Gomes

 This source code is release under the BSD License.

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

/*
 Copyright (C) 2003 Ferdinando Ametrano
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2007 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */

package org.jquantlib.instruments;

import org.jquantlib.QL;
import org.jquantlib.Settings;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.lang.reflect.ReflectConstants;
import org.jquantlib.pricingengines.GenericEngine;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.time.Date;
import org.jquantlib.util.Observer;

/**
 * Base class for options on a single asset
 *
 * @author Richard Gomes
 */
public class OneAssetOption extends Option {


    //
    // private fields
    //

    // results
    private double delta;
    private double deltaForward;
    private double elasticity;
    private double gamma;
    private double theta;
    private double thetaPerDay;
    private double vega;
    private double rho;
    private double dividendRho;
    private double itmCashProbability;


    //
    // public constructors
    //

    public OneAssetOption(
            final Payoff payoff,
            final Exercise exercise) {
        super(payoff, exercise);
    }

    //
    // public methods
    //

    public double delta() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(delta) , "delta not provided"); // QA:[RG]::verified // TODO: message
        return delta;
    }

    public double deltaForward() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(deltaForward) , "forward delta not provided"); // QA:[RG]::verified // TODO: message
        return deltaForward;
    }

    public double elasticity() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(elasticity) , "elasticity not provided"); // QA:[RG]::verified // TODO: message
        return elasticity;
    }

    public double gamma() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(gamma) , "gamma not provided"); // QA:[RG]::verified // TODO: message
        return gamma;
    }

    public double theta() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(theta) , "theta not provided"); // QA:[RG]::verified // TODO: message
        return theta;
    }

    public double thetaPerDay() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(thetaPerDay) , "theta per-day not provided"); // QA:[RG]::verified // TODO: message
        return thetaPerDay;
    }

    public double vega() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(vega) , "vega not provided"); // QA:[RG]::verified // TODO: message
        return vega;
    }

    public double rho() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(rho) , "rho not provided"); // QA:[RG]::verified // TODO: message
        return rho;
    }

    public double dividendRho() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(dividendRho) , "dividend rho not provided"); // QA:[RG]::verified // TODO: message
        return dividendRho;
    }

    public double itmCashProbability() /* @ReadOnly */ {
        calculate();
        QL.ensure(!Double.isNaN(itmCashProbability) , "in-the-money cash probability not provided"); // QA:[RG]::verified // TODO: message
        return itmCashProbability;
    }

    //
    // overrides Instrument
    //

    @Override
    public boolean isExpired() /* @ReadOnly */ {
        final Date evaluationDate = new Settings().evaluationDate();
        return exercise.lastDate().le( evaluationDate );
    }


    //
    // overrides Instrument
    //

    /**
     * {@inheritDoc}
     *
     * Obtains {@link Greeks} and {@link MoreGreeks} calculated by a {@link PricingEngine}
     *
     * @see Greeks
     * @see MoreGreeks
     * @see PricingEngine
     */
    @Override
    public void fetchResults(final PricingEngine.Results results) /* @ReadOnly */ {
        super.fetchResults(results);

        // bind a Results interface to specific classes
        QL.require(OneAssetOption.Results.class.isAssignableFrom(results.getClass()), ReflectConstants.WRONG_ARGUMENT_TYPE); // QA:[RG]::verified
        final OneAssetOption.ResultsImpl r = (OneAssetOption.ResultsImpl)results;
        final GreeksImpl     greeks = r.greeks();
        final MoreGreeksImpl moreGreeks = r.moreGreeks();

        //
        // No check on Double.NaN values - just copy. this allows:
        // a) To decide in derived options what to do when null results are returned
        //    (throw numerical calculation?)
        // b) To implement slim engines which only calculate the value.
        //    Of course care must be taken not to call the greeks methods when using these.
        //
        delta          = greeks.delta;
        gamma          = greeks.gamma;
        theta          = greeks.theta;
        vega           = greeks.vega;
        rho            = greeks.rho;
        dividendRho    = greeks.dividendRho;

        //
        // No check on Double.NaN values - just copy. this allows:
        // a) To decide in derived options what to do when null results are returned
        //    (throw numerical calculation?)
        // b) To implement slim engines which only calculate the value.
        //    Of course care must be taken not to call the greeks methods when using these.
        //
        deltaForward       = moreGreeks.deltaForward;
        elasticity         = moreGreeks.elasticity;
        thetaPerDay        = moreGreeks.thetaPerDay;
        itmCashProbability = moreGreeks.itmCashProbability;
    }

    @Override
    protected void setupExpired() /* @ReadOnly */ {
        super.setupExpired();
        delta = deltaForward = elasticity = gamma = theta =
            thetaPerDay = vega = rho = dividendRho =
                itmCashProbability = 0.0;
    }


    //
    // public inner interfaces
    //

    /**
     * basic option arguments
     *
     * @author Richard Gomes
     */
    public interface Arguments extends Option.Arguments { }

    /**
     * Results from single-asset option calculation
     *
     * @author Richard Gomes
     */
    public interface Results extends Instrument.Results, Option.Greeks, Option.MoreGreeks { }


    public interface Engine extends PricingEngine, Observer { }



    //
    // public inner classes
    //

    static public class ArgumentsImpl extends Option.ArgumentsImpl implements OneAssetOption.Arguments { }


    /**
     * Results from single-asset option calculation
     *
     * @author Richard Gomes
     */
    static public class ResultsImpl extends Instrument.ResultsImpl implements OneAssetOption.Results {

        private final Option.GreeksImpl       greeks;
        private final Option.MoreGreeksImpl   moreGreeks;

        public ResultsImpl() {
            greeks = new Option.GreeksImpl();
            moreGreeks = new Option.MoreGreeksImpl();
        }

        final public Option.GreeksImpl greeks() {
            return greeks;
        }

        final public Option.MoreGreeksImpl moreGreeks() {
            return moreGreeks;
        }

        //
        // implements Results
        //

        @Override
        public void reset() /* @ReadOnly */ {
            super.reset();
            greeks.reset();
            moreGreeks.reset();
        }

    }


    /**
     * The pricing engine for one-asset options
     *
     * @author Richard Gomes
     */
    static abstract public class EngineImpl
            extends GenericEngine<OneAssetOption.Arguments, OneAssetOption.Results>
            implements OneAssetOption.Engine {

        public EngineImpl() {
            super(new ArgumentsImpl(), new ResultsImpl());
        }

        public EngineImpl(final OneAssetOption.Arguments arguments, final OneAssetOption.Results results) {
            super(arguments, results);
        }

    }

}
