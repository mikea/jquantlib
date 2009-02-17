/*
 Copyright (C) 2008 
  
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

package org.jquantlib.testsuite.math.optimization;
import static org.junit.Assert.fail;

import org.jquantlib.math.Array;
import org.jquantlib.math.Closeness;
import org.jquantlib.math.optimization.EndCriteria;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 *
 *
 */


public class EndCriteriaTest {
	
	private final static Logger logger = LoggerFactory.getLogger(EndCriteriaTest.class);
	
	private final EndCriteria ec;
	
	public EndCriteriaTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");

		/*
		   Set the parameters of EndCriteria -- will determine the corresponding test parameters
		   Integer maxIterations,
                    Integer maxStationaryStateIterations,
                    Double rootEpsilon,
                    Double functionEpsilon,
                    Double gradientNormEpsilon
		*/
		
		//FIXME: this initialization fails!
		ec = new EndCriteria(100, 30, 0.05, 0.08, 0.2);
		// ec=null; // TODO: remove this line after EndCriteria is ready
	}
	
	@Test
	public void dummyTest(){
	}
	
	@Ignore("End Criteria needs code review")
	//maxIterations = 100
	//iteration >= maxIterations_
	public void testCheckMaxIterations() {
		//create the matching constraint type -- MaxIterations
		org.jquantlib.math.optimization.EndCriteria.CriteriaType cType = org.jquantlib.math.optimization.EndCriteria.CriteriaType.None;
		//checkMaxIterations(int iteration, CriteriaType ecType)
		//Case 0 checks if the max. number of iterations has been met
		//rest of cases checks for correct behaviour when the max. number of iterations criteria is not being met
		//Case 0: iteration >= maxIterations_ 
		boolean isLessThanMaxIter = false;
		int  iteration = 100;
				
		logger.info("testCheckMaxIterations : maxIterations_ =" + ec.getMaxIterations());		
		
		isLessThanMaxIter = ec.checkMaxIterations(iteration, cType);
		logger.info("testCheckMaxIterations : iteration =" + iteration);
		logger.info("testCheckMaxIterations : isLessThanMaxIter =" + isLessThanMaxIter);		
		if (!isLessThanMaxIter && cType != org.jquantlib.math.optimization.EndCriteria.CriteriaType.MaxIterations) {
			fail("EndCriteria test CheckMaxIterations failed!");
		}
		logger.info("testCheckMaxIterations -- SUCCESS : Case iteration >= maxIterations_ ");
		
		//Case 1: iteration  < maxIterations_ 
		iteration = 99;
		
		isLessThanMaxIter = ec.checkMaxIterations(iteration, cType);//maxIterations is a 100 as set in ec
		logger.info("testCheckMaxIterations : iteration =" + iteration);
		logger.info("testCheckMaxIterations : isLessThanMaxIter =" + isLessThanMaxIter);		
		if (isLessThanMaxIter) {
			fail("EndCriteria test CheckMaxIterations failed!");
		}
		logger.info("testCheckMaxIterations -- SUCCESS : Case iteration  < maxIterations_");
	}
	
	
	@Ignore("End Criteria needs code review")
	//maxStationaryStateIterations = 30
	//rootEpsilon = 0.05
	//Math.abs( xNew-xOld) < rootEpsilon_ 
	//statStateIterations > maxStationaryStateIterations_
	public void testCheckStationaryPoint() {
		//create the matching constraint type -- StationaryPoint
	    org.jquantlib.math.optimization.EndCriteria.CriteriaType cType = org.jquantlib.math.optimization.EndCriteria.CriteriaType.None;
		//checkStationaryPoint(double xOld, double xNew,  int statStateIterations,  CriteriaType ecType)
		//Case 0 checks if the stationary point falls within the difference limit set, and that the max. number of iterations has been met
		//rest of cases checks for correct behaviour when the difference limit or max. number of iterations criteria is not being met
		//Case 0: Math.abs( xNew-xOld) < rootEpsilon_ 
		//            statStateIterations > maxStationaryStateIterations_
		boolean IsStatPt = false;
		logger.info("testCheckStationaryPoint : rootEpsilon =" + ec.getRootEpsilon());
		logger.info("testCheckStationaryPoint : maxStationaryStateIterations_ =" + ec.getMaxStationaryStateIterations());
		
		double xNew = 0.08, xOld = 0.1;
		int statStateIterations = 100;
		logger.info("testCheckStationaryPoint : xNew =" + xNew);
		logger.info("testCheckStationaryPoint : xOld =" + xOld);		
		logger.info("testCheckStationaryPoint : statStateIterations =" + statStateIterations);		
		IsStatPt = ec.checkStationaryPoint(xOld,xNew,statStateIterations,cType);
        if (!IsStatPt && cType != EndCriteria.CriteriaType.StationaryPoint){
			fail("testCheckStationaryPoint failed ");
		}
		logger.info("testCheckStationaryPoint -- SUCCESS : Math.abs( xNew-xOld) < rootEpsilon_ ; statStateIterations > maxStationaryStateIterations_ return TRUE");
		
		//Case 1: Math.abs( xNew-xOld) < rootEpsilon_ 
		//            statStateIterations <= maxStationaryStateIterations_
		statStateIterations = 29;
		xOld = 0.1;
		xNew = 0.08;
		logger.info("testCheckStationaryPoint : xNew =" + xNew);
		logger.info("testCheckStationaryPoint : xOld =" + xOld);		
		logger.info("testCheckStationaryPoint : statStateIterations =" + statStateIterations);		
		IsStatPt = ec.checkStationaryPoint(xOld,xNew,statStateIterations,cType);
        if (IsStatPt ){
			fail("testCheckStationaryPoint failed ");
		}
		logger.info("testCheckStationaryPoint -- SUCCESS : Math.abs( xNew-xOld) < rootEpsilon_ ;statStateIterations <= maxStationaryStateIterations_ return FALSE");
		
		//Case 2: Math.abs( xNew-xOld) >= rootEpsilon_ 
		//            statStateIterations <= maxStationaryStateIterations_
		statStateIterations = 30;
		xOld = 0.1;
		xNew = 0.18;
		logger.info("testCheckStationaryPoint : xNew =" + xNew);
		logger.info("testCheckStationaryPoint : xOld =" + xOld);		
		logger.info("testCheckStationaryPoint : statStateIterations =" + statStateIterations);		
		IsStatPt = ec.checkStationaryPoint(xOld,xNew,statStateIterations,cType);
        if (IsStatPt ){
			fail("testCheckStationaryPoint failed ");
		}
		logger.info("testCheckStationaryPoint -- SUCCESS : Math.abs( xNew-xOld) >= rootEpsilon_  ;statStateIterations <= maxStationaryStateIterations_ return FALSE");
		
		//Case 3: Math.abs( xNew-xOld) >= rootEpsilon_ 
		//            statStateIterations > maxStationaryStateIterations_
		statStateIterations = 100;
		xOld = 0.1;
		xNew = 0.18;
		logger.info("testCheckStationaryPoint : xNew =" + xNew);
		logger.info("testCheckStationaryPoint : xOld =" + xOld);		
		logger.info("testCheckStationaryPoint : statStateIterations =" + statStateIterations);
		IsStatPt = ec.checkStationaryPoint(xOld,xNew,statStateIterations,cType);
        if (IsStatPt ){
			fail("testCheckStationaryPoint failed ");
		}
	    logger.info("testCheckStationaryPoint -- SUCCESS : Math.abs( xNew-xOld) >= rootEpsilon_  ;statStateIterations > maxStationaryStateIterations_   return FALSE");	
    }
	
	
	
	@Ignore("End Criteria needs code review")
    //maxStationaryStateIterations_ = 30
	//functionEpsilon = 0.08			
	public void testCheckStationaryFunctionValue() {
		//create the matching constraint type -- StationaryFunctionValue
	    org.jquantlib.math.optimization.EndCriteria.CriteriaType cType = org.jquantlib.math.optimization.EndCriteria.CriteriaType.StationaryFunctionValue;
		//checkStationaryFunctionValue(   double fxOld,  double fxNew,   int statStateIterations,  CriteriaType ecType)
		//Case 0 checks if the stationary point falls within the difference limit set, and that the max. number of iterations has been met
		//rest of cases checks for correct behaviour when the difference limit or max. number of iterations criteria is not being met
		//Case 0: Math.abs(fxNew-fxOld) < functionEpsilon_
		//            statStateIterations <= maxStationaryStateIterations_
		boolean IsStatFv = false;
		
		logger.info("testCheckStationaryFunctionValue : functionEpsilon =" + ec.getFunctionEpsilon());
		logger.info("testCheckStationaryFunctionValue : maxStationaryStateIterations_ =" + ec.getMaxStationaryStateIterations());
		
		double fxOld = 0.1, fxNew = 0.08;
		int statStateIterations = 100;
		logger.info("testCheckStationaryFunctionValue : fxNew =" + fxNew);
		logger.info("testCheckStationaryFunctionValue : fxOld =" + fxOld);		
		logger.info("testCheckStationaryFunctionValue : statStateIterations =" + statStateIterations);
		
		IsStatFv = ec.checkStationaryFunctionValue(fxOld,fxNew,statStateIterations,cType);
        if (!IsStatFv && cType != EndCriteria.CriteriaType.StationaryFunctionValue){
			fail("testCheckStationaryFunctionValue failed ");
		}
		logger.info("testCheckStationaryFunctionValue -- SUCCESS : Math.abs(fxNew-fxOld) < functionEpsilon_ ;statStateIterations <= maxStationaryStateIterations_   return TRUE");	
		
		//Case 1: Math.abs(fxNew-fxOld) < functionEpsilon_
		//            statStateIterations > maxStationaryStateIterations_
		fxOld = 0.1;
		fxNew = 0.08;
		statStateIterations = 100;
		logger.info("testCheckStationaryFunctionValue : fxNew =" + fxNew);
		logger.info("testCheckStationaryFunctionValue : fxOld =" + fxOld);		
		logger.info("testCheckStationaryFunctionValue : statStateIterations =" + statStateIterations);
		IsStatFv = ec.checkStationaryFunctionValue(fxOld,fxNew,statStateIterations,cType);
        if (!IsStatFv ){
			fail("testCheckStationaryFunctionValue failed ");
		}
		logger.info("testCheckStationaryFunctionValue -- SUCCESS : Math.abs(fxNew-fxOld) < functionEpsilon_ ;statStateIterations > maxStationaryStateIterations_   return FALSE");	
		
		//Case 2: Math.abs(fxNew-fxOld) >= functionEpsilon_
		//            statStateIterations > maxStationaryStateIterations_
		fxOld = 0.1;
		fxNew = 0.18;
		statStateIterations = 100;
		logger.info("testCheckStationaryFunctionValue : fxNew =" + fxNew);
		logger.info("testCheckStationaryFunctionValue : fxOld =" + fxOld);		
		logger.info("testCheckStationaryFunctionValue : statStateIterations =" + statStateIterations);
		
		IsStatFv = ec.checkStationaryFunctionValue(fxOld,fxNew,statStateIterations,cType);
        if (!IsStatFv ){
			fail("testCheckStationaryFunctionValue failed ");
		}
		logger.info("testCheckStationaryFunctionValue -- SUCCESS : Math.abs(fxNew-fxOld) >= functionEpsilon_ ;statStateIterations > maxStationaryStateIterations_   return FALSE");	
		
		//Case 3: Math.abs(fxNew-fxOld) >= functionEpsilon_
		//            statStateIterations <= maxStationaryStateIterations_
		fxOld = 0.1;
		fxNew = 0.18;
		statStateIterations = 30;
		logger.info("testCheckStationaryFunctionValue : fxNew =" + fxNew);
		logger.info("testCheckStationaryFunctionValue : fxOld =" + fxOld);		
		logger.info("testCheckStationaryFunctionValue : statStateIterations =" + statStateIterations);
		IsStatFv = ec.checkStationaryFunctionValue(0.1,0.18,30,cType);
        if (!IsStatFv ){
			fail("testCheckStationaryFunctionValue failed ");
		}
		logger.info("testCheckStationaryFunctionValue -- SUCCESS : Math.abs(fxNew-fxOld) >= functionEpsilon_ ;statStateIterations <= maxStationaryStateIterations_   return FALSE");	
	}
	
	
	
	@Ignore("End Criteria needs code review")
	//functionEpsilon_ = 0.08
	public void testCheckStationaryFunctionAccuracy() {
		//create the matching constraint type -- StationaryFunctionAccuracy
	    org.jquantlib.math.optimization.EndCriteria.CriteriaType cType = org.jquantlib.math.optimization.EndCriteria.CriteriaType.StationaryFunctionAccuracy;
		//checkStationaryFunctionAccuracy(   double f,  boolean positiveOptimization,  CriteriaType ecType) 
		//Case 0 checks if the optimization flag has been set and that the value being tested is less than the limit (function)
		//rest of cases checks for correct behaviour when the conditions are not being met
		//Case 0: positiveOptimization == true
		//            f < functionEpsilon_
		
		logger.info("testCheckStationaryFunctionAccuracy : functionEpsilon_ =" + ec.getFunctionEpsilon());
		logger.info("testCheckStationaryFunctionAccuracy : maxStationaryStateIterations_ =" + ec.getMaxStationaryStateIterations());
		
		boolean IsStatFa = false;
		IsStatFa = ec.checkStationaryFunctionAccuracy(0.07,true,cType);
        if (!IsStatFa && cType != EndCriteria.CriteriaType.StationaryFunctionAccuracy){
			fail("testCheckStationaryFunctionAccuracy failed ");
		}
		logger.info("testCheckStationaryFunctionAccuracy -- SUCCESS : positiveOptimization=true ;0.07 < functionEpsilon_   return TRUE");	
		
		//Case 1: positiveOptimization == true
		//            f >= functionEpsilon_
		IsStatFa = ec.checkStationaryFunctionAccuracy(0.12,false,cType);
        if (IsStatFa && cType != EndCriteria.CriteriaType.StationaryFunctionAccuracy){
			fail("testCheckStationaryFunctionAccuracy failed ");
			
		}
		logger.info("testCheckStationaryFunctionAccuracy -- SUCCESS : positiveOptimization=true ;0.12 >= functionEpsilon_   return FALSE");	
		
		//Case 2: positiveOptimization == false
		//            f >= functionEpsilon_
		IsStatFa = ec.checkStationaryFunctionAccuracy(0.12,false,cType);
        if (IsStatFa && cType != EndCriteria.CriteriaType.StationaryFunctionAccuracy){
			fail("testCheckStationaryFunctionAccuracy failed ");
			
		}
		logger.info("testCheckStationaryFunctionAccuracy -- SUCCESS : positiveOptimization=false ;0.12 >= functionEpsilon_   return FALSE");	
		
		//Case 3: positiveOptimization == false
		//            f < functionEpsilon_
		IsStatFa = ec.checkStationaryFunctionAccuracy(0.07,false,cType);
        if (IsStatFa && cType != EndCriteria.CriteriaType.StationaryFunctionAccuracy){
			fail("testCheckStationaryFunctionAccuracy failed ");
			
		}
		logger.info("testCheckStationaryFunctionAccuracy -- SUCCESS : positiveOptimization=false ;0.07 < functionEpsilon_   return FALSE");	
	}
	

	@Ignore("End Criteria needs code review")
	//gradientNormEpsilon = 0.2
	public void testCheckZeroGradientNorm() {
		//create the matching constraint type -- ZeroGradientNorm
	    org.jquantlib.math.optimization.EndCriteria.CriteriaType cType = org.jquantlib.math.optimization.EndCriteria.CriteriaType.StationaryFunctionAccuracy;
		//checkZeroGradientNorm(double gradientNorm,  CriteriaType ecType) 
		//Case 0 checks if the gradientNorm >= gradientNormEpsilon_
		//rest of cases checks for correct behaviour when the conditions are not being met
		//Case 0: gradientNorm < gradientNormEpsilon_
		
		
		logger.info("testCheckZeroGradientNorm : gradientNorm =" + ec.getGradientNormEpsilon());
		
		
		boolean IsZeroGraNorm = false;
		IsZeroGraNorm = ec.checkZeroGradientNorm(0.1,cType);
        if (!IsZeroGraNorm && cType != EndCriteria.CriteriaType.ZeroGradientNorm){
			fail("testCheckZeroGradientNorm failed ");
		}
		logger.info("testCheckZeroGradientNorm -- SUCCESS : 0.1 < gradientNormEpsilon_   return TRUE");	
		
		//Case 1: positiveOptimization == true
		//            f >= functionEpsilon_
		IsZeroGraNorm = ec.checkZeroGradientNorm(0.2,cType);
        if (IsZeroGraNorm && cType != EndCriteria.CriteriaType.ZeroGradientNorm){
			fail("testCheckZeroGradientNorm failed ");
			
		}
		logger.info("testCheckZeroGradientNorm -- SUCCESS : 0.2 >= gradientNormEpsilon_   return FALSE");	
		
		
	}
	
	//TO DO - Verify this should be removed
	//private boolean isArrayEqual(Array one,Array two,double precision){
	//	Array diffArray = one.operatorSubtractCopy(two);
	//	System.out.println("diffArray =" + "{"+diffArray.getData()[0]+","+diffArray.getData()[1]+","+diffArray.getData()[2]+"}");
	//	return Closeness.isCloseEnough(diffArray.dotProduct(diffArray,diffArray) , precision*precision );
	//	
	//}
}

