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
 * Test Division class.
 *
 * @author Dev Team
 */
public class DivisionTest{

    // ------------------------------------
	// default constructor
    // ------------------------------------
	public DivisionTest() {
		subscriber = new DivisionSubscriber();
	}

	// test methods
	@Test
	/*
	 * Initiate DivisionTest.
	 */
	public void startTest() throws Throwable {
		try {
			LOGGER.info("**********************************************************");
			LOGGER.info("Beginning test on Division...");
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
	 * jumpstart the process by instantiating2 Division
	 */
	protected void jumpStart() throws Throwable {
		LOGGER.info( "\n======== create instances to get the ball rolling  ======== ");

		divisionId = DivisionBusinessDelegate.getDivisionInstance()
		.createDivision( generateNewCommand() )
		.get();

		// ---------------------------------------------
		// set up query subscriptions after the 1st create
		// ---------------------------------------------
		testingStep = "create";
		setUpQuerySubscriptions();

		DivisionBusinessDelegate.getDivisionInstance()
				.createDivision( generateNewCommand() )
				.get();

	}

	/** 
	 * Set up query subscriptions
	 */
	protected void setUpQuerySubscriptions() throws Throwable {
		LOGGER.info( "\n======== Setting Up Query Subscriptions ======== ");
			
		try {            
			subscriber.divisionSubscribe().updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetAll update received for Division : " + successValue.getDivisionId());
							  if (successValue.getDivisionId().equals(divisionId)) {
								  if (testingStep.equals("create")) {
									  testingStep = "update";
									  update();
								  } else if (testingStep.equals("delete")) {
									  testingStep = "complete";
									  state( getAll().size() == sizeOfDivisionList - 1 , "value not deleted from list");
									  LOGGER.info("**********************************************************");
									  LOGGER.info("Division test completed successfully...");
									  LOGGER.info("**********************************************************\n");
								  }
							  }
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on division consumed")
					);
			subscriber.divisionSubscribe( divisionId ).updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetOne update received for Division : " + successValue.getDivisionId() + " in step " + testingStep);
							  testingStep = "delete";
							  sizeOfDivisionList = getAll().size();
							  delete();
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on division for divisionId consumed")

					);
			

			}
			catch (Exception e) {
				LOGGER.warning( e.getMessage() );
				throw e;
			}
		}
		
		/** 
	 * read a Division. 
	 */
	protected Division read() throws Throwable {
		LOGGER.info( "\n======== READ ======== ");
		LOGGER.info( "-- Reading a previously created Division" );

		Division entity = null;
		StringBuilder msg = new StringBuilder( "-- Failed to read Division with primary key" );
		msg.append( divisionId );
		
		DivisionFetchOneSummary fetchOneSummary = new DivisionFetchOneSummary( divisionId );

		try {
			entity = DivisionBusinessDelegate.getDivisionInstance().getDivision( fetchOneSummary );

			assertNotNull( entity,msg.toString() );

			LOGGER.info( "-- Successfully found Division " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : " + e );

			throw e;
		}
		
		return entity;
	}

	/** 
	 * updating a Division.
	 */
	protected void update() throws Throwable {
		LOGGER.info( "\n======== UPDATE ======== ");
		LOGGER.info( "-- Attempting to update a Division." );

		StringBuilder msg = new StringBuilder( "Failed to update a Division : " );        
		Division entity = read();
		UpdateDivisionCommand command = generateUpdateCommand();
		command.setDivisionId(entity.getDivisionId());

		try {            
			assertNotNull( entity, msg.toString() );

			LOGGER.info( "-- Now updating the created Division." );

			// for use later on...
			divisionId = entity.getDivisionId();

			DivisionBusinessDelegate proxy = DivisionBusinessDelegate.getDivisionInstance();  

			proxy.updateDivision( command ).get();

			LOGGER.info( "-- Successfully saved Division - " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : primarykey = " + divisionId + " : command -" +  command + " : " + e );

			throw e;
		}
	}

	/** 
	 * delete a Division.
	 */
	protected void delete() throws Throwable {
		LOGGER.info( "\n======== DELETE ======== ");
		LOGGER.info( "-- Deleting a previously created Division." );

		Division entity = null;
		
		try{
		    entity = read(); 
			LOGGER.info( "-- Successfully read Division with id " + divisionId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to read Division with id " + divisionId );

			throw e;
		}

		try{
			DivisionBusinessDelegate.getDivisionInstance().delete( new DeleteDivisionCommand( entity.getDivisionId() ) ).get();
			LOGGER.info( "-- Successfully deleted Division with id " + divisionId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to delete Division with id " + divisionId );

			throw e;
		}
	}

	/**
	 * get all Divisions.
	 */
	protected List<Division> getAll() throws Throwable {    
		LOGGER.info( "======== GETALL ======== ");
		LOGGER.info( "-- Retrieving Collection of Divisions:" );

		StringBuilder msg = new StringBuilder( "-- Failed to get all Division : " );        
		List<Division> collection  = new ArrayList<>();

		try {
			// call the static get method on the DivisionBusinessDelegate
			collection = DivisionBusinessDelegate.getDivisionInstance().getAllDivision();
			assertNotNull( collection, "An Empty collection of Division was incorrectly returned.");
			
			// Now print out the values
			Division entity = null;            
			Iterator<Division> iter = collection.iterator();
			int index = 1;

			while( iter.hasNext() ) {
				// Retrieve the entity   
				entity = iter.next();

				assertNotNull( entity,"-- null entity in Collection." );
				assertNotNull( entity.getDivisionId(), "-- entity in Collection has a null primary key" );        

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
	 * @return		DivisionTest
	 */
	protected DivisionTest setHandler(Handler handler) {
		if ( handler != null )
			LOGGER.addHandler(handler); 
		return this;
	}

	/**
	 * Returns a new populated Division
	 * 
	 * @return CreateDivisionCommand alias
	 */
	protected CreateDivisionCommand generateNewCommand() {
        CreateDivisionCommand command = new CreateDivisionCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16) );
		
		return( command );
	}

		/**
		 * Returns a new populated Division
		 * 
		 * @return UpdateDivisionCommand alias
		 */
	protected UpdateDivisionCommand generateUpdateCommand() {
	        UpdateDivisionCommand command = new UpdateDivisionCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  null,  new HashSet<>() );
			
			return( command );
		}
	//-----------------------------------------------------
	// attributes 
	//-----------------------------------------------------
	protected UUID divisionId = null;
	protected DivisionSubscriber subscriber = null;
	private final String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
	private final Logger LOGGER = Logger.getLogger(DivisionTest.class.getName());
	private String testingStep = "";
	private Integer sizeOfDivisionList = 0;
}
