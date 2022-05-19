/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.entity;

import java.util.*

import javax.persistence.*
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery

import com.harbormaster.api.*;

// --------------------------------------------
// Entity Definitions
// --------------------------------------------
@Entity
@NamedQueries(
        NamedQuery(
                name = "Company.findCompanyByName",
            query = "SELECT c FROM Company c WHERE c.name = (:name) ORDER BY c.name"
        ),
        NamedQuery(
                name = "Company.findCompaniesByIndustry",
            query = "SELECT c FROM Company c WHERE c.industry = (:industry) ORDER BY c.industry"
        ),
        NamedQuery(
                name = "Company.findCompaniesByType",
            query = "SELECT c FROM Company c WHERE c.type = (:type) ORDER BY c.type"
        )
)
data class Company(
    @Id var companyId: UUID? = null,
    var name: String? = null,
    var establishedOn: String? = null,
    var revenue: Double? = null,
    @ElementCollection (fetch = FetchType.EAGER)var divisions:  Set<Division>? = null,
    @ElementCollection (fetch = FetchType.EAGER)var boardMembers:  Set<Employee>? = null,
    @Embedded     @AttributeOverrides(
      AttributeOverride( name = "street", column = Column(name = "address_street")),
      AttributeOverride( name = "city", column = Column(name = "address_city")),
      AttributeOverride( name = "state", column = Column(name = "address_state")),
      AttributeOverride( name = "zipCode", column = Column(name = "address_zipCode"))
    )
    var address: Address? = null,
    @Embedded     @AttributeOverrides(
      AttributeOverride( name = "street", column = Column(name = "billingAddress_street")),
      AttributeOverride( name = "city", column = Column(name = "billingAddress_city")),
      AttributeOverride( name = "state", column = Column(name = "billingAddress_state")),
      AttributeOverride( name = "zipCode", column = Column(name = "billingAddress_zipCode"))
    )
    var billingAddress: Address? = null,
    @Enumerated(EnumType.STRING) var type: CompanyType? = null,
    @Enumerated(EnumType.STRING) var industry: Industry? = null
)

@Entity
data class Division(
    @Id var divisionId: UUID? = null,
    var name: String? = null,
    @OneToOne(fetch = FetchType.EAGER) @JoinColumn(name = "head", referencedColumnName = "employeeId") var head: Employee? = null,
    @ElementCollection (fetch = FetchType.EAGER)var departments:  Set<Department>? = null
)

@Entity
data class Department(
    @Id var departmentId: UUID? = null,
    var name: String? = null,
    @OneToOne(fetch = FetchType.EAGER) @JoinColumn(name = "head", referencedColumnName = "employeeId") var head: Employee? = null,
    @ElementCollection (fetch = FetchType.EAGER)var employees:  Set<Employee>? = null
)

@Entity
data class Employee(
    @Id var employeeId: UUID? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    @Enumerated(EnumType.STRING) var type: EmploymentType? = null
)

