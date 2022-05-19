/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.harbormaster.api.*;
import com.harbormaster.delegate.*;
import com.harbormaster.entity.*;
import com.harbormaster.exception.*;
import com.harbormaster.handler.*;

/** 
 * Implements Struts action processing for business entity Company.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Company")
public class CompanyRestController extends BaseSpringRestController {

    /**
     * Handles create a Company.  if not key provided, calls create, otherwise calls save
     * @param		Company	company
     * @return		CompletableFuture<UUID> 
     */
	@PostMapping("/create")
    public CompletableFuture<UUID> create( @RequestBody(required=true) CreateCompanyCommand command ) {
		CompletableFuture<UUID> completableFuture = null;
		try {       
        	
			completableFuture = CompanyBusinessDelegate.getCompanyInstance().createCompany( command );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage(), exc );        	
        }
		
		return completableFuture;
    }

    /**
     * Handles updating a Company.  if no key provided, calls create, otherwise calls save
     * @param		Company company
     * @return		CompletableFuture<Void>
     */
	@PutMapping("/update")
    public CompletableFuture<Void> update( @RequestBody(required=true) RefreshCompanyCommand command ) {
		CompletableFuture<Void> completableFuture = null;
		try {                        	        
			// -----------------------------------------------
			// delegate the RefreshCompanyCommand
			// -----------------------------------------------
			completableFuture = CompanyBusinessDelegate.getCompanyInstance().updateCompany(command);;
	    }
	    catch( Throwable exc ) {
	    	LOGGER.log( Level.WARNING, "CompanyController:update() - successfully update Company - " + exc.getMessage());        	
	    }		
		
		return completableFuture;
	}
 
    /**
     * Handles deleting a Company entity
     * @param		command ${class.getDeleteCommandAlias()}
     * @return		CompletableFuture<Void>
     */
    @DeleteMapping("/delete")    
    public CompletableFuture<Void> delete( @RequestBody(required=true) CloseCompanyCommand command ) {                
    	CompletableFuture<Void> completableFuture = null;
    	try {
        	CompanyBusinessDelegate delegate = CompanyBusinessDelegate.getCompanyInstance();

        	completableFuture = delegate.delete( command );
    		LOGGER.log( Level.WARNING, "Successfully deleted Company with key " + command.getCompanyId() );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage() );
        }
        
        return completableFuture;
	}        
	
    /**
     * Handles loading a Company using a UUID
     * @param		UUID uuid 
     * @return		Company
     */    
    @GetMapping("/load")
    public Company load( @RequestParam(required=true) UUID uuid ) {    	
    	Company entity = null;

    	try {  
    		entity = CompanyBusinessDelegate.getCompanyInstance().getCompany( new CompanyFetchOneSummary( uuid ) );   
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING, "failed to load Company using Id " + uuid );
            return null;
        }

        return entity;
    }

    /**
     * Handles loading all Company business objects
     * @return		Set<Company>
     */
    @GetMapping("/")
    public List<Company> loadAll() {                
    	List<Company> companyList = null;
        
    	try {
            // load the Company
            companyList = CompanyBusinessDelegate.getCompanyInstance().getAllCompany();
            
            if ( companyList != null )
                LOGGER.log( Level.INFO,  "successfully loaded all Companys" );
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING,  "failed to load all Companys ", exc );
        	return null;
        }

        return companyList;
                            
    }


    /**
     * save Divisions on Company
     * @param		command AssignDivisionsToCompanyCommand
     */     
	@PutMapping("/addToDivisions")
	public void addToDivisions( @RequestBody(required=true) AssignDivisionsToCompanyCommand command ) {
		try {
			CompanyBusinessDelegate.getCompanyInstance().addToDivisions( command );   
		}
		catch( Exception exc ) {
			LOGGER.log( Level.WARNING, "Failed to add to Set Divisions", exc );
		}
	}

    /**
     * remove Divisions on Company
     * @param		command RemoveDivisionsFromCompanyCommand
     */     	
	@PutMapping("/removeFromDivisions")
	public void removeFromDivisions( 	@RequestBody(required=true) RemoveDivisionsFromCompanyCommand command )
	{		
		try {
			CompanyBusinessDelegate.getCompanyInstance().removeFromDivisions( command );
		}
		catch( Exception exc ) {
			LOGGER.log( Level.WARNING, "Failed to remove from Set Divisions", exc );
		}
	}

    /**
     * save BoardMembers on Company
     * @param		command AssignBoardMembersToCompanyCommand
     */     
	@PutMapping("/addToBoardMembers")
	public void addToBoardMembers( @RequestBody(required=true) AssignBoardMembersToCompanyCommand command ) {
		try {
			CompanyBusinessDelegate.getCompanyInstance().addToBoardMembers( command );   
		}
		catch( Exception exc ) {
			LOGGER.log( Level.WARNING, "Failed to add to Set BoardMembers", exc );
		}
	}

    /**
     * remove BoardMembers on Company
     * @param		command DemoteFromBoardMemberCommand
     */     	
	@PutMapping("/removeFromBoardMembers")
	public void removeFromBoardMembers( 	@RequestBody(required=true) DemoteFromBoardMemberCommand command )
	{		
		try {
			CompanyBusinessDelegate.getCompanyInstance().removeFromBoardMembers( command );
		}
		catch( Exception exc ) {
			LOGGER.log( Level.WARNING, "Failed to remove from Set BoardMembers", exc );
		}
	}


    /**
     * finder method to findCompanyByName
     * @param 		String name
     * @return		Company
     */     
	@PostMapping("/findCompanyByName")
	public Company findCompanyByName( @RequestBody(required=true) FindByNameQuery query ) {
		Company result = null;
        try {  
            // call the delegate directly
        	result = new CompanyBusinessDelegate().findCompanyByName(query);
            
            if ( result != null )
                LOGGER.log( Level.WARNING,  "successfully executed findCompanyByName" );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING,  "failed to execute findCompanyByName" );
        }
        return result;
	}
    /**
     * finder method to findCompaniesByIndustry
     * @param 		Industry industry
     * @return		List<Company>
     */     
	@PostMapping("/findCompaniesByIndustry")
	public List<Company> findCompaniesByIndustry( @RequestBody(required=true) FindCompaniesByIndustryQuery query ) {
		List<Company> result = null;
        try {  
            // call the delegate directly
        	result = new CompanyBusinessDelegate().findCompaniesByIndustry(query);
            
            if ( result != null )
                LOGGER.log( Level.WARNING,  "successfully executed findCompaniesByIndustry" );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING,  "failed to execute findCompaniesByIndustry" );
        }
        return result;
	}
    /**
     * finder method to findCompaniesByType
     * @param 		CompanyType type
     * @return		List<Company>
     */     
	@PostMapping("/findCompaniesByType")
	public List<Company> findCompaniesByType( @RequestBody(required=true) FindByTypeQuery query ) {
		List<Company> result = null;
        try {  
            // call the delegate directly
        	result = new CompanyBusinessDelegate().findCompaniesByType(query);
            
            if ( result != null )
                LOGGER.log( Level.WARNING,  "successfully executed findCompaniesByType" );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING,  "failed to execute findCompaniesByType" );
        }
        return result;
	}


//************************************************************************    
// Attributes
//************************************************************************
    protected Company company = null;
    private static final Logger LOGGER = Logger.getLogger(CompanyRestController.class.getName());
    
}
