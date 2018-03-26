package com.demo.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.entity.Massage;
import com.demo.service.DemoService;
import com.demo.service.MassageService;

@RestController
public class MassageController {
	@Autowired
	private MassageService massageService;
	
	@Autowired
	private DemoService demoService;

	@RequestMapping("massage.page.action")
	public Map<String, Object> selectMassagePages(HttpServletRequest request) {
		//报文信息的分页查询，同数据信息
		Map<String, Object> lists = massageService.getPackage(Integer.valueOf(request.getParameter("page")) - 1,
				Integer.valueOf(request.getParameter("rows")));
		return lists;
	}

	@RequestMapping("showMessage")
	public Map<String, Object> showMessage(HttpServletRequest request) {
		String messageName=request.getParameter("messageName");
		//报文信息的分页查询，同数据信息
		Map<String, Object> demos = demoService.getPackageMSG(Integer.valueOf(request.getParameter("page")) - 1,
				Integer.valueOf(request.getParameter("rows")), messageName);
		return demos;
	}
	
	@RequestMapping("messages.remove.action")
	public Integer deleteMassage(Integer id) throws IOException {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			//打回一个报文，实则为清空该报文
			Massage massage=massageService.findById(id);
			// 获取报文文件
			File fileText = new File(massage.getPath());
			// 获取字符输出流
			fos = new FileOutputStream(fileText);// 覆写文档
			bw = new BufferedWriter(new OutputStreamWriter(fos, "GBK"));
			// 将文档重写为空
			bw.write("");
			bw.flush();
			//删除报文信息
			massageService.deleteDemo(id);
			demoService.returnMessage(massage.getName());
			return 1;
		} catch (Exception e) {
			return 0;
		} finally {
			if (bw != null) {
				bw.flush();
				bw.close();
			}
			if (fos != null) {
				fos.flush();
				fos.close();
			}
		}
	}

	// 下载报文
	@RequestMapping(value = "/downloadMessages", method = RequestMethod.GET)
	public void downloadMessages(HttpServletRequest request, HttpServletResponse response) {
		InputStream input = null;
		OutputStream output = null;
		FileInputStream fis = null;
		try {
			//获取要下载的报文信息
			int id=Integer.parseInt(request.getParameter("id"));
			Massage massage=massageService.findById(id);
			//获取报文文件
			File file = new File(massage.getPath());
			//判断文件是否存在
			if (file.exists()) {
				response.setCharacterEncoding("GBK");
				response.setContentType("application/octet-stream;charset=GBK");
				//设置文件标题
				response.setHeader("Content-Disposition",
						"attachment;filename=" + new String(file.toString().substring(11, file.toString().length()).getBytes("GBK"), "ISO8859-1"));
				//输入流
				input = new FileInputStream(file);
				int len = 0;
				byte[] buffer = new byte[1024];
				//获取输出流
				output = response.getOutputStream();
				//输出文件，即为下载
				while ((len = input.read(buffer)) > 0) {
					output.write(buffer, 0, len);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}

				if (output != null) {
					output.flush();
					output.close();
				}

				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
