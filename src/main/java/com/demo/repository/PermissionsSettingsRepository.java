package com.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.demo.entity.PermissionsSettings;

@Transactional
@Repository
public interface PermissionsSettingsRepository extends PagingAndSortingRepository<PermissionsSettings, Integer>, JpaSpecificationExecutor<PermissionsSettings>{
	@Query(value="select p from permissionsSettings p where p.userId=?1")
	List<PermissionsSettings> selectAllTree(int id);

	@Modifying
	@Query(value="delete from permissionsSettings p where p.userId=?1")
	int removeUserPer(int userid);
}
