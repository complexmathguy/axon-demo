/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.delegate;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import java.util.concurrent.*;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.harbormaster.api.*;
import com.harbormaster.entity.*;
import com.harbormaster.exception.*;
import com.harbormaster.validator.*;

/**
 * Department business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Department related services in the case of a Department business related service failing.</li>
 * <li>Exposes a simpler, uniform Department interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Department business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class DepartmentBusinessDelegate 
extends BaseBusinessDelegate {
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public DepartmentBusinessDelegate()  {
    	queryGateway 		= applicationContext.getBean(QueryGateway.class);
    	commandGateway 		= applicationContext.getBean(CommandGateway.class);
    	queryUpdateEmitter  = applicationContext.getBean(QueryUpdateEmitter.class);
	}


   /**
	* Department Business Delegate Factory Method
	*
	* All methods are expected to be self-sufficient.
	*
	* @return 	DepartmentBusinessDelegate
	*/
	public static DepartmentBusinessDelegate getDepartmentInstance() {
		return( new DepartmentBusinessDelegate() );
	}
 
   /**
    * Creates the provided command.
    * 
    * @param		command ${class.getCreateCommandAlias()}
    * @exception    ProcessingException
    * @exception	IllegalArgumentException
    * @return		CompletableFuture<UUID>
    */
	public CompletableFuture<UUID> createDepartment( CreateDepartmentCommand command )
    throws ProcessingException, IllegalArgumentException {

		CompletableFuture<UUID> completableFuture = null;
				
		try {
			// --------------------------------------
        	// assign identity now if none
        	// -------------------------------------- 
			if ( command.getDepartmentId() == null )
				command.setDepartmentId( UUID.randomUUID() );
				
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	DepartmentValidator.getInstance().validate( command );    

    		// ---------------------------------------
    		// issue the CreateDepartmentCommand - by convention the future return value for a create command
        	// that is handled by the constructor of an aggregate will return the UUID 
    		// ---------------------------------------
        	completableFuture = commandGateway.send( command );
        	
			LOGGER.log( Level.INFO, "return from Command Gateway for CreateDepartmentCommand of Department is " + command );
			
        }
        catch (Exception exc) {
            final String errMsg = "Unable to create Department - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return completableFuture;
    }

   /**
    * Update the provided command.
    * @param		command UpdateDepartmentCommand
    * @return		CompletableFuture<Void>
    * @exception    ProcessingException
    * @exception  	IllegalArgumentException
    */
    public CompletableFuture<Void> updateDepartment( UpdateDepartmentCommand command ) 
    throws ProcessingException, IllegalArgumentException {
    	CompletableFuture<Void> completableFuture = null;
    	
    	try {       

			// --------------------------------------
        	// validate 
        	// --------------------------------------    	
        	DepartmentValidator.getInstance().validate( command );    

        	// --------------------------------------
        	// issue the UpdateDepartmentCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
    	}
        catch (Exception exc) {
            final String errMsg = "Unable to save Department - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        
    	return completableFuture;
    }
   
   /**
    * Deletes the associatied value object
    * @param		command DeleteDepartmentCommand
    * @return		CompletableFuture<Void>
    * @exception 	ProcessingException
    */
    public CompletableFuture<Void> delete( DeleteDepartmentCommand command ) 
    throws ProcessingException, IllegalArgumentException {	
    	
    	CompletableFuture<Void> completableFuture = null;
    	
        try {  
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	DepartmentValidator.getInstance().validate( command );    
        	
        	// --------------------------------------
        	// issue the DeleteDepartmentCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
        }
        catch (Exception exc) {
            final String errMsg = "Unable to delete Department using Id = "  + command.getDepartmentId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }
        
        return completableFuture;
    }

    /**
     * Method to retrieve the Department via DepartmentFetchOneSummary
     * @param 	summary DepartmentFetchOneSummary 
     * @return 	DepartmentFetchOneResponse
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    public Department getDepartment( DepartmentFetchOneSummary summary ) 
    throws ProcessingException, IllegalArgumentException {
    	
    	if( summary == null )
    		throw new IllegalArgumentException( "DepartmentFetchOneSummary arg cannot be null" );
    	
    	Department entity = null;
    	
        try {
        	// --------------------------------------
        	// validate the fetch one summary
        	// --------------------------------------    	
        	DepartmentValidator.getInstance().validate( summary );    
        	
        	// --------------------------------------
        	// use queryGateway to send request to Find a Department
        	// --------------------------------------
        	CompletableFuture<Department> futureEntity = queryGateway.query(new FindDepartmentQuery( new LoadDepartmentFilter( summary.getDepartmentId() ) ), ResponseTypes.instanceOf(Department.class));
        	
        	entity = futureEntity.get();
        }
        catch( Exception exc ) {
            final String errMsg = "Unable to locate Department with id " + summary.getDepartmentId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return entity;
    }


    /**
     * Method to retrieve a collection of all Departments
     *
     * @return 	List<Department> 
     * @exception ProcessingException Thrown if any problems
     */
    public List<Department> getAllDepartment() 
    throws ProcessingException {
        List<Department> list = null;

        try {
        	CompletableFuture<List<Department>> futureList = queryGateway.query(new FindAllDepartmentQuery(), ResponseTypes.multipleInstancesOf(Department.class));
        	
        	list = futureList.get();
        }
        catch( Exception exc ) {
            String errMsg = "Failed to get all Department";
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return list;
    }

    /**
     * assign Head on Department
     * @param		command AssignHeadToDepartmentCommand	
     * @exception	ProcessingException
     */     
	public void assignHead( AssignHeadToDepartmentCommand command ) throws ProcessingException {

		// --------------------------------------------
		// load the parent
		// --------------------------------------------
		load( command.getDepartmentId() );
		
		EmployeeBusinessDelegate childDelegate 	= EmployeeBusinessDelegate.getEmployeeInstance();
		DepartmentBusinessDelegate parentDelegate = DepartmentBusinessDelegate.getDepartmentInstance();			
		UUID childId = command.getAssignment().getEmployeeId();
		Employee child = null;
		
		try {
			// --------------------------------------
	    	// best to validate the command now
	    	// --------------------------------------    
	    	DepartmentValidator.getInstance().validate( command );    

	    	// --------------------------------------
        	// issue the command
        	// --------------------------------------    	
    		commandGateway.sendAndWait( command );

		}
        catch( Throwable exc ) {
			final String msg = "Failed to get Employee using id " + childId;
			LOGGER.log( Level.WARNING,  msg );
			throw new ProcessingException( msg, exc );
        }
	}

    /**
     * unAssign Head on Department
     * @param		command UnAssignHeadFromDepartmentCommand
     * @exception	ProcessingException
     */     
	public void unAssignHead( UnAssignHeadFromDepartmentCommand command ) throws ProcessingException {

		try {
			// --------------------------------------
	    	// validate the command
	    	// --------------------------------------    
	    	DepartmentValidator.getInstance().validate( command );    
	
	    	// --------------------------------------
	    	// issue the command
	    	// --------------------------------------    	
			commandGateway.sendAndWait( command );
		}
		catch( Exception exc ) {
			final String msg = "Failed to unassign Head on Department";
			LOGGER.log( Level.WARNING, msg, exc );
			throw new ProcessingException( msg, exc );
		}
	}
	

    /**
     * add Employee to Employees 
     * @param		command AssignEmployeesToDepartmentCommand
     * @exception	ProcessingException
     */     
	public void addToEmployees( AssignEmployeesToDepartmentCommand command ) throws ProcessingException {
		
		
		// -------------------------------------------
		// load the parent
		// -------------------------------------------
		load( command.getDepartmentId() );

		EmployeeBusinessDelegate childDelegate 	= EmployeeBusinessDelegate.getEmployeeInstance();
		DepartmentBusinessDelegate parentDelegate = DepartmentBusinessDelegate.getDepartmentInstance();		
		UUID childId = command.getAddTo().getEmployeeId();
		
		try {		
			// --------------------------------------
	    	// validate the command
	    	// --------------------------------------    
	    	DepartmentValidator.getInstance().validate( command );    

	    	// --------------------------------------
        	// issue the command
        	// --------------------------------------    	
    		commandGateway.sendAndWait( command );			
		}
		catch( Exception exc ) {
			final String msg = "Failed to add a Employee as Employees to Department" ; 
			LOGGER.log( Level.WARNING, msg, exc );
			throw new ProcessingException( msg, exc );
		}

	}

    /**
     * remove Employee from Employees
     * @param		command RemoveEmployeesFromDepartmentCommand
     * @exception	ProcessingException
     */     	
	public void removeFromEmployees( RemoveEmployeesFromDepartmentCommand command ) throws ProcessingException {		
		
		EmployeeBusinessDelegate childDelegate 	= EmployeeBusinessDelegate.getEmployeeInstance();
		UUID childId = command.getRemoveFrom().getEmployeeId();

		try {
			
			// --------------------------------------
	    	// validate the command
	    	// --------------------------------------    
	    	DepartmentValidator.getInstance().validate( command );    

	    	// --------------------------------------
	    	// issue the command
	    	// --------------------------------------    	
			commandGateway.sendAndWait( command );

		}
		catch( Exception exc ) {
			final String msg = "Failed to remove child using Id " + childId; 
			LOGGER.log( Level.WARNING, msg, exc );
			throw new ProcessingException( msg, exc );
		}
	}



	/**
	 * Internal helper method to load the root 
	 * 
	 * @param		id	UUID
	 * @return		Department
	 */
	protected Department load( UUID id ) throws ProcessingException {
		department = DepartmentBusinessDelegate.getDepartmentInstance().getDepartment( new DepartmentFetchOneSummary(id) );	
		return department;
	}


//************************************************************************
// Attributes
//************************************************************************
	private final QueryGateway queryGateway;
	private final CommandGateway commandGateway;
	private final QueryUpdateEmitter queryUpdateEmitter;
	private Department department 	= null;
    private static final Logger LOGGER 			= Logger.getLogger(DepartmentBusinessDelegate.class.getName());
    
}
