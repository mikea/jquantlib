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
package org.jquantlib.currencies;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jquantlib.Configuration;
import org.jquantlib.Currency;
import org.jquantlib.ExchangeRate;
import org.jquantlib.currencies.America.PEHCurrency;
import org.jquantlib.currencies.America.PEICurrency;
import org.jquantlib.currencies.America.PENCurrency;
import org.jquantlib.currencies.Europe.ATSCurrency;
import org.jquantlib.currencies.Europe.BEFCurrency;
import org.jquantlib.currencies.Europe.DEMCurrency;
import org.jquantlib.currencies.Europe.ESPCurrency;
import org.jquantlib.currencies.Europe.EURCurrency;
import org.jquantlib.currencies.Europe.FIMCurrency;
import org.jquantlib.currencies.Europe.FRFCurrency;
import org.jquantlib.currencies.Europe.GRDCurrency;
import org.jquantlib.currencies.Europe.IEPCurrency;
import org.jquantlib.currencies.Europe.ITLCurrency;
import org.jquantlib.currencies.Europe.LUFCurrency;
import org.jquantlib.currencies.Europe.NLGCurrency;
import org.jquantlib.currencies.Europe.PTECurrency;
import org.jquantlib.currencies.Europe.ROLCurrency;
import org.jquantlib.currencies.Europe.RONCurrency;
import org.jquantlib.currencies.Europe.TRLCurrency;
import org.jquantlib.currencies.Europe.TRYCurrency;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;


/**
 * 
 * @author Ueli Hofstetter
 * 
 * 
 * Exchange rate Repository.
 * 
 * Tests: org.jquantlib.testsuite.money.MoneyTest
 *
 */
public class ExchangeRateManager {
    
    private static volatile ExchangeRateManager instance = null;
    
    public static ExchangeRateManager getInstance() {
        if (instance == null) {
            synchronized (ExchangeRateManager.class) {
                if (instance == null) {
                    instance = new ExchangeRateManager();
                }
            }
        }
        return instance;
    }
        
    private HashMap<Object, List<Entry>> data_ = new HashMap<Object, List<Entry>>();
    
    //FIXME: check whether this should be derived from some kind of (generic function)
    public static class Valid_at /*implements Ops.DoublePredicate*/{
        Date d;
        public Valid_at(final Date  d){
            this.d = d;
        }
        public boolean operator(Entry  e) {
            return d.ge(e.startDate) && d.le(e.endDate);
        }
//        @Override
//        public boolean op(double a) {
//            // TODO Auto-generated method stub
//            return false;
//        }
        
    }
    
   //mimic Entry struct
    public static class Entry {
        public ExchangeRate rate;
        public Date startDate, endDate;

        public Entry(final ExchangeRate rate, final Date start, final Date end) {
            this.rate = (rate);
            this.startDate = (start);
            this.endDate = (end);
        };
    }

    public ExchangeRateManager() {
        addKnownRates();
    }
    
    //! Add an exchange rate.
    /*! The given rate is valid between the given dates.

        \note If two rates are given between the same currencies
              and with overlapping date ranges, the latest one
              added takes precedence during lookup.
    */

    public void add(final ExchangeRate  rate,
                                  final Date  startDate,
                                  final Date  endDate) {
      /*Key*/int k = hash(rate.source(), rate.target());
      if(data_.get(k)==null){
          data_.put(k, new ArrayList<Entry>());
      }
      data_.get(k).add(0,new Entry(rate,startDate,endDate));
    }
    
    public void add(final ExchangeRate  rate) {
        add(rate, DateFactory.getFactory().getMinDate(), DateFactory.getFactory().getMaxDate());

}
    
    
    public ExchangeRate lookup( final Currency  source,
            final Currency  target){
        return lookup(source, target, DateFactory.getFactory().getTodaysDate(), ExchangeRate.Type.Derived);
    }
    
    public ExchangeRate lookup( final Currency  source,
            final Currency  target, final Date date){
        return lookup(source, target, date, ExchangeRate.Type.Derived);
    }

    /*! Lookup the exchange rate between two currencies at a given
    date.  If the given type is Direct, only direct exchange
    rates will be returned if available; if Derived, direct
    rates are still preferred but derived rates are allowed.

    \warning if two or more exchange-rate chains are possible
             which allow to specify a requested rate, it is
             unspecified which one is returned.
     */
    public ExchangeRate lookup( final Currency  source,
                                             final Currency  target,
                                             Date date,
                                             ExchangeRate.Type type)  {
        //FIXME: review equals operator
        if (source.equals(target)){
            return new ExchangeRate(source,target,1.0);
        }

        if (date.eq(DateFactory.getFactory().getTodaysDate())){
            date = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();
        }

        if (type == ExchangeRate.Type.Direct) {
            return directLookup(source,target,date);
        } else if (!source.triangulationCurrency().empty()) {
            Currency  link = source.triangulationCurrency();
            if (link.equals(target)){
                return directLookup(source,link,date);
            }
            else{
                  return ExchangeRate.chain(directLookup(source,link,date), lookup(link,target,date));
            }
        } else if (!target.triangulationCurrency().empty()) {
              Currency  link = target.triangulationCurrency();
            if (source.equals(link)){
                return directLookup(link,target,date);
            }
            else{
                    return ExchangeRate.chain(lookup(source,link,date),directLookup(link,target,date));
            }
        } else {
            return smartLookup(source,target,date);
        }
    }

    public void clear() {
        data_.clear();
        addKnownRates();
    }
    
    //ok
    public int hash(final Currency  c1, final Currency  c2)   {
        return Math.min(c1.numericCode(),c2.numericCode())*1000
             + Math.max(c1.numericCode(),c2.numericCode());
    }

    //ok
    public boolean hashes(/*ExchangeRateManager::Key*/int k,
                                     final Currency  c)   {
        return c.numericCode() == k % 1000 || c.numericCode() == k/1000;
    }

    //ok
    private void  addKnownRates() {
        // currencies obsoleted by Euro
        add(new ExchangeRate(new EURCurrency(), new ATSCurrency(), 13.7603),
            DateFactory.getFactory().getDate(1, Month.JANUARY, 1999), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new EURCurrency(), new BEFCurrency(), 40.3399),
                DateFactory.getFactory().getDate(1, Month.JANUARY, 1999), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new EURCurrency(), new DEMCurrency(), 1.95583),
                DateFactory.getFactory().getDate(1, Month.JANUARY, 1999), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new EURCurrency(), new ESPCurrency(), 166.386),
                DateFactory.getFactory().getDate(1, Month.JANUARY, 1999), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new EURCurrency(), new FIMCurrency(), 5.94573),
                DateFactory.getFactory().getDate(1, Month.JANUARY, 1999), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new EURCurrency(), new FRFCurrency(), 6.55957),
                DateFactory.getFactory().getDate(1, Month.JANUARY, 1999), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new EURCurrency(), new GRDCurrency(), 340.750),
                DateFactory.getFactory().getDate(1, Month.JANUARY, 2001), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new EURCurrency(), new IEPCurrency(), 0.787564),
                DateFactory.getFactory().getDate(1, Month.JANUARY, 1999), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new EURCurrency(), new ITLCurrency(), 1936.27),
                DateFactory.getFactory().getDate(1, Month.JANUARY, 1999), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new EURCurrency(), new LUFCurrency(), 40.3399),
                DateFactory.getFactory().getDate(1, Month.JANUARY, 1999), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new EURCurrency(), new NLGCurrency(), 2.20371),
                DateFactory.getFactory().getDate(1, Month.JANUARY, 1999), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new EURCurrency(), new PTECurrency(), 200.482),
                DateFactory.getFactory().getDate(1, Month.JANUARY, 1999), DateFactory.getFactory().getMaxDate());
        // other obsoleted currencies
        add(new ExchangeRate(new TRYCurrency(), new TRLCurrency(), 1000000.0),
                DateFactory.getFactory().getDate(1, Month.JANUARY, 2005), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new RONCurrency(), new ROLCurrency(), 10000.0),
                DateFactory.getFactory().getDate(1, Month.JULY, 2005), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new PENCurrency(),new  PEICurrency(), 1000000.0),
                DateFactory.getFactory().getDate(1, Month.JULY, 1991), DateFactory.getFactory().getMaxDate());
        add(new ExchangeRate(new PEICurrency(),new  PEHCurrency(), 1000.0),
                DateFactory.getFactory().getDate(1, Month.FEBRUARY, 1985), DateFactory.getFactory().getMaxDate());
    }

    private ExchangeRate directLookup(final Currency  source,
                                                   final Currency  target,
                                                   final Date  date)  {
        
        ExchangeRate rate = null;
        assert((rate = fetch(source, target, date)) != null):"no direct conversion available from "
                    + source.code() + " to " + target.code()
                    + " for " + date.getShortFormat();

        return  rate;
    }
    
    private ExchangeRate  smartLookup(final Currency  source,
            final Currency  target,
            final Date  date){
        return smartLookup(source, target, date, new int[0]);
    }

    private ExchangeRate  smartLookup(final Currency  source,
                                         final Currency  target,
                                         final Date  date,
                                         int[]  forbidden)   {
        // direct exchange rates are preferred.
        ExchangeRate direct = fetch(source, target, date);
        if(direct != null){
            return direct;
        }

        // if none is found, turn to smart lookup. The source currency
        // is forbidden to subsequent lookups in order to avoid cycles.
        int temp[] = forbidden.clone();
        forbidden = new int[temp.length + 1];
        System.arraycopy(temp,0,forbidden, 0, temp.length);
        forbidden[forbidden.length-1]=(source.numericCode());
        Iterator keyIterator = data_.keySet().iterator();
        while (keyIterator.hasNext()) {
            // we look for exchange-rate data which involve our source
            // currency...
            Object key = keyIterator.next();
            if (hashes((Integer)key, source) && !(data_.get(key).isEmpty())) {
                // ...whose other currency is not forbidden...
                Entry e = data_.get(key).get(0);
                Currency other = 
                    //if
                    (source == e.rate.source()) ?
                        //then
                        e.rate.target() :
                        //else
                        e.rate.source();
                if (match(forbidden,other.numericCode()) == (forbidden.length-1)) {
                     //...and which carries information for the requested date.
                    ExchangeRate head = fetch(source, other, date);
                    try{
                        if(head!=null){
                            ExchangeRate tail = smartLookup(other,target,date,
                                forbidden);
                            // ..we're done.
                            return ExchangeRate.chain(head,tail);
                        }
                    }
                    catch (Exception ex){
                        // fall through...
                        // otherwise, we just discard this rate.
                    }
                }
            }
        }
        // if the loop completed, we have no way to return the requested rate.
        throw new AssertionError("no conversion available from "
                + source.code() + " to " + target.code()
                + " for " + date.getShortFormat());
    }

    public ExchangeRate fetch(final Currency  source,
                               final Currency  target,
                               final Date  date)   {
        final List<Entry>  rates = data_.get(hash(source,target));
        int i = matchValidateAt(rates, date);
        return i==rates.size()-1?rates.get(i).rate:null;
    }
  
    
      //see std::find
      private int match(int[] list, int value) {
        for (int i = 0; i < list.length; i++) {
            if (value == list[i]) {
                return i;
            }
        }
        return -1;
    }
    
    //see std::find_if
    private int matchValidateAt(List<Entry> rates, Date date){
        Valid_at va = new Valid_at(date);
        for(int i = 0; i<rates.size(); i++){
            if(va.operator(rates.get(i))){
                return i;
            }
        }
        return -1;
    }
}
