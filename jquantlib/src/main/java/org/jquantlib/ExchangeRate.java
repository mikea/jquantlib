/*
 Copyright (C) 2009 Ueli Hofstetter

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

import org.jquantlib.math.Constants;
import org.jquantlib.util.Pair;


public class ExchangeRate {
    
    public enum Type { Direct,  /* given directly by the user */
        Derived  /* derived from exchange rates between
                      other currencies */
    };
    
    private Currency source_, target_;
    private     /*Decimal*/double rate_;
    private     Type type_;
    public     Pair<ExchangeRate,ExchangeRate> rateChain_;
    
    public ExchangeRate(){
        this.rate_=Constants.NULL_REAL;
    }
    
    //copy constructor
    public ExchangeRate(ExchangeRate toCopy){
        //shouldn't matter
        source_ = toCopy.source_;
        target_ = toCopy.target_;
        rate_ = toCopy.rate_;
        type_ = toCopy.type_;
    }
    
    public  ExchangeRate(final Currency  source,
                                      final Currency  target,
                                      /*Decimal*/double rate){
        this.source_=(source);
        this.target_=(target);
        this.rate_=(rate);
        this.type_=(Type.Direct) ;
    }

    public Currency   source() {
        return source_;
    }

    public Currency target()   {
        return target_;
    }

    public  Type  type()   {
        return type_;
    }

    public /*Decimal*/ double rate()   {
        return rate_;
    }
    
    
    public Money  exchange(final Money  amount)   {
        switch (type_) {
          case Direct:
            if (amount.currency().equals(source_)){
                return new Money(amount.value()*rate_, target_);
            }
            else if (amount.currency().equals(target_)){
                return new Money(amount.value()/rate_, source_);
            }
            else
                throw new AssertionError("exchange rate not applicable");
          case Derived:
            if (amount.currency() == rateChain_.getFirst().source() ||
                amount.currency() == rateChain_.getFirst().target())
                return rateChain_.getSecond().exchange(
                                         rateChain_.getFirst().exchange(amount));
            else if (amount.currency() == rateChain_.getSecond().source() ||
                       amount.currency() == rateChain_.getSecond().target())
                return rateChain_.getFirst().exchange(
                                         rateChain_.getSecond().exchange(amount));
            else
                throw new AssertionError("exchange rate not applicable");
          default:
            throw new AssertionError("unknown exchange-rate type");
        }
    }

    public static ExchangeRate chain(final ExchangeRate  r1,
                                     final ExchangeRate  r2) {
        ExchangeRate result = new ExchangeRate();
        result.type_ = ExchangeRate.Type.Derived;
        result.rateChain_ = new Pair<ExchangeRate, ExchangeRate>( new ExchangeRate(r1), new ExchangeRate(r2));
        if (r1.source_ == r2.source_) {
            result.source_ = r1.target_;
            result.target_ = r2.target_;
            result.rate_ = r2.rate_/r1.rate_;
        } else if (r1.source_ == r2.target_) {
            result.source_ = r1.target_;
            result.target_ = r2.source_;
            result.rate_ = 1.0/(r1.rate_*r2.rate_);
        } else if (r1.target_ == r2.source_) {
            result.source_ = r1.source_;
            result.target_ = r2.target_;
            result.rate_ = r1.rate_*r2.rate_;
        } else if (r1.target_ == r2.target_) {
            result.source_ = r1.source_;
            result.target_ = r2.source_;
            result.rate_ = r1.rate_/r2.rate_;
        } else {
            throw new AssertionError("exchange rates not chainable");
        }
        return result;
    }

}
