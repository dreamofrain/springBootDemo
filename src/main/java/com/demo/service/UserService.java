package com.demo.service;

import java.util.List;
import java.util.Map;

import com.demo.entity.Users;

public interface UserService {
	// 注册
	public Users save(Users user);

	// 删除
	public void deleteUser(int id);

	// 分页查询
	public Map<String, Object> getPackage(int pageSize, int page);

	public List<Users> getUsers();

	//登录
	public Users login(String userName,String passWord);

	public int stopUser(Integer id,String caozuo);

	public int findLoginId(String loginId);

	public int findUserName(String userName);

}
