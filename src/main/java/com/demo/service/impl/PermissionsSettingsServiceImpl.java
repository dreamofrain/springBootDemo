package com.demo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.demo.entity.PermissionsSettings;
import com.demo.repository.PermissionsSettingsRepository;
import com.demo.service.PermissionsSettingsSerive;


@Service
public class PermissionsSettingsServiceImpl implements PermissionsSettingsSerive {

	@Resource
	private PermissionsSettingsRepository repository;

	@Override
	public List<PermissionsSettings> selectAllTree(int id) {
		return repository.selectAllTree(id);
	}

	@Override
	public PermissionsSettings save(PermissionsSettings p) {
		return repository.save(p);
	}

	@Override
	public int removeUserPer(int userid) {
		return repository.removeUserPer(userid);
	}

}
