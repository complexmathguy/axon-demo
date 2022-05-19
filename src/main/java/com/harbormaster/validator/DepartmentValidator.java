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

public class DepartmentValidator {
		
	/**
	 * default constructor
	 */
	protected DepartmentValidator() {	
	}
	
	/**
	 * factory method
	 */
	static public DepartmentValidator getInstance() {
		return new DepartmentValidator();
	}
		
	/**
	 * handles creation validation for a Department
	 */
	public void validate( CreateDepartmentCommand department )throws Exception {
		Assert.notNull( department, "CreateDepartmentCommand should not be null" );
//		Assert.isNull( department.getDepartmentId(), "CreateDepartmentCommand identifier should be null" );
		Assert.notNull( department.getName(), "Field CreateDepartmentCommand.name should not be null" );
	}

	/**
	 * handles update validation for a Department
	 */
	public void validate( UpdateDepartmentCommand department ) throws Exception {
		Assert.notNull( department, "UpdateDepartmentCommand should not be null" );
		Assert.notNull( department.getDepartmentId(), "UpdateDepartmentCommand identifier should not be null" );
		Assert.notNull( department.getName(), "Field UpdateDepartmentCommand.name should not be null" );
    }

	/**
	 * handles delete validation for a Department
	 */
    public void validate( DeleteDepartmentCommand department ) throws Exception {
		Assert.notNull( department, "{commandAlias} should not be null" );
		Assert.notNull( department.getDepartmentId(), "DeleteDepartmentCommand identifier should not be null" );
	}
	
	/**
	 * handles fetchOne validation for a Department
	 */
	public void validate( DepartmentFetchOneSummary summary ) throws Exception {
		Assert.notNull( summary, "DepartmentFetchOneSummary should not be null" );
		Assert.notNull( summary.getDepartmentId(), "DepartmentFetchOneSummary identifier should not be null" );
	}

	/**
	 * handles assign Head validation for a Department
	 * 
	 * @param	command AssignHeadToDepartmentCommand
	 */	
	public void validate( AssignHeadToDepartmentCommand command ) throws Exception {
		Assert.notNull( command, "AssignHeadToDepartmentCommand should not be null" );
		Assert.notNull( command.getDepartmentId(), "AssignHeadToDepartmentCommand identifier should not be null" );
		Assert.notNull( command.getAssignment(), "AssignHeadToDepartmentCommand assignment should not be null" );
	}

	/**
	 * handles unassign Head validation for a Department
	 * 
	 * @param	command UnAssignHeadFromDepartmentCommand
	 */	
	public void validate( UnAssignHeadFromDepartmentCommand command ) throws Exception {
		Assert.notNull( command, "UnAssignHeadFromDepartmentCommand should not be null" );
		Assert.notNull( command.getDepartmentId(), "UnAssignHeadFromDepartmentCommand identifier should not be null" );
	}

	/**
	 * handles add to Employees validation for a Department
	 * 
	 * @param	command AssignEmployeesToDepartmentCommand
	 */	
	public void validate( AssignEmployeesToDepartmentCommand command ) throws Exception {
		Assert.notNull( command, "AssignEmployeesToDepartmentCommand should not be null" );
		Assert.notNull( command.getDepartmentId(), "AssignEmployeesToDepartmentCommand identifier should not be null" );
		Assert.notNull( command.getAddTo(), "AssignEmployeesToDepartmentCommand addTo attribute should not be null" );
	}

	/**
	 * handles remove from Employees validation for a Department
	 * 
	 * @param	command RemoveEmployeesFromDepartmentCommand
	 */	
	public void validate( RemoveEmployeesFromDepartmentCommand command ) throws Exception {
		Assert.notNull( command, "RemoveEmployeesFromDepartmentCommand should not be null" );
		Assert.notNull( command.getDepartmentId(), "RemoveEmployeesFromDepartmentCommand identifier should not be null" );
		Assert.notNull( command.getRemoveFrom(), "RemoveEmployeesFromDepartmentCommand removeFrom attribute should not be null" );
		Assert.notNull( command.getRemoveFrom().getEmployeeId(), "RemoveEmployeesFromDepartmentCommand removeFrom attribubte identifier should not be null" );
	}
	

}
