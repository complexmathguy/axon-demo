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
 * Projector for Division as outlined for the CQRS pattern.
 * 
 * Commands are handled by DivisionAggregate
 * 
 * @author Dev Team
 *
 */
@Component("division-entity-projector")
public class DivisionEntityProjector {
		
	// core constructor
	public DivisionEntityProjector(DivisionRepository repository ) {
        this.repository = repository;
    }	

	/*
	 * Insert a Division
	 * 
     * @param	entity Division
     */
    public Division create( Division entity) {
	    LOGGER.info("creating " + entity.toString() );
	   
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    return repository.save(entity);
        
    }

	/*
	 * Update a Division
	 * 
     * @param	entity Division
     */
    public Division update( Division entity) {
	    LOGGER.info("updating " + entity.toString() );

	    // ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		return repository.save(entity);

    }
    
	/*
	 * Delete a Division
	 * 
     * @param	id		UUID
     */
    public Division delete( UUID id ) {
	    LOGGER.info("deleting " + id.toString() );

    	// ------------------------------------------
    	// locate the entity by the provided id
    	// ------------------------------------------
	    Division entity = repository.findById(id).get();
	    
    	// ------------------------------------------
    	// delete what is discovered 
    	// ------------------------------------------
    	repository.delete( entity );
    	
    	return entity;
    }    

    /*
     * Assign a Head
     * 
     * @param	parentId	UUID
     * @param	assignment 	Employee 
     * @return	Division
     */
    public Division assignHead( UUID parentId, Employee assignment ) {
	    LOGGER.info("assigning Head as " + assignment.toString() );

	    Division parentEntity = repository.findById( parentId ).get();
	    assignment = EmployeeProjector.find(assignment.getEmployeeId());
	    
	    // ------------------------------------------
		// assign the Head to the parent entity
		// ------------------------------------------ 
	    parentEntity.setHead( assignment );

	    // ------------------------------------------
    	// save the parent entity
    	// ------------------------------------------ 
	    repository.save(parentEntity);
        
	    return parentEntity;
    }
    

	/*
	 * Unassign the Head
	 * 
	 * @param	parentId		UUID
	 * @return	Division
	 */
	public Division unAssignHead( UUID parentId ) {
		Division parentEntity = repository.findById(parentId).get();

		LOGGER.info("unAssigning Head on " + parentEntity.toString() );
		
	    // ------------------------------------------
		// null out the Head on the parent entithy
		// ------------------------------------------     
	    parentEntity.setHead(null);

	    // ------------------------------------------
		// save the parent entity
		// ------------------------------------------ 
	    repository.save(parentEntity);
    
	    return parentEntity;
	}


    /*
     * Add to the Departments
     * 
     * @param	parentID	UUID
     * @param	addTo		childType
     * @return	Division
     */
    public Division addToDepartments( UUID parentId, Department addTo ) {
	    LOGGER.info("handling AssignDepartmentsToDivisionEvent - " );
	    
	    Division parentEntity = repository.findById(parentId).get();
	    Department child = DepartmentProjector.find(addTo.getDepartmentId());
	    
	    parentEntity.getDepartments().add( child );

	    // ------------------------------------------
    	// save 
    	// ------------------------------------------ 
	    repository.save(parentEntity);
        
	    return parentEntity;
    }
    

    /*
     * Remove from the Departments
     * 
     * @param	parentID	UUID
     * @param	removeFrom	childType
     * @return	Division
     */
	public Division removeFromDepartments( UUID parentId, Department removeFrom ) {
	    LOGGER.info("handling RemoveDepartmentsFromDivisionEvent " );
	
	    Division parentEntity = repository.findById(parentId).get();
	    Department child = DepartmentProjector.find(removeFrom.getDepartmentId());
	    
	    parentEntity.getDepartments().remove( child );
	
	    // ------------------------------------------
		// save
		// ------------------------------------------ 
	    update(parentEntity);
	    
	    return parentEntity;
	}



    /**
     * Method to retrieve the Division via an FindDivisionQuery
     * @return 	query	FindDivisionQuery
     */
    @SuppressWarnings("unused")
    public Division find( UUID id ) {
    	LOGGER.info("handling find using " + id.toString() );
    	try {
    		return repository.findById(id).get();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find a Division - {0}", exc.getMessage() );
    	}
    	return null;
    }
    
    /**
     * Method to retrieve a collection of all Divisions
     *
     * @param	query	FindAllDivisionQuery 
     * @return 	List<Division> 
     */
    @SuppressWarnings("unused")
    public List<Division> findAll( FindAllDivisionQuery query ) {
    	LOGGER.info("handling findAll using " + query.toString() );
    	try {
    		return repository.findAll();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find all Division - {0}", exc.getMessage() );
    	}
    	return null;
    }




    //--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
    protected final DivisionRepository repository;
    @Autowired
	@Qualifier(value = "employee-entity-projector")
	EmployeeEntityProjector EmployeeProjector;
    @Autowired
	@Qualifier(value = "department-entity-projector")
	DepartmentEntityProjector DepartmentProjector;

    private static final Logger LOGGER 	= Logger.getLogger(DivisionEntityProjector.class.getName());

}
