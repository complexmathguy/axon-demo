/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.handler;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;


import com.harbormaster.api.*;
import com.harbormaster.entity.*;
import com.harbormaster.exception.*;
import com.harbormaster.repository.*;

/**
 * Projector for Employee as outlined for the CQRS pattern.
 * 
 * Commands are handled by EmployeeAggregate
 * 
 * @author Dev Team
 *
 */
@Component("employee-entity-projector")
public class EmployeeEntityProjector {
		
	// core constructor
	public EmployeeEntityProjector(EmployeeRepository repository ) {
        this.repository = repository;
    }	

	/*
	 * Insert a Employee
	 * 
     * @param	entity Employee
     */
    public Employee create( Employee entity) {
	    LOGGER.info("creating " + entity.toString() );
	   
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    return repository.save(entity);
        
    }

	/*
	 * Update a Employee
	 * 
     * @param	entity Employee
     */
    public Employee update( Employee entity) {
	    LOGGER.info("updating " + entity.toString() );

	    // ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		return repository.save(entity);

    }
    
	/*
	 * Delete a Employee
	 * 
     * @param	id		UUID
     */
    public Employee delete( UUID id ) {
	    LOGGER.info("deleting " + id.toString() );

    	// ------------------------------------------
    	// locate the entity by the provided id
    	// ------------------------------------------
	    Employee entity = repository.findById(id).get();
	    
    	// ------------------------------------------
    	// delete what is discovered 
    	// ------------------------------------------
    	repository.delete( entity );
    	
    	return entity;
    }    




    /**
     * Method to retrieve the Employee via an FindEmployeeQuery
     * @return 	query	FindEmployeeQuery
     */
    @SuppressWarnings("unused")
    public Employee find( UUID id ) {
    	LOGGER.info("handling find using " + id.toString() );
    	try {
    		return repository.findById(id).get();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find a Employee - {0}", exc.getMessage() );
    	}
    	return null;
    }
    
    /**
     * Method to retrieve a collection of all Employees
     *
     * @param	query	FindAllEmployeeQuery 
     * @return 	List<Employee> 
     */
    @SuppressWarnings("unused")
    public List<Employee> findAll( FindAllEmployeeQuery query ) {
    	LOGGER.info("handling findAll using " + query.toString() );
    	try {
    		return repository.findAll();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find all Employee - {0}", exc.getMessage() );
    	}
    	return null;
    }




    //--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
    protected final EmployeeRepository repository;

    private static final Logger LOGGER 	= Logger.getLogger(EmployeeEntityProjector.class.getName());

}
