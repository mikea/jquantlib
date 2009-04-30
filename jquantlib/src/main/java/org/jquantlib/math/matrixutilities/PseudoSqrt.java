/*
 Copyright (C) 
 2009 Ueli Hofstetter

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
package org.jquantlib.math.matrixutilities;

import org.jquantlib.math.Array;
import org.jquantlib.math.Closeness;
import org.jquantlib.math.Matrix;
import org.jquantlib.math.optimization.CostFunction;

class HypersphereCostFunction extends CostFunction {
    
    
    
    
    private int size_;
    private boolean lowerDiagonal_;
    private Matrix targetMatrix_;
    private Array targetVariance_;
    private  Matrix currentRoot_, tempMatrix_, currentMatrix_;

 public HypersphereCostFunction(final Matrix targetMatrix,
                           final Array targetVariance,
                          boolean lowerDiagonal){
  this.size_ = targetMatrix.rows();
  this.lowerDiagonal_=lowerDiagonal;
    this.targetMatrix_= targetMatrix;
    this.targetVariance_=targetVariance;
    currentRoot_ = new Matrix(size_, size_);
    tempMatrix_ = new Matrix(size_, size_);
    currentMatrix_ = new Matrix(size_, size_); }
  public Array values(final Array array)  {
      throw new UnsupportedOperationException("values method not implemented");
  }
  
  public double value(final Array x)  {
      int i,j,k;
      //std::fill(currentRoot_.begin(), currentRoot_.end(), 1.0);
      for(int cols = 0; cols<currentRoot_.columns(); cols++){
          for(int rows = 0; rows<currentRoot_.rows(); rows++){
              currentRoot_.set(rows, cols, 1.0);
          }
      }
     
      if (lowerDiagonal_) {
          for (i=0; i<size_; i++) {
              for (k=0; k<size_; k++) {
                  if (k>i) {
                      currentRoot_.set(i,k,0);
                  } else {
                      for (j=0; j<=k; j++) {
                          if (j == k && k!=i){
                              currentRoot_.set(i,k, currentMatrix_.get(i, k) * Math.cos(x.get(i*(i-1)/2+j)));
                          }
                           else if (j!=i){
                              currentRoot_.set(i,k, currentRoot_.get(i, k)*
                                  Math.sin(x.get(i*(i-1)/2+j)));
                           }
                      }
                  }
              }
          }
      } else {
          for (i=0; i<size_; i++) {
              for (k=0; k<size_; k++) {
                  for (j=0; j<=k; j++) {
                      if (j == k && k!=size_-1){
                          currentRoot_.set(i, k, currentRoot_.get(i,k)
                              *Math.cos(x.get(j*size_+i)));
                      }
                      else if (j!=size_-1){
                          currentRoot_.set(i,k,currentRoot_.get(i, j)*Math.sin(x.get(j*size_+i)));
                      }
                  }
              }
          }
      }
      double temp, error=0;
      tempMatrix_ = currentRoot_.transpose(currentRoot_);
      currentMatrix_ = currentRoot_.operatorMultiply(currentRoot_,tempMatrix_);
      for (i=0;i<size_;i++) {
          for (j=0;j<size_;j++) {
              temp = currentMatrix_.get(i, j)*targetVariance_.get(i)
                *targetVariance_.get(j)-targetMatrix_.get(i, j);
              error += temp*temp;
          }
      }
      return error;
  }
}

public class PseudoSqrt {
    private final static String unknown_salvaging_algorithm = "unknown salvaging algorithm";
    public enum SalvagingAlgorithm {
        None, Spectral, Hypersphere, LowerDiagonal, Higham;
    }
    
  //! Returns the pseudo square root of a real symmetric matrix
    /*! Given a matrix \f$ M \f$, the result \f$ S \f$ is defined
        as the matrix such that \f$ S S^T = M. \f$
        If the matrix is not positive semi definite, it can
        return an approximation of the pseudo square root
        using a (user selected) salvaging algorithm.

        For more information see: "The most general methodology to create
        a valid correlation matrix for risk management and option pricing
        purposes", by R. Rebonato and P. Jaeckel.
        The Journal of Risk, 2(2), Winter 1999/2000
        http://www.rebonato.com/correlationmatrix.pdf

        Revised and extended in "Monte Carlo Methods in Finance",
        by Peter Jaeckel, Chapter 6.

        \pre the given matrix must be symmetric.

        \relates Matrix

        \warning Higham algorithm only works for correlation matrices.

        \test
        - the correctness of the results is tested by reproducing
          known good data.
        - the correctness of the results is tested by checking
          returned values against numerical calculations.
    */
    public Matrix pseudoSqrt( final Matrix matrix){
        return pseudoSqrt(matrix, SalvagingAlgorithm.None);
    }
    


    //! Returns the rank-reduced pseudo square root of a real symmetric matrix
    /*! The result matrix has rank<=maxRank. If maxRank>=size, then the
        specified percentage of eigenvalues out of the eigenvalues' sum is
        retained.

        If the input matrix is not positive semi definite, it can return an
        approximation of the pseudo square root using a (user selected)
        salvaging algorithm.

        \pre the given matrix must be symmetric.

        \relates Matrix
    */
    public Matrix rankReducedSqrt(final Matrix matrix,
                                             int maxRank,
                                             int componentRetainedPercentage,
                                             SalvagingAlgorithm algoritm){
                                                return null;
                                            }
    
    
    public static void checkSymmetry(final Matrix matrix) {
        int size = matrix.rows();
        if(size != matrix.columns()){
            throw new IllegalArgumentException("non square matrix: " + size + " rows, " +
                   matrix.columns() + " columns");
            }
        for (int i=0; i<size; ++i){
            for (int j=0; j<i; ++j){
                if(Closeness.isClose(matrix.get(i, j), matrix.get(j, i))){
                    throw new IllegalArgumentException("non symmetric matrix: " +
                           "[" + i + "][" + j + "]=" + matrix.get(i,j) +
                           ", [" + j + "][" + i + "]=" + matrix.get(j,i));
                }
            }
        }                
    }

    public static void normalizePseudoRoot(final Matrix matrix,
                             Matrix pseudo) {
        int size = matrix.rows();
        if(size != pseudo.rows()){
            throw new IllegalArgumentException("matrix/pseudo mismatch: matrix rows are " + size +
                   " while pseudo rows are " + pseudo.columns());
        }

        int pseudoCols = pseudo.columns();

        // row normalization
        for (int i=0; i<size; ++i) {
            double norm = 0.0;
            for (int j=0; j<pseudoCols; ++j){
                norm += pseudo.get(i, j)*pseudo.get(j, i);
            }
            if (norm>0.0) {
                double normAdj = Math.sqrt(matrix.get(i,i)/norm);
                for (int j=0; j<pseudoCols; ++j){
                    pseudo.set(i, j, pseudo.get(i, j) * normAdj);
                }
            }
        }
    }
    
/*
    // Optimization function for hypersphere and lower-diagonal algorithm
    public  Matrix hypersphereOptimize(final Matrix targetMatrix,
                                            final Matrix currentRoot,
                                            final boolean lowerDiagonal) {
        int i,j,k;
        int size = targetMatrix.rows();
        Matrix result = currentRoot;
        Array variance = new Array(size, 0);
        for (i=0; i<size; i++){
            variance.set(i, Math.sqrt(targetMatrix.get(i,i)));
        }
        if (lowerDiagonal) {
            //TODO: write test case for matrix operations
            Matrix approxMatrix = new Matrix(result.operatorMultiply(result, result.transpose(result)));
            result = new CholeskyDecomposition().CholeskyDecomposition(approxMatrix, true);
            for (i=0; i<size; i++) {
                for (j=0; j<size; j++) {
                    result.set(i,j, result.get(i, j) / Math.sqrt(approxMatrix.get(i,i)));
                }
            }
        } else {
            for (i=0; i<size; i++) {
                for (j=0; j<size; j++) {
                    result.set(i,j, result.get(i, j)/ variance.get(i));
                }
            }
        }

        ConjugateGradient optimize = new ConjugateGradient();
        EndCriteria endCriteria = new EndCriteria(100, 10, 1e-8, 1e-8, 1e-8);
        HypersphereCostFunction costFunction(targetMatrix, variance,
                                             lowerDiagonal);
        NoConstraint constraint;

        // hypersphere vector optimization

        if (lowerDiagonal) {
            Array theta(size * (size-1)/2);
            const Real eps=1e-16;
            for (i=1; i<size; i++) {
                for (j=0; j<i; j++) {
                    theta[i*(i-1)/2+j]=result[i][j];
                    if (theta[i*(i-1)/2+j]>1-eps)
                        theta[i*(i-1)/2+j]=1-eps;
                    if (theta[i*(i-1)/2+j]<-1+eps)
                        theta[i*(i-1)/2+j]=-1+eps;
                    for (k=0; k<j; k++) {
                        theta[i*(i-1)/2+j] /= std::sin(theta[i*(i-1)/2+k]);
                        if (theta[i*(i-1)/2+j]>1-eps)
                            theta[i*(i-1)/2+j]=1-eps;
                        if (theta[i*(i-1)/2+j]<-1+eps)
                            theta[i*(i-1)/2+j]=-1+eps;
                    }
                    theta[i*(i-1)/2+j] = std::acos(theta[i*(i-1)/2+j]);
                    if (j==i-1) {
                        if (result[i][i]<0)
                            theta[i*(i-1)/2+j]=-theta[i*(i-1)/2+j];
                    }
                }
            }
            Problem p(costFunction, constraint, theta);
            optimize.minimize(p, endCriteria);
            theta = p.currentValue();
            std::fill(result.begin(),result.end(),1.0);
            for (i=0; i<size; i++) {
                for (k=0; k<size; k++) {
                    if (k>i) {
                        result[i][k]=0;
                    } else {
                        for (j=0; j<=k; j++) {
                            if (j == k && k!=i)
                                result[i][k] *=
                                    std::cos(theta[i*(i-1)/2+j]);
                            else if (j!=i)
                                result[i][k] *=
                                    std::sin(theta[i*(i-1)/2+j]);
                        }
                    }
                }
            }
        } else {
            Array theta(size * (size-1));
            const Real eps=1e-16;
            for (i=0; i<size; i++) {
                for (j=0; j<size-1; j++) {
                    theta[j*size+i]=result[i][j];
                    if (theta[j*size+i]>1-eps)
                        theta[j*size+i]=1-eps;
                    if (theta[j*size+i]<-1+eps)
                        theta[j*size+i]=-1+eps;
                    for (k=0;k<j;k++) {
                        theta[j*size+i] /= std::sin(theta[k*size+i]);
                        if (theta[j*size+i]>1-eps)
                            theta[j*size+i]=1-eps;
                        if (theta[j*size+i]<-1+eps)
                            theta[j*size+i]=-1+eps;
                    }
                    theta[j*size+i] = std::acos(theta[j*size+i]);
                    if (j==size-2) {
                        if (result[i][j+1]<0)
                            theta[j*size+i]=-theta[j*size+i];
                    }
                }
            }
            Problem p(costFunction, constraint, theta);
            optimize.minimize(p, endCriteria);
            theta=p.currentValue();
            std::fill(result.begin(),result.end(),1.0);
            for (i=0; i<size; i++) {
                for (k=0; k<size; k++) {
                    for (j=0; j<=k; j++) {
                        if (j == k && k!=size-1)
                            result[i][k] *= std::cos(theta[j*size+i]);
                        else if (j!=size-1)
                            result[i][k] *= std::sin(theta[j*size+i]);
                    }
                }
            }
        }

        for (i=0; i<size; i++) {
            for (j=0; j<size; j++) {
                result[i][j]*=variance[i];
            }
        }
        return result;
    }
/*
    // Matrix infinity norm. See Golub and van Loan (2.3.10) or
    // <http://en.wikipedia.org/wiki/Matrix_norm>
    Real normInf(const Matrix& M) {
        Size rows = M.rows();
        Size cols = M.columns();
        Real norm = 0.0;
        for (Size i=0; i<rows; ++i) {
            Real colSum = 0.0;
            for (Size j=0; j<cols; ++j)
                colSum += std::fabs(M[i][j]);
            norm = std::max(norm, colSum);
        }
        return norm;
    }

    // Take a matrix and make all the diagonal entries 1.
    const Disposable <Matrix>
    projectToUnitDiagonalMatrix(const Matrix& M) {
        Size size = M.rows();
        QL_REQUIRE(size == M.columns(),
                   "matrix not square");

        Matrix result(M);
        for (Size i=0; i<size; ++i)
            result[i][i] = 1.0;

        return result;
    }

    // Take a matrix and make all the eigenvalues non-negative
    const Disposable <Matrix>
    projectToPositiveSemidefiniteMatrix(Matrix& M) {
        Size size = M.rows();
        QL_REQUIRE(size == M.columns(),
                   "matrix not square");

        Matrix diagonal(size, size, 0.0);
        SymmetricSchurDecomposition jd(M);
        for (Size i=0; i<size; ++i)
            diagonal[i][i] = std::max<Real>(jd.eigenvalues()[i], 0.0);

        Matrix result =
            jd.eigenvectors()*diagonal*transpose(jd.eigenvectors());
        return result;
    }

    // implementation of the Higham algorithm to find the nearest
    // correlation matrix.
    const Disposable <Matrix>
    highamImplementation(const Matrix& A,
                         const Size maxIterations,
                         const Real& tolerance) {

        Size size = A.rows();
        Matrix R, Y(A), X(A), deltaS(size, size, 0.0);

        Matrix lastX(X);
        Matrix lastY(Y);

        for (Size i=0; i<maxIterations; ++i) {
            R = Y - deltaS;
            X = projectToPositiveSemidefiniteMatrix(R);
            deltaS = X - R;
            Y = projectToUnitDiagonalMatrix(X);

            // convergence test
            if (std::max(normInf(X-lastX)/normInf(X),
                    std::max(normInf(Y-lastY)/normInf(Y),
                            normInf(Y-X)/normInf(Y)))
                    <= tolerance)
            {
                break;
            }
            lastX = X;
            lastY = Y;
        }

        // ensure we return a symmetric matrix
        for (Size i=0; i<size; ++i)
            for (Size j=0; j<i; ++j)
                Y[i][j] = Y[j][i];

        return Y;
    }

}

*/
public static Matrix pseudoSqrt(final Matrix matrix,
                                    SalvagingAlgorithm sa) {
    int size = matrix.rows();
    /*
    #if defined(QL_EXTRA_SAFETY_CHECKS)
    checkSymmetry(matrix);
    #else
    */
    if(size != matrix.columns()){
               throw new IllegalArgumentException("non square matrix: " + size + " rows, " +
               matrix.columns() + " columns");
    }


    // spectral (a.k.a Principal Component) analysis
    SymmetricSchurDecomposition jd = new SymmetricSchurDecomposition(matrix);
    Matrix diagonal = new Matrix(size, size, 0.0);

    // salvaging algorithm
    Matrix result = new Matrix(size, size);
    boolean negative;
    switch (sa) {
      case None:
        // eigenvalues are sorted in decreasing order
        if(jd.eigenvalues().get(size-1)<-1e-16){
                  throw new IllegalArgumentException( "negative eigenvalue(s) ("
                    + /*std::scientific*/ + jd.eigenvalues().get(size-1)
                  + ")");
        }
        result = new CholeskyDecomposition().CholeskyDecomposition(matrix, true);
        break;
      case Spectral:
        // negative eigenvalues set to zero
        for (int i=0; i<size; i++){
            diagonal.set(i, i, Math.sqrt(Math.max((jd.eigenvalues().get(i)), 0.0)));
        }
        if(true)
        throw new UnsupportedOperationException("work in progress");
        //result = jd.eigenvectors() * diagonal;
        normalizePseudoRoot(matrix, result);
        break;
      case Hypersphere:
        // negative eigenvalues set to zero
        negative=false;
        for (int i=0; i<size; ++i){
            diagonal.set(i, i, Math.sqrt(Math.max(jd.eigenvalues().get(i), 0.0)));
            if (jd.eigenvalues().get(i)<0.0) {negative=true;}
        }
        if(true)
        throw new UnsupportedOperationException("work in progress");
        //result = jd.eigenvectors() * diagonal;
        normalizePseudoRoot(matrix, result);

        if (negative){
            throw new UnsupportedOperationException("work in progress");
            //result = hypersphereOptimize(matrix, result, false);
        }
        break;
      case LowerDiagonal:
        // negative eigenvalues set to zero
        negative=false;
        for (int i=0; i<size; ++i){
            diagonal.set(i, i, Math.sqrt(Math.max(jd.eigenvalues().get(i), 0.0)));
            if (jd.eigenvalues().get(i)<0.0) {negative=true;}
        }
        if(true)
        throw new UnsupportedOperationException("work in progress");
        //result = jd.eigenvectors() * diagonal;
        normalizePseudoRoot(matrix, result);

        if (negative)
            throw new UnsupportedOperationException("work in progress");
            //result = hypersphereOptimize(matrix, result, true);
        break;
      case Higham: {
          int maxIterations = 40;
          double tol = 1e-6;
          if(true)
          throw new UnsupportedOperationException("work in progress");
          //result = highamImplementation(matrix, maxIterations, tol);
          result = new CholeskyDecomposition().CholeskyDecomposition(result, true);
        }
        break;
      default:
        throw new IllegalArgumentException(unknown_salvaging_algorithm);
    }
    return result;
}

/*
const Disposable<Matrix> rankReducedSqrt(const Matrix& matrix,
                                         Size maxRank,
                                         Real componentRetainedPercentage,
                                         SalvagingAlgorithm::Type sa) {
    Size size = matrix.rows();

    #if defined(QL_EXTRA_SAFETY_CHECKS)
    checkSymmetry(matrix);
    #else
    QL_REQUIRE(size == matrix.columns(),
               "non square matrix: " << size << " rows, " <<
               matrix.columns() << " columns");
    #endif

    QL_REQUIRE(componentRetainedPercentage>0.0,
               "no eigenvalues retained");

    QL_REQUIRE(componentRetainedPercentage<=1.0,
               "percentage to be retained > 100%");

    QL_REQUIRE(maxRank>=1,
               "max rank required < 1");

    // spectral (a.k.a Principal Component) analysis
    SymmetricSchurDecomposition jd(matrix);
    Array eigenValues = jd.eigenvalues();

    // salvaging algorithm
    switch (sa) {
      case SalvagingAlgorithm::None:
        // eigenvalues are sorted in decreasing order
        QL_REQUIRE(eigenValues[size-1]>=-1e-16,
                   "negative eigenvalue(s) ("
                   << std::scientific << eigenValues[size-1]
                   << ")");
        break;
      case SalvagingAlgorithm::Spectral:
        // negative eigenvalues set to zero
        for (Size i=0; i<size; ++i)
            eigenValues[i] = std::max<Real>(eigenValues[i], 0.0);
        break;
      case SalvagingAlgorithm::Higham:
          {
              int maxIterations = 40;
              Real tolerance = 1e-6;
              Matrix adjustedMatrix = highamImplementation(matrix, maxIterations, tolerance);
              jd = SymmetricSchurDecomposition(adjustedMatrix);
              eigenValues = jd.eigenvalues();
          }
          break;
      default:
        QL_FAIL("unknown or invalid salvaging algorithm");
    }

    // factor reduction
    Real enough = componentRetainedPercentage *
                  std::accumulate(eigenValues.begin(),
                                  eigenValues.end(), 0.0);
    if (componentRetainedPercentage == 1.0) {
        // numerical glitches might cause some factors to be discarded
        enough *= 1.1;
    }
    // retain at least one factor
    Real components = eigenValues[0];
    Size retainedFactors = 1;
    for (Size i=1; components<enough && i<size; ++i) {
        components += eigenValues[i];
        retainedFactors++;
    }
    // output is granted to have a rank<=maxRank
    retainedFactors=std::min(retainedFactors, maxRank);

    Matrix diagonal(size, retainedFactors, 0.0);
    for (Size i=0; i<retainedFactors; ++i)
        diagonal[i][i] = std::sqrt(eigenValues[i]);
    Matrix result = jd.eigenvectors() * diagonal;

    normalizePseudoRoot(matrix, result);
    return result;
}

*/
}

  
   
