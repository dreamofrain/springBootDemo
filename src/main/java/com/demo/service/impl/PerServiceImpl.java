package com.demo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.demo.entity.Permissions;
import com.demo.repository.PerRepository;
import com.demo.service.PerSerive;


@Service
public class PerServiceImpl implements PerSerive {

	@Resource
	private PerRepository repository;


	@Override
	public List<Permissions> selectAllTree() {
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		return repository.findAll(null,sort);
	}

}
