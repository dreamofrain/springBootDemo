package com.demo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.entity.PermissionsSettings;
import com.demo.service.PermissionsSettingsSerive;
import com.demo.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private PermissionsSettingsSerive permissionsSettingsSerive;

	//设置权限
	@RequestMapping("users.setper.action")
	public String setper(HttpServletRequest request) {
		//获取用户和选中的权限
		String user = request.getParameter("user");
		String pers = request.getParameter("pers");
		String[] persArry = pers.split("-");
		//每次分配时，先清除掉该用户拥有的权限
		permissionsSettingsSerive.removeUserPer(Integer.parseInt(user));
		//设置权限
		for (int i = 0; i < persArry.length; i++) {
			PermissionsSettings p = new PermissionsSettings();
			p.setUserId(Integer.parseInt(user));
			p.setPerid(Integer.parseInt(persArry[i]));
			permissionsSettingsSerive.save(p);
		}
		return "1";
	}

	@RequestMapping("users.page.action")
	public Map<String, Object> usersPages(HttpServletRequest request) {
		//用户列表的分页查询
		Map<String, Object> users = userService.getPackage(Integer.valueOf(request.getParameter("page")) - 1,
				Integer.valueOf(request.getParameter("rows")));
		return users;
	}

	@RequestMapping("users.update.action")
	public Integer stopUser(Integer id, String status) {
		//停封和解封
		try {
			String caozuo = null;
			switch (status) {
			case "stop":
				caozuo = "已停封";
				break;
			case "deblock":
				caozuo = "正常";
				break;
			}
			userService.stopUser(id, caozuo);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	@RequestMapping("users.per.action")
	public List<PermissionsSettings> usersPer(int id) {
		//获取该用户权限
		List<PermissionsSettings> list = permissionsSettingsSerive.selectAllTree(id);
		return list;
	}
}
