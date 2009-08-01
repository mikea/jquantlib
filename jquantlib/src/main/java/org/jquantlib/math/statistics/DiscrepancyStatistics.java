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
package org.jquantlib.math.statistics;

public class DiscrepancyStatistics extends GenericSequenceStatistics {

    private static final String dimension_not_allowed = "dimension==1 not allowed";

    private double adiscr_, cdiscr_;
    private double bdiscr_, ddiscr_;

    public DiscrepancyStatistics() {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
    }

    public DiscrepancyStatistics(final int dimension) {
        super(dimension);
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        reset(dimension);
    }

    public void add(final double[] samples, final int begin, final int end) {
        add(samples, begin, end, 1.0);
    }

    public void add(final double[] samples, final int begin, final int end, final double weight) {
        super.add(samples, weight);
        int k, m;
        final double N = samples();

        double r_ik, r_jk, temp = 1.0;
        int it;
        for (k = 0, it = begin; k < dimension_; ++it, ++k) {
            r_ik = samples[it]; // i=N
            temp *= (1.0 - r_ik * r_ik);
        }
        cdiscr_ += temp;

        for (m = 0; m < N - 1; m++) {
            temp = 1.0;
            for (k = 0, it = begin; k < dimension_; ++it, ++k) {
                // running i=1..(N-1)
                r_ik = stats_[k].data().get(m).getFirst();
                // fixed j=N
                r_jk = samples[it];
                temp *= (1.0 - Math.max(r_ik, r_jk));
            }
            adiscr_ += temp;

            temp = 1.0;
            for (k = 0, it = begin; k < dimension_; ++it, ++k) {
                // fixed i=N
                r_ik = samples[it];
                // running j=1..(N-1)
                r_jk = stats_[k].data().get(m).getFirst();
                temp *= (1.0 - Math.max(r_ik, r_jk));
            }
            adiscr_ += temp;
        }
        temp = 1.0;
        for (k = 0, it = begin; k < dimension_; ++it, ++k) {
            // fixed i=N, j=N
            r_ik = r_jk = samples[it];
            temp *= (1.0 - Math.max(r_ik, r_jk));
        }
        adiscr_ += temp;
    }

    @Override
    public void reset(int dimension) {
        assert dimension != 1 : dimension_not_allowed;
        if (dimension == 0)
            dimension = dimension_; // keep the current one
        super.reset(dimension);

        adiscr_ = 0.0;
        bdiscr_ = 1.0 / Math.pow(2.0, dimension - 1);
        cdiscr_ = 0.0;
        ddiscr_ = 1.0 / Math.pow(3.0, dimension);
    }

    public double discrepancy() {
        final int N = samples();
        //outcommented c++ code
        /*
        Size i;
        Real r_ik, r_jk, cdiscr = adiscr = 0.0, temp = 1.0;

        for (i=0; i<N; i++) {
            Real temp = 1.0;
            for (Size k=0; k<dimension_; k++) {
                r_ik = stats_[k].sampleData()[i].first;
                temp *= (1.0 - r_ik*r_ik);

            }
            cdiscr += temp;
        }

        for (i=0; i<N; i++) {
            for (Size j=0; j<N; j++) {
                Real temp = 1.0;
                for (Size k=0; k<dimension_; k++) {
                    r_jk = stats_[k].sampleData()[j].first;
                    r_ik = stats_[k].sampleData()[i].first;
                    temp *= (1.0 - std::max(r_ik, r_jk));
                }
                adiscr += temp;
            }
        }
         */
        return Math.sqrt(adiscr_/(N*N)-bdiscr_/N*cdiscr_+ddiscr_);
    }

}
