/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.api;

import java.time.Instant
import java.util.*

import javax.persistence.*

import com.harbormaster.entity.*;


//-----------------------------------------------------------
// Event definitions
//-----------------------------------------------------------

// Company Events

data class CreatedCompanyEvent(
    @Id var companyId: UUID? = null,
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

data class RefreshedCompanyEvent(
    @Id var companyId: UUID? = null,
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

data class ClosedCompanyEvent(@Id val companyId: UUID? = null)

// single association events

// multiple association events
data class AssignDivisionsToCompanyEvent(@Id val companyId: UUID, val addTo: Division )
data class RemoveDivisionsFromCompanyEvent(@Id val companyId: UUID, val removeFrom: Division )
data class AssignBoardMembersToCompanyEvent(@Id val companyId: UUID, val addTo: Employee )
data class DemomotedFromBoardMemberCommand(@Id val companyId: UUID, val removeFrom: Employee )


// Division Events

data class CreateDivisionEvent(
    @Id var divisionId: UUID? = null,
    var name: String? = null
)

data class UpdateDivisionEvent(
    @Id var divisionId: UUID? = null,
    var name: String? = null,
    var head: Employee? = null,
    var departments:  Set<Department>? = null
)

data class DeleteDivisionEvent(@Id val divisionId: UUID? = null)

// single association events
data class PromotedToDivisionHeadEvent(@Id val divisionId: UUID, val assignment: Employee )
data class DemomotedFromDivisionHeadCommand(@Id val divisionId: UUID? = null )

// multiple association events
data class AssignDepartmentsToDivisionEvent(@Id val divisionId: UUID, val addTo: Department )
data class RemoveDepartmentsFromDivisionEvent(@Id val divisionId: UUID, val removeFrom: Department )


// Department Events

data class CreateDepartmentEvent(
    @Id var departmentId: UUID? = null,
    var name: String? = null
)

data class UpdateDepartmentEvent(
    @Id var departmentId: UUID? = null,
    var name: String? = null,
    var head: Employee? = null,
    var employees:  Set<Employee>? = null
)

data class DeleteDepartmentEvent(@Id val departmentId: UUID? = null)

// single association events
data class AssignHeadToDepartmentEvent(@Id val departmentId: UUID, val assignment: Employee )
data class UnAssignHeadFromDepartmentEvent(@Id val departmentId: UUID? = null )

// multiple association events
data class AssignEmployeesToDepartmentEvent(@Id val departmentId: UUID, val addTo: Employee )
data class RemoveEmployeesFromDepartmentEvent(@Id val departmentId: UUID, val removeFrom: Employee )


// Employee Events

data class CreateEmployeeEvent(
    @Id var employeeId: UUID? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    @Enumerated(EnumType.STRING) var type: EmploymentType? = null
)

data class UpdateEmployeeEvent(
    @Id var employeeId: UUID? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    @Enumerated(EnumType.STRING) var type: EmploymentType? = null
)

data class DeleteEmployeeEvent(@Id val employeeId: UUID? = null)

// single association events

// multiple association events



