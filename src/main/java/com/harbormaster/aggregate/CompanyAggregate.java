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
 * Aggregate handler for Company as outlined for the CQRS pattern, all write responsibilities 
 * related to Company are handled here.
 * 
 * @author Dev Team
 * 
 */
@Aggregate(snapshotTriggerDefinition = "companyAggregateSnapshotTriggerDefinition")
public class CompanyAggregate {  

	// -----------------------------------------
	// Axon requires an empty constructor
    // -----------------------------------------
    public CompanyAggregate() {
    }

	// ----------------------------------------------
	// intrinsic command handlers
	// ----------------------------------------------
    @CommandHandler
    public CompanyAggregate(CreateCompanyCommand command) throws Exception {
    	LOGGER.info( "Handling command CreateCompanyCommand" );
    	CreatedCompanyEvent event = new CreatedCompanyEvent(command.getCompanyId(), command.getName(), command.getEstablishedOn(), command.getRevenue(), command.getAddress(), command.getBillingAddress(), command.getType(), command.getIndustry());
    	
        apply(event);
    }

    @CommandHandler
    public void handle(RefreshCompanyCommand command) throws Exception {
    	LOGGER.info( "handling command RefreshCompanyCommand" );
    	RefreshedCompanyEvent event = new RefreshedCompanyEvent(command.getCompanyId(), command.getName(), command.getEstablishedOn(), command.getRevenue(), command.getDivisions(), command.getBoardMembers(), command.getAddress(), command.getBillingAddress(), command.getType(), command.getIndustry());        
    	
        apply(event);
    }

    @CommandHandler
    public void handle(CloseCompanyCommand command) throws Exception {
    	LOGGER.info( "Handling command CloseCompanyCommand" );
        apply(new ClosedCompanyEvent(command.getCompanyId()));
    }

	// ----------------------------------------------
	// association command handlers
	// ----------------------------------------------

    // single association commands

    // multiple association commands
    @CommandHandler
    public void handle(AssignDivisionsToCompanyCommand command) throws Exception {
    	LOGGER.info( "Handling command AssignDivisionsToCompanyCommand" );
    	
    	if ( divisions.contains( command.getAddTo() ) )
    		throw new ProcessingException( "Divisions already contains an entity with id " + command.getAddTo().getDivisionId() );

    	apply(new AssignDivisionsToCompanyEvent(command.getCompanyId(), command.getAddTo()));
    }

    @CommandHandler
    public void handle(RemoveDivisionsFromCompanyCommand command) throws Exception {
    	LOGGER.info( "Handling command RemoveDivisionsFromCompanyCommand" );
    	
    	if ( !divisions.contains( command.getRemoveFrom() ) )
    		throw new ProcessingException( "Divisions does not contain an entity with id " + command.getRemoveFrom().getDivisionId() );

        apply(new RemoveDivisionsFromCompanyEvent(command.getCompanyId(), command.getRemoveFrom()));
    }
    @CommandHandler
    public void handle(AssignBoardMembersToCompanyCommand command) throws Exception {
    	LOGGER.info( "Handling command AssignBoardMembersToCompanyCommand" );
    	
    	if ( boardMembers.contains( command.getAddTo() ) )
    		throw new ProcessingException( "BoardMembers already contains an entity with id " + command.getAddTo().getEmployeeId() );

    	apply(new AssignBoardMembersToCompanyEvent(command.getCompanyId(), command.getAddTo()));
    }

    @CommandHandler
    public void handle(DemoteFromBoardMemberCommand command) throws Exception {
    	LOGGER.info( "Handling command DemoteFromBoardMemberCommand" );
    	
    	if ( !boardMembers.contains( command.getRemoveFrom() ) )
    		throw new ProcessingException( "BoardMembers does not contain an entity with id " + command.getRemoveFrom().getEmployeeId() );

        apply(new DemomotedFromBoardMemberCommand(command.getCompanyId(), command.getRemoveFrom()));
    }

	// ----------------------------------------------
	// intrinsic event source handlers
	// ----------------------------------------------
    @EventSourcingHandler
    void on(CreatedCompanyEvent event) {	
    	LOGGER.info( "Event sourcing CreatedCompanyEvent" );
    	this.companyId = event.getCompanyId();
        this.name = event.getName();
        this.establishedOn = event.getEstablishedOn();
        this.revenue = event.getRevenue();
        this.address = event.getAddress();
        this.billingAddress = event.getBillingAddress();
        this.type = event.getType();
        this.industry = event.getIndustry();
    }
    
    @EventSourcingHandler
    void on(RefreshedCompanyEvent event) {
    	LOGGER.info( "Event sourcing classObject.getUpdateEventAlias()}" );
        this.name = event.getName();
        this.establishedOn = event.getEstablishedOn();
        this.revenue = event.getRevenue();
        this.divisions = event.getDivisions();
        this.boardMembers = event.getBoardMembers();
        this.address = event.getAddress();
        this.billingAddress = event.getBillingAddress();
        this.type = event.getType();
        this.industry = event.getIndustry();
    }   

	// ----------------------------------------------
	// association event source handlers
	// ----------------------------------------------

	// multiple associations
    @EventSourcingHandler
    void on(AssignDivisionsToCompanyEvent event ) {
    	LOGGER.info( "Event sourcing AssignDivisionsToCompanyEvent" );
    	this.divisions.add( event.getAddTo() );
    }

	@EventSourcingHandler
	void on(RemoveDivisionsFromCompanyEvent event ) {	
		LOGGER.info( "Event sourcing RemoveDivisionsFromCompanyEvent" );
		this.divisions.remove( event.getRemoveFrom() );
	}
	
	// multiple associations
    @EventSourcingHandler
    void on(AssignBoardMembersToCompanyEvent event ) {
    	LOGGER.info( "Event sourcing AssignBoardMembersToCompanyEvent" );
    	this.boardMembers.add( event.getAddTo() );
    }

	@EventSourcingHandler
	void on(DemomotedFromBoardMemberCommand event ) {	
		LOGGER.info( "Event sourcing DemomotedFromBoardMemberCommand" );
		this.boardMembers.remove( event.getRemoveFrom() );
	}
	

    // ------------------------------------------
    // attributes
    // ------------------------------------------
	
    @AggregateIdentifier
    private UUID companyId;
    
    private String name;
    private String establishedOn;
    private Double revenue;
    private Address address;
    private Address billingAddress;
    private CompanyType type;
    private Industry industry;
    private Set<Division> divisions = new HashSet<>();
    private Set<Employee> boardMembers = new HashSet<>();

    private static final Logger LOGGER 	= Logger.getLogger(CompanyAggregate.class.getName());
}
