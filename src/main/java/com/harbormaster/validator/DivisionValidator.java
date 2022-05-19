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

public class DivisionValidator {
		
	/**
	 * default constructor
	 */
	protected DivisionValidator() {	
	}
	
	/**
	 * factory method
	 */
	static public DivisionValidator getInstance() {
		return new DivisionValidator();
	}
		
	/**
	 * handles creation validation for a Division
	 */
	public void validate( CreateDivisionCommand division )throws Exception {
		Assert.notNull( division, "CreateDivisionCommand should not be null" );
//		Assert.isNull( division.getDivisionId(), "CreateDivisionCommand identifier should be null" );
		Assert.notNull( division.getName(), "Field CreateDivisionCommand.name should not be null" );
	}

	/**
	 * handles update validation for a Division
	 */
	public void validate( UpdateDivisionCommand division ) throws Exception {
		Assert.notNull( division, "UpdateDivisionCommand should not be null" );
		Assert.notNull( division.getDivisionId(), "UpdateDivisionCommand identifier should not be null" );
		Assert.notNull( division.getName(), "Field UpdateDivisionCommand.name should not be null" );
    }

	/**
	 * handles delete validation for a Division
	 */
    public void validate( DeleteDivisionCommand division ) throws Exception {
		Assert.notNull( division, "{commandAlias} should not be null" );
		Assert.notNull( division.getDivisionId(), "DeleteDivisionCommand identifier should not be null" );
	}
	
	/**
	 * handles fetchOne validation for a Division
	 */
	public void validate( DivisionFetchOneSummary summary ) throws Exception {
		Assert.notNull( summary, "DivisionFetchOneSummary should not be null" );
		Assert.notNull( summary.getDivisionId(), "DivisionFetchOneSummary identifier should not be null" );
	}

	/**
	 * handles assign Head validation for a Division
	 * 
	 * @param	command PromoteToDivisionHeadCommand
	 */	
	public void validate( PromoteToDivisionHeadCommand command ) throws Exception {
		Assert.notNull( command, "PromoteToDivisionHeadCommand should not be null" );
		Assert.notNull( command.getDivisionId(), "PromoteToDivisionHeadCommand identifier should not be null" );
		Assert.notNull( command.getAssignment(), "PromoteToDivisionHeadCommand assignment should not be null" );
	}

	/**
	 * handles unassign Head validation for a Division
	 * 
	 * @param	command DemoteFromDivisionHeadCommand
	 */	
	public void validate( DemoteFromDivisionHeadCommand command ) throws Exception {
		Assert.notNull( command, "DemoteFromDivisionHeadCommand should not be null" );
		Assert.notNull( command.getDivisionId(), "DemoteFromDivisionHeadCommand identifier should not be null" );
	}

	/**
	 * handles add to Departments validation for a Division
	 * 
	 * @param	command AssignDepartmentsToDivisionCommand
	 */	
	public void validate( AssignDepartmentsToDivisionCommand command ) throws Exception {
		Assert.notNull( command, "AssignDepartmentsToDivisionCommand should not be null" );
		Assert.notNull( command.getDivisionId(), "AssignDepartmentsToDivisionCommand identifier should not be null" );
		Assert.notNull( command.getAddTo(), "AssignDepartmentsToDivisionCommand addTo attribute should not be null" );
	}

	/**
	 * handles remove from Departments validation for a Division
	 * 
	 * @param	command RemoveDepartmentsFromDivisionCommand
	 */	
	public void validate( RemoveDepartmentsFromDivisionCommand command ) throws Exception {
		Assert.notNull( command, "RemoveDepartmentsFromDivisionCommand should not be null" );
		Assert.notNull( command.getDivisionId(), "RemoveDepartmentsFromDivisionCommand identifier should not be null" );
		Assert.notNull( command.getRemoveFrom(), "RemoveDepartmentsFromDivisionCommand removeFrom attribute should not be null" );
		Assert.notNull( command.getRemoveFrom().getDepartmentId(), "RemoveDepartmentsFromDivisionCommand removeFrom attribubte identifier should not be null" );
	}
	

}
