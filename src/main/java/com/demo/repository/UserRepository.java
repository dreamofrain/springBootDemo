package com.demo.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.demo.entity.Users;

@Repository
public interface UserRepository extends PagingAndSortingRepository<Users, Integer>, JpaSpecificationExecutor<Users> {

	@Query(value="SELECT d FROM users d WHERE d.loginId=?1 and d.passWord=?2")
	public Users login(String loginId, String passWord);
	
	@Modifying
	@Query(value="update users d set d.status=?2 WHERE d.id=?1")
	public int stopUser(Integer id, String caozuo);

	@Query(value="SELECT COUNT(d) FROM users d WHERE d.loginId=?1")
	public int findLoginId(String loginId);

	@Query(value="SELECT COUNT(d) FROM users d WHERE d.userName=?1")
	public int findUserName(String userName);

}