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
 * Implements Struts action processing for business entity Division.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Division")
public class DivisionRestController extends BaseSpringRestController {

    /**
     * Handles create a Division.  if not key provided, calls create, otherwise calls save
     * @param		Division	division
     * @return		CompletableFuture<UUID> 
     */
	@PostMapping("/create")
    public CompletableFuture<UUID> create( @RequestBody(required=true) CreateDivisionCommand command ) {
		CompletableFuture<UUID> completableFuture = null;
		try {       
        	
			completableFuture = DivisionBusinessDelegate.getDivisionInstance().createDivision( command );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage(), exc );        	
        }
		
		return completableFuture;
    }

    /**
     * Handles updating a Division.  if no key provided, calls create, otherwise calls save
     * @param		Division division
     * @return		CompletableFuture<Void>
     */
	@PutMapping("/update")
    public CompletableFuture<Void> update( @RequestBody(required=true) UpdateDivisionCommand command ) {
		CompletableFuture<Void> completableFuture = null;
		try {                        	        
			// -----------------------------------------------
			// delegate the UpdateDivisionCommand
			// -----------------------------------------------
			completableFuture = DivisionBusinessDelegate.getDivisionInstance().updateDivision(command);;
	    }
	    catch( Throwable exc ) {
	    	LOGGER.log( Level.WARNING, "DivisionController:update() - successfully update Division - " + exc.getMessage());        	
	    }		
		
		return completableFuture;
	}
 
    /**
     * Handles deleting a Division entity
     * @param		command ${class.getDeleteCommandAlias()}
     * @return		CompletableFuture<Void>
     */
    @DeleteMapping("/delete")    
    public CompletableFuture<Void> delete( @RequestBody(required=true) DeleteDivisionCommand command ) {                
    	CompletableFuture<Void> completableFuture = null;
    	try {
        	DivisionBusinessDelegate delegate = DivisionBusinessDelegate.getDivisionInstance();

        	completableFuture = delegate.delete( command );
    		LOGGER.log( Level.WARNING, "Successfully deleted Division with key " + command.getDivisionId() );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage() );
        }
        
        return completableFuture;
	}        
	
    /**
     * Handles loading a Division using a UUID
     * @param		UUID uuid 
     * @return		Division
     */    
    @GetMapping("/load")
    public Division load( @RequestParam(required=true) UUID uuid ) {    	
    	Division entity = null;

    	try {  
    		entity = DivisionBusinessDelegate.getDivisionInstance().getDivision( new DivisionFetchOneSummary( uuid ) );   
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING, "failed to load Division using Id " + uuid );
            return null;
        }

        return entity;
    }

    /**
     * Handles loading all Division business objects
     * @return		Set<Division>
     */
    @GetMapping("/")
    public List<Division> loadAll() {                
    	List<Division> divisionList = null;
        
    	try {
            // load the Division
            divisionList = DivisionBusinessDelegate.getDivisionInstance().getAllDivision();
            
            if ( divisionList != null )
                LOGGER.log( Level.INFO,  "successfully loaded all Divisions" );
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING,  "failed to load all Divisions ", exc );
        	return null;
        }

        return divisionList;
                            
    }

    /**
     * save Head on Division
     * @param		command PromoteToDivisionHeadCommand
     */     
	@PutMapping("/assignHead")
	public void assignHead( @RequestBody PromoteToDivisionHeadCommand command ) {
		try {
			DivisionBusinessDelegate.getDivisionInstance().assignHead( command );   
		}
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to assign Head", exc );
        }
	}

    /**
     * unassign Head on Division
     * @param		 command DemoteFromDivisionHeadCommand
     */     
	@PutMapping("/unAssignHead")
	public void unAssignHead( @RequestBody(required=true)  DemoteFromDivisionHeadCommand command ) {
		try {
			DivisionBusinessDelegate.getDivisionInstance().unAssignHead( command );   
		}
		catch( Exception exc ) {
			LOGGER.log( Level.WARNING, "Failed to unassign Head", exc );
		}
	}
	

    /**
     * save Departments on Division
     * @param		command AssignDepartmentsToDivisionCommand
     */     
	@PutMapping("/addToDepartments")
	public void addToDepartments( @RequestBody(required=true) AssignDepartmentsToDivisionCommand command ) {
		try {
			DivisionBusinessDelegate.getDivisionInstance().addToDepartments( command );   
		}
		catch( Exception exc ) {
			LOGGER.log( Level.WARNING, "Failed to add to Set Departments", exc );
		}
	}

    /**
     * remove Departments on Division
     * @param		command RemoveDepartmentsFromDivisionCommand
     */     	
	@PutMapping("/removeFromDepartments")
	public void removeFromDepartments( 	@RequestBody(required=true) RemoveDepartmentsFromDivisionCommand command )
	{		
		try {
			DivisionBusinessDelegate.getDivisionInstance().removeFromDepartments( command );
		}
		catch( Exception exc ) {
			LOGGER.log( Level.WARNING, "Failed to remove from Set Departments", exc );
		}
	}




//************************************************************************    
// Attributes
//************************************************************************
    protected Division division = null;
    private static final Logger LOGGER = Logger.getLogger(DivisionRestController.class.getName());
    
}
