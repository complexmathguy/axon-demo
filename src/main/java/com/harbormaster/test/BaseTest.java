/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.test;

import java.util.logging.*;

/**
 * Base class for application Test classes.
 *
 * @author    Dev Team
 */
public class BaseTest
{
	/**
	 * hidden
	 */
	protected BaseTest() {
	}
	
	public static void runTheTest( Handler logHandler ) 
    {
         try {
		    new CompanyTest().setHandler(logHandler).startTest();
			Thread.sleep(timeToWait);
		    new DivisionTest().setHandler(logHandler).startTest();
			Thread.sleep(timeToWait);
		    new DepartmentTest().setHandler(logHandler).startTest();
			Thread.sleep(timeToWait);
		    new EmployeeTest().setHandler(logHandler).startTest();
			Thread.sleep(timeToWait);
        } catch( Throwable exc ) {
        	exc.printStackTrace();
        }
    }
	//-----------------------------------------------------
	// attributes
	//-----------------------------------------------------
	private static final Integer timeToWait = 5000; //milliseconds
}
