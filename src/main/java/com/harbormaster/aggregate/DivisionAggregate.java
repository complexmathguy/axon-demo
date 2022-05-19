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
 * Aggregate handler for Division as outlined for the CQRS pattern, all write responsibilities 
 * related to Division are handled here.
 * 
 * @author Dev Team
 * 
 */
@Aggregate(snapshotTriggerDefinition = "divisionAggregateSnapshotTriggerDefinition")
public class DivisionAggregate {  

	// -----------------------------------------
	// Axon requires an empty constructor
    // -----------------------------------------
    public DivisionAggregate() {
    }

	// ----------------------------------------------
	// intrinsic command handlers
	// ----------------------------------------------
    @CommandHandler
    public DivisionAggregate(CreateDivisionCommand command) throws Exception {
    	LOGGER.info( "Handling command CreateDivisionCommand" );
    	CreateDivisionEvent event = new CreateDivisionEvent(command.getDivisionId(), command.getName());
    	
        apply(event);
    }

    @CommandHandler
    public void handle(UpdateDivisionCommand command) throws Exception {
    	LOGGER.info( "handling command UpdateDivisionCommand" );
    	UpdateDivisionEvent event = new UpdateDivisionEvent(command.getDivisionId(), command.getName(), command.getHead(), command.getDepartments());        
    	
        apply(event);
    }

    @CommandHandler
    public void handle(DeleteDivisionCommand command) throws Exception {
    	LOGGER.info( "Handling command DeleteDivisionCommand" );
        apply(new DeleteDivisionEvent(command.getDivisionId()));
    }

	// ----------------------------------------------
	// association command handlers
	// ----------------------------------------------

    // single association commands
    @CommandHandler
    public void handle(PromoteToDivisionHeadCommand command) throws Exception {
    	LOGGER.info( "Handling command PromoteToDivisionHeadCommand" );
    	
    	if (  head != null && head.getEmployeeId() == command.getAssignment().getEmployeeId() )
    		throw new ProcessingException( "Head already assigned with id " + command.getAssignment().getEmployeeId() );  
    		
        apply(new PromotedToDivisionHeadEvent(command.getDivisionId(), command.getAssignment()));
    }

    @CommandHandler
    public void handle(DemoteFromDivisionHeadCommand command) throws Exception {
    	LOGGER.info( "Handlign command DemoteFromDivisionHeadCommand" );

    	if (  head == null )
    		throw new ProcessingException( "Head already has nothing assigned." );  

    	apply(new DemomotedFromDivisionHeadCommand(command.getDivisionId()));
    }

    // multiple association commands
    @CommandHandler
    public void handle(AssignDepartmentsToDivisionCommand command) throws Exception {
    	LOGGER.info( "Handling command AssignDepartmentsToDivisionCommand" );
    	
    	if ( departments.contains( command.getAddTo() ) )
    		throw new ProcessingException( "Departments already contains an entity with id " + command.getAddTo().getDepartmentId() );

    	apply(new AssignDepartmentsToDivisionEvent(command.getDivisionId(), command.getAddTo()));
    }

    @CommandHandler
    public void handle(RemoveDepartmentsFromDivisionCommand command) throws Exception {
    	LOGGER.info( "Handling command RemoveDepartmentsFromDivisionCommand" );
    	
    	if ( !departments.contains( command.getRemoveFrom() ) )
    		throw new ProcessingException( "Departments does not contain an entity with id " + command.getRemoveFrom().getDepartmentId() );

        apply(new RemoveDepartmentsFromDivisionEvent(command.getDivisionId(), command.getRemoveFrom()));
    }

	// ----------------------------------------------
	// intrinsic event source handlers
	// ----------------------------------------------
    @EventSourcingHandler
    void on(CreateDivisionEvent event) {	
    	LOGGER.info( "Event sourcing CreateDivisionEvent" );
    	this.divisionId = event.getDivisionId();
        this.name = event.getName();
    }
    
    @EventSourcingHandler
    void on(UpdateDivisionEvent event) {
    	LOGGER.info( "Event sourcing classObject.getUpdateEventAlias()}" );
        this.name = event.getName();
        this.head = event.getHead();
        this.departments = event.getDepartments();
    }   

	// ----------------------------------------------
	// association event source handlers
	// ----------------------------------------------
	// single associations
    @EventSourcingHandler
    void on(PromotedToDivisionHeadEvent event ) {	
    	LOGGER.info( "Event sourcing PromotedToDivisionHeadEvent" );
    	this.head = event.getAssignment();
    }

	@EventSourcingHandler
	void on(DemomotedFromDivisionHeadCommand event ) {	
		LOGGER.info( "Event sourcing DemomotedFromDivisionHeadCommand" );
		this.head = null;
	}

	// multiple associations
    @EventSourcingHandler
    void on(AssignDepartmentsToDivisionEvent event ) {
    	LOGGER.info( "Event sourcing AssignDepartmentsToDivisionEvent" );
    	this.departments.add( event.getAddTo() );
    }

	@EventSourcingHandler
	void on(RemoveDepartmentsFromDivisionEvent event ) {	
		LOGGER.info( "Event sourcing RemoveDepartmentsFromDivisionEvent" );
		this.departments.remove( event.getRemoveFrom() );
	}
	

    // ------------------------------------------
    // attributes
    // ------------------------------------------
	
    @AggregateIdentifier
    private UUID divisionId;
    
    private String name;
    private Employee head = null;
    private Set<Department> departments = new HashSet<>();

    private static final Logger LOGGER 	= Logger.getLogger(DivisionAggregate.class.getName());
}
