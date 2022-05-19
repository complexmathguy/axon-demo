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

import java.util.*;

import javax.persistence.Entity
import javax.persistence.Id

//-----------------------------------------------------------
// Query definitions
//-----------------------------------------------------------

// -----------------------------------------
// Company Queries 
// -----------------------------------------

data class LoadCompanyFilter(val companyId :  UUID? = null )

class FindCompanyQuery(val filter: LoadCompanyFilter = LoadCompanyFilter()) {
    override fun toString(): String = "LoadCompanyQuery"
}

class FindAllCompanyQuery() {
    override fun toString(): String = "LoadAllCompanyQuery"
}

data class CompanyFetchOneSummary(@Id var companyId : UUID? = null) {
}



data class FindByNameFilter(val name: String? = null)
class FindByNameQuery(val filter: FindByNameFilter = FindByNameFilter())

data class FindCompaniesByIndustryFilter(val industry: Industry? = null)
class FindCompaniesByIndustryQuery(val offset: Int, val limit: Int, val filter: FindCompaniesByIndustryFilter = FindCompaniesByIndustryFilter() )

data class FindByTypeFilter(val type: CompanyType? = null)
class FindByTypeQuery(val offset: Int, val limit: Int, val filter: FindByTypeFilter = FindByTypeFilter() )



// -----------------------------------------
// Division Queries 
// -----------------------------------------

data class LoadDivisionFilter(val divisionId :  UUID? = null )

class FindDivisionQuery(val filter: LoadDivisionFilter = LoadDivisionFilter()) {
    override fun toString(): String = "LoadDivisionQuery"
}

class FindAllDivisionQuery() {
    override fun toString(): String = "LoadAllDivisionQuery"
}

data class DivisionFetchOneSummary(@Id var divisionId : UUID? = null) {
}





// -----------------------------------------
// Department Queries 
// -----------------------------------------

data class LoadDepartmentFilter(val departmentId :  UUID? = null )

class FindDepartmentQuery(val filter: LoadDepartmentFilter = LoadDepartmentFilter()) {
    override fun toString(): String = "LoadDepartmentQuery"
}

class FindAllDepartmentQuery() {
    override fun toString(): String = "LoadAllDepartmentQuery"
}

data class DepartmentFetchOneSummary(@Id var departmentId : UUID? = null) {
}





// -----------------------------------------
// Employee Queries 
// -----------------------------------------

data class LoadEmployeeFilter(val employeeId :  UUID? = null )

class FindEmployeeQuery(val filter: LoadEmployeeFilter = LoadEmployeeFilter()) {
    override fun toString(): String = "LoadEmployeeQuery"
}

class FindAllEmployeeQuery() {
    override fun toString(): String = "LoadAllEmployeeQuery"
}

data class EmployeeFetchOneSummary(@Id var employeeId : UUID? = null) {
}






