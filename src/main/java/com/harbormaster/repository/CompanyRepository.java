/*******************************************************************************
  Turnstone Biologics Confidential
  
  2018 Turnstone Biologics
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Turnstone Biologics - General Release
 ******************************************************************************/
package com.harbormaster.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;;
import org.springframework.stereotype.Repository;

import com.harbormaster.api.*;
import com.harbormaster.entity.*;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    public Company findCompanyByName( String name );
    
    public List<Company> findCompaniesByIndustry( Industry industry, Pageable page );
    
    public List<Company> findCompaniesByType( CompanyType type, Pageable page );
    
}
