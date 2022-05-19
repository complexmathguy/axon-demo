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
 * Implements Struts action processing for business entity Employee.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Employee")
public class EmployeeRestController extends BaseSpringRestController {

    /**
     * Handles create a Employee.  if not key provided, calls create, otherwise calls save
     * @param		Employee	employee
     * @return		CompletableFuture<UUID> 
     */
	@PostMapping("/create")
    public CompletableFuture<UUID> create( @RequestBody(required=true) CreateEmployeeCommand command ) {
		CompletableFuture<UUID> completableFuture = null;
		try {       
        	
			completableFuture = EmployeeBusinessDelegate.getEmployeeInstance().createEmployee( command );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage(), exc );        	
        }
		
		return completableFuture;
    }

    /**
     * Handles updating a Employee.  if no key provided, calls create, otherwise calls save
     * @param		Employee employee
     * @return		CompletableFuture<Void>
     */
	@PutMapping("/update")
    public CompletableFuture<Void> update( @RequestBody(required=true) UpdateEmployeeCommand command ) {
		CompletableFuture<Void> completableFuture = null;
		try {                        	        
			// -----------------------------------------------
			// delegate the UpdateEmployeeCommand
			// -----------------------------------------------
			completableFuture = EmployeeBusinessDelegate.getEmployeeInstance().updateEmployee(command);;
	    }
	    catch( Throwable exc ) {
	    	LOGGER.log( Level.WARNING, "EmployeeController:update() - successfully update Employee - " + exc.getMessage());        	
	    }		
		
		return completableFuture;
	}
 
    /**
     * Handles deleting a Employee entity
     * @param		command ${class.getDeleteCommandAlias()}
     * @return		CompletableFuture<Void>
     */
    @DeleteMapping("/delete")    
    public CompletableFuture<Void> delete( @RequestBody(required=true) DeleteEmployeeCommand command ) {                
    	CompletableFuture<Void> completableFuture = null;
    	try {
        	EmployeeBusinessDelegate delegate = EmployeeBusinessDelegate.getEmployeeInstance();

        	completableFuture = delegate.delete( command );
    		LOGGER.log( Level.WARNING, "Successfully deleted Employee with key " + command.getEmployeeId() );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage() );
        }
        
        return completableFuture;
	}        
	
    /**
     * Handles loading a Employee using a UUID
     * @param		UUID uuid 
     * @return		Employee
     */    
    @GetMapping("/load")
    public Employee load( @RequestParam(required=true) UUID uuid ) {    	
    	Employee entity = null;

    	try {  
    		entity = EmployeeBusinessDelegate.getEmployeeInstance().getEmployee( new EmployeeFetchOneSummary( uuid ) );   
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING, "failed to load Employee using Id " + uuid );
            return null;
        }

        return entity;
    }

    /**
     * Handles loading all Employee business objects
     * @return		Set<Employee>
     */
    @GetMapping("/")
    public List<Employee> loadAll() {                
    	List<Employee> employeeList = null;
        
    	try {
            // load the Employee
            employeeList = EmployeeBusinessDelegate.getEmployeeInstance().getAllEmployee();
            
            if ( employeeList != null )
                LOGGER.log( Level.INFO,  "successfully loaded all Employees" );
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING,  "failed to load all Employees ", exc );
        	return null;
        }

        return employeeList;
                            
    }





//************************************************************************    
// Attributes
//************************************************************************
    protected Employee employee = null;
    private static final Logger LOGGER = Logger.getLogger(EmployeeRestController.class.getName());
    
}
