package org.jquantlib.testsuite.math;

public class MinPack {

/*
Minpack_f77.java copyright claim:

This software is based on the public domain MINPACK routines.
It was translated from FORTRAN to Java by a US government employee
on official time.  Thus this software is also in the public domain.


The translator's mail address is:

Steve Verrill
USDA Forest Products Laboratory
1 Gifford Pinchot Drive
Madison, Wisconsin
53705


The translator's e-mail address is:

steve@ws13.fpl.fs.fed.us


***********************************************************************

DISCLAIMER OF WARRANTIES:

THIS SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND.
THE TRANSLATOR DOES NOT WARRANT, GUARANTEE OR MAKE ANY REPRESENTATIONS
REGARDING THE SOFTWARE OR DOCUMENTATION IN TERMS OF THEIR CORRECTNESS,
RELIABILITY, CURRENCY, OR OTHERWISE. THE ENTIRE RISK AS TO
THE RESULTS AND PERFORMANCE OF THE SOFTWARE IS ASSUMED BY YOU.
IN NO CASE WILL ANY PARTY INVOLVED WITH THE CREATION OR DISTRIBUTION
OF THE SOFTWARE BE LIABLE FOR ANY DAMAGE THAT MAY RESULT FROM THE USE
OF THIS SOFTWARE.

Sorry about that.

***********************************************************************


History:

Date        Translator        Changes

11/3/00     Steve Verrill     Translated

*/


/**
*
*<p>
*This class contains Java translations of the MINPACK nonlinear least
*squares routines.  As of November 2000, it does not yet contain
*the MINPACK solvers of systems of nonlinear equations.  They should
*be added in the Spring of 2001.<p>
*The original FORTRAN MINPACK package was produced by
*Burton S. Garbow, Kenneth E. Hillstrom, and Jorge J. More
*as part of the Argonne National Laboratory MINPACK project, March 1980.
*
*<p>
*<b>IMPORTANT:</b>  The "_f77" suffixes indicate that these routines use
*FORTRAN style indexing.  For example, you will see
*<pre>
*   for (i = 1; i <= n; i++)
*</pre>
*rather than
*<pre>
*   for (i = 0; i < n; i++)
*</pre>
*To use the "_f77" routines you will have to declare your vectors
*and matrices to be one element larger (e.g., v[101] rather than
*v[100], and a[101][101] rather than a[100][100]), and you will have
*to fill elements 1 through n rather than elements 0 through n - 1.
*Versions of these programs that use C/Java style indexing might
*eventually be available.  They would end with the suffix "_j".
*
*<p>
*This class was translated by a statistician from FORTRAN versions of
*lmder and lmdif.  It is NOT an official translation.  It wastes
*memory by failing to use the first elements of vectors.  When
*public domain Java optimization routines become available
*from the people who produced MINPACK, then <b>THE CODE PRODUCED
*BY THE NUMERICAL ANALYSTS SHOULD BE USED</b>.
*
*<p>
*Meanwhile, if you have suggestions for improving this
*code, please contact Steve Verrill at steve@ws13.fpl.fs.fed.us.
*
*@author (translator)Steve Verrill
*@version .5 --- November 3, 2000
*
*/


//epsmch is the machine precision

static final double epsmch = 2.22044604926e-16;

//minmag is the smallest magnitude

static final double minmag = 2.22507385852e-308;

static final double zero = 0.0;
static final double one =  1.0;
static final double p0001 = .0001;
static final double p001 =  .001;
static final double p05 =   .05;
static final double p1  =   .1;
static final double p25 =   .25;
static final double p5  =   .5;
static final double p75 =   .75;


/**
*
*<p>
*The qrfac_f77 method uses Householder transformations with column
*pivoting (optional) to compute a QR factorization of the
*m by n matrix A.  That is, qrfac_f77 determines an orthogonal
*matrix Q, a permutation matrix P, and an upper trapezoidal
*matrix R with diagonal elements of nonincreasing magnitude,
*such that AP = QR.
*<p>
*Translated by Steve Verrill on November 17, 2000
*from the FORTRAN MINPACK source produced by Garbow, Hillstrom, and More.<p>
*
*
*@param  m      The number of rows of A.
*@param  n      The number of columns of A.
*@param  a      A is an m by n array.  On input A contains the matrix for
*               which the QR factorization is to be computed.  On output
*               the strict upper trapezoidal part of A contains the strict
*               upper trapezoidal part of R, and the lower trapezoidal
*               part of A contains a factored form of Q.
*@param  pivot  pivot is a logical input variable.  If pivot is set true,
*               then column pivoting is enforced.  If pivot is set false,
*               then no column pivoting is done.
*@param  ipvt   ipvt is an integer output array.  ipvt
*               defines the permutation matrix P such that A*P = Q*R.
*               Column j of P is column ipvt[j] of the identity matrix.
*               If pivot is false, ipvt is not referenced.
*@param  rdiag  rdiag is an output array of length n which contains the
*               diagonal elements of R.
*@param  acnorm acnorm is an output array of length n which contains the
*               norms of the corresponding columns of the input matrix A.
*@param  wa     wa is a work array of length n.
*
*
*/


public void qrfac_f77(final int m, final int n, final double a[][], final boolean pivot,
                        final int ipvt[], final double rdiag[], final double acnorm[],
                        final double wa[]) {

/*

Here is a copy of the qrfac FORTRAN documentation:


  subroutine qrfac(m,n,a,lda,pivot,ipvt,lipvt,rdiag,acnorm,wa)

  integer m,n,lda,lipvt
  integer ipvt(lipvt)
  logical pivot
  double precision a(lda,n),rdiag(n),acnorm(n),wa(n)

c     **********
c
c     subroutine qrfac
c
c     this subroutine uses householder transformations with column
c     pivoting (optional) to compute a qr factorization of the
c     m by n matrix a. that is, qrfac determines an orthogonal
c     matrix q, a permutation matrix p, and an upper trapezoidal
c     matrix r with diagonal elements of nonincreasing magnitude,
c     such that a*p = q*r. the householder transformation for
c     column k, k = 1,2,...,min(m,n), is of the form
c
c                           t
c           i - (1/u(k))*u*u
c
c     where u has zeros in the first k-1 positions. the form of
c     this transformation and the method of pivoting first
c     appeared in the corresponding linpack subroutine.
c
c     the subroutine statement is
c
c       subroutine qrfac(m,n,a,lda,pivot,ipvt,lipvt,rdiag,acnorm,wa)
c
c     where
c
c       m is a positive integer input variable set to the number
c         of rows of a.
c
c       n is a positive integer input variable set to the number
c         of columns of a.
c
c       a is an m by n array. on input a contains the matrix for
c         which the qr factorization is to be computed. on output
c         the strict upper trapezoidal part of a contains the strict
c         upper trapezoidal part of r, and the lower trapezoidal
c         part of a contains a factored form of q (the non-trivial
c         elements of the u vectors described above).
c
c       lda is a positive integer input variable not less than m
c         which specifies the leading dimension of the array a.
c
c       pivot is a logical input variable. if pivot is set true,
c         then column pivoting is enforced. if pivot is set false,
c         then no column pivoting is done.
c
c       ipvt is an integer output array of length lipvt. ipvt
c         defines the permutation matrix p such that a*p = q*r.
c         column j of p is column ipvt(j) of the identity matrix.
c         if pivot is false, ipvt is not referenced.
c
c       lipvt is a positive integer input variable. if pivot is false,
c         then lipvt may be as small as 1. if pivot is true, then
c         lipvt must be at least n.
c
c       rdiag is an output array of length n which contains the
c         diagonal elements of r.
c
c       acnorm is an output array of length n which contains the
c         norms of the corresponding columns of the input matrix a.
c         if this information is not needed, then acnorm can coincide
c         with rdiag.
c
c       wa is a work array of length n. if pivot is false, then wa
c         can coincide with rdiag.
c
c     subprograms called
c
c       minpack-supplied ... dpmpar,enorm
c
c       fortran-supplied ... dmax1,dsqrt,min0
c
c     argonne national laboratory. minpack project. march 1980.
c     burton s. garbow, kenneth e. hillstrom, jorge j. more
c
c     **********

*/


  int i,j,jp1,k,kmax,minmn;
//  double ajnorm,one,p05,sum,temp,zero;
  double ajnorm,sum,temp;
  double fac;

  final double tempvec[] = new double[m+1];

//  one = 1.0;
//  p05 = .05;
//  zero = 0.0;


//Compute the initial column norms and initialize several arrays.

  for (j = 1; j <= n; j++) {

     for (i = 1; i <= m; i++) {

        tempvec[i] = a[i][j];

     }

//     acnorm[j] = Minpack_f77.enorm_f77(m,a[1][j]);

     acnorm[j] = enorm_f77(m,tempvec);

     rdiag[j] = acnorm[j];
     wa[j] = rdiag[j];
     if (pivot) ipvt[j] = j;

  }

//Reduce A to R with Householder transformations.

  minmn = Math.min(m,n);

  for (j = 1; j <= minmn; j++) {

     if (pivot) {

//Bring the column of largest norm into the pivot position.

        kmax = j;

        for (k = j; k <= n; k++) {

           if (rdiag[k] > rdiag[kmax]) kmax = k;

        }

        if (kmax != j) {

           for (i = 1; i <= m; i++) {

              temp = a[i][j];
              a[i][j] = a[i][kmax];
              a[i][kmax] = temp;

           }

           rdiag[kmax] = rdiag[j];
           wa[kmax] = wa[j];
           k = ipvt[j];
           ipvt[j] = ipvt[kmax];
           ipvt[kmax] = k;

        }

     }

//Compute the Householder transformation to reduce the
//j-th column of A to a multiple of the j-th unit vector.

     for (i = j; i <= m; i++) {

        tempvec[i - j + 1] = a[i][j];

     }

//     ajnorm = Minpack_f77.enorm_f77(m-j+1,a[j][j]);
     ajnorm = enorm_f77(m-j+1,tempvec);

     if (ajnorm != zero) {

        if (a[j][j] < zero) ajnorm = -ajnorm;

        for (i = j; i <= m; i++) {

           a[i][j] /= ajnorm;

        }

        a[j][j] += one;

//Apply the transformation to the remaining columns
//and update the norms.

        jp1 = j + 1;

        if (n >= jp1) {

           for (k = jp1; k <= n; k++) {

              sum = zero;

              for (i = j; i <= m; i++) {

                 sum += a[i][j]*a[i][k];

              }

              temp = sum/a[j][j];

              for (i = j; i <= m; i++) {

                 a[i][k] -= temp*a[i][j];

              }

              if (pivot && rdiag[k] != zero) {

                 temp = a[j][k]/rdiag[k];
                 rdiag[k] *= Math.sqrt(Math.max(zero,one-temp*temp));

                 fac = rdiag[k]/wa[k];
                 if (p05*fac*fac <= epsmch) {

                    for (i = jp1; i <= m; i++) {

                       tempvec[i - j] = a[i][k];

                    }

//                    rdiag[k] = Minpack_f77.enorm_f77(m-j,a[jp1][k]);
                    rdiag[k] = enorm_f77(m-j,tempvec);
                    wa[k] = rdiag[k];

                 }

              }

           }

        }

     }

     rdiag[j] = -ajnorm;

  }

  return;

}




/**
*
*<p>
*Given an m by n matrix A, an n by n diagonal matrix D,
*and an m-vector b, the problem is to determine an x which
*solves the system
*<pre>
*    Ax = b ,     Dx = 0 ,
*</pre>
*in the least squares sense.
*<p>
*This method completes the solution of the problem
*if it is provided with the necessary information from the
*QR factorization, with column pivoting, of A.  That is, if
*AP = QR, where P is a permutation matrix, Q has orthogonal
*columns, and R is an upper triangular matrix with diagonal
*elements of nonincreasing magnitude, then qrsolv_f77 expects
*the full upper triangle of R, the permutation matrix P,
*and the first n components of (Q transpose)b.  The system
*<pre>
*           Ax = b, Dx = 0, is then equivalent to
*
*                 t     t
*           Rz = Q b,  P DPz = 0 ,
*</pre>
*where x = Pz.  If this system does not have full rank,
*then a least squares solution is obtained.  On output qrsolv_f77
*also provides an upper triangular matrix S such that
*<pre>
*            t  t              t
*           P (A A + DD)P = S S .
*</pre>
*S is computed within qrsolv_f77 and may be of separate interest.
*<p>
*Translated by Steve Verrill on November 17, 2000
*from the FORTRAN MINPACK source produced by Garbow, Hillstrom, and More.<p>
*
*
*@param  n       The order of r.
*@param  r       r is an n by n array.  On input the full upper triangle
*                must contain the full upper triangle of the matrix R.
*                On output the full upper triangle is unaltered, and the
*                strict lower triangle contains the strict upper triangle
*                (transposed) of the upper triangular matrix S.
*@param ipvt     ipvt is an integer input array of length n which defines the
*                permutation matrix P such that AP = QR.  Column j of P
*                is column ipvt[j] of the identity matrix.
*@param diag     diag is an input array of length n which must contain the
*                diagonal elements of the matrix D.
*@param qtb      qtb is an input array of length n which must contain the first
*                n elements of the vector (Q transpose)b.
*@param x        x is an output array of length n which contains the least
*                squares solution of the system Ax = b, Dx = 0.
*@param sdiag    sdiag is an output array of length n which contains the
*                diagonal elements of the upper triangular matrix S.
*@param wa       wa is a work array of length n.
*
*
*/


public void qrsolv_f77(final int n, final double r[][], final int ipvt[],
                         final double  diag[], final double qtb[],
                         final double x[], final double sdiag[],
                         final double wa[]) {

/*

Here is a copy of the qrsolv FORTRAN documentation:


  subroutine qrsolv(n,r,ldr,ipvt,diag,qtb,x,sdiag,wa)
  integer n,ldr
  integer ipvt(n)
  double precision r(ldr,n),diag(n),qtb(n),x(n),sdiag(n),wa(n)

c     **********
c
c     subroutine qrsolv
c
c     given an m by n matrix a, an n by n diagonal matrix d,
c     and an m-vector b, the problem is to determine an x which
c     solves the system
c
c           a*x = b ,     d*x = 0 ,
c
c     in the least squares sense.
c
c     this subroutine completes the solution of the problem
c     if it is provided with the necessary information from the
c     qr factorization, with column pivoting, of a. that is, if
c     a*p = q*r, where p is a permutation matrix, q has orthogonal
c     columns, and r is an upper triangular matrix with diagonal
c     elements of nonincreasing magnitude, then qrsolv expects
c     the full upper triangle of r, the permutation matrix p,
c     and the first n components of (q transpose)*b. the system
c     a*x = b, d*x = 0, is then equivalent to
c
c                  t       t
c           r*z = q *b ,  p *d*p*z = 0 ,
c
c     where x = p*z. if this system does not have full rank,
c     then a least squares solution is obtained. on output qrsolv
c     also provides an upper triangular matrix s such that
c
c            t   t               t
c           p *(a *a + d*d)*p = s *s .
c
c     s is computed within qrsolv and may be of separate interest.
c
c     the subroutine statement is
c
c       subroutine qrsolv(n,r,ldr,ipvt,diag,qtb,x,sdiag,wa)
c
c     where
c
c       n is a positive integer input variable set to the order of r.
c
c       r is an n by n array. on input the full upper triangle
c         must contain the full upper triangle of the matrix r.
c         on output the full upper triangle is unaltered, and the
c         strict lower triangle contains the strict upper triangle
c         (transposed) of the upper triangular matrix s.
c
c       ldr is a positive integer input variable not less than n
c         which specifies the leading dimension of the array r.
c
c       ipvt is an integer input array of length n which defines the
c         permutation matrix p such that a*p = q*r. column j of p
c         is column ipvt(j) of the identity matrix.
c
c       diag is an input array of length n which must contain the
c         diagonal elements of the matrix d.
c
c       qtb is an input array of length n which must contain the first
c         n elements of the vector (q transpose)*b.
c
c       x is an output array of length n which contains the least
c         squares solution of the system a*x = b, d*x = 0.
c
c       sdiag is an output array of length n which contains the
c         diagonal elements of the upper triangular matrix s.
c
c       wa is a work array of length n.
c
c     subprograms called
c
c       fortran-supplied ... dabs,dsqrt
c
c     argonne national laboratory. minpack project. march 1980.
c     burton s. garbow, kenneth e. hillstrom, jorge j. more
c
c     **********

*/


  int i,j,jp1,k,kp1,l,nsing;
//  double cos,cotan,p5,p25,qtbpj,sin,sum,tan,temp,zero;
  double cos,cotan,qtbpj,sin,sum,tan,temp;

//  p5 = .5;
//  p25 = .25;
//  zero = 0.0;

//Copy R and (Q transpose)b to preserve input and initialize S.
//In particular, save the diagonal elements of R in x.

  for (j = 1; j <= n; j++) {

     for (i = j; i <= n; i++) {

        r[i][j] = r[j][i];

     }

     x[j] = r[j][j];
     wa[j] = qtb[j];

  }

//Eliminate the diagonal matrix D using a Givens rotation.

  for (j = 1; j <= n; j++) {

//Prepare the row of D to be eliminated, locating the
//diagonal element using P from the QR factorization.

     l = ipvt[j];

     if (diag[l] != zero) {

        for (k = j; k <= n; k++) {

           sdiag[k] = zero;

        }

        sdiag[j] = diag[l];

//The transformations to eliminate the row of D
//modify only a single element of (Q transpose)b
//beyond the first n, which is initially zero.    ??????

        qtbpj = zero;

        for (k = j; k <= n; k++) {

//Determine a Givens rotation which eliminates the
//appropriate element in the current row of D.

           if (sdiag[k] != zero) {

              if (Math.abs(r[k][k]) < Math.abs(sdiag[k])) {

                 cotan = r[k][k]/sdiag[k];
                 sin = p5/Math.sqrt(p25+p25*cotan*cotan);
                 cos = sin*cotan;

              } else {

                 tan = sdiag[k]/r[k][k];
                 cos = p5/Math.sqrt(p25+p25*tan*tan);
                 sin = cos*tan;

              }

//Compute the modified diagonal element of R and
//the modified element of ((Q transpose)b,0).

              r[k][k] = cos*r[k][k] + sin*sdiag[k];
              temp = cos*wa[k] + sin*qtbpj;
              qtbpj = -sin*wa[k] + cos*qtbpj;
              wa[k] = temp;

//Accumulate the tranformation in the row of S.

              kp1 = k + 1;

              for (i = kp1; i <= n; i++) {

                 temp = cos*r[i][k] + sin*sdiag[i];
                 sdiag[i] = -sin*r[i][k] + cos*sdiag[i];
                 r[i][k] = temp;

              }

           }

        }

     }

//Store the diagonal element of S and restore
//the corresponding diagonal element of R.

     sdiag[j] = r[j][j];
     r[j][j] = x[j];

  }

//Solve the triangular system for z. if the system is
//singular, then obtain a least squares solution.

  nsing = n;

  for (j = 1; j <= n; j++) {

     if (sdiag[j] == zero && nsing == n) nsing = j - 1;
     if (nsing < n) wa[j] = zero;

  }

//  if (nsing >= 1) {

  for (k = 1; k <= nsing; k++) {

     j = nsing - k + 1;
     sum = zero;
     jp1 = j + 1;

//     if (nsing >= jp1) {

     for (i = jp1; i <= nsing; i++) {

        sum += r[i][j]*wa[i];

     }

//     }

     wa[j] = (wa[j] - sum)/sdiag[j];

  }

//  }

//Permute the components of z back to components of x.

  for (j = 1; j <= n; j++) {

     l = ipvt[j];
     x[l] = wa[j];

  }

  return;

}


/**
*
*<p>
*The enorm_f77 method calculates the Euclidean norm of a vector.
*<p>
*Translated by Steve Verrill on November 14, 2000
*from the FORTRAN MINPACK source produced by Garbow, Hillstrom, and More.<p>
*
*
*@param n  The length of the vector, x.
*@param x  The vector whose Euclidean norm is to be calculated.
*
*/


   private double enorm_f77(final int n, final double x[]) {


/*

Here is a copy of the enorm FORTRAN documentation:


      double precision function enorm(n,x)
      integer n
      double precision x(n)

c     **********
c
c     function enorm
c
c     given an n-vector x, this function calculates the
c     euclidean norm of x.
c
c     the euclidean norm is computed by accumulating the sum of
c     squares in three different sums. the sums of squares for the
c     small and large components are scaled so that no overflows
c     occur. non-destructive underflows are permitted. underflows
c     and overflows do not occur in the computation of the unscaled
c     sum of squares for the intermediate components.
c     the definitions of small, intermediate and large components
c     depend on two constants, rdwarf and rgiant. the main
c     restrictions on these constants are that rdwarf**2 not
c     underflow and rgiant**2 not overflow. the constants
c     given here are suitable for every known computer.
c
c     the function statement is
c
c       double precision function enorm(n,x)
c
c     where
c
c       n is a positive integer input variable.
c
c       x is an input array of length n.
c
c     subprograms called
c
c       fortran-supplied ... dabs,dsqrt
c
c     argonne national laboratory. minpack project. march 1980.
c     burton s. garbow, kenneth e. hillstrom, jorge j. more
c
c     **********

*/


      int i;
//      double agiant,floatn,one,rdwarf,rgiant,s1,s2,s3,xabs,
//            x1max,x3max,zero;
      double agiant,floatn,rdwarf,rgiant,s1,s2,s3,xabs,
            x1max,x3max;
      double enorm;

//      one = 1.0;
//      zero = 0.0;
      rdwarf = 3.834e-20;
      rgiant = 1.304e+19;

      s1 = zero;
      s2 = zero;
      s3 = zero;
      x1max = zero;
      x3max = zero;
      floatn = n;
      agiant = rgiant/floatn;

      for (i = 1; i <= n; i++) {

         xabs = Math.abs(x[i]);

         if (xabs <= rdwarf || xabs >= agiant) {

            if (xabs > rdwarf) {

// Sum for large components.

               if (xabs > x1max) {

                  s1 = one + s1*(x1max/xabs)*(x1max/xabs);
                  x1max = xabs;

               } else {

                  s1 += (xabs/x1max)*(xabs/x1max);

               }


            } else {

// Sum for small components.

               if (xabs > x3max) {

                  s3 = one + s3*(x3max/xabs)*(x3max/xabs);
                  x3max = xabs;

               } else {

                  if (xabs != zero) s3 += (xabs/x3max)*(xabs/x3max);

               }

            }

         } else {

// Sum for intermediate components.

            s2 += xabs*xabs;

         }

      }

// Calculation of norm.

      if (s1 != zero) {

         enorm = x1max*Math.sqrt(s1+(s2/x1max)/x1max);

      } else {

         if (s2 != zero) {

            if (s2 >= x3max) {

               enorm = Math.sqrt(s2*(one+(x3max/s2)*(x3max*s3)));

            } else {

               enorm = Math.sqrt(x3max*((s2/x3max)+(x3max*s3)));

            }

         } else {

            enorm = x3max*Math.sqrt(s3);

         }

      }

      return enorm;

   }


}
