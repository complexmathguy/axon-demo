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
 * Implements Struts action processing for business entity Department.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Department")
public class DepartmentRestController extends BaseSpringRestController {

    /**
     * Handles create a Department.  if not key provided, calls create, otherwise calls save
     * @param		Department	department
     * @return		CompletableFuture<UUID> 
     */
	@PostMapping("/create")
    public CompletableFuture<UUID> create( @RequestBody(required=true) CreateDepartmentCommand command ) {
		CompletableFuture<UUID> completableFuture = null;
		try {       
        	
			completableFuture = DepartmentBusinessDelegate.getDepartmentInstance().createDepartment( command );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage(), exc );        	
        }
		
		return completableFuture;
    }

    /**
     * Handles updating a Department.  if no key provided, calls create, otherwise calls save
     * @param		Department department
     * @return		CompletableFuture<Void>
     */
	@PutMapping("/update")
    public CompletableFuture<Void> update( @RequestBody(required=true) UpdateDepartmentCommand command ) {
		CompletableFuture<Void> completableFuture = null;
		try {                        	        
			// -----------------------------------------------
			// delegate the UpdateDepartmentCommand
			// -----------------------------------------------
			completableFuture = DepartmentBusinessDelegate.getDepartmentInstance().updateDepartment(command);;
	    }
	    catch( Throwable exc ) {
	    	LOGGER.log( Level.WARNING, "DepartmentController:update() - successfully update Department - " + exc.getMessage());        	
	    }		
		
		return completableFuture;
	}
 
    /**
     * Handles deleting a Department entity
     * @param		command ${class.getDeleteCommandAlias()}
     * @return		CompletableFuture<Void>
     */
    @DeleteMapping("/delete")    
    public CompletableFuture<Void> delete( @RequestBody(required=true) DeleteDepartmentCommand command ) {                
    	CompletableFuture<Void> completableFuture = null;
    	try {
        	DepartmentBusinessDelegate delegate = DepartmentBusinessDelegate.getDepartmentInstance();

        	completableFuture = delegate.delete( command );
    		LOGGER.log( Level.WARNING, "Successfully deleted Department with key " + command.getDepartmentId() );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage() );
        }
        
        return completableFuture;
	}        
	
    /**
     * Handles loading a Department using a UUID
     * @param		UUID uuid 
     * @return		Department
     */    
    @GetMapping("/load")
    public Department load( @RequestParam(required=true) UUID uuid ) {    	
    	Department entity = null;

    	try {  
    		entity = DepartmentBusinessDelegate.getDepartmentInstance().getDepartment( new DepartmentFetchOneSummary( uuid ) );   
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING, "failed to load Department using Id " + uuid );
            return null;
        }

        return entity;
    }

    /**
     * Handles loading all Department business objects
     * @return		Set<Department>
     */
    @GetMapping("/")
    public List<Department> loadAll() {                
    	List<Department> departmentList = null;
        
    	try {
            // load the Department
            departmentList = DepartmentBusinessDelegate.getDepartmentInstance().getAllDepartment();
            
            if ( departmentList != null )
                LOGGER.log( Level.INFO,  "successfully loaded all Departments" );
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING,  "failed to load all Departments ", exc );
        	return null;
        }

        return departmentList;
                            
    }

    /**
     * save Head on Department
     * @param		command AssignHeadToDepartmentCommand
     */     
	@PutMapping("/assignHead")
	public void assignHead( @RequestBody AssignHeadToDepartmentCommand command ) {
		try {
			DepartmentBusinessDelegate.getDepartmentInstance().assignHead( command );   
		}
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to assign Head", exc );
        }
	}

    /**
     * unassign Head on Department
     * @param		 command UnAssignHeadFromDepartmentCommand
     */     
	@PutMapping("/unAssignHead")
	public void unAssignHead( @RequestBody(required=true)  UnAssignHeadFromDepartmentCommand command ) {
		try {
			DepartmentBusinessDelegate.getDepartmentInstance().unAssignHead( command );   
		}
		catch( Exception exc ) {
			LOGGER.log( Level.WARNING, "Failed to unassign Head", exc );
		}
	}
	

    /**
     * save Employees on Department
     * @param		command AssignEmployeesToDepartmentCommand
     */     
	@PutMapping("/addToEmployees")
	public void addToEmployees( @RequestBody(required=true) AssignEmployeesToDepartmentCommand command ) {
		try {
			DepartmentBusinessDelegate.getDepartmentInstance().addToEmployees( command );   
		}
		catch( Exception exc ) {
			LOGGER.log( Level.WARNING, "Failed to add to Set Employees", exc );
		}
	}

    /**
     * remove Employees on Department
     * @param		command RemoveEmployeesFromDepartmentCommand
     */     	
	@PutMapping("/removeFromEmployees")
	public void removeFromEmployees( 	@RequestBody(required=true) RemoveEmployeesFromDepartmentCommand command )
	{		
		try {
			DepartmentBusinessDelegate.getDepartmentInstance().removeFromEmployees( command );
		}
		catch( Exception exc ) {
			LOGGER.log( Level.WARNING, "Failed to remove from Set Employees", exc );
		}
	}




//************************************************************************    
// Attributes
//************************************************************************
    protected Department department = null;
    private static final Logger LOGGER = Logger.getLogger(DepartmentRestController.class.getName());
    
}
