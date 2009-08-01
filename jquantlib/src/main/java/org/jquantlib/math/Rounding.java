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
package org.jquantlib.math;

/**
 * Basic rounding class.
 */
public class Rounding {

    private int precision_;
    public Type type_;
    private int digit_;

    /**
     * ! The rounding methods follow the OMG specification available at ftp://ftp.omg.org/pub/docs/formal/00-06-29.pdf
     * 
     *  Warning the names of the Floor and Ceiling methods might be misleading. Check the provided reference.
     */
    public enum Type {
        None, /*  do not round: return the number unmodified */
        Up, /*
             *   the first decimal place past the precision will be rounded up. This differs from the OMG rule which rounds up only
             * if the decimal to be rounded is greater than or equal to the rounding digit
             */
        Down, /*
               *   all decimal places past the precision will be truncated
               */
        Closest, /*
                  *   the first decimal place past the precision will be rounded up if greater than or equal to the rounding digit;
                  * this corresponds to the OMG round-up rule. When the rounding digit is 5, the result will be the one closest to
                  * the original number, hence the name.
                  */
        Floor, /*
                * !< positive numbers will be rounded up and negative numbers will be rounded down using the OMG round up and round
                * down rules
                */
        Ceiling
        /*
         *   positive numbers will be rounded down and negative numbers will be rounded up using the OMG round up and round down
         * rules
         */
    };


    /**
     *  Instances built through this constructor don't perform any rounding.
     */
    public Rounding() {
        this.type_ = Type.None;
    }

    public Rounding(int precision) {
        this(precision, Type.Closest, 5);
    }

    public Rounding(int precision, Type type /* = Closest */, int digit /* = 5 */) {

        this.precision_ = (precision);
        this.type_ = (type);
        this.digit_ = (digit);
    }

    public int precision() {
        return precision_;
    }

    public Type type() {
        return type_;
    }

    public int roundingDigit() {
        return digit_;
    }
    
    
    final public /*Decimal*/ double operator(/*Decimal*/double value)  {

        if (type_ == Rounding.Type.None){
            return value;
        }

        /*Real*/ double mult = Math.pow(10.0,precision_);
        boolean neg = (value < 0.0);
        /*Real*/ double lvalue = Math.abs(value)*mult;
        /*Real*/ double integral = (double)((int)lvalue);
        /*Real*/ double modVal = (double)(lvalue-(int)lvalue );
        lvalue -= modVal;
        switch (type_) {
          case Down:
            break;
          case Up:
            lvalue += 1.0;
            break;
          case Closest:
            if (modVal >= (digit_/10.0)){
                lvalue += 1.0;
            }
            break;
          case Floor:
            if (!neg) {
                if (modVal >= (digit_/10.0)){
                    lvalue += 1.0;
                }
            }
            break;
          case Ceiling:
            if (neg) {
                if (modVal >= (digit_/10.0))
                    lvalue += 1.0;
            }
            break;
          default:
            throw new AssertionError("unknown rounding method");
        }
        return (neg) ? -(lvalue/mult) : lvalue/mult;
    }

    
    
    

    // ! Up-rounding.
    public static class UpRounding extends Rounding {
        public UpRounding(int precision) {
            this(precision, 5);
        }

        public UpRounding(int precision, int digit) {
            super(precision, Type.Up, digit);
        }
    };

    // ! Down-rounding.
    public static class DownRounding extends Rounding {
        public DownRounding(int precision) {
            this(precision, 5);
        }

        public DownRounding(int precision, int digit) {
            super(precision, Type.Down, digit);
        }
    };

    // ! Closest rounding.
    public static class ClosestRounding extends Rounding {
        public ClosestRounding(int precision) {
            this(precision, 5);
        }

        public ClosestRounding(int precision, int digit) {
            super(precision, Type.Closest, digit);
        }
    };

    // ! Ceiling truncation.
    public static class CeilingTruncation extends Rounding {
        public CeilingTruncation(int precision) {
            this(precision, 5);
        }

        public CeilingTruncation(int precision, int digit) {
            super(precision, Type.Ceiling, digit);
        }
    };

    // ! %Floor truncation.

    public static class FloorTruncation extends Rounding {
        public FloorTruncation(int precision) {
            this(precision, 5);
        }

        public FloorTruncation(int precision, int digit) {
            super(precision, Type.Floor, digit);
        }
    };

}

