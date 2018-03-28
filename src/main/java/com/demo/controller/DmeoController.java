package com.demo.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.xml.sax.SAXException;

import com.demo.entity.Demo;
import com.demo.entity.Massage;
import com.demo.service.DemoService;
import com.demo.service.MassageService;

import net.sf.json.JSONObject;

@RestController
public class DmeoController {
	@Autowired
	private DemoService demoService;

	@Autowired
	private MassageService massageService;

	@RequestMapping("demo.page.action")
	public Map<String, Object> selectPages(HttpServletRequest request) {
		//分页查询返回一个MAP,page和rows对应easyui的分页
		Map<String, Object> demos = demoService.getPackage(Integer.valueOf(request.getParameter("page")) - 1,
				Integer.valueOf(request.getParameter("rows")),request.getParameter("otherValue"));
		return demos;
	}

	@RequestMapping("demo.delete.action")
	public Integer deleteDemo(Integer id,String type) {
		try {
			//删除，两种情况，一是删除一条数据，一是删除一类数据
			if (id!=null) {
				demoService.deleteDemo(id);
			} else {
				demoService.deleteDemoByType(type);
			}
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	@RequestMapping("demo.deleteAll.action")
	public Integer deleteAll() {
		try {
			//删除全部
			demoService.deleteAll();
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	@RequestMapping(value="demo.update.action",method=RequestMethod.POST)
	public Integer updateDemo(Demo demo) {//spring将from表单对象按照对应的属性名封装为对象
		try {
			if ("".equals(demo.getStatus())||null==demo.getStatus()) {
				demo.setStatus("无");
			}
			//保存和修改，调用的是一个方法
			demoService.save(demo);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	@RequestMapping("demo.generateMessage.action")
	public String generateMessageDemo(HttpServletResponse response, HttpServletRequest request) throws IOException {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			//目前生成报文是根据类别生成，获取生成报文的类别
			String type=request.getParameter("otherValue");
			List<Demo> list = demoService.getDemosByType(type);
			// 依次判断文件夹是否存在，不存在则创建文件夹
			File file = new File("D:/massage");
			if (!file.exists()) {
				file.mkdir();
			}
			// 创建报文文件
			File fileText = new File(file + "/"+type+".txt");
			if (!fileText.exists()) {
				fileText.createNewFile();
			}
			// 获取字符输出流
			fos = new FileOutputStream(fileText, true);//续写文档
			// fos = new FileOutputStream(fileText, false);// 覆写文档

			bw = new BufferedWriter(new OutputStreamWriter(fos, "GBK"));
			//格式化一个当前的日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String messagedate = sdf.format((new Date()));

			String messageName=type + messagedate.replace("-", "");
			// 定义生成报文名和生成报文时间
			for (int i = 0; i < list.size(); i++) {
				Demo g = list.get(i);
				// 生成要写入的数据
				StringBuffer str = new StringBuffer("" + g.getId());
				str.append("," + g.getName());
				str.append("," + g.getSex());
				str.append("," + g.getType());
				str.append("," + g.getCard());
				//输出到文档
				bw.write(str.toString());
				//换行
				bw.newLine();
				demoService.generateMessage(g.getId(),messageName);
			}
			//创建报文对象
			Massage massage = new Massage();
			//报文名为类别+XXXXXXXX
			massage.setName(messageName);
			massage.setPath(fileText.toString());
			//查找该报文是否存在
			Massage massage2 = massageService.findByPath(fileText.toString());
			if (massage2 != null) {
				massage.setId(massage2.getId());
			}
			//如果报文存在，则修改，不存在则生成
			Massage massage3 = massageService.save(massage);
			if (massage3 != null) {
				//清空缓冲区
				bw.flush();
				return "1";
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				bw.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return "0";
	}

	@RequestMapping("demo.export.action")
	public void exportDemo(HttpServletResponse response, HttpServletRequest request) throws IOException {
		try {
			//导出可以根据类别来导出，也可导出全部
			String type=request.getParameter("type");
			List<Demo> demos =new ArrayList<Demo>();
			
			if ("".equals(type)||type==null) {
				demos= demoService.getDemos();
			} else {
				demos=demoService.getDemosByType(type);
			}
			//设置表头，用于对应数据
			String[] headers = { "姓名", "性别", "类别", "编号" };
			String fileName = "demos.xlsx"; // 设置下载的文件名称
			String title = "Sheet"; // Excel表格sheet名称

			SXSSFWorkbook wb = new SXSSFWorkbook(); // 创建一个新的excel
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/x-download");

			fileName = URLEncoder.encode(fileName, "UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			SXSSFSheet sheet = wb.createSheet(title); // 创建sheet页
			SXSSFRow row = sheet.createRow(0); // 创建第一行
			// 创建第一行第i格,循环创建表头标题行
			for (int i = 0; i < headers.length; i++) {
				SXSSFCell cell = row.createCell(i);
				cell.setCellValue(headers[i]);
			}

			// 根据集合中的数据量，创建数据行
			for (int i = 0; i < demos.size(); i++) {
				SXSSFRow rowPlus = sheet.createRow(i + 1); // 从第二行开始创建
				Demo t = demos.get(i);

				SXSSFCell cell0 = rowPlus.createCell(0);
				cell0.setCellValue(t.getName());

				SXSSFCell cell1 = rowPlus.createCell(1);
				cell1.setCellValue(t.getSex());

				SXSSFCell cell2 = rowPlus.createCell(2);
				cell2.setCellValue(t.getType());

				SXSSFCell cell3 = rowPlus.createCell(3);
				cell3.setCellValue(t.getCard());
			}
			OutputStream out = response.getOutputStream();
			wb.write(out);
			if (wb != null) {
				wb.close();
			}
			if (out != null) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("demo.import.action")
	public void importDemo(HttpServletResponse response, HttpServletRequest request) throws IOException {
		Workbook wb = null;
		PrintWriter out = null;
		InputStream in = null;
		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.setContentType("text/plain; charset=utf-8");
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file_info");
			out = response.getWriter();
			JSONObject json = new JSONObject();

			if (file == null) {
				json.put("msg", "导入文件失败!");
			} else {
				in = file.getInputStream();
				OPCPackage p = OPCPackage.open(in);
				//使用xlsx2cvs的方式导入文件
				DemoXLSX2CSV xlsx2csv = new DemoXLSX2CSV(p, -1, new StringBuffer(), response, demoService); // 构造器传入需要的参数
				xlsx2csv.process();
				p.close(); // 释放
				// 将信息加到日志中
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (OpenXML4JException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
			if (in != null) {
				in.close();
			}
			if (wb != null) {
				wb.close();
			}
		}
	}
}
