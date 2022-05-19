/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.controller;

import java.io.*;
import java.util.logging.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** 
 * Test class for all Rest Controller delegation functions
 *
 * @author Dev Team
 */
@RestController
public class TestSpringRestController
{
	@RequestMapping("/")
    ModelAndView home() 
	{
        return new ModelAndView("startTest", "message", "sample message" );
    }

	/**
     * Handles calling the underlying JUnit testing facility
     */
	@RequestMapping("/test")
    public String test()
    {
		ByteArrayOutputStream os 	= new java.io.ByteArrayOutputStream();
		StreamHandler handler 		= new StreamHandler( os, new LogTestFormatter() );
		
		com.harbormaster.test.BaseTest.runTheTest(handler);
		
		return( os.toString() );
    }
	
	private StreamHandler handler = null;
	
	class LogTestFormatter extends Formatter 
	{
	    public String format(LogRecord record) 
	    {
	        StringBuilder builder = new StringBuilder(1000);
	        
	        if ( record.getLevel() == Level.WARNING )
	        	builder.append( "<span><style='color:red'>" );
	        else if ( record.getLevel() == Level.SEVERE )
	        	builder.append( "<span><style='color:red;font-weight:bolder'>" );
	        else
	        	builder.append( "<span><style='color:black'>" );
	        
	        builder.append(formatMessage(record));

	        builder.append( "</style></span>" );

	        builder.append("<br>");
	        return builder.toString();
	    }
	}
}



