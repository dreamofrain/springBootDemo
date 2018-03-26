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

import com.demo.entity.Massage;
import com.demo.repository.MassageRepository;
import com.demo.service.MassageService;

@Service
@Transactional
public class MassageServiceImpl implements MassageService {
	@Autowired
	private MassageRepository typeRepository;

	@Override
	public List<Massage> findAll() {
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		return typeRepository.findAll(null, sort);
	}

	@Override
	public Map<String, Object> getPackage(int pageSize, int page) {
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = new PageRequest(pageSize, page, sort);
		Page<Massage> demos = typeRepository.findAll(pageable);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", demos.getTotalElements());
		result.put("rows", demos.getContent());
		return result;
	}

	@Override
	public void deleteDemo(Integer id) {
		typeRepository.delete(id);
	}

	@Override
	public Massage save(Massage massage) {
		return typeRepository.save(massage);
	}

	@Override
	public Massage findByPath(String path) {
		return typeRepository.findByPath(path);
	}

	@Override
	public Massage findById(Integer id) {
		return typeRepository.findById(id);
	}

}
