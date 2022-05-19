package com.harbormaster.aggregate;

import com.harbormaster.api.*;
import com.harbormaster.entity.*;
import com.harbormaster.exception.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.context.annotation.Profile;

/**
 * Aggregate handler for Employee as outlined for the CQRS pattern, all write responsibilities 
 * related to Employee are handled here.
 * 
 * @author Dev Team
 * 
 */
@Aggregate(snapshotTriggerDefinition = "employeeAggregateSnapshotTriggerDefinition")
public class EmployeeAggregate {  

	// -----------------------------------------
	// Axon requires an empty constructor
    // -----------------------------------------
    public EmployeeAggregate() {
    }

	// ----------------------------------------------
	// intrinsic command handlers
	// ----------------------------------------------
    @CommandHandler
    public EmployeeAggregate(CreateEmployeeCommand command) throws Exception {
    	LOGGER.info( "Handling command CreateEmployeeCommand" );
    	CreateEmployeeEvent event = new CreateEmployeeEvent(command.getEmployeeId(), command.getFirstName(), command.getLastName(), command.getType());
    	
        apply(event);
    }

    @CommandHandler
    public void handle(UpdateEmployeeCommand command) throws Exception {
    	LOGGER.info( "handling command UpdateEmployeeCommand" );
    	UpdateEmployeeEvent event = new UpdateEmployeeEvent(command.getEmployeeId(), command.getFirstName(), command.getLastName(), command.getType());        
    	
        apply(event);
    }

    @CommandHandler
    public void handle(DeleteEmployeeCommand command) throws Exception {
    	LOGGER.info( "Handling command DeleteEmployeeCommand" );
        apply(new DeleteEmployeeEvent(command.getEmployeeId()));
    }

	// ----------------------------------------------
	// association command handlers
	// ----------------------------------------------

    // single association commands

    // multiple association commands

	// ----------------------------------------------
	// intrinsic event source handlers
	// ----------------------------------------------
    @EventSourcingHandler
    void on(CreateEmployeeEvent event) {	
    	LOGGER.info( "Event sourcing CreateEmployeeEvent" );
    	this.employeeId = event.getEmployeeId();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.type = event.getType();
    }
    
    @EventSourcingHandler
    void on(UpdateEmployeeEvent event) {
    	LOGGER.info( "Event sourcing classObject.getUpdateEventAlias()}" );
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.type = event.getType();
    }   

	// ----------------------------------------------
	// association event source handlers
	// ----------------------------------------------


    // ------------------------------------------
    // attributes
    // ------------------------------------------
	
    @AggregateIdentifier
    private UUID employeeId;
    
    private String firstName;
    private String lastName;
    private EmploymentType type;

    private static final Logger LOGGER 	= Logger.getLogger(EmployeeAggregate.class.getName());
}
