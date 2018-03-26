package com.demo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.demo.entity.Demo;
import com.demo.service.DemoService;

import net.sf.json.JSONObject;

/**
 * A rudimentary XLSX -> CSV processor modeled on the POI sample program
 * XLS2CSVmra from the package org.apache.poi.hssf.eventusermodel.examples. As
 * with the HSSF version, this tries to spot missing rows and cells, and output
 * empty entries for them.
 * <p/>
 * Data sheets are read using a SAX parser to keep the memory footprint
 * relatively small, so this should be able to read enormous workbooks. The
 * styles table and the shared-string table must be kept in memory. The standard
 * POI styles table class is used, but a custom (read-only) class is used for
 * the shared string table because the standard POI SharedStringsTable grows
 * very quickly with the number of unique strings.
 * <p/>
 * For a more advanced implementation of SAX event parsing of XLSX files, see
 * {@link XSSFEventBasedExcelExtractor} and {@link XSSFSheetXMLHandler}. Note
 * that for many cases, it may be possible to simply use those with a custom
 * {@link SheetContentsHandler} and no SAX code needed of your own!
 */
public class DemoXLSX2CSV {
	/**
	 * Uses the XSSF Event SAX helpers to do most of the work of parsing the Sheet
	 * XML, and outputs the contents as a (basic) CSV.
	 */
	private StringBuffer lineBuffer;// 记录单元格错误信息的stringbuffer;
	private List<Demo> lists = new ArrayList<Demo>();
	private PrintWriter out = null;
	private HttpServletResponse response;
	private DemoService gcs;

	private class SheetToCSV implements SheetContentsHandler {
		private boolean isError = true;
		private int currentRow = -1;
		private int currentCol = -1;
		private Demo t;
		private StringBuffer errorBuffer = new StringBuffer();;// 记录单元格错误信息的stringbuffer;
		private String[] headers = { "姓名", "性别", "类别", "编号" };

		private void outputMissingRows(int number) {
			for (int i = 0; i < number; i++) {
				for (int j = 0; j < minColumns; j++) {
				}
			}
		}

		@Override
		public void startRow(int rowNum) {
			// If there were gaps, output the missing rows
			outputMissingRows(rowNum - currentRow - 1);
			// Prepare for this row
			currentRow = rowNum;
			currentCol = -1;
			if (isError && rowNum != 0) {
				t = new Demo();
			}
		}

		@Override
		public void endRow(int rowNum) {
			if (isError) {
				int missCol = headers.length - currentCol - 1;// header.length永远比当前行大1，则多减去1得到本行后部分无数据的行数
				if (missCol > 0) {// 如果后部分丢失的行数大于0
					if (rowNum == 0) {
						lineBuffer.append(",导入文件模板不正确!");
						isError = false;
					} else {
						for (int i = 0; i < missCol; i++) {// 则对当前行数进行自增，如果其中有行数为不允许为空的行，则记录行数信息，如果为最后一行，则将错误信息保存
							currentCol += 1;// 每次进来需要对当前行+1
							if ((currentCol >= 0) && (currentCol <= 17)) {// 因为下标从0开始，所以比length少1，判断最后一行时应对length-1//此处只判断最后一行
								errorBuffer.append(",列'" + headers[currentCol] + "'不能为空!");//
								if (currentCol == 17) {
									// 判断一行中是否有错误信息，有则将该行的行号及错误信息记录下来
									lineBuffer.append(
											",----------------------------------,第" + (rowNum + 1) + "行数据不符合导入规则!");// rowNum表示当前行
									lineBuffer.append(errorBuffer);// 将单元格错误信息存储到列错误信息中
									errorBuffer.setLength(0);// 清空单元格错误信息以存储新一行的错误信息
								}
							}
						}
					}
				}
			}
		}

		@Override
		public void cell(String cellReference, String formattedValue, XSSFComment comment) {
			// gracefully handle missing CellRef here in a similar way as XSSFCell does
			if (cellReference == null) {
				cellReference = new CellAddress(currentRow, currentCol).formatAsString();
			}

			int thisRow = (new CellReference(cellReference)).getRow();
			int thisCol = (new CellReference(cellReference)).getCol();
			// Did we miss any cells?
			int missedCols = thisCol - currentCol - 1;
			String value = null;
			// Number or string?
			try {
				formattedValue = formattedValue.replaceAll(",", "");
				Double.parseDouble(formattedValue);
				value = formattedValue;
			} catch (NumberFormatException e) {
				value = formattedValue;
			}
			if (thisRow == 0) {
				if (!lineBuffer.toString().equals("") && lineBuffer.length() > 0) {
					isError = false;
				} else {
					for (int i = 0; i < headers.length; i++) {
						if (thisCol == i) {
							if (value.equals(headers[i])) {

							} else {
								lineBuffer.append(",导入文件模板不正确!");
								break;
							}
						}
					}
				}
				currentCol = thisCol;
			} else {
				if (isError) {
					// 获取空的行数
					if (missedCols > 0) {
						for (int i = 0; i < missedCols; i++) {
							currentCol += 1;
							// 如果进入循环，则该行必定为空
							if ((currentCol >= 0) && (currentCol <= 17)) {
								errorBuffer.append(",列'" + headers[currentCol] + "'不能为空!");
							}
						}
					}
					currentCol = thisCol;

					// 判断数据是否正确
					if (thisCol == 0) {
						int a = gcs.getNUM(value);
						if (a > 0) {
							errorBuffer.append(",列'" + headers[thisCol] + "'已存在！");
						} else {
							t.setName(value);
						}
					}
					if (thisCol == 1) {
						if ("♂".equals(value) || "♀".equals(value)) {
							t.setSex(value);
						} else {
							errorBuffer.append(",列'" + headers[thisCol] + "'不匹配！");
						}
					}
					if (thisCol == 2) {
						if ("一级".equals(value) || "二级".equals(value) || "三级".equals(value)) {
							t.setType(value);
						} else {
							errorBuffer.append(",列'" + headers[thisCol] + "'不匹配！");

						}
					}
					if (thisCol == 3) {
						String vaild = null;
						switch (t.getType()) {
						case "一级":
							vaild = "^\\d{4}$";
							break;

						case "二级":
							vaild = "(^\\d{4})-(\\d{2}$)";
							break;

						case "三级":
							vaild = "^[0-8]{1}\\d{7}$|^9\\d{6}$";
							break;

						default:
							vaild = null;
							errorBuffer.append(",列'" + headers[thisCol] + "'不匹配！");
						}
						if (!"".equals(vaild) || (vaild != null)) {
							if (Pattern.compile(vaild).matcher(value).matches()) {
								t.setCard(value);
							} else {
								errorBuffer.append(",列'" + headers[thisCol] + "'与类别不匹配！");
							}
						}
					}
					if (!errorBuffer.toString().equals("") || (errorBuffer.length() > 0)) {
						// 判断一行中是否有错误信息，有则将该行的行号及错误信息记录下来
						lineBuffer.append(",----------------------------------,第" + (thisRow + 1) + "行数据不符合导入规则!");
						lineBuffer.append(errorBuffer + "");// 将单元格错误信息存储到列错误信息中
						errorBuffer.setLength(0);// 清空单元格错误信息以存储新一行的错误信息
					} else {
						// 判断是否已存在错误信息，如果有错误信息则清空对象且不添加到list
						if (!lineBuffer.toString().equals("") || (lineBuffer.length() > 0)) {
							t = null;
							lists.clear();
						} else {
							lists.add(t);
						}
					}
				}
			}
		}
		@Override
		public void headerFooter(String text, boolean isHeader, String tagName) {
			
		}
	}

	private final OPCPackage xlsxPackage;

	private final int minColumns;

	/**
	 * Creates a new XLSX -> CSV converter
	 * 
	 * @param pkg
	 *            The XLSX package to process
	 * @param output
	 *            The PrintStream to output the CSV to
	 * @param minColumns
	 *            The minimum number of columns to output, or -1 for no minimum
	 */
	public DemoXLSX2CSV(OPCPackage pkg, int minColumns, StringBuffer lineBuffer, HttpServletResponse response,
			DemoService gcs) {
		this.xlsxPackage = pkg;
		this.minColumns = minColumns;
		this.lineBuffer = lineBuffer;// 记录单元格错误信息的stringbuffer
		this.response = response;
		this.gcs = gcs;
	}

	/**
	 * Parses and shows the content of one sheet using the specified styles and
	 * shared-strings tables.
	 * 
	 * @param styles
	 * @param strings
	 * @param sheetInputStream
	 */
	public void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetHandler,
			InputStream sheetInputStream) throws IOException, ParserConfigurationException, SAXException {
		DataFormatter formatter = new DataFormatter();
		InputSource sheetSource = new InputSource(sheetInputStream);
		try {
			XMLReader sheetParser = SAXHelper.newXMLReader();
			ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
			sheetParser.setContentHandler(handler);
			sheetParser.parse(sheetSource);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
		}
	}

	/**
	 * Initiates the processing of the XLS workbook file to CSV.
	 * 
	 * @throws IOException
	 * @throws OpenXML4JException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void process() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
		ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
		XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
		StylesTable styles = xssfReader.getStylesTable();
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		// 循环遍历Excel数据
		while (iter.hasNext()) {
			InputStream stream = iter.next();
			// 读取sheet的方法
			processSheet(styles, strings, new SheetToCSV(), stream);
			stream.close();
		}
		// 循环结束，数据读取完毕
		out = response.getWriter();
		JSONObject json = new JSONObject();
		// 如果有错误信息则不导入数据且返回错误信息
		if (lineBuffer.length() > 0) {
			lineBuffer.insert(0, "导入文件失败!");
			json.put("msg", lineBuffer.toString());
			out.write(json.toString());
		} else {
			List<Demo> res = gcs.saveList(lists);
			if (res != null && res.size() > 0) {
				json.put("msg", "导入文件成功！");
				out.write(json.toString());
			} else {
				json.put("msg", "导入文件失败！");
				out.write(json.toString());
			}
		}
		out.flush();
		out.close();
	}
}