package com.tgb.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.cust.CustNzNeedService;
import com.tgb.service.cust.CustZzService;
import com.tgb.service.propertymanage.LrdRoleService;
import com.tgb.service.tree.CustMgTreeService;
import com.tgb.util.DicReplace;

import net.sf.json.JSONObject;
@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/custExport")
public class CustExportController extends BaseController{
	Logger log = Logger.getLogger(CustExportController.class);
	@Autowired
	private LrdRoleService lrdRoleService;
	@Autowired
	private CustMgTreeService custMgTreeService;
	@Autowired
	private CustNzNeedService custNzNeedService;
	@Autowired
	private CustZzService custZzService;
	@ResponseBody  
	@RequestMapping(value = "/exportCustBase", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@SystemControllerLog(description = "客户信息导出")
	public String exportCustBase(
			HttpServletRequest request, HttpServletResponse response, String exportParams) throws Exception{
		log.info("---------进入导出"); 
		JSONObject obj = JSONObject.fromObject(exportParams);
		//@RequestParam(value="exportParams",required = false)
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			List<Map<String, Object>> userRole = lrdRoleService.queryUserRole(map);//获取用户权限
			List<Map<String,Object>> roleList = user.getROLE_CD();//权限列表
			String fw="";
			
			for (Map<String, Object> map2 : roleList) {
				fw=map2.get("ROLE_FW")+"";
			}
			
			
			//寻找最大范围
			if (fw.contentEquals("01")) {//可查看所有---不对数据进行处理
				
				
			}else if (fw.contentEquals("02")) {//可查看本级及下级
				
				map.put("ZH_ID",user.getOrgCD());
				
			}else {//查询本级的
				map.put("ZH_ID",user.getOrgCD());
				map.put("JL_ID",user.getUser_id());
				
			}
			//String isPagination = obj.getString("isPagination");
			if(obj.get("CUST_TYPE") != null && !obj.get("CUST_TYPE").equals("")){
				map.put("CUST_TYPE", obj.get("CUST_TYPE"));
			}
			if(obj.get("C_ID") != null && !obj.get("C_ID").equals("")){
				map.put("C_ID", obj.get("C_ID"));
			}
			if(obj.get("CUST_GRP") != null && !obj.get("CUST_GRP").equals("")){
				map.put("CUST_GRP", obj.get("CUST_GRP"));
			}
			if(obj.get("CUST_GRP2") != null && !obj.get("CUST_GRP2").equals("")){
				map.put("CUST_GRP2", obj.get("CUST_GRP2"));
			}
			if(obj.get("JL_TYPE") != null && !obj.get("JL_TYPE").equals("")){
				map.put("JL_TYPE", obj.get("JL_TYPE"));
			}
			if(obj.get("CREATE_TYPE") != null && !obj.get("CREATE_TYPE").equals("")){
				map.put("CREATE_TYPE", obj.get("CREATE_TYPE"));
			}
			if(obj.get("PJ_JB") != null && !obj.get("PJ_JB").equals("")){
				map.put("PJ_JB", obj.get("PJ_JB"));
			}
			if(obj.get("ED_LMT_START") != null && !obj.get("ED_LMT_START").equals("")){
				map.put("ED_LMT_START", obj.get("ED_LMT_START"));
			}
			if(obj.get("ED_LMT_END") != null && !obj.get("ED_LMT_END").equals("")){
				map.put("ED_LMT_END", obj.get("ED_LMT_END"));
			}
			if(obj.get("CRT_DATE_START") != null && !obj.get("CRT_DATE_START").equals("")){
				map.put("CRT_DATE_START", obj.get("CRT_DATE_START"));
			}
			if(obj.get("CRT_DATE_END") != null && !obj.get("CRT_DATE_END").equals("")){
				map.put("CRT_DATE_END", obj.get("CRT_DATE_END"));
			}
			if(obj.get("ORG_CD_SELECT") != null && !obj.get("ORG_CD_SELECT").equals("")){
				map.put("ORG_CD_SELECT", obj.get("ORG_CD_SELECT"));
			}
			if(obj.get("USER_ID_SELECT") != null && !obj.get("USER_ID_SELECT").equals("")){
				map.put("USER_ID_SELECT", obj.get("USER_ID_SELECT"));
			}
			if(obj.get("BF_CRT_DATE_START") != null && !obj.get("BF_CRT_DATE_START").equals("")){
				map.put("BF_CRT_DATE_START", obj.get("BF_CRT_DATE_START"));
			}
			if(obj.get("BF_CRT_DATE_END") != null && !obj.get("BF_CRT_DATE_END").equals("")){
				map.put("BF_CRT_DATE_END", obj.get("BF_CRT_DATE_END"));
			}
			if(obj.get("BF_COUNT") != null && !obj.get("BF_COUNT").equals("")){
				map.put("BF_COUNT", obj.get("BF_COUNT"));
			}
			if(obj.get("ISHF") != null && !obj.get("ISHF").equals("")){
				map.put("ISHF", obj.get("ISHF"));
			}
			if(obj.get("HF_DATE_START") != null && !obj.get("HF_DATE_START").equals("")){
				map.put("HF_DATE_START", obj.get("HF_DATE_START"));
			}
			if(obj.get("HF_DATE_END") != null && !obj.get("HF_DATE_END").equals("")){
				map.put("HF_DATE_END", obj.get("HF_DATE_END"));
			}
			if(obj.get("HF_RESULT") != null && !obj.get("HF_RESULT").equals("")){
				map.put("HF_RESULT", obj.get("HF_RESULT"));
			}
			//去除评级级别为0情况
			if(map.get("PJ_JB") != null && map.get("PJ_JB").equals("0.0")){
				log.info("请注意，不存在评级结果为0的情况");
				map.remove("PJ_JB");
			}
			//将授信区间数值转化为decimal
			if(map.get("ED_LMT_START") != null && !map.get("ED_LMT_START").equals("")){
				String strStart = (String) map.get("ED_LMT_START");
				BigDecimal bdStart = new BigDecimal(strStart);
				bdStart = bdStart.multiply(new BigDecimal(10000));
				map.put("ED_LMT_START", bdStart);
			}
			if(map.get("ED_LMT_END") != null && !map.get("ED_LMT_END").equals("")){
				String strEnd = (String) map.get("ED_LMT_END");
				BigDecimal bdEnd = new BigDecimal(strEnd);
				bdEnd = bdEnd.multiply(new BigDecimal(10000));
				map.put("ED_LMT_END", bdEnd);
			}
			//判断是否涉及拜访
			if ((map.get("BF_CRT_DATE_START") != null && !map.get("BF_CRT_DATE_START").equals(""))
					|| (map.get("BF_CRT_DATE_END") != null && !map.get("BF_CRT_DATE_END").equals(""))
					|| (map.get("BF_COUNT") != null && !map.get("BF_COUNT").equals(""))
					|| (map.get("ISHF") != null && !map.get("ISHF").equals(""))
					|| (map.get("HF_DATE_START") != null && !map.get("HF_DATE_START").equals(""))
					|| (map.get("HF_DATE_END") != null && !map.get("HF_DATE_END").equals(""))
					|| (map.get("HF_RESULT") != null && !map.get("HF_RESULT").equals(""))) {
				if(map.get("BF_COUNT") != null&&map.get("BF_COUNT").equals("0")){
					// 当拜访次数为0时
					// 此时需要查询出在cust_feedback中没有state="bf"的记录以及在cust_feedback中
					// 没有关联的记录
					// 0表示未拜访
					map.put("ISBF", "0");
				}else{
					map.put("BF", "true");

					if ((map.get("BF_CRT_DATE_END") == null || map.get("BF_CRT_DATE_END").equals(""))
							&& (map.get("BF_COUNT") != null && !map.get("BF_COUNT").equals(""))) {
						// 默认日期小于等于当前日期，为了拜访次数查询结果
						map.put("BF_CRT_DATE_END", getCurrentDate());
					}
					
					// 回访
					map.put("HF_COUNT", "0");
				}
				
			}
			List<Map<String, Object>> resultList =custMgTreeService.queryList(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			if (null != page) {
				this.page.removeContext();
			}
			dicMap.put("CUST_TYPE", "CUST_TYPE");
			dicMap.put("JL_TYPE", "JL_TYPE");
			dicMap.put("CUST_GRP", "CUST_GRP1");
			// dicMap.put("CUST_GRP2", "CUST_GRP2");
			dicMap.put("AREA_CD", "AREA_CD1");
			dicMap.put("CREATE_TYPE", "ZTBJ");
			//字典项对应的数值
			DicReplace.replaceDic(resultList, dicMap);
			//二级
			DicReplace.replaceChildDic(resultList, "CUST_GRP", "CUST_GRP2");
			DicReplace.replaceChildDic(resultList, "AREA_CD", "AREA_CD2");
			
//////////////////////////////建立Excel///////////////////////////////////////
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(); 
			// 创建样式-文本 (文本居中)
			XSSFCellStyle xssfCellStyle = xssfWorkbook.createCellStyle();
			xssfCellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
			xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			// 创建样式-标题 (文本居中, 背景为蓝颜色，有边框)
			XSSFCellStyle xssfCellStyleTitle = xssfWorkbook.createCellStyle();
			xssfCellStyleTitle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
			xssfCellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
			XSSFFont font = xssfWorkbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			xssfCellStyleTitle.setFont(font);
			Sheet sheet1 = xssfWorkbook.createSheet("Sheet1");
			sheet1.setColumnWidth(0, 7000);
			sheet1.setColumnWidth(1, 4000);
			sheet1.setColumnWidth(2, 4000);
			sheet1.setColumnWidth(3, 4000);
			sheet1.setColumnWidth(4, 4000);
			sheet1.setColumnWidth(5, 4000);
			sheet1.setColumnWidth(6, 4000);
			sheet1.setColumnWidth(7, 4000); 
			sheet1.setColumnWidth(8, 4000);
			sheet1.setColumnWidth(9, 4000);
			Row row1 = xssfWorkbook.getSheetAt(0).createRow(0);
			//所属机构
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 0, 0));
			Cell cell11 = row1.createCell(0);
			cell11.setCellValue("所属机构");
			cell11.setCellStyle(xssfCellStyleTitle);
			//客户名称
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 1, 1));
			Cell cell12 = row1.createCell(1);
			cell12.setCellValue("客户名称");
			cell12.setCellStyle(xssfCellStyleTitle);
			//电话号码
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 2, 2));
			Cell cell13 = row1.createCell(2);
			cell13.setCellValue("电话号码");
			cell13.setCellStyle(xssfCellStyleTitle);
			//客户类型
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 3, 3));
			Cell cell14 = row1.createCell(3);
			cell14.setCellValue("客户类型");
			cell14.setCellStyle(xssfCellStyleTitle);
			//数据更新日期
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 4, 4));
			Cell cell15 = row1.createCell(4);
			cell15.setCellValue("数据更新日期");
			cell15.setCellStyle(xssfCellStyleTitle);
			//状态标记
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 5, 5));
			Cell cell16 = row1.createCell(5);
			cell16.setCellValue("状态标记");
			cell16.setCellStyle(xssfCellStyleTitle);
			//最高额度
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 6, 6));
			Cell cell17 = row1.createCell(6);
			cell17.setCellValue("最高额度");
			cell17.setCellStyle(xssfCellStyleTitle);
			//信用额度
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 7, 7));
			Cell cell18 = row1.createCell(7);
			cell18.setCellValue("信用额度");
			cell18.setCellStyle(xssfCellStyleTitle);
			//亲情额度
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 8, 8));
			Cell cell19 = row1.createCell(8);
			cell19.setCellValue("亲情额度");
			cell19.setCellStyle(xssfCellStyleTitle);
			//管户经理
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 9, 9));
			Cell cell20 = row1.createCell(9);
			cell20.setCellValue("管户经理");
			cell20.setCellStyle(xssfCellStyleTitle);
			//
			double totalEdLmt = 0,totalCreaLmt = 0,totalQqLmt = 0;
			for(int i = 0;i < resultList.size();i++){
				Map<String, Object> indexMap = resultList.get(i);
				Row row = xssfWorkbook.getSheetAt(0).createRow(1 + i);
				Cell indexCorp = row.createCell(0); // 所属机构
				Cell indexCustName = row.createCell(1); // 客户名称
				Cell indexTel = row.createCell(2); // 电话号码
				Cell indexCustType = row.createCell(3); // 客户类型
				Cell indexMntDate = row.createCell(4); //  数据更新日期
				Cell indexZt = row.createCell(5); // 状态标记
				Cell indexEdLmt = row.createCell(6); // 最高额度
				Cell indexCreaLmt = row.createCell(7); //信用额度
				Cell indexQqLmt = row.createCell(8); //亲情额度
				Cell indexUserName = row.createCell(9); //管户经理
				String strEdLmt = (String) indexMap.get("ED_LMT");//额度保留一位小数
				String strCreaLmt = (String) indexMap.get("CREALIMIT");
				String strQqLmt = (String) indexMap.get("QQLIMIT");
				double edLmt = 0;
				if(strEdLmt == null){
					strEdLmt = "";
				}else{
					edLmt = Double.parseDouble(strEdLmt);
					totalEdLmt += edLmt;
					edLmt = div(edLmt, 10000, 1);
				}
				if(strCreaLmt == null){
					strCreaLmt = "";
				}else{
					double creaLmt = Double.parseDouble(strCreaLmt);
					totalCreaLmt += creaLmt;
				}
				if(strQqLmt == null){
					strQqLmt = "";
				}else{
					double qqLmt = Double.parseDouble(strQqLmt);
					totalQqLmt += qqLmt;
				}
				//double edLmt = Double.parseDouble(strEdLmt);
				//double creaLmt = Double.parseDouble(strCreaLmt);
				indexCorp.setCellValue((String)indexMap.get("ORG_ABB"));
				indexCustName.setCellValue((String)indexMap.get("CUST_NAME"));
				indexTel.setCellValue((String)indexMap.get("TEL_NO"));
				indexCustType.setCellValue((String)indexMap.get("CUST_TYPEC"));
				indexMntDate.setCellValue((String)indexMap.get("MTN_DATE"));
				indexZt.setCellValue((String)indexMap.get("CREATE_TYPEC"));
				indexEdLmt.setCellValue(strEdLmt==""?"暂无":String.format("%.1f", edLmt));
				indexCreaLmt.setCellValue(strCreaLmt==""?"暂无":String.format("%.1f", Double.parseDouble(strCreaLmt)));
				indexQqLmt.setCellValue(strCreaLmt==""?"暂无":String.format("%.1f", Double.parseDouble(strQqLmt)));
				indexUserName.setCellValue((String)indexMap.get("CUST_GRP_JLC"));
				indexCorp.setCellStyle(xssfCellStyle);
				indexCustName.setCellStyle(xssfCellStyle);
				indexTel.setCellStyle(xssfCellStyle);
				indexCustType.setCellStyle(xssfCellStyle);
				indexMntDate.setCellStyle(xssfCellStyle);
				indexZt.setCellStyle(xssfCellStyle);
				indexEdLmt.setCellStyle(xssfCellStyle);
				indexCreaLmt.setCellStyle(xssfCellStyle);
				indexQqLmt.setCellStyle(xssfCellStyle);
				indexUserName.setCellStyle(xssfCellStyle);
			}
			//合计信息
			Row resultRow = xssfWorkbook.getSheetAt(0).createRow(1 + resultList.size());
			Cell overCell1 = resultRow.createCell(0);
			Cell overCell7 = resultRow.createCell(6);
			Cell overCell8 = resultRow.createCell(7);
			Cell overCell9 = resultRow.createCell(8);
			overCell1.setCellValue("合计");
			overCell7.setCellValue(div(totalEdLmt, 10000, 1)); //div(totalEdLmt, 10000, 1)
			overCell8.setCellValue(String.format("%.1f", totalCreaLmt)); //
			overCell9.setCellValue(String.format("%.1f", totalQqLmt)); //
			overCell1.setCellStyle(xssfCellStyleTitle);
			overCell7.setCellStyle(xssfCellStyleTitle);
			overCell8.setCellStyle(xssfCellStyleTitle);
			overCell9.setCellStyle(xssfCellStyleTitle);
			
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			xssfWorkbook.write(os);

			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);

			response.reset();
			response.setContentType("application/x-download;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + URLEncoder.encode("客户管理", "UTF-8") + ".xlsx");

			ServletOutputStream out = response.getOutputStream();

			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead;

			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}

			if (bis != null) {
				bis.close();
			} 
			if (bos != null) {
				bos.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.setErr(page, e.getLocalizedMessage());
		}
		
		return "导出excel";
	}
	@ResponseBody  
	@RequestMapping(value = "/exportKhxq", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@SystemControllerLog(description = "客户需求信息导出")
	public String exportKhxq(HttpServletRequest request, HttpServletResponse response, String exportParams_khxq){
		log.info("客户需求信息导出");   
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			List<Map<String, Object>> userRole = lrdRoleService.queryUserRole(map);//获取用户权限
			List<Map<String,Object>> roleList = user.getROLE_CD();//权限列表
			String fw="";
			
			for (Map<String, Object> map2 : roleList) {
				fw=map2.get("ROLE_FW")+"";
			}
			//寻找最大范围
			if (fw.contentEquals("01")) {//可查看所有---不对数据进行处理
				
				
			}else if (fw.contentEquals("02")) {//可查看本级及下级
				
				map.put("ZH_ID",user.getOrgCD());
				
			}else {//查询本级的
				map.put("ZH_ID",user.getOrgCD());
				map.put("JL_ID",user.getUser_id());
				
			}
			//默认用“录入”类信息，当涉及农资用户时，用“拜访”类信息
			map.put("TYPE", "LR");
			//筛选条件
			String selectItem = exportParams_khxq;
			String itemName = "";
			if(selectItem.equals("XQED")){ 
				map.put("XQED", "1");
				itemName = "贷款需求";
			}else if(selectItem.equals("SCYH_DESC")){
				map.put("SCYH_DESC", "1");
				itemName = "商城用户";
			}else if(selectItem.equals("SH_DESC")){
				map.put("SH_DESC", "1");
				itemName = "商户";
			}else if(selectItem.equals("NZYH_DESC")){
				map.put("NZYH_DESC", "1");
				map.put("TYPE", "BF");
				itemName = "农资用户";
			}else if(selectItem.equals("BANK_CD")){
				map.put("BANK_CD", "1");
				itemName = "借记卡";
			}else if(selectItem.equals("DJ_DESC")){
				map.put("DJ_DESC", "1");
				itemName = "贷记卡";
			}else if(selectItem.equals("CK_DESC")){
				map.put("CK_DESC", "1");
				itemName = "存款";
			}else if(selectItem.equals("LC_DESC")){ 
				map.put("LC_DESC", "1");
				itemName = "理财";
			}else if(selectItem.equals("DZ_DESC")){
				map.put("DZ_DESC", "1");
				itemName = "电子银行";
			}else if(selectItem.equals("POS_DESC")){
				map.put("POS_DESC", "1");
				itemName = "POS机";
			}else if(selectItem.equals("WH_DESC")){
				map.put("WH_DESC", "1");
				itemName = "外汇";
			}else if(selectItem.equals("DFGZ_DESC")){
				map.put("DFGZ_DESC", "1");
				itemName = "代发工资";
			}
			List<Map<String, Object>> resultList =custMgTreeService.filterQuery(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			if (null != page) {
				this.page.removeContext(); 
			}
			dicMap.put("CUST_TYPE", "CUST_TYPE");
			dicMap.put("JL_TYPE", "JL_TYPE");
			dicMap.put("CUST_GRP", "CUST_GRP1");
			// dicMap.put("CUST_GRP2", "CUST_GRP2");
			dicMap.put("AREA_CD", "AREA_CD1");
			dicMap.put("CREATE_TYPE", "ZTBJ");
			//字典项对应的数值
			DicReplace.replaceDic(resultList, dicMap);
			DicReplace.replaceChildDic(resultList, "CUST_GRP", "CUST_GRP2");
			DicReplace.replaceChildDic(resultList, "AREA_CD", "AREA_CD2");
			////当涉及农资需求时，查询出对应的需求详情信息
			List<List<Map<String, Object>>> resultNzxq = new ArrayList<List<Map<String,Object>>>();
			if (map.get("NZYH_DESC") != null && map.get("NZYH_DESC").equals("1")) {
				Map<String,Object> mapNzxq = new HashMap<String, Object>();
				for(int i = 0;i < resultList.size();i ++){
					String cust_id = (String) resultList.get(i).get("CUST_ID");
					String bf_date = (String) resultList.get(i).get("bf_date");
					List<Map<String, Object>> listResult = new ArrayList<Map<String,Object>>();
					mapNzxq.put("CUST_ID", cust_id);
					mapNzxq.put("CRT_DATE", bf_date);
					mapNzxq.put("DIC_PARENTID", "NZ_TYPE");
					listResult = custNzNeedService.query(mapNzxq);
					resultNzxq.add(listResult);
					mapNzxq.clear();
				}
				
			}
			//----------创建excel----------
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(); 
			// 创建样式-文本 (文本居中)
			XSSFCellStyle xssfCellStyle = xssfWorkbook.createCellStyle();
			xssfCellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
			xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			// 创建样式-标题 (文本居中, 背景为蓝颜色，有边框)
			XSSFCellStyle xssfCellStyleTitle = xssfWorkbook.createCellStyle();
			xssfCellStyleTitle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
			xssfCellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
			XSSFFont font = xssfWorkbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			xssfCellStyleTitle.setFont(font);
			Sheet sheet1 = xssfWorkbook.createSheet("Sheet1");
			sheet1.setColumnWidth(0, 7000);
			sheet1.setColumnWidth(1, 4000);
			sheet1.setColumnWidth(2, 4000);
			sheet1.setColumnWidth(3, 4000);
			sheet1.setColumnWidth(4, 4000);
			sheet1.setColumnWidth(5, 4000);
			Row row1 = xssfWorkbook.getSheetAt(0).createRow(0);
			//所属机构
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 0, 0));
			Cell cell11 = row1.createCell(0);
			cell11.setCellValue("所属机构");
			cell11.setCellStyle(xssfCellStyleTitle);
			//客户名称
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 1, 1));
			Cell cell12 = row1.createCell(1);
			cell12.setCellValue("客户名称");
			cell12.setCellStyle(xssfCellStyleTitle);
			//电话号码
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 2, 2));
			Cell cell13 = row1.createCell(2);
			cell13.setCellValue("电话号码");
			cell13.setCellStyle(xssfCellStyleTitle);
			//需求项目
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 3, 3));
			Cell cell14 = row1.createCell(3);
			cell14.setCellValue("需求项目");
			cell14.setCellStyle(xssfCellStyleTitle);
			//数量
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 4, 4));
			Cell cell15 = row1.createCell(4);
			cell15.setCellValue("数量");
			cell15.setCellStyle(xssfCellStyleTitle);
			//客户经理
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 5, 5));
			Cell cell16 = row1.createCell(5);
			cell16.setCellValue("客户经理");
			cell16.setCellStyle(xssfCellStyleTitle);
			for(int i = 0;i < resultList.size();i++){
				Map<String, Object> indexMap = resultList.get(i);
				Row row = xssfWorkbook.getSheetAt(0).createRow(1 + i);
				Cell indexCorp = row.createCell(0); // 所属机构
				Cell indexCustName = row.createCell(1); // 客户名称
				Cell indexTel = row.createCell(2); // 电话号码
				Cell indexXqItem = row.createCell(3); // 需求项目
				Cell indexCount = row.createCell(4); //  数量
				Cell indexUserName = row.createCell(5); // 客户经理
				String xqCount = "";
				if(selectItem.equals("XQED")){
					xqCount = indexMap.get("XQED")+"";
					cell15.setCellValue("数量(万元)");
				}else if(selectItem.equals("NZYH_DESC")){
					sheet1.setColumnWidth(4, 8000);
					//获取农资需求详情 
					List<Map<String, Object>> nzxqResult = resultNzxq.get(i);
					if(nzxqResult.isEmpty()){
						xqCount = "暂无";
					}else{
						int size = nzxqResult.size();
						for(int j = 0;j < size;j ++){
							//xqCount += 
							String dic_name = (String) nzxqResult.get(j).get("dic_name");
							String NUM = (String) nzxqResult.get(j).get("NUM");
							String MONTH = (String) nzxqResult.get(j).get("MONTH");
							xqCount += dic_name +":"+NUM+"吨/瓶"+",需求月份:"+MONTH+"\r\n";
						}
					}
				}else{
					xqCount = "暂无";
				}
				indexCorp.setCellValue((String)indexMap.get("ORG_ABB"));
				indexCustName.setCellValue((String)indexMap.get("CUST_NAME"));
				indexTel.setCellValue((String)indexMap.get("TEL_NO"));
				indexXqItem.setCellValue(itemName);
				indexCount.setCellValue(xqCount);
				indexUserName.setCellValue((String)indexMap.get("CUST_GRP_JLC"));
				indexCorp.setCellStyle(xssfCellStyle);
				indexCustName.setCellStyle(xssfCellStyle);
				indexTel.setCellStyle(xssfCellStyle);
				indexXqItem.setCellStyle(xssfCellStyle);
				indexCount.setCellStyle(xssfCellStyle);
				indexUserName.setCellStyle(xssfCellStyle);
			}
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			xssfWorkbook.write(os);

			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);

			response.reset();
			response.setContentType("application/x-download;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + URLEncoder.encode("客户需求", "UTF-8") + ".xlsx");

			ServletOutputStream out = response.getOutputStream();

			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead;

			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}

			if (bis != null) {
				bis.close();
			} 
			if (bos != null) {
				bos.close();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.setErr(page, e.getLocalizedMessage());
		}
		return null;
	}
	@ResponseBody  
	@RequestMapping(value = "/exportZz", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@SystemControllerLog(description = "客户种植信息导出")
	public String exportZz(HttpServletRequest request, HttpServletResponse response, String exportParams_zz){
		log.info("种植筛选导出");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			List<Map<String, Object>> userRole = lrdRoleService.queryUserRole(map);//获取用户权限
			List<Map<String,Object>> roleList = user.getROLE_CD();//权限列表
			String fw="";
			
			for (Map<String, Object> map2 : roleList) {
				fw=map2.get("ROLE_FW")+"";
			}
			
			
			//寻找最大范围
			if (fw.contentEquals("01")) {//可查看所有---不对数据进行处理
				
				
			}else if (fw.contentEquals("02")) {//可查看本级及下级
				
				map.put("ZH_ID",user.getOrgCD());
				
			}else {//查询本级的
				map.put("ZH_ID",user.getOrgCD());
				map.put("JL_ID",user.getUser_id());
				
			}
			String selectItem = exportParams_zz;
			map.put("ZW_TYPE", selectItem);
			List<Map<String, Object>> resultList = custZzService.filterQuery(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			if (null != page) {
				this.page.removeContext();
			}
			dicMap.put("ZZ_MODEL", "ZZ_MODEL");
			dicMap.put("ZW_TYPE", "ZW_TYPE");
			DicReplace.replaceDic(resultList, dicMap);
			
			//------新建excel--------
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(); 
			// 创建样式-文本 (文本居中)
			XSSFCellStyle xssfCellStyle = xssfWorkbook.createCellStyle();
			xssfCellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
			xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			// 创建样式-标题 (文本居中, 背景为蓝颜色，有边框)
			XSSFCellStyle xssfCellStyleTitle = xssfWorkbook.createCellStyle();
			xssfCellStyleTitle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
			xssfCellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
			XSSFFont font = xssfWorkbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			xssfCellStyleTitle.setFont(font);
			Sheet sheet1 = xssfWorkbook.createSheet("Sheet1");
			sheet1.setColumnWidth(0, 7000);
			sheet1.setColumnWidth(1, 4000);
			sheet1.setColumnWidth(2, 4000);
			sheet1.setColumnWidth(3, 4000);
			sheet1.setColumnWidth(4, 4000);
			sheet1.setColumnWidth(5, 4000);
			sheet1.setColumnWidth(6, 4000);
			sheet1.setColumnWidth(7, 4000); 
			sheet1.setColumnWidth(8, 4000);
			Row row1 = xssfWorkbook.getSheetAt(0).createRow(0);
			//所属机构
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 0, 0));
			Cell cell11 = row1.createCell(0);
			cell11.setCellValue("所属机构");
			cell11.setCellStyle(xssfCellStyleTitle);
			//客户名称
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 1, 1));
			Cell cell12 = row1.createCell(1);
			cell12.setCellValue("客户名称");
			cell12.setCellStyle(xssfCellStyleTitle);
			//电话号码
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 2, 2));
			Cell cell13 = row1.createCell(2);
			cell13.setCellValue("电话号码");
			cell13.setCellStyle(xssfCellStyleTitle);
			//作物种类
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 3, 3));
			Cell cell14 = row1.createCell(3);
			cell14.setCellValue("作物种类");
			cell14.setCellStyle(xssfCellStyleTitle);
			//作物亩数
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 4, 4));
			Cell cell15 = row1.createCell(4);
			cell15.setCellValue("作物亩数");
			cell15.setCellStyle(xssfCellStyleTitle);
			//种植模式
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 5, 5));
			Cell cell16 = row1.createCell(5);
			cell16.setCellValue("种植模式");
			cell16.setCellStyle(xssfCellStyleTitle);
			//亩产（斤）
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 6, 6));
			Cell cell17 = row1.createCell(6);
			cell17.setCellValue("亩产（斤）");
			cell17.setCellStyle(xssfCellStyleTitle);
			//下架时间
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 7, 7));
			Cell cell18 = row1.createCell(7);
			cell18.setCellValue("下架时间");
			cell18.setCellStyle(xssfCellStyleTitle);
			//年收入（万元）
			xssfWorkbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 8, 8));
			Cell cell19 = row1.createCell(8);
			cell19.setCellValue("年收入（万元）");
			cell19.setCellStyle(xssfCellStyleTitle);
			
			for(int i = 0;i < resultList.size();i ++){
				Map<String, Object> indexMap = resultList.get(i);
				Row row = xssfWorkbook.getSheetAt(0).createRow(1 + i);
				Cell indexCorp = row.createCell(0); // 所属机构
				Cell indexCustName = row.createCell(1); // 客户名称
				Cell indexTel = row.createCell(2); // 电话号码
				Cell indexCropType = row.createCell(3); // 作物种类
				Cell indexCropNum = row.createCell(4); //  作物亩数
				Cell indexCropMod = row.createCell(5); // 种植模式
				Cell indexCropOut = row.createCell(6); // 亩产（斤）
				Cell indexGotTime = row.createCell(7); //下架时间
				Cell indexIncome = row.createCell(8); //年收入（万元）
				indexCorp.setCellValue((String)indexMap.get("ORG_ABB"));
				indexCustName.setCellValue((String)indexMap.get("CUST_NAME"));
				indexTel.setCellValue((String)indexMap.get("TEL_NO"));
				indexCropType.setCellValue((String)indexMap.get("ZW_TYPEC"));
				indexCropNum.setCellValue((BigDecimal)indexMap.get("ZZ_MS")+"");
				indexCropMod.setCellValue((String)indexMap.get("ZZ_MODELC"));
				indexCropOut.setCellValue((BigDecimal)indexMap.get("MC") +"");
				indexGotTime.setCellValue((String)indexMap.get("XJ_SJ"));
				indexIncome.setCellValue((BigDecimal)indexMap.get("INCOME_Y") +"");
				indexCorp.setCellStyle(xssfCellStyle);
				indexCustName.setCellStyle(xssfCellStyle);
				indexTel.setCellStyle(xssfCellStyle);
				indexCropType.setCellStyle(xssfCellStyle);
				indexCropNum.setCellStyle(xssfCellStyle);
				indexCropMod.setCellStyle(xssfCellStyle);
				indexCropOut.setCellStyle(xssfCellStyle);
				indexGotTime.setCellStyle(xssfCellStyle);
				indexIncome.setCellStyle(xssfCellStyle);
			}
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			xssfWorkbook.write(os);

			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);

			response.reset();
			response.setContentType("application/x-download;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + URLEncoder.encode("种植作物", "UTF-8") + ".xlsx");

			ServletOutputStream out = response.getOutputStream();

			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead;

			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}

			if (bis != null) {
				bis.close();
			} 
			if (bos != null) {
				bos.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.setErr(page, e.getLocalizedMessage());
		}
		return null;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> castMap(Map<String,Object> properties){
		 
	    // 返回值Map  
	    Map returnMap = new HashMap();  
	    Iterator entries = properties.entrySet().iterator();  
	    Map.Entry entry;  
	    String name = "";  
	    String value = "";  
	    while (entries.hasNext()) {  
	        entry = (Map.Entry) entries.next();  
	        name = (String) entry.getKey();  
	        Object valueObj = entry.getValue();  
	        if(null == valueObj){  
	            value = "";  
	        }else if(valueObj instanceof String[]){  
	            String[] values = (String[])valueObj;  
	            for(int i=0;i<values.length;i++){  
	                value = values[i] + ",";  
	            }  
	            value = value.substring(0, value.length()-1);  
	        }else{  
	            value = valueObj.toString();  
	        }  
	        returnMap.put(name, value);  
	    }  
		return returnMap;
	}

	public String getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		// System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		return df.format(new Date());
	}

	public double div(double d1,double d2,int scale){  
        if(scale<0){  
            throw new IllegalArgumentException("The scale must be a positive integer or zero");  
        }  
        BigDecimal b1=new BigDecimal(Double.toString(d1));  
        BigDecimal b2=new BigDecimal(Double.toString(d2));  
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();  
          
    }  
}
