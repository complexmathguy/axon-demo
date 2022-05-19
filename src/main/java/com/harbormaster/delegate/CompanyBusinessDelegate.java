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
 * Company business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Company related services in the case of a Company business related service failing.</li>
 * <li>Exposes a simpler, uniform Company interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Company business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class CompanyBusinessDelegate 
extends BaseBusinessDelegate {
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public CompanyBusinessDelegate()  {
    	queryGateway 		= applicationContext.getBean(QueryGateway.class);
    	commandGateway 		= applicationContext.getBean(CommandGateway.class);
    	queryUpdateEmitter  = applicationContext.getBean(QueryUpdateEmitter.class);
	}


   /**
	* Company Business Delegate Factory Method
	*
	* All methods are expected to be self-sufficient.
	*
	* @return 	CompanyBusinessDelegate
	*/
	public static CompanyBusinessDelegate getCompanyInstance() {
		return( new CompanyBusinessDelegate() );
	}
 
   /**
    * Creates the provided command.
    * 
    * @param		command ${class.getCreateCommandAlias()}
    * @exception    ProcessingException
    * @exception	IllegalArgumentException
    * @return		CompletableFuture<UUID>
    */
	public CompletableFuture<UUID> createCompany( CreateCompanyCommand command )
    throws ProcessingException, IllegalArgumentException {

		CompletableFuture<UUID> completableFuture = null;
				
		try {
			// --------------------------------------
        	// assign identity now if none
        	// -------------------------------------- 
			if ( command.getCompanyId() == null )
				command.setCompanyId( UUID.randomUUID() );
				
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	CompanyValidator.getInstance().validate( command );    

    		// ---------------------------------------
    		// issue the CreateCompanyCommand - by convention the future return value for a create command
        	// that is handled by the constructor of an aggregate will return the UUID 
    		// ---------------------------------------
        	completableFuture = commandGateway.send( command );
        	
			LOGGER.log( Level.INFO, "return from Command Gateway for CreateCompanyCommand of Company is " + command );
			
        }
        catch (Exception exc) {
            final String errMsg = "Unable to create Company - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return completableFuture;
    }

   /**
    * Update the provided command.
    * @param		command RefreshCompanyCommand
    * @return		CompletableFuture<Void>
    * @exception    ProcessingException
    * @exception  	IllegalArgumentException
    */
    public CompletableFuture<Void> updateCompany( RefreshCompanyCommand command ) 
    throws ProcessingException, IllegalArgumentException {
    	CompletableFuture<Void> completableFuture = null;
    	
    	try {       

			// --------------------------------------
        	// validate 
        	// --------------------------------------    	
        	CompanyValidator.getInstance().validate( command );    

        	// --------------------------------------
        	// issue the RefreshCompanyCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
    	}
        catch (Exception exc) {
            final String errMsg = "Unable to save Company - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        
    	return completableFuture;
    }
   
   /**
    * Deletes the associatied value object
    * @param		command CloseCompanyCommand
    * @return		CompletableFuture<Void>
    * @exception 	ProcessingException
    */
    public CompletableFuture<Void> delete( CloseCompanyCommand command ) 
    throws ProcessingException, IllegalArgumentException {	
    	
    	CompletableFuture<Void> completableFuture = null;
    	
        try {  
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	CompanyValidator.getInstance().validate( command );    
        	
        	// --------------------------------------
        	// issue the CloseCompanyCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
        }
        catch (Exception exc) {
            final String errMsg = "Unable to delete Company using Id = "  + command.getCompanyId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }
        
        return completableFuture;
    }

    /**
     * Method to retrieve the Company via CompanyFetchOneSummary
     * @param 	summary CompanyFetchOneSummary 
     * @return 	CompanyFetchOneResponse
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    public Company getCompany( CompanyFetchOneSummary summary ) 
    throws ProcessingException, IllegalArgumentException {
    	
    	if( summary == null )
    		throw new IllegalArgumentException( "CompanyFetchOneSummary arg cannot be null" );
    	
    	Company entity = null;
    	
        try {
        	// --------------------------------------
        	// validate the fetch one summary
        	// --------------------------------------    	
        	CompanyValidator.getInstance().validate( summary );    
        	
        	// --------------------------------------
        	// use queryGateway to send request to Find a Company
        	// --------------------------------------
        	CompletableFuture<Company> futureEntity = queryGateway.query(new FindCompanyQuery( new LoadCompanyFilter( summary.getCompanyId() ) ), ResponseTypes.instanceOf(Company.class));
        	
        	entity = futureEntity.get();
        }
        catch( Exception exc ) {
            final String errMsg = "Unable to locate Company with id " + summary.getCompanyId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return entity;
    }


    /**
     * Method to retrieve a collection of all Companys
     *
     * @return 	List<Company> 
     * @exception ProcessingException Thrown if any problems
     */
    public List<Company> getAllCompany() 
    throws ProcessingException {
        List<Company> list = null;

        try {
        	CompletableFuture<List<Company>> futureList = queryGateway.query(new FindAllCompanyQuery(), ResponseTypes.multipleInstancesOf(Company.class));
        	
        	list = futureList.get();
        }
        catch( Exception exc ) {
            String errMsg = "Failed to get all Company";
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return list;
    }


    /**
     * add Division to Divisions 
     * @param		command AssignDivisionsToCompanyCommand
     * @exception	ProcessingException
     */     
	public void addToDivisions( AssignDivisionsToCompanyCommand command ) throws ProcessingException {
		
		
		// -------------------------------------------
		// load the parent
		// -------------------------------------------
		load( command.getCompanyId() );

		DivisionBusinessDelegate childDelegate 	= DivisionBusinessDelegate.getDivisionInstance();
		CompanyBusinessDelegate parentDelegate = CompanyBusinessDelegate.getCompanyInstance();		
		UUID childId = command.getAddTo().getDivisionId();
		
		try {		
			// --------------------------------------
	    	// validate the command
	    	// --------------------------------------    
	    	CompanyValidator.getInstance().validate( command );    

	    	// --------------------------------------
        	// issue the command
        	// --------------------------------------    	
    		commandGateway.sendAndWait( command );			
		}
		catch( Exception exc ) {
			final String msg = "Failed to add a Division as Divisions to Company" ; 
			LOGGER.log( Level.WARNING, msg, exc );
			throw new ProcessingException( msg, exc );
		}

	}

    /**
     * remove Division from Divisions
     * @param		command RemoveDivisionsFromCompanyCommand
     * @exception	ProcessingException
     */     	
	public void removeFromDivisions( RemoveDivisionsFromCompanyCommand command ) throws ProcessingException {		
		
		DivisionBusinessDelegate childDelegate 	= DivisionBusinessDelegate.getDivisionInstance();
		UUID childId = command.getRemoveFrom().getDivisionId();

		try {
			
			// --------------------------------------
	    	// validate the command
	    	// --------------------------------------    
	    	CompanyValidator.getInstance().validate( command );    

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
     * add Employee to BoardMembers 
     * @param		command AssignBoardMembersToCompanyCommand
     * @exception	ProcessingException
     */     
	public void addToBoardMembers( AssignBoardMembersToCompanyCommand command ) throws ProcessingException {
		
		
		// -------------------------------------------
		// load the parent
		// -------------------------------------------
		load( command.getCompanyId() );

		EmployeeBusinessDelegate childDelegate 	= EmployeeBusinessDelegate.getEmployeeInstance();
		CompanyBusinessDelegate parentDelegate = CompanyBusinessDelegate.getCompanyInstance();		
		UUID childId = command.getAddTo().getEmployeeId();
		
		try {		
			// --------------------------------------
	    	// validate the command
	    	// --------------------------------------    
	    	CompanyValidator.getInstance().validate( command );    

	    	// --------------------------------------
        	// issue the command
        	// --------------------------------------    	
    		commandGateway.sendAndWait( command );			
		}
		catch( Exception exc ) {
			final String msg = "Failed to add a Employee as BoardMembers to Company" ; 
			LOGGER.log( Level.WARNING, msg, exc );
			throw new ProcessingException( msg, exc );
		}

	}

    /**
     * remove Employee from BoardMembers
     * @param		command DemoteFromBoardMemberCommand
     * @exception	ProcessingException
     */     	
	public void removeFromBoardMembers( DemoteFromBoardMemberCommand command ) throws ProcessingException {		
		
		EmployeeBusinessDelegate childDelegate 	= EmployeeBusinessDelegate.getEmployeeInstance();
		UUID childId = command.getRemoveFrom().getEmployeeId();

		try {
			
			// --------------------------------------
	    	// validate the command
	    	// --------------------------------------    
	    	CompanyValidator.getInstance().validate( command );    

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
     * finder method to findCompanyByName
     * @param 		String name
     * @return		Company
     */     
	public Company findCompanyByName( FindByNameQuery query ) {
		Company result = null;
        try {  
		    CompletableFuture<Company> futureResult = queryGateway.query(query, ResponseTypes.instanceOf(Company.class));
        	result = futureResult.get();
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to execute findCompanyByName", exc );
        }
        return result;
	}

    /**
     * finder method to findCompaniesByIndustry
     * @param 		Industry industry
     * @return		List<Company>
     */     
	public List<Company> findCompaniesByIndustry( FindCompaniesByIndustryQuery query ) {
		List<Company> result = null;
        try {  
		    CompletableFuture<List<Company>> futureResult = queryGateway.query(query, ResponseTypes.multipleInstancesOf(Company.class));
        	result = futureResult.get();
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to execute findCompaniesByIndustry", exc );
        }
        return result;
	}

    /**
     * finder method to findCompaniesByType
     * @param 		CompanyType type
     * @return		List<Company>
     */     
	public List<Company> findCompaniesByType( FindByTypeQuery query ) {
		List<Company> result = null;
        try {  
		    CompletableFuture<List<Company>> futureResult = queryGateway.query(query, ResponseTypes.multipleInstancesOf(Company.class));
        	result = futureResult.get();
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to execute findCompaniesByType", exc );
        }
        return result;
	}

	/**
	 * Internal helper method to load the root 
	 * 
	 * @param		id	UUID
	 * @return		Company
	 */
	protected Company load( UUID id ) throws ProcessingException {
		company = CompanyBusinessDelegate.getCompanyInstance().getCompany( new CompanyFetchOneSummary(id) );	
		return company;
	}


//************************************************************************
// Attributes
//************************************************************************
	private final QueryGateway queryGateway;
	private final CommandGateway commandGateway;
	private final QueryUpdateEmitter queryUpdateEmitter;
	private Company company 	= null;
    private static final Logger LOGGER 			= Logger.getLogger(CompanyBusinessDelegate.class.getName());
    
}
