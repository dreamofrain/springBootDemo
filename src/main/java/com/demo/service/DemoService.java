package com.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.demo.entity.Demo;

public interface DemoService {
	// 添加
	public Demo save(Demo demo);

	// 添加
	public List<Demo> saveList(List<Demo> demos);

	// 删除
	public void deleteDemo(int id);

	// 分页查询
	public Map<String, Object> getPackage(int pageSize, int page, String otherValue);

	// 分页查询
	public Map<String, Object> getPackageMSG(int pageSize, int page, String status);
		
	public List<Demo> getDemos();

	public int getNUM(String value);

	List<Demo> getNums(Specification<Demo> spec);

	public void deleteAll();

	public List<Demo> getDemosByType(String type);

	public int deleteDemoByType(String type);

	public void generateMessage(Integer id,String status);

	public void returnMessage(String status);
}
