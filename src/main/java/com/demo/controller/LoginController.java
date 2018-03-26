package com.demo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.demo.entity.Permissions;
import com.demo.entity.PermissionsSettings;
import com.demo.entity.Users;
import com.demo.service.PerSerive;
import com.demo.service.PermissionsSettingsSerive;
import com.demo.service.UserService;

@RestController
public class LoginController {
	@Autowired
	private UserService userService;

	@Autowired
	private PerSerive perService;

	@Autowired
	private PermissionsSettingsSerive permissionsSettingsSerive;

	@RequestMapping("login.demo.action")
	public ModelAndView login(HttpServletRequest request) {
		//获取账号密码
		String loginId = request.getParameter("loginId");
		String passWord = request.getParameter("password");
		ModelAndView mv = null;
		//使用登录方法判断该用户是否存在
		Users user = userService.login(loginId, passWord);
		if (user != null) {
			//判断用户是否被停封
			if ("已停封".equals(user.getStatus())) {
				mv = new ModelAndView("login");
				mv.addObject("message", "登录失败！该账户已被停封！");
			} else {
				//设置跳转到主页
				mv = new ModelAndView("common/index");
				//存储用户信息
				request.getSession().setAttribute("user", user);
				//判断用户如果不为admin，则获取该用户拥有的权限
				if (!user.getLoginId().equals("admin")) {
					List<PermissionsSettings> permissionsSettings = permissionsSettingsSerive
							.selectAllTree(user.getId());
					List<Integer> perList = new ArrayList<Integer>();
					for (PermissionsSettings permissionsSettings2 : permissionsSettings) {
						perList.add(permissionsSettings2.getPerid());
					}
					Collections.sort(perList);
					request.getSession().setAttribute("perList", perList);
				}
			}
		} else {
			mv = new ModelAndView("login");
			mv.addObject("message", "登录失败！账号或密码错误！");
		}
		return mv;
	}

	@RequestMapping("register.demo.action")
	public String register(HttpServletRequest request, Users user,String oldLoginId,String oldName) {
		//注册用户和修改用户
		//如果用户id为空，则为注册用户
		if (user.getId()==null) {
			//判断账号或名称是否被使用
			if (userService.findLoginId(user.getLoginId())>0||userService.findUserName(user.getUserName())>0) {
				return "3";
			}else {
				if (userService.save(user) != null) {
					return "1";
				} else {
					return "0";
				}
			}
		}else {
			//判断用户是否修改了用户名和账号，如果未修改，则直接保存
			if (user.getUserName().equals(oldName)&&user.getLoginId().equals(oldLoginId)) {
				if (userService.save(user) != null) {
					return "1";
				} else {
					return "0";
				}
			}else {
				//判断用户是否修改了名称
				if (user.getUserName().equals(oldName)) {
					//如果未修改名称，则修改了账号，则判断新账号是否存在
					if (userService.findLoginId(user.getLoginId())>0) {
						return "3";
					}else {
						if (userService.save(user) != null) {
							return "1";
						} else {
							return "0";
						}
					}
				//同上，判断用户是否修改了账号
				}else if (user.getLoginId().equals(oldLoginId)) {
					if (userService.findUserName(user.getUserName())>0) {
						return "3";
					}else {
						if (userService.save(user) != null) {
							return "1";
						} else {
							return "0";
						}
					}
				}else {
					//如果账号和名称全部被修改
					if (userService.findLoginId(user.getLoginId())>0||userService.findUserName(user.getUserName())>0) {
						return "3";
					}else {
						if (userService.save(user) != null) {
							return "1";
						} else {
							return "0";
						}
					}
				}
			}
		}
	}

	@RequestMapping("per.list.action")
	public List<Permissions> getPers() {
		//获取权限信息
		List<Permissions> list = perService.selectAllTree();
		return list;
	}
}
