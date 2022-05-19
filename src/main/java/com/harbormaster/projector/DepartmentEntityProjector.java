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
 * Projector for Department as outlined for the CQRS pattern.
 * 
 * Commands are handled by DepartmentAggregate
 * 
 * @author Dev Team
 *
 */
@Component("department-entity-projector")
public class DepartmentEntityProjector {
		
	// core constructor
	public DepartmentEntityProjector(DepartmentRepository repository ) {
        this.repository = repository;
    }	

	/*
	 * Insert a Department
	 * 
     * @param	entity Department
     */
    public Department create( Department entity) {
	    LOGGER.info("creating " + entity.toString() );
	   
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    return repository.save(entity);
        
    }

	/*
	 * Update a Department
	 * 
     * @param	entity Department
     */
    public Department update( Department entity) {
	    LOGGER.info("updating " + entity.toString() );

	    // ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		return repository.save(entity);

    }
    
	/*
	 * Delete a Department
	 * 
     * @param	id		UUID
     */
    public Department delete( UUID id ) {
	    LOGGER.info("deleting " + id.toString() );

    	// ------------------------------------------
    	// locate the entity by the provided id
    	// ------------------------------------------
	    Department entity = repository.findById(id).get();
	    
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
     * @return	Department
     */
    public Department assignHead( UUID parentId, Employee assignment ) {
	    LOGGER.info("assigning Head as " + assignment.toString() );

	    Department parentEntity = repository.findById( parentId ).get();
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
	 * @return	Department
	 */
	public Department unAssignHead( UUID parentId ) {
		Department parentEntity = repository.findById(parentId).get();

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
     * Add to the Employees
     * 
     * @param	parentID	UUID
     * @param	addTo		childType
     * @return	Department
     */
    public Department addToEmployees( UUID parentId, Employee addTo ) {
	    LOGGER.info("handling AssignEmployeesToDepartmentEvent - " );
	    
	    Department parentEntity = repository.findById(parentId).get();
	    Employee child = EmployeeProjector.find(addTo.getEmployeeId());
	    
	    parentEntity.getEmployees().add( child );

	    // ------------------------------------------
    	// save 
    	// ------------------------------------------ 
	    repository.save(parentEntity);
        
	    return parentEntity;
    }
    

    /*
     * Remove from the Employees
     * 
     * @param	parentID	UUID
     * @param	removeFrom	childType
     * @return	Department
     */
	public Department removeFromEmployees( UUID parentId, Employee removeFrom ) {
	    LOGGER.info("handling RemoveEmployeesFromDepartmentEvent " );
	
	    Department parentEntity = repository.findById(parentId).get();
	    Employee child = EmployeeProjector.find(removeFrom.getEmployeeId());
	    
	    parentEntity.getEmployees().remove( child );
	
	    // ------------------------------------------
		// save
		// ------------------------------------------ 
	    update(parentEntity);
	    
	    return parentEntity;
	}



    /**
     * Method to retrieve the Department via an FindDepartmentQuery
     * @return 	query	FindDepartmentQuery
     */
    @SuppressWarnings("unused")
    public Department find( UUID id ) {
    	LOGGER.info("handling find using " + id.toString() );
    	try {
    		return repository.findById(id).get();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find a Department - {0}", exc.getMessage() );
    	}
    	return null;
    }
    
    /**
     * Method to retrieve a collection of all Departments
     *
     * @param	query	FindAllDepartmentQuery 
     * @return 	List<Department> 
     */
    @SuppressWarnings("unused")
    public List<Department> findAll( FindAllDepartmentQuery query ) {
    	LOGGER.info("handling findAll using " + query.toString() );
    	try {
    		return repository.findAll();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find all Department - {0}", exc.getMessage() );
    	}
    	return null;
    }




    //--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
    protected final DepartmentRepository repository;
    @Autowired
	@Qualifier(value = "employee-entity-projector")
	EmployeeEntityProjector EmployeeProjector;

    private static final Logger LOGGER 	= Logger.getLogger(DepartmentEntityProjector.class.getName());

}
