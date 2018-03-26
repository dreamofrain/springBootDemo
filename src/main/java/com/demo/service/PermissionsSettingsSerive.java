package com.demo.service;

import java.util.List;

import com.demo.entity.PermissionsSettings;

public interface PermissionsSettingsSerive {
	public List<PermissionsSettings> selectAllTree(int id);

	public PermissionsSettings save(PermissionsSettings p);

	public int removeUserPer(int parseInt);
}
