/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.config;

import org.axonframework.eventsourcing.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SnapshotConfig {

	// --------------------------------------------------------
	// define a snapshot trigger for each aggregate,
	// as implicitly defined per class and explicitly defined in the model
	// --------------------------------------------------------
	@Bean
    public SnapshotTriggerDefinition companyAggregateSnapshotTriggerDefinition(
        Snapshotter snapshotter,
        @Value("${axon.aggregate.company.snapshot-threshold:50}") int threshold) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
    }

	@Bean
    public SnapshotTriggerDefinition divisionAggregateSnapshotTriggerDefinition(
        Snapshotter snapshotter,
        @Value("${axon.aggregate.division.snapshot-threshold:50}") int threshold) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
    }

	@Bean
    public SnapshotTriggerDefinition departmentAggregateSnapshotTriggerDefinition(
        Snapshotter snapshotter,
        @Value("${axon.aggregate.department.snapshot-threshold:50}") int threshold) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
    }

	@Bean
    public SnapshotTriggerDefinition employeeAggregateSnapshotTriggerDefinition(
        Snapshotter snapshotter,
        @Value("${axon.aggregate.employee.snapshot-threshold:50}") int threshold) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
    }

	
}