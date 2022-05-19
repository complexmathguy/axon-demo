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
 * Division business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Division related services in the case of a Division business related service failing.</li>
 * <li>Exposes a simpler, uniform Division interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Division business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class DivisionBusinessDelegate 
extends BaseBusinessDelegate {
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public DivisionBusinessDelegate()  {
    	queryGateway 		= applicationContext.getBean(QueryGateway.class);
    	commandGateway 		= applicationContext.getBean(CommandGateway.class);
    	queryUpdateEmitter  = applicationContext.getBean(QueryUpdateEmitter.class);
	}


   /**
	* Division Business Delegate Factory Method
	*
	* All methods are expected to be self-sufficient.
	*
	* @return 	DivisionBusinessDelegate
	*/
	public static DivisionBusinessDelegate getDivisionInstance() {
		return( new DivisionBusinessDelegate() );
	}
 
   /**
    * Creates the provided command.
    * 
    * @param		command ${class.getCreateCommandAlias()}
    * @exception    ProcessingException
    * @exception	IllegalArgumentException
    * @return		CompletableFuture<UUID>
    */
	public CompletableFuture<UUID> createDivision( CreateDivisionCommand command )
    throws ProcessingException, IllegalArgumentException {

		CompletableFuture<UUID> completableFuture = null;
				
		try {
			// --------------------------------------
        	// assign identity now if none
        	// -------------------------------------- 
			if ( command.getDivisionId() == null )
				command.setDivisionId( UUID.randomUUID() );
				
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	DivisionValidator.getInstance().validate( command );    

    		// ---------------------------------------
    		// issue the CreateDivisionCommand - by convention the future return value for a create command
        	// that is handled by the constructor of an aggregate will return the UUID 
    		// ---------------------------------------
        	completableFuture = commandGateway.send( command );
        	
			LOGGER.log( Level.INFO, "return from Command Gateway for CreateDivisionCommand of Division is " + command );
			
        }
        catch (Exception exc) {
            final String errMsg = "Unable to create Division - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return completableFuture;
    }

   /**
    * Update the provided command.
    * @param		command UpdateDivisionCommand
    * @return		CompletableFuture<Void>
    * @exception    ProcessingException
    * @exception  	IllegalArgumentException
    */
    public CompletableFuture<Void> updateDivision( UpdateDivisionCommand command ) 
    throws ProcessingException, IllegalArgumentException {
    	CompletableFuture<Void> completableFuture = null;
    	
    	try {       

			// --------------------------------------
        	// validate 
        	// --------------------------------------    	
        	DivisionValidator.getInstance().validate( command );    

        	// --------------------------------------
        	// issue the UpdateDivisionCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
    	}
        catch (Exception exc) {
            final String errMsg = "Unable to save Division - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        
    	return completableFuture;
    }
   
   /**
    * Deletes the associatied value object
    * @param		command DeleteDivisionCommand
    * @return		CompletableFuture<Void>
    * @exception 	ProcessingException
    */
    public CompletableFuture<Void> delete( DeleteDivisionCommand command ) 
    throws ProcessingException, IllegalArgumentException {	
    	
    	CompletableFuture<Void> completableFuture = null;
    	
        try {  
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	DivisionValidator.getInstance().validate( command );    
        	
        	// --------------------------------------
        	// issue the DeleteDivisionCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
        }
        catch (Exception exc) {
            final String errMsg = "Unable to delete Division using Id = "  + command.getDivisionId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }
        
        return completableFuture;
    }

    /**
     * Method to retrieve the Division via DivisionFetchOneSummary
     * @param 	summary DivisionFetchOneSummary 
     * @return 	DivisionFetchOneResponse
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    public Division getDivision( DivisionFetchOneSummary summary ) 
    throws ProcessingException, IllegalArgumentException {
    	
    	if( summary == null )
    		throw new IllegalArgumentException( "DivisionFetchOneSummary arg cannot be null" );
    	
    	Division entity = null;
    	
        try {
        	// --------------------------------------
        	// validate the fetch one summary
        	// --------------------------------------    	
        	DivisionValidator.getInstance().validate( summary );    
        	
        	// --------------------------------------
        	// use queryGateway to send request to Find a Division
        	// --------------------------------------
        	CompletableFuture<Division> futureEntity = queryGateway.query(new FindDivisionQuery( new LoadDivisionFilter( summary.getDivisionId() ) ), ResponseTypes.instanceOf(Division.class));
        	
        	entity = futureEntity.get();
        }
        catch( Exception exc ) {
            final String errMsg = "Unable to locate Division with id " + summary.getDivisionId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return entity;
    }


    /**
     * Method to retrieve a collection of all Divisions
     *
     * @return 	List<Division> 
     * @exception ProcessingException Thrown if any problems
     */
    public List<Division> getAllDivision() 
    throws ProcessingException {
        List<Division> list = null;

        try {
        	CompletableFuture<List<Division>> futureList = queryGateway.query(new FindAllDivisionQuery(), ResponseTypes.multipleInstancesOf(Division.class));
        	
        	list = futureList.get();
        }
        catch( Exception exc ) {
            String errMsg = "Failed to get all Division";
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return list;
    }

    /**
     * assign Head on Division
     * @param		command PromoteToDivisionHeadCommand	
     * @exception	ProcessingException
     */     
	public void assignHead( PromoteToDivisionHeadCommand command ) throws ProcessingException {

		// --------------------------------------------
		// load the parent
		// --------------------------------------------
		load( command.getDivisionId() );
		
		EmployeeBusinessDelegate childDelegate 	= EmployeeBusinessDelegate.getEmployeeInstance();
		DivisionBusinessDelegate parentDelegate = DivisionBusinessDelegate.getDivisionInstance();			
		UUID childId = command.getAssignment().getEmployeeId();
		Employee child = null;
		
		try {
			// --------------------------------------
	    	// best to validate the command now
	    	// --------------------------------------    
	    	DivisionValidator.getInstance().validate( command );    

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
     * unAssign Head on Division
     * @param		command DemoteFromDivisionHeadCommand
     * @exception	ProcessingException
     */     
	public void unAssignHead( DemoteFromDivisionHeadCommand command ) throws ProcessingException {

		try {
			// --------------------------------------
	    	// validate the command
	    	// --------------------------------------    
	    	DivisionValidator.getInstance().validate( command );    
	
	    	// --------------------------------------
	    	// issue the command
	    	// --------------------------------------    	
			commandGateway.sendAndWait( command );
		}
		catch( Exception exc ) {
			final String msg = "Failed to unassign Head on Division";
			LOGGER.log( Level.WARNING, msg, exc );
			throw new ProcessingException( msg, exc );
		}
	}
	

    /**
     * add Department to Departments 
     * @param		command AssignDepartmentsToDivisionCommand
     * @exception	ProcessingException
     */     
	public void addToDepartments( AssignDepartmentsToDivisionCommand command ) throws ProcessingException {
		
		
		// -------------------------------------------
		// load the parent
		// -------------------------------------------
		load( command.getDivisionId() );

		DepartmentBusinessDelegate childDelegate 	= DepartmentBusinessDelegate.getDepartmentInstance();
		DivisionBusinessDelegate parentDelegate = DivisionBusinessDelegate.getDivisionInstance();		
		UUID childId = command.getAddTo().getDepartmentId();
		
		try {		
			// --------------------------------------
	    	// validate the command
	    	// --------------------------------------    
	    	DivisionValidator.getInstance().validate( command );    

	    	// --------------------------------------
        	// issue the command
        	// --------------------------------------    	
    		commandGateway.sendAndWait( command );			
		}
		catch( Exception exc ) {
			final String msg = "Failed to add a Department as Departments to Division" ; 
			LOGGER.log( Level.WARNING, msg, exc );
			throw new ProcessingException( msg, exc );
		}

	}

    /**
     * remove Department from Departments
     * @param		command RemoveDepartmentsFromDivisionCommand
     * @exception	ProcessingException
     */     	
	public void removeFromDepartments( RemoveDepartmentsFromDivisionCommand command ) throws ProcessingException {		
		
		DepartmentBusinessDelegate childDelegate 	= DepartmentBusinessDelegate.getDepartmentInstance();
		UUID childId = command.getRemoveFrom().getDepartmentId();

		try {
			
			// --------------------------------------
	    	// validate the command
	    	// --------------------------------------    
	    	DivisionValidator.getInstance().validate( command );    

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
	 * @return		Division
	 */
	protected Division load( UUID id ) throws ProcessingException {
		division = DivisionBusinessDelegate.getDivisionInstance().getDivision( new DivisionFetchOneSummary(id) );	
		return division;
	}


//************************************************************************
// Attributes
//************************************************************************
	private final QueryGateway queryGateway;
	private final CommandGateway commandGateway;
	private final QueryUpdateEmitter queryUpdateEmitter;
	private Division division 	= null;
    private static final Logger LOGGER 			= Logger.getLogger(DivisionBusinessDelegate.class.getName());
    
}
