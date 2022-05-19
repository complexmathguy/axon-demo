/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.api

import org.axonframework.modelling.command.TargetAggregateIdentifier


import java.util.*
import javax.persistence.*

import com.harbormaster.entity.*;

//-----------------------------------------------------------
// Command definitions
//-----------------------------------------------------------

// Company Commands
data class CreateCompanyCommand(
    @TargetAggregateIdentifier var companyId: UUID? = null,
    var name: String? = null,
    var establishedOn: String? = null,
    var revenue: Double? = null,
        @AttributeOverrides(
      AttributeOverride( name = "street", column = Column(name = "address_street")),
      AttributeOverride( name = "city", column = Column(name = "address_city")),
      AttributeOverride( name = "state", column = Column(name = "address_state")),
      AttributeOverride( name = "zipCode", column = Column(name = "address_zipCode"))
    )
    var address: Address? = null,
        @AttributeOverrides(
      AttributeOverride( name = "street", column = Column(name = "billingAddress_street")),
      AttributeOverride( name = "city", column = Column(name = "billingAddress_city")),
      AttributeOverride( name = "state", column = Column(name = "billingAddress_state")),
      AttributeOverride( name = "zipCode", column = Column(name = "billingAddress_zipCode"))
    )
    var billingAddress: Address? = null,
    @Enumerated(EnumType.STRING) var type: CompanyType? = null,
    @Enumerated(EnumType.STRING) var industry: Industry? = null
)

data class RefreshCompanyCommand(
    @TargetAggregateIdentifier var companyId: UUID? = null,
    var name: String? = null,
    var establishedOn: String? = null,
    var revenue: Double? = null,
    var divisions:  Set<Division>? = null,
    var boardMembers:  Set<Employee>? = null,
        @AttributeOverrides(
      AttributeOverride( name = "street", column = Column(name = "address_street")),
      AttributeOverride( name = "city", column = Column(name = "address_city")),
      AttributeOverride( name = "state", column = Column(name = "address_state")),
      AttributeOverride( name = "zipCode", column = Column(name = "address_zipCode"))
    )
    var address: Address? = null,
        @AttributeOverrides(
      AttributeOverride( name = "street", column = Column(name = "billingAddress_street")),
      AttributeOverride( name = "city", column = Column(name = "billingAddress_city")),
      AttributeOverride( name = "state", column = Column(name = "billingAddress_state")),
      AttributeOverride( name = "zipCode", column = Column(name = "billingAddress_zipCode"))
    )
    var billingAddress: Address? = null,
    @Enumerated(EnumType.STRING) var type: CompanyType? = null,
    @Enumerated(EnumType.STRING) var industry: Industry? = null
)

data class CloseCompanyCommand(@TargetAggregateIdentifier  var companyId: UUID? = null)

// single association commands

// multiple association commands
data class AssignDivisionsToCompanyCommand(@TargetAggregateIdentifier  val companyId: UUID, val addTo: Division )
data class RemoveDivisionsFromCompanyCommand(@TargetAggregateIdentifier  val companyId: UUID, val removeFrom: Division )
data class AssignBoardMembersToCompanyCommand(@TargetAggregateIdentifier  val companyId: UUID, val addTo: Employee )
data class DemoteFromBoardMemberCommand(@TargetAggregateIdentifier  val companyId: UUID, val removeFrom: Employee )


// Division Commands
data class CreateDivisionCommand(
    @TargetAggregateIdentifier var divisionId: UUID? = null,
    var name: String? = null
)

data class UpdateDivisionCommand(
    @TargetAggregateIdentifier var divisionId: UUID? = null,
    var name: String? = null,
    var head: Employee? = null,
    var departments:  Set<Department>? = null
)

data class DeleteDivisionCommand(@TargetAggregateIdentifier  var divisionId: UUID? = null)

// single association commands
data class PromoteToDivisionHeadCommand(@TargetAggregateIdentifier  val divisionId: UUID, val assignment: Employee )
data class DemoteFromDivisionHeadCommand(@TargetAggregateIdentifier  val divisionId: UUID )

// multiple association commands
data class AssignDepartmentsToDivisionCommand(@TargetAggregateIdentifier  val divisionId: UUID, val addTo: Department )
data class RemoveDepartmentsFromDivisionCommand(@TargetAggregateIdentifier  val divisionId: UUID, val removeFrom: Department )


// Department Commands
data class CreateDepartmentCommand(
    @TargetAggregateIdentifier var departmentId: UUID? = null,
    var name: String? = null
)

data class UpdateDepartmentCommand(
    @TargetAggregateIdentifier var departmentId: UUID? = null,
    var name: String? = null,
    var head: Employee? = null,
    var employees:  Set<Employee>? = null
)

data class DeleteDepartmentCommand(@TargetAggregateIdentifier  var departmentId: UUID? = null)

// single association commands
data class AssignHeadToDepartmentCommand(@TargetAggregateIdentifier  val departmentId: UUID, val assignment: Employee )
data class UnAssignHeadFromDepartmentCommand(@TargetAggregateIdentifier  val departmentId: UUID )

// multiple association commands
data class AssignEmployeesToDepartmentCommand(@TargetAggregateIdentifier  val departmentId: UUID, val addTo: Employee )
data class RemoveEmployeesFromDepartmentCommand(@TargetAggregateIdentifier  val departmentId: UUID, val removeFrom: Employee )


// Employee Commands
data class CreateEmployeeCommand(
    @TargetAggregateIdentifier var employeeId: UUID? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    @Enumerated(EnumType.STRING) var type: EmploymentType? = null
)

data class UpdateEmployeeCommand(
    @TargetAggregateIdentifier var employeeId: UUID? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    @Enumerated(EnumType.STRING) var type: EmploymentType? = null
)

data class DeleteEmployeeCommand(@TargetAggregateIdentifier  var employeeId: UUID? = null)

// single association commands

// multiple association commands



