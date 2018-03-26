package com.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class UrlAction {

	// 设置跳转链接
	@RequestMapping("/login")
	public String to(HttpServletRequest request) {
		return "login";
	}

	@RequestMapping("/loginOut")
	public String toOut(HttpServletRequest request) {
		request.getSession().removeAttribute("user");
		request.getSession().removeAttribute("perList");
		return "login";
	}

	@RequestMapping("/register")
	public ModelAndView register() {
		ModelAndView mv=new ModelAndView("user/register");
		return mv;
	}

	@RequestMapping("/messageDate")
	public ModelAndView messageDate(HttpServletRequest request) {
		ModelAndView mv=new ModelAndView("message/messageDate");
		mv.addObject("messageName", request.getParameter("messageName"));
		return mv;
	}

	@RequestMapping("/userList")
	public ModelAndView userList() {
		ModelAndView mv=new ModelAndView("user/userList");
		return mv;
	}

	@RequestMapping("/userData")
	public String userData() {
		return "user/userData";
	}

	@RequestMapping("/massage")
	public String massage() {
		return "message/massage";
	}

	@RequestMapping("/demoList")
	public String demoList() {
		return "message/demoList";
	}
}
