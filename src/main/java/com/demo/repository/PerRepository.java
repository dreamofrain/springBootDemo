package com.demo.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.demo.entity.Permissions;

@Repository
public interface PerRepository extends PagingAndSortingRepository<Permissions, Integer>, JpaSpecificationExecutor<Permissions>{
}
