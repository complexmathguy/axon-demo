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
 * Subscriber for Employee related events.  .
 * 
 * @author Dev Team
 *
 */
@Component("employee-subscriber")
public class EmployeeSubscriber extends BaseSubscriber {

	public EmployeeSubscriber() {
		queryGateway = applicationContext.getBean(QueryGateway.class);
	}
	
    public SubscriptionQueryResult<List<Employee>, Employee> employeeSubscribe() {
        return queryGateway
                .subscriptionQuery(new FindAllEmployeeQuery(), 
                		ResponseTypes.multipleInstancesOf(Employee.class),
                		ResponseTypes.instanceOf(Employee.class));
    }

    public SubscriptionQueryResult<Employee, Employee> employeeSubscribe(@DestinationVariable UUID employeeId) {
        return queryGateway
                .subscriptionQuery(new FindEmployeeQuery(new LoadEmployeeFilter(employeeId)), 
                		ResponseTypes.instanceOf(Employee.class),
                		ResponseTypes.instanceOf(Employee.class));
    }




    // -------------------------------------------------
    // attributes
    // -------------------------------------------------
	@Autowired
    private final QueryGateway queryGateway;
}

