/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.subscriber;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.axonframework.messaging.responsetypes.ResponseTypes;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Component;


import com.harbormaster.api.*;
import com.harbormaster.entity.*;
import com.harbormaster.exception.*;

/**
 * Subscriber for Division related events.  .
 * 
 * @author Dev Team
 *
 */
@Component("division-subscriber")
public class DivisionSubscriber extends BaseSubscriber {

	public DivisionSubscriber() {
		queryGateway = applicationContext.getBean(QueryGateway.class);
	}
	
    public SubscriptionQueryResult<List<Division>, Division> divisionSubscribe() {
        return queryGateway
                .subscriptionQuery(new FindAllDivisionQuery(), 
                		ResponseTypes.multipleInstancesOf(Division.class),
                		ResponseTypes.instanceOf(Division.class));
    }

    public SubscriptionQueryResult<Division, Division> divisionSubscribe(@DestinationVariable UUID divisionId) {
        return queryGateway
                .subscriptionQuery(new FindDivisionQuery(new LoadDivisionFilter(divisionId)), 
                		ResponseTypes.instanceOf(Division.class),
                		ResponseTypes.instanceOf(Division.class));
    }




    // -------------------------------------------------
    // attributes
    // -------------------------------------------------
	@Autowired
    private final QueryGateway queryGateway;
}

