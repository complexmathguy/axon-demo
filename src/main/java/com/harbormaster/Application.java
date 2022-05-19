/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;	
@SpringBootApplication
@ComponentScan(basePackages = "com.harbormaster.*")
@EnableJpaRepositories(basePackages = "com.harbormaster.repository")
public class Application {

    public static void main(String[] args) {
    	ApplicationContext context = SpringApplication.run(Application.class, args);
    	
		System.out.println( "=================================" );
		System.out.println( "Checking in ApplicationContext for discovered handler components:\n" );
        System.out.println( "- Contains company-handler = " + context.containsBeanDefinition("company-handler"));
        System.out.println( "- Contains division-handler = " + context.containsBeanDefinition("division-handler"));
        System.out.println( "- Contains department-handler = " + context.containsBeanDefinition("department-handler"));
        System.out.println( "- Contains employee-handler = " + context.containsBeanDefinition("employee-handler"));
        System.out.println( "=================================" );
    }
    
}