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
 * Subscriber for Department related events.  .
 * 
 * @author Dev Team
 *
 */
@Component("department-subscriber")
public class DepartmentSubscriber extends BaseSubscriber {

	public DepartmentSubscriber() {
		queryGateway = applicationContext.getBean(QueryGateway.class);
	}
	
    public SubscriptionQueryResult<List<Department>, Department> departmentSubscribe() {
        return queryGateway
                .subscriptionQuery(new FindAllDepartmentQuery(), 
                		ResponseTypes.multipleInstancesOf(Department.class),
                		ResponseTypes.instanceOf(Department.class));
    }

    public SubscriptionQueryResult<Department, Department> departmentSubscribe(@DestinationVariable UUID departmentId) {
        return queryGateway
                .subscriptionQuery(new FindDepartmentQuery(new LoadDepartmentFilter(departmentId)), 
                		ResponseTypes.instanceOf(Department.class),
                		ResponseTypes.instanceOf(Department.class));
    }




    // -------------------------------------------------
    // attributes
    // -------------------------------------------------
	@Autowired
    private final QueryGateway queryGateway;
}

