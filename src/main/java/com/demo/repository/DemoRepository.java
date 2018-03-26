package com.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.demo.entity.Demo;

@Repository
public interface DemoRepository extends PagingAndSortingRepository<Demo, Integer>, JpaSpecificationExecutor<Demo> {

	@Query(value="SELECT d FROM demo d where d.status='无'")
	public List<Demo> findDemos();

	//	// 删除
	//	@Modifying
	//	@Query("delete from demo where id=?")
	//	public int deleteDemo(int id);

	@Query(value="SELECT COUNT(d.id) FROM demo d WHERE d.name=?1")
	public int getNUMS(String name);

	@Query(value="SELECT d FROM demo d where d.type=?1 and d.status='无'")
	public List<Demo> getDemosByType(String type);

	@Modifying
	@Query(value="DELETE FROM demo d where d.type=?1 and d.status='无'")
	public int deleteDemoByType(String type);

	@Modifying
	@Query(value="update demo d set d.status=?2 where d.id=?1")
	public void generateMessage(Integer id,String status);

	@Modifying
	@Query(value="update demo d set d.status='无' where d.status=?1")
	public void returnMessage(String status);
}