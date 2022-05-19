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

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbormaster.api.*;
import com.harbormaster.entity.*;
import com.harbormaster.exception.*;
import com.harbormaster.repository.*;

/**
 * Projector for Employee as outlined for the CQRS pattern.  All event handling and query handling related to Employee are invoked here
 * and dispersed as an event to be handled elsewhere.
 * 
 * Commands are handled by EmployeeAggregate
 * 
 * @author Dev Team
 *
 */
@ProcessingGroup("employee")
@Component("employee-projector")
public class EmployeeProjector extends EmployeeEntityProjector {
		
	// core constructor
	public EmployeeProjector(EmployeeRepository repository, QueryUpdateEmitter queryUpdateEmitter ) {
        super(repository);
        this.queryUpdateEmitter = queryUpdateEmitter;
    }	

	/*
     * @param	event CreateEmployeeEvent
     */
    @EventHandler( payloadType=CreateEmployeeEvent.class )
    public void handle( CreateEmployeeEvent event) {
	    LOGGER.info("handling CreateEmployeeEvent - " + event );
	    Employee entity = new Employee();
        entity.setEmployeeId( event.getEmployeeId() );
        entity.setFirstName( event.getFirstName() );
        entity.setLastName( event.getLastName() );
        entity.setType( event.getType() );
	    
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    create(entity);
        
        // ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllEmployee( entity );
    }

    /*
     * @param	event UpdateEmployeeEvent
     */
    @EventHandler( payloadType=UpdateEmployeeEvent.class )
    public void handle( UpdateEmployeeEvent event) {
    	LOGGER.info("handling UpdateEmployeeEvent - " + event );
    	
	    Employee entity = new Employee();
        entity.setEmployeeId( event.getEmployeeId() );
        entity.setFirstName( event.getFirstName() );
        entity.setLastName( event.getLastName() );
        entity.setType( event.getType() );
 
    	// ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		update(entity);

    	// ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindEmployee( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllEmployee( entity );        
    }
    
    /*
     * @param	event DeleteEmployeeEvent
     */
    @EventHandler( payloadType=DeleteEmployeeEvent.class )
    public void handle( DeleteEmployeeEvent event) {
    	LOGGER.info("handling DeleteEmployeeEvent - " + event );
    	
    	// ------------------------------------------
    	// delete delegation
    	// ------------------------------------------
    	Employee entity = delete( event.getEmployeeId() );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllEmployee( entity );

    }    




    /**
     * Method to retrieve the Employee via an EmployeePrimaryKey.
     * @param 	id Long
     * @return 	Employee
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public Employee handle( FindEmployeeQuery query ) 
    throws ProcessingException, IllegalArgumentException {
    	return find( query.getFilter().getEmployeeId() );
    }
    
    /**
     * Method to retrieve a collection of all Employees
     *
     * @param	query	FindAllEmployeeQuery 
     * @return 	List<Employee> 
     * @exception ProcessingException Thrown if any problems
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public List<Employee> handle( FindAllEmployeeQuery query ) throws ProcessingException {
    	return findAll( query );
    }


	/**
	 * emit to subscription queries of type FindEmployee, 
	 * but only if the id matches
	 * 
	 * @param		entity	Employee
	 */
	protected void emitFindEmployee( Employee entity ) {
		LOGGER.info("handling emitFindEmployee" );
		
	    queryUpdateEmitter.emit(FindEmployeeQuery.class,
	                            query -> query.getFilter().getEmployeeId().equals(entity.getEmployeeId()),
	                            entity);
	}
	
	/**
	 * unconditionally emit to subscription queries of type FindAllEmployee
	 * 
	 * @param		entity	Employee
	 */
	protected void emitFindAllEmployee( Employee entity ) {
		LOGGER.info("handling emitFindAllEmployee" );
		
	    queryUpdateEmitter.emit(FindAllEmployeeQuery.class,
	                            query -> true,
	                            entity);
	}


    //--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
	private final QueryUpdateEmitter queryUpdateEmitter;
    private static final Logger LOGGER 	= Logger.getLogger(EmployeeProjector.class.getName());

}
