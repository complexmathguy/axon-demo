/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.validator;

import org.springframework.util.Assert;

import com.harbormaster.api.*;

public class EmployeeValidator {
		
	/**
	 * default constructor
	 */
	protected EmployeeValidator() {	
	}
	
	/**
	 * factory method
	 */
	static public EmployeeValidator getInstance() {
		return new EmployeeValidator();
	}
		
	/**
	 * handles creation validation for a Employee
	 */
	public void validate( CreateEmployeeCommand employee )throws Exception {
		Assert.notNull( employee, "CreateEmployeeCommand should not be null" );
//		Assert.isNull( employee.getEmployeeId(), "CreateEmployeeCommand identifier should be null" );
		Assert.notNull( employee.getFirstName(), "Field CreateEmployeeCommand.firstName should not be null" );
		Assert.notNull( employee.getLastName(), "Field CreateEmployeeCommand.lastName should not be null" );
	}

	/**
	 * handles update validation for a Employee
	 */
	public void validate( UpdateEmployeeCommand employee ) throws Exception {
		Assert.notNull( employee, "UpdateEmployeeCommand should not be null" );
		Assert.notNull( employee.getEmployeeId(), "UpdateEmployeeCommand identifier should not be null" );
		Assert.notNull( employee.getFirstName(), "Field UpdateEmployeeCommand.firstName should not be null" );
		Assert.notNull( employee.getLastName(), "Field UpdateEmployeeCommand.lastName should not be null" );
    }

	/**
	 * handles delete validation for a Employee
	 */
    public void validate( DeleteEmployeeCommand employee ) throws Exception {
		Assert.notNull( employee, "{commandAlias} should not be null" );
		Assert.notNull( employee.getEmployeeId(), "DeleteEmployeeCommand identifier should not be null" );
	}
	
	/**
	 * handles fetchOne validation for a Employee
	 */
	public void validate( EmployeeFetchOneSummary summary ) throws Exception {
		Assert.notNull( summary, "EmployeeFetchOneSummary should not be null" );
		Assert.notNull( summary.getEmployeeId(), "EmployeeFetchOneSummary identifier should not be null" );
	}



}
