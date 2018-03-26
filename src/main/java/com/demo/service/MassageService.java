package com.demo.service;

import java.util.List;
import java.util.Map;

import com.demo.entity.Massage;

public interface MassageService {
	public List<Massage> findAll();

	public Map<String, Object> getPackage(int pageSize, int page);

	public void deleteDemo(Integer id);

	public Massage save(Massage massage);

	public Massage findByPath(String path);

	public Massage findById(Integer id);
}
