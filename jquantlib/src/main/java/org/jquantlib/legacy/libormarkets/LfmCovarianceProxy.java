package org.jquantlib.legacy.libormarkets;

import org.jquantlib.math.Array;
import org.jquantlib.math.Matrix;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.GaussKronrodAdaptive;
import org.jquantlib.processes.LfmCovarianceParameterization;
import org.jquantlib.util.stdlibc.Std;

public class LfmCovarianceProxy extends LfmCovarianceParameterization {
    
    protected LmVolatilityModel  volaModel_;
    protected LmCorrelationModel corrModel_;
        
    
    public LfmCovarianceProxy(LmVolatilityModel volaModel, LmCorrelationModel corrModel){
        super(corrModel.size(), corrModel.factors());
        this.volaModel_ = volaModel;
        this.corrModel_ = corrModel;
    }

    public LmVolatilityModel volatilityModel(){
        return volaModel_;
    }
    
    public LmCorrelationModel correlationModel(){
        return corrModel_;
    }
    
    public Matrix diffusion(/*@Time*/ double t, final Array x){
        Matrix pca = corrModel_.pseudoSqrt(t, x);
        Array  vol = new Array(volaModel_.volatility(t, x.dataAsList()));
        for (int i=0; i<size_; ++i) {
            Std.getInstance().transform(pca.getRow(i), pca.getRow(i), Std.getInstance().multiplies(vol.get(i)));
        }
        return pca;
    }
    
    public Matrix covariance(/* @Time */double t, final Array x) {

        Array volatility = new Array(volaModel_.volatility(t, x.dataAsList()));
        Matrix correlation = corrModel_.correlation(t, x);

        Matrix tmp = new Matrix(size_, size_);
        for (int i = 0; i < size_; ++i) {
            for (int j = 0; j < size_; ++j) {
                tmp.set(i, j, volatility.get(i) * correlation.get(i, j) * volatility.get(j));
            }
        }

        return tmp;
    }
    
    static class Var_Helper implements UnaryFunctionDouble {

        private final  int i_, j_;
        private final LmVolatilityModel/* * */   volaModel_;
        private final LmCorrelationModel/* * */  corrModel_;

      public Var_Helper(final LfmCovarianceProxy proxy,
                                                 int i, int j){
      this.i_ = i;
        this.j_ = j;
        this.volaModel_ = proxy.volaModel_;
        this.corrModel_ = proxy.corrModel_;
      }

      public double evaluate(double t)  {
          /*@Volatility*/ double v1, v2;

          if (i_ == j_) {
              v1 = v2 = volaModel_.volatility(i_, t);
          } else {
              v1 = volaModel_.volatility(i_, t);
              v2 = volaModel_.volatility(j_, t);
          }

          return  v1 * corrModel_.correlation(i_, j_, t) * v2;
      }
    }

     public  double integratedCovariance(
                                 int i, int j, /*@Time*/double t, final Array x)  {

          if (corrModel_.isTimeIndependent()) {
              try {
                  // if all objects support these methods
                  // thats by far the fastest way to get the
                  // integrated covariance
                  return corrModel_.correlation(i, j, 0.0, x)
                          * volaModel_.integratedVariance(j, i, t, x.dataAsList());
              }
              catch (Exception ex) {
                  // okay proceed with the
                  // slow numerical integration routine
              }
          }

          if(x.empty()){
              throw new IllegalArgumentException("can not handle given x here");
          }

          double tmp=0.0;
          Var_Helper helper = new Var_Helper(this, i, j);

          GaussKronrodAdaptive integrator = new GaussKronrodAdaptive(1e-10, 10000);
          for (int k=0; k<64; ++k) {
              tmp+=integrator.evaluate(helper, k*t/64., (k+1)*t/64.);
          }
          return tmp;
      }

    
}
