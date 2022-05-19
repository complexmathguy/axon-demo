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
 * Projector for Division as outlined for the CQRS pattern.  All event handling and query handling related to Division are invoked here
 * and dispersed as an event to be handled elsewhere.
 * 
 * Commands are handled by DivisionAggregate
 * 
 * @author Dev Team
 *
 */
@ProcessingGroup("division")
@Component("division-projector")
public class DivisionProjector extends DivisionEntityProjector {
		
	// core constructor
	public DivisionProjector(DivisionRepository repository, QueryUpdateEmitter queryUpdateEmitter ) {
        super(repository);
        this.queryUpdateEmitter = queryUpdateEmitter;
    }	

	/*
     * @param	event CreateDivisionEvent
     */
    @EventHandler( payloadType=CreateDivisionEvent.class )
    public void handle( CreateDivisionEvent event) {
	    LOGGER.info("handling CreateDivisionEvent - " + event );
	    Division entity = new Division();
        entity.setDivisionId( event.getDivisionId() );
        entity.setName( event.getName() );
	    
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    create(entity);
        
        // ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllDivision( entity );
    }

    /*
     * @param	event UpdateDivisionEvent
     */
    @EventHandler( payloadType=UpdateDivisionEvent.class )
    public void handle( UpdateDivisionEvent event) {
    	LOGGER.info("handling UpdateDivisionEvent - " + event );
    	
	    Division entity = new Division();
        entity.setDivisionId( event.getDivisionId() );
        entity.setName( event.getName() );
        entity.setHead( event.getHead() );
        entity.setDepartments( event.getDepartments() );
 
    	// ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		update(entity);

    	// ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindDivision( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllDivision( entity );        
    }
    
    /*
     * @param	event DeleteDivisionEvent
     */
    @EventHandler( payloadType=DeleteDivisionEvent.class )
    public void handle( DeleteDivisionEvent event) {
    	LOGGER.info("handling DeleteDivisionEvent - " + event );
    	
    	// ------------------------------------------
    	// delete delegation
    	// ------------------------------------------
    	Division entity = delete( event.getDivisionId() );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllDivision( entity );

    }    

    /*
     * @param	event PromotedToDivisionHeadEvent
     */
    @EventHandler( payloadType=PromotedToDivisionHeadEvent.class)
    public void handle( PromotedToDivisionHeadEvent event) {
	    LOGGER.info("handling PromotedToDivisionHeadEvent - " + event );

	    // ------------------------------------------
	    // delegate to assignTo
	    // ------------------------------------------
	    Division entity = assignHead( event.getDivisionId(), event.getAssignment() );

	    // ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindDivision( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllDivision( entity );
    }
    

	/*
	 * @param	event DemomotedFromDivisionHeadCommand
	 */
	@EventHandler( payloadType=DemomotedFromDivisionHeadCommand.class)
	public void handle( DemomotedFromDivisionHeadCommand event) {
	    LOGGER.info("handling DemomotedFromDivisionHeadCommand - " + event );

	    // ------------------------------------------
	    // delegate to unAssignFrom
	    // ------------------------------------------
	    Division entity = unAssignHead( event.getDivisionId() );

		// ------------------------------------------
		// emit to subscribers that find one
		// ------------------------------------------    	
	    emitFindDivision( entity );
	
		// ------------------------------------------
		// emit to subscribers that find all
		// ------------------------------------------    	
	    emitFindAllDivision( entity );
	}


    /*
     * @param	event AssignDepartmentsToDivisionEvent
     */
    @EventHandler( payloadType=AssignDepartmentsToDivisionEvent.class)
    public void handle( AssignDepartmentsToDivisionEvent event) {
	    LOGGER.info("handling AssignDepartmentsToDivisionEvent - " + event );
	    
	    // ------------------------------------------
    	// delegate to addTo 
    	// ------------------------------------------ 
	    Division entity = addToDepartments(event.getDivisionId(), event.getAddTo() );
        
    	// ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindDivision( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllDivision( entity );
    }
    

/*
 * @param	event RemoveDepartmentsFromDivisionEvent
 */
@EventHandler( payloadType=RemoveDepartmentsFromDivisionEvent.class)
public void handle( RemoveDepartmentsFromDivisionEvent event) {
    LOGGER.info("handling RemoveDepartmentsFromDivisionEvent - " + event );

    Division entity = removeFromDepartments(event.getDivisionId(), event.getRemoveFrom() );
    
	// ------------------------------------------
	// emit to subscribers that find one
	// ------------------------------------------    	
    emitFindDivision( entity );

	// ------------------------------------------
	// emit to subscribers that find all
	// ------------------------------------------    	
    emitFindAllDivision( entity );
}



    /**
     * Method to retrieve the Division via an DivisionPrimaryKey.
     * @param 	id Long
     * @return 	Division
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public Division handle( FindDivisionQuery query ) 
    throws ProcessingException, IllegalArgumentException {
    	return find( query.getFilter().getDivisionId() );
    }
    
    /**
     * Method to retrieve a collection of all Divisions
     *
     * @param	query	FindAllDivisionQuery 
     * @return 	List<Division> 
     * @exception ProcessingException Thrown if any problems
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public List<Division> handle( FindAllDivisionQuery query ) throws ProcessingException {
    	return findAll( query );
    }


	/**
	 * emit to subscription queries of type FindDivision, 
	 * but only if the id matches
	 * 
	 * @param		entity	Division
	 */
	protected void emitFindDivision( Division entity ) {
		LOGGER.info("handling emitFindDivision" );
		
	    queryUpdateEmitter.emit(FindDivisionQuery.class,
	                            query -> query.getFilter().getDivisionId().equals(entity.getDivisionId()),
	                            entity);
	}
	
	/**
	 * unconditionally emit to subscription queries of type FindAllDivision
	 * 
	 * @param		entity	Division
	 */
	protected void emitFindAllDivision( Division entity ) {
		LOGGER.info("handling emitFindAllDivision" );
		
	    queryUpdateEmitter.emit(FindAllDivisionQuery.class,
	                            query -> true,
	                            entity);
	}


    //--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
	private final QueryUpdateEmitter queryUpdateEmitter;
    private static final Logger LOGGER 	= Logger.getLogger(DivisionProjector.class.getName());

}
