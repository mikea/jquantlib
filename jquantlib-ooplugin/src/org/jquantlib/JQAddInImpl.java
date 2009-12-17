/*
Copyright (C) 2008 Praneet Tiwari

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
package org.jquantlib;

import org.jquantlib.ooimpl.MathHelper;
import org.jquantlib.ooimpl.OptionHelper;

import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.uno.XComponentContext;


public final class JQAddInImpl extends WeakBase
   implements com.sun.star.lang.XServiceInfo,
              com.sun.star.lang.XLocalizable,
              org.jquantlib.XJQAddIn
{
    private final XComponentContext m_xContext;
    private static final String m_implementationName = JQAddInImpl.class.getName();
    private static final String[] m_serviceNames = {
        "org.jquantlib.JQAddIn" };

    private com.sun.star.lang.Locale m_locale = new com.sun.star.lang.Locale();

    public JQAddInImpl( final XComponentContext context )
    {
        m_xContext = context;
    }

    public static XSingleComponentFactory __getComponentFactory( final String sImplementationName ) {
        XSingleComponentFactory xFactory = null;

        if ( sImplementationName.equals( m_implementationName ) )
            xFactory = Factory.createComponentFactory(JQAddInImpl.class, m_serviceNames);
        return xFactory;
    }

    public static boolean __writeRegistryServiceInfo( final XRegistryKey xRegistryKey ) {
        return Factory.writeRegistryServiceInfo(m_implementationName,
                                                m_serviceNames,
                                                xRegistryKey);
    }

    // com.sun.star.lang.XServiceInfo:
    public String getImplementationName() {
         return m_implementationName;
    }

    public boolean supportsService( final String sService ) {
        final int len = m_serviceNames.length;

        for( int i=0; i < len; i++)
            if (sService.equals(m_serviceNames[i]))
                return true;
        return false;
    }

    public String[] getSupportedServiceNames() {
        return m_serviceNames;
    }

    // com.sun.star.lang.XLocalizable:
    public void setLocale(final com.sun.star.lang.Locale eLocale)
    {
        m_locale = eLocale;
    }

    public com.sun.star.lang.Locale getLocale()
    {
        return m_locale;
    }

    // org.jquantlib.XJQAddIn:
    public int JQlnFactorial(final int parameter0)
    {

        return  (int) MathHelper.getlNFactorial(parameter0);
    }

    public double JQfactorial(final int n)
    {

        return   MathHelper.getFactorial(n);
    }

    public double BetaContinuedFraction(final double BetaContinuedFraction, final double b, final double x, final double accuracy, final int a)
    {
        // TODO: Exchange the default return implementation for "BetaContinuedFraction" !!!
        // NOTE: Default initialized polymorphic structs can cause problems
        // because of missing default initialization of primitive types of
        // some C++ compilers or different Any initialization in Java and C++
        // polymorphic structs.
        return 0;
    }

    public int JQIncompleteBetaFunction(final double accuracy, final double maxiteration, final double x, final double b, final double a)
    {
        // TODO: Exchange the default return implementation for "JQIncompleteBetaFunction" !!!
        // NOTE: Default initialized polymorphic structs can cause problems
        // because of missing default initialization of primitive types of
        // some C++ compilers or different Any initialization in Java and C++
        // polymorphic structs.
        return 0;
    }

    public int JQBetaFunction(final double z, final double w)
    {
        // TODO: Exchange the default return implementation for "JQBetaFunction" !!!
        // NOTE: Default initialized polymorphic structs can cause problems
        // because of missing default initialization of primitive types of
        // some C++ compilers or different Any initialization in Java and C++
        // polymorphic structs.
        return 0;
    }

    public double JQEuropeanBlackScholes(final double strike, final double underlying, final double riskFreeRate, final double volatility, final double dividendYield, final String optionType, final int settlementDay, final int settlementMonth, final int settlementYear, final int maturityDay, final int maturityMonth, final int maturityYear)
    {

       final double npv = OptionHelper.europeanBlackScholes(strike, underlying, riskFreeRate, volatility, dividendYield,  optionType, settlementDay, settlementMonth, settlementYear, maturityDay, maturityMonth, maturityYear);
       if (npv >= 0.0)
           return npv;
       else return 0.0;

    }
     public double JQEuropeanBlackScholesCall( final double strike, final double underlying, final double riskFreeRate, final double volatility,  final double dividendYield,   final int settlementDay,  final int settlementMonth,  final int settlementYear,  final int maturityDay,  final int maturityMonth,  final int maturityYear)
    {

   //     return OptionHelper.europeanBlackScholes(strike, underlying, riskFreeRate, volatility, dividendYield,   settlementDay, settlementMonth, settlementYear, maturityDay, maturityMonth, maturityYear);
         return 0.0;
    }



    public double JQBlackFormula(final double strike, final double stdDev, final double forward, final String optionType)
    {
        // TODO: Exchange the default return implementation for "JQBlackFormula" !!!
        // NOTE: Default initialized polymorphic structs can cause problems
        // because of missing default initialization of primitive types of
        // some C++ compilers or different Any initialization in Java and C++
        // polymorphic structs.
        return 0;
    }
// FIXME: sigma is used as random variable, it's not the real sigma
    public int JQEvaluatePoissonDistribution(final double mu, final double sigma)
    {

        return (int) MathHelper.evaluatePoissonDistribution(mu, sigma);
    }

    public double JQEvaluateNonCentralChiSquaredDistribution(final double df, final double x, final double ncp)
    {

        return MathHelper.evaluateNonCentralChiSquaredDistribution(df, x, ncp);
    }

    public double JQEvaluateInverseCumulativePoisson(final double lambda, final double x)
    {

        return MathHelper.evaluateInverseCumulativePoisson(lambda, x);
    }

    public double JQEvaluateInverseCumulativeNormal(final double average, final double sigma, final double x)
    {

        return MathHelper.evaluateInverseCumulativeNormal(average, sigma, x);
    }

    public double JQEvaluateGammaDistribution(final double a, final double x)
    {

        return MathHelper.evaluateGammaDistribution(a, x);
    }

    public double JQEvaluateCumulativePoissonDistribution(final double mean, final int k)
    {

        return MathHelper.evaluateCumulativePoissonDistribution(mean, k);
    }

    public double JQEvaluateCumulativeNormalDistribution(final double mean, final double sigma, final double z)
    {

        return MathHelper.evaluateCumulativeNormalDistribution(mean, sigma, z);
    }

    public int JQEvaluateBinomialDistributionValue(final double probability, final int k)
    {
       //FIXME
        return (int) MathHelper.evaluateBinomialDistributionValue(probability, k);
    }

    public double JQGetPrimeNumberAt(final double absoluteIndex)
    {
      //FIXME
        final int n = (int) absoluteIndex;

        return MathHelper.getPrimeNumberAt(n);
    }

}
