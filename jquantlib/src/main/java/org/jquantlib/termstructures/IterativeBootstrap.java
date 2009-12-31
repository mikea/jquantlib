package org.jquantlib.termstructures;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import org.jquantlib.QL;
import org.jquantlib.math.interpolations.factories.Linear;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.solvers1D.Brent;
import org.jquantlib.termstructures.Bootstrapper;
import org.jquantlib.termstructures.yieldcurves.BootstrapTraits;
import org.jquantlib.time.Date;

public class IterativeBootstrap implements Bootstrapper
{
    private boolean validCurve;
    
    private YieldTermStructure termStructure;
    
    private Bootstrapable bootstrapable;

    private BootstrapTraits traits;

    private RateHelper [] instruments; 
    

    public IterativeBootstrap ()
    {
        this.validCurve = false;
    }

    public void setup (YieldTermStructure termStructure, Bootstrapable bootstrapable, RateHelper [] instruments,
            BootstrapTraits traits)
    {
        QL.ensure (termStructure != null, "TermStructure cannot be null");
        this.termStructure = termStructure;
        this.bootstrapable = bootstrapable;
        this.instruments = instruments;
        this.traits = traits;
        int n = instruments.length;
        // FIXME
        QL.require (n >= 2, "Not enough instruments provided");

        for (int i = 0; i < n; ++ i)
        {
            termStructure.addObserver (instruments[i]);
        }
        bootstrapable.resetData (n + 1);
        bootstrapable.resetTime (n + 1);
        bootstrapable.resetDates (n + 1);
    }

    public void calculate ()
    {
        int isize = instruments.length;
        Array data = bootstrapable.getData();
        Date [] dates  = bootstrapable.getDates();
        Array times = bootstrapable.getTimes();
        // sorting and maturity date check is done by check instruments on the piecewise yield curve
        // assert no invalid quotes, and assign term structure
        for (RateHelper i : instruments)
        {
            QL.ensure (i.quoteIsValid(), " Instrument cannot have invalid quote.");
            // set term structure on instrument
            i.setTermStructure (termStructure);
        }
        // set initial guess only if the current curve cannot be used as a guess
        if (validCurve)
        {
            QL.ensure (bootstrapable.getData().size() == isize + 1, "Dimensions mismatch expected");
        }
        else
        {
            bootstrapable.resetData (isize + 1);
            data = bootstrapable.getData ();
            data.set (0, traits.initialValue());
            for (int k = 0; k < data.size(); ++k)
            {
                data.set (k, traits.initialGuess());
            }
        }
        // we really only need to do this once, why bother doing it everytime we calculate?
        dates[0] = traits.initialDate (termStructure);
        times.set (0, termStructure.timeFromReference (dates[0]));
        data.set (0, traits.initialValue ());
        for (int i = 0; i < isize ; ++ i)
        {
            dates[i + 1] = instruments[i].latestDate();
            times.set (i + 1, termStructure.timeFromReference (dates[i+1]));
            if (! validCurve)
            {
                data.set (i + 1, data.get (i));
            }
        }
        

        DecimalFormat df = new DecimalFormat("###.###############");
        df.setMinimumFractionDigits (15);

        Brent solver = new Brent ();
        int maxIterations = traits.maxIterations();
        
        for (int iteration = 0;; ++iteration) 
        {
            // only read safe to use as a reference
            final Array previousData = data.clone();
            // restart from the previous interpolation
            if (validCurve) 
            {
                bootstrapable.setInterpolation 
                (bootstrapable.getInterpolator().interpolate (
                     times.constIterator (), data.constIterator ()));
                
                /*
                termStructure.interpolation_ = termStructure.interpolator_.interpolate(
                                                      termStructure.times_.begin(),
                                                      termStructure.times_.end(),
                                                      data.begin());
                */
            }
            
            for (int i = 1; i < isize + 1; ++i) 
            {
                /*
                for (int k = 0; k < data.size(); ++ k)
                {
                    StringBuilder sb = new StringBuilder ();
                    sb.append ("Date: ");
                    sb.append (dates[k]);
                    sb.append ("\t Time: ");
                    sb.append (df.format (times.get (k)));
                    sb.append ("\t Discount: ");
                    sb.append (df.format (data.get(k)));
                    QL.debug (sb.toString ());
                }
                */
                // calculate guess before extending interpolation
                // to ensure that any extrapolation is performed
                // using the curve bootstrapped so far and no more
                RateHelper instrument = instruments[i-1];
                double guess = 0.0;
                if (validCurve|| iteration>0) 
                {
                    guess = data.get (i);
                } 
                else if (i==1) 
                {
                    guess = traits.initialGuess();
                } 
                else 
                {
                    // most traits extrapolate
                    guess = traits.guess(termStructure, dates[i]);
                }
                
                //QL.debug (" Guess : " + ((Double)(guess)).toString());
                
                // bracket
                double min = traits.minValueAfter(i, data);
                double max = traits.maxValueAfter(i, data);

                if (guess <= min || guess >= max)
                {
                    guess = (min + max) / 2.0;
                }

                if (! validCurve && iteration == 0)
                {
                    // extend interpolation a point at a time
                    try
                    {
                        bootstrapable.setInterpolation 
                            (bootstrapable.getInterpolator().interpolate (i + 1,
                             times.constIterator (), data.constIterator ()));
                    }
                    catch (Exception e) 
                    {
                        // no chance to fix it in a later iteration
                        //if (bootstrapable.getInterpolator().global());
                        //    throw; 

                        // otherwise, if the target interpolation is
                        // not usable yet
                        bootstrapable.setInterpolation 
                            (new Linear().interpolate (i + 1,
                             times.constIterator (), data.constIterator ()));
                    }
                }
                // required because we just changed the data
                // is it really required?
                bootstrapable.getInterpolation().update();
                                
                try
                {
                    BootstrapError error = new BootstrapError (bootstrapable, instrument, traits, i);
                    double r = solver.solve (error, traits.getAccuracy(),guess, min, max);
                    // redundant assignment (as it has been already performed
                    // by BootstrapError in solve procedure), but safe
                    data.set (i, r);
                } 
                catch (Exception e) 
                {
                    validCurve = false;
                    QL.error ("could not bootstrap");
                }
            }

            // no need for convergence loop, i don't like this and no one seems to know 
            // what the hell it is.
            /*
            if (! bootstrapable.getInterpolator ().global ())
            {
                break;      
            }
            */
            if (!validCurve && iteration == 0)
            {
                // ensure the target interpolation is used
                bootstrapable.setInterpolation 
                (bootstrapable.getInterpolator().interpolate (times.constIterator (), data.constIterator ()));
                
                // at least one more iteration is needed to check convergence
                continue;
            }

            // exit conditions, wat?
            double improvement = 0.0;
            for (int i = 1; i < isize + 1; ++i)
            {
                improvement = Math.max(improvement, Math.abs (data.get (i) - previousData.get (i)));
            }
            //QL.debug ("improvement :" + ((Double) improvement).toString());
            if (improvement <= traits.getAccuracy())
            {
                // convergence reached
                break;
            }

            QL.require (iteration + 1 < maxIterations, "convergence not reached after " +
                        ((Integer) (iteration + 1)).toString() + 
                        " iterations; last improvement " +
                        ((Double) (improvement)).toString() + ", required accuracy " +
                        ((Double) (traits.getAccuracy())).toString());

        }
        validCurve = true;
    }
}