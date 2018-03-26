package com.demo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.entity.Users;
import com.demo.repository.UserRepository;
import com.demo.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public Users save(Users user) {
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(int id) {
		
	}

	@Override
	public Map<String, Object> getPackage(int pageSize, int page) {
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = new PageRequest(pageSize, page, sort);
		Page<Users> users = userRepository.findAll(pageable);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", users.getTotalElements());
		result.put("rows", users.getContent());
		return result;
	}

	@Override
	public List<Users> getUsers() {
		return null;
	}

	@Override
	public Users login(String loginId, String passWord) {
		return userRepository.login(loginId,passWord);
	}

	@Override
	public int stopUser(Integer id,String caozuo) {
		return userRepository.stopUser(id,caozuo);
	}

	@Override
	public int findLoginId(String loginId) {
		return userRepository.findLoginId(loginId);
	}

	@Override
	public int findUserName(String userName) {
		return userRepository.findUserName(userName);
	}

}
