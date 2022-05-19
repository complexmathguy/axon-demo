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
 * Projector for Company as outlined for the CQRS pattern.  All event handling and query handling related to Company are invoked here
 * and dispersed as an event to be handled elsewhere.
 * 
 * Commands are handled by CompanyAggregate
 * 
 * @author Dev Team
 *
 */
@ProcessingGroup("company")
@Component("company-projector")
public class CompanyProjector extends CompanyEntityProjector {
		
	// core constructor
	public CompanyProjector(CompanyRepository repository, QueryUpdateEmitter queryUpdateEmitter ) {
        super(repository);
        this.queryUpdateEmitter = queryUpdateEmitter;
    }	

	/*
     * @param	event CreatedCompanyEvent
     */
    @EventHandler( payloadType=CreatedCompanyEvent.class )
    public void handle( CreatedCompanyEvent event) {
	    LOGGER.info("handling CreatedCompanyEvent - " + event );
	    Company entity = new Company();
        entity.setCompanyId( event.getCompanyId() );
        entity.setName( event.getName() );
        entity.setEstablishedOn( event.getEstablishedOn() );
        entity.setRevenue( event.getRevenue() );
        entity.setAddress( event.getAddress() );
        entity.setBillingAddress( event.getBillingAddress() );
        entity.setType( event.getType() );
        entity.setIndustry( event.getIndustry() );
	    
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    create(entity);
        
        // ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllCompany( entity );
    }

    /*
     * @param	event RefreshedCompanyEvent
     */
    @EventHandler( payloadType=RefreshedCompanyEvent.class )
    public void handle( RefreshedCompanyEvent event) {
    	LOGGER.info("handling RefreshedCompanyEvent - " + event );
    	
	    Company entity = new Company();
        entity.setCompanyId( event.getCompanyId() );
        entity.setName( event.getName() );
        entity.setEstablishedOn( event.getEstablishedOn() );
        entity.setRevenue( event.getRevenue() );
        entity.setDivisions( event.getDivisions() );
        entity.setBoardMembers( event.getBoardMembers() );
        entity.setAddress( event.getAddress() );
        entity.setBillingAddress( event.getBillingAddress() );
        entity.setType( event.getType() );
        entity.setIndustry( event.getIndustry() );
 
    	// ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		update(entity);

    	// ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindCompany( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllCompany( entity );        
    }
    
    /*
     * @param	event ClosedCompanyEvent
     */
    @EventHandler( payloadType=ClosedCompanyEvent.class )
    public void handle( ClosedCompanyEvent event) {
    	LOGGER.info("handling ClosedCompanyEvent - " + event );
    	
    	// ------------------------------------------
    	// delete delegation
    	// ------------------------------------------
    	Company entity = delete( event.getCompanyId() );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllCompany( entity );

    }    


    /*
     * @param	event AssignDivisionsToCompanyEvent
     */
    @EventHandler( payloadType=AssignDivisionsToCompanyEvent.class)
    public void handle( AssignDivisionsToCompanyEvent event) {
	    LOGGER.info("handling AssignDivisionsToCompanyEvent - " + event );
	    
	    // ------------------------------------------
    	// delegate to addTo 
    	// ------------------------------------------ 
	    Company entity = addToDivisions(event.getCompanyId(), event.getAddTo() );
        
    	// ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindCompany( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllCompany( entity );
    }
    

/*
 * @param	event RemoveDivisionsFromCompanyEvent
 */
@EventHandler( payloadType=RemoveDivisionsFromCompanyEvent.class)
public void handle( RemoveDivisionsFromCompanyEvent event) {
    LOGGER.info("handling RemoveDivisionsFromCompanyEvent - " + event );

    Company entity = removeFromDivisions(event.getCompanyId(), event.getRemoveFrom() );
    
	// ------------------------------------------
	// emit to subscribers that find one
	// ------------------------------------------    	
    emitFindCompany( entity );

	// ------------------------------------------
	// emit to subscribers that find all
	// ------------------------------------------    	
    emitFindAllCompany( entity );
}

    /*
     * @param	event AssignBoardMembersToCompanyEvent
     */
    @EventHandler( payloadType=AssignBoardMembersToCompanyEvent.class)
    public void handle( AssignBoardMembersToCompanyEvent event) {
	    LOGGER.info("handling AssignBoardMembersToCompanyEvent - " + event );
	    
	    // ------------------------------------------
    	// delegate to addTo 
    	// ------------------------------------------ 
	    Company entity = addToBoardMembers(event.getCompanyId(), event.getAddTo() );
        
    	// ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindCompany( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllCompany( entity );
    }
    

/*
 * @param	event DemomotedFromBoardMemberCommand
 */
@EventHandler( payloadType=DemomotedFromBoardMemberCommand.class)
public void handle( DemomotedFromBoardMemberCommand event) {
    LOGGER.info("handling DemomotedFromBoardMemberCommand - " + event );

    Company entity = removeFromBoardMembers(event.getCompanyId(), event.getRemoveFrom() );
    
	// ------------------------------------------
	// emit to subscribers that find one
	// ------------------------------------------    	
    emitFindCompany( entity );

	// ------------------------------------------
	// emit to subscribers that find all
	// ------------------------------------------    	
    emitFindAllCompany( entity );
}



    /**
     * Method to retrieve the Company via an CompanyPrimaryKey.
     * @param 	id Long
     * @return 	Company
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public Company handle( FindCompanyQuery query ) 
    throws ProcessingException, IllegalArgumentException {
    	return find( query.getFilter().getCompanyId() );
    }
    
    /**
     * Method to retrieve a collection of all Companys
     *
     * @param	query	FindAllCompanyQuery 
     * @return 	List<Company> 
     * @exception ProcessingException Thrown if any problems
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public List<Company> handle( FindAllCompanyQuery query ) throws ProcessingException {
    	return findAll( query );
    }


	/**
	 * emit to subscription queries of type FindCompany, 
	 * but only if the id matches
	 * 
	 * @param		entity	Company
	 */
	protected void emitFindCompany( Company entity ) {
		LOGGER.info("handling emitFindCompany" );
		
	    queryUpdateEmitter.emit(FindCompanyQuery.class,
	                            query -> query.getFilter().getCompanyId().equals(entity.getCompanyId()),
	                            entity);
	}
	
	/**
	 * unconditionally emit to subscription queries of type FindAllCompany
	 * 
	 * @param		entity	Company
	 */
	protected void emitFindAllCompany( Company entity ) {
		LOGGER.info("handling emitFindAllCompany" );
		
	    queryUpdateEmitter.emit(FindAllCompanyQuery.class,
	                            query -> true,
	                            entity);
	}


    //--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
	private final QueryUpdateEmitter queryUpdateEmitter;
    private static final Logger LOGGER 	= Logger.getLogger(CompanyProjector.class.getName());

}
