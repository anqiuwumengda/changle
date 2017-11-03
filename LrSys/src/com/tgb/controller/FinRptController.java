package com.tgb.controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.cust.CustBaseService;
import com.tgb.service.cust.FinRptService;
import com.tgb.util.DateTools;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/finrpt")
public class FinRptController extends BaseController {
	Logger log=Logger.getLogger(FinRptController.class); 
	@Autowired
	private FinRptService finRptService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private CustBaseService custBaseService;
	
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户-财务报表信息")
	public String query(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			User user = this.getSessionUser(request);
			//map.put("cust_grp_jl", user.getId());
			isPage(map);
			List<Map<String, Object>> resultList = finRptService.query(map);
			for (Map<String, Object> map2 : resultList) {
				String XJJYHCK=map2.get("XJJYHCK")+"";
				if (XJJYHCK==null||XJJYHCK=="") {
				String YSZK = map2.get("YSZK")+"";
				String YFCX = map2.get("YFCX")+"";
				String CK = map2.get("CK")+"";
				String GDZC = map2.get("GDZC")+"";
				String QTJYZC = map2.get("QTJYZC")+"";
				String QTFJYZC = map2.get("QTFJYZC")+"";
				String ZFZK = map2.get("ZFZK")+"";
				String YSKX = map2.get("YSKX")+"";
				String DQDK = map2.get("DQDK")+"";
				String CQDK = map2.get("CQDK")+"";
				String QTFZ = map2.get("QTFZ")+"";
				
				double xjjhck=0;
				try {
					xjjhck = Double.valueOf(XJJYHCK);
				} catch (Exception e) {}
				double yszk=0;
				try {
					yszk = Double.valueOf(YSZK);
				} catch (Exception e) {}
				
				double yfcx=0;
				try {
					yfcx = Double.valueOf(YFCX);
				} catch (Exception e) {}
				
				double ck=0;
				try {
					ck = Double.valueOf(CK);
				} catch (Exception e) {}
				double gdzc=0;
				try {
					gdzc = Double.valueOf(GDZC);
				} catch (Exception e) {}
				double qtjyzc=0;
				try {
					qtjyzc = Double.valueOf(QTJYZC);
				} catch (Exception e) {}
				double qtfjyzc=0;
				try {
					qtfjyzc = Double.valueOf(QTFJYZC);
				} catch (Exception e) {}
				double zfzk=0;
				try {
					zfzk = Double.valueOf(ZFZK);
				} catch (Exception e) {}
				double yskx=0;
				try {
					yskx = Double.valueOf(YSKX);
				} catch (Exception e) {}
				double dqdk=0;
				try {
					dqdk = Double.valueOf(DQDK);
				} catch (Exception e) {}
				double cqdk=0;
				try {
					cqdk = Double.valueOf(CQDK);
				} catch (Exception e) {}
				double qtfz=0;
				try {
					qtfz = Double.valueOf(QTFZ);
				} catch (Exception e) {}
				
				
				//流动资产
				double LDZC=xjjhck+yszk+yfcx+ck;
				//短期负债=短期借款+应付账款+预付款项
				double DQFZ=zfzk+yskx+dqdk;
				//总资产=流动资产+固定资产+其他资产
				double ZZC=LDZC+gdzc+qtjyzc;
				//总负债
				double ZFZ=DQFZ+cqdk+qtfz;
				//权益
				double QY=ZZC-ZFZ;
				//资产负债率=总负债/总资产*100%（小数点后两位）
				double ZCFZL=(ZZC==0.0)?0.0:((ZFZ/ZZC)*100);
				//速动比率=(流动资产-存款)/短期负债*100%小数点后两位
				double SDBL=(DQFZ==0.0)?0.0:(((LDZC-ck)/DQFZ)*100);
				map2.put("LDZC", LDZC+"");
				map2.put("ZZC", ZZC+"");
				map2.put("DQFZ", DQFZ+"");
				map2.put("ZFZ", ZFZ+"");
				map2.put("QY", QY+"");
				map2.put("SDBL", SDBL+"");
				map2.put("ZCFZL", ZCFZL+"");
				}
				
			}
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryDistinct", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户-财务报表信息")
	public String queryDistinct(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			User user = this.getSessionUser(request);
			//map.put("cust_grp_jl", user.getId());
			isPage(map);
			List<Map<String, Object>> resultList = finRptService.queryDistinct(map);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "维护客户-财务报表信息")
	public String save(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			//User user = this.getSessionUser(request);
			//财务报表计算
			String XJJYHCK=map.get("XJJYHCK")+"";
			String YSZK = map.get("YSZK")+"";
			String YFCX = map.get("YFCX")+"";
			String CK = map.get("CK")+"";
			String GDZC = map.get("GDZC")+"";
			String QTJYZC = map.get("QTJYZC")+"";
			String QTFJYZC = map.get("QTFJYZC")+"";
			String ZFZK = map.get("ZFZK")+"";
			String YSKX = map.get("YSKX")+"";
			String DQDK = map.get("DQDK")+"";
			String CQDK = map.get("CQDK")+"";
			String QTFZ = map.get("QTFZ")+"";
			
			double xjjhck=0.0;
			try {
				xjjhck = Double.valueOf(XJJYHCK);
			} catch (Exception e) {}
			double yszk=0.0;
			try {
				yszk = Double.valueOf(YSZK);
			} catch (Exception e) {}
			
			double yfcx=0.0;
			try {
				yfcx = Double.valueOf(YFCX);
			} catch (Exception e) {}
			
			double ck=0.0;
			try {
				ck = Double.valueOf(CK);
			} catch (Exception e) {}
			double gdzc=0.0;
			try {
				gdzc = Double.valueOf(GDZC);
			} catch (Exception e) {}
			double qtjyzc=0.0;
			try {
				qtjyzc = Double.valueOf(QTJYZC);
			} catch (Exception e) {}
			double qtfjyzc=0.0;
			try {
				qtfjyzc = Double.valueOf(QTFJYZC);
			} catch (Exception e) {}
			double zfzk=0.0;
			try {
				zfzk = Double.valueOf(ZFZK);
			} catch (Exception e) {}
			double yskx=0.0;
			try {
				yskx = Double.valueOf(YSKX);
			} catch (Exception e) {}
			double dqdk=0.0;
			try {
				dqdk = Double.valueOf(DQDK);
			} catch (Exception e) {}
			double cqdk=0.0;
			try {
				cqdk = Double.valueOf(CQDK);
			} catch (Exception e) {}
			double qtfz=0.0;
			try {
				qtfz = Double.valueOf(QTFZ);
			} catch (Exception e) {}
			
			
			//流动资产
			double LDZC=xjjhck+yszk+yfcx+ck;
			//短期负债=短期借款+应付账款+预付款项
			double DQFZ=zfzk+yskx+dqdk;
			//总资产=流动资产+固定资产+其他资产
			double ZZC=LDZC+gdzc+qtjyzc;
			//总负债
			double ZFZ=DQFZ+cqdk+qtfz;
			//权益
			double QY=ZZC-ZFZ;
			//资产负债率=总负债/总资产*100%（小数点后两位）
			double ZCFZL=(ZZC==0.0)?0.0:((ZFZ/ZZC)*100);
			//速动比率=(流动资产-存款)/短期负债*100%小数点后两位
			double SDBL=(DQFZ==0.0)?0.0:(((LDZC-ck)/DQFZ)*100);
			
			DecimalFormat   df=new  DecimalFormat("#.##");
			map.put("LDZC", LDZC+"");
			map.put("ZZC", ZZC+"");
			map.put("DQFZ", DQFZ+"");
			map.put("ZFZ", ZFZ+"");
			map.put("QY", QY+"");
			map.put("SDBL", (df.format(SDBL)+""));
			map.put("ZCFZL", (df.format(ZCFZL)+""));
			
			
			List<Map<String, Object>> resultList= finRptService.query(map);
			if(null!=resultList && resultList.size()>0){
				map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				finRptService.update(map);
				map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custBaseService.updCustBaseMtnData(map);
			}else{
				map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				finRptService.save(map);
				map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custBaseService.updCustBaseMtnData(map);
			}
			txManager.commit(status);
			this.setSuccess(null, page);

		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "修改客户-财务报表信息")
	public String update(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			//User user = this.getSessionUser(request);
			finRptService.update(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除客户-财务报表信息")
	public String deleteCustZg(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			//User user = this.getSessionUser(request);
			finRptService.deleteByPk(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
}
