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
 * Aggregate handler for Department as outlined for the CQRS pattern, all write responsibilities 
 * related to Department are handled here.
 * 
 * @author Dev Team
 * 
 */
@Aggregate(snapshotTriggerDefinition = "departmentAggregateSnapshotTriggerDefinition")
public class DepartmentAggregate {  

	// -----------------------------------------
	// Axon requires an empty constructor
    // -----------------------------------------
    public DepartmentAggregate() {
    }

	// ----------------------------------------------
	// intrinsic command handlers
	// ----------------------------------------------
    @CommandHandler
    public DepartmentAggregate(CreateDepartmentCommand command) throws Exception {
    	LOGGER.info( "Handling command CreateDepartmentCommand" );
    	CreateDepartmentEvent event = new CreateDepartmentEvent(command.getDepartmentId(), command.getName());
    	
        apply(event);
    }

    @CommandHandler
    public void handle(UpdateDepartmentCommand command) throws Exception {
    	LOGGER.info( "handling command UpdateDepartmentCommand" );
    	UpdateDepartmentEvent event = new UpdateDepartmentEvent(command.getDepartmentId(), command.getName(), command.getHead(), command.getEmployees());        
    	
        apply(event);
    }

    @CommandHandler
    public void handle(DeleteDepartmentCommand command) throws Exception {
    	LOGGER.info( "Handling command DeleteDepartmentCommand" );
        apply(new DeleteDepartmentEvent(command.getDepartmentId()));
    }

	// ----------------------------------------------
	// association command handlers
	// ----------------------------------------------

    // single association commands
    @CommandHandler
    public void handle(AssignHeadToDepartmentCommand command) throws Exception {
    	LOGGER.info( "Handling command AssignHeadToDepartmentCommand" );
    	
    	if (  head != null && head.getEmployeeId() == command.getAssignment().getEmployeeId() )
    		throw new ProcessingException( "Head already assigned with id " + command.getAssignment().getEmployeeId() );  
    		
        apply(new AssignHeadToDepartmentEvent(command.getDepartmentId(), command.getAssignment()));
    }

    @CommandHandler
    public void handle(UnAssignHeadFromDepartmentCommand command) throws Exception {
    	LOGGER.info( "Handlign command UnAssignHeadFromDepartmentCommand" );

    	if (  head == null )
    		throw new ProcessingException( "Head already has nothing assigned." );  

    	apply(new UnAssignHeadFromDepartmentEvent(command.getDepartmentId()));
    }

    // multiple association commands
    @CommandHandler
    public void handle(AssignEmployeesToDepartmentCommand command) throws Exception {
    	LOGGER.info( "Handling command AssignEmployeesToDepartmentCommand" );
    	
    	if ( employees.contains( command.getAddTo() ) )
    		throw new ProcessingException( "Employees already contains an entity with id " + command.getAddTo().getEmployeeId() );

    	apply(new AssignEmployeesToDepartmentEvent(command.getDepartmentId(), command.getAddTo()));
    }

    @CommandHandler
    public void handle(RemoveEmployeesFromDepartmentCommand command) throws Exception {
    	LOGGER.info( "Handling command RemoveEmployeesFromDepartmentCommand" );
    	
    	if ( !employees.contains( command.getRemoveFrom() ) )
    		throw new ProcessingException( "Employees does not contain an entity with id " + command.getRemoveFrom().getEmployeeId() );

        apply(new RemoveEmployeesFromDepartmentEvent(command.getDepartmentId(), command.getRemoveFrom()));
    }

	// ----------------------------------------------
	// intrinsic event source handlers
	// ----------------------------------------------
    @EventSourcingHandler
    void on(CreateDepartmentEvent event) {	
    	LOGGER.info( "Event sourcing CreateDepartmentEvent" );
    	this.departmentId = event.getDepartmentId();
        this.name = event.getName();
    }
    
    @EventSourcingHandler
    void on(UpdateDepartmentEvent event) {
    	LOGGER.info( "Event sourcing classObject.getUpdateEventAlias()}" );
        this.name = event.getName();
        this.head = event.getHead();
        this.employees = event.getEmployees();
    }   

	// ----------------------------------------------
	// association event source handlers
	// ----------------------------------------------
	// single associations
    @EventSourcingHandler
    void on(AssignHeadToDepartmentEvent event ) {	
    	LOGGER.info( "Event sourcing AssignHeadToDepartmentEvent" );
    	this.head = event.getAssignment();
    }

	@EventSourcingHandler
	void on(UnAssignHeadFromDepartmentEvent event ) {	
		LOGGER.info( "Event sourcing UnAssignHeadFromDepartmentEvent" );
		this.head = null;
	}

	// multiple associations
    @EventSourcingHandler
    void on(AssignEmployeesToDepartmentEvent event ) {
    	LOGGER.info( "Event sourcing AssignEmployeesToDepartmentEvent" );
    	this.employees.add( event.getAddTo() );
    }

	@EventSourcingHandler
	void on(RemoveEmployeesFromDepartmentEvent event ) {	
		LOGGER.info( "Event sourcing RemoveEmployeesFromDepartmentEvent" );
		this.employees.remove( event.getRemoveFrom() );
	}
	

    // ------------------------------------------
    // attributes
    // ------------------------------------------
	
    @AggregateIdentifier
    private UUID departmentId;
    
    private String name;
    private Employee head = null;
    private Set<Employee> employees = new HashSet<>();

    private static final Logger LOGGER 	= Logger.getLogger(DepartmentAggregate.class.getName());
}
