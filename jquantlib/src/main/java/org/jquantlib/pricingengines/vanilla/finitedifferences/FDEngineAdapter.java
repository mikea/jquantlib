/*
 Copyright (C) 2007 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */
package org.jquantlib.pricingengines.vanilla.finitedifferences;

import java.lang.reflect.Constructor;

import org.jquantlib.instruments.VanillaOption;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.lang.reflect.TypeToken;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

/**
 * Finite-differences pricing engine for American-style vanilla options
 *
 * @category vanillaengines
 *
 * @author Srinivas Hasti
 */
//TODO: class comments
//TODO: work in progress
public class FDEngineAdapter<T extends FDVanillaEngine> extends VanillaOption.EngineImpl {

    //
    // private fields
    //

    private final FDVanillaEngine fdVanillaEngine;


    //
    // public constructors
    //

    public FDEngineAdapter(
            final GeneralizedBlackScholesProcess process,
            final int timeSteps,
            final int gridPoints,
            final boolean timeDependent) {
        try {
            final Class<T> rsgClass = (Class<T>) TypeToken.getClazz(this.getClass());
            final Constructor<T> c = rsgClass.getConstructor(GeneralizedBlackScholesProcess.class, int.class, int.class, boolean.class);
            fdVanillaEngine = c.newInstance(process, timeSteps, gridPoints, timeDependent);
        } catch (final Exception e) {
            throw new LibraryException(e); // QA:[RG]::verified
        }
        process.addObserver(this);
    }


    //
    // implements PricingEngine
    //

    @Override
    public void calculate() {
        fdVanillaEngine.setupArguments(arguments);
        fdVanillaEngine.calculate(results);
    }
}
