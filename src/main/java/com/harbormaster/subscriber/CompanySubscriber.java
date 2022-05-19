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
 * Subscriber for Company related events.  .
 * 
 * @author Dev Team
 *
 */
@Component("company-subscriber")
public class CompanySubscriber extends BaseSubscriber {

	public CompanySubscriber() {
		queryGateway = applicationContext.getBean(QueryGateway.class);
	}
	
    public SubscriptionQueryResult<List<Company>, Company> companySubscribe() {
        return queryGateway
                .subscriptionQuery(new FindAllCompanyQuery(), 
                		ResponseTypes.multipleInstancesOf(Company.class),
                		ResponseTypes.instanceOf(Company.class));
    }

    public SubscriptionQueryResult<Company, Company> companySubscribe(@DestinationVariable UUID companyId) {
        return queryGateway
                .subscriptionQuery(new FindCompanyQuery(new LoadCompanyFilter(companyId)), 
                		ResponseTypes.instanceOf(Company.class),
                		ResponseTypes.instanceOf(Company.class));
    }

	public SubscriptionQueryResult<Company, Company> findCompanyByNameSubscribe( FindByNameQuery query ) {
		return queryGateway.subscriptionQuery(query, 
			ResponseTypes.instanceOf(Company.class), 
			ResponseTypes.instanceOf(Company.class) );
    }
	
	public SubscriptionQueryResult<List<Company>, Company> findCompaniesByIndustrySubscribe( FindCompaniesByIndustryQuery query ) {
	    return queryGateway.subscriptionQuery(query, 
			ResponseTypes.multipleInstancesOf(Company.class), 
			ResponseTypes.instanceOf(Company.class) );
    }
	
	public SubscriptionQueryResult<List<Company>, Company> findCompaniesByTypeSubscribe( FindByTypeQuery query ) {
	    return queryGateway.subscriptionQuery(query, 
			ResponseTypes.multipleInstancesOf(Company.class), 
			ResponseTypes.instanceOf(Company.class) );
    }
	



    // -------------------------------------------------
    // attributes
    // -------------------------------------------------
	@Autowired
    private final QueryGateway queryGateway;
}

