package com.demo.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.demo.entity.Massage;

@Repository
public interface MassageRepository extends PagingAndSortingRepository<Massage, Integer>, JpaSpecificationExecutor<Massage> {
	@Query("select g from massage g where g.path=?1")
	Massage findByPath(String path);

	@Query("select g from massage g where g.id=?1")
	Massage findById(Integer id);
}