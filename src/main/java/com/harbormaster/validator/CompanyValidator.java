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

public class CompanyValidator {
		
	/**
	 * default constructor
	 */
	protected CompanyValidator() {	
	}
	
	/**
	 * factory method
	 */
	static public CompanyValidator getInstance() {
		return new CompanyValidator();
	}
		
	/**
	 * handles creation validation for a Company
	 */
	public void validate( CreateCompanyCommand company )throws Exception {
		Assert.notNull( company, "CreateCompanyCommand should not be null" );
//		Assert.isNull( company.getCompanyId(), "CreateCompanyCommand identifier should be null" );
		Assert.notNull( company.getName(), "Field CreateCompanyCommand.name should not be null" );
		Assert.notNull( company.getEstablishedOn(), "Field CreateCompanyCommand.establishedOn should not be null" );
		Assert.notNull( company.getRevenue(), "Field CreateCompanyCommand.revenue should not be null" );
	}

	/**
	 * handles update validation for a Company
	 */
	public void validate( RefreshCompanyCommand company ) throws Exception {
		Assert.notNull( company, "RefreshCompanyCommand should not be null" );
		Assert.notNull( company.getCompanyId(), "RefreshCompanyCommand identifier should not be null" );
		Assert.notNull( company.getName(), "Field RefreshCompanyCommand.name should not be null" );
		Assert.notNull( company.getEstablishedOn(), "Field RefreshCompanyCommand.establishedOn should not be null" );
		Assert.notNull( company.getRevenue(), "Field RefreshCompanyCommand.revenue should not be null" );
    }

	/**
	 * handles delete validation for a Company
	 */
    public void validate( CloseCompanyCommand company ) throws Exception {
		Assert.notNull( company, "{commandAlias} should not be null" );
		Assert.notNull( company.getCompanyId(), "CloseCompanyCommand identifier should not be null" );
	}
	
	/**
	 * handles fetchOne validation for a Company
	 */
	public void validate( CompanyFetchOneSummary summary ) throws Exception {
		Assert.notNull( summary, "CompanyFetchOneSummary should not be null" );
		Assert.notNull( summary.getCompanyId(), "CompanyFetchOneSummary identifier should not be null" );
	}


	/**
	 * handles add to Divisions validation for a Company
	 * 
	 * @param	command AssignDivisionsToCompanyCommand
	 */	
	public void validate( AssignDivisionsToCompanyCommand command ) throws Exception {
		Assert.notNull( command, "AssignDivisionsToCompanyCommand should not be null" );
		Assert.notNull( command.getCompanyId(), "AssignDivisionsToCompanyCommand identifier should not be null" );
		Assert.notNull( command.getAddTo(), "AssignDivisionsToCompanyCommand addTo attribute should not be null" );
	}

	/**
	 * handles remove from Divisions validation for a Company
	 * 
	 * @param	command RemoveDivisionsFromCompanyCommand
	 */	
	public void validate( RemoveDivisionsFromCompanyCommand command ) throws Exception {
		Assert.notNull( command, "RemoveDivisionsFromCompanyCommand should not be null" );
		Assert.notNull( command.getCompanyId(), "RemoveDivisionsFromCompanyCommand identifier should not be null" );
		Assert.notNull( command.getRemoveFrom(), "RemoveDivisionsFromCompanyCommand removeFrom attribute should not be null" );
		Assert.notNull( command.getRemoveFrom().getDivisionId(), "RemoveDivisionsFromCompanyCommand removeFrom attribubte identifier should not be null" );
	}
	
	/**
	 * handles add to BoardMembers validation for a Company
	 * 
	 * @param	command AssignBoardMembersToCompanyCommand
	 */	
	public void validate( AssignBoardMembersToCompanyCommand command ) throws Exception {
		Assert.notNull( command, "AssignBoardMembersToCompanyCommand should not be null" );
		Assert.notNull( command.getCompanyId(), "AssignBoardMembersToCompanyCommand identifier should not be null" );
		Assert.notNull( command.getAddTo(), "AssignBoardMembersToCompanyCommand addTo attribute should not be null" );
	}

	/**
	 * handles remove from BoardMembers validation for a Company
	 * 
	 * @param	command DemoteFromBoardMemberCommand
	 */	
	public void validate( DemoteFromBoardMemberCommand command ) throws Exception {
		Assert.notNull( command, "DemoteFromBoardMemberCommand should not be null" );
		Assert.notNull( command.getCompanyId(), "DemoteFromBoardMemberCommand identifier should not be null" );
		Assert.notNull( command.getRemoveFrom(), "DemoteFromBoardMemberCommand removeFrom attribute should not be null" );
		Assert.notNull( command.getRemoveFrom().getEmployeeId(), "DemoteFromBoardMemberCommand removeFrom attribubte identifier should not be null" );
	}
	

}
