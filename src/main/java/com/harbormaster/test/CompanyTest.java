/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.test;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.util.Assert.state;

import com.harbormaster.delegate.*;
import com.harbormaster.entity.*;
import com.harbormaster.api.*;
import com.harbormaster.subscriber.*;

/**
 * Test Company class.
 *
 * @author Dev Team
 */
public class CompanyTest{

    // ------------------------------------
	// default constructor
    // ------------------------------------
	public CompanyTest() {
		subscriber = new CompanySubscriber();
	}

	// test methods
	@Test
	/*
	 * Initiate CompanyTest.
	 */
	public void startTest() throws Throwable {
		try {
			LOGGER.info("**********************************************************");
			LOGGER.info("Beginning test on Company...");
			LOGGER.info("**********************************************************\n");
			
			// ---------------------------------------------
			// jumpstart process
			// ---------------------------------------------
			jumpStart();
			
		} catch (Throwable e) {
			throw e;
		} finally {
		}
	}

	/** 
	 * jumpstart the process by instantiating2 Company
	 */
	protected void jumpStart() throws Throwable {
		LOGGER.info( "\n======== create instances to get the ball rolling  ======== ");

		companyId = CompanyBusinessDelegate.getCompanyInstance()
		.createCompany( generateNewCommand() )
		.get();

		// ---------------------------------------------
		// set up query subscriptions after the 1st create
		// ---------------------------------------------
		testingStep = "create";
		setUpQuerySubscriptions();

		CompanyBusinessDelegate.getCompanyInstance()
				.createCompany( generateNewCommand() )
				.get();

	}

	/** 
	 * Set up query subscriptions
	 */
	protected void setUpQuerySubscriptions() throws Throwable {
		LOGGER.info( "\n======== Setting Up Query Subscriptions ======== ");
			
		try {            
			subscriber.companySubscribe().updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetAll update received for Company : " + successValue.getCompanyId());
							  if (successValue.getCompanyId().equals(companyId)) {
								  if (testingStep.equals("create")) {
									  testingStep = "update";
									  update();
								  } else if (testingStep.equals("delete")) {
									  testingStep = "complete";
									  state( getAll().size() == sizeOfCompanyList - 1 , "value not deleted from list");
									  LOGGER.info("**********************************************************");
									  LOGGER.info("Company test completed successfully...");
									  LOGGER.info("**********************************************************\n");
								  }
							  }
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on company consumed")
					);
			subscriber.companySubscribe( companyId ).updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetOne update received for Company : " + successValue.getCompanyId() + " in step " + testingStep);
							  testingStep = "delete";
							  sizeOfCompanyList = getAll().size();
							  delete();
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on company for companyId consumed")

					);
			

			}
			catch (Exception e) {
				LOGGER.warning( e.getMessage() );
				throw e;
			}
		}
		
		/** 
	 * read a Company. 
	 */
	protected Company read() throws Throwable {
		LOGGER.info( "\n======== READ ======== ");
		LOGGER.info( "-- Reading a previously created Company" );

		Company entity = null;
		StringBuilder msg = new StringBuilder( "-- Failed to read Company with primary key" );
		msg.append( companyId );
		
		CompanyFetchOneSummary fetchOneSummary = new CompanyFetchOneSummary( companyId );

		try {
			entity = CompanyBusinessDelegate.getCompanyInstance().getCompany( fetchOneSummary );

			assertNotNull( entity,msg.toString() );

			LOGGER.info( "-- Successfully found Company " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : " + e );

			throw e;
		}
		
		return entity;
	}

	/** 
	 * updating a Company.
	 */
	protected void update() throws Throwable {
		LOGGER.info( "\n======== UPDATE ======== ");
		LOGGER.info( "-- Attempting to update a Company." );

		StringBuilder msg = new StringBuilder( "Failed to update a Company : " );        
		Company entity = read();
		RefreshCompanyCommand command = generateUpdateCommand();
		command.setCompanyId(entity.getCompanyId());

		try {            
			assertNotNull( entity, msg.toString() );

			LOGGER.info( "-- Now updating the created Company." );

			// for use later on...
			companyId = entity.getCompanyId();

			CompanyBusinessDelegate proxy = CompanyBusinessDelegate.getCompanyInstance();  

			proxy.updateCompany( command ).get();

			LOGGER.info( "-- Successfully saved Company - " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : primarykey = " + companyId + " : command -" +  command + " : " + e );

			throw e;
		}
	}

	/** 
	 * delete a Company.
	 */
	protected void delete() throws Throwable {
		LOGGER.info( "\n======== DELETE ======== ");
		LOGGER.info( "-- Deleting a previously created Company." );

		Company entity = null;
		
		try{
		    entity = read(); 
			LOGGER.info( "-- Successfully read Company with id " + companyId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to read Company with id " + companyId );

			throw e;
		}

		try{
			CompanyBusinessDelegate.getCompanyInstance().delete( new CloseCompanyCommand( entity.getCompanyId() ) ).get();
			LOGGER.info( "-- Successfully deleted Company with id " + companyId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to delete Company with id " + companyId );

			throw e;
		}
	}

	/**
	 * get all Companys.
	 */
	protected List<Company> getAll() throws Throwable {    
		LOGGER.info( "======== GETALL ======== ");
		LOGGER.info( "-- Retrieving Collection of Companys:" );

		StringBuilder msg = new StringBuilder( "-- Failed to get all Company : " );        
		List<Company> collection  = new ArrayList<>();

		try {
			// call the static get method on the CompanyBusinessDelegate
			collection = CompanyBusinessDelegate.getCompanyInstance().getAllCompany();
			assertNotNull( collection, "An Empty collection of Company was incorrectly returned.");
			
			// Now print out the values
			Company entity = null;            
			Iterator<Company> iter = collection.iterator();
			int index = 1;

			while( iter.hasNext() ) {
				// Retrieve the entity   
				entity = iter.next();

				assertNotNull( entity,"-- null entity in Collection." );
				assertNotNull( entity.getCompanyId(), "-- entity in Collection has a null primary key" );        

				LOGGER.info( " - " + String.valueOf(index) + ". " + entity.toString() );
				index++;
			}
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : " + e );

			throw e;
		}
		
		return collection;			
	}

	/**
	 * Assigns a common log handler for each test class in the suite 
	 * in the event log output needs to go elsewhere
	 * 
	 * @param		handler	Handler
	 * @return		CompanyTest
	 */
	protected CompanyTest setHandler(Handler handler) {
		if ( handler != null )
			LOGGER.addHandler(handler); 
		return this;
	}

	/**
	 * Returns a new populated Company
	 * 
	 * @return CreateCompanyCommand alias
	 */
	protected CreateCompanyCommand generateNewCommand() {
        CreateCompanyCommand command = new CreateCompanyCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  0.0D,  new Address(),  new Address(),  CompanyType.values()[0],  Industry.values()[0] );
		
		return( command );
	}

		/**
		 * Returns a new populated Company
		 * 
		 * @return RefreshCompanyCommand alias
		 */
	protected RefreshCompanyCommand generateUpdateCommand() {
	        RefreshCompanyCommand command = new RefreshCompanyCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  0.0D,  new HashSet<>(),  new HashSet<>(),  new Address(),  new Address(),  CompanyType.values()[0],  Industry.values()[0] );
			
			return( command );
		}
	//-----------------------------------------------------
	// attributes 
	//-----------------------------------------------------
	protected UUID companyId = null;
	protected CompanySubscriber subscriber = null;
	private final String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
	private final Logger LOGGER = Logger.getLogger(CompanyTest.class.getName());
	private String testingStep = "";
	private Integer sizeOfCompanyList = 0;
}
