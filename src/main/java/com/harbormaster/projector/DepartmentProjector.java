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
 * Projector for Department as outlined for the CQRS pattern.  All event handling and query handling related to Department are invoked here
 * and dispersed as an event to be handled elsewhere.
 * 
 * Commands are handled by DepartmentAggregate
 * 
 * @author Dev Team
 *
 */
@ProcessingGroup("department")
@Component("department-projector")
public class DepartmentProjector extends DepartmentEntityProjector {
		
	// core constructor
	public DepartmentProjector(DepartmentRepository repository, QueryUpdateEmitter queryUpdateEmitter ) {
        super(repository);
        this.queryUpdateEmitter = queryUpdateEmitter;
    }	

	/*
     * @param	event CreateDepartmentEvent
     */
    @EventHandler( payloadType=CreateDepartmentEvent.class )
    public void handle( CreateDepartmentEvent event) {
	    LOGGER.info("handling CreateDepartmentEvent - " + event );
	    Department entity = new Department();
        entity.setDepartmentId( event.getDepartmentId() );
        entity.setName( event.getName() );
	    
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    create(entity);
        
        // ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllDepartment( entity );
    }

    /*
     * @param	event UpdateDepartmentEvent
     */
    @EventHandler( payloadType=UpdateDepartmentEvent.class )
    public void handle( UpdateDepartmentEvent event) {
    	LOGGER.info("handling UpdateDepartmentEvent - " + event );
    	
	    Department entity = new Department();
        entity.setDepartmentId( event.getDepartmentId() );
        entity.setName( event.getName() );
        entity.setHead( event.getHead() );
        entity.setEmployees( event.getEmployees() );
 
    	// ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		update(entity);

    	// ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindDepartment( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllDepartment( entity );        
    }
    
    /*
     * @param	event DeleteDepartmentEvent
     */
    @EventHandler( payloadType=DeleteDepartmentEvent.class )
    public void handle( DeleteDepartmentEvent event) {
    	LOGGER.info("handling DeleteDepartmentEvent - " + event );
    	
    	// ------------------------------------------
    	// delete delegation
    	// ------------------------------------------
    	Department entity = delete( event.getDepartmentId() );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllDepartment( entity );

    }    

    /*
     * @param	event AssignHeadToDepartmentEvent
     */
    @EventHandler( payloadType=AssignHeadToDepartmentEvent.class)
    public void handle( AssignHeadToDepartmentEvent event) {
	    LOGGER.info("handling AssignHeadToDepartmentEvent - " + event );

	    // ------------------------------------------
	    // delegate to assignTo
	    // ------------------------------------------
	    Department entity = assignHead( event.getDepartmentId(), event.getAssignment() );

	    // ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindDepartment( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllDepartment( entity );
    }
    

	/*
	 * @param	event UnAssignHeadFromDepartmentEvent
	 */
	@EventHandler( payloadType=UnAssignHeadFromDepartmentEvent.class)
	public void handle( UnAssignHeadFromDepartmentEvent event) {
	    LOGGER.info("handling UnAssignHeadFromDepartmentEvent - " + event );

	    // ------------------------------------------
	    // delegate to unAssignFrom
	    // ------------------------------------------
	    Department entity = unAssignHead( event.getDepartmentId() );

		// ------------------------------------------
		// emit to subscribers that find one
		// ------------------------------------------    	
	    emitFindDepartment( entity );
	
		// ------------------------------------------
		// emit to subscribers that find all
		// ------------------------------------------    	
	    emitFindAllDepartment( entity );
	}


    /*
     * @param	event AssignEmployeesToDepartmentEvent
     */
    @EventHandler( payloadType=AssignEmployeesToDepartmentEvent.class)
    public void handle( AssignEmployeesToDepartmentEvent event) {
	    LOGGER.info("handling AssignEmployeesToDepartmentEvent - " + event );
	    
	    // ------------------------------------------
    	// delegate to addTo 
    	// ------------------------------------------ 
	    Department entity = addToEmployees(event.getDepartmentId(), event.getAddTo() );
        
    	// ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindDepartment( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllDepartment( entity );
    }
    

/*
 * @param	event RemoveEmployeesFromDepartmentEvent
 */
@EventHandler( payloadType=RemoveEmployeesFromDepartmentEvent.class)
public void handle( RemoveEmployeesFromDepartmentEvent event) {
    LOGGER.info("handling RemoveEmployeesFromDepartmentEvent - " + event );

    Department entity = removeFromEmployees(event.getDepartmentId(), event.getRemoveFrom() );
    
	// ------------------------------------------
	// emit to subscribers that find one
	// ------------------------------------------    	
    emitFindDepartment( entity );

	// ------------------------------------------
	// emit to subscribers that find all
	// ------------------------------------------    	
    emitFindAllDepartment( entity );
}



    /**
     * Method to retrieve the Department via an DepartmentPrimaryKey.
     * @param 	id Long
     * @return 	Department
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public Department handle( FindDepartmentQuery query ) 
    throws ProcessingException, IllegalArgumentException {
    	return find( query.getFilter().getDepartmentId() );
    }
    
    /**
     * Method to retrieve a collection of all Departments
     *
     * @param	query	FindAllDepartmentQuery 
     * @return 	List<Department> 
     * @exception ProcessingException Thrown if any problems
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public List<Department> handle( FindAllDepartmentQuery query ) throws ProcessingException {
    	return findAll( query );
    }


	/**
	 * emit to subscription queries of type FindDepartment, 
	 * but only if the id matches
	 * 
	 * @param		entity	Department
	 */
	protected void emitFindDepartment( Department entity ) {
		LOGGER.info("handling emitFindDepartment" );
		
	    queryUpdateEmitter.emit(FindDepartmentQuery.class,
	                            query -> query.getFilter().getDepartmentId().equals(entity.getDepartmentId()),
	                            entity);
	}
	
	/**
	 * unconditionally emit to subscription queries of type FindAllDepartment
	 * 
	 * @param		entity	Department
	 */
	protected void emitFindAllDepartment( Department entity ) {
		LOGGER.info("handling emitFindAllDepartment" );
		
	    queryUpdateEmitter.emit(FindAllDepartmentQuery.class,
	                            query -> true,
	                            entity);
	}


    //--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
	private final QueryUpdateEmitter queryUpdateEmitter;
    private static final Logger LOGGER 	= Logger.getLogger(DepartmentProjector.class.getName());

}
