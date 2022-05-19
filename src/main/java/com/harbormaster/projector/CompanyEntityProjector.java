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
 * Projector for Company as outlined for the CQRS pattern.
 * 
 * Commands are handled by CompanyAggregate
 * 
 * @author Dev Team
 *
 */
@Component("company-entity-projector")
public class CompanyEntityProjector {
		
	// core constructor
	public CompanyEntityProjector(CompanyRepository repository ) {
        this.repository = repository;
    }	

	/*
	 * Insert a Company
	 * 
     * @param	entity Company
     */
    public Company create( Company entity) {
	    LOGGER.info("creating " + entity.toString() );
	   
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    return repository.save(entity);
        
    }

	/*
	 * Update a Company
	 * 
     * @param	entity Company
     */
    public Company update( Company entity) {
	    LOGGER.info("updating " + entity.toString() );

	    // ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		return repository.save(entity);

    }
    
	/*
	 * Delete a Company
	 * 
     * @param	id		UUID
     */
    public Company delete( UUID id ) {
	    LOGGER.info("deleting " + id.toString() );

    	// ------------------------------------------
    	// locate the entity by the provided id
    	// ------------------------------------------
	    Company entity = repository.findById(id).get();
	    
    	// ------------------------------------------
    	// delete what is discovered 
    	// ------------------------------------------
    	repository.delete( entity );
    	
    	return entity;
    }    


    /*
     * Add to the Divisions
     * 
     * @param	parentID	UUID
     * @param	addTo		childType
     * @return	Company
     */
    public Company addToDivisions( UUID parentId, Division addTo ) {
	    LOGGER.info("handling AssignDivisionsToCompanyEvent - " );
	    
	    Company parentEntity = repository.findById(parentId).get();
	    Division child = DivisionProjector.find(addTo.getDivisionId());
	    
	    parentEntity.getDivisions().add( child );

	    // ------------------------------------------
    	// save 
    	// ------------------------------------------ 
	    repository.save(parentEntity);
        
	    return parentEntity;
    }
    

    /*
     * Remove from the Divisions
     * 
     * @param	parentID	UUID
     * @param	removeFrom	childType
     * @return	Company
     */
	public Company removeFromDivisions( UUID parentId, Division removeFrom ) {
	    LOGGER.info("handling RemoveDivisionsFromCompanyEvent " );
	
	    Company parentEntity = repository.findById(parentId).get();
	    Division child = DivisionProjector.find(removeFrom.getDivisionId());
	    
	    parentEntity.getDivisions().remove( child );
	
	    // ------------------------------------------
		// save
		// ------------------------------------------ 
	    update(parentEntity);
	    
	    return parentEntity;
	}

    /*
     * Add to the BoardMembers
     * 
     * @param	parentID	UUID
     * @param	addTo		childType
     * @return	Company
     */
    public Company addToBoardMembers( UUID parentId, Employee addTo ) {
	    LOGGER.info("handling AssignBoardMembersToCompanyEvent - " );
	    
	    Company parentEntity = repository.findById(parentId).get();
	    Employee child = EmployeeProjector.find(addTo.getEmployeeId());
	    
	    parentEntity.getBoardMembers().add( child );

	    // ------------------------------------------
    	// save 
    	// ------------------------------------------ 
	    repository.save(parentEntity);
        
	    return parentEntity;
    }
    

    /*
     * Remove from the BoardMembers
     * 
     * @param	parentID	UUID
     * @param	removeFrom	childType
     * @return	Company
     */
	public Company removeFromBoardMembers( UUID parentId, Employee removeFrom ) {
	    LOGGER.info("handling DemomotedFromBoardMemberCommand " );
	
	    Company parentEntity = repository.findById(parentId).get();
	    Employee child = EmployeeProjector.find(removeFrom.getEmployeeId());
	    
	    parentEntity.getBoardMembers().remove( child );
	
	    // ------------------------------------------
		// save
		// ------------------------------------------ 
	    update(parentEntity);
	    
	    return parentEntity;
	}



    /**
     * Method to retrieve the Company via an FindCompanyQuery
     * @return 	query	FindCompanyQuery
     */
    @SuppressWarnings("unused")
    public Company find( UUID id ) {
    	LOGGER.info("handling find using " + id.toString() );
    	try {
    		return repository.findById(id).get();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find a Company - {0}", exc.getMessage() );
    	}
    	return null;
    }
    
    /**
     * Method to retrieve a collection of all Companys
     *
     * @param	query	FindAllCompanyQuery 
     * @return 	List<Company> 
     */
    @SuppressWarnings("unused")
    public List<Company> findAll( FindAllCompanyQuery query ) {
    	LOGGER.info("handling findAll using " + query.toString() );
    	try {
    		return repository.findAll();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find all Company - {0}", exc.getMessage() );
    	}
    	return null;
    }

    /**
     * query method to findCompanyByName
     * @param 		String name
     * @return		Company
     */     
	@SuppressWarnings("unused")
	public Company findCompanyByName( FindByNameQuery query ) {
		LOGGER.info("handling findCompanyByName" );
		Company result = null;
		
		try {
		    result = repository.findCompanyByName( query.getFilter().getName() );

        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to findCompanyByName using " + query.getFilter(), exc );
        }
        
        return result;
	}
    /**
     * query method to findCompaniesByIndustry
     * @param 		Industry industry
     * @return		List<Company>
     */     
	@SuppressWarnings("unused")
	public List<Company> findCompaniesByIndustry( FindCompaniesByIndustryQuery query ) {
		LOGGER.info("handling findCompaniesByIndustry" );
		List<Company> result = null;
		
		try {
            Pageable pageable = PageRequest.of(query.getOffset(), query.getLimit());
            result = repository.findCompaniesByIndustry( query.getFilter().getIndustry(), pageable );

        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to findCompaniesByIndustry using " + query.getFilter(), exc );
        }
        
        return result;
	}
    /**
     * query method to findCompaniesByType
     * @param 		CompanyType type
     * @return		List<Company>
     */     
	@SuppressWarnings("unused")
	public List<Company> findCompaniesByType( FindByTypeQuery query ) {
		LOGGER.info("handling findCompaniesByType" );
		List<Company> result = null;
		
		try {
            Pageable pageable = PageRequest.of(query.getOffset(), query.getLimit());
            result = repository.findCompaniesByType( query.getFilter().getType(), pageable );

        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to findCompaniesByType using " + query.getFilter(), exc );
        }
        
        return result;
	}



    //--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
    protected final CompanyRepository repository;
    @Autowired
	@Qualifier(value = "division-entity-projector")
	DivisionEntityProjector DivisionProjector;
    @Autowired
	@Qualifier(value = "employee-entity-projector")
	EmployeeEntityProjector EmployeeProjector;

    private static final Logger LOGGER 	= Logger.getLogger(CompanyEntityProjector.class.getName());

}
